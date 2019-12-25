function loadSettings(){
	aanpassenTijdenHandlers();
	determineOpeningsTijd();
	determineSluitingsTijd()
	createTimePicker("openingsTijdMaandag");
	
	checkBoxes = ["geslotenMaandag", "geslotenDinsdag", "geslotenWoensdag","geslotenDonderdag","geslotenVrijdag","geslotenZaterdag", "geslotenZondag"]
	tijdenAreas = ["tijdenMaandag", "tijdenDinsdag", "tijdenWoensdag","tijdenDonderdag","tijdenVrijdag","tijdenZaterdag", "tijdenZondag"]
	openingsTijdSpans = ["openingsTijdMaandag","openingsTijdDinsdag","openingsTijdWoensdag","openingsTijdDonderdag","openingsTijdVrijdag","openingsTijdZaterdag","openingsTijdZondag"];
	sluitingsTijdSpans = ["sluitingsTijdMaandag", "sluitingsTijdDinsdag", "sluitingsTijdWoensdag", "sluitingsTijdDonderdag", "sluitingsTijdVrijdag", "sluitingsTijdZaterdag", "sluitingsTijdZondag"];
	for (i = 0; i < checkBoxes.length; i++) {
		checkboxChecker(tijdenAreas[i], checkBoxes[i]);
	}
}


function getPlanInfo(){
		var fetchoptions = {
		headers: {
			'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
		}
	}
	fetch("restservices/instellingen/getAfspraakInfo", fetchoptions)
		.then(response => response.json())
		.then(function (instellingen) {
			console.log(instellingen);
			document.getElementById("telefoonInvoerVeld").checked = instellingen.telefoonKlantInvoer;
			document.getElementById("emailInvoerVeld").checked = instellingen.emailKlantInvoer;
			document.getElementById("adresInvoerVeld").checked = instellingen.adresKlantInvoer;

			document.getElementById("emailBedrijfInput").value = instellingen.bedrijfsEmail;
			document.getElementById("telefoonBedrijfInput").value = instellingen.bedrijfsTelefoon;
			document.getElementById("adresBedrijfInput").value = instellingen.bedrijfsAdres;
		})
		function checkByName(name, color){
		 var klasse =document.getElementsByName(name);
		for (i = 0; i < klasse.length; i++) {
			if (klasse[i].value == color) {
				klasse[i].checked = true;   
			}
		}
	}
}

function saveInplanInfo(){
	var formData = new FormData();

	var telefoonKlant = document.getElementById("telefoonInvoerVeld").checked;
	formData.append("telefoonKlant", telefoonKlant);
	var emailKlant = document.getElementById("emailInvoerVeld").checked;
	formData.append("emailKlant", emailKlant);
	var adresKlant = document.getElementById("adresInvoerVeld").checked;
	formData.append("adresKlant", adresKlant);
	
	var emailBedrijfInput = document.getElementById("emailBedrijfInput").value;
	formData.append("bedrijfsEmail", emailBedrijfInput);
	var telefoonBedrijfInput = document.getElementById("telefoonBedrijfInput").value;
	formData.append("bedrijfsTelefoon", telefoonBedrijfInput);
	var adresBedrijfInput = document.getElementById("adresBedrijfInput").value;
	formData.append("bedrijfsAdres", adresBedrijfInput);
	
	var encData = new URLSearchParams(formData.entries());
	var fetchoptions = {
		method: 'POST',
		body: encData,
		headers: {
			'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
		}
	}
	// De wijziging wordt verstuurt naar de backend
	fetch("restservices/instellingen/saveAfspraakInfo", fetchoptions)
	.then(function (response){
		if(response.ok){
			succesMessage("De klanten pagina is gewijzigt");
			return true;
		} else{
			errorMessage(response.status);
			return false;
		}
	});
}

/*====================================================================================*/
/*===============================saveAfspraakColor================================================*/
/*====================================================================================*/

function saveAfspraakColor(){ 
	var formData = new FormData();

	var kleurKlasse1 = findCheckedByName("kleurKlasse1");
	formData.append("kleurKlasse1", kleurKlasse1);
	formData.append("tot1", document.getElementById("maxPrijs1").value);

	var kleurKlasse2 = findCheckedByName("kleurKlasse2");
	formData.append("kleurKlasse2", kleurKlasse2);
	formData.append("tot2", document.getElementById("maxPrijs2").value);

	var kleurKlasse3 = findCheckedByName("kleurKlasse3");
	formData.append("kleurKlasse3", kleurKlasse3);
	
	function findCheckedByName(name){
		 var kleurKlasse1 =document.getElementsByName(name);
		for (i = 0; i < kleurKlasse1.length; i++) {
			if (kleurKlasse1[i].checked) {
				return kleurKlasse1[i].value;        
			}
		}
	}

	var encData = new URLSearchParams(formData.entries());
	var fetchoptions = {
		method: 'POST',
		body: encData,
		headers: {
			'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
		}
	}
	// De wijziging wordt verstuurt naar de backend
	fetch("restservices/setup/saveAfspraakColor", fetchoptions)
	.then(function (response){
		if(response.ok){
			succesMessage("De klanten pagina is gewijzigt");
			return true;
		} else{
			errorMessage(response.status);
			return false;
		}
	});
}



/*====================================================================================*/
/*===============================dagen================================================*/
/*====================================================================================*/


function createTimePicker(id){
	document.addEventListener('DOMContentLoaded', function() {
		var elems = document.querySelectorAll('#'+id);
		var options = {twelveHour : false};
		var instances = M.Timepicker.init(elems, options);
	});
}

function saveDagen(){
	var formData = new FormData(document.getElementById("dagenForm"));

	if(document.getElementById("geslotenMaandag").checked){
		formData.append("openingsTijdMaandag", document.getElementById("openingsTijdMaandag").innerHTML);
		formData.append("sluitingsTijdMaandag", document.getElementById("sluitingsTijdMaandag").innerHTML);
	}
	if(document.getElementById("geslotenDinsdag").checked){
		formData.append("openingsTijdDinsdag", document.getElementById("openingsTijdDinsdag").innerHTML);
		formData.append("sluitingsTijdDinsdag", document.getElementById("sluitingsTijdDinsdag").innerHTML);
	}
	if(document.getElementById("geslotenWoensdag").checked){
		formData.append("openingsTijdWoensdag", document.getElementById("openingsTijdWoensdag").innerHTML);
		formData.append("sluitingsTijdWoensdag", document.getElementById("sluitingsTijdWoensdag").innerHTML);
	}
	if(document.getElementById("geslotenDonderdag").checked){
		formData.append("openingsTijdDonderdag", document.getElementById("openingsTijdDonderdag").innerHTML);
		formData.append("sluitingsTijdDonderdag", document.getElementById("sluitingsTijdDonderdag").innerHTML);
	}
	if(document.getElementById("geslotenVrijdag").checked){
		formData.append("openingsTijdVrijdag", document.getElementById("openingsTijdVrijdag").innerHTML);
		formData.append("sluitingsTijdVrijdag", document.getElementById("sluitingsTijdVrijdag").innerHTML);
	}
	if(document.getElementById("geslotenZaterdag").checked){
		formData.append("openingsTijdZaterdag", document.getElementById("openingsTijdZaterdag").innerHTML);
		formData.append("sluitingsTijdZaterdag", document.getElementById("sluitingsTijdZaterdag").innerHTML);
	}
	if(document.getElementById("geslotenZondag").checked){
		formData.append("openingsTijdZondag", document.getElementById("openingsTijdZondag").innerHTML);
		formData.append("sluitingsTijdZondag", document.getElementById("sluitingsTijdZondag").innerHTML);
	}

	var encData = new URLSearchParams(formData.entries());
	alert(encData);
	var fetchoptions = {
		method: 'POST',
		body: encData,
		headers: {
			'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
		}
	}
	// De wijziging wordt verstuurt naar de backend
	fetch("restservices/instellingen/dagen", fetchoptions)
	.then(function (response){
		if(response.ok){
			succesMessage("De klanten pagina is gewijzigt");
			return true;
		} else{
			errorMessage(response.status);
			return false;
		}
	});
}


function aanpassenTijdenHandlers(){
	var okButton = document.getElementById("okButton");

	document.getElementById("TijdMaandagAanpassen").addEventListener("click", function () {
		document.getElementById("tijdKiezen").style.display = "block";
		okButton.setAttribute("data-dag", "maandag");
	});
	document.getElementById("TijdDinsdagAanpassen").addEventListener("click", function () {
		document.getElementById("tijdKiezen").style.display = "block";
		okButton.setAttribute("data-dag", "dinsdag");
	});
	document.getElementById("TijdWoensdagAanpassen").addEventListener("click", function () {
		document.getElementById("tijdKiezen").style.display = "block";
		okButton.setAttribute("data-dag", "woensdag");
	});
	document.getElementById("TijdDonderdagAanpassen").addEventListener("click", function () {
		document.getElementById("tijdKiezen").style.display = "block";
		okButton.setAttribute("data-dag", "donderdag");
	});
	document.getElementById("TijdVrijdagAanpassen").addEventListener("click", function () {
		document.getElementById("tijdKiezen").style.display = "block";
		okButton.setAttribute("data-dag", "vrijdag");
	});
	document.getElementById("TijdZaterdagAanpassen").addEventListener("click", function () {
		document.getElementById("tijdKiezen").style.display = "block";
		okButton.setAttribute("data-dag", "zaterdag");
	});
	document.getElementById("TijdZondagAanpassen").addEventListener("click", function () {
		document.getElementById("tijdKiezen").style.display = "block";
		okButton.setAttribute("data-dag", "zondag");
	});
	veranderTijdDag();
}
function veranderTijdDag(){
	okButton.addEventListener("click", function () {
		var openingsTijd = document.getElementById("openingsTijd").innerHTML;
		var sluitingsTijd =	document.getElementById("sluitingsTijd").innerHTML;

		var openingsTijdUur = parseInt(openingsTijd.substring(0, 2));
		var openingsTijdMinuut = parseInt(openingsTijd.substring(3, 5));

		var sluitingsTijdUur = parseInt(sluitingsTijd.substring(0, 2));
		var sluitingsTijdMinuut = parseInt(sluitingsTijd.substring(3, 5));

		if(openingsTijdUur > sluitingsTijdUur){
			errorMessage("De openings tijd moet voor de sluitings tijd zijn");
		} else if(openingsTijdUur == sluitingsTijdUur && openingsTijdMinuut >= sluitingsTijdMinuut){
			errorMessage("De openings tijd moet voor de sluitings tijd zijn");
		} else{
			var dag = okButton.getAttribute("data-dag");
			if(dag == "maandag"){
				document.getElementById("openingsTijdMaandag").innerHTML = openingsTijd;
				document.getElementById("sluitingsTijdMaandag").innerHTML = sluitingsTijd;
			} else if(dag == "dinsdag"){
				document.getElementById("openingsTijdDinsdag").innerHTML = openingsTijd;
				document.getElementById("sluitingsTijdDinsdag").innerHTML = sluitingsTijd;
			} else if(dag == "woensdag"){
				document.getElementById("openingsTijdWoensdag").innerHTML = openingsTijd;
				document.getElementById("sluitingsTijdWoensdag").innerHTML = sluitingsTijd;
			} else if(dag == "donderdag"){
				document.getElementById("openingsTijdDonderdag").innerHTML = openingsTijd;
				document.getElementById("sluitingsTijdDonderdag").innerHTML = sluitingsTijd;
			} else if(dag == "vrijdag"){
				document.getElementById("openingsTijdVrijdag").innerHTML = openingsTijd;
				document.getElementById("sluitingsTijdVrijdag").innerHTML = sluitingsTijd;
			} else if(dag == "zaterdag"){
				document.getElementById("openingsTijdZaterdag").innerHTML = openingsTijd;
				document.getElementById("sluitingsTijdZaterdag").innerHTML = sluitingsTijd;
			} else if(dag == "zondag"){
				document.getElementById("openingsTijdZondag").innerHTML = openingsTijd;
				document.getElementById("sluitingsTijdZondag").innerHTML = sluitingsTijd;
			}
			document.getElementById("openingsTijd").innerHTML = "00:00";
			document.getElementById("sluitingsTijd").innerHTML = "00:00";

			document.getElementById("timepicker-hours-openingstijd").style.display = "block";
			document.getElementById("timepicker-minutes-openingstijd").style.display = "none";
			document.getElementById("tijdKiezen").style.display= "none";
		}
	});
}
function determineOpeningsTijd(){ 
	var openingsTijd = "00:00";
	var selectedUur = "";
	var selectedMinuut = "";

	document.getElementById("timepicker-hours-openingstijd").style.display = "block"; 

	document.getElementById("showUrenOpeningsTijd").addEventListener("click", function () {
		document.getElementById("timepicker-hours-openingstijd").style.display = "block";
		document.getElementById("timepicker-minutes-openingstijd").style.display = "none";	
	})
	document.getElementById("showMinutenOpeningsTijd").addEventListener("click", function () {
		document.getElementById("timepicker-minutes-openingstijd").style.display = "block";	
		document.getElementById("timepicker-hours-openingstijd").style.display = "none";
	})
	var uren = document.getElementsByClassName("timepicker-uur-openingstijd");
	for(var i = 0; i < uren.length; i++){
		handleUurKeuzeOpeningsTijd(uren[i]);
	};

	var minuten = document.getElementsByClassName("timepicker-minuut-openingstijd");
	for(var i = 0; i < minuten.length; i++){
		handleMinuutKeuzeOpeningsTijd(minuten[i]);   
	};

	function handleUurKeuzeOpeningsTijd(uur){
		uur.addEventListener("click", function () { 
			selectedUur.className = "timepicker-uur-openingstijd";
			selectedUur = uur;
			selectedUur.className = "selectedTime";

			openingsTijd =  parseInt(uur.innerHTML) +":"+parseInt(openingsTijd.substring(3, 5));
			document.getElementById("openingsTijd").innerHTML = openingsTijd;
		})
	}

	function handleMinuutKeuzeOpeningsTijd(minuut){
		minuut.addEventListener("click", function () {
			selectedMinuut.className = "timepicker-minuut-openingstijd";
			selectedMinuut = minuut;
			selectedMinuut.className = "selectedTime";

			openingsTijd =  parseInt(openingsTijd.substring(0, 2)) +":"+parseInt(minuut.innerHTML);
			document.getElementById("openingsTijd").innerHTML = openingsTijd;
		})
	}
}

function determineSluitingsTijd(){ 
	var sluitingsTijd = "00:00";
	var selectedUur = "";
	var selectedMinuut = "";

	document.getElementById("timepicker-hours-sluitingsTijd").style.display = "block"; 

	document.getElementById("showUrenSluitingsTijd").addEventListener("click", function () {
		document.getElementById("timepicker-hours-sluitingsTijd").style.display = "block";
		document.getElementById("timepicker-minutes-sluitingsTijd").style.display = "none";	
	})
	document.getElementById("showMinutenSluitingsTijd").addEventListener("click", function () {
		document.getElementById("timepicker-minutes-sluitingsTijd").style.display = "block";	
		document.getElementById("timepicker-hours-sluitingsTijd").style.display = "none";
	})
	var uren = document.getElementsByClassName("timepicker-uur-sluitingsTijd");
	for(var i = 0; i < uren.length; i++){
		handleUurKeuzeOpeningsTijd(uren[i]);
	};

	var minuten = document.getElementsByClassName("timepicker-minuut-sluitingsTijd");
	for(var i = 0; i < minuten.length; i++){
		handleMinuutKeuzeOpeningsTijd(minuten[i]);   
	};

	function handleUurKeuzeOpeningsTijd(uur){
		uur.addEventListener("click", function () {        
			selectedUur.className = "timepicker-uur-openingstijd";
			selectedUur = uur;
			selectedUur.className = "selectedTime";

			sluitingsTijd =  parseInt(uur.innerHTML) +":"+parseInt(sluitingsTijd.substring(3, 5));
			document.getElementById("sluitingsTijd").innerHTML = sluitingsTijd;
		})
	}

	function handleMinuutKeuzeOpeningsTijd(minuut){
		minuut.addEventListener("click", function () {
			selectedMinuut.className = "timepicker-minuut-openingstijd";
			selectedMinuut = minuut;
			selectedMinuut.className = "selectedTime";

			sluitingsTijd =  parseInt(sluitingsTijd.substring(0, 2)) +":"+parseInt(minuut.innerHTML);
			document.getElementById("sluitingsTijd").innerHTML = sluitingsTijd;
		})
	}
}

function getDagenSettings(){
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
		fetch("restservices/instellingen/getDagen", fetchoptions)
		.then(response => response.json())
		.then(function (dagen) {
			for(var dag of dagen){
				console.log(dag.dagNummmer);
				if(dag.sluitingstijd != null){
					var dagnummer = dag.dagNummmer;
					console.log("========================");
					document.getElementById(openingsTijdSpans[dagnummer]).innerHTML= dag.opeingstijd;
					document.getElementById(sluitingsTijdSpans[dagnummer]).innerHTML = dag.sluitingstijd;
					document.getElementById(checkBoxes[dagnummer]).checked = true;
					document.getElementById(tijdenAreas[dagnummer]).style.display = "block"
				}
				console.log(dag)
			}
		})
}

function checkboxChecker(textId, checkId) {
 	var checkBox = document.getElementById(checkId);
	checkBox.addEventListener("click", function () {
		var text = document.getElementById(textId);
		if (checkBox.checked == true){
			text.style.display = "block";
		} else {
			text.style.display = "none";
		}
	})
}


