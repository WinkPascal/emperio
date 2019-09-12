
document.getElementById("plus").addEventListener("click", function() {
	voegItemToe();
})
// //hier kunnen behandelingen worden toegevoegd
// function voegBehandelingItemToe() {
// toevoegingen ++;
//	
// var behandeling = document.createElement("form");
// behandeling.className = "nieuweBehandeling";
// behandeling.id = "behandeling"+ toevoegingen;
// //verwijder een behandeling uit de lijst voor toevoegeingen
// var verwijder = document.createElement("i");
// verwijder.innerHTML = "x";
// verwijder.className = "verwijderButton";
// verwijder.addEventListener("click", function() {
// behandeling.remove();
// })
// behandeling.appendChild(verwijder);
//
// //voeg behandeling naam toe
// var behandelingNaam = document.createElement("input");
// behandelingNaam.id = "naam"+toevoegingen;
// behandelingNaam.type="tekst";
// behandeling.appendChild(behandelingNaam);
//
// //voeg prijs van behandeling toe
// var prijs = document.createElement("input");
// prijs.id = "behandelingPrijs"+toevoegingen;
// prijs.type = "number";
// behandeling.appendChild(prijs);
//
// //voeg een beschrijving toe
// var beschrijving = document.createElement("input");
// beschrijving.id = "behandelingBeschrijving"+toevoegingen;
// behandeling.appendChild(beschrijving);
//
// //voeg een beschrijving toe
// var lengte = document.createElement("input");
// lengte.id = "behandelingslengte";
// behandeling.appendChild(lengte);
//
// document.getElementById("toevoegLijst").appendChild(behandeling);
// }
// document.getElementById("voegBehandelingToe").addEventListener("click",
// function() {
// var formData = new FormData(document.querySelector("#behandeling1"));
// alert(formData);
// var naam = document.getElementById("naam").value;
// var prijs = document.getElementById("behandelingPrijs1").value;
// var bs = document.getElementById("behandelingBeschrijving1").value;
// var len = document.getElementById("behandelingslengte").value;
//
// formData.append("datum", dateForm);
// var encData = new URLSearchParams(formData.entries());
// alert(encData);
// fetch("restservices/service/behandeling", {method : 'POST', body : encData})
// .then(function (response){
// if(response.ok){
// alert("het ging goed");
// } else{
// alert("er ging iets fout");
// }
// }).catch(function(){
// document.getElementById("inplanFout").style.display = "block";
// })
// // for(i = 1; i > toevoegingen; i++){
// // alert(i);
// // }
// })
