function onload(){
	var hoeveelheid = 10;
	getEmails(hoeveelheid);
	document.getElementById("loadEmails").addEventListener("click", function(){
		hoeveelheid = hoeveelheid + 10;
		getEmail(hoeveelheid);
	})

	document.getElementById("versturen").addEventListener("click", function(){
		sendEmail();
	})
}

function getEmails(hoeveelheid){
	var verzonden = document.getElementById("verzonden");
	var fetchoptions = {
	 	headers: {
	 		'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
	 	}
	}
	fetch("restservices/marketing/emails/" + hoeveelheid, fetchoptions)
	.then(response => response.json())
	.then(function (emails) {
		for(let email of emails){
			console.log(email.id);
			verzonden.appendChild(createEmail(email));
			getEmail(email);
		}
	})
}

function getEmail(email){
	document.getElementById(email.id).addEventListener("click", function(){
		var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
		fetch("restservices/marketing/email/"+email.id, fetchoptions)
		.then(response => response.json())
		.then(function (email) {
			console.log("email");
			console.log(email.inhoud);
			getFullEmail(email);
		})
	})
}

function getFullEmail(email){
	document.getElementById("versturen").style.display = "none";
	document.getElementById("verwijderen").style.display = "none";
	document.getElementById("klantenKiezen").style.display = "none";
	document.getElementById("inhoud").style.display = "none";
	document.getElementById("onderwerp").style.display = "none";

	document.getElementById("klantenOphalen").style.display = "block";
	document.getElementById("onderwerpVanMail").style.display = "block";
	document.getElementById("inhoudVanMail").style.display = "block";

	document.getElementById("verstuurdOp").innerHTML = email.verzendtijd;
	document.getElementById("klantenLijst").innerHTML = email.aantalKlanten;	
	document.getElementById("onderwerpVanMail").innerHTML = email.onderwerp;
	document.getElementById("inhoudVanMail").innerHTML = email.inhoud;
}

function sendEmail(){
	var formData = new FormData();
	formData.append("klanten", geslecteerdeKlanten);
	formData.append("onderwerp", document.getElementById("onderwerp").value);
	formData.append("inhoud", document.getElementById("inhoud").innerHTML);
	
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
	fetch("restservices/marketing/email" , fetchoptions)
		.then(function (response) {
			if (response.ok) {
				alert("toegevoegd");
			} else {
				alert("kapot fout");
			}
		})
}

function createEmail(email){
	var verstuurdeMail = document.createElement('div');
	verstuurdeMail.setAttribute('id', email.id);
	verstuurdeMail.setAttribute('class', 'verstuurdeMail');

	var topInfo = document.createElement('div');
	topInfo.setAttribute('class', 'topInfo');
	verstuurdeMail.appendChild(topInfo);

	var aantalGekregen = document.createElement('span');
	aantalGekregen.setAttribute('class', 'aantalGekregen');
	aantalGekregen.innerHTML = email.aantalKlanten;
	topInfo.appendChild(aantalGekregen);

	var onderwerp = document.createElement('span');
	onderwerp.setAttribute('class', 'onderwerp');
	onderwerp.innerHTML = email.onderwerp;
	topInfo.appendChild(onderwerp);

	var timestamp = document.createElement('span');
	timestamp.setAttribute('class', 'timestamp');
	timestamp.innerHTML = email.verzendtijd;
	verstuurdeMail.appendChild(timestamp);

	var inhoud = document.createElement('span');
	inhoud.setAttribute('class', 'inhoud');
	inhoud.innerHTML = email.inhoud;
	verstuurdeMail.appendChild(inhoud);

	return verstuurdeMail;
}


document.getElementById("klantenKiezen").addEventListener("click", function(){
	document.getElementById("klantenModal").style.display = "block";
	bladzijdeManager("load");
})

document.getElementById("sluitKlantenSelectModal").addEventListener("click", function(){
	document.getElementById("klantenModal").style.display = "none";
})

let geslecteerdeKlanten = [];
function selecteerKlant(klantId){
	console.log(klantId);
	for (i = 0; i < geslecteerdeKlanten.length; i++) { 
		if(geslecteerdeKlanten[i] == klantId){
			geslecteerdeKlanten.splice(i, 1);
			document.getElementById("hoeveelheidOntavngers").innerHTML = geslecteerdeKlanten.length;
			return false;
		}
	}
	geslecteerdeKlanten.push(klantId);
	document.getElementById("hoeveelheidOntavngers").innerHTML = geslecteerdeKlanten.length;
	return true;
}


