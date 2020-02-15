$(document).ready(function () {

    // collapse the sidebar when the button is clicked
    $('#sidebarCollapse').on('click', function() {
        $('#sidebar').toggleClass('active');
    });

    var formattedDate = displayDate();

    getDataDisplayGraph();
    updateGraph(); // listens for button click

    displayEntryTable(formattedDate);
    updateEntries(formattedDate);
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

    // return a formatted date for making AJAX requests (MM-dd-yyyy)
    return $("#dateDisplay").text().split("/").join("-");

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
        url: "http://localhost:8080/api/metricentries/" + "1" + "/" + date,
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
        var typeArray = typeList[0];
        var parentArrayForEntries = parentList[0]; // this array's child arrays are sorted by metricType 
        console.log("PARENT ARRAY: ", parentArrayForEntries);

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

        // Iterating over MetricTypes
        // for each MetricType, create a new dataSet
        for (i = 0; i < parentArrayForEntries.length; i++){
            // get the index/typeList in parentList
            var childList = parentArrayForEntries[i];

            var dataPoints = new Array();

            // Iterating over entries in a type-specific list
            /* 
            for each date that exists for the user, check if it equals the date
            contained within a child entry. If there is no child entry for that date,
            add a null value instead.
            */
            var childListIndex = 0;

                for (j = 0; j < dateArray.length; j++){
                    if (childListIndex != childList.length){
                        if (dateArray[j] === childList[childListIndex].dayLog.logDate){
                            dataPoints.push(childList[childListIndex].metricValue);
                            childListIndex++;
                        }
                        else{
                            dataPoints.push(null);
                        }
                    }
                    else{
                        dataPoints.push(null);
                    }
                }

            console.log("DATES: ", dateArray);
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
                spanGaps: true,
                fill: false
            };

            dataSetList.push(dataSet);
        }
        console.log("Here's the array of datasets: ", dataSetList);

        // Y-AXES
        var yAxes = new Array();
        for (k = 0; k < dataSetList.length; k++){
            var yAxis = {
                id: k,
                type: 'linear',
                position: 'left',
                ticks: {
                    display: true,
                    beginAtZero: true
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
function displayEntryTable(date){
    // get all the MetricTypes associated with the user's account
    // get all the entries for the date being displayed

    // after all AJAX calls are made:
    $.when(getMetricTypes(), getEntriesForDate(date)).done(function(typeList, entryList){

        var typeArray = typeList[0];
        var entriesForDateArray = entryList[0];

        var tableBody = $('#entryTableBody');

        // clear entry table
        tableBody.empty();

        // for all the metricTypes in typeArray, append a row to the table body
        for (i = 0; i < typeArray.length; i++){
            var metricName = typeArray[i].metricName;

            // determine if metricType (index i) has an entry
            var entry = null;
            for (j = 0; j < entriesForDateArray.length; j++){
                // if the current metricType has an entry, assign it to entry
                if (typeArray[i].metricTypeId == entriesForDateArray[j].metricType.metricTypeId){
                    entry = entriesForDateArray[j];
                    console.log("COMPLETED ENTRY: ", entry);
                }
            }

            var metricNameNoWhitespace = metricName.replace(/\s/g,'');

            // if metricType has an entry
            if (entry != null){

                // DO NOT USE FOR NOW: FUTURE FEATURE
                // var editButtonHTML = '<td><button id="' + metricNameNoWhitespace + 'EditButton" type="button" class="btn border-dark editButton">edit</button></td>';
                // var deleteButtonHTML = '<td><button id="' + metricNameNoWhitespace + 'DeleteButton" type="button" class="btn border-dark deleteButton">delete</button></td>';

                tableBody.append(
                    // label
                    '<tr id="' + entry.metricEntryId + '"><td><label for="'+ metricNameNoWhitespace + '">' + metricName + '</label></td>'
                    // input
                    + '<td><input type="number" id="' + metricNameNoWhitespace + '" class="form-control border-dark metricInput" value="' + entry.metricValue + '"</td>'
                );
            }
            // if metricType has no entry
            else{
                tableBody.append(
                    // label
                    '<tr id="' + metricNameNoWhitespace + 'Row"><td><label for="' + metricNameNoWhitespace + '" class="control-label">' + metricName + '</label></td>'
                    // input
                    + '<td><input id="' + metricNameNoWhitespace + '" name="' + typeArray[i].metricTypeId + '" class="form-control border-dark metricInput" type="number"</td></tr>'
                );
            }
        }

        // DO NOT USE FOR NOW, FUTURE FEATURE
        // editEntry(editButtonHTML, deleteButtonHTML);

    });
}

// NOT USED FOR NOW, FUTURE FEATURE
function editEntry(editButtonHTML, deleteButtonHTML){

    $('.editButton').click(function(event){
        event.stopPropagation();
        event.stopImmediatePropagation();
        // get the row for the edit button
        var rowId = $(this).parent().parent().attr("id");
        var currentRow = $('#' + rowId);

        // make the input not readonly
        var input = currentRow.find('input');
        input.prop('readOnly', false);
        input.focus();

        // make the edit and delete buttons disappear
        $(this).remove();
        currentRow.find('.deleteButton').remove();

        // add a temporary done button
        currentRow.append(
            '<td><button id="doneButton" type="button" class="btn border-dark">done</button></td>'
        );

        // when 'doneButton' is clicked, remove it, set the value back to readonly, re-add edit and delete buttons
        $('#doneButton').click(function(){
            $(this).remove();
            currentRow.append(editButtonHTML);
            currentRow.append(deleteButtonHTML);
        });


    });
}

// AJAX: HAS HARDCODED USER ID
function sendEntriesToApi(updatedEntryArray, newEntryArray, date){

    console.log("UPDATED ENTRY ARRAY: ", updatedEntryArray);

    var data = {
            "date": date,
			"updatedEntries": updatedEntryArray,
			"newEntries": newEntryArray,
			"notes": ""
    };

    $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/updateLog/" + "1",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(data),
        success: function (response) {
            getDataDisplayGraph();
            displayEntryTable(date);
        },
        error: function(xhr){
            alert("Request status: " + xhr.status + "Status text: " + xhr.statusText + xhr.responseText);
        }
    });
}

// waits for 'save changes' to be clicked
/*
    Creates an array of new entries(id, value) and an array of updated entries(type, value).
    Will send them to API in two arrays. Back-end will delete entries
    based on which ones don't exist for the DayLog.
*/
function updateEntries(date){
    $('#saveChangesButton').click(function(){

        // array to hold previously existing entries
        var updatedEntryArray = new Array();

        // array to hold new entries
        var newEntryArray = new Array();
        
        // iterate through table rows
        $('tbody tr').each(function(index){

            // get the metricEntryId
            var entryId = $(this).attr('id');

            // get the value of the entry
            var entryValue = $(this).find('input').val();

            // IF ENTRY ALREADY EXISTS
            if (!isNaN(entryId)){

                // create the updatedEntry object, push it real good
                var updatedEntry = {"entryId": entryId, "value": entryValue};
                updatedEntryArray.push(updatedEntry);
            }
            // IF ENTRY HAS A VALUE BUT IS A NEW ENTRY
            else if(isNaN(entryId) && entryValue != false){
                // create newEntry object, push it
                var newEntry = {"typeId": index + 1, "value": entryValue};
                newEntryArray.push(newEntry);
            }
        });
        console.log("UPDATED ENTRY ARRAY: ", updatedEntryArray);
        console.log("NEW ENTRY ARRAY: ", newEntryArray);

        console.log("DATE: ", date);

        sendEntriesToApi(updatedEntryArray, newEntryArray, date);
    });
}