function onload(){
	getKlanten(1);
}

function getKlanten(page){
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/adminService/getBedrijven/" + page, fetchoptions)
	.then(function(response){
		if(response.ok){
			//het inloggen is geslaagdt
			return response.json()
		} else {
			//het inloggen is niet gelukt
			alert("er ging iets verkeerd");
		};
	})
	.then(function(bedrijven){
		var table = document.getElementById("klantenTable");
		var topRow = table.insertRow(0);
		var topRowNaam = topRow.insertCell(0);
		topRowNaam.innerHTML = "Naam"
		var topRowEmail = topRow.insertCell(1);
		topRowEmail.innerHTML = "Email"
		var topRowTelefoon = topRow.insertCell(2);
		topRowTelefoon.innerHTML = "Telefoon"
		var topRowAdres = topRow.insertCell(3);
		topRowAdres.innerHTML = "Adres"
		
		for(let bedrijf of bedrijven){
			var row = table.insertRow(1);
			var naam = row.insertCell(0);
			naam.innerHTML = 	bedrijf.naam;
			
			var email = row.insertCell(0);
			email.innerHTML = 	bedrijf.email;
			
			var telefoon = row.insertCell(0);
			telefoon.innerHTML = 	bedrijf.telefoon;
			
			var adres = row.insertCell(0);
			adres.innerHTML = 	bedrijf.adres;
		}
	})
}

document.getElementById("submitKlant").addEventListener("click", function(){
	setKlant();
})

function setKlant(){
	var formData = new FormData(document.getElementById("createKlant"));

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
	fetch("restservices/adminService/bedrijf", fetchoptions)
	.then(function (response){
		if(response.ok){
			onload();
		} else{
			alert("fout");
		}
	})
}








