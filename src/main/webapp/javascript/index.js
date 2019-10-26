document.getElementById("loginNav").addEventListener("click", function(){
	login();
})

document.getElementById("anulleer").addEventListener("click", function(){
	errorMessage("geanulleerd");
})



function login(){
	document.getElementById("loginModal").style.display = "block";
	document.getElementById("loginButton").addEventListener("click", function(){
		var formData = new FormData(document.querySelector("#loginForm"));
		var encData = new URLSearchParams(formData.entries());

		fetch("restservices/authentication", {method : 'POST', body : encData})
		.then(function(response){
			if(response.ok){
				// het inloggen is geslaagdt
				return response.json()
			} else {
				// het inloggen is niet gelukt
				alert("er ging iets verkeerd");
			};
		})
		.then(function(myJson){
			// de JWTKey wordt in de sessionStorage gezet en de actor wordt
			// doorverbonden naar de afspraken.html pagina
			window.sessionStorage.setItem("sessionToken", myJson.JWT);
			var jwtData = sessionStorage.getItem("sessionToken").split('.')[1]
			var decodedJwtJsonData = window.atob(jwtData)
					// rol een warde geven
		// decodedJwtJsonData.role werkte niet
			var role = "";
			let array = decodedJwtJsonData.split(",");
			for(var item of array){
				let items = item.split('"');
					if(items[1] == "role"){
					role = items[3];
				}
			}
			
			if(role == "user"){
				window.location.href = "home.html";
			} else if(role == "admin"){
				window.location.href = "adminHome.html";
			}
		})
	})
}

document.getElementById("registreerNav").addEventListener("click", function(){
	alert("moet nog gemaakt worden G");
})

document.getElementById("contactNav").addEventListener("click", function(){
	alert("moet nog gemaakt worden G");
})

document.getElementById("verkoopPuntBeschikbaar").addEventListener("click", function(){
	smoothScroll(document.getElementById('marketingPuntUitleg'));
});

document.getElementById("verkoopPuntVoorraad").addEventListener("click", function(){
	smoothScroll(document.getElementById('marketingPuntUitleg'));
});

document.getElementById("verkoopPuntMarketing").addEventListener("click", function(){
	smoothScroll(document.getElementById('marketingPuntUitleg'));
});

document.getElementById("verkoopPuntAgenda").addEventListener("click", function(){
	smoothScroll(document.getElementById('agendaPuntUitleg'));
});

document.getElementById("verkoopPuntKlantenbeheer").addEventListener("click", function(){
	smoothScroll(document.getElementById('klantBeheerPuntUitleg'));
});

document.getElementById("verkoopPuntStatistieken").addEventListener("click", function(){
	smoothScroll(document.getElementById('statistiekenPuntUitleg'));
});

document.getElementById("verkoopPuntTelefoon").addEventListener("click", function(){
	smoothScroll(document.getElementById('tijdVerkoopPuntDivUitleg'));
});

document.getElementById("verkoopPuntWebsite").addEventListener("click", function(){
	smoothScroll(document.getElementById('websiteVerkoopPuntDivUitleg'));
});



function smoothScroll(target) {
    var scrollContainer = target;
    do { // find scroll container
        scrollContainer = scrollContainer.parentNode;
        if (!scrollContainer) return;
        scrollContainer.scrollTop += 1;
    } while (scrollContainer.scrollTop == 0);

    var targetY = 0;
    do { // find the top of target relatively to the container
        if (target == scrollContainer) break;
        targetY += target.offsetTop;
    } while (target = target.offsetParent);

    scroll = function(c, a, b, i) {
        i++; if (i > 30) return;
        c.scrollTop = a + (b - a) / 30 * i;
        setTimeout(function(){ scroll(c, a, b, i); }, 20);
    }
    // start scrolling
    scroll(scrollContainer, scrollContainer.scrollTop, targetY, 0);
}