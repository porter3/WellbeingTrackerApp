$(document).ready(function () {

    // collapse the sidebar when the button is clicked
   $('#sidebarCollapse').on('click', function() {
       $('#sidebar').toggleClass('active');
   });

   displayDate();

   getDataDisplayGraph();
   updateGraph(); // listens for button click

   displayEntryTable();
//    updateEntries();
});

function displayDummyGraph(){
    // get the canvas element. for whatever reason, using the jQuery selector here screws this up.
    var canvas = document.getElementById('lineGraph');
    // make a new graph, passing in the canvas element to display it in there
    var lineGraph = new Chart(canvas, {
        type: 'line',
        data: {
            // x-axis labels (dates in this case)
            labels:['01/01/2000','01/02/2000','01/04/2000'], 
            datasets: [
                // first line
                {
                    label: 'Protein(g)',
                    yAxisID: '0',
                    data: [200, 170, 186],
                    // many more color customization options exist. rgb values range from 0-255. a = alpha, opacity on a scale of 0-1
                    borderColor: [
                    'rgba(100, 99, 132, 1)'
                    ]
                },
                {
                    label: 'Mood',
                    yAxisID: '1',
                    data: [9, 6, 7],
                    borderColor: [
                    'rgba(300, 99, 132, 1)'
                    ]
                }
            ]
        },
        options: {
            title:{
                display: true,
                text: 'Metrics'
            },
            scales: {
                yAxes: [
                    {
                        id: '0',
                        type: 'linear',
                        position: 'left',
                        min: 0,
                        ticks: {
                            display: true
                        }
                    },
                    {
                        id: '1',
                        type: 'linear',
                        position: 'left',
                        ticks: {
                            display: true
                        }
                    }
                ]
            }
        }
    });
}

function displayDate(){
    // get today's date
    var selectedDate = new Date();

    // format today's date
    // padStart pads the string with a certain character until it reaches the desired length
    var dd = String(selectedDate.getDate()).padStart(2, '0');
    var mm = String(selectedDate.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = selectedDate.getFullYear();

    selectedDate = mm + '/' + dd + '/' + yyyy;
    console.log("selectedDate: ", selectedDate);

    //display today's date
    $("#dateDisplay").text(selectedDate);

    // TODO: if user clicks back button, display yesterday's date
}

// AJAX: HAS HARDCODED USER ID
function getDates(){
    return $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/dates/" + "1",
        success: function (dates) {
        },
        error: function(xhr){
            alert("Request status: " + xhr.status + " Status text: " + xhr.statusText + " " + xhr.responseText);
        }
    });
}

// AJAX: HAS HARDCODED USER ID
function getMetricTypes(){
    return $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/metrictypes/" + "1",
        success: function (data) {
        },
        error: function(xhr){
            alert("Request status: " + xhr.status + " Status text: " + xhr.statusText + " " + xhr.responseText);
        }
    });
}

// AJAX: HAS HARDCODED USER ID
function getAllMetricEntries(){
    return $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/metricentries/" + "1",
        success: function (parentList) {
        },
        error: function(xhr){
            alert("Request status: " + xhr.status + " Status text: " + xhr.statusText + " " + xhr.responseText);
        }
    });
}

// AJAX: HAS HARDCODED USER ID
function getEntriesForDate(date){
    return $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/metricentries/" + "1" + date,
        success: function (entryList) {
        },
        error: function(xhr){
            alert("Request status: " + xhr.status + " Status text: " + xhr.statusText + " " + xhr.responseText);
        }
    });
}

function displayGraph(labels, dataSets, yAxes){
    // get the canvas element. for whatever reason, using the jQuery selector here screws this up.
    var canvas = document.getElementById('lineGraph');
    // make a new graph, passing in the canvas element to display it in there
    var lineGraph = new Chart(canvas, {
        type: 'line',
        data: {
            labels: labels,
            datasets: dataSets,
        },
        options: {
            title:{
                display: true,
                text: "Metrics"
            },
            scales:{
                yAxes
            }
        }
    });
}

function getDataDisplayGraph(){

    // get the current user's ID  to pass into AJAX calls (will just use a hardcoded one for jakep310 right now)
    
    $.when(getDates(), getMetricTypes(), getAllMetricEntries()).done(function(dates, typeList, parentList){

        // arrays to hold stuff
        var dateArray = dates[0];
        console.log("DATEARRAY: ", dateArray);
        var typeArray = typeList[0];
        console.log("TYPEARRAY: ", typeArray);
        var parentArrayForEntries = parentList[0];
        console.log("PARENTARRAY: ", parentArrayForEntries);

        // list of colors to make the lines (20 right now)
        var colors = [
            "#e6194B", // red
            "#3cb44b", // green
            "#ffe119", // yellow
            "#4363d8", // blue
            "#f58231", // orange
            "#911eb4", // purple
            "#42d4f4", // cyan
            "#f032e6", // magenta
            "#bfef45", // lime
            "#fabebe", // pink
            "#469990", // teal
            "#e6beff", // lavender
            "#9A6324", // brown
            "#800000", // maroon
            "#aaffc3", // mint
            "#808000", // olive
            "#ffd8b1", // apricot
            "#000075", // navy
            "#a9a9a9", // gray
            "#000000" // black
        ];

        // DATASETS
        var dataSetList = new Array();


        // for each MetricType, create a new dataSet
        for (i = 0; i < parentArrayForEntries.length; i++){
            // get the index/typeList in parentList
            var childList = parentArrayForEntries[i];
            console.log("CHILDLIST: ", childList);

            var dataPoints = new Array();

            // for each entry in childList, grab the metricValue and add to 
            for (j = 0; j < childList.length; j++){
                dataPoints.push(childList[j].metricValue);
            }
            console.log("DATAPOINTS: ", dataPoints);

            // choose labeling format depending on whether dataset/metricType is a subjective type
            var label;
            // type is subjective if scale is not 0
            if (typeArray[i].scale != 0){
                label = typeArray[i].metricName + " (1-" + typeArray[i].scale + ")";
            }
            else{
                label = typeArray[i].metricName + " (" + typeArray[i].unit + ")";
            }

            // set each dataSet's label
            var dataSet = {
                label: label,
                yAxisID: i,
                data: dataPoints,
                // change this later to change colors
                borderColor: [
                    colors[i]
                ],
                fill: false
            };

            dataSetList.push(dataSet);
        }
        console.log("Here's the array of datasets: ", dataSetList);

        // YAXES
        var yAxes = new Array();
        for (k = 0; k < dataSetList.length; k++){
            var yAxis = {
                id: k,
                type: 'linear',
                position: 'left',
                ticks: {
                    display: true
                }
            };
            yAxes.push(yAxis);
        }
        console.log("Y AXES: ", yAxes);

        // display graph
        displayGraph(dateArray, dataSetList, yAxes);
    });
}

// same as getDataDisplayGraph but waits for button click
function updateGraph(){
    $('#updateGraphButton').click(function(){
        getDataDisplayGraph();
    });
}

// display entry table
function displayEntryTable(){
    // get all the MetricTypes associated with the user's account
    // get all the entries for the date being displayed
        // if an entry exists, it will contain an edit and delete button
    var date = $("#dateDisplay").text();

    //$.when(getMetricTypes(), getEntriesForDate(date))
}

// waits for 'save changes' to be clicked
function updateEntries(){
    $('#saveChangesButton').click(function(){

        // get all the MetricTypes associated with the user's account
        // get all the entries for the date being displayed
    });
}