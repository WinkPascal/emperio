function onload(){
	datum = new Date();
	getRooster();

}

function createAfspraakRooster(beginTijd ,lengte, klant, prijs){
	var event = document.createElement('li');
	event.setAttribute('class', 'afspraak');
	
	var klasse1 = localStorage.getItem("klasse1");
	var klasse2 = localStorage.getItem("klasse2");
	var klasse3 = localStorage.getItem("klasse3");

	var beginMinuutRooster = parseInt(sessionStorage.getItem('beginMinuutRooster'));
	var beginUurRooster = parseInt(sessionStorage.getItem('beginUurRooster'));
	
	if(prijs <  klasse1 && prijs > 0){
		event.style.backgroundColor= getComputedStyle(document.documentElement)
	    .getPropertyValue("--afspraakKlasse1");
	} 
	if(prijs < klasse2){
		event.style.backgroundColor= getComputedStyle(document.documentElement)
	    .getPropertyValue("--afspraakKlasse2");
	} 
	if(prijs > klasse3){
		event.style.backgroundColor= getComputedStyle(document.documentElement)
	    .getPropertyValue("--afspraakKlasse3");
	} 
	if(prijs == 0){
		event.style.backgroundColor= getComputedStyle(document.documentElement)
	    .getPropertyValue('--afspraakKlasse0');
	} 
	var afspraakText = 
		 "Klant: "+ klant +
	"<br> prijs: "+prijs +
	"<br> Begin tijd: "+ beginTijd +
	"<br> lengte: "+lengte;
	event.innerHTML = afspraakText;

	// begin punt van de afspraak
	var topArray = beginTijd.split(":");
	var uurTop = parseInt(topArray[0]);
	var minuutTop = parseInt(topArray[1]);

	var minuutVerschilTop = minuutTop + uurTop*60;
	var minuutBegin = beginMinuutRooster + beginUurRooster*60;

	var topMinuten = minuutVerschilTop-minuutBegin;
	var a = 5/3;
	var top = topMinuten * a;
	event.style.top = top+"px";

	// lengte van afspraak
	var lengteArray = lengte.split(":");
	var uurLengte = parseInt(lengteArray[0]);
	var minuutLengte = parseInt(lengteArray[1]);
	var minuutVerschilLengte = minuutLengte + uurLengte*60;

	var a = 5/3;
	var hoogte = minuutVerschilLengte * a;
	if(hoogte == 0){
		return null;
	}
	event.style.height = hoogte+"px";

	return event;
}

function getRooster(){ 
	afsprakenLijst = [];
	dagenLijst = [];
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/afspraak/getWeekRooster", fetchoptions)
	.then(response => response.json())
	.then(function(dagen){
		var i =1;
		var weekdagen = new Array(7);
		weekdagen[0] = "Zondag";
		weekdagen[1] = "Maandag";
		weekdagen[2] = "Dinsdag";
		weekdagen[3] = "Woensdag";
		weekdagen[4] = "Donderdag";
		weekdagen[5] = "Vrijdag";
		weekdagen[6] = "Zaterdag";
		this.aantalDagen = 0;

		var eindUurRooster = 0;
		var eindMinuutRooster = 0;

		for(let dag of dagen){
			if(i == 1){
				// timeline wordt eerst aangemaakt
				var beginTijd= dag.vroegsteOpeningsTijd.split(":");
				beginUur = parseInt(beginTijd[0]);
				beginUurRooster = beginUur;
				beginMinuut = parseInt(beginTijd[1]);
				beginMinuutRooster = beginMinuut;

				var eindTijd = dag.laatsteSluitingsTijd.split(":");
				eindUur = parseInt(eindTijd[0]);
				eindUurRooster =eindUur;
				eindMinuut = parseInt(eindTijd[1]);
				eindMinuutRooster=eindMinuut;

				//wordt gebruikt bij het maken van de afspraken 
				sessionStorage.setItem('beginMinuutRooster', beginMinuutRooster);
				sessionStorage.setItem('beginUurRooster', beginUurRooster);
				
				var ul = document.createElement('ul');
				while(true){
					var timeLi = document.createElement('li');
					var timeSpan = document.createElement('span');

					timeSpan.innerHTML = beginUur +":"+ beginMinuut;

					timeLi.appendChild(timeSpan);
					ul.appendChild(timeLi);

					beginMinuut = beginMinuut + 30;
					if(beginMinuut > 59){
						beginUur = beginUur + 1;
						beginMinuut = beginMinuut - 60;
					}

					if(beginUur >= eindUur && beginMinuut >= eindMinuut){
						break;
					}
				}
				document.getElementById("timeline").appendChild(ul);
				i++;
			} else{
				aantalDagen ++;
				// er wordt een dag aangemaakt
				var dagVanRooster = document.createElement('li');
				dagVanRooster.setAttribute('class', 'dag');

				var topinfo = document.createElement('div');
				topinfo.setAttribute('class', 'top-info');

				var weekNaam = document.createElement('a');
				weekNaam.innerHTML= weekdagen[dag.weekNummer] +"<br>";
				
				// datums onder de dag zetten
				// get Date maandag van de week
				var maandag = new Date(datum);
				var day = maandag.getDay(),
			    diff = maandag.getDate() - day + (day == 0 ? -6:1);
				maandag.setDate(diff);
				maandag.setDate(maandag.getDate() + dag.weekNummer);

				var dagDate = formatDate(maandag);
				
				var weekDate = document.createElement('a');
				weekDate.setAttribute('id', "dagDate"+dag.weekNummer);
				weekDate.innerHTML = dagDate;
				topinfo.appendChild(weekNaam);
				topinfo.appendChild(weekDate);

				dagVanRooster.appendChild(topinfo);
				// events van de dag
				var events = document.createElement('ul');
				events.setAttribute('class', 'eventsDag');
				events.setAttribute('id', 'dag'+dag.weekNummer);

				// ochtend gesloten wordt aangemaakt
				var openingsTijdDag = dag.openingsTijd.split(":");
				var beginUurDag = parseInt(openingsTijdDag[0]);
				var beginMinuutDag = parseInt(openingsTijdDag[1]);

				var uurVerschilLengte = beginUurDag -beginUurRooster;
				var minuutVerschilLengte = beginMinuutDag - beginMinuutRooster;
				if(minuutVerschilLengte < 0){
					uurVerschilLengte = uurVerschilLengte - 1;
					minuutVerschilLengte = minuutVerschilLengte + 60;
				}
				var lengteOchtend = uurVerschilLengte+":"+minuutVerschilLengte;
				var topOchtend = beginUurRooster+":"+beginMinuutRooster;
				var ochtend = createAfspraakRooster(topOchtend, lengteOchtend, "ochtend", 0);
				if(ochtend != null){
					events.appendChild(ochtend);
				}

				// avond wordt aangemaakt
				var sluitingsTijdDag = dag.sluitingsTijd.split(":");
				var eindUurDag = parseInt(sluitingsTijdDag[0]);
				var eindMinuutDag = parseInt(sluitingsTijdDag[1]);
				var eindTijdDag = eindUurDag+":"+eindMinuutDag;

				var uurEindLengte = eindUurRooster - eindUurDag;
				var minuutEindLengte = eindMinuutRooster - eindMinuutDag;
				if(minuutEindLengte < 0){
					minuutEindLengte + 60;
					uurEindLengte = uurEindLengte -1;
				}
				var lengteAvond = uurEindLengte+":"+minuutEindLengte;
				var avond = createAfspraakRooster(eindTijdDag, lengteAvond, "avond", 0);
				if(avond != null){
					events.appendChild(avond);
				}
				dagVanRooster.appendChild(events);
				document.getElementById("rooster").appendChild(dagVanRooster);
			}
		}

		// maken breedte css
		var dagen = document.getElementsByClassName('dag');
		var breedte = 100 / aantalDagen;
		for(m = 0; m < dagen.length; m++) {
			dagen[m].style.width = breedte +"%";
		}
		document.getElementById("weekTerug").addEventListener("click", function(){
			datum.setDate(datum.getDate() - 7);
			getAfspraken(datum, false);
		});

		document.getElementById("weekVerder").addEventListener("click", function(){
			datum.setDate(datum.getDate() + 7);
			getAfspraken(datum, false);
		});
		// afspraken van huidige week ophalen
		getAfspraken(datum, true);
	})
}



function getAfspraken(datum, onload){	
	// alle afspraken worden uit het rooster gehaalt
	for(let afspraak of afsprakenLijst){
		afspraak.remove();
	}
	afsprakenLijst = [];
	var i = 0;

	// als het niet voor de eerste keer geladen wordt
	var maandag = new Date(datum);
	var day = maandag.getDay(),
    diff = maandag.getDate() - day + (day == 0 ? -6:1);
	maandag.setDate(diff);
	var date = maandag.getFullYear() +'-'+ maandag.getMonth() +'-'+ maandag.getDate();

	if(!onload){
		// datums onder de dag zetten
		// get Date maandag van de week
		while(i < 7){
			var dagDate = formatDate(maandag);
			document.getElementById("dagDate"+i).innerHTML = dagDate;
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
	.then(function(afspraken){
		for(let afspraak of afspraken){
			// variabalen initializen
			var uurLengte = 0;
			var minuutLente = 0;

			var behandelingen = "";
			var prijs = 0;
			
			// behandelingen ophalen
			for(let behandeling of afspraak.behandelingen){
				var lengteArray = behandeling.lengte.split(":");
				uurLengte = uurLengte+parseInt(lengteArray[0]);
				minuutLente = minuutLente+parseInt(lengteArray[1]);
				if(minuutLente>59){
					uurLengte = uurLengte + 1;
					minuutLente = minuutLente - 60;
				}
				prijs = prijs + behandeling.prijs;
				behandelingen = behandelingen + behandeling.naam;
			}
			// afpsraak attributen en afspraak aanmaken
			var afspraakLengte = uurLengte+":"+minuutLente;
			var top = afspraak.timestamp.substring(16, 11);
			var klant = afspraak.klant;

			
 			var event = createAfspraakRooster(top, afspraakLengte, klant, prijs);
 
 			event.setAttribute('id', afspraak.id);
 			afsprakenLijst.push(event);
			
 			// bepalen waar de afspraak wordt toegvoegdt
 			var afspraakDate = new Date(afspraak.timestamp.substring(0, 10));
 			var dagNummer = afspraakDate.getDay();
			var dagId = "dag"+dagNummer;

			document.getElementById(dagId).appendChild(event);

			createOnClickListener(afspraak.id);
		}
	    var elements = document.getElementsByClassName("afspraak");
	    var breedte = document.getElementsByClassName("dag")[0].offsetWidth;
	    for (var i = 0; i < elements.length; i++) {
	        elements[i].style.width=(breedte+"px");
	    }
	})
}

function createOnClickListener(id){
	document.getElementById(id).addEventListener("click", function(){
		document.getElementById("afspraakModal").style.display = "block";
		var fetchoptions = {
				headers: {
					'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
				}
			}
		fetch("restservices/afspraak/getAfspraak/" + id, fetchoptions)
		.then(response => response.json())
		.then(function(afspraak){
			// klant info
			document.getElementById("naamInfo").innerHTML = afspraak.klantNaam;
			document.getElementById("emailInfo").innerHTML = afspraak.klantEmail;
			document.getElementById("geslachtInfo").innerHTML = afspraak.klantGeslacht;
			document.getElementById("telefoonInfo").innerHTML = afspraak.klantTelefoon;
			document.getElementById("aantalAfsprakenInfo").innerHTML = afspraak.aantalAfspraken;
			document.getElementById("hoeveelheidInkomstenInfo").innerHTML = afspraak.hoeveelheidInkomsten;
			// afspraak info
			document.getElementById("prijsInfo").innerHTML = afspraak.totaalPrijs;
			document.getElementById("lengteInfo").innerHTML = afspraak.totaalLengte;
			document.getElementById("datumInfo").innerHTML = afspraak.datum;
			document.getElementById("tijdInfo").innerHTML = afspraak.tijd;		
			// behandelingen
			var behandelingenLijst = document.getElementById("behandelingenLijst");
			for(let behandeling of afspraak.behandelingen){
				var behandelingSpan = document.createElement('span');
				behandelingSpan.setAttribute('class', 'behandelingSpan');
				behandelingSpan.innerHTML = behandeling.naam + " " + behandeling.lengte;
				behandelingenLijst.appendChild(behandelingSpan);
			}
			document.getElementById("sluitAfspraakInfo").addEventListener("click", function(){
				document.getElementById("afspraakModal").style.display = "none";
				document.getElementById("naamInfo").innerHTML = "";
				document.getElementById("emailInfo").innerHTML = "";
				document.getElementById("geslachtInfo").innerHTML = "";
				document.getElementById("telefoonInfo").innerHTML = "";
				// afspraak info
				document.getElementById("prijsInfo").innerHTML = "";
				document.getElementById("lengteInfo").innerHTML = "";
				document.getElementById("datumInfo").innerHTML = "";
				document.getElementById("tijdInfo").innerHTML = "";
				behandelingenLijst.innerHTML="";
			})
			document.getElementById("mailKlantAfspraak").addEventListener("click", function(){
				
			})
		})
	})
}

document.getElementById("afspraakInplannen").addEventListener("click", function(){
	document.getElementById("inplannenModal").style.display = "block";
	document.getElementById("inplannenGeslacht").style.display = "block";

	inplannenGeslachtKiezen();
});
