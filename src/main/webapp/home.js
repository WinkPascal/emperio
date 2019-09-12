
//eerste wat er gedaan wordt bij het laden van de pagina
function onload(){
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/afsprakenVandaag", fetchoptions)
	.then(response => response.json())
	.then(function(afspraken){
		succesMessage("Succesvol ingelogd.");
	}).catch(function() {
		// De gebruiker is niet ingelogt
		alert("niet meer ingelogd");
	});
}

document.getElementById("inventarisCard").addEventListener("click", function() {
	document.getElementById("inventarisModal").style.display = "block";
	// de producten voor de eerste pagina worden opgehaalt
	getAlleProducten(1);
	
	document.getElementById("volgendeKlanten").addEventListener("click", function() {
		alert();
		volgendeKnopInplannen();
	})
	
	document.getElementById("terugKlanten").addEventListener("click", function() {
		alert();
		terugKnopInplannen();
	})
})

document.getElementById("klantenCard").addEventListener("click", function() {
	document.getElementById("KlantenModel").style.display = "block";
	getAlleKlanten();
	
	document.getElementById("zoekKlantButton").addEventListener("click", function() {
		zoekKlanten();
	})
	
	document.getElementById("volgendeKlanten").addEventListener("click", function() {
		alert();
		volgendeKnopInplannen();
	})
	
	document.getElementById("terugKlanten").addEventListener("click", function() {
		alert();
		terugKnopInplannen();
	})
})


document.getElementById("inplannenCard").addEventListener("click", function() {
	document.getElementById("inplannenModal").style.display = "block";
	document.getElementById("inplannenGeslacht").style.display = "block";

	inplannenGeslachtKiezen();
	document.getElementById("volgendeInplannen").addEventListener("click", function() {
		volgendeKnopInplannen();
	})
	
	document.getElementById("terugInplannen").addEventListener("click", function() {
		terugKnopInplannen();
	})
})

document.getElementById("logoutButton").addEventListener("click", function() {

})


// settings card
document.getElementById("settingsButton").addEventListener("click", function() {
	var settings = document.getElementById("settings");
	var btn = document.getElementById("myBtn");
	var span = document.getElementsByClassName("close")[0];

	settings.style.display = "block";
	// de sluitknop
	span.onclick = function() {
		settings.style.display = "none";
	}
	toevoegingen = 0;

	voegBehandelingItemToe();
})
