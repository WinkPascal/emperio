function login() {
	document.getElementById("loginModal").style.display = "block";
	document.getElementById("loginButton").addEventListener("click", function () {
		var formData = new FormData(document.querySelector("#loginForm"));
		var encData = new URLSearchParams(formData.entries());

		fetch("restservices/authentication", { method: 'POST', body: encData })
			.then(function (response) {
				var responseStatus;
				if (response.ok) {
					// het inloggen is geslaagdt
					responseStatus = "ingelogt";
					return response.json()
				} if (response.status == 412) {
					responseStatus = "setup";
					return response.json()
				} else {
					responseStatus = "fout";
					errorMessage(response.status);
				}
			}).then(function (myJson) {
				// de JWTKey wordt in de sessionStorage gezet en de actor wordt
				// doorverbonden naar de afspraken.html pagina
				window.sessionStorage.setItem("sessionToken", myJson.JWT);
				var jwtData = sessionStorage.getItem("sessionToken").split('.')[1]
				var decodedJwtJsonData = window.atob(jwtData)
				// rol een warde geven
				// decodedJwtJsonData.role werkte niet
				var role = "";
				let array = decodedJwtJsonData.split(",");
				for (var item of array) {
					let items = item.split('"');
					if (items[1] == "role") {
						role = items[3];
					}
				}

				if (role == "user") {
					window.location.href = "home.html";
				} else if (role == "admin") {
					window.location.href = "adminHome.html";
				} else if (role == "setup") {
					window.location.href = "setup.html";
				}
			})
	})
	localStorage.setItem("klasse1", 10);
	localStorage.setItem("klasse2", 20);
	localStorage.setItem("klasse3", 30);
}


document.getElementById("loginNav").addEventListener("click", function () {
	login();
})

document.getElementById("annuleerLogin").addEventListener("click", function () {
	document.getElementById("loginModal").style.display = "none";
})

document.getElementById("registreerNav").addEventListener("click", function () {
	window.location.href = "registreren.html";
})

document.getElementById("contactNav").addEventListener("click", function () {
	document.getElementById("contactForm").style.display="block";
})

document.getElementById("verstuurContact").addEventListener("click", function () {
	succesMessage("Dankuwel voor uw vraag! \n" +
			"Wij doen ons best om zo snel mogelijk een antwoord te geven.");
})

document.getElementById("closeContact").addEventListener("click", function () {
	document.getElementById("contactForm").style.display="none";
})


document.getElementById("verkoopPuntBeschikbaar").addEventListener("click",
	function () {
		smoothScroll(document.getElementById('marketingPuntUitleg'));
	});

document.getElementById("verkoopPuntVoorraad").addEventListener("click",
	function () {
		smoothScroll(document.getElementById('marketingPuntUitleg'));
	});

document.getElementById("verkoopPuntMarketing").addEventListener("click",
	function () {
		smoothScroll(document.getElementById('marketingPuntUitleg'));
	});

document.getElementById("verkoopPuntAgenda").addEventListener("click",
	function () {
		smoothScroll(document.getElementById('agendaPuntUitleg'));
	});

document.getElementById("verkoopPuntKlantenbeheer").addEventListener("click",
	function () {
		smoothScroll(document.getElementById('klantBeheerPuntUitleg'));
	});

document.getElementById("verkoopPuntStatistieken").addEventListener("click",
	function () {
		smoothScroll(document.getElementById('statistiekenPuntUitleg'));
	});

document.getElementById("verkoopPuntTelefoon").addEventListener("click",
	function () {
		smoothScroll(document.getElementById('tijdVerkoopPuntDivUitleg'));
	});

document.getElementById("verkoopPuntWebsite")
	.addEventListener(
		"click",
		function () {
			smoothScroll(document
				.getElementById('websiteVerkoopPuntDivUitleg'));
		});

function smoothScroll(target) {
	var scrollContainer = target;
	do { // find scroll container
		scrollContainer = scrollContainer.parentNode;
		if (!scrollContainer)
			return;
		scrollContainer.scrollTop += 1;
	} while (scrollContainer.scrollTop == 0);

	var targetY = 0;
	do { // find the top of target relatively to the container
		if (target == scrollContainer)
			break;
		targetY += target.offsetTop;
	} while (target = target.offsetParent);

	scroll = function (c, a, b, i) {
		i++;
		if (i > 30)
			return;
		c.scrollTop = a + (b - a) / 30 * i;
		setTimeout(function () {
			scroll(c, a, b, i);
		}, 20);
	}
	// start scrolling
	scroll(scrollContainer, scrollContainer.scrollTop, targetY, 0);
}