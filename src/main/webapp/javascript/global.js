function errorMessage(message) {
	var messageDiv = document.createElement('div');
	messageDiv.setAttribute('class', 'meldingDiv');
	messageDiv.style.backgroundColor = "#ae1414";
	messageDiv.innerHTML = message;
	document.body.appendChild(messageDiv);

	setTimeout(function () {
		messageDiv.remove();
	}, 1000);
}

function succesMessage(message) {
	var messageDiv = document.createElement('div');

	messageDiv.setAttribute('class', 'meldingDiv');
	messageDiv.style.backgroundColor = "#228B22";
	messageDiv.innerHTML = message;
	document.body.appendChild(messageDiv);

	setTimeout(function () {
		messageDiv.remove();
	}, 1000);
}

document.getElementById("logoutButton").addEventListener("click", function() {
	window.sessionStorage.setItem("sessionToken", "");
	location.href = 'index.html';
})


document.getElementById("homeButton").addEventListener("click", function () {
	location.href = 'home.html';
})

function formatDate(date){
	var date = new Date(date);
	var monthNames = [
		"Januari", "Februari", "Maart",
		"April", "Mei", "Juni", "Juli",
		"Augustus", "September", "Oktober",
		"November", "December"
	];
	var day = date.getDate();
	var monthIndex = date.getMonth();
	var year = date.getFullYear();
	return day + ' ' + monthNames[monthIndex] + ' ' + year;
}

function showLoadingScreen(){
	var laadscherm = document.createElement('div');
	laadscherm.setAttribute('id', 'laadscherm');
	var contentDiv = document.createElement('div');
	contentDiv.setAttribute('class', 'contentDiv');
	var laadDiv = document.createElement('div');
	laadDiv.setAttribute('class', 'lds-ring');
	laadDiv.appendChild(document.createElement('div'));
	laadDiv.appendChild(document.createElement('div'));
	laadDiv.appendChild(document.createElement('div'));
	laadDiv.appendChild(document.createElement('div'));
	contentDiv.appendChild(laadDiv);
	contentDiv.innerHTML += "<br> Loading...";
	laadscherm.appendChild(contentDiv);
	document.body.appendChild(laadscherm);
}

function removeLoadingScreen(){
		setTimeout(function () {
		document.getElementById("laadscherm").remove();
	}, 1000);
}
