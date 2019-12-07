function onload(){
	bladzijdeManager("load");
}

document.getElementById("vorigeKlantenPagina").addEventListener("click", function(){
	bladzijdeManager("terug");
})

document.getElementById("volgendeKlantenPagina").addEventListener("click", function(){
	bladzijdeManager("volgende");
})

function bladzijdeManager(functie){
	if(functie == "load"){
		blz=1;
	} else if(functie == "volgende"){
		blz = blz + 1;
	} else if(functie == "terug"){
		if(blz != 1){
			blz = blz - 1;
		} else{
			errorMessage("u kunt niet verder terug");
			return;
		}
	}
	document.getElementById("paginaNummer").innerHTML = blz;
	getKlanten(blz);
}

function getKlanten(blz){ 
	var klantenLijst = document.getElementById("klantenLijst");
	klantenLijst.innerHTML="";
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/klanten/alleKlanten/"+blz, fetchoptions)
	.then(response => response.json())
	.then(function(klanten){
		if(klanten.length == 0){
			errorMessage("er zijn geen klanten gevonden");
			return;			
		} else{
			klantenLijst.appendChild(createTopRow());
			for(let klant of klanten){
                createKlantRow(klant);
			}
		}
	})
}
function createKlantRow(klant){
	var row = document.createElement('tr');
	row.id = klant.id;
	
	var klantNaam = document.createElement('td');
	klantNaam.innerHTML = klant.naam;
	klantNaam.className="telefoon";
	row.appendChild(klantNaam);

	var klantGeslacht = document.createElement('td');
	klantGeslacht.innerHTML = klant.geslacht;
	klantGeslacht.className="desktop";
	row.appendChild(klantGeslacht);

	var klantEmail = document.createElement('td');
	klantEmail.innerHTML = klant.email;
	klantEmail.className="tablet";
	row.appendChild(klantEmail);

	var klantTelefoon = document.createElement('td');
	klantTelefoon.innerHTML = klant.telefoon;
	klantTelefoon.className="tablet";
	row.appendChild(klantTelefoon);

	var klantAdres = document.createElement('td');
	klantAdres.className="desktop";
	klantAdres.innerHTML = klant.adres;
	row.appendChild(klantAdres);

	var klantAfspraken = document.createElement('td');
	klantAfspraken.className="telefoon";
	klantAfspraken.innerHTML = "11";
	row.appendChild(klantAfspraken);

	var klantInkomsten = document.createElement('td');
	klantInkomsten.className="telefoon";
	klantInkomsten.innerHTML = "$12.22";
	row.appendChild(klantInkomsten);
	
	document.getElementById("klantenLijst").appendChild(row);
	document.getElementById(klant.id).addEventListener("click", function(){
		getKlant(klant.id);
	})
}

function createTopRow(){
	var topRow = document.createElement('tr');
	topRow.className = "topRow";

	var klantNaam = document.createElement('th');
	klantNaam.innerHTML= "Naam";
	klantNaam.className="telefoon";
	topRow.appendChild(klantNaam);

	var klantGeslacht = document.createElement('th');
	klantGeslacht.innerHTML= "Geslacht";
	klantGeslacht.className="desktop";
	topRow.appendChild(klantGeslacht);
		
	var klantEmail = document.createElement('th');
	klantEmail.innerHTML= "Email";
	klantEmail.className="tablet";
	topRow.appendChild(klantEmail);
		
	var klantTelefoon = document.createElement('th');
	klantTelefoon.innerHTML= "Telefoon nummer";
	klantTelefoon.className="tablet";
	topRow.appendChild(klantTelefoon);

	var klantAdres = document.createElement('th');
	klantAdres.innerHTML= "Adres";
	klantAdres.className="desktop";
	topRow.appendChild(klantAdres);

	var klantAantalAfspraken = document.createElement('th');
	klantAantalAfspraken.innerHTML= "Afspraken";
	klantAantalAfspraken.className="telefoon";
	topRow.appendChild(klantAantalAfspraken);

	var klantInkomsten = document.createElement('th');
	klantInkomsten.innerHTML= "Inkomsten";
	klantInkomsten.className="telefoon";
	topRow.appendChild(klantInkomsten);

	return topRow;
}

function getKlant(id){
	document.getElementById("klantModal").style.display = "block";
	document.getElementById("sluitKlantInfo").addEventListener("click", function(){
		document.getElementById("naamInfo").innerHTML = "";
		document.getElementById("geslachtInfo").innerHTML = "";
		document.getElementById("telefoonInfo").innerHTML = "";
		document.getElementById("emailInfo").innerHTML = "";
		document.getElementById("aantalAfsprakenInfo").innerHTML = "";
		document.getElementById("hoeveelheidInkomstenInfo").innerHTML = "";
		document.getElementById("afsprakenLijst").innerHTML = "";
		document.getElementById("klantModal").style.display = "none";
	})
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/klanten/klant/"+id, fetchoptions)
	.then(response => response.json())
	.then(function(data){
		var i = 0;
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
		document.getElementById("emailKlant").addEventListener("click", function(){
			alert("email optie van "+id);
		})
		document.getElementById("aantalAfsprakenInfo").innerHTML = data.afspraken;
		document.getElementById("hoeveelheidInkomstenInfo").innerHTML = data.inkomsten;
		var afspraakListHeader = document.createElement('h2');
		afspraakListHeader.innerHTML = "laatste 5 afspraken";
		document.getElementById("afsprakenLijst").appendChild(afspraakListHeader);

		for(let afspraak of data.afspraken){
			var afspraakSpan = document.createElement('span');
			afspraakSpan.setAttribute('class', "afspraakKlant");
			var afspraakDatum = document.createElement('a');
			afspraakDatum.setAttribute('class', "afspraakDatum");
			afspraakDatum.innerHTML = afspraak.datum;
			afspraakSpan.appendChild(afspraakDatum);
			
			var afspraakTijd = document.createElement('a');
			afspraakDatum.setAttribute('class', "afspraakTijd");
			afspraakDatum.innerHTML = afspraak.tijd;
			afspraakSpan.appendChild(afspraakTijd);
				
			var afspraakPrijs = document.createElement('a');
			afspraakDatum.setAttribute('class', "afspraakPrijs");
			afspraakPrijs.innerHTML = "€"+ afspraak.prijs;
			afspraakSpan.appendChild(afspraakPrijs);
			
			document.getElementById("afsprakenLijst").appendChild(afspraakSpan);
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
	fetch("restservices/klanten/klantenZoekReq/"+requestKlant, fetchoptions)
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
	})
}
 