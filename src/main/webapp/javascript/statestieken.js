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

function buttonColorManager(lengte){
	document.getElementById("weekButton").className="defaultButton";
	document.getElementById("maandButton").className="defaultButton";
	document.getElementById("jaarButton").className="defaultButton";
	document.getElementById("altijdButton").className="defaultButton";
	console.log(lengte);
	switch(lengte){
	case "week":
		document.getElementById("weekButton").className ="defaultButtonSelected";
		break;
	case "maand":
		document.getElementById("maandButton").className = "defaultButtonSelected";
		break;
	case "jaar":
		document.getElementById("jaarButton").className="defaultButtonSelected";
		break;
	case "altijd":
		document.getElementById("altijdButton").className="defaultButtonSelected";
		break;
	}	
}

function getData(lengte){
	buttonColorManager(lengte);
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/statistics/getData/"+lengte, fetchoptions)
	.then(response => response.json())
	.then(function(data){
		var i= 1;
		getallenHandler(data.getallen);
	
		maakBehandelingenOverzicht(data.behandelingen);
		maakGeslachtenOverzicht(data.geslachten);
		maakLengteOverzicht(data.lengtes);
		maakUitgaveOverzicht(data.uitgave);

		maakAfsprakenGrafiek(data.afsprakenPerDag);
		createAfsprakLineGraph();
	})
}

function getallenHandler(data){
	document.getElementById("aantalAfspraken").innerHTML = data.afspraken;
	document.getElementById("inkomsten").innerHTML = data.inkomsten;
}
//==========================================================================
//==========================================================================
//==========================================================================

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
	
	google.charts.setOnLoadCallback(drawPieChartBehandelingen(behandelingen));
}
function drawPieChartBehandelingen(behandelingen) {
	var data = google.visualization.arrayToDataTable(behandelingen);
	var options = { 'title': 'Behandelingen', 'width': 550, 'height': 400 };
	var chart = new google.visualization.PieChart(document.getElementById("behandelingenChart"));
	chart.draw(data, options);
}

//==========================================================================
//==========================================================================
//==========================================================================
//==========================================================================

function maakGeslachtenOverzicht(dataArray){
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
	
	google.charts.setOnLoadCallback(drawPieChartGeslachten(behandelingen));
}
function drawPieChartGeslachten(geslachten) {
	var data = google.visualization.arrayToDataTable(geslachten);
	var chart = new google.visualization.PieChart(document.getElementById("geslachtenChart"));
	var options = { 'title': 'Behandelingen', 'width': 550, 'height': 400 };
	chart.draw(data, options);
}

//==========================================================================
//==========================================================================
//==========================================================================
//==========================================================================

function maakLengteOverzicht(data){
	var behandelingen = [];
	var b = ['afspraak', 'keuzes'];
	behandelingen.push(b);
	console.log(data);
	var behandelingInfo = [];	
	behandelingInfo.push("10 minuten");
	behandelingInfo.push(data.min10);
	behandelingen.push(behandelingInfo);

	var behandelingInfo = [];	
	behandelingInfo.push("20 minuten");
	behandelingInfo.push(data.min20);
	behandelingen.push(behandelingInfo);

	var behandelingInfo = [];	
	behandelingInfo.push("30 minuten");
	behandelingInfo.push(data.min30);
	behandelingen.push(behandelingInfo);

	var behandelingInfo = [];	
	behandelingInfo.push("1 uur");
	behandelingInfo.push(data.min60);
	behandelingen.push(behandelingInfo);
	
	
	google.charts.setOnLoadCallback(drawPieChartLengte(behandelingen));
}
function drawPieChartLengte(lengtes) {
	var data = google.visualization.arrayToDataTable(lengtes);
	var chart = new google.visualization.PieChart(document.getElementById("LengteChart"));
	var options = { 'title': 'Behandelingen', 'width': 550, 'height': 400 };
	chart.draw(data, options);
}
//==========================================================================
//==========================================================================
//==========================================================================
//==========================================================================

function maakUitgaveOverzicht(dataArray){
	var behandelingen = [];
	var b = ['Behandeling', 'keuzes'];
	behandelingen.push(b);
	
	var behandelingInfo = [];
	behandelingInfo.push("minder dan €10");
	behandelingInfo.push(dataArray.d10);		
	behandelingen.push(behandelingInfo);
	
	var behandelingInfo = [];
	behandelingInfo.push("meer dan €10");
	behandelingInfo.push(dataArray.d10);		
	behandelingen.push(behandelingInfo);
	
	var behandelingInfo = [];
	behandelingInfo.push("meer dan €20");
	behandelingInfo.push(dataArray.d10);		
	behandelingen.push(behandelingInfo);
	
	var behandelingInfo = [];
	behandelingInfo.push("meer dan €30");
	behandelingInfo.push(dataArray.d40);		
	behandelingen.push(behandelingInfo);
	console.log(behandelingen);
	google.charts.setOnLoadCallback(drawPieChartUitgave(behandelingen));
}

function drawPieChartUitgave(uitgave) {
	var data = google.visualization.arrayToDataTable(uitgave);
	var chart = new google.visualization.PieChart(document.getElementById("uitgaveChart"));
	var options = { 'title': 'Uitgave per afspraak', 'width': 550, 'height': 400 };
	chart.draw(data, options);
}
//==========================================================================
//==========================================================================
//==========================================================================
//==========================================================================

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
	view.setColumns(
		[0,
		1,
		{
			calc: "stringify",
			sourceColumn: 1,
			type: "string",
			role: "annotation"
		},
		2]
	);

	var options = {
		title: "Aantal afspraken per dag",
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
		title: 'inkomsten',
		hAxis: {title: 'Year',  titleTextStyle: {color: '#333'}},
		vAxis: {minValue: 0},
    };

    var chart = new google.visualization.AreaChart(document.getElementById('chart_div'));
    chart.draw(data, options);
  }
}
