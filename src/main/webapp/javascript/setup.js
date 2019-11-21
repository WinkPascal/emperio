function onload() {
	var pagina = "behandelingen";
	document.getElementById("behandelingen").style.display = "block"
	var vorigeButton = document.getElementById("vorigeStap");
	vorigeButton.addEventListener("click", function () {
		if (pagina == "productenDiv") {

			document.getElementById("behandelingen").style.display = "block"
			document.getElementById("productenDiv").style.display = "none"
			vorigeButton.style.display = "none";

			pagina = "behandelingen";
		} else if (pagina == "dagenDiv") {
			document.getElementById("productenDiv").style.display = "block"
			document.getElementById("dagenDiv").style.display = "none"
			pagina = "productenDiv";
		}
	})
	var volgendeButton = document.getElementById("volgendeStap");
	volgendeButton.addEventListener("click", function () {
		if (pagina == "behandelingen") {
			document.getElementById("behandelingen").style.display = "none"
			document.getElementById("productenDiv").style.display = "block"

			vorigeButton.style.display = "block";

			pagina = "productenDiv";
		} else if (pagina == "productenDiv") {
			document.getElementById("productenDiv").style.display = "none"
			document.getElementById("dagenDiv").style.display = "block"

			volgendeButton.style.backgroundColor = "green";
			volgendeButton.value = "Start!"

			pagina = "dagenDiv";
		} else if (pagina == "dagenDiv") {
			// controle
			location.href = 'home.html';
		}
	})
	loadBehandelingen();
	behandelingToevoegenForm();
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

function loadBehandelingen() {
		var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	var behandelingenLijst = document.getElementById("behandelingenLijst");
	behandelingenLijst.innerHTML= "";
	behandelingenLijst.innerHTML = "";
	fetch("restservices/setup/getbehandelingen", fetchoptions)
	.then(response => response.json())
	.then(function(behandelingen){
		for(let behandeling of behandelingen){
			var behandelingDiv = document.createElement('div');
			behandelingDiv.setAttribute('id', behandeling.id);
			behandelingDiv.setAttribute('class', 'behandelingDiv');

			var behandelingNaam = document.createElement('span');
			behandelingNaam.innerHTML = behandeling.naam;
			behandelingNaam.setAttribute('class', 'behandelingNaam');

			var behandelingBeshrijving = document.createElement('span');
			behandelingBeshrijving.innerHTML = behandeling.beschrijving;
			behandelingBeshrijving.setAttribute('class', 'behandelingBeshrijving');

			var behandelingGeslacht = document.createElement('span');
			behandelingGeslacht.innerHTML = behandeling.geslacht;
			behandelingGeslacht.setAttribute('class', 'behandelingGeslacht');
			
			var behandelingLengteEnPrijs = document.createElement('span');
			behandelingLengteEnPrijs.setAttribute('class', 'behandelingLengteEnPrijs');
				var behandelingLengte = document.createElement('a');
				behandelingLengte.setAttribute('class', 'behandelingLengte');
				behandelingLengteEnPrijs.appendChild(behandelingLengte);

				var behandelingPrijs = document.createElement('a');
				behandelingPrijs.setAttribute('class', 'behandelingPrijs');
				behandelingLengteEnPrijs.appendChild(behandelingPrijs);

			behandelingDiv.appendChild(behandelingNaam);
			behandelingDiv.appendChild(behandelingBeshrijving);
			behandelingDiv.appendChild(behandelingGeslacht);
			behandelingDiv.appendChild(behandelingLengteEnPrijs);
			behandelingenLijst.appendChild(behandelingDiv);
		}
	})
}

function behandelingManager(geslacht, geslachten) {
	console.log(geslachten);
	if (geslachten.includes(geslacht)) {
		for (var i = 0; i < geslachten.length; i++) {
			console.log(geslachten[i]);
			console.log(geslacht);
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
	fetch("restservices/setup/behandeling", fetchoptions)
		.then(function (response) {
			if (response.ok) {
				loadBehandelingen();
				succesMessage("goed");
			} else {
				errorMessage(response.status);
			}
		})
}