document.getElementById("volgendeInplannen").addEventListener("click", function() {
	volgendeKnopInplannen();
})

// de knop om terug te gaanbij het inplannen
document.getElementById("terugInplannen").addEventListener("click", function() {
	terugKnopInplannen();
})

function inplannenGeslachtKiezen(){
	//wordt gebruikt bij het navigeren van de volgende en terug knop
	status = "behandeling"
	behandelingen = [];
	// geslacht word gekozen
	document.getElementById("geslachtMan").addEventListener("click", function() {
		document.getElementById("geslachtMan").style.background = "blue";
		behandelingenOphalen("man");
	})
	document.getElementById("geslachtVrouw").addEventListener("click", function() {
		document.getElementById("geslachtVrouw").style.background = "blue";
		behandelingenOphalen("vrouw");
	})
	document.getElementById("geslachtJongen").addEventListener("click", function() {
		document.getElementById("geslachtJongen").style.background = "blue";
		behandelingenOphalen("jongen");
	})
	document.getElementById("geslachtMeisje").addEventListener("click", function() {
		document.getElementById("geslachtMeisje").style.background = "blue"
		behandelingenOphalen("meisje");
	})
}

// de behandelingen beschikbaar voor het gekozen geslacht word opgehaalt
function behandelingenOphalen(geslacht){
	document.getElementById("inplanFormGeslacht").innerHTML = geslacht;
	inplanVoorbereiding("geslacht", geslacht);		
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/behandelingen/" + geslacht, fetchoptions)
	.then(response => response.json())
	.then(function(behandelingen){
		var behandelingKeuzes = document.getElementById("behandelingenKeuzeLijst");
		behandelingKeuzes.style.display = "block";
			for (let behandeling of behandelingen){
			var behandelingsDiv = document.createElement('div');
			behandelingsDiv.setAttribute('id', behandeling.id);
			behandelingsDiv.setAttribute('class', 'behandelingKeuze');
			
			var behandelingsNaam = document.createElement('div');
			var behandelingsBeschrijving = document.createElement('div');
			var behandelingsLengte = document.createElement('div');
			var behandelingsPrijs = document.createElement('div');
				
			behandelingsNaam.innerHTML = behandeling.naam;
			behandelingsBeschrijving.innerHTML = behandeling.beschrijving;
			behandelingsLengte.innerHTML = behandeling.tijd;
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
				behandelingsDiv.style.background = "blue"
				var gekozenBehandelingsSpan = document.createElement('span');
				gekozenBehandelingsSpan.setAttribute('id', behandeling.id);
				gekozenBehandelingsSpan.setAttribute('class', 'navigatieButton');
					
				inplanVoorbereiding("behandelingToevoegen", behandeling.id);
				
				gekozenBehandelingsSpan.innerHTML = behandeling.naam;
				document.getElementById("inplanFormBehandeling").appendChild(gekozenBehandelingsSpan);
			})
			behandelingKeuzes.appendChild(behandelingsDiv);
		}			
	}).catch(function() {
		// De gebruiker is niet ingelogt
	});
}


function inplanVoorbereiding(state, value){
	if(state == "geslacht"){
		afspraakKlantGeslacht = value;
	} else if(state == "behandelingToevoegen"){
		behandelingen.push(value);
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
		console.log("naam " +afspraakKlantNaam);
		console.log("geslacht " +afspraakKlantGeslacht);
		console.log("email " +afspraakKlantEmail);
		
		console.log("behandelingen " + JSON.stringify(behandelingen));
		console.log("tel " +afspraakKlantTel);
		console.log("tijd " +afspraakTijd);
		console.log("Datum " +afspraakDatum);

		var formData = new FormData();
		formData.append("afspraakKlantNaam", afspraakKlantNaam);
		formData.append("afspraakKlantGeslacht", afspraakKlantGeslacht);
		formData.append("afspraakKlantEmail", afspraakKlantEmail);
		formData.append("afspraakKlantTel", afspraakKlantTel);
		
		formData.append("afspraakBehandeling", JSON.stringify(behandelingen));
		
		formData.append("afspraakTijd", afspraakTijd);
		formData.append("afspraakDatum", afspraakDatum);
		var encData = new URLSearchParams(formData.entries());
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

	
