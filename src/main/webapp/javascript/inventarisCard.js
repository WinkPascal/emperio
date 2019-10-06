function getAlleProducten(paginaNummer){
	document.getElementById("inventarisLijstLinks").innerHTML = "";
	document.getElementById("inventarisLijstRechts").innerHTML = "";
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/producten/" + paginaNummer, fetchoptions)
	.then(response => response.json())
	.then(function(producten){
		var i = 0;
		for(let product of producten){
			i++;
			var klantDiv = document.createElement('div');
			klantDiv.setAttribute('id', product.id);
			klantDiv.setAttribute('class', "klantDiv");
			
			var klantNaam = document.createElement('span');
			klantNaam.innerHTML = "Naam: "+product.naam +"<br>";
			klantNaam.setAttribute('class', "klantNaam");
			klantDiv.appendChild(klantNaam);

			var klantGeslacht = document.createElement('span');
			klantGeslacht.innerHTML = "Hoeveelheid: "+product.hoeveelheid+"<br>";
			klantGeslacht.setAttribute('class', "klantGeslacht");
			klantDiv.appendChild(klantGeslacht);

			if(i > 5){
				document.getElementById("inventarisLijstLinks").appendChild(klantDiv);
			} else{
				document.getElementById("inventarisLijstRechts").appendChild(klantDiv);
			}
		}
	}).catch(function() {
		// De gebruiker is niet ingelogt
		alert("niet meer ingelogd");
	});
}

function createProduct(){
	var formData = new FormData(document.getElementById("ProductToevoegenForm"));
	var encData = new URLSearchParams(formData.entries());
	var fetchoptions = {
			method: 'POST',
			body: encData,
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/product", fetchoptions)
	.then(function (response){
		if(response.ok){
			alert("toegevoegd");
		} else{
			alert("kapot fout");
		}
	}).catch(function() {
		// De gebruiker is niet ingelogt
		alert("niet meer ingelogd");
	});
}

