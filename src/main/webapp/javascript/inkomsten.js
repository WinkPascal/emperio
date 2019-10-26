//Load google charts
google.charts.load('current', {'packages':['corechart']});
function onload(){
	getData("week");
}

document.getElementById("weekButton").addEventListener("click", function(){
	getData("week");
});
document.getElementById("maandButton").addEventListener("click", function(){
	getData("maand");
});
document.getElementById("jaarButton").addEventListener("click", function(){
	getData("jaar");
});
document.getElementById("altijdButton").addEventListener("click", function(){
	getData("altijd");
});

function getData(lengte){
	var behandelingen;
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/getData/"+lengte, fetchoptions)
	.then(response => response.json())
	.then(function(dataArrays){
		var i= 1;
		for(let dataArray of dataArrays){
			if(i == 1){
				// aantal afspraken en inkomsten
				document.getElementById("aantalAfspraken").innerHTML = dataArray.afspraken;
				document.getElementById("inkomsten").innerHTML = dataArray.inkomsten;
				i++;
			} else if(i ==2){
				var behandelingen = [];
				var b = ['Behandeling', 'keuzes'];
				behandelingen.push(b);
				
				for(let behandeling of dataArray){
					console.log(behandeling.naam);
					var behandelingInfo = [];
					
					behandelingInfo.push(behandeling.naam);
					behandelingInfo.push(behandeling.count);
					
					behandelingen.push(behandelingInfo);
				}
				console.log(behandelingen);
				
				google.charts.setOnLoadCallback(drawPieChart);
				i++;
				function drawPieChart() {
					var data = google.visualization.arrayToDataTable(behandelingen);

					// Optional; add a title and set the width and height of the chart
					var options = {'title':'Behandelingen', 'width':550, 'height':400};
					// Display the chart inside the <div> element with id="piechart"
					var chart = new google.visualization.PieChart(document.getElementById("behandelingenChart"));
					chart.draw(data, options);
				}
			}
			else if(i == 3){
				var weekdagen = new Array(7);
				weekdagen[0] = "Zondag";
				weekdagen[1] = "Maandag";
				weekdagen[2] = "Dinsdag";
				weekdagen[3] = "Woensdag";
				weekdagen[4] = "Donderdag";
				weekdagen[5] = "Vrijdag";
				weekdagen[6] = "Zaterdag";
				
				var dagen = [];
				var topRow = ["Element", "Aantal afspraken", { role: "style" } ];
				dagen.push(topRow);
				for(let dag of dataArray){
					console.log(dag.dagNummer);
					var dagArray = [];
					
					dagArray.push(weekdagen[dag.dagNummer]);
					dagArray.push(dag.aantalAfspraken);
					dagArray.push("blue");

					dagen.push(dagArray);
				}
				google.charts.load("current", {packages:['corechart']});
				google.charts.setOnLoadCallback(drawAfsprakenStats);
				function drawAfsprakenStats() {
				  var data = google.visualization.arrayToDataTable(dagen);

				  var view = new google.visualization.DataView(data);
				  view.setColumns([0, 1,
				                   { calc: "stringify",
				                     sourceColumn: 1,
				                     type: "string",
				                     role: "annotation" },
				                   2]);

				  var options = {
				    title: "",
				    width: 1400,
				    height: 600,
				    bar: {groupWidth: "95%"},
				    legend: { position: "none" },
				  };
				  var chart = new google.visualization.ColumnChart(document.getElementById("columnchart_values"));
				  chart.draw(view, options);
				}
			}
		}
	}).catch(function() {
		// De gebruiker is niet ingelogt
		alert("niet meer ingelogd");
	});
}




