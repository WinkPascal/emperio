function onload(){
	//errorMessage("error");
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
		if(blz != 1){
			blz = blz - 1;
		} else{
			errorMessage("u kunt niet verder terug");
			return;
		}
	}
	document.getElementById("paginaNummer").innerHTML = blz;
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
		if(klanten.length == 0){
			errorMessage("er zijn geen klanten gevonden");
			return;			
		}
		succesMessage("klanten zijn opgehaald");
		
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
			row.id = klant.id;
			
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
			document.getElementById(klant.id).addEventListener("click", function(){
				document.getElementById("klantModal").style.display = "block";
				getKlant(klant.id);
			})
		}
	}).catch(function() {
		alert("ging iets fout");
	});	
}

function getKlant(id){
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/klant/"+id, fetchoptions)
	.then(response => response.json())
	.then(function(dataArray){
		var i = 0;
		for(let data of dataArray){
			if(i == 0){
				document.getElementById("naamInfo").innerHTML = data.naam;
			
				document.getElementById("geslachtInfo").innerHTML = data.geslacht;
				if(data.email == undefined){
					document.getElementById("emailInfo").innerHTML = "-";
				} else{
					document.getElementById("emailInfo").innerHTML = data.email;
				}
				if(data.telefoon == undefined){
					document.getElementById("telefoonInfo").innerHTML = "-";		
				} else{
					document.getElementById("telefoonInfo").innerHTML = data.telefoon;
				}
				document.getElementById("email").addEventListener("click", function(){
					alert("email optie van "+id);
				})
				i++;
			} else if(i == 1){
				document.getElementById("aantalAfsprakenInfo").innerHTML = data.afspraken;
			    document.getElementById("hoeveelheidInkomstenInfo").innerHTML = data.inkomsten;
				i++;
			} else{
				var afspraak = document.createElement('span');
				afspraak.setAttribute('class', "afspraakKlant");
				var afspraakDatum = document.createElement('a');
				afspraakDatum.setAttribute('class', "afspraakDatum");
				afspraakDatum.innerHTML = data.datum;
				afspraak.appendChild(afspraakDatum);
				
				var afspraakTijd = document.createElement('a');
				afspraakDatum.setAttribute('class', "afspraakTijd");
				afspraakDatum.innerHTML = data.tijd;
				afspraak.appendChild(afspraakTijd);
				
				var afspraakPrijs = document.createElement('a');
				afspraakDatum.setAttribute('class', "afspraakPrijs");
				afspraakPrijs.innerHTML = "€"+ data.prijs;
				afspraak.appendChild(afspraakPrijs);

				document.getElementById("afsprakenLijst").appendChild(afspraak);

			}
		}	
	})
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
 