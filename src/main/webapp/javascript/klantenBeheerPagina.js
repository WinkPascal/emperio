function onloadKlantenBeheer(){
	document.getElementById("klantenPaginaBeheerModal").style.display = "block";
}


document.getElementById("opslaanKlantenBeheer").addEventListener("click", function(){
	//verplichte contact veld
	var emailContact = document.getElementById("emailPrimary").value;
	var telefoonContact = document.getElementById("phonePrimary").value; 
		
	//invoervelden
	var telefoon = document.getElementById("telefoonInvoerVeld").value;
	var email = document.getElementById("emailInvoerVeld").value;
	var adres = document.getElementById("adresInvoerVeld").value;
	
	var formData = new FormData();
	if(emailContact == "on"){
		formData.append("contact", "email");		
	} else{
		formData.append("contact", "telefoon");
	}
	formData.append("email", email);
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
		if(response.ok){
			alert("goed");
		} else{
			alert("fout");
		}
	})
	alert(adres);
});

document.getElementById("sluitKlantenBeheer").addEventListener("click", function(){
	document.getElementById("klantenPaginaBeheerModal").style.display = "none";
});
