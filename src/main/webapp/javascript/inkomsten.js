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
					var chart = new google.visualization.PieChart(document.getElementById("behandelingen"));
					chart.draw(data, options);
				}
			}
			else if(i == 3){
				
				google.charts.load("current", {packages:['corechart']});
				google.charts.setOnLoadCallback(drawAfsprakenStats);
				function drawAfsprakenStats() {
				  var data = google.visualization.arrayToDataTable([
				    ["Element", "Density", { role: "style" } ],
				    ["Copper", 8.94, "#b87333"],
				    ["Silver", 10.49, "silver"],
				    ["Gold", 19.30, "gold"],
				    ["Platinum", 211.45, "color: #e5e4e2"]
				  ]);

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
	// Draw the chart and set the chart values
}




