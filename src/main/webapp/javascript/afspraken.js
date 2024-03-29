function onload() {
	datum = new Date();

	getRooster(datum, true);
}

function createAfspraakRooster(beginTijd, lengte, klant, prijs) {
	var event = document.createElement('li');
	event.setAttribute('class', 'afspraak');

	var klasse1 = localStorage.getItem("klasse1");
	var klasse2 = localStorage.getItem("klasse2");
	var klasse3 = localStorage.getItem("klasse3");

	var beginMinuutRooster = parseInt(sessionStorage.getItem('beginMinuutRooster'));
	var beginUurRooster = parseInt(sessionStorage.getItem('beginUurRooster'));

	if (prijs < klasse1 && prijs > 0) {
		event.style.backgroundColor = getComputedStyle(document.documentElement)
			.getPropertyValue("--afspraakKlasse1");
	}
	if (prijs < klasse2) {
		event.style.backgroundColor = getComputedStyle(document.documentElement)
			.getPropertyValue("--afspraakKlasse2");
	}
	if (prijs > klasse3) {
		event.style.backgroundColor = getComputedStyle(document.documentElement)
			.getPropertyValue("--afspraakKlasse3");
	}
	if (prijs == 0) {
		event.style.backgroundColor = getComputedStyle(document.documentElement)
			.getPropertyValue('--afspraakKlasse0');
	}
	var afspraakText =
		"Klant: " + klant +
		"<br> prijs: " + prijs +
		"<br> Begin tijd: " + beginTijd +
		"<br> lengte: " + lengte;
	event.innerHTML = afspraakText;

	// begin punt van de afspraak
	var topArray = beginTijd.split(":");
	var uurTop = parseInt(topArray[0]);
	var minuutTop = parseInt(topArray[1]);

	var minuutVerschilTop = minuutTop + uurTop * 60;
	var minuutBegin = beginMinuutRooster + beginUurRooster * 60;

	var topMinuten = minuutVerschilTop - minuutBegin;
	var a = 5 / 3;
	var top = topMinuten * a;
	event.style.top = top + "px";

	// lengte van afspraak
	var lengteArray = lengte.split(":");
	var uurLengte = parseInt(lengteArray[0]);
	var minuutLengte = parseInt(lengteArray[1]);
	var minuutVerschilLengte = minuutLengte + uurLengte * 60;

	var a = 5 / 3;
	var hoogte = minuutVerschilLengte * a;
	if (hoogte == 0) {
		return null;
	}
	event.style.height = hoogte + "px";

	return event;
}

function createTimeline(dag){
	// timeline wordt eerst aangemaakt
	beginTijd = dag.vroegsteOpeningsTijd.split(":");
	beginUur = parseInt(beginTijd[0]);
	beginUurRooster = beginUur;
	beginMinuut = parseInt(beginTijd[1]);
	beginMinuutRooster = beginMinuut;

	var eindTijd = dag.laatsteSluitingsTijd.split(":");
	eindUur = parseInt(eindTijd[0]);
	eindUurRooster = eindUur;
	eindMinuut = parseInt(eindTijd[1]);
	eindMinuutRooster = eindMinuut;

	//wordt gebruikt bij het maken van de afspraken 
	sessionStorage.setItem('beginMinuutRooster', beginMinuutRooster);
	sessionStorage.setItem('beginUurRooster', beginUurRooster);

	var ul = document.createElement('ul');
	while (true) {
		var timeLi = document.createElement('li');
		var timeSpan = document.createElement('span');

		timeSpan.innerHTML = beginUur + ":" + beginMinuut;

		timeLi.appendChild(timeSpan);
		ul.appendChild(timeLi);

		beginMinuut = beginMinuut + 30;
		if (beginMinuut > 59) {
			beginUur = beginUur + 1;
			beginMinuut = beginMinuut - 60;
		}

		if (beginUur >= eindUur && beginMinuut >= eindMinuut) {
			break;
		}

	}
	document.getElementById("timeline").appendChild(ul);
	return beginTijd;
}
function createToInfo(weekNummer){
	var weekdagen = new Array(7);
	weekdagen[0] = "Maandag";
	weekdagen[1] = "Dinsdag";
	weekdagen[2] = "Woensdag";
	weekdagen[3] = "Donderdag";
	weekdagen[4] = "Vrijdag";
	weekdagen[5] = "Zaterdag";
	weekdagen[6] = "Zondag";
	
	var topinfo = document.createElement('div');
	topinfo.setAttribute('class', 'top-info');

	var weekNaam = document.createElement('a');
	weekNaam.innerHTML = weekdagen[weekNummer] + "<br>";

	// datums onder de dag zetten
	// get Date maandag van de week
	var maandag = new Date(datum);
	var day = maandag.getDay(),
		diff = maandag.getDate() - day + (day == 0 ? -6 : 1);
	maandag.setDate(diff);
	maandag.setDate(maandag.getDate() + weekNummer);
	maandag.getDay(), diff = maandag.getDate() - day + (day == 0 ? -6 : 1);

	var dagDate = formatDate(maandag);

	var weekDate = document.createElement('a');
	weekDate.setAttribute('id', "dagDate" + weekNummer);
	weekDate.innerHTML = dagDate;
	topinfo.appendChild(weekNaam);
	topinfo.appendChild(weekDate);
	return topinfo;
}

function verwijderenVorigRooster(maandag){
	var day = maandag.getDay(), diff = maandag.getDate() - day + (day == 0 ? -6 : 1);
	maandag.setDate(diff);
	var date = maandag.getFullYear() + '-' + maandag.getMonth() + '-' + maandag.getDate();
	var  i = 0;
	if (!onload) {
		// datums onder de dag zetten
		// get Date maandag van de week
		while (i < 7) {
			var dagDate = formatDate(maandag);
			document.getElementById("dagDate" + i).innerHTML = dagDate;
			maandag.setDate(maandag.getDate() + 1);
			i++;
		}
		for (let afspraak of afsprakenLijst) {
			afspraak.remove();
		}
		afsprakenLijst = [];
	} else{
		afsprakenLijst = [];
	}
	dagenLijst = [];
}

function getRooster(datum, onload) {
	// als het niet voor de eerste keer geladen wordt
	var maandag = new Date(datum);
	verwijderenVorigRooster(maandag);
	
	var fetchoptions = {
		headers: {
			'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
		}
	}
	var date = maandag.getFullYear() + '-' + maandag.getMonth() + '-' + maandag.getDate();
	fetch("restservices/afspraak/getWeekRooster/"+date, fetchoptions)
		.then(response => response.json())
		.then(function (rooster) {
			var i = 1;

			var eindUurRooster = 0;
			var eindMinuutRooster = 0;
			
			var beginTijd = createTimeline(rooster.uitersteTijden);
			let dagTijden = rooster.dagTijden;
			alert(dagTijden.length);
			for(let dag of dagTijden){	
			// er wordt een dag aangemaakt
					var dagVanRooster = document.createElement('li');
					dagVanRooster.setAttribute('class', 'dag');
					dagVanRooster.appendChild(createToInfo(dag.weekNummer));
					// events van de dag
					var events = document.createElement('ul');
					events.setAttribute('class', 'eventsDag');
					events.setAttribute('id', 'dag' + dag.weekNummer);
					// ochtend gesloten wordt aangemaakt
					var beginTijd = dag.openingsTijd;
					if(beginTijd != "gesloten"){
						var eventOchtend = berekenOpeningstijd(beginTijd);
						if(eventOchtend != null){
							events.appendChild(eventOchtend);
						}
						avond = berekenSluitingstijd(dag.sluitingsTijd);
						if (avond != null) {
							events.appendChild(avond);
						}
					} else{
						console.log("gessloten");
						console.log(dag.openingsTijd);
					}
					console.log(dag.openingsTijd);


					function berekenSluitingstijd(sluitingsTijdAvond){
						var sluitingsTijdDag = sluitingsTijdAvond.split(":");
						var eindUurDag = parseInt(sluitingsTijdDag[0]);
						var eindMinuutDag = parseInt(sluitingsTijdDag[1]);
						var eindTijdDag = eindUurDag + ":" + eindMinuutDag;

						var uurEindLengte = eindUurRooster - eindUurDag;
						var minuutEindLengte = eindMinuutRooster - eindMinuutDag;
						if (minuutEindLengte < 0) {
							minuutEindLengte + 60;
							uurEindLengte = uurEindLengte - 1;
						}
						var lengteAvond = uurEindLengte + ":" + minuutEindLengte;
						var avond = createAfspraakRooster(eindTijdDag, lengteAvond, "avond", 0);
						return avond;
					}

					function berekenOpeningstijd(openingsTijdOchtend){
						var openingsTijdDag = openingsTijdOchtend.split(":");
						var beginUurDag = parseInt(openingsTijdDag[0]);
						var beginMinuutDag = parseInt(openingsTijdDag[1]);

						var uurVerschilLengte = beginUurDag - beginUurRooster;
						var minuutVerschilLengte = beginMinuutDag - beginMinuutRooster;
						if (minuutVerschilLengte < 0) {
							uurVerschilLengte = uurVerschilLengte - 1;
							minuutVerschilLengte = minuutVerschilLengte + 60;
						}
						var lengteOchtend = uurVerschilLengte + ":" + minuutVerschilLengte;
						var topOchtend = beginUurRooster + ":" + beginMinuutRooster;
						var ochtend = createAfspraakRooster(topOchtend, lengteOchtend, "ochtend", 0);
						return ochtend;
					}					

					dagVanRooster.appendChild(events);
					document.getElementById("rooster").appendChild(dagVanRooster);
				
		}

			// maken breedte css
			var dagen = document.getElementsByClassName('dag');
			var breedte = 100 / 7;
			for (m = 0; m < dagen.length; m++) {
				dagen[m].style.width = breedte + "%";
			}
			document.getElementById("weekTerug").addEventListener("click", function () {
				datum.setDate(datum.getDate() - 7);
				getRooster(datum, false);
			});

			document.getElementById("weekVerder").addEventListener("click", function () {
				datum.setDate(datum.getDate() + 7);
				getRooster(datum, false);
			});
		})
}


function getAfspraken(datum, onload) {
	// alle afspraken worden uit het rooster gehaalt
	for (let afspraak of afsprakenLijst) {
		afspraak.remove();
	}
	afsprakenLijst = [];
	var i = 0;

	// als het niet voor de eerste keer geladen wordt
	var maandag = new Date(datum);
	var day = maandag.getDay(), diff = maandag.getDate() - day + (day == 0 ? -6 : 1);
	maandag.setDate(diff);
	var date = maandag.getFullYear() + '-' + maandag.getMonth() + '-' + maandag.getDate();

	if (!onload) {
		// datums onder de dag zetten
		// get Date maandag van de week
		while (i < 7) {
			var dagDate = formatDate(maandag);
			document.getElementById("dagDate" + i).innerHTML = dagDate;
			maandag.setDate(maandag.getDate() + 1);
			i++;
		}
	}

	var fetchoptions = {
		headers: {
			'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
		}
	}
	fetch("restservices/afspraak/getWeekAfspraken/" + date, fetchoptions)
		.then(response => response.json())
		.then(function (afspraken) {
			for (let afspraak of afspraken) {
				// variabalen initializen
				var uurLengte = 0;
				var minuutLente = 0;

				var behandelingen = "";
				var prijs = 0;

				// behandelingen ophalen
				for (let behandeling of afspraak.behandelingen) {
					var lengteArray = behandeling.lengte.split(":");
					uurLengte = uurLengte + parseInt(lengteArray[0]);
					minuutLente = minuutLente + parseInt(lengteArray[1]);
					if (minuutLente > 59) {
						uurLengte = uurLengte + 1;
						minuutLente = minuutLente - 60;
					}
					prijs = prijs + behandeling.prijs;
					behandelingen = behandelingen + behandeling.naam;
				}
				// afpsraak attributen en afspraak aanmaken
				var afspraakLengte = uurLengte + ":" + minuutLente;
				var top = afspraak.timestamp.substring(16, 11);
				var klant = afspraak.klant;


				var event = createAfspraakRooster(top, afspraakLengte, klant, prijs);

				event.setAttribute('id', afspraak.id);
				afsprakenLijst.push(event);

				// bepalen waar de afspraak wordt toegvoegdt
				var afspraakDate = new Date(afspraak.timestamp.substring(0, 10));
				var dagnummer = afspraakDate.getDay()-1;
				var dagId = "dag" + dagnummer;
				console.log(dagId)
				document.getElementById(dagId).appendChild(event);

				createOnClickListener(afspraak.id, top, afspraakDate, afspraakLengte);
			}
			var elements = document.getElementsByClassName("afspraak");
			var breedte = document.getElementsByClassName("dag")[0].offsetWidth;
			for (var i = 0; i < elements.length; i++) {
				elements[i].style.width = (breedte + "px");
			}
		})
}

function createOnClickListener(id, tijd, afspraakDate, afspraakLengte) {
	document.getElementById(id).addEventListener("click", function () {
		document.getElementById("afspraakModal").style.display = "block";
		var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
		fetch("restservices/afspraak/getAfspraak/" + id, fetchoptions)
			.then(response => response.json())
			.then(function (afspraak) {
				// klant info
				document.getElementById("naamInfo").innerHTML = afspraak.klantNaam;
				document.getElementById("emailInfo").innerHTML = afspraak.klantEmail;
				document.getElementById("geslachtInfo").innerHTML = afspraak.klantGeslacht;
				document.getElementById("telefoonInfo").innerHTML = afspraak.klantTelefoon;
				document.getElementById("adresInfo").innerHTML = afspraak.adres;
				// afspraak info
				document.getElementById("prijsInfo").innerHTML = "€ " + afspraak.totaalPrijs;
				document.getElementById("lengteInfo").innerHTML = afspraakLengte;
				document.getElementById("datumInfo").innerHTML = afspraakDate;
				document.getElementById("tijdInfo").innerHTML = tijd;
				// behandelingen
				var behandelingenLijst = document.getElementById("behandelingenLijst");
				console.log(afspraak.behandelingen);
				for (let behandeling of afspraak.behandelingen) {
					console.log(behandeling);
					var behandelingSpan = document.createElement('span');
					behandelingSpan.setAttribute('class', 'behandelingSpan');
					behandelingSpan.innerHTML = behandeling.naam;

					var behandelingLengte = document.createElement('a');
					behandelingLengte.innerHTML = behandeling.lengte;
					behandelingSpan.appendChild(behandelingLengte);

					var behandelingPrijs = document.createElement('a');
					behandelingPrijs.innerHTML = "€ " + behandeling.prijs;
					behandelingSpan.appendChild(behandelingPrijs);

					behandelingenLijst.appendChild(behandelingSpan);
				}
				document.getElementById("sluitAfspraakInfo").addEventListener("click", function () { sluitAfspraakInfo() })
				document.getElementById("mailKlantAfspraak").addEventListener("click", function () {

				})
				document.getElementById("verwijderAfspraak").addEventListener("click", function () {
					verwijderAfspraak(id);
				})
			})

	})
}
function sluitAfspraakInfo() {
	document.getElementById("afspraakModal").style.display = "none";
	// klant info
	document.getElementById("naamInfo").innerHTML = "";
	document.getElementById("emailInfo").innerHTML = "";
	document.getElementById("geslachtInfo").innerHTML = "";
	document.getElementById("telefoonInfo").innerHTML = "";
	// afspraak info
	document.getElementById("prijsInfo").innerHTML = "";
	document.getElementById("lengteInfo").innerHTML = "";
	document.getElementById("datumInfo").innerHTML = "";
	document.getElementById("tijdInfo").innerHTML = "";
	// behandelingen
	behandelingenLijst.innerHTML = "";
}

function verwijderAfspraak(id) {
	var fetchoptions = {
		method: 'DELETE',
		headers: {
			'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
		}
	}
	console.log(id);
	// De wijziging wordt verstuurt naar de backend
	fetch("restservices/afspraak/deleteafspraak/"+id, fetchoptions)
		.then(function (response) {
			if (response.ok) {
				location.reload();
 			} else {
				errorMessage(response.status);
			}
		})}

document.getElementById("afspraakInplannen").addEventListener("click", function () {
	document.getElementById("inplannenModal").style.display = "block";
	document.getElementById("inplannenGeslacht").style.display = "block";

	inplannenGeslachtKiezen();
});
