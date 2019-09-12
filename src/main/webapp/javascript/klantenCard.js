function getAlleKlanten(){
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/alleKlanten/"+1, fetchoptions)
	.then(response => response.json())
	.then(function(klanten){
		for(let klant of klanten){
			var klantDiv = document.createElement('div');
			klantDiv.setAttribute('id', klant.id);
			klantDiv.setAttribute('class', "klantDiv");
			
			
			var klantNaamLijst = document.createElement('span');
			klantNaamLijst.innerHTML = klant.naam;
			klantNaamLijst.setAttribute('class', "klantNaamLijst");
			klantDiv.appendChild(klantNaamLijst);

			var klantLastSeen = document.createElement('span');
			klantLastSeen.innertHTML = klant.lastSeen;
			klantLastSeen.setAttribute('class', "klantLastSeen");
			klantDiv.appendChild(klantLastSeen);
			
			document.getElementById("klantenLijst").appendChild(klantDiv);
		}
	}).catch(function() {
		alert("ging iets fout");
		// De gebruiker is niet ingelogt
	});
}

function zoekKlanten(){
	// Zoek klanten door de zoekbalk
	document.getElementById("klantenLijst").innerHTML="";
	var requestKlant = document.getElementById("zoekKlantInput").value;
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/klantenZoekReq/" + requestKlant, fetchoptions)
	.then(response => response.json())
	.then(function(klanten){
		for(let klant of klanten){
			var klantDiv = document.createElement('div');
			klantDiv.setAttribute('id', klant.id);
				klantDiv.setAttribute('class', "klantDiv");
			klantDiv.innerHTML = klant.naam;			
			document.getElementById("klantenLijst").appendChild(klantDiv);
		}
	}).catch(function() {
		alert("ging iets fout");
		// De gebruiker is niet ingelogt
	});
}

function terugKnopKlanten(){
	
}




	
