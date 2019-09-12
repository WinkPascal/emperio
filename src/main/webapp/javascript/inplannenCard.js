var afspraakBehandelingen;
var fruits = ["Banana", "Orange", "Apple", "Mango"];

function inplannenGeslachtKiezen(){


	
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

function volgendeKnopInplannen(){

	//variabelen voor de afspraak
	// klant
	var afspraakKlantNaam;
	var afspraakKlantGeslacht;
	var afspraakKlantEmail;
	var afspraakKlantTel;
	// behandeling

	// afspraak
	var afspraakTijd;
	var afspraakDatum;
	
	
	//eerste stap is afgerond
	//geslacht en behandelingen zijn ingevuld
	if(status == "datum"){
		if(document.getElementById("datumInplannen").value ==""){
			alert("vul een datum in");
		} else{
			// de datum is ingevuld
			status = "tijd";
			document.getElementById("inplanFormDatum").innerHTML = document.getElementById("datumInplannen").value;

			document.getElementById("inplannenDatum").style.display = "none";
			document.getElementById("inplannenTijd").style.display = "block";
			afspraakDatum = document.getElementById("datumInplannen").value;
			document.getElementById("inplannenTijdKeuze").addEventListener("click", function() {
				document.getElementById("inplannenTijdKeuze").style.background = "blue";
				afspraakTijd = "9:00";
			})
		}
	} else if(status == "tijd"){
		
		if(afspraakTijd == null){
			alert("vul een tijd in");
		} else{
				// de tijd is ingevuld
			document.getElementById("inplanFormDatum").innerHTML = document.getElementById("inplannenTijdKeuze").value;
			status = "klantenInfo";
			document.getElementById("volgendeInplannen").value = "maak afspraak";
			document.getElementById("inplannenTijd").style.display = "none";
			document.getElementById("inplannenKlantInfo").style.display = "block";
			
			status = "klantenInfo";	
		}			
	} else if(status == "klantenInfo"){
		if(document.getElementById("inplannenKlantNaam").value == ""){
			alert("niet alles ingevuld");
		} else{

		}
	} else{
		if(document.getElementById("inplanFormGeslacht").value != undefined){
			// het geslacht en behandeling is ingevuld
			status = "datum";
			document.getElementById("inplannenDatum").style.display = "block";
			document.getElementById("inplannenGeslacht").style.display = "none";
			document.getElementById("behandelingenKeuzeLijst").style.display = "none";	
		} else{
			alert("vul een geslacht en behandeling in");
		}
	} 
}

function terugKnopInplannen(){
	var terugButton = document.getElementById("terugInplannen");
	
	if(status == "datum"){
		// het geslacht en behandeling is ingevuld
		status = "behandeling"
		document.getElementById("inplannenForm").style.display = "none";
		document.getElementById("inplannenDatum").style.display = "none";
		document.getElementById("inplannenGeslacht").style.display = "block";
		document.getElementById("behandelingenKeuzeLijst").style.display = "block";

	} else if(status == "tijd"){
		document.getElementById("inplannenDatum").style.display = "block";
		document.getElementById("inplannenTijd").style.display = "none";
		// de datum is ingevuld
		status = "datum";
		
	} else if(status == "klantInfo"){
		// de tijd is ingevuld
		volgende.value = "volgende";
		document.getElementById("inplannenTijd").style.display = "block";
		document.getElementById("inplannenKlantInfo").style.display = "none";
		status = "tijd";
	} else if(status == "klantenInfo"){
		// De Klanteninformatie is genoteerd de afspraak kan gemaakt
		// worden
		afspraakKlantNaam
	} else{
		document.getElementById("inplannenGeslacht").style.display = "none";
		document.getElementById("behandelingKeuze").style.display = "none";
		document.getElementById("inplannenModal").style.display = "none";
	}
}

function inplannenAfspraak(){
	afspraakKlantNaam = document.getElementById("inplannenKlantNaam").value;
	afspraakKlantEmail = document.getElementById("inplannenKlantEmail").value;
	afspraakKlantTel = document.getElementById("inplannenKlantTel").value;
	alert("word ingeplant");
	
	var formData = new FormData();
	formData.append("afspraakKlantNaam", afspraakKlantNaam);
	formData.append("afspraakKlantGeslacht", afspraakKlantGeslacht);
	formData.append("afspraakKlantEmail", afspraakKlantEmail);
	formData.append("afspraakKlantTel", afspraakKlantTel);
	formData.append("afspraakBehandeling", afspraakBehandeling);
	
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
	//De wijziging wordt verstuurt naar de backend
	fetch("restservices/service/afspraak", fetchoptions)
	.then(function (response){
		if(response.ok){
			alert("ingepland");
			
		} else{
			alert("fout");
		}
	})
}
	
function behandelingenOphalen(geslacht){

	document.getElementById("inplanFormGeslacht").value = geslacht;
	afspraakKlantGeslacht = geslacht;
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
				
				//var behandelingenSession = [];
				//behandelingenSession = JSON.parse(sessionStorage.getItem("behandelingen"));
				//behandelingenSession.push(behandeling.id);
				//alert(behandelingenSession);
				//sessionStorage.setItem("behandelingen", JSON.stringify(behandelingenSession))				
				
				gekozenBehandelingsSpan.innerHTML = behandeling.naam;
				document.getElementById("inplanFormBehandeling").appendChild(gekozenBehandelingsSpan);
			})
			behandelingKeuzes.appendChild(behandelingsDiv);
		}			
	}).catch(function() {
		// De gebruiker is niet ingelogt
	});
}