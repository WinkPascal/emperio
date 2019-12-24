function onload() {
	getBehandelingen("load", "alle");
	wijzigBehandeling();
	verwijderBehandeling()
	var geslacht = [];
	
	function geslachtManager(geslachtObject){
		var gevonden = false;
		for (i = 0; i < geslacht.length; i++) {
			if(geslacht[i] == geslachtObject){
				geslacht.splice(i,1);
				gevonden = true;
			}
		} 
		if(gevonden == false){
			geslacht.push(geslachtObject);
			return true;
		} else{
			return false;
		}
	}
	
	var manKnop = document.getElementById("geslachtMan");
	document.getElementById("geslachtMan").addEventListener("click", function () {
		if(geslachtManager("man")){
			manKnop.className = "selectedGeslachtButtton";
		} else{
			manKnop.className = "geslachtButton";
		}
		getBehandelingen("load", geslacht);
	})
	var vrouwKnop = document.getElementById("geslachtVrouw");
	vrouwKnop.addEventListener("click", function () {
		if(geslachtManager("vrouw")){
			vrouwKnop.className = "selectedGeslachtButtton";
		} else{
			vrouwKnop.className = "geslachtButton";
		}
		getBehandelingen("load", geslacht);
	})
	var jongensKnop = document.getElementById("geslachtJongen");
	jongensKnop.addEventListener("click", function () {
		if(geslachtManager("jongen")){
			jongensKnop.className = "selectedGeslachtButtton";
		} else{
			jongensKnop.className = "geslachtButton";
		}
		getBehandelingen("load", geslacht);
	})
	var meisjesKnop = document.getElementById("geslachtMeisje");
	meisjesKnop.addEventListener("click", function () {
		if(geslachtManager("meisje")){
			meisjesKnop.className = "selectedGeslachtButtton";
		} else{
			meisjesKnop.className = "geslachtButton";
		}
		getBehandelingen("load", geslacht);
	})

	document.getElementById("vorigeKlantenPagina").addEventListener("click", function () {
		getBehandelingen("terug", geslacht);
	})

	document.getElementById("volgendeKlantenPagina").addEventListener("click", function () {
		getBehandelingen("volgende", geslacht);
	})
	document.getElementById("zoekKlant").addEventListener("click", function () {
		getBehandelingen("load", geslacht);
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
	
	if(geslacht.length < 1){
		geslacht = "alle";
	}
	
	document.getElementById("paginaNummer").innerHTML = blz;

	var behandelingenLijst = document.getElementById("behandelingenLijst");
	behandelingenLijst.innerHTML = "";
	var selectList = document.getElementById("selectList").value;
	var zoekInput = document.getElementById("zoekInput").value;
	if(zoekInput == ""){
		zoekInput = "-";
	}
	var data = "geslacht=" + geslacht + "&pageNumber=" + blz + "&sort=" + selectList + "&zoek="+zoekInput;
	console.log(data);
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

		behandelingenLijst.appendChild(createTopRow());
		for (let behandeling of behandelingen) {
			behandelingenLijst.appendChild(createBehandelingRow(behandeling));
			getBehandeling(behandeling.id, behandeling.prijs, behandeling.lengte, behandeling.afspraken, behandeling.inkomsten);
		}
	})
}

function createTopRow(){
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
	return topRow;
}

function createBehandelingRow(behandeling){
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
	return row;
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
				
				document.getElementById("naam").value = behandeling.naam;
				document.getElementById("beschrijving").value = behandeling.beschrijving;
				document.getElementById("prijsBehandeling").value = behandeling.prijs;
				document.getElementById("uur").value = behandeling.lengte;
				document.getElementById("minuten").value = behandeling.lengte;
				console.log(behandeling.geslacht);
				document.getElementById("doelgroepId").innerHTML = behandeling.geslacht;
				document.getElementById("afsprakenId").innerHTML = behandeling.afspraken;
				document.getElementById("inkomstenId").innerHTML = behandeling.inkomsten;
			})
	})
}

document.getElementById("Annuleren").addEventListener("click", function () {
	document.getElementById("BehandelingModal").style.display = "none";
})

function wijzigBehandeling() {
	document.getElementById("Wijzigen").addEventListener("click", function () {
		var id = document.getElementById("naam").getAttribute("data-id");
		var formData = new FormData(document.querySelector("#BehandelingForm"));
		formData.append("id", id);
		
		formData.append("minuten", document.getElementById("minuten").value);
		formData.append("uur", document.getElementById("uur").value);
		
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


document.getElementById("verwijderde").addEventListener("click", function () {
	alert("lijst met verwijderde behandelingen");
});

document.getElementById("aanmaken").addEventListener("click", function () {
	document.getElementById("BehandelingToevoegenModal").style.display = "block";
	behandelingToevoegenForm();
})

function behandelingToevoegenForm() {
	var geslachten = [];
	
	function behandelingManager(geslacht) {
		for (var i = 0; i < geslachten.length; i++) {
			if (geslachten[i] == geslacht) {
				geslachten.splice(i, 1);
				return false;
			}
		}
		geslachten.push(geslacht);
		return true;
	}
	
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
	//hier worden behandelingen toegevoegd
	document.getElementById("inplannenBehandelingToevoegen").addEventListener("click", function () {
		behandelingToevoegen(geslachten);
	})
}



document.getElementById("annuleerBehandelingAnnuleren").addEventListener("click", function () {
	document.getElementById("BehandelingToevoegenModal").style.display = "none";
})



function behandelingToevoegen(geslachten) {
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

