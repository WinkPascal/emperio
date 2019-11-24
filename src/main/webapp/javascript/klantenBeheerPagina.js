function onloadKlantenBeheer(){
	document.getElementById("klantenPaginaBeheerModal").style.display = "block";
	
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/klantenBeheer/getKlantPaginaSettings", fetchoptions)
	.then(response => response.json())
	.then(function(checkmarks){
		if(checkmarks.verplichtContactVeld == "email"){
			emailContact = document.getElementById("emailPrimary").checked = true;			
			document.getElementById("phonePrimary").checked = false;
		} else{
			document.getElementById("phonePrimary").checked = true;
			emailContact = document.getElementById("emailPrimary").checked = false;			
		}
		
		document.getElementById("telefoonInvoerVeld").checked = checkmarks.invoerveldTelefoon;
		document.getElementById("emailInvoerVeld").checked = checkmarks.invoerveldEmail;
		document.getElementById("adresInvoerVeld").checked = checkmarks.invoerveldAdres;
	})
}

document.getElementById("opslaanKlantenBeheer").addEventListener("click", function(){
	//invoervelden
	var telefoon = document.getElementById("telefoonInvoerVeld").checked;
	var email = document.getElementById("emailInvoerVeld").checked;
	var adres = document.getElementById("adresInvoerVeld").checked;
	
	var formData = new FormData();
	if(emailContact == true){
		formData.append("contact", "email");
	} else{
		formData.append("contact", "telefoon");
	}

	formData.append("telefoon", telefoon);
	formData.append("email", email);
	formData.append("adres", adres);
	var encData = new URLSearchParams(formData.entries());
	
	var fetchoptions = {
			method: 'POST',
			body: encData,
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	// De wijziging wordt verstuurt naar de backend
	fetch("restservices/klantenBeheer", fetchoptions)
	.then(function (response){
		if(response.status == 202){
			succesMessage("De klanten pagina is gewijzigt");
		} else{
			errorMessage(response.status);
		}
	})
});

document.getElementById("sluitKlantenBeheer").addEventListener("click", function(){
	document.getElementById("klantenPaginaBeheerModal").style.display = "none";
});
