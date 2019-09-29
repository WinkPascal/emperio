function onload(){
	datum = new Date();
	getRooster();
	getAfspraken(datum);
}

function getRooster(){ 
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/getWeekRooster", fetchoptions)
	.then(response => response.json())
	.then(function(dagen){
		var i =1;
		var weekdagen = new Array(7);
		weekdagen[0] = "Maandag";
		weekdagen[1] = "Dinsdag";
		weekdagen[2] = "Woensdag";
		weekdagen[3] = "Donderdag";
		weekdagen[4] = "Vrijdag";
		weekdagen[5] = "Zaterdag";
		weekdagen[6] = "Zondag";
		var aantalDagen = 0;

		beginUurRooster = 0;
		beginMinuutRooster =0;

		eindUurRooster = 0;
		eindMinuutRooster = 0;

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
				var weekDate = document.createElement('a');
				weekDate.setAttribute('id', "dagDate"+dag.weekNummer);
				weekDate.innerHTML = "dsa";
				
				topinfo.appendChild(weekNaam);
				topinfo.appendChild(weekDate);

				dagVanRooster.appendChild(topinfo);
				//events van de dag
				var events = document.createElement('ul');
				events.setAttribute('class', 'eventsDag');
				events.setAttribute('id', 'dag'+dag.weekNummer);

				//ochtend gesloten wordt aangemaakt
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
				var ochtend = createAfspraak(topOchtend, lengteOchtend, "ochtend", 0);
				events.appendChild(ochtend);

				//avond wordt aangemaakt
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
				var avond = createAfspraak(eindTijdDag, lengteAvond, "avond", "nbsb");
				events.appendChild(avond);

				dagVanRooster.appendChild(events);
				document.getElementById("rooster").appendChild(dagVanRooster);
			}

		}
		var dagen = document.getElementsByClassName('dag');
		var breedte = 100 / aantalDagen;
		for(m = 0; m < dagen.length; m++) {
			dagen[m].style.width = breedte +"%";
		}
	}).catch(function() {
		// De gebruiker is niet ingelogt
		alert("niet meer ingelogd");
	});
}


function createAfspraak(beginTijd ,lengte, innerhtml, soort){
	//event aanmaken
	var event = document.createElement('li');
	event.setAttribute('class', 'afspraak');
	if(soort == 0){
		// is gesloten
	} if(soort == 1){
		event.style.backgroundColor= "#34D77B";
	} if(soort == 2){
		event.style.backgroundColor= "#445DFF";
	} if(soort == 3){
		event.style.backgroundColor= "#B23636";
	}
	event.innerHTML = innerhtml;

	//begin punt van de afspraak
	var topArray = beginTijd.split(":");
	var uurTop = parseInt(topArray[0]);
	var minuutTop = parseInt(topArray[1]);
	console.log(uurTop,minuutTop);

	var minuutVerschilTop = minuutTop + uurTop*60;
	var minuutBegin = beginMinuutRooster+beginUurRooster*60;

	var topMinuten = minuutVerschilTop-minuutBegin;
	var a = 5/3;
	var top = topMinuten * a;	
	event.style.top = top+"px";


	//lengte van afspraak
	var lengteArray = lengte.split(":");
	var uurLengte = parseInt(lengteArray[0]);
	var minuutLengte = parseInt(lengteArray[1]);
	var minuutVerschilLengte = minuutLengte + uurLengte*60;

	var a = 5/3;
	var hoogte = minuutVerschilLengte * a;
	event.style.height = hoogte+"px";

	return event;
}

document.getElementById("weekTerug").addEventListener("click", function(){
	datum.setDate(datum.getDate() - 7);
	getAfspraken(datum);
});

document.getElementById("weekVerder").addEventListener("click", function(){
	datum.setDate(datum.getDate() + 7);
	getAfspraken(datum);
});

function getAfspraken(datum){
	var maandag = new Date(datum);
	var day = maandag.getDay(),
    diff = maandag.getDate() - day + (day == 0 ? -6:1);
	maandag.setDate(diff);
	var i = 0;
	while(i < 7){
		var dagDate = maandag.getFullYear() +'-'+ maandag.getMonth() +'-'+ maandag.getDate();
		document.getElementById("dagDate"+i).innerHTML = dagDate;
		maandag.setDate(maandag.getDate() + 1);
		i++;
	}
	
	var date = datum.getFullYear() +'-'+ datum.getMonth() +'-'+ datum.getDate();
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/getWeekAfspraken/" + date, fetchoptions)
	.then(response => response.json())
	.then(function(afspraken){
		for(let afspraak of afspraken){
			var uurLengte = 0;
			var minuutLente = 0;
			var behandelingen = "";
			var prijs = 0;
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
			var afspraakLengte = uurLengte+":"+minuutLente;
			var top = afspraak.timestamp.substring(16, 11);
			var klant = afspraak.klant;
			var afspraakText = "Klant: "+ klant +"<br> prijs: "+prijs +"<br> Begin tijd: "+ top +"<br> lengte: "+uurLengte+":"+minuutLente;
 			var event = createAfspraak(top, afspraakLengte, afspraakText, 1);
 			
 			var afspraakDate = new Date(afspraak.timestamp.substring(0, 10));
 			var dagNummer = afspraakDate.getDay() - 1;
 			var dagId = "dag"+dagNummer;
 			console.log(dagId);
 			document.getElementById(dagId).appendChild(event);
		}
	}).catch(function() {
		// De gebruiker is niet ingelogt
		alert("niet meer ingelogd");
	});
}