function onload() {
	var welkomDiv =document.getElementById("welkomDiv");
	welkomDiv.addEventListener("click", function () {
		welkomDiv.style.display= "none";
		begin();
	});
}


function begin(){
	document.getElementById("setupDiv").style.display = "block"
	document.getElementById("wizard").style.display = "block"
	document.getElementById("behandelingen").style.display = "block"

	var stap1 = document.getElementById("behandelingenWizard");
	var stap2 = document.getElementById("instellingenWizard");
	var stap3 = document.getElementById("tijdenWizard");
	
	loadBehandelingen();
	behandelingToevoegenForm();

	aanpassenTijdenHandlers();

	determineOpeningsTijd();
	determineSluitingsTijd()
	
	var pagina = "behandelingen";
	var volgendeButton = document.getElementById("volgendeStap");
	volgendeButton.addEventListener("click", function () {
		if (pagina == "behandelingen") {
			stap1.className = "done";
			stap2.className = "active";
			
			document.getElementById("behandelingen").style.display = "none"
			document.getElementById("afspraakSettingsDiv").style.display = "block"
			document.getElementById("vorigeStap").value = "Vorige";
			getPlanInfo();

			pagina = "afspraakSettingsDiv";
		} else if (pagina == "afspraakSettingsDiv") {
			saveInplanInfo()
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
}

//===================================================================================
//===================================================================================
//===================================================================================

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








