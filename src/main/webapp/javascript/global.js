function errorMessage(message){
	var messageDiv = document.createElement('div');
	messageDiv.setAttribute('class', 'meldingDiv');
	messageDiv.style.backgroundColor = "#ae1414";
	messageDiv.innerHTML = message;
	document.body.appendChild(messageDiv);

	setTimeout(function() {
		messageDiv.remove();
	}, 1000);
}

function succesMessage(message){
	var messageDiv = document.createElement('div');

	messageDiv.setAttribute('class', 'meldingDiv');
	messageDiv.style.backgroundColor = "#228B22";
	messageDiv.innerHTML = message;
	document.body.appendChild(messageDiv);

	setTimeout(function() {
		messageDiv.remove();
	}, 1000);
}
