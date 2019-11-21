document.getElementById("registreer").addEventListener("click", function () {
	console.log("ssss");
	var formData = new FormData(document.querySelector("#klantenInformatie"));
	var encData = new URLSearchParams(formData.entries());
	alert(encData);
		fetch("restservices/setup/registreer", { method: 'POST', body: encData })
			.then(function (response) {
				if (response.ok) {
					// het inloggen is geslaagdt
					return response.json()
				}
			})
});