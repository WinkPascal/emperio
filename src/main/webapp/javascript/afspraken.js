function onload(){
	var datum = new Date();
	var date = datum.getFullYear() +'-'+ datum.getMonth() +'-'+ datum.getDate();
	getAfspraken(date);
}

function getRooster(){
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/getWeekRooster/" + date, fetchoptions)
	.then(response => response.json())
	.then(function(dataArrays){

	}).catch(function() {
		// De gebruiker is niet ingelogt
		alert("niet meer ingelogd");
	});
}

function getAfspraken(date){
	var fetchoptions = {
			headers: {
				'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken")
			}
		}
	fetch("restservices/service/getWeekRooster/" + date, fetchoptions)
	.then(response => response.json())
	.then(function(dataArrays){

	}).catch(function() {
		// De gebruiker is niet ingelogt
		alert("niet meer ingelogd");
	});
}

function maakAfspraken() {
	var self = this;
	this.singleEvents.each(function(){
		//place each event in the grid -> need to set top position and height
		var start = getScheduleTimestamp($(this).attr('data-start')),
			duration = getScheduleTimestamp($(this).attr('data-end')) - start;

		var eventTop = self.eventSlotHeight*(start - self.timelineStart)/self.timelineUnitDuration,
			eventHeight = self.eventSlotHeight*duration/self.timelineUnitDuration;
		
	});
};