function openNav() {
	document.getElementById("mySidebar").style.width = "250px";
	document.getElementById("main").style.marginLeft = "250px";
}

function closeNav() {
	document.getElementById("mySidebar").style.width = "0";
	document.getElementById("main").style.marginLeft = "0";
}

function onload() {
	var url = window.location.href.split("?")[1];;
	fetch("restservices/klantenPlanProvider/getBedrijfDataStart/" + url)
		.then(response => response.json())
		.then(function (data) {
			for (let dataPunten in data) {
				document.getElementById("klantEmail").hidden = !data.invoerveldEmail;
				document.getElementById("klantTelefoon").hidden = !data.invoerveldTelefoon;
				document.getElementById("klantAdres").hidden = !data.invoerveldAdres;
				document.getElementById("bedrijfsnaam").innerHTML = data.bedrijfEmail;
			}
			removeLoadingScreen();
		})
	inplannenGeslachtKiezen();
}

// de eerste functie die word aangeroepen om eet geslacht te kiezen
function inplannenGeslachtKiezen() {
	document.getElementById("geslachtMan").addEventListener("click", function () {
		behandelingenOphalen("man");
		geslachtKnopKleurManager("man");
	})
	document.getElementById("geslachtVrouw").addEventListener("click", function () {
		behandelingenOphalen("vrouw");
		geslachtKnopKleurManager("vrouw");
	})
	document.getElementById("geslachtJongen").addEventListener("click", function () {
		behandelingenOphalen("jongen");
		geslachtKnopKleurManager("jongen");
	})
	document.getElementById("geslachtMeisje").addEventListener("click", function () {
		behandelingenOphalen("meisje");
		geslachtKnopKleurManager("meisje");
	})

	document.getElementById("volgendeInplannen").addEventListener("click", function () {
		errorMessage("selecteer eerst een geslacht en daarna een behandeling.");
	})
}
function geslachtKnopKleurManager(geslacht) {
	document.getElementById("geslachtMeisje").style.background = "white"
	document.getElementById("geslachtVrouw").style.background = "white";
	document.getElementById("geslachtJongen").style.background = "white";
	document.getElementById("geslachtMan").style.background = "white";
	if (geslacht == "man") {
		document.getElementById("geslachtMan").style.background = "blue";
	} else if (geslacht == "vrouw") {
		document.getElementById("geslachtVrouw").style.background = "blue";
	} else if (geslacht == "jongen") {
		document.getElementById("geslachtJongen").style.background = "blue";
	} else if (geslacht == "meisje") {
		document.getElementById("geslachtMeisje").style.background = "blue"
	}
	document.getElementById("inplanBehandelingenForm").innerHTML = "";
	document.getElementById("lengteAfspraakForm").innerHTML = "0:00";
	document.getElementById("prijsAfspraakForm").innerHTML = "0.00";
	document.getElementById("inplanFormGeslacht").innerHTML = geslacht;
	document.getElementById("behandelingenKeuzeLijst").innerHTML = "";
}
//de behandelingen voor het gekozen geslacht worden opgehaalt
function behandelingenOphalen(geslacht) {
	var prijs = 0;
	var uren = 0;
	var minuten = 0;
	let behandelingenLijstFinal = [];

	var url = window.location.href.split("?")[1];;
	url = url + "&geslacht=" + geslacht;
	fetch("restservices/klantenPlanProvider/getBehandelingenByGeslacht/" + url)
		.then(response => response.json())
		.then(function (behandelingen) {
			for (let behandeling of behandelingen) {
				var  behandelingsDiv = createBehandelingDiv(behandeling);
				document.getElementById("behandelingenKeuzeLijst").appendChild(behandelingsDiv);
				
				behandelingsDiv.addEventListener("click", function () {
					var gekozen = false;
					if(behandelingenLijstFinal.length != 0){
						for (i = 0; i < behandelingenLijstFinal.length; i++) {
							console.log(behandeling.id +"=="+behandelingenLijstFinal[i]);
							if (behandelingenLijstFinal[i] == behandeling.id) {
								console.log("hoort");
								behandelingenLijstFinal.splice(i, 1);
								gekozen = true;
							} 
						}
					}
					if (gekozen) {
						document.getElementById("d" + behandeling.id).style.backgroundColor = "white";
						document.getElementById(behandeling.id).remove();
						prijs = prijs - behandeling.prijs;
						
						var lengte = behandeling.lengte;
						uren = uren - lengte.substring(0, 2);
						minuten = minuten - lengte.substring(3, 5);

						document.getElementById("prijsAfspraakForm").innerHTML = prijs;
						document.getElementById("lengteAfspraakForm").innerHTML = tijd;
					} else {
						
						var tijd = uren + ":" + minuten;
						behandelingenLijstFinal.push(behandeling.id);
						
						prijs = prijs + behandeling.prijs;
						
						var lengte = behandeling.lengte;
						uren = uren + lengte.substring(0, 2);
						minuten = minuten + lengte.substring(3, 5);
						
						document.getElementById("prijsAfspraakForm").innerHTML = prijs;
						document.getElementById("lengteAfspraakForm").innerHTML = tijd;
						
						var gekozenBehandelingsSpan = createGekozenBehandelingSpan(behandeling);
						document.getElementById("inplanBehandelingenForm").appendChild(gekozenBehandelingsSpan);
					}
				})
			}
		})
	document.getElementById("volgendeInplannen").addEventListener("click", function () {
		console.log(behandelingenLijstFinal.length);
		if (behandelingenLijstFinal.length > 0) {
			// het geslacht en behandeling is ingevuld
			document.getElementById("inplannenDatum").style.display = "block";
			document.getElementById("inplannenGeslacht").style.display = "none";
			document.getElementById("behandelingenKeuzeLijst").style.display = "none";

			datumOphalen(geslacht, behandelingenLijstFinal, uren, minuten);
		} else {
			errorMessage("selecteer eerst een behandeling");
		}
	})
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
function datumOphalen(geslacht, behandelingenLijstFinal, uren, minuten) {
	var dagMiliSeconden = 24 * 60 * 60 * 1000;

	var werkdagenLijst = [];
	var dagNamen = [
		"Maandag", "Dinsdag",
		"Woensdag", "Donderdag",
		"Vrijdag", "Zaterdag",
		"Zondag"
	];
	var url = window.location.href.split("?")[1];;

	fetch("restservices/klantenPlanProvider/werkdagen/" + url)
		.then(response => response.json())
		.then(function (dagen) {
			var inplanDatum = document.getElementById("dagenLijst");
			for (let dag of dagen) {
				var dagNummer = dag.dagNummmer;
				werkdagenLijst.push(dagNummer);

				var dagSpan = document.createElement('span');
				dagSpan.setAttribute('class', 'dagKiezen');
				dagSpan.setAttribute('id', "dagKeuze" + dagNummer);

				var dagNaam = document.createElement('a');
				var weekNaam = dagNamen[dagNummer];
				dagNaam.innerHTML = weekNaam + "<br>";
				dagSpan.appendChild(dagNaam);

				var dagDatum = document.createElement('a');
				var datum = new Date();
				datum.setTime(getMaandag(datum).getTime() + dagMiliSeconden * dagNummer);

				var formattedDate = formatDate(datum);
				dagDatum.innerHTML = formattedDate;
				dagDatum.setAttribute('id', 'date' + dagNamen[dagNummer]);
				dagSpan.appendChild(dagDatum);

				inplanDatum.appendChild(dagSpan);

				// op elke dag kan geklikt worden
				dagOphalen(geslacht, behandelingenLijstFinal, dagNummer, formattedDate, weekNaam, datum, uren, minuten);
			}
		})
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
	document.getElementById("terugInplannen").addEventListener("click", function () {
		// het geslacht en behandeling is ingevuld
		document.getElementById("dagenLijst").innerHTML = "";
		document.getElementById("inplannenDatum").style.display = "none";
		document.getElementById("inplannenGeslacht").style.display = "block";
		document.getElementById("behandelingenKeuzeLijst").style.display = "block";
	})
	document.getElementById("weekVerderInplannen").addEventListener("click", function () {
		var weekMiliSeconden = 7 * 24 * 60 * 60 * 1000;
		date.setTime(date.getTime() + weekMiliSeconden);
		document.getElementById("dagenLijst").innerHTML = "";
		datumOphalen();
	})

	document.getElementById("weekTerugInplannen").addEventListener("click", function () {
		var weekMiliSeconden = 7 * 24 * 60 * 60 * 1000;
		date.setTime(date.getTime() - weekMiliSeconden);
		document.getElementById("dagenLijst").innerHTML = "";
		datumOphalen();
	})
}


function dagOphalen(geslacht, behandelingenLijstFinal, dagNummer, formattedDate, weekNaam, datum, uren, minuten) {
	document.getElementById("dagKeuze" + dagNummer).addEventListener("click", function () {
		//behandelingen lijst toevoeging van datum
		var dagSpan = document.createElement('span');
		dagSpan.setAttribute('id', 'gekozenDag');
		var dagNaam = document.createElement('a');
		dagNaam.innerHTML = weekNaam + "<br>";
		var dagDatum = document.createElement('a');
		dagDatum.innerHTML = formattedDate;
		dagSpan.append(dagNaam);
		dagSpan.append(dagDatum);
		document.getElementById("datumInplanFormDiv").append(dagSpan);

		document.getElementById("datumInplanFormDiv").style.display = "inline-block";

		// aanmaken potentiele afspraak div
		var plan = document.getElementById("potentieleAfspraak");
		plan.innerHTML = "lengte:  " + document.getElementById("lengteAfspraakForm").innerHTML;
		var uur = uren * 60;
		var minuut = uur + minuten;
		var hoogte = minuut * 2;
		plan.style.height = hoogte + "px";
		createDraggable();

		document.getElementById("inplannenDatum").style.display = "none";
		document.getElementById("inplannenTijd").style.display = "block";

		var i = 0;
		var url = window.location.href.split("?")[1];

		datum = datum.getFullYear() + '-' + datum.getMonth() + '-' + datum.getDate();

		url = url + "&datum=" + datum;
		fetch("restservices/klantenPlanProvider/tijdslotenOphalen/" + url)
			.then(response => response.json())
			.then(function (afspraken) {
				for (let afspraak of afspraken) {
					if (i == 0) {
						// timeline wordt eerst aangemaakt
						console.log(afspraak.openingsTijd);

						var beginTijd = afspraak.openingsTijd.split(":");
						beginUur = parseInt(beginTijd[0]);
						beginMinuut = parseInt(beginTijd[1]);

						beginUurGlobal = beginUur;
						beginMinuutGlobal = beginMinuut;

						var eindTijd = afspraak.sluitingstijd.split(":");
						eindUur = parseInt(eindTijd[0]);
						eindMinuut = parseInt(eindTijd[1]);

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
						i++;
					} else {
						// gemaakte afsprraken worden in de timeline gezet
						var event = createAfspraak(afspraak.beginTijd, afspraak.lengte);

						document.getElementById("roosterInplanAgenda").appendChild(event);
					}
				}
			});
		document.getElementById("terugDag").addEventListener("click", function () {
			document.getElementById("inplannenDatum").style.display = "block";
			document.getElementById("inplannenTijd").style.display = "none";
			document.getElementById("timelineInplanAgenda").innerHTML = "";
			document.getElementById("roosterInplanAgenda").innerHTML = "";
		})
		document.getElementById("submitInplanFormButton").addEventListener("click", function () {
			inplannen(geslacht, behandelingenLijstFinal, datum);
		})
	});
}


//het vullen van de afspraken lijst bij de tijd instellen
function createAfspraak(beginTijd, lengte) {
	// event aanmaken
	var event = document.createElement('li');
	event.setAttribute('class', 'nietBeschikbaarAfspraak');

	event.style.backgroundColor = "#34D77B";

	event.innerHTML = beginTijd + " <br> " + lengte;

	// begin punt van de afspraak
	var topArray = beginTijd.split(":");
	var uurTop = parseInt(topArray[0]);
	var minuutTop = parseInt(topArray[1]);

	var minuutVerschilTop = minuutTop + uurTop * 60;
	var minuutBegin = beginMinuutGlobal + beginUurGlobal * 60;

	var topMinuten = minuutVerschilTop - minuutBegin;
	var top = topMinuten * 2 + 20;
	event.style.top = top + "px";
	// lengte van afspraak
	var lengteArray = lengte.split(":");
	var uurLengte = parseInt(lengteArray[0]);
	var minuutLengte = parseInt(lengteArray[1]);
	var minuutVerschilLengte = minuutLengte + uurLengte * 60;

	var hoogte = minuutVerschilLengte * 2;
	console.log(hoogte);
	event.style.height = hoogte + "px";

	return event;
}

//de afspraak die je naar een tijd kan slepen
function createDraggable() {
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
				var tijd = document.getElementsByClassName("drop-target")[0].getAttribute("tijd").split(":");
				var potentieelUur = parseInt(tijd[0]) - uren;
				var potentieelMinuut = parseInt(tijd[1] - minuten);

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




