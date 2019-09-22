document.getElementById("volgendeInplannen").addEventListener("click", function() {
	volgendeKnopInplannen();
})

// de knop om terug te gaanbij het inplannen
document.getElementById("terugInplannen").addEventListener("click", function() {
	terugKnopInplannen();
})

document.getElementById("inplannenBehnadelingToevoegen").addEventListener("click", function(){
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
	
	document.getElementById("inplannenBehandelingToevoegen").addEventListener("click", function(){
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
				alert("fout");
			}
		})
	})
})


function tijdslotenOphalen(){
	var date = new Date();
	datumsOphalen(date);
	var week = 7 * 24 * 60 * 60 * 1000;
	var dag = 24 * 60 * 60 * 1000;
	document.getElementById("weekVerderInplannen").addEventListener("click", function(){
		date.setTime(date.getTime() + week);
		datumsOphalen(date);
	})

	document.getElementById("weekTerugInplannen").addEventListener("click", function(){
		date.setTime(date.getTime() - week);
		datumsOphalen(date);
	})
	
	var dateSpanMaandag = document.getElementById("dateSpanMaandag");
	var dateSpanDinsdag = document.getElementById("dateSpanDinsdag");
	var dateSpanWoensdag=document.getElementById("dateSpanWoensdag");
	var dateSpanDonderdag= document.getElementById("dateSpanDonderdag");
	var dateSpanVrijdag=document.getElementById("dateSpanVrijdag");
	var dateSpanZaterdag=document.getElementById("dateSpanZaterdag");
	var dateSpanZondag=document.getElementById("dateSpanZondag");
	
	dateSpanMaandag.addEventListener("click", function(){
		hideDagen("dateSpanMaandag");

		var costumDate = getMaandag(date);
		date.setTime(costumDate.getTime() + dag*0);
		ophalen(date);
	})
	dateSpanDinsdag.addEventListener("click", function(){
		hideDagen("dateSpanDinsdag");
		var costumDate = getMaandag(date);
		date.setTime(costumDate.getTime() + dag*1);
		ophalen(date);
	})
	dateSpanWoensdag.addEventListener("click", function(){
		hideDagen("dateSpanWoensdag");
		var costumDate = getMaandag(date);
		date.setTime(costumDate.getTime() + dag*2);
		ophalen(date);
	})
	dateSpanDonderdag.addEventListener("click", function(){
		hideDagen("dateSpanDonderdag");
		var costumDate = getMaandag(date);
		date.setTime(costumDate.getTime() + dag*3);
		ophalen(date);
	})
	dateSpanVrijdag.addEventListener("click", function(){
		hideDagen("dateSpanVrijdag");
		var costumDate = getMaandag(date);
		date.setTime(costumDate.getTime() + dag*4);
		ophalen(date);
	})
	dateSpanZaterdag.addEventListener("click", function(){
		hideDagen("dateSpanZaterdag");
		var costumDate = getMaandag(date);
		date.setTime(costumDate.getTime() + dag*5);
		ophalen(date);
	})
	dateSpanZondag.addEventListener("click", function(){
		hideDagen("dateSpanZondag");
		var costumDate = getMaandag(date);
		date.setTime(costumDate.getTime() + dag*6);
		ophalen(date);
	})
	function ophalen(datum){
		alert("dssssssssssss");
		var date = datum.getFullYear() +'-'+ datum.getMonth() +'-'+ datum.getDate();
		console.log(date);
		var fetchoptions = {
				headers: {
					'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
				}
			}
		fetch("restservices/service/tijdslotenOphalen/"+date, fetchoptions)
		.then(response => response.json())
		.then(function(afspraken){
			var i = 0;
			var openingstijd = null;
			var sluitngstijd = null;
			var tijdsloten = document.getElementById("inplannenDatum");
			for(let afspraak of afspraken){
				if(i == 0){
					openingstijd=afspraak.openingsTijd;
					sluitngstijd=afspraak.sluitingstijd;
					var afspraakTijdSpan = document.createElement('span');
					afspraakTijdSpan.setAttribute('class', 'afspraakTijdslot');
					afspraakTijdSpan.innerHTML= afspraak.openingsTijd;
					
					tijdsloten.appendChild(afspraakTijdSpan);					
				} else{
					afspraak.timestamp;
					afspraak.lengte;
				}
			}
		})
	}
}

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

function datumsOphalen(d){

	d = new Date(getMaandag(d));
	
	document.getElementById("dateMaandag").innerHTML = formatDate(d);
	d.setDate(d.getDate() + 1)
	document.getElementById("dateDinsdag").innerHTML = formatDate(d);
	d.setDate(d.getDate() + 1)
	document.getElementById("dateWoensdag").innerHTML = formatDate(d);
	d.setDate(d.getDate() + 1)
	document.getElementById("dateDonderdag").innerHTML = formatDate(d);
	d.setDate(d.getDate() + 1)
	document.getElementById("dateVrijdag").innerHTML = formatDate(d);
	d.setDate(d.getDate() + 1)
	document.getElementById("dateZaterdag").innerHTML = formatDate(d);
	d.setDate(d.getDate() + 1)
	document.getElementById("dateZondag").innerHTML = formatDate(d);
}

function formatDate(date){

	var date = new Date(date);
	var monthNames = [
		"January", "February", "March",
		"April", "May", "June", "July",
		"August", "September", "October",
		"November", "December"
	];
	var day = date.getDate();
	var monthIndex = date.getMonth();
	var year = date.getFullYear();
	return day + ' ' + monthNames[monthIndex] + ' ' + year;
}

function inplannenGeslachtKiezen(){
	//wordt gebruikt bij het navigeren van de volgende en terug knop
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

// de behandelingen beschikbaar voor het gekozen geslacht word opgehaalt
function behandelingenOphalen(geslacht){

	behandelingenLijst = [];
	prijs = 0;
	lengte = 0;
	
	document.getElementById("inplanGeslachtOverzicht").innerHTML = geslacht;
	inplanVoorbereiding("geslacht", geslacht);	
	var behandelingKeuzes = document.getElementById("behandelingenKeuzeLijst");
	behandelingKeuzes.innerHTML = "";
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/behandelingen/" + geslacht, fetchoptions)
	.then(response => response.json())
	.then(function(behandelingen){
		behandelingKeuzes.style.display = "block";
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
				inplanVoorbereiding("tijdToevoegen", behandeling.lengte);
				inplanVoorbereiding("prijsToevoegen", behandeling.prijs);
				inplanVoorbereiding("behandelingToevoegen", behandeling.id);
				
				var gekozenBehandelingsSpan = document.createElement('span');
				gekozenBehandelingsSpan.setAttribute('id', behandeling.id);
				gekozenBehandelingsSpan.setAttribute('class', 'inplannenBehandelingOverzicht');
				
				var textGekozenBehandeling = document.createElement('a');
				textGekozenBehandeling.setAttribute('class', "textGekozenBehandeling");
				textGekozenBehandeling.innerHTML = behandeling.naam;
				gekozenBehandelingsSpan.appendChild(textGekozenBehandeling);
				
				document.getElementById("prijsAfspraak").innerHTML = prijs;
				document.getElementById("lengteAfspraak").innerHTML = lengte;
				
				gekozenBehandelingsSpan.addEventListener("click", function(){
					removeItem(behandeling.id);
					showItem("d"+behandeling.id);
					inplanVoorbereiding("tijdVerwijderen", behandeling.lengte);
					inplanVoorbereiding("prijsVerwijderen", behandeling.prijs);
					document.getElementById("prijsAfspraak").innerHTML = prijs;
					document.getElementById("lengteAfspraak").innerHTML = lengte;
				})
				
				document.getElementById("inplanBehandelingenOverzicht").appendChild(gekozenBehandelingsSpan);
			})
			behandelingKeuzes.appendChild(behandelingsDiv);
		}			
	}).catch(function() {
		// De gebruiker is niet ingelogt
	});
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

function inplanVoorbereiding(state, value){

	if(state == "geslacht"){
		afspraakKlantGeslacht = value;
	} else if(state == "behandelingToevoegen"){
		behandelingenLijst.push(value);
	} else if(state == "behandelingVerwijderen"){
		alert();
	} else if(state == "uurToevoegen"){
		lengte = lengte + value;
	} else if(state == "prijsToevoegen"){
		prijs = prijs + value;
	} else if(state == "tijdVerwijderen"){
		lengte = lengte - value;
	} else if(state == "prijsVerwijderen"){
		prijs = prijs - value;
	} else if(state == "datum"){
		afspraakDatum=value;
	} else if(state == "tijd"){
		afspraakTijd =value;
	} else if(state == "naam"){
		afspraakKlantNaam = value;
	} else if(state == "email"){
		afspraakKlantEmail = value;
	} else if(state == "telefoon"){
		afspraakKlantTel = value;
	} else if(state=="inplannen"){
		
		var formData = new FormData();
		formData.append("afspraakKlantNaam", afspraakKlantNaam);
		formData.append("afspraakKlantGeslacht", afspraakKlantGeslacht);
		formData.append("afspraakKlantEmail", afspraakKlantEmail);
		formData.append("afspraakKlantTel", afspraakKlantTel);
		
		formData.append("afspraakBehandeling", JSON.stringify(behandelingenLijst));
		
		formData.append("afspraakTijd", afspraakTijd);
		formData.append("afspraakDatum", afspraakDatum);
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
				alert("ingeplands");
			} else{
				alert("fout");
			}
		})
	}
}

// de knop om naar de volgende stap te gaan bij het inplannen
function volgendeKnopInplannen(){	
	// geslacht en behandelingen zijn ingevuld
	if (status == "behandeling"){
		// het geslacht en behandeling is ingevuld
		status = "datum";
		document.getElementById("inplannenDatum").style.display = "block";
		document.getElementById("inplannenGeslacht").style.display = "none";
		document.getElementById("behandelingenKeuzeLijst").style.display = "none";
		
		tijdslotenOphalen();
	} else if(status == "datum"){
		// de datum is ingevuld
		inplanVoorbereiding("datum", document.getElementById("datumInplannen").value);

		status = "tijd";
		document.getElementById("inplanFormDatum").innerHTML = document.getElementById("datumInplannen").value;
		document.getElementById("inplannenDatum").style.display = "none";
		document.getElementById("inplannenTijd").style.display = "block";
		afspraakDatum = document.getElementById("datumInplannen").value;
		document.getElementById("inplannenTijdKeuze").addEventListener("click", function() {
			document.getElementById("inplannenTijdKeuze").style.background = "blue";
			inplanVoorbereiding("tijd", "9:00");
		})
	} else if(status == "tijd"){
		// de tijd is ingevuld
		document.getElementById("inplanFormDatum").innerHTML = document.getElementById("inplannenTijdKeuze").value;
		status = "klantenInfo";
		document.getElementById("volgendeInplannen").value = "maak afspraak";
		document.getElementById("inplannenTijd").style.display = "none";
		document.getElementById("inplannenKlantInfo").style.display = "block";
		status = "klantenInfo";				
	} else if(status == "klantenInfo"){
		inplanVoorbereiding("naam", document.getElementById("inplannenKlantNaam").value);
		inplanVoorbereiding("email", document.getElementById("inplannenKlantEmail").value);
		inplanVoorbereiding("telefoon", document.getElementById("inplannenKlantTel").value);
		inplanVoorbereiding("inplannen");
	}  
}

function terugKnopInplannen(){
	var terugButton = document.getElementById("terugInplannen");
	if(status == "behandeling"){
		document.getElementById("inplannenGeslacht").style.display = "none";
		document.getElementById("behandelingKeuze").style.display = "none";
		document.getElementById("inplannenModal").style.display = "none";
		
	}
	if(status == "datum"){
		// het geslacht en behandeling is ingevuld
		status = "behandeling"
		
		document.getElementById("inplannenDatum").style.display = "none";
		document.getElementById("inplannenGeslacht").style.display = "block";
		document.getElementById("behandelingenKeuzeLijst").style.display = "block";

	} else if(status == "tijd"){
		status = "datum";

		document.getElementById("inplannenDatum").style.display = "block";
		document.getElementById("inplannenTijd").style.display = "none";
		// de datum is ingevuld
		
	} else if(status == "klantInfo"){
		status = "tijd";

		// de tijd is ingevuld
		document.getElementById("inplannenTijd").style.display = "block";
		document.getElementById("inplannenKlantInfo").style.display = "none";
	}
}

	
