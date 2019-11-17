//Load google charts
google.charts.load('current', {'packages':['corechart']});
function onload(){
	getData("week");
	createAfsprakLineGraph();
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
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/statistics/getData/"+lengte, fetchoptions)
	.then(response => response.json())
	.then(function(dataArrays){
		var i= 1;
		for(let dataArray of dataArrays){
			if(i == 1){
				// aantal afspraken en inkomsten
				inkomstenAndAfspraken(dataArray.afspraken, dataArray.inkomsten);
				i++;
			} else if(i ==2){
				maakBehandelingenOverzicht(dataArray);	
			 	i++;
			}
			else if(i == 3){
				maakAfsprakenGrafiek(dataArray);
				i++;
			} else if(i == 4){
				createAfsprakLineGraph();
				i++;
			}
		}
		createAfsprakLineGraph();
	})
}

function inkomstenAndAfspraken(afspraken, inkomsten){
	document.getElementById("aantalAfspraken").innerHTML = afspraken;
	document.getElementById("inkomsten").innerHTML = inkomsten;
}

function maakBehandelingenOverzicht(dataArray){ 
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
	
	google.charts.setOnLoadCallback(drawPieChart(behandelingen));
}

function drawPieChart(behandelingen) {
	var data = google.visualization.arrayToDataTable(behandelingen);

	// Optional; add a title and set the width and height of the chart
	var options = { 'title': 'Behandelingen', 'width': 550, 'height': 400 };
	// Display the chart inside the <div> element with id="piechart"
	var chart = new google.visualization.PieChart(document.getElementById("behandelingenChart"));
	chart.draw(data, options);
}

function maakAfsprakenGrafiek(dataArray){
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
		var dagArray = [];
		
		dagArray.push(weekdagen[dag.dagNummer]);
		dagArray.push(dag.aantalAfspraken);
		dagArray.push("blue");

		dagen.push(dagArray);
	}
	google.charts.load("current", {packages:['corechart']});
	google.charts.setOnLoadCallback(drawAfsprakenStats(dagen));

}

function drawAfsprakenStats(dagen) {
	var data = google.visualization.arrayToDataTable(dagen);

	var view = new google.visualization.DataView(data);
	view.setColumns([0, 1,
		{
			calc: "stringify",
			sourceColumn: 1,
			type: "string",
			role: "annotation"
		},
		2]);

	var options = {
		title: "",
		width: "70%",
		height: 600,
   	   float: "left",
		bar: { groupWidth: "95%" },
		legend: { position: "none" },
	};
	var chart = new google.visualization.ColumnChart(document.getElementById("columnchart_values"));
	chart.draw(view, options);
}

function createAfsprakLineGraph(){
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['Year', 'Sales'],
          ['2013',  1000],
          ['2014',  1170],
          ['2015',  660],
          ['2016',  1030]
        ]);

        var options = {
			title: 'Company Performance',
			width: "70%",
			height: 600,
			float: "right",
			hAxis: {title: 'Year',  titleTextStyle: {color: '#333'}},
			vAxis: {minValue: 0},
        };

        var chart = new google.visualization.AreaChart(document.getElementById('chart_div'));
        chart.draw(data, options);
      }

}
