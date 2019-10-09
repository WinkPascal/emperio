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
			
			tijdslotenOphalen();
		}
	} else if(status == "datum"){
		document.getElementById("inplannenDatum").style.display = "none";
		document.getElementById("inplannenForm").style.width = "800px";
		document.getElementById("klantInfo").style.display = "block";
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
	fetch("restservices/service/behandelingen/" + geslacht, fetchoptions)
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
			
			behandelingsDiv.addEventListener("click", function() {
				hideItem("d"+behandeling.id);
				inplanVoorbereiding("lengteToevoegen", behandeling.lengte);
				inplanVoorbereiding("prijsToevoegen", behandeling.prijs);
				
				document.getElementById("prijsAfspraakForm").innerHTML = "€"+ prijs;
				
				document.getElementById("lengteAfspraakForm").innerHTML = uren +":"+ minuten;

				inplanVoorbereiding("behandelingToevoegen", behandeling.id);
				
				var gekozenBehandelingsSpan = document.createElement('span');
				gekozenBehandelingsSpan.setAttribute('id', behandeling.id);
				gekozenBehandelingsSpan.setAttribute('class', 'inplannenBehandelingOverzicht');
				
				var textGekozenBehandeling = document.createElement('a');
				textGekozenBehandeling.setAttribute('class', "textGekozenBehandeling");
				textGekozenBehandeling.innerHTML = behandeling.naam;
				gekozenBehandelingsSpan.appendChild(textGekozenBehandeling);
				
				gekozenBehandelingsSpan.addEventListener("click", function(){
					removeItem(behandeling.id);
					showItem("d"+behandeling.id);
					
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
	document.getElementById("tijslotenLijst").innerHTML = "";
	tijdslotenOphalen();
})

document.getElementById("weekTerugInplannen").addEventListener("click", function(){
	var weekMiliSeconden = 7 * 24 * 60 * 60 * 1000;
	date.setTime(date.getTime() - weekMiliSeconden);
	document.getElementById("tijslotenLijst").innerHTML = "";
	tijdslotenOphalen();
})

// de datum en tijden worden opgehaalt
function tijdslotenOphalen(){ 	 
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
	fetch("restservices/service/werkdagen", fetchoptions)
	.then(response => response.json())
	.then(function(dagen){
		var inplanDatum = document.getElementById("tijslotenLijst");
		for(let dag of dagen){
			var dagNummmer = dag.dagNummmer;
			werkdagenLijst.push(dagNummmer);

			var dagSpan = document.createElement('span');
			dagSpan.setAttribute('class', 'dagKiezen');
			dagSpan.setAttribute('id', "dagKeuze"+dagNummmer);

			var dagNaam = document.createElement('a');
			dagNaam.innerHTML = dagNamen[dagNummmer] +"<br>";
			dagSpan.appendChild(dagNaam);
			
			var dagDatum = document.createElement('a');
			var datum = new Date();
			datum.setTime(getMaandag(date).getTime() + dagMiliSeconden*dagNummmer);

			var formattedDate = formatDate(datum);

			dagDatum.innerHTML = formattedDate;
			dagDatum.setAttribute('id', 'date'+dagNamen[dagNummmer]);
			dagSpan.appendChild(dagDatum);
		
			inplanDatum.appendChild(dagSpan);
			
			dagKlik(dagNummmer);
		}
		
		/* De dagkeuzes worden hidden en de gekozen dag komt in het form */
		function dagKlik(dagNummmer){
			console.log("dagKeuze"+dagNummmer);
			document.getElementById("dagKeuze"+dagNummmer).addEventListener("click", function(){
				toggle('dagKiezen', 'none');
				dagKlik(dagNummmer, formattedDate);
				var costumDate = getMaandag(date);
				date.setTime(costumDate.getTime() + dagMiliSeconden*dagNummmer);
				ophalen(date);
				
				var dagSpan = document.createElement('span');
				dagSpan.setAttribute('class', 'dagGekozen');
				
				var dagNaam = document.createElement('p');
				dagNaam.innerHTML = dagNamen[dagNummmer];
				dagSpan.appendChild(dagNaam);
				
				var dagDatum = document.createElement('p');
				var datum = new Date();
				datum.setTime(getMaandag(date).getTime() + dagMiliSeconden*dagNummmer);				
				var formattedDate = formatDate(datum);
				dagDatum.setAttribute('id', 'dagDatum');

				dagDatum.innerHTML = formattedDate;
				dagSpan.appendChild(dagDatum);

				var dagTijd = document.createElement('a');
				dagTijd.setAttribute('id', 'dagTijd');
				dagSpan.appendChild(dagTijd);

				document.getElementById("datumInplanForm").appendChild(dagSpan);
			})
		}
	})
}

function toggle(className, displayState){
    var elements = document.getElementsByClassName(className)

    for (var i = 0; i < elements.length; i++){
        elements[i].style.display = displayState;
    }
}

function ophalen(datum){
	var date = datum.getFullYear() +'-'+ datum.getMonth() +'-'+ datum.getDate();
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/tijdslotenOphalen/"+date, fetchoptions)
	.then(response => response.json())
	.then(function(afspraken){
		beginTijdslotUur = null;
		beginTijdslotMinuut = null;
		
		tijdslotUren = [];
		tijdslotMinuten = [];
		tijdslotenDag = [];
					
		var tijdsloten = document.getElementById("tijslotenLijst");
		
		var i = 0;
		for(let afspraak of afspraken){
			if(i == 0){
				i++;
				openingsTijd = afspraak.openingsTijd;
				
				var openingsTijden = openingsTijd.split(":");
				beginTijdslotUur = parseInt(openingsTijden[0]);
				beginTijdslotMinuut = parseInt(openingsTijden[1]);
				
				var openingsTijdSpan = document.createElement('span');
				openingsTijdSpan.setAttribute('class', 'openingsTijdInplannen');
				openingsTijdSpan.innerHTML = openingsTijd;
				
				tijdsloten.appendChild(openingsTijdSpan);
			} else{					
				var afspraakBeginTijden = afspraak.beginTijd.split(":");
				var afspraakBeginUur = parseInt(afspraakBeginTijden[0]);
				var afspraakBeginMinuten = parseInt(afspraakBeginTijden[1]);
				
				var afspraakEindTijden = afspraak.eindTijd.split(":");
				var afspraakEindUur = parseInt(afspraakEindTijden[0]);
				var afspraakEindMinuten = parseInt(afspraakEindTijden[1]);
			
				 while(true){

					var eindTijdslotUur = parseInt(beginTijdslotUur) + parseInt(uren);
					var eindTijdslotMinuut = parseInt(beginTijdslotMinuut) + parseInt(minuten);
					if(eindTijdslotMinuut > 59){
						eindTijdslotUur++;
						eindTijdslotMinuut=eindTijdslotMinuut-60;
					}

					if(eindTijdslotUur >= afspraakBeginUur && eindTijdslotMinuut > afspraakBeginMinuten){
						 // er is al een afspraak op dit tijdslot
						 if(afspraakEindUur == "NaN"){
								var openingsTijdSpan = document.createElement('span');
								openingsTijdSpan.setAttribute('class', 'openingsTijdInplannen');
								openingsTijdSpan.innerHTML = afspraakBeginUur +":"+afspraakBeginMinuten;
								
								tijdsloten.appendChild(openingsTijdSpan);
						 } else{
							 var afspraakTijdSpan = document.createElement('span');
							 afspraakTijdSpan.setAttribute('class', 'geplandeAfspraak');
							 afspraakTijdSpan.innerHTML= afspraakBeginUur+":"+ afspraakBeginMinuten +
							 "tot"+ afspraakEindUur+":"+afspraakEindMinuten;
							 tijdsloten.appendChild(afspraakTijdSpan);
							 // Er kan geen afspraak gemaakt worden omdat
								// er al een afspraak is
							 beginTijdslotUur = afspraakEindUur;
							 beginTijdslotMinuut = afspraakEindMinuten;
						 }
						break;							
					 } else{
						 // er word een tijdslot aangermaakt
						 // beschikbare minuten van het uur wordt hieraan
							// toegevoegd
						 tijdslotMinuten.push(beginTijdslotMinuut);
						 nietGemaakt = true;

						 for(var uur of tijdslotUren){
							 if(uur == beginTijdslotUur){
								 nietGemaakt = false;
							 }
						 }
						 if(nietGemaakt){
								 tijdslotUren.push(beginTijdslotUur);
							 var afspraakTijdSpan = document.createElement('span');
							 afspraakTijdSpan.setAttribute('class', 'afspraakTijdslot');
							 afspraakTijdSpan.setAttribute('id', "dagTijdslot"+beginTijdslotUur);
							 afspraakTijdSpan.innerHTML= beginTijdslotUur +":"+ tijdslotMinuten[0];
							 tijdsloten.appendChild(afspraakTijdSpan);
							 getTijdsloten(beginTijdslotUur);
						 }
						 beginTijdslotMinuut = beginTijdslotMinuut+10;
						 if(beginTijdslotMinuut > 59){								 
							 beginTijdslotMinuut = beginTijdslotMinuut - 60;
							 
							 tijdslotenDag.push(tijdslotMinuten);
							 // minuten worden leeggemaakt voor het
							// volgende uur
							 tijdslotMinuten = [];
							 beginTijdslotUur++;
						 }
					 }
				 }
			 }
		}
		function getTijdsloten(id){
			document.getElementById("dagTijdslot"+id).addEventListener("click", function(){
				toggle("afspraakTijdslot", "none");
				toggle("geplandeAfspraak", "none");
				toggle("openingsTijdInplannen", "none");
				var i = 0;
				for(var uur of tijdslotUren){
					if(uur == id){
						for(tijdslotMinuut of tijdslotenDag[i]){
							var eindTijdslotMinuut = tijdslotMinuut +parseInt(minuten);								
							var eindTijdslotUur = parseInt(uur) + parseInt(uren);
							
							if(eindTijdslotMinuut > 59){
								eindTijdslotUur++;
								eindTijdslotMinuut=eindTijdslotMinuut-60;
							}
							
							
							var afspraakTijdSpan = document.createElement('span');
							afspraakTijdSpan.setAttribute('class', 'afspraakTijdslot');
							afspraakTijdSpan.setAttribute('id', "tijdslotInplan"+id+":"+tijdslotMinuut);
							
							var text = uur +":"+tijdslotMinuut +" tot " + eindTijdslotMinuut +" : "+eindTijdslotMinuut;
							
							afspraakTijdSpan.innerHTML=text;
							tijdsloten.appendChild(afspraakTijdSpan);
							setTijdslot(id+":"+tijdslotMinuut);
						}
						break;
					}
					i++;
				}
			});
		}
		function setTijdslot(id){
			document.getElementById("tijdslotInplan"+id).addEventListener("click", function(){
				document.getElementById("dagTijd").innerHTML = id;
			})
		}
	})
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
	console.log("inplannsssen");

	formData.append("klantGeslacht", geslacht);
	formData.append("afspraakBehandeling", JSON.stringify(behandelingenlijst));
	formData.append("afspraakTijd", document.getElementById("dagTijd").innerHTML);
	formData.append("afspraakDatum", document.getElementById("dagDatum").innerHTML);

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
	fetch("restservices/service/afspraak", fetchoptions)
	.then(function (response){
		if(response.ok){
			alert("ingepland");
		} else{
			alert("fout");
		}
	})
}


// hier worden behandelingen toegevoegd
document.getElementById("inplannenBehnadelingToevoegen").addEventListener("click", function(){
	behandelignToevoegenForm();
})
function behandelignToevoegenForm(){
	geslachten = [];
	
	document.getElementById("BehandelingToevoegenModal").style.display = "block";
	document.getElementById("inplannenModal").style.display = "none";
	
	document.getElementById("annuleerBehandelingAnnuleren").addEventListener("click", function(){
		document.getElementById("BehandelingToevoegenModal").style.display = "none";
		document.getElementById("inplannenModal").style.display = "block";
	})
	
	document.getElementById("geslachtManToevoegen").addEventListener("click", function(){
		if(behandelingManager("man")){
			document.getElementById("geslachtManToevoegen").style.background = "blue";
		} else{
			document.getElementById("geslachtManToevoegen").style.background = "white";
		}
	})
	document.getElementById("geslachtVrouwToevoegen").addEventListener("click", function(){
		if(behandelingManager("vrouw")){
			document.getElementById("geslachtVrouwToevoegen").style.background = "blue";
		} else{
			document.getElementById("geslachtVrouwToevoegen").style.background = "white";
		}
	})	
	document.getElementById("geslachtJongenToevoegen").addEventListener("click", function(){
		if(behandelingManager("jongen")){
			document.getElementById("geslachtJongenToevoegen").style.background = "blue";
		} else{
			document.getElementById("geslachtJongenToevoegen").style.background = "white";
		}
	})	
	document.getElementById("geslachtMeisjeToevoegen").addEventListener("click", function(){
		if(behandelingManager("meisje")){
			document.getElementById("geslachtMeisjeToevoegen").style.background = "blue";
		} else{
			document.getElementById("geslachtMeisjeToevoegen").style.background = "white";
		}
	})

	function behandelingManager(geslacht){
		if(geslachten.includes(geslacht)){
			for( var i = 0; i < geslachten.length; i++){ 
				if (geslachten[i] === geslacht) {
					geslachten.splice(i, 1); 
					i--;
					return false;
				}
			}
			
		} else{
			geslachten.push(geslacht);
			return true;
		}
	}
}

document.getElementById("inplannenBehandelingToevoegen").addEventListener("click", function(){
	behandelingToevoegen();
})
function behandelingToevoegen(){
	var formData = new FormData(document.querySelector("#BehandelingToevoegenForm"));	
	formData.append("geslachten", JSON.stringify(geslachten));
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
	fetch("restservices/service/behandeling", fetchoptions)
	.then(function (response){
		if(response.ok){
			alert("toegevoegd");
		} else{
			alert("kapot fout");
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