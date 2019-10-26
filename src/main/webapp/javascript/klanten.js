function onload(){
	getKlanten("load");
}

document.getElementById("vorigeKlantenPagina").addEventListener("click", function(){
	getKlanten("terug");
})

document.getElementById("volgendeKlantenPagina").addEventListener("click", function(){
	getKlanten("volgende");
})

function getKlanten(functie){ 
	if(functie == "load"){
		blz=1;
	} else if(functie == "volgende"){
		blz = blz +1;
	} else if(functie == "terug"){
		blz = blz - 1;
	}
	var klantenLijst = document.getElementById("klantenLijst");
	klantenLijst.innerHTML="";
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/alleKlanten/"+blz, fetchoptions)
	.then(response => response.json())
	.then(function(klanten){
		var topRow = document.createElement('tr');

		var klantNaam = document.createElement('th');
		klantNaam.innerHTML= "Naam";
		topRow.appendChild(klantNaam);

		var klantGeslacht = document.createElement('th');
		klantGeslacht.innerHTML= "Geslacht";
		topRow.appendChild(klantGeslacht);
		
		var klantEmail = document.createElement('th');
		klantEmail.innerHTML= "Email";
		topRow.appendChild(klantEmail);
		
		var klantTelefoon = document.createElement('th');
		klantTelefoon.innerHTML= "Telefoon nummer";
		topRow.appendChild(klantTelefoon);
		
		klantenLijst.appendChild(topRow);
		for(let klant of klanten){
			var row = document.createElement('tr');
			
			var klantNaam = document.createElement('td');
			klantNaam.innerHTML = klant.naam;
			row.appendChild(klantNaam);

			var klantGeslacht = document.createElement('td');
			klantGeslacht.innerHTML = klant.geslacht;
			row.appendChild(klantGeslacht);

			var klantEmail = document.createElement('td');
			klantEmail.innerHTML = klant.email;
			row.appendChild(klantEmail);

			var klantTelefoon = document.createElement('td');
			klantTelefoon.innerHTML = klant.telefoon;
			row.appendChild(klantTelefoon);
			
			klantenLijst.appendChild(row);
		}
	}).catch(function() {
		alert("ging iets fout");
	});	
}

document.getElementById("zoekKlant").addEventListener("click", function(){
	zoekKlanten();
})

//Zoek klanten door de zoekbalk
function zoekKlanten(){
	var requestKlant = document.getElementById("zoekKlantInput").value;
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	var klantenLijst = document.getElementById("klantenLijst");
	klantenLijst.innerHTML = "";
	fetch("restservices/service/klantenZoekReq/"+requestKlant, fetchoptions)
	.then(response => response.json())
	.then(function(klanten){
		var topRow = document.createElement('tr');

		var klantNaam = document.createElement('th');
		klantNaam.innerHTML= "Naam";
		topRow.appendChild(klantNaam);

		var klantGeslacht = document.createElement('th');
		klantGeslacht.innerHTML= "Geslacht";
		topRow.appendChild(klantGeslacht);
		
		var klantEmail = document.createElement('th');
		klantEmail.innerHTML= "Email";
		topRow.appendChild(klantEmail);
		
		var klantTelefoon = document.createElement('th');
		klantTelefoon.innerHTML= "Telefoon nummer";
		topRow.appendChild(klantTelefoon);
		
		klantenLijst.appendChild(topRow);
		for(let klant of klanten){
			var row = document.createElement('tr');
			
			var klantNaam = document.createElement('td');
			klantNaam.innerHTML = klant.naam;
			row.appendChild(klantNaam);

			var klantGeslacht = document.createElement('td');
			klantGeslacht.innerHTML = klant.geslacht;
			row.appendChild(klantGeslacht);

			var klantEmail = document.createElement('td');
			klantEmail.innerHTML = klant.email;
			row.appendChild(klantEmail);

			var klantTelefoon = document.createElement('td');
			klantTelefoon.innerHTML = klant.telefoon;
			row.appendChild(klantTelefoon);
			
			klantenLijst.appendChild(row);
		}
	}).catch(function() {
		alert("ging iets fout");
	});	
}
 