function errorMessage(message){
	var errorDiv = document.createElement('div');

	errorDiv.setAttribute('class', 'errorDiv');
	errorDiv.innerHTML = "De gegeven inlog gegevens zijn niet correct.";
	document.getElementById("errorLijst").appendChild(errorDiv);

	setTimeout(function() {
		errorDiv.remove();
	}, 1000);
}

function succesMessage(message){
	var errorDiv = document.createElement('div');

	errorDiv.setAttribute('class', 'errorDiv');
	errorDiv.innerHTML = "De gegeven inlog gegevens zijn niet correct.";
	document.getElementById("errorLijst").appendChild(errorDiv);

	setTimeout(function() {
		errorDiv.remove();
	}, 1000);
}
