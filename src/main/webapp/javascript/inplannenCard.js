document.getElementById("volgendeInplannen").addEventListener("click", function() {
	volgendeKnopInplannen();
	date = new Date();
})

function volgendeKnopInplannen(){ 
	// geslacht en behandelingen zijn ingevuld
	if (status == "behandeling"){
		if(behandelingenLijst.length == 0){
			alert("selecteer eerst een behandeling");
		} else{
			// het geslacht en behandeling is ingevuld
			status = "datum";
			document.getElementById("inplannenDatum").style.display = "block";
			document.getElementById("inplannenGeslacht").style.display = "none";
			document.getElementById("behandelingenKeuzeLijst").style.display = "none";
			
			datumOphalen();
		}
	} else if(status == "datum"){
		document.getElementById("inplannenDatum").style.display = "none";
		document.getElementById("inplannenForm").style.width = "800px";
		document.getElementById("klantInfo").style.display = "block";
		
		var tijd = document.getElementById("urenInplannen").innerHTML + ":" + 
		document.getElementById("minutenInplannen").value;
		document.getElementById("tijdInplanForm").innerHTML = tijd;

	} 
}

document.getElementById("terugInplannen").addEventListener("click", function() {
	terugKnopInplannen();
})

function terugKnopInplannen(){
	if(status == "behandeling"){
		document.getElementById("inplannenModal").style.display = "none";
	} else if(status == "datum"){
		// het geslacht en behandeling is ingevuld
		status = "behandeling"
		
		document.getElementById("inplannenDatum").style.display = "none";
		document.getElementById("inplannenGeslacht").style.display = "block";
		document.getElementById("behandelingenKeuzeLijst").style.display = "block";

	} 
}

// de eerste functie die word aangeroepen om eet geslacht te kiezen
function inplannenGeslachtKiezen(){
	// wordt gebruikt bij het navigeren van de volgende en terug knop
	afspraakKlantGeslacht = '';
	behandelingenLijst = [];
	afspraakDatum = '';
	
	prijs = 0;
	uren = 0;
	minuten = 0;
	status = "behandeling"
	// geslacht word gekozen
	document.getElementById("geslachtMan").addEventListener("click", function() {
		document.getElementById("geslachtMan").style.background = "blue";
		document.getElementById("geslachtVrouw").style.background = "white";
		document.getElementById("geslachtJongen").style.background = "white";
		document.getElementById("geslachtMeisje").style.background = "white";

		behandelingenOphalen("man");
	})
	document.getElementById("geslachtVrouw").addEventListener("click", function() {
		document.getElementById("geslachtVrouw").style.background = "blue";
		document.getElementById("geslachtMan").style.background = "white";
		document.getElementById("geslachtJongen").style.background = "white";
		document.getElementById("geslachtMeisje").style.background = "white";
		behandelingenOphalen("vrouw");
	})
	document.getElementById("geslachtJongen").addEventListener("click", function() {
		document.getElementById("geslachtJongen").style.background = "blue";
		document.getElementById("geslachtVrouw").style.background = "white";
		document.getElementById("geslachtMan").style.background = "white";
		document.getElementById("geslachtMeisje").style.background = "white";
		
		behandelingenOphalen("jongen");
	})
	document.getElementById("geslachtMeisje").addEventListener("click", function() {
		document.getElementById("geslachtMeisje").style.background = "blue"
		document.getElementById("geslachtVrouw").style.background = "white";
		document.getElementById("geslachtJongen").style.background = "white";
		document.getElementById("geslachtMan").style.background = "white";
		behandelingenOphalen("meisje");
	})
}

document.getElementById("optiesBehandeling").addEventListener("click", function() {
	var opties = document.getElementById("behandelingenOpties");
	if(opties.style.display == "block"){
		opties.style.display="none";
	} else{
		opties.style.display="block";
	}
})

// de behandelingen voor het gekozen geslacht worden opgehaalt
function behandelingenOphalen(geslacht){	
	document.getElementById("inplanFormGeslacht").innerHTML = geslacht;

	inplanVoorbereiding("geslacht", geslacht);
	document.getElementById("behandelingenKeuzeLijst").innerHTML = "";
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/plan/behandelingen/" + geslacht, fetchoptions)
	.then(response => response.json())
	.then(function(behandelingen){
		for (let behandeling of behandelingen){
			var behandelingsDiv = document.createElement('div');
			behandelingsDiv.setAttribute('id', "d"+behandeling.id);
			behandelingsDiv.setAttribute('class', 'behandelingKeuze');
			
			var behandelingsNaam = document.createElement('div');
			var behandelingsBeschrijving = document.createElement('div');
			var behandelingsLengte = document.createElement('div');
			var behandelingsPrijs = document.createElement('div');
				
			behandelingsNaam.innerHTML = behandeling.naam;
			behandelingsBeschrijving.innerHTML = behandeling.beschrijving;
			behandelingsLengte.innerHTML = behandeling.lengte;
			behandelingsPrijs.innerHTML = behandeling.prijs;
				
			behandelingsNaam.setAttribute('class', 'naamBehandelingKeuze');
			behandelingsBeschrijving.setAttribute('class', 'beschrijvingBehandelingKeuze');
			behandelingsLengte.setAttribute('class', 'lengteBehandelingKeuze');
			behandelingsPrijs.setAttribute('class', 'prijsBehandelingKeuze');
			
			behandelingsDiv.appendChild(behandelingsNaam);
			behandelingsDiv.appendChild(behandelingsBeschrijving);
			behandelingsDiv.appendChild(behandelingsLengte);
			behandelingsDiv.appendChild(behandelingsPrijs);
			// behandeling is gekozen
			behandelingsDiv.addEventListener("click", function() {
				hideItem("d"+behandeling.id);
				// totaal blokken worden geupdate
				inplanVoorbereiding("lengteToevoegen", behandeling.lengte);
				inplanVoorbereiding("prijsToevoegen", behandeling.prijs);
				document.getElementById("prijsAfspraakForm").innerHTML = "€"+ prijs;
				document.getElementById("lengteAfspraakForm").innerHTML = uren +":"+ minuten;

				inplanVoorbereiding("behandelingToevoegen", behandeling.id);
				// blok in de lijst wordt aangemaakt
				var gekozenBehandelingsSpan = document.createElement('span');
				gekozenBehandelingsSpan.setAttribute('id', behandeling.id);
				gekozenBehandelingsSpan.setAttribute('class', 'inplannenBehandelingOverzicht');
				
				var textGekozenBehandeling = document.createElement('a');
				textGekozenBehandeling.style.cssFloat ="left";
				textGekozenBehandeling.innerHTML = behandeling.naam;
				gekozenBehandelingsSpan.appendChild(textGekozenBehandeling);
				
				var prijsGekozenBehandeling = document.createElement('a');
				prijsGekozenBehandeling.style.cssFloat ="right";
				prijsGekozenBehandeling.innerHTML = " €"+behandeling.prijs;
				gekozenBehandelingsSpan.appendChild(prijsGekozenBehandeling);

				// behandeling verwijderen uit gekozen lijst
				gekozenBehandelingsSpan.addEventListener("click", function(){
					removeItem(behandeling.id);
					showItem("d"+behandeling.id);
					
					// totaal blokken worden geupdate
					inplanVoorbereiding("lengteVerwijderen", behandeling.lengte);
					inplanVoorbereiding("prijsVerwijderen", behandeling.prijs);
					document.getElementById("prijsAfspraakForm").innerHTML = "€"+ prijs;
					document.getElementById("lengteAfspraakForm").innerHTML = uren +":"+ minuten;
				})
				// behandeling toevoegen aan inplanForm
				document.getElementById("inplanBehandelingenForm").appendChild(gekozenBehandelingsSpan);

			})
			// voeg toe aan keuzelijst
			document.getElementById("behandelingenKeuzeLijst").appendChild(behandelingsDiv);
		}			
	}).catch(function() {
		// De gebruiker is niet ingelogt
	});
}

document.getElementById("weekVerderInplannen").addEventListener("click", function(){
	var weekMiliSeconden = 7 * 24 * 60 * 60 * 1000;
	date.setTime(date.getTime() + weekMiliSeconden);
	document.getElementById("dagenLijst").innerHTML = "";
	datumOphalen();
})

document.getElementById("weekTerugInplannen").addEventListener("click", function(){
	var weekMiliSeconden = 7 * 24 * 60 * 60 * 1000;
	date.setTime(date.getTime() - weekMiliSeconden);
	document.getElementById("dagenLijst").innerHTML = "";
	datumOphalen();
})

// de datum en tijden worden opgehaalt
function datumOphalen(){ 	  
	var weekMiliSeconden = 7 * 24 * 60 * 60 * 1000;
	var dagMiliSeconden = 24 * 60 * 60 * 1000;
	
	var werkdagenLijst = [];
	var dagNamen = [
		"Maandag", "Dinsdag",
		"Woensdag", "Donderdag", 
		"Vrijdag", "Zaterdag", 
		"Zondag"
	];
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/plan/werkdagen", fetchoptions)
	.then(response => response.json())
	.then(function(dagen){
		var inplanDatum = document.getElementById("dagenLijst");
		for(let dag of dagen){
			var dagNummer = dag.dagNummmer;
			werkdagenLijst.push(dagNummer);

			var dagSpan = document.createElement('span');
			dagSpan.setAttribute('class', 'dagKiezen');
			dagSpan.setAttribute('id', "dagKeuze"+dagNummer);

			var dagNaam = document.createElement('a');
			dagNaam.innerHTML = dagNamen[dagNummer] +"<br>";
			dagSpan.appendChild(dagNaam);
			
			var dagDatum = document.createElement('a');
			var datum = new Date();
			datum.setTime(getMaandag(date).getTime() + dagMiliSeconden*dagNummer);

			var formattedDate = formatDate(datum);

			dagDatum.innerHTML = formattedDate;
			dagDatum.setAttribute('id', 'date'+dagNamen[dagNummer]);
			dagSpan.appendChild(dagDatum);
		
			inplanDatum.appendChild(dagSpan);
			
			// op elke dag kan geklikt worden
			dagOphalen(dagNummer, datum);
		}
	})
}

function dagOphalen(dagNummer, datum){
	document.getElementById("dagKeuze"+dagNummer).addEventListener("click", function(){
		// aanmaken potentiele afspraak div
		var plan = document.getElementById("potentieleAfspraak");
		plan.innerHTML = "lengte:  "+document.getElementById("lengteAfspraakForm").innerHTML+"<br><br> Datum: "+document.getElementById("datumInplanForm").innerHTML;
		console.log(uren +" "+ minuten);
		var uur = uren * 60;
		var minuut = uur + minuten;
		console.log("min " +  minuut);
		var hoogte = minuut * 2;
		console.log("hoogte "+ hoogte);

		plan.style.height = hoogte+"px";

		createDraggable();

		document.getElementById("inplannenDatum").style.display = "none";
		document.getElementById("inplannenTijd").style.display = "block";
		var i = 0;
		var date = datum.getFullYear() +'-'+ datum.getMonth() +'-'+ datum.getDate();
		
		document.getElementById("datumInplanForm").innerHTML = date;
		
		var fetchoptions = {
				headers: {
					'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
				}
			}
		fetch("restservices/plan/tijdslotenOphalen/"+date, fetchoptions)
		.then(response => response.json())
		.then(function(afspraken){
			for(let afspraak of afspraken){
				if(i == 0){
					// timeline wordt eerst aangemaakt
					console.log(afspraak.openingsTijd);
					
					var beginTijd= afspraak.openingsTijd.split(":");
					beginUur = parseInt(beginTijd[0]);
					beginMinuut = parseInt(beginTijd[1]);
					
					beginUurGlobal = beginUur;
					beginMinuutGlobal = beginMinuut;
					
					var eindTijd = afspraak.sluitingstijd.split(":");
					eindUur = parseInt(eindTijd[0]);
					eindMinuut = parseInt(eindTijd[1]);
		
					var ul = document.createElement('ul');
					var timeLi = document.createElement('li');
					timeLi.setAttribute('tijd', beginUur +":"+ beginMinuut);

					ul.appendChild(timeLi);
					timeLi.setAttribute('class', 'dropzone line');
					timeLi.innerHTML = beginUur +":"+ beginMinuut;
					
					while(true){
						beginMinuut = beginMinuut + 10;
						var timeLi = document.createElement('li');
						timeLi.setAttribute('tijd', beginUur +":"+ beginMinuut);
						ul.appendChild(timeLi);
						
						if(beginMinuut == 30){
							timeLi.setAttribute('class', 'dropzone line');
							timeLi.innerHTML = beginUur +":"+ beginMinuut;
						} else if(beginMinuut > 59){
							beginUur = beginUur + 1;
							beginMinuut = 0;
							timeLi.setAttribute('class', 'dropzone line');
							timeLi.innerHTML = beginUur +":"+ beginMinuut;
						} else{
							timeLi.setAttribute('class', 'dropzone');
						}
		
						if(beginUur >= eindUur && beginMinuut >= eindMinuut){
							break;
						}
					}
					document.getElementById("timelineInplanAgenda").appendChild(ul);
					i++;
				} else{
					// gemaakte afsprraken worden in de timeline gezet
					var event = createAfspraak(afspraak.beginTijd, afspraak.lengte);
					
					document.getElementById("roosterInplanAgenda").appendChild(event);
				}
			}			
		});
	});
}

// het vullen van de afspraken lijst bij de tijd instellen
function createAfspraak(beginTijd ,lengte){ 
	// event aanmaken
	var event = document.createElement('li');
	event.setAttribute('class', 'nietBeschikbaarAfspraak');

	event.style.backgroundColor= "#34D77B";

	event.innerHTML = beginTijd +" <br> "+ lengte;

	// begin punt van de afspraak
	var topArray = beginTijd.split(":");
	var uurTop = parseInt(topArray[0]);
	var minuutTop = parseInt(topArray[1]);

	var minuutVerschilTop = minuutTop + uurTop*60;
	var minuutBegin = beginMinuutGlobal + beginUurGlobal*60;
	
	var topMinuten = minuutVerschilTop-minuutBegin;
	var top = topMinuten * 2 + 20;
	event.style.top = top+"px";
	// lengte van afspraak
	var lengteArray = lengte.split(":");
	var uurLengte = parseInt(lengteArray[0]);
	var minuutLengte = parseInt(lengteArray[1]);
	var minuutVerschilLengte = minuutLengte + uurLengte*60;

	var hoogte = minuutVerschilLengte * 2;
	console.log(hoogte);
	event.style.height = hoogte+"px";

	return event;
}

// de afspraak die je naar een tijd kan slepen
function createDraggable(){   
	var element = document.getElementById('potentieleAfspraak')
	var y = 0
	// de afspraak verplaatsbaar maken
	interact(element)
	  .draggable({
	    modifiers: [
	      interact.modifiers.snap({
	        targets: [
	          interact.createSnapGrid({ x: 0, y: 20 })
	        ],
	        range: Infinity,
	        relativePoints: [ { x: 0, y: 0 } ]
	      }),
	      interact.modifiers.restrict({
	        restriction: element.parentNode,
	        elementRect: { top: 0, left: 0, bottom: 1, right: 1 },
	        endOnly: true
	      })
	    ],
	    inertia: true
	  })
	  .on('dragmove', function (event) {
	    y += event.dy

	    event.target.style.webkitTransform =
	    event.target.style.transform =
	        'translate(0px, ' + y + 'px)'
	  }),
	  
	  // de dropzone maken
	  interact('.dropzone').dropzone({
	  	overlap: 0.01,

	    	ondropactivate: function (event) {
	    		event.target.classList.add('drop-active')
	    	},
	    	ondragenter: function (event) {
	    		var draggableElement = event.relatedTarget
	    		var dropzoneElement = event.target
	    		// feedback the possibility of a drop
	    		dropzoneElement.classList.add('drop-target')
	      		console.log(dropzoneElement.innerHTML);
	    	},
	    	ondragleave: function (event) {
	    		var draggableElement = event.relatedTarget
	    		var dropzoneElement = event.target
	    		// remove the drop feedback style
	    		dropzoneElement.classList.remove('drop-target')
	    	},
	    	ondrop: function (event) {
	  	  		var draggableElement = event.relatedTarget
	  	  		var tijd= document.getElementsByClassName("drop-target")[0].getAttribute("tijd").split(":");
	  	  		var potentieelUur = parseInt(tijd[0]) - uren;
	  	  		var potentieelMinuut = parseInt(tijd[1] - minuten);
	  	  		
	  	  		if(potentieelMinuut < 0){
	  	  			potentieelMinuut = potentieelMinuut + 60;
	  	  			potentieelUur = potentieelUur - 1;
	  	  		}
	  	  		
	  	  		document.getElementById("urenInplannen").innerHTML = potentieelUur;
	  	  		document.getElementById("minutenInplannen").value = potentieelMinuut;
	    	},
	      ondropdeactivate: function (event) {
	          // remove active dropzone feedback
	          event.target.classList.remove('drop-active')
	          event.target.classList.remove('drop-target')
	        }
	  })
}

function toggle(className, displayState){
    var elements = document.getElementsByClassName(className)

    for (var i = 0; i < elements.length; i++){
        elements[i].style.display = displayState;
    }
}

function inplanVoorbereiding(state, value){
	if(state == "geslacht"){
		afspraakKlantGeslacht = value;
	} else if(state == "behandelingToevoegen"){
		behandelingenLijst.push(value);
	} else if(state == "lengteToevoegen"){
		var nieuw=value.split(":");
		uren=uren + parseInt(nieuw[0]);
		minuten = minuten + parseInt(nieuw[1]);
		 if(minuten > 59){
			 uren = uren +1;
			 minuten = minuten - 60;
		 }
	} else if(state == "lengteVerwijderen"){
		var nieuw=value.split(":");
		uren=uren - parseInt(nieuw[0]);
		minuten = minuten - parseInt(nieuw[1]);
		 if(minuten < 0){
			 uren = uren - 1;
			 minuten = minuten + 60;
		 }
	} else if(state == "prijsToevoegen"){
		prijs = prijs + value;
	} else if(state == "prijsVerwijderen"){
		prijs = prijs - value;
	} else if(state == "datum"){
		afspraakDatum=value;
	
	} else if(state = "inplannen"){
		console.log("inplannen");
		inplannen(afspraakKlantGeslacht, behandelingenLijst);
	}
}

document.getElementById("submitInplanFormButton").addEventListener("click", function(){
	inplanVoorbereiding("inplannen","");
})

function inplannen(geslacht, behandelingenlijst){ 
	var formData = new FormData(document.getElementById("inplannenForm"));
	console.log(geslacht);
	console.log(JSON.stringify(behandelingenlijst));
	console.log(document.getElementById("datumInplanForm").innerHTML);
	console.log(document.getElementById("tijdInplanForm").innerHTML);

	formData.append("klantGeslacht", geslacht);
	formData.append("afspraakBehandeling", JSON.stringify(behandelingenlijst));
	formData.append("afspraakDatum", document.getElementById("datumInplanForm").innerHTML);
	formData.append("afspraakTijd", document.getElementById("tijdInplanForm").innerHTML);
	
	var encData = new URLSearchParams(formData.entries());
	alert(encData);
	var fetchoptions = {
			method: 'POST',
			body: encData,
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	// De wijziging wordt verstuurt naar de backend
	fetch("restservices/plan/afspraak", fetchoptions)
	.then(function (response){
		if(response.ok){
			alert("ingepland");
		} else{
			alert("fout");
		}
	})
}

// hulphulphulphulphulphulphulphulphulphulphulphulphulphulphulphulp
function hideDagen(id){
	var dagen = [
		"dateSpanMaandag", "dateSpanDinsdag",
		"dateSpanWoensdag", "dateSpanDonderdag", 
		"dateSpanVrijdag", "dateSpanZaterdag", 
		"dateSpanZondag"
	];
	for (i = 0; i < dagen.length; i++) {
		var dag = dagen[i];
		if(dag != id){
			document.getElementById(dag).style.display = "none";
		}
	}
}

function getMaandag(d){
	d = new Date(d);
	var day = d.getDay(),
    diff = d.getDate() - day + (day == 0 ? -6:1);
	d.setDate(diff);
	return d;
}

function hideItem(id){
	document.getElementById(id).style.display = "none";
}

function showItem(id){
	document.getElementById(id).style.display = "block";
}

function removeItem(id){
	document.getElementById(id).remove();
}

function formatDate(date){
	var date = new Date(date);
	var monthNames = [
		"Januari", "Februari", "Maart",
		"April", "Mei", "Juni", "Juli",
		"Augustus", "September", "Oktober",
		"November", "December"
	];
	var day = date.getDate();
	var monthIndex = date.getMonth();
	var year = date.getFullYear();
	return day + ' ' + monthNames[monthIndex] + ' ' + year;
}








  
