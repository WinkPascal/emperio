var geslacht;
let behandelingenLijstFinal = [];
var prijs = 0;
var tijd = "00:00";

function onload() {	
	navigatie();
	var url = window.location.href.split("?")[1];;
	fetch("restservices/klantenPlanProvider/getBedrijfDataStart/" + url)
		.then(response => response.json())
		.then(function (data) {
			document.getElementById("bedrijfsnaam").innerHTML = data.bedrijfsNaam;
			
			var email =data.bedrijfEmail;
			var telefoon = data.bedrijfsTelefoon;
			var adres = data.bedrijfsAdres;
			
			document.getElementById("emailBedrijf").innerHTML = email;
			document.getElementById("telefoonBedrijf").innerHTML = telefoon;
			document.getElementById("adresBedrijf").innerHTML = adres;

			document.getElementById("klantEmail").hidden = !data.invoerveldEmail;
			document.getElementById("klantTelefoon").hidden = !data.invoerveldTelefoon;
			document.getElementById("klantAdres").hidden = !data.invoerveldAdres;

		})
		
	removeLoadingScreen();
	inplannenGeslachtKiezen();
	
}

function openNav() {
	document.getElementById("mySidebar").style.width = "250px";
	document.getElementById("main").style.marginLeft = "250px";
}

function closeNav() {
	document.getElementById("mySidebar").style.width = "0";
	document.getElementById("main").style.marginLeft = "0";
}

function navigatie(){
	var pagina = "behandelingen";
	
	document.getElementById("volgendeInplannen").addEventListener("click", function () {
		switch(pagina){
		case "behandelingen":
			if(behandelingenLijstFinal < 1){
				errorMessage("kies eerste een behandeling om verder te gaan!");
			} else{
				datumNaviagtie();
				document.getElementById("inplannenDatum").style.display = "block";
				document.getElementById("inplannenGeslacht").style.display = "none";
				document.getElementById("behandelingenKeuzeLijst").style.display = "none";
				pagina = "datum";
			}
			break;
		case "datum":
			errorMessage("kies een datum om verder te gaan!");
			break;
		case "tijd":
			
			break;
		}
	})

	document.getElementById("terugInplannen").addEventListener("click", function () {
		switch(pagina){
		case "behandelingen":
			errorMessage("U kan niet verder terug gaan!");
			break;
		case "datum":
			document.getElementById("inplannenDatum").style.display = "none";
			document.getElementById("inplannenGeslacht").style.display = "block";
			document.getElementById("behandelingenKeuzeLijst").style.display = "block";
			pagina = "behandelingen";
			break;
		case "tijd":
			document.getElementById("inplannenDatum").style.display = "block";
			document.getElementById("inplannenGeslacht").style.display = "none";
			document.getElementById("behandelingenKeuzeLijst").style.display = "none";
			pagina = "datum";
			break;

		}
		document.getElementById("inplannenDatum").style.display = "none";
		document.getElementById("inplannenGeslacht").style.display = "block";
		document.getElementById("behandelingenKeuzeLijst").style.display = "block";
	})
}


// de eerste functie die word aangeroepen om eet geslacht te kiezen
function inplannenGeslachtKiezen() {
	document.getElementById("inplannenGeslacht").style.display="block";
	document.getElementById("geslachtMan").addEventListener("click", function () {
		geslachtKnopKleurManager("man");
	})
	document.getElementById("geslachtVrouw").addEventListener("click", function () {
		geslachtKnopKleurManager("vrouw");
	})
	document.getElementById("geslachtJongen").addEventListener("click", function () {
		geslachtKnopKleurManager("jongen");
	})
	document.getElementById("geslachtMeisje").addEventListener("click", function () {
		geslachtKnopKleurManager("meisje");
	})
}
function geslachtKnopKleurManager(geslacht) {
	document.getElementById("geslachtMeisje").style.background = "white"
	document.getElementById("geslachtVrouw").style.background = "white";
	document.getElementById("geslachtJongen").style.background = "white";
	document.getElementById("geslachtMan").style.background = "white";
	if (geslacht == "man") {
		behandelingenOphalen("man");
		document.getElementById("geslachtMan").style.background = "blue";
	} else if (geslacht == "vrouw") {
		behandelingenOphalen("vrouw");
		document.getElementById("geslachtVrouw").style.background = "blue";
	} else if (geslacht == "jongen") {
		behandelingenOphalen("jongen");
		document.getElementById("geslachtJongen").style.background = "blue";
	} else if (geslacht == "meisje") {
		document.getElementById("geslachtMeisje").style.background = "blue"
		behandelingenOphalen("meisje");
	}
	document.getElementById("inplanBehandelingenForm").innerHTML = "";
	document.getElementById("lengteAfspraakForm").innerHTML = "0:00";
	document.getElementById("prijsAfspraakForm").innerHTML = "0.00";
	document.getElementById("behandelingenKeuzeLijst").innerHTML = "";
}

//de behandelingen voor het gekozen geslacht worden opgehaalt
function behandelingenOphalen(geslacht) {
	var url = window.location.href.split("?")[1];;
	url = url + "&geslacht=" + geslacht;
	fetch("restservices/klantenPlanProvider/getBehandelingenByGeslacht/" + url)
	.then(response => response.json())
	.then(function (behandelingen) {
		for (let behandeling of behandelingen) {
			var  behandelingsDiv = createBehandelingDiv(behandeling);
			document.getElementById("behandelingenKeuzeLijst").appendChild(behandelingsDiv);
			behandelingsDiv.addEventListener("click", function () {
				// controle of de gekozen behandeling als is gekozen
				var gekozen = false;
				if(behandelingenLijstFinal.length != 0){
					for (i = 0; i < behandelingenLijstFinal.length; i++) {
						if (behandelingenLijstFinal[i] == behandeling.id) {
							behandelingenLijstFinal.splice(i, 1);
							gekozen = true;
						} 
					}
				}
				if (gekozen) {
					document.getElementById("d" + behandeling.id).style.backgroundColor = "white";
					document.getElementById(behandeling.id).remove();
					prijs = prijs - behandeling.prijs;
					tijd = pasTijdAan(tijd, behandeling.lengte, "afhalen");
				} else {
					behandelingenLijstFinal.push(behandeling.id);
					prijs = prijs + behandeling.prijs;
					tijd = pasTijdAan(tijd, behandeling.lengte, "optellen");
					document.getElementById("inplanBehandelingenForm").appendChild(createGekozenBehandelingSpan(behandeling));
				}
				document.getElementById("prijsAfspraakForm").innerHTML = prijs;
				document.getElementById("lengteAfspraakForm").innerHTML = tijd;
			})
		}
	})

}

function datumNaviagtie(){
	var datum = new Date();
	datumOphalen(geslacht, behandelingenLijstFinal, tijd, datum);

	var weekMiliSeconden = 7 * 24 * 60 * 60 * 1000;
	document.getElementById("weekVerderInplannen").addEventListener("click", function(){
		datum.setTime(datum.getTime() + weekMiliSeconden);
		datumOphalen(geslacht, behandelingenLijstFinal, tijd, datum);			
	});
	
	document.getElementById("weekTerugInplannen").addEventListener("click", function(){
		datum.setTime(datum.getTime() - weekMiliSeconden);
		datumOphalen(geslacht, behandelingenLijstFinal, tijd, datum);			
	});		
}

function pasTijdAan(tijd, lengte, functie){
	var uren = parseInt(tijd.substring(0, 2));
	var minuten = parseInt(tijd.substring(3, 5));

	var urenLengte = parseInt(lengte.substring(0, 2));
	var minutenLengte = parseInt(lengte.substring(3, 5));
	if(functie == "afhalen"){
		minuten = minuten - minutenLengte;
		if(minuten < 0){
			uren = uren - 1;
			minuten = minuten + 60;
		}
		uren = uren - urenLengte;
	} else if (functie == "optellen"){
		minuten = minuten + minutenLengte;
		if(minuten > 59){
			uren = uren + 1;
			minuten = minuten - 60;
		}	
		uren = uren + urenLengte;
	}
	if(uren.toString().length == 1){
		uren = "0"+uren
	}
	return uren + ":"+minuten;
}

function createBehandelingDiv(behandeling){ 
	var behandelingsDiv = document.createElement('div');
	behandelingsDiv.setAttribute('id', "d" + behandeling.id);
	behandelingsDiv.setAttribute('class', 'behandelingKeuze');

	var behandelingsNaam = document.createElement('div');
	var behandelingsBeschrijving = document.createElement('div');
	var behandelingsLengte = document.createElement('div');
	var behandelingsPrijs = document.createElement('div');

	behandelingsNaam.innerHTML = behandeling.naam;
	behandelingsBeschrijving.innerHTML = behandeling.beschrijving;
	behandelingsLengte.innerHTML = "geschatte lengte: "+behandeling.lengte;
	behandelingsPrijs.innerHTML = "€"+behandeling.prijs;

	behandelingsNaam.setAttribute('class', 'naamBehandelingKeuze');
	behandelingsBeschrijving.setAttribute('class', 'beschrijvingBehandelingKeuze');
	behandelingsLengte.setAttribute('class', 'lengteBehandelingKeuze');
	behandelingsPrijs.setAttribute('class', 'prijsBehandelingKeuze');

	behandelingsDiv.appendChild(behandelingsNaam);
	behandelingsDiv.appendChild(behandelingsLengte);
	behandelingsDiv.appendChild(behandelingsBeschrijving);
	behandelingsDiv.appendChild(behandelingsPrijs);
	return behandelingsDiv;
}

function createGekozenBehandelingSpan(behandeling){ 
	document.getElementById("d" + behandeling.id).style.backgroundColor = "green";
	// blok in de lijst wordt aangemaakt
	var gekozenBehandelingsSpan = document.createElement('span');
	gekozenBehandelingsSpan.setAttribute('id', behandeling.id);
	gekozenBehandelingsSpan.setAttribute('class', 'inplannenBehandelingOverzicht');

	var textGekozenBehandeling = document.createElement('a');
	textGekozenBehandeling.style.cssFloat = "left";
	textGekozenBehandeling.innerHTML = behandeling.naam;
	gekozenBehandelingsSpan.appendChild(textGekozenBehandeling);

	var prijsGekozenBehandeling = document.createElement('a');
	prijsGekozenBehandeling.style.cssFloat = "right";
	prijsGekozenBehandeling.innerHTML = "€ " + behandeling.prijs;
	gekozenBehandelingsSpan.appendChild(prijsGekozenBehandeling);
	return gekozenBehandelingsSpan;
}

//de datum en tijden worden opgehaalt
function datumOphalen(geslacht, behandelingenLijstFinal, tijd, datum) { 
	var url = window.location.href.split("?")[1];;
	fetch("restservices/klantenPlanProvider/werkdagen/" + url)
		.then(response => response.json())
		.then(function (dagen) {
			document.getElementById("dagenLijst").innerHTML = "";
			for (let dag of dagen) {
				var dagNummer = dag.dagNummmer;
				var dagMiliSeconden = 24 * 60 * 60 * 1000;
				datum.setTime(getMaandag(datum).getTime() + dagMiliSeconden * dagNummer);
				var dagSpan = createDag(dagNummer, datum, geslacht);
				if(dagSpan != null){
					document.getElementById("dagenLijst").appendChild(dagSpan);
					dagOphalen(geslacht, behandelingenLijstFinal, dagNummer, datum, tijd);
				}
			}
		})
}

function createDag(dagNummer, datum){  
		var dagSpan = document.createElement('span');
		dagSpan.setAttribute('class', 'dagKiezen');
		dagSpan.setAttribute('id', "dagKeuze" + dagNummer);
		if(datum == new Date){
			dagSpan.innerHTML = "Vandaag";
		} else{
			var dagNaam = document.createElement('a');
			var weekNaam = getDagNaam(dagNummer);
			dagNaam.innerHTML = weekNaam + "<br>";
			dagSpan.appendChild(dagNaam);
			

			var formattedDate = formatDate(datum);
			var dagDatum = document.createElement('a');
			dagDatum.innerHTML = formattedDate;
			dagDatum.setAttribute('id', 'date' + getDagNaam(dagNummer));
			dagSpan.appendChild(dagDatum);
		}
		return dagSpan;
}

function getDagNaam(dagNummer){
	var dagNamen = [
		"Maandag", "Dinsdag",
		"Woensdag", "Donderdag",
		"Vrijdag", "Zaterdag",
		"Zondag"
	];
	return dagNamen[dagNummer];
}
function getMaandag(d) {
	d = new Date(d);
	var day = d.getDay(),
		diff = d.getDate() - day + (day == 0 ? -6 : 1);
	d.setDate(diff);
	return d;
}
function formatDate(date) {
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

function dagOphalen(geslacht, behandelingenLijstFinal, dagNummer, datum, tijd) {
	document.getElementById("dagKeuze" + dagNummer).addEventListener("click", function () {

		document.getElementById("inplannenDatum").style.display = "none";
		document.getElementById("inplannenTijd").style.display = "block";
 		var dagMiliSeconden = 24 * 60 * 60 * 1000;
		datum.setTime(getMaandag(datum).getTime() + dagMiliSeconden * dagNummer);
		createDagInForm(dagNummer, datum);
		// aanmaken potentiele afspraak div
		createDraggable(tijd);

		var url = window.location.href.split("?")[1];
		date = datum.getFullYear() + '-' + datum.getMonth() + '-' + datum.getDate();
		url = url + "&datum=" + date;
		fetch("restservices/klantenPlanProvider/tijdslotenOphalen/" + url)
			.then(response => response.json())
			.then(function (afspraken) {
				var beginTijdRooster = "00:00"
				var i = 0;
				for (let afspraak of afspraken) {
					if (i == 0) {
						i++;
						// timeline wordt eerst aangemaakt
						beginTijdRooster = afspraak.openingsTijd;
						var beginUur = parseInt(beginTijdRooster.split(":")[0]);
						var beginMinuut = parseInt(beginTijdRooster.split(":")[1]);

						var eindTijd = afspraak.sluitingstijd.split(":");
						var eindUur = parseInt(eindTijd[0]);
						var eindMinuut = parseInt(eindTijd[1]);

						var ul = document.createElement('ul');
						var timeLi = document.createElement('li');
						timeLi.setAttribute('tijd', beginUur + ":" + beginMinuut);

						ul.appendChild(timeLi);
						timeLi.setAttribute('class', 'dropzone line');
						timeLi.innerHTML = beginUur + ":" + beginMinuut;

						while (true) {
							beginMinuut = beginMinuut + 10;
							var timeLi = document.createElement('li');
							timeLi.setAttribute('tijd', beginUur + ":" + beginMinuut);
							ul.appendChild(timeLi);

							if (beginMinuut == 30) {
								timeLi.setAttribute('class', 'dropzone line');
								timeLi.innerHTML = beginUur + ":" + beginMinuut;
							} else if (beginMinuut > 59) {
								beginUur = beginUur + 1;
								beginMinuut = 0;
								timeLi.setAttribute('class', 'dropzone line');
								timeLi.innerHTML = beginUur + ":" + beginMinuut;
							} else {
								timeLi.setAttribute('class', 'dropzone');
							}

							if (beginUur >= eindUur && beginMinuut >= eindMinuut) {
								break;
							}
						}
						document.getElementById("timelineInplanAgenda").appendChild(ul);
					} else {
						// gemaakte afsprraken worden in de timeline gezet
						var event = createAfspraak(afspraak.beginTijd, afspraak.lengte, beginTijdRooster);
						document.getElementById("roosterInplanAgenda").appendChild(event);
					}
				}
			});
		document.getElementById("submitInplanFormButton").addEventListener("click", function () {
			inplannen(geslacht, behandelingenLijstFinal, date);
		})
	});
}

function createDagInForm(dagNummer, datum){
	var dagSpan = document.createElement('span');
	dagSpan.setAttribute('id', 'gekozenDag');
	var dagNaam = document.createElement('a');
	var weekNaam = getDagNaam(dagNummer);

	dagNaam.innerHTML = weekNaam + "<br>";
	dagSpan.append(dagNaam);
	
	var dagDatum = document.createElement('a');
	var formattedDate = formatDate(datum);

	dagDatum.innerHTML = formattedDate;
	dagSpan.append(dagDatum);

	document.getElementById("datumInplanFormDiv").append(dagSpan);
}

//het vullen van de afspraken lijst bij de tijd instellen
function createAfspraak(beginTijd, lengte, beginTijdRooster) {
	// begin punt van de afspraak
	var minuutVerschilTop = parseInt(beginTijd.split(":")[1]) + parseInt(beginTijd.split(":")[0]) * 60;
	var minuutBegin = parseInt(beginTijdRooster.split(":")[1]) + parseInt(beginTijdRooster.split(":")[0]) * 60;
	var topMinuten = minuutVerschilTop - minuutBegin;

	var event = document.createElement('li');
	event.setAttribute('class', 'nietBeschikbaarAfspraak');
	event.innerHTML = beginTijd + " - " + lengte;
	event.style.top = topMinuten * 2 + 20 + "px";
	event.style.height = parseInt(lengte.split(":")[1]) + parseInt(lengte.split(":")[0]) * 60 * 2 + "px";

	return event;
}

//de afspraak die je naar een tijd kan slepen
function createDraggable(tijd) { 
	var plan = document.getElementById("potentieleAfspraak");
	plan.innerHTML = "lengte:  " + document.getElementById("lengteAfspraakForm").innerHTML;
	var minuut = parseInt(tijd.substring(3, 5)) + parseInt(tijd.substring(0, 2)) * 60;
	var hoogte = minuut * 2;
	plan.style.height = hoogte + "px";

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
					relativePoints: [{ x: 0, y: 0 }]
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
				var tijdTarget = document.getElementsByClassName("drop-target")[0].getAttribute("tijd").split(":");
				var potentieelUur = parseInt(tijdTarget[0]) - parseInt(tijd.substring(0, 2));
				var potentieelMinuut = parseInt(tijdTarget[1] - parseInt(tijd.substring(3, 5)));

				if (potentieelMinuut < 0) {
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

function inplannen(geslacht, behandelingenLijstFinal, datum) {
	var formData = new FormData(document.getElementById("inplannenForm"));
	var afspraakTijd = document.getElementById("urenInplannen").innerHTML
					   + ":" +
					   document.getElementById("minutenInplannen").value;
	formData.append("afspraakTijd", afspraakTijd);
	var encData = new URLSearchParams(formData.entries());
	var url = window.location.href.split("?")[1];
	url = url + "&geslacht=" + geslacht + "&behandelingen=" + behandelingenLijstFinal + "&datum=" + datum + "&afspraakTijd=" + afspraakTijd;
	// De wijziging wordt verstuurt naar de backend
	var fetchoptions = {
		method: 'POST',
		body: encData
	}
	fetch("restservices/klantenPlanProvider/afspraak/" + url, fetchoptions)
		.then(function (response) {
			if (response.ok) {
				alert("ingepland");
			} else if (response.status = 409) {
				alert("door validatie");
			} else {
				alert("idk wtf");
			}
		})
}
