//eerste wat er gedaan wordt bij het laden van de pagina
//hier worden de afspraken van de eerste dag geladen
function onload(){
	var date = new Date();
	var dagDate = date.getFullYear() +'-'+ date.getMonth() +'-'+ date.getDate();
	//ophalen van afspraken vandaag
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/afspraak/afsprakenByDate/"+dagDate, fetchoptions)
	.then(response => response.json())
	.then(function(afspraken){
		var afsprakenLijst = document.getElementById("afsprakenVandaagLijst");
		for(let afspraak of afspraken){
			document.getElementById("geenAfsprakenGevonden").style.display="none";
			
			var afspraakSpan = document.createElement('span');
			
			afspraakSpan.setAttribute('id', afspraak.id);
			afspraakSpan.setAttribute('class', 'afspraakSpan');

			var tekst = "Klant: "+afspraak.klantNaam +"" +
				   "<br> Tijd: "+ afspraak.timestamp +"" +
			       "<br> Lengte: "+afspraak.lengte +""+
			       "<br> Prijs: "+afspraak.prijs +"<br>";
			afspraakSpan.innerHTML =  tekst;
			if(afspraak.prijs > 30){
				//prijs van de afspraak is groter dan 30
				afspraakSpan.style.backgroundColor = "#FF5733" ;
				 
			} else if(afspraak.prijs > 10){
				//prijs van afspraak is groter dan 10
				afspraakSpan.style.backgroundColor = "blue";

			} else{
				//prijs van afspraak is kleiner dan 10
				afspraakSpan.style.backgroundColor = "green";
			}
			//beslissen wat de kleur wordt
			afsprakenLijst.appendChild(afspraakSpan);
			
			//als op de afspraak wordt geklikt
			document.getElementById(afspraak.id).addEventListener("click", function(){
				document.getElementById("afspraakModal").style.display = "block";
				var fetchoptions = {
						headers: {
							'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
						}
					}
				fetch("restservices/afspraak/getAfspraak/" + afspraak.id, fetchoptions)
				.then(response => response.json())
				.then(function(afspraak){
					//klant info
					document.getElementById("naamInfo").innerHTML = afspraak.klantNaam;
					document.getElementById("emailInfo").innerHTML = afspraak.klantEmail;
					document.getElementById("geslachtInfo").innerHTML = afspraak.klantGeslacht;
					document.getElementById("telefoonInfo").innerHTML = afspraak.klantTelefoon;
					document.getElementById("aantalAfsprakenInfo").innerHTML = afspraak.aantalAfspraken;
					document.getElementById("hoeveelheidInkomstenInfo").innerHTML = afspraak.hoeveelheidInkomsten;
					//afspraak info
					document.getElementById("prijsInfo").innerHTML = afspraak.totaalPrijs;
					document.getElementById("lengteInfo").innerHTML = afspraak.totaalLengte;
					document.getElementById("datumInfo").innerHTML = afspraak.datum;
					document.getElementById("tijdInfo").innerHTML = afspraak.tijd;		
					//behandelingen
					var behandelingenLijst = document.getElementById("behandelingenLijst");
					for(let behandeling of afspraak.behandelingen){
						var behandelingSpan = document.createElement('span');
						behandelingSpan.setAttribute('class', 'behandelingSpan');
						behandelingSpan.innerHTML = behandeling.naam + " " + behandeling.lengte;
						behandelingenLijst.appendChild(behandelingSpan);
					}
					document.getElementById("sluitAfspraakInfo").addEventListener("click", function(){
						document.getElementById("afspraakModal").style.display = "none";
						document.getElementById("naamInfo").innerHTML = "";
						document.getElementById("emailInfo").innerHTML = "";
						document.getElementById("geslachtInfo").innerHTML = "";
						document.getElementById("telefoonInfo").innerHTML = "";
						//afspraak info
						document.getElementById("prijsInfo").innerHTML = "";
						document.getElementById("lengteInfo").innerHTML = "";
						document.getElementById("datumInfo").innerHTML = "";
						document.getElementById("tijdInfo").innerHTML = "";
						behandelingenLijst.innerHTML="";
					})
					document.getElementById("mailKlantAfspraak").addEventListener("click", function(){
						
					})
				})		
			})
		}
	}).catch(function() {
		alert("fout G");
	});
}

//onclick listener voor aanklikken card
document.getElementById("inventarisCard").addEventListener("click", function() {
	location.href = 'inventaris.html';
})

//onclick listener voor klanten card
document.getElementById("klantenCard").addEventListener("click", function() {
	location.href = 'klanten.html';
})

//starten rooster proces
document.getElementById("afsprakenCard").addEventListener("click", function(){
	location.href = 'afspraken.html';
})

//starten rooster proces
document.getElementById("inkomstenCard").addEventListener("click", function(){
	location.href = 'statestieken.html';
})

document.getElementById("inplannenCard").addEventListener("click", function() {
	inplannenCard();
})

function inplannenCard(){
	document.getElementById("inplannenModal").style.display = "block";
	document.getElementById("inplannenGeslacht").style.display = "block";
	inplannenGeslachtKiezen();
}

document.getElementById("klantenPaginaBeheer").addEventListener("click", function() {
	onloadKlantenBeheer();
})

document.getElementById("logoutButton").addEventListener("click", function() {
	logoutButton();
})

function logoutButton(){ 
	window.sessionStorage.setItem("sessionToken", "");
	location.href = 'index.html';
}

// settings card
document.getElementById("instellingenButton").addEventListener("click", function() {
	settingsButton();
})

//starten instellingen proces
function settingsButton(){
	var settings = document.getElementById("settings");
	var btn = document.getElementById("myBtn");
	var span = document.getElementsByClassName("close")[0];

	settings.style.display = "block";
	
	//de opties openen en sluiten
	var coll = document.getElementsByClassName("collapsible");
	var i;

	for (i = 0; i < coll.length; i++) {
	  coll[i].addEventListener("click", function() {
	    this.classList.toggle("active");
	    var content = this.nextElementSibling;
	    if (content.style.maxHeight){
	      content.style.maxHeight = null;
	    } else {
	      content.style.maxHeight = content.scrollHeight + "px";
	    } 
	  });
	}
	
	// de sluitknop
	span.onclick = function() {
		settings.style.display = "none";
	}
	toevoegingen = 0;

	voegBehandelingItemToe();
}

//hier worden behandelingen toegevoegd
document.getElementById("inplannenBehnadelingToevoegen").addEventListener("click", function(){
	behandelignToevoegenForm();
})

function behandelignToevoegenForm(){
	geslachten = [];
	
	document.getElementById("BehandelingToevoegenModal").style.display = "block";
	document.getElementById("inplannenModal").style.display = "none";
	
	document.getElementById("annuleerBehandelingAnnuleren").addEventListener("click", function(){
		document.getElementById("BehandelingToevoegenModal").style.display = "none";
		document.getElementById("inplannenModal").style.display = "block";
	})
	
	document.getElementById("geslachtManToevoegen").addEventListener("click", function(){
		if(behandelingManager("man")){
			document.getElementById("geslachtManToevoegen").style.background = "blue";
		} else{
			document.getElementById("geslachtManToevoegen").style.background = "white";
		}
	})
	document.getElementById("geslachtVrouwToevoegen").addEventListener("click", function(){
		if(behandelingManager("vrouw")){
			document.getElementById("geslachtVrouwToevoegen").style.background = "blue";
		} else{
			document.getElementById("geslachtVrouwToevoegen").style.background = "white";
		}
	})	
	document.getElementById("geslachtJongenToevoegen").addEventListener("click", function(){
		if(behandelingManager("jongen")){
			document.getElementById("geslachtJongenToevoegen").style.background = "blue";
		} else{
			document.getElementById("geslachtJongenToevoegen").style.background = "white";
		}
	})	
	document.getElementById("geslachtMeisjeToevoegen").addEventListener("click", function(){
		if(behandelingManager("meisje")){
			document.getElementById("geslachtMeisjeToevoegen").style.background = "blue";
		} else{
			document.getElementById("geslachtMeisjeToevoegen").style.background = "white";
		}
	})

	function behandelingManager(geslacht){
		if(geslachten.includes(geslacht)){
			for( var i = 0; i < geslachten.length; i++){ 
				if (geslachten[i] === geslacht) {
					geslachten.splice(i, 1); 
					i--;
					return false;
				}
			}
			
		} else{
			geslachten.push(geslacht);
			return true;
		}
	}
}

