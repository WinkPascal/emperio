function errorMessage(message){
	var errorDiv = document.createElement('div');
	alert(message);
	errorDiv.setAttribute('class', 'errorDiv');
	errorDiv.innerHTML = message;
	document.body.appendChild(errorDiv);

//	setTimeout(function() {
//		errorDiv.remove();
//	}, 1000);
}

function succesMessage(message){
	var errorDiv = document.createElement('div');

	errorDiv.setAttribute('class', 'errorDiv');
	errorDiv.innerHTML = message;
	document.body.appendChild(errorDiv);

	setTimeout(function() {
		errorDiv.remove();
	}, 1000);
}
