function onload() {
	getBehandelingen("load", "alle");
	wijzigBehandeling();
	verwijderBehandeling()
	var geslacht = [];
	document.getElementById("geslachtMan").addEventListener("click", function () {
		geslacht.push("man");
		getBehandelingen("load", geslacht);
	})
	document.getElementById("geslachtVrouw").addEventListener("click", function () {
		geslacht.push("vrouw");
		getBehandelingen("load", geslacht);
	})
	document.getElementById("geslachtJongen").addEventListener("click", function () {
		geslacht.push("jongen");
		getBehandelingen("load", geslacht);
	})
	document.getElementById("geslachtMeisje").addEventListener("click", function () {
		geslacht.push("meisje");
		getBehandelingen("load", geslacht);
	})

	document.getElementById("vorigeButton").addEventListener("click", function () {
		getBehandelingen("terug", geslacht);
	})

	document.getElementById("volgendeButton").addEventListener("click", function () {
		getBehandelingen("volgende", geslacht);
	})
}

function getBehandelingen(functie, geslacht) {
	if (functie == "load") {
		blz = 1;
	} else if (functie == "volgende") {
		blz = blz + 1;
	} else if (functie == "terug") {
		if (blz != 1) {
			blz = blz - 1;
		} else {
			errorMessage("u kunt niet verder terug");
			return;
		}
	}
	document.getElementById("paginaNummer").innerHTML = blz;

	var behandelingenLijst = document.getElementById("behandelingenLijst");
	behandelingenLijst.innerHTML = "";
	var selectList = document.getElementById("selectList").value;
	var data = "geslacht=" + geslacht + "&pageNumber=" + blz + "&sort=" + selectList;
	var fetchoptions = {
		headers: {
			'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
		}
	}
	fetch("restservices/behandelingProvider/alleBehandelingen/" + data, fetchoptions)
		.then(response => response.json())
		.then(function (behandelingen) {
			if (behandelingen.length == 0) {
				errorMessage("er zijn geen behandelingen gevonden");
				return;
			}
			succesMessage("behandelingen zijn opgehaald");

			var topRow = document.createElement('tr');

			var behandelingNaam = document.createElement('th');
			behandelingNaam.innerHTML = "Naam";
			topRow.appendChild(behandelingNaam);

			var behandelingPrijs = document.createElement('th');
			behandelingPrijs.innerHTML = "prijs";
			topRow.appendChild(behandelingPrijs);

			var behandelingLengte = document.createElement('th');
			behandelingLengte.innerHTML = "lengte";
			topRow.appendChild(behandelingLengte);

			var behandelingGeslacht = document.createElement('th');
			behandelingGeslacht.innerHTML = "Geslacht";
			topRow.appendChild(behandelingGeslacht);

			var behandelingInkomsten = document.createElement('th');
			behandelingInkomsten.innerHTML = "inkomsten";
			topRow.appendChild(behandelingInkomsten);

			var behandelingAfspraken = document.createElement('th');
			behandelingAfspraken.innerHTML = "afspraken";
			topRow.appendChild(behandelingAfspraken);

			behandelingenLijst.appendChild(topRow);

			for (let behandeling of behandelingen) {
				var row = document.createElement('tr');
				row.id = behandeling.id;

				var behandelingNaam = document.createElement('td');
				behandelingNaam.innerHTML = behandeling.naam;
				row.appendChild(behandelingNaam);

				var behandelingPrijs = document.createElement('td');
				behandelingPrijs.innerHTML = behandeling.prijs;
				row.appendChild(behandelingPrijs);

				var behandelingLengte = document.createElement('td');
				behandelingLengte.innerHTML = behandeling.lengte;
				row.appendChild(behandelingLengte);

				var behandelingGeslacht = document.createElement('td');
				behandelingGeslacht.innerHTML = behandeling.geslacht;
				row.appendChild(behandelingGeslacht);

				var behandelingInkomsten = document.createElement('td');
				behandelingInkomsten.innerHTML = behandeling.inkomsten;
				row.appendChild(behandelingInkomsten);

				var behandelingAfspraken = document.createElement('td');
				behandelingAfspraken.innerHTML = behandeling.afspraken;
				row.appendChild(behandelingAfspraken);

				behandelingenLijst.appendChild(row);
				getBehandeling(behandeling.id, behandeling.prijs, behandeling.lengte, behandeling.afspraken, behandeling.inkomsten);
			}
		})
}

function getBehandeling(id, naam, prijs, lengte, afspraken, inkomsten) {
	document.getElementById(id).addEventListener("click", function () {
		document.getElementById("naam").setAttribute("data-id", id);
		var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
		fetch("restservices/behandelingProvider/getBehandeling/" + id, fetchoptions)
			.then(response => response.json())
			.then(function (behandeling) {
				document.getElementById("BehandelingModal").style.display = "block";
				document.getElementById("naam").value = naam;
				document.getElementById("beschrijving").value = "dsaudgasdasdsaduhabsdbhjasds";
				document.getElementById("prijsBehandeling").value = prijs;
				document.getElementById("uur").value = lengte;
				document.getElementById("minuten").value = lengte;
			})
	})
}
document.getElementById("Annuleren").addEventListener("click", function () {
	document.getElementById("BehandelingModal").style.display = "none";

})

function wijzigBehandeling() {
	document.getElementById("Wijzigen").addEventListener("click", function () {
		var id = document.getElementById("naam").getAttribute("data-id");

		var formData = new FormData(document.querySelector("#BehandelingToevoegenForm"));
		var encData = new URLSearchParams(formData.entries());
		alert(encData);
		var fetchoptions = {
			method: 'PUT',
			body: encData,
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
		// De wijziging wordt verstuurt naar de backend
		fetch("restservices/behandelingProvider/behandeling", fetchoptions)
			.then(function (response) {
				if (response.ok) {
					alert("toegevoegd");
				} else {
					alert("kapot fout");
				}
			})
	})
}

function verwijderBehandeling() {
	document.getElementById("Verwijderen").addEventListener("click", function () {
		var id = document.getElementById("naam").getAttribute("data-id");
		var fetchoptions = {
			method: 'DELETE',
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
		// De wijziging wordt verstuurt naar de backend
		fetch("restservices/behandelingProvider/behandeling/"+id, fetchoptions)
			.then(function (response) {
				if (response.ok) {
					alert("toegevoegd");
				} else {
					alert("kapot fout");
				}
			})
	})
}



document.getElementById("aanmaken").addEventListener("click", function () {
	document.getElementById("BehandelingToevoegenModal").style.display = "block";
	behandelingToevoegenForm();
})

function behandelingToevoegenForm() {
	var manKnop = document.getElementById("geslachtManToevoegen");
	manKnop.addEventListener("click", function () {
		if (behandelingManager("man")) {
			manKnop.className = "selectedGeslachtButtton";
		} else {
			manKnop.className = "geslachtButton";
		}
	})
	var vrouwKnop = document.getElementById("geslachtVrouwToevoegen");
	vrouwKnop.addEventListener("click", function () {
		if (behandelingManager("vrouw")) {
			vrouwKnop.className = "selectedGeslachtButtton";
		} else {
			vrouwKnop.className = "geslachtButton";
		}
	})
	var jongenKnop = document.getElementById("geslachtJongenToevoegen");
	jongenKnop.addEventListener("click", function () {
		if (behandelingManager("jongen")) {
			jongenKnop.className = "selectedGeslachtButtton";
		} else {
			jongenKnop.className = "geslachtButton";
		}
	})
	var meisjeKnop = document.getElementById("geslachtMeisjeToevoegen");
	meisjeKnop.addEventListener("click", function () {
		if (behandelingManager("meisje")) {
			meisjeKnop.className = "selectedGeslachtButtton";
		} else {
			meisjeKnop.className = "geslachtButton";
		}
	})
}

function behandelingManager(geslacht) {
	geslachten = [];
	if (geslachten.includes(geslacht)) {
		for (var i = 0; i < geslachten.length; i++) {
			if (geslachten[i] === geslacht) {
				geslachten.splice(i, 1);
				i--;
				return false;
			}
		}
	} else {
		geslachten.push(geslacht);
		return true;
	}
}

document.getElementById("annuleerBehandelingAnnuleren").addEventListener("click", function () {
	document.getElementById("BehandelingToevoegenModal").style.display = "none";
})

//hier worden behandelingen toegevoegd
document.getElementById("inplannenBehandelingToevoegen").addEventListener("click", function () {
	behandelingToevoegen();
})
function behandelingToevoegen() {
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
	fetch("restservices/behandelingProvider/behandeling", fetchoptions)
		.then(function (response) {
			if (response.ok) {
				alert("toegevoegd");
			} else {
				alert("kapot fout");
			}
		})
}

