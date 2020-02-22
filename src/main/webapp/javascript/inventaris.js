function onload(){ 
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

function getAlleProducten(paginaNummer){
	var search = document.getElementById("zoekProductenInput").value;
	if(search == ""){
		search = "-";
	}
	var data = "page="+paginaNummer+"&search="+search;
	alert(data);
	document.getElementById("inventarisItems").innerHTML = "";
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/product/producten/" + data, fetchoptions)
	.then(response => response.json())
	.then(function(producten){
		var i = 0;
		for(let product of producten){
			i++;
			var productSpan = document.createElement('span');
			productSpan.setAttribute('id', product.id);
			productSpan.setAttribute('class', "inventarisItem");
			
			var productHoeveelheid = document.createElement('input');
			productHoeveelheid.type = "number";
			productHoeveelheid.value = product.hoeveelheid;
			productHoeveelheid.setAttribute('class', "productHoeveelheid");
			productSpan.appendChild(productHoeveelheid);
			
			var productNaam = document.createElement('span');
			productNaam.innerHTML = product.naam;
			productNaam.setAttribute('class', "productNaam");
			productSpan.appendChild(productNaam);

			document.getElementById("inventarisItems").appendChild(productSpan);
		}
	}).catch(function() {
		// De gebruiker is niet ingelogt
		alert("niet meer ingelogd");
	});
}

function createProduct(){
	var formData = new FormData(document.getElementById("ProductToevoegenForm"));
	var encData = new URLSearchParams(formData.entries());
	var fetchoptions = {
			method: 'POST',
			body: encData,
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/product", fetchoptions)
	.then(function (response){
		if(response.ok){
			alert("toegevoegd");
		} else{
			alert("kapot fout");
		}
	}).catch(function() {
		// De gebruiker is niet ingelogt
		alert("niet meer ingelogd");
	});
}

