//eerste wat er gedaan wordt bij het laden van de pagina
//hier worden de afspraken van de eerste dag geladen
function onload(){
	var date = new Date();
	var dagDate = date.getFullYear() +'-'+ date.getMonth() +'-'+ date.getDate();
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/afsprakenByDate/"+dagDate, fetchoptions)
	.then(response => response.json())
	.then(function(afspraken){
		var afsprakenLijst = document.getElementById("afsprakenVandaagLijst");
		for(let afspraak of afspraken){
			var afspraakSpan = document.createElement('span');
			
			afspraakSpan.setAttribute('id', afspraak.id);
			afspraakSpan.setAttribute('class', 'afspraakSpan');

			var tekst = "Klant: "+afspraak.klantNaam +"" +
				   "<br> Tijd: "+ afspraak.timestamp +"" +
			       "<br> Lengte: "+afspraak.lengte +""+
			       "<br> Prijs: "+afspraak.prijs +"<br>";
			afspraakSpan.innerHTML =  tekst;
			afsprakenLijst.appendChild(afspraakSpan);
			document.getElementById(afspraak.id).addEventListener("click", function(){
				
				document.getElementById("afspraakModal").style.display = "block";
				var fetchoptions = {
						headers: {
							'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
						}
					}
				fetch("restservices/service/getAfspraak/" + afspraak.id, fetchoptions)
				.then(response => response.json())
				.then(function(afspraak){
						alert(afspraak.klantNaam);
				})			
			})
		}
	}).catch(function() {
		alert("fout G");
	});
}

//onclick listener voor aanklikken card
document.getElementById("inventarisCard").addEventListener("click", function() {
	inventarisCard();
})
//starten inventaris proces
function inventarisCard(){
	document.getElementById("inventarisModal").style.display = "block";
	// de producten voor de eerste pagina worden opgehaalt
	var paginaNummerInventaris = 1;
	getAlleProducten(paginaNummerInventaris);
	document.getElementById("terugButtonInventaris").addEventListener("click", function() {
		if(paginaNummerInventaris == 1){
			alert("kan niet terug");
		}
		paginaNummerInventaris =paginaNummerInventaris - 1;

		volgendeKnopInplannen(paginaNummerInventaris);
	})
	
	document.getElementById("volgendeButtonInventaris").addEventListener("click", function() {
		paginaNummerInventaris++;
		terugKnopInplannen(paginaNummerInventaris);
	})
	
	document.getElementById("voegProductToe").addEventListener("click", function(){
		document.getElementById("inventarisModal").style.display = "none";
		document.getElementById("ProductToevoegenModal").style.display = "block";
		
		document.getElementById("productToevoegenKnop").addEventListener("click", function(){
			createProduct();
		})
		document.getElementById("annulerenProductToevoegenKnop").addEventListener("click", function(){
			document.getElementById("inventarisModal").style.display = "block";
			document.getElementById("ProductToevoegenModal").style.display = "none";
		})
	});
}

//onclick listener voor klanten card
document.getElementById("klantenCard").addEventListener("click", function() {
	klantenCard();
})

//starten klanten proces
function klantenCard(){
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
}

//starten rooster proces
document.getElementById("afsprakenCard").addEventListener("click", function(){
	location.href = 'afspraken.html';
})

//starten rooster proces
document.getElementById("inkomstenCard").addEventListener("click", function(){
	location.href = 'inkomsten.html';
})



document.getElementById("inplannenCard").addEventListener("click", function() {
	inplannenCard();
})

function inplannenCard(){
	document.getElementById("inplannenModal").style.display = "block";
	document.getElementById("inplannenGeslacht").style.display = "block";
	inplannenGeslachtKiezen();
}

document.getElementById("logoutButton").addEventListener("click", function() {
	logoutButton();
})

function logoutButton(){ 
	window.sessionStorage.setItem("sessionToken", "");
	location.href = 'index.html';
}

// settings card
document.getElementById("settingsButton").addEventListener("click", function() {
	settingsButton();
})

//starten instellingen proces
function settingsButton(){
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
}

