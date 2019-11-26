document.getElementById("registreer").addEventListener("click", function () {
	var formData = new FormData(document.querySelector("#informatie"));
	var encData = new URLSearchParams(formData.entries());
	alert(encData);
		fetch("restservices/setup/registreer", { method: 'POST', body: encData })
			.then(function (response) {
				if (response.ok) {
					return response.json()
				}
			})
});