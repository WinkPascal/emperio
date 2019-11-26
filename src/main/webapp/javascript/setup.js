function onload() {
	var pagina = "behandelingen";
	document.getElementById("behandelingen").style.display = "block"

	var volgendeButton = document.getElementById("volgendeStap");

	var stap1 = document.getElementById("behandelingenWizard");
	var stap2 = document.getElementById("instellingenWizard");
	var stap3 = document.getElementById("tijdenWizard");

	volgendeButton.addEventListener("click", function () {
		if (pagina == "behandelingen") {
			stap1.className = "done";
			stap2.className = "active";
			
			document.getElementById("behandelingen").style.display = "none"
			document.getElementById("afspraakSettingsDiv").style.display = "block"
			document.getElementById("vorigeStap").value = "Vorige";
			getInstellingen();

			pagina = "afspraakSettingsDiv";
		} else if (pagina == "afspraakSettingsDiv") {
			saveAfspraakSettings()
			var saved = true;
			if(saved){
				stap2.className = "done";
				stap3.className = "active";

				document.getElementById("afspraakSettingsDiv").style.display = "none"
				document.getElementById("dagenDiv").style.display = "block"

				volgendeButton.style.backgroundColor = "green";
				volgendeButton.value = "Start!"

				createTimePicker("openingsTijdMaandag");


				pagina = "dagenDiv";
			} else{
				errorMessage("niet alles ingevuld");
			}
		} else if (pagina == "dagenDiv") {
			saveDagen();
		}
			var checkBoxes = ["geslotenMaandag", "geslotenDinsdag", "geslotenWoensdag","geslotenDonderdag","geslotenVrijdag","geslotenZaterdag", "geslotenZondag"]
			var tijdenAreas = ["tijdenMaandag", "tijdenDinsdag", "tijdenWoensdag","tijdenDonderdag","tijdenVrijdag","tijdenZaterdag", "tijdenZondag"]
			for (i = 0; i < checkBoxes.length; i++) {
				checkboxChecker(tijdenAreas[i], checkBoxes[i]);
			}
			// controle
			//			location.href = 'home.html';
	})
	document.getElementById("vorigeStap").addEventListener("click", function () {
		if (pagina == "afspraakSettingsDiv") {
			stap1.className = "active";
			stap2.className = "";

			document.getElementById("behandelingen").style.display = "block"
			document.getElementById("afspraakSettingsDiv").style.display = "none"
			document.getElementById("vorigeStap").value = "Cancel";

			pagina = "behandelingen";
		} else if (pagina == "dagenDiv") {
			stap2.className = "active";
			stap3.className = "";

			document.getElementById("afspraakSettingsDiv").style.display = "block"
			document.getElementById("dagenDiv").style.display = "none"
			pagina = "afspraakSettingsDiv";
		}
	})
	loadBehandelingen();
	behandelingToevoegenForm();


	aanpassenTijdenHandlers();

	determineOpeningsTijd();
	determineSluitingsTijd()
}

function createTimePicker(id){
	document.addEventListener('DOMContentLoaded', function() {
		var elems = document.querySelectorAll('#'+id);
		var options = {twelveHour : false};
		var instances = M.Timepicker.init(elems, options);
	});
}


function getInstellingen(){
		var fetchoptions = {
		headers: {
			'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
		}
	}
	fetch("restservices/setup/getInstellingen", fetchoptions)
		.then(response => response.json())
		.then(function (instellingen) {
						checkByName("kleurKlasse1",instellingen.kleurKlasse1);
			document.getElementById("maxPrijs1").value= instellingen.maximumPrijsVanKlasse1;
			checkByName("kleurKlasse2",instellingen.kleurKlasse2);
			document.getElementById("maxPrijs2").value= instellingen.maximumPrijsVanKlasse2;
			checkByName("kleurKlasse3",instellingen.kleurKlasse3);
			
			document.getElementById("telefoonInvoerVeld").checked = instellingen.telefoonKlantInvoer;
			document.getElementById("emailInvoerVeld").checked = instellingen.emailKlantInvoer;
			document.getElementById("adresInvoerVeld").checked = instellingen.adresKlantInvoer;

			document.getElementById("bedrijfsEmail").checked = instellingen.bedrijfsEmail;
			document.getElementById("bedrijfsTelefoon").checked = instellingen.bedrijfsTelefoon;
			document.getElementById("bedrijfsAdres").checked = instellingen.bedrijfsAdres;
		})
	function checkByName(name, color){
	 var klasse =document.getElementsByName(name);
	for (i = 0; i < klasse.length; i++) {
		if (klasse[i].value == color) {
			klasse[i].checked = true;   
		}
	}
}

}

function loadBehandelingen() {
	var fetchoptions = {
		headers: {
			'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
		}
	}
	var behandelingenLijst = document.getElementById("behandelingenLijst");
	behandelingenLijst.innerHTML = "";
	behandelingenLijst.innerHTML = "";
	fetch("restservices/setup/getbehandelingen", fetchoptions)
		.then(response => response.json())
		.then(function (behandelingen) {
			for (let behandeling of behandelingen) {
				var behandelingNaam = document.createElement('span');
				behandelingNaam.innerHTML = behandeling.naam;
				behandelingNaam.setAttribute('class', 'behandelingNaam');

				var behandelingPrijs = document.createElement('a');
				behandelingPrijs.setAttribute('class', 'behandelingPrijs');
				behandelingPrijs.innerHTML = "€ "+ behandeling.prijs;

				var behandelingBeshrijving = document.createElement('span');
				behandelingBeshrijving.innerHTML = behandeling.beschrijving;
				behandelingBeshrijving.setAttribute('class', 'behandelingBeshrijving');

				var geslachtEnLengte = document.createElement('span');
				geslachtEnLengte.setAttribute('class', 'geslachtEnBehandeling');

				var behandelingGeslacht = document.createElement('span');
				behandelingGeslacht.innerHTML = "Doelgroep "+behandeling.geslacht;
				behandelingGeslacht.setAttribute('class', 'behandelingGeslacht');
				geslachtEnLengte.appendChild(behandelingGeslacht);

				var behandelingLengte = document.createElement('a');
				behandelingLengte.setAttribute('class', 'behandelingLengte');
				behandelingLengte.innerHTML = "Lengte: "+behandeling.lengte;
				geslachtEnLengte.appendChild(behandelingLengte);

				var behandelingDiv = document.createElement('div');
				behandelingDiv.setAttribute('id', behandeling.id);
				behandelingDiv.setAttribute('class', 'behandelingDiv');

				behandelingDiv.appendChild(behandelingNaam);
				behandelingDiv.appendChild(behandelingPrijs);
				behandelingDiv.appendChild(behandelingBeshrijving);
				behandelingDiv.appendChild(geslachtEnLengte);
				behandelingenLijst.appendChild(behandelingDiv);
			}
		})
}

function behandelingToevoegenForm() {
	var geslachten = [];

	document.getElementById("man").addEventListener("click", function () {
		geslachten = behandelingManager("man", geslachten);
	})
	document.getElementById("vrouw").addEventListener("click", function () {
		geslachten = behandelingManager("vrouw", geslachten);
	})
	document.getElementById("jongen").addEventListener("click", function () {
		geslachten = behandelingManager("jongen", geslachten);
	})
	document.getElementById("meisje").addEventListener("click", function () {
		geslachten = behandelingManager("meisje", geslachten);
	})
	document.getElementById("addBehandeling").addEventListener("click", function () {
		saveBehandeling(geslachten);
	})
}

function behandelingManager(geslacht, geslachten) {
	if (geslachten.includes(geslacht)) {
		for (var i = 0; i < geslachten.length; i++) {
			if (geslachten[i] === geslacht) {
				document.getElementById(geslacht).className = "geslachtButton";
				geslachten.splice(i, 1);
				i--;
			}
		}
	} else {
		geslachten.push(geslacht);
		document.getElementById(geslacht).className = "selectedGeslachtButtton";
	}
	return geslachten;
}

function saveBehandeling(geslachten) {
	if(geslachten < 1){
		errorMessage("Selecteer voor welke doelgroep de behandeling is.");
		return;
	}
	var formData = new FormData(document.querySelector("#BehandelingToevoegenForm"));
	formData.append("geslachten", JSON.stringify(geslachten));

	var encData = new URLSearchParams(formData.entries());
	var fetchoptions = {
		method: 'POST',
		body: encData,
		headers: {
			'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
		}
	}
	// De wijziging wordt verstuurt naar de backend
	fetch("restservices/setup/behandeling", fetchoptions)
		.then(function (response) {
			if (response.ok) {
				location.reload(); 
			} else {
           	 	console.log(response.status);
           	 	console.log(response.JSON.body);
			}
		})

}

 
function saveAfspraakSettings(){ 
	var formData = new FormData();

	var kleurKlasse1 = findCheckedByName("kleurKlasse1");
	formData.append("kleurKlasse1", kleurKlasse1);
	formData.append("tot1", document.getElementById("maxPrijs1").value);

	var kleurKlasse2 = findCheckedByName("kleurKlasse2");
	formData.append("kleurKlasse2", kleurKlasse2);
	formData.append("tot2", document.getElementById("maxPrijs2").value);

	var kleurKlasse3 = findCheckedByName("kleurKlasse3");
	formData.append("kleurKlasse3", kleurKlasse3);

	var telefoonKlant = document.getElementById("telefoonInvoerVeld").checked;
	formData.append("telefoonKlant", telefoonKlant);
	var emailKlant = document.getElementById("emailInvoerVeld").checked;
	formData.append("emailKlant", emailKlant);
	var adresKlant = document.getElementById("adresInvoerVeld").checked;
	formData.append("adresKlant", adresKlant);
	
	var emailBedrijfInput = document.getElementById("bedrijfsEmail").checked;
	formData.append("bedrijfsEmail", emailBedrijfInput);
	var telefoonBedrijfInput = document.getElementById("bedrijfsTelefoon").checked;
	formData.append("bedrijfsTelefoon", telefoonBedrijfInput);
	var adresBedrijfInput = document.getElementById("bedrijfsAdres").checked;
	formData.append("bedrijfsAdres", adresBedrijfInput);

	var encData = new URLSearchParams(formData.entries());
	var fetchoptions = {
		method: 'POST',
		body: encData,
		headers: {
			'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
		}
	}
	// De wijziging wordt verstuurt naar de backend
	fetch("restservices/setup/afspraakSettings", fetchoptions)
	.then(function (response){
		if(response.ok){
			succesMessage("De klanten pagina is gewijzigt");
			return true;
		} else{
			errorMessage(response.status);
			return false;
		}
	});
}

function findCheckedByName(name){
	 var kleurKlasse1 =document.getElementsByName(name);
	for (i = 0; i < kleurKlasse1.length; i++) {
		if (kleurKlasse1[i].checked) {
			return kleurKlasse1[i].value;        
		}
	}
}

function aanpassenTijdenHandlers(){
	var okButton = document.getElementById("okButton");

	document.getElementById("TijdMaandagAanpassen").addEventListener("click", function () {
		document.getElementById("tijdKiezen").style.display = "block";
		okButton.setAttribute("data-dag", "maandag");
	});
	document.getElementById("TijdDinsdagAanpassen").addEventListener("click", function () {
		document.getElementById("tijdKiezen").style.display = "block";
		okButton.setAttribute("data-dag", "dinsdag");
	});
	document.getElementById("TijdWoensdagAanpassen").addEventListener("click", function () {
		document.getElementById("tijdKiezen").style.display = "block";
		okButton.setAttribute("data-dag", "woensdag");
	});
	document.getElementById("TijdDonderdagAanpassen").addEventListener("click", function () {
		document.getElementById("tijdKiezen").style.display = "block";
		okButton.setAttribute("data-dag", "donderdag");
	});
	document.getElementById("TijdVrijdagAanpassen").addEventListener("click", function () {
		document.getElementById("tijdKiezen").style.display = "block";
		okButton.setAttribute("data-dag", "vrijdag");
	});
	document.getElementById("TijdZaterdagAanpassen").addEventListener("click", function () {
		document.getElementById("tijdKiezen").style.display = "block";
		okButton.setAttribute("data-dag", "zaterdag");
	});
	document.getElementById("TijdZondagAanpassen").addEventListener("click", function () {
		document.getElementById("tijdKiezen").style.display = "block";
		okButton.setAttribute("data-dag", "zondag");
	});
	veranderTijdDag();
}
	
function veranderTijdDag(){
	okButton.addEventListener("click", function () {
		var openingsTijd = document.getElementById("openingsTijd").innerHTML;
		var sluitingsTijd =	document.getElementById("sluitingsTijd").innerHTML;

		var openingsTijdUur = parseInt(openingsTijd.substring(0, 2));
		var openingsTijdMinuut = parseInt(openingsTijd.substring(3, 5));

		var sluitingsTijdUur = parseInt(sluitingsTijd.substring(0, 2));
		var sluitingsTijdMinuut = parseInt(sluitingsTijd.substring(3, 5));

		if(openingsTijdUur > sluitingsTijdUur){
			errorMessage("De openings tijd moet voor de sluitings tijd zijn");
		} else if(openingsTijdUur == sluitingsTijdUur && openingsTijdMinuut >= sluitingsTijdMinuut){
			errorMessage("De openings tijd moet voor de sluitings tijd zijn");
		} else{
			var dag = okButton.getAttribute("data-dag");
			if(dag == "maandag"){
				document.getElementById("openingsTijdMaandag").innerHTML = openingsTijd;
				document.getElementById("sluitingsTijdMaandag").innerHTML = sluitingsTijd;
			} else if(dag == "dinsdag"){
				document.getElementById("openingsTijdDinsdag").innerHTML = openingsTijd;
				document.getElementById("sluitingsTijdDinsdag").innerHTML = sluitingsTijd;
			} else if(dag == "woensdag"){
				document.getElementById("openingsTijdWoensdag").innerHTML = openingsTijd;
				document.getElementById("sluitingsTijdWoensdag").innerHTML = sluitingsTijd;
			} else if(dag == "donderdag"){
				document.getElementById("openingsTijdDonderdag").innerHTML = openingsTijd;
				document.getElementById("sluitingsTijdDonderdag").innerHTML = sluitingsTijd;
			} else if(dag == "vrijdag"){
				document.getElementById("openingsTijdVrijdag").innerHTML = openingsTijd;
				document.getElementById("sluitingsTijdVrijdag").innerHTML = sluitingsTijd;
			} else if(dag == "zaterdag"){
				document.getElementById("openingsTijdZaterdag").innerHTML = openingsTijd;
				document.getElementById("sluitingsTijdZaterdag").innerHTML = sluitingsTijd;
			} else if(dag == "zondag"){
				document.getElementById("openingsTijdZondag").innerHTML = openingsTijd;
				document.getElementById("sluitingsTijdZondag").innerHTML = sluitingsTijd;
			}
			document.getElementById("openingsTijd").innerHTML = "00:00";
			document.getElementById("sluitingsTijd").innerHTML = "00:00";

			document.getElementById("timepicker-hours-openingstijd").style.display = "block";
			document.getElementById("timepicker-minutes-openingstijd").style.display = "none";
			document.getElementById("tijdKiezen").style.display= "none";
		}
	});
}

function determineOpeningsTijd(){ 
	var openingsTijd = "00:00";
	var selectedUur = "";
	var selectedMinuut = "";

	document.getElementById("timepicker-hours-openingstijd").style.display = "block"; 

	document.getElementById("showUrenOpeningsTijd").addEventListener("click", function () {
		document.getElementById("timepicker-hours-openingstijd").style.display = "block";
		document.getElementById("timepicker-minutes-openingstijd").style.display = "none";	
	})
	document.getElementById("showMinutenOpeningsTijd").addEventListener("click", function () {
		document.getElementById("timepicker-minutes-openingstijd").style.display = "block";	
		document.getElementById("timepicker-hours-openingstijd").style.display = "none";
	})
	var uren = document.getElementsByClassName("timepicker-uur-openingstijd");
	for(var i = 0; i < uren.length; i++){
		handleUurKeuzeOpeningsTijd(uren[i]);
	};

	var minuten = document.getElementsByClassName("timepicker-minuut-openingstijd");
	for(var i = 0; i < minuten.length; i++){
		handleMinuutKeuzeOpeningsTijd(minuten[i]);   
	};

	function handleUurKeuzeOpeningsTijd(uur){
		uur.addEventListener("click", function () { 
			selectedUur.className = "timepicker-uur-openingstijd";
			selectedUur = uur;
			selectedUur.className = "selectedTime";

			openingsTijd =  parseInt(uur.innerHTML) +":"+parseInt(openingsTijd.substring(3, 5));
			document.getElementById("openingsTijd").innerHTML = openingsTijd;
		})
	}

	function handleMinuutKeuzeOpeningsTijd(minuut){
		minuut.addEventListener("click", function () {
			selectedMinuut.className = "timepicker-minuut-openingstijd";
			selectedMinuut = minuut;
			selectedMinuut.className = "selectedTime";

			openingsTijd =  parseInt(openingsTijd.substring(0, 2)) +":"+parseInt(minuut.innerHTML);
			document.getElementById("openingsTijd").innerHTML = openingsTijd;
		})
	}
}



function determineSluitingsTijd(){
	var sluitingsTijd = "00:00";
	var selectedUur = "";
	var selectedMinuut = "";

	document.getElementById("timepicker-hours-sluitingsTijd").style.display = "block"; 

	document.getElementById("showUrenSluitingsTijd").addEventListener("click", function () {
		document.getElementById("timepicker-hours-sluitingsTijd").style.display = "block";
		document.getElementById("timepicker-minutes-sluitingsTijd").style.display = "none";	
	})
	document.getElementById("showMinutenSluitingsTijd").addEventListener("click", function () {
		document.getElementById("timepicker-minutes-sluitingsTijd").style.display = "block";	
		document.getElementById("timepicker-hours-sluitingsTijd").style.display = "none";
	})
	var uren = document.getElementsByClassName("timepicker-uur-sluitingsTijd");
	for(var i = 0; i < uren.length; i++){
		handleUurKeuzeOpeningsTijd(uren[i]);
	};

	var minuten = document.getElementsByClassName("timepicker-minuut-sluitingsTijd");
	for(var i = 0; i < minuten.length; i++){
		handleMinuutKeuzeOpeningsTijd(minuten[i]);   
	};

	function handleUurKeuzeOpeningsTijd(uur){
		uur.addEventListener("click", function () {        
			selectedUur.className = "timepicker-uur-openingstijd";
			selectedUur = uur;
			selectedUur.className = "selectedTime";

			sluitingsTijd =  parseInt(uur.innerHTML) +":"+parseInt(sluitingsTijd.substring(3, 5));
			document.getElementById("sluitingsTijd").innerHTML = sluitingsTijd;
		})
	}

	function handleMinuutKeuzeOpeningsTijd(minuut){
		minuut.addEventListener("click", function () {
			selectedMinuut.className = "timepicker-minuut-openingstijd";
			selectedMinuut = minuut;
			selectedMinuut.className = "selectedTime";

			sluitingsTijd =  parseInt(sluitingsTijd.substring(0, 2)) +":"+parseInt(minuut.innerHTML);
			document.getElementById("sluitingsTijd").innerHTML = sluitingsTijd;
		})
	}
}

function checkboxChecker(textId, checkId) {
 	var checkBox = document.getElementById(checkId);
	checkBox.addEventListener("click", function () {
		var text = document.getElementById(textId);
		if (checkBox.checked == true){
			text.style.display = "block";
		} else {
			text.style.display = "none";
		}
	})
}

function saveDagen(){
	var formData = new FormData(document.getElementById("dagenForm"));

	if(document.getElementById("geslotenMaandag").checked){
		formData.append("openingsTijdMaandag", document.getElementById("openingsTijdMaandag").innerHTML);
		formData.append("sluitingsTijdMaandag", document.getElementById("sluitingsTijdMaandag").innerHTML);
	}
	if(document.getElementById("geslotenDinsdag").checked){
		formData.append("openingsTijdDinsdag", document.getElementById("openingsTijdDinsdag").innerHTML);
		formData.append("sluitingsTijdDinsdag", document.getElementById("sluitingsTijdDinsdag").innerHTML);
	}
	if(document.getElementById("geslotenWoensdag").checked){
		formData.append("openingsTijdWoensdag", document.getElementById("openingsTijdWoensdag").innerHTML);
		formData.append("sluitingsTijdWoensdag", document.getElementById("sluitingsTijdWoensdag").innerHTML);
	}
	if(document.getElementById("geslotenDonderdag").checked){
		formData.append("openingsTijdDonderdag", document.getElementById("openingsTijdDonderdag").innerHTML);
		formData.append("sluitingsTijdDonderdag", document.getElementById("sluitingsTijdDonderdag").innerHTML);
	}
	if(document.getElementById("geslotenVrijdag").checked){
		formData.append("openingsTijdVrijdag", document.getElementById("openingsTijdVrijdag").innerHTML);
		formData.append("sluitingsTijdVrijdag", document.getElementById("sluitingsTijdVrijdag").innerHTML);
	}
	if(document.getElementById("geslotenZaterdag").checked){
		formData.append("openingsTijdZaterdag", document.getElementById("openingsTijdZaterdag").innerHTML);
		formData.append("sluitingsTijdZaterdag", document.getElementById("sluitingsTijdZaterdag").innerHTML);
	}
	if(document.getElementById("geslotenZondag").checked){
		formData.append("openingsTijdZondag", document.getElementById("openingsTijdZondag").innerHTML);
		formData.append("sluitingsTijdZondag", document.getElementById("sluitingsTijdZondag").innerHTML);
	}

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
	fetch("restservices/setup/dagen", fetchoptions)
	.then(function (response){
		if(response.ok){
			succesMessage("De klanten pagina is gewijzigt");
			return true;
		} else{
			errorMessage(response.status);
			return false;
		}
	});
}














