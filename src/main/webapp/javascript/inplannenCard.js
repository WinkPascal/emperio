document.getElementById("volgendeInplannen").addEventListener("click", function() {
	volgendeKnopInplannen();
})
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
		document.getElementById("inplannenDatum").style.display = "none";
		var inplannenForm = document.getElementById("inplannenForm");
		inplannenForm.style.width = "800px";
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

document.getElementById("behandelingenOptions").addEventListener("click", function() {
	alert();  
	var popup = document.getElementById("myPopup");
	popup.classList.toggle("show");
})

//de behandelingen voor het gekozen geslacht worden opgehaalt
function behandelingenOphalen(geslacht){

	
	afspraakKlantGeslacht = '';
	behandelingenLijst = [];
	afspraakDatum = '';
	
	prijs = 0;
	uren = 0;
	minuten = 0;
		
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
				
				document.getElementById("prijsAfspraakForm").innerHTML = "€ "+ prijs;
				
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
					
					document.getElementById("prijsAfspraakForm").innerHTML = "€ "+ prijs;
					document.getElementById("lengteAfspraakForm").innerHTML = uren +":"+ minuten;
				})
				//behandeling toevoegen aan inplanForm
				document.getElementById("inplanBehandelingenForm").appendChild(gekozenBehandelingsSpan);

			})
			//voeg toe aan keuzelijst
			document.getElementById("behandelingenKeuzeLijst").appendChild(behandelingsDiv);
		}			
	}).catch(function() {
		// De gebruiker is niet ingelogt
	});
}
//de datum en tijden worden opgehaalt
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
		var date = datum.getFullYear() +'-'+ datum.getMonth() +'-'+ datum.getDate();
		inplanVoorbereiding("datum",date);
		document.getElementById("datumInplanForm").innerHTML = date;

		var fetchoptions = {
				headers: {
					'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
				}
			}
		fetch("restservices/service/tijdslotenOphalen/"+date, fetchoptions)
		.then(response => response.json())
		.then(function(afspraken){
			var i = 0;
			
			var openingsTijdUur = null;
			var openingsTijdMinuut = null;
			
			var sluitingsTijdUur = null;
			var sluitingsTijdMinuut = null;
			
			var beginUren = [];
			var beginMinuten = [];
			var eindUren = [];
			var eindMinuten = [];
			
			//variableen worden toegewezen
			var tijdsloten = document.getElementById("inplannenDatum");
			for(let afspraak of afspraken){
				if(i == 0){
					i++;
					openingsTijdUur = afspraak.openingsTijdUur;
					openingsTijdMinuut = afspraak.openingsTijdMinuut;					
					
					sluitingsTijdUur = afspraak.sluitingsTijdUur;
					sluitingsTijdMinuut = afspraak.sluitingsTijdMinuut;
				} else{
					beginUren.push(afspraak.beginUur);
					beginMinuten.push(afspraak.beginMinuut);
					eindUren.push(afspraak.eindUur);
					eindMinuten.push(afspraak.eindMinuut);
				}
			}
			
			var afspraak = 0;
			//tijdsloten worden aan gemaakt
			while(true){
				if(openingsTijdUur == beginUren[afspraak]){
					//het uur van een afspraak komt overeenn met het uur van tijdslot				
					var afspraakTijdSpan = document.createElement('span');
					afspraakTijdSpan.setAttribute('class', 'afspraakTijdslot');
					afspraakTijdSpan.style.background ='red';
					afspraakTijdSpan.innerHTML= beginUren[afspraak] +":"+beginMinuten[afspraak] 
					+" tot "+eindUren[afspraak] +":"+eindMinuten[afspraak];
					tijdsloten.appendChild(afspraakTijdSpan);
					
					afspraak ++;					
				}
				//geen geplande afspraken op dit uur
				var afspraakTijdSpan = document.createElement('span');
				afspraakTijdSpan.setAttribute('class', 'afspraakTijdslot');
				afspraakTijdSpan.innerHTML= openingsTijdUur +":"+openingsTijdMinuut;
				
				var id = openingsTijdUur +":"+openingsTijdMinuut;
				
				afspraakTijdSpan.setAttribute('id', id);
				tijdsloten.appendChild(afspraakTijdSpan);
				
				setEventListener(id);
				
				
				// uur erbij
				openingsTijdUur = openingsTijdUur + 1;
				
				//stopt als de tijdsloten bij de sluitingstijd zijn
				if(openingsTijdUur == sluitingsTijdUur){
					break;
				}
				function setEventListener(id){
					document.getElementById(id).addEventListener("click", function(){											
						var tijdSlot = document.getElementById(id).innerHTML;
						var nieuw=tijdSlot.split(":");
						var tijdslotUur = parseInt(nieuw[0]);
						var tijdslotMinuut = parseInt(nieuw[1]);
						
						document.getElementById("inplannenDatum").innerHTML = "";
						
						while(tijdslotMinuut<59){
							var afspraakTijdMinuutSpan = document.createElement('span');
							afspraakTijdMinuutSpan.setAttribute('class', 'afspraakTijdslot');

							var tijdslotUurEind = tijdslotUur + uren;
							var tijdslotMinuutEind = tijdslotMinuut +minuten;
							if(tijdslotMinuutEind > 59){
								tijdslotMinuutEind =tijdslotMinuutEind-60;
								tijdslotUurEind = tijdslotUurEind+1;
							}
							var tijdslotUurMetMinuut = tijdslotUur +":"+ tijdslotMinuut;
							afspraakTijdMinuutSpan.innerHTML = tijdslotUurMetMinuut +" tot "+ tijdslotUurEind +":"+ tijdslotMinuutEind;
							
							afspraakTijdMinuutSpan.setAttribute('id', tijdslotUurMetMinuut);
							
							document.getElementById("inplannenDatum").appendChild(afspraakTijdMinuutSpan);
							
							setEventListenerTijd(tijdslotUurMetMinuut);
							
							tijdslotMinuut = tijdslotMinuut +10;
						}
					})
				}
				function setEventListenerTijd(id){
					document.getElementById(id).addEventListener("click", function(){
						inplanVoorbereiding("tijd", id);
						document.getElementById("tijdInplanForm").innerHTML = id;
					})
				}
			}
		})
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
	} else if(state == "tijd"){
		afspraakTijd = value;
	} else if(state = "inplannen"){

		var tijdInplannen = uren +":"+minuten;

		inplannen(afspraakKlantGeslacht, behandelingenLijst, afspraakTijd, afspraakDatum);
	}
}

document.getElementById("submitInplanFormButton").addEventListener("click", function(){
	inplanVoorbereiding("inplannen","");
})

function inplannen(geslacht, behandelingenlijst, tijd, datum){
	var formData = new FormData(document.getElementById("inplannenForm"));
	
	formData.append("klantGeslacht", geslacht);
	formData.append("afspraakBehandeling", JSON.stringify(behandelingenlijst));
	formData.append("afspraakTijd", tijd);
	formData.append("afspraakDatum", datum);

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


//hier worden behandelingen toegevoegd
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

//hulphulphulphulphulphulphulphulphulphulphulphulphulphulphulphulp
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