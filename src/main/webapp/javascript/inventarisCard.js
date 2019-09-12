function getAlleProducten(paginaNummer){
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/klantenZoekReq/" + paginaNummer, fetchoptions)
	.then(response => response.json())
	.then(function(producten){
		
	}).catch(function() {
		// De gebruiker is niet ingelogt
		alert("niet meer ingelogd");
	});
}