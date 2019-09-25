function getAlleKlanten(){
	blz = 1;
	getKlanten("load");
}

document.getElementById("vorigeKlantenPagina").addEventListener("click", function(){
	getKlanten("terug");
})

document.getElementById("volgendeKlantenPagina").addEventListener("click", function(){
	getKlanten("volgende");
})

function getKlanten(functie){

	document.getElementById("klantenLijstLinks").innerHTML = "";
	document.getElementById("klantenLijstRechts").innerHTML = "";

	if(functie == "load"){
		blz=1;
	} else if(functie == "volgende"){
		blz = blz +1;
	} else if(functie == "terug"){
		blz = blz - 1;
	}
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/alleKlanten/"+blz, fetchoptions)
	.then(response => response.json())
	.then(function(klanten){
		var i = 0;
		for(let klant of klanten){
			i++;
			var klantDiv = document.createElement('div');
			klantDiv.setAttribute('id', klant.id);
			klantDiv.setAttribute('class', "klantDiv");
			
			var klantNaam = document.createElement('span');
			klantNaam.innerHTML = "Naam: "+klant.naam +"<br>";
			klantNaam.setAttribute('class', "klantNaam");
			klantDiv.appendChild(klantNaam);

			var klantGeslacht = document.createElement('span');
			klantGeslacht.innerHTML = "Geslacht "+klant.geslacht+"<br>";
			klantGeslacht.setAttribute('class', "klantGeslacht");
			klantDiv.appendChild(klantGeslacht);

			var klantEmail = document.createElement('span');
			klantEmail.innerHTML = "Email "+klant.email+"<br>";
			klantEmail.setAttribute('class', "klantEmail");
			klantDiv.appendChild(klantEmail);

			var klantTelefoon = document.createElement('span');
			klantTelefoon.innerHTML = "Telefoonnummer "+klant.telefoon;
			klantTelefoon.setAttribute('class', "klantTelefoon");
			klantDiv.appendChild(klantTelefoon);
			if(i > 5){
				document.getElementById("klantenLijstLinks").appendChild(klantDiv);
			} else{
				document.getElementById("klantenLijstRechts").appendChild(klantDiv);
			}
		}
	}).catch(function() {
		alert("ging iets fout");
	});	
}

document.getElementById("zoekKlantButton").addEventListener("click", function(){
	zoekKlanten();
})

// Zoek klanten door de zoekbalk
function zoekKlanten(){
	document.getElementById("klantenLijstLinks").innerHTML = "";
	document.getElementById("klantenLijstRechts").innerHTML = "";
	
	var requestKlant = document.getElementById("zoekKlantInput").value;
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/klantenZoekReq/"+requestKlant, fetchoptions)
	.then(response => response.json())
	.then(function(klanten){
		var i = 0;
		for(let klant of klanten){
			i++;
			var klantDiv = document.createElement('div');
			klantDiv.setAttribute('id', klant.id);
			klantDiv.setAttribute('class', "klantDiv");
			
			var klantNaam = document.createElement('span');
			klantNaam.innerHTML = "Naam: "+klant.naam +"<br>";
			klantNaam.setAttribute('class', "klantNaam");
			klantDiv.appendChild(klantNaam);

			var klantGeslacht = document.createElement('span');
			klantGeslacht.innerHTML = "Geslacht "+klant.geslacht+"<br>";
			klantGeslacht.setAttribute('class', "klantGeslacht");
			klantDiv.appendChild(klantGeslacht);

			var klantEmail = document.createElement('span');
			klantEmail.innerHTML = "Email "+klant.email+"<br>";
			klantEmail.setAttribute('class', "klantEmail");
			klantDiv.appendChild(klantEmail);

			var klantTelefoon = document.createElement('span');
			klantTelefoon.innerHTML = "Telefoonnummer "+klant.telefoon;
			klantTelefoon.setAttribute('class', "klantTelefoon");
			klantDiv.appendChild(klantTelefoon);
			if(i > 5){
				document.getElementById("klantenLijstLinks").appendChild(klantDiv);
			} else{
				document.getElementById("klantenLijstRechts").appendChild(klantDiv);
			}
		}
	}).catch(function() {
		alert("ging iets fout");
	});	
}



	
