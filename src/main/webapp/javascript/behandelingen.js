function onload() {
	getBehandelingen("load", "alle");
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
			}
		})
}


document.getElementById("aanmaken").addEventListener("click", function () {
	document.getElementById("BehandelingToevoegenModal").style.display = "block";
	behandelingToevoegenForm();
})

function behandelingToevoegenForm() {
	document.getElementById("geslachtManToevoegen").addEventListener("click", function () {
		behandelingManager("geslachtManToevoegen");
	})
	document.getElementById("geslachtVrouwToevoegen").addEventListener("click", function () {
		behandelingManager("geslachtVrouwToevoegen");
	})
	document.getElementById("geslachtJongenToevoegen").addEventListener("click", function () {
		behandelingManager("geslachtJongenToevoegen");
	})
	document.getElementById("geslachtMeisjeToevoegen").addEventListener("click", function () {
		behandelingManager("geslachtMeisjeToevoegen");
	})
}

function behandelingManager(geslacht) {
	geslachten = [];
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

