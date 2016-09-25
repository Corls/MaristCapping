var stage = 0;
$(function(){
	$("#user-bar").click(function(){
		if(stage == 0) {
			$("#user-bar").load("/login/");
			stage++;
		}
	});
});

var check;
function displayTable() {
	if(check) {
		clearInterval(check);
	}
	var tSelect = document.getElementById("table-select");
	var url = tSelect.options[tSelect.selectedIndex].value;
	if(url == "default") {
		document.getElementById("table-display").innerHTML = "";
		return;
	}
	$("#table-display").load(url);
}

function setCheck(theUrl, refresh) {
	check = setInterval(function(){
		$.ajax({url: theUrl+"/ch/"}).done(function(data) {
			if(data == "1") {
    			$("#tamaya").load(theUrl);
			}
		});
	},refresh);
});

function submitIt(fid, url) {
	var search = $("#" + fid).serialize();
	$.post(url, search).done(function(data) {
		document.getElementById("tamaya").innerHTML = data;
		document.getElementById("table-form").innerHTML = "";
	});
}

function getForm(url) {
	$("#table-form").load(url);
}