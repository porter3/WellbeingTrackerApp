$(document).ready(function () {
    // collapse the sideBar when the button is clicked
    $('#sidebarCollapse').on('click', function() {
        $('#sidebar').toggleClass('active');
    });
    
    displayTodaysDate();
    // when back or forward button is clicked, take formatted date, add one day to it, return it
    moveDateBack();
    moveDateForward();

    getDataDisplayGraph();

    displayEntryTable();
    updateEntries();
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

function displayTodaysDate(){
    var today = new Date();
    
    // format today's date
    // padStart pads the string with a certain character until it reaches the desired length
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    today = mm + '/' + dd + '/' + yyyy;
    
    //display today's date
    $("#dateDisplay").text(today);
}

function moveDateBack(){
    $('#backButton').click(function (event) { 
        var dateComponents = $("#dateDisplay").text().split("/");
    
        console.log("DATE COMPONENT: ", dateComponents);
        var newDate = new Date(
            parseInt(dateComponents[2]),
            parseInt(dateComponents[0] - 1),
            parseInt(dateComponents[1])
        );
        console.log("UNFORMATTED DATE: ", newDate);
        newDate.setDate(newDate.getDate() - 1);
        console.log("UNFORMATTED date: ", newDate);
        var dd = String(newDate.getDate()).padStart(2, '0');
        var mm = String(newDate.getMonth() + 1).padStart(2, '0'); //January is 0!
        var yyyy = newDate.getFullYear();
        $('#dateDisplay').text(mm + '/' + dd + '/' + yyyy);

        displayEntryTable();
    });
}

function moveDateForward(){
    $('#forwardButton').click(function (event) { 
        var dateComponents = $("#dateDisplay").text().split("/");
    
        console.log("DATE COMPONENT: ", dateComponents);
        var newDate = new Date(
            parseInt(dateComponents[2]),
            parseInt(dateComponents[0] - 1),
            parseInt(dateComponents[1])
        );
        console.log("UNFORMATTED DATE: ", newDate);
        newDate.setDate(newDate.getDate() + 1);
        console.log("UNFORMATTED date: ", newDate);
        var dd = String(newDate.getDate()).padStart(2, '0');
        var mm = String(newDate.getMonth() + 1).padStart(2, '0'); //January is 0!
        var yyyy = newDate.getFullYear();
        $('#dateDisplay').text(mm + '/' + dd + '/' + yyyy);

        displayEntryTable();
    });
}

// AJAX
function getDates(userId){

    return $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/dates/" + userId,
        success: function (dates) {
        },
        error: function(xhr){
            alert("Request status: " + xhr.status + " Status text: " + xhr.statusText + " " + xhr.responseText);
        }
    });
}

// AJAX
function getMetricTypes(userId){
    return $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/metrictypes/" + userId,
        success: function (data) {
        },
        error: function(xhr){
            alert("Request status: " + xhr.status + " Status text: " + xhr.statusText + " " + xhr.responseText);
        }
    });
}

// AJAX
function getAllMetricEntries(userId){
    return $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/metricentries/" + userId,
        success: function (parentList) {
        },
        error: function(xhr){
            alert("Request status: " + xhr.status + " Status text: " + xhr.statusText + " " + xhr.responseText);
        }
    });
}

// AJAX
function getEntriesForDate(userId, date){
    return $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/metricentries/" + userId + "/" + date,
        success: function (entryList) {
        },
        error: function(xhr){
            alert("Request status: " + xhr.status + " Status text: " + xhr.statusText + " " + xhr.responseText);
        }
    });
}

function displayGraph(labels, dataSets, yAxes){
    // get the canvas element. for whatever reason, using the jQuery selector here screws this up.
    // var canvas = document.getElementById('lineGraph').getContext('2d');
    // var lineGraph;
    // // make a new graph, passing in the canvas element to display it in there
    // if(lineGraph != undefined){
    //     lineGraph.destroy();
    // }

    var ctxLine = document.getElementById("lineGraph").getContext("2d");
    // don't know what window.bar is, but doing this prevents the graph from glitching
    if(window.bar != undefined){
        window.bar.destroy(); 
    }
        window.bar = new Chart(ctxLine, {type: 'line',
        data: {
            labels: labels,
            datasets: dataSets,
        },
        options: {
            responsive: true,
            title:{
                display: false,
                text: "Metrics",
            },
            hover:{
                mode: 'nearest',
                intersect: true
            },
            scales:{
                yAxes: yAxes,
            },
            legend:{
                position: "left"
            },
            layout: {
                padding: {
                  top: 12
                }
              }
        }});
}

function getDataDisplayGraph(){

    // get the current user's ID  to pass into AJAX calls
    var userId = $('#userId').text();
    
    $.when(getDates(userId), getMetricTypes(userId), getAllMetricEntries(userId)).done(function(dates, typeList, parentList){

        // arrays to hold stuff
        var dateArray = dates[0];
        console.log("DATE ARRAY: ", dateArray);
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
            console.log("childlist: ", childList);

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
            var label = typeArray[i].metricName;

            // set each dataSet's label
            var dataSet = {
                label: label,
                yAxisID: i,
                data: dataPoints,
                backgroundColor: colors[i],
                borderColor: colors[i],
                spanGaps: true,
                fill: false,
                pointRadius: 6,
                pointHoverRadius: 5,
                lineTension: .25
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
                    display: false,
                    beginAtZero: true
                },
                display: false
            };
            yAxes.push(yAxis);
        }

        // format dates for diplaying on graph
        var dateComponents;
        for (x = 0; x < dateArray.length; x++){
            dateComponents = dateArray[x].split("-");
            dateArray[x] = dateComponents[1] + "/" + dateComponents[2];
        }

        // display graph
        displayGraph(dateArray, dataSetList, yAxes);
    });
}

// display entry table
function displayEntryTable(){
    // get all the MetricTypes associated with the user's account
    // get all the entries for the date being displayed

    var userId = $('#userId').text();
    console.log("USERID: ", userId);
    var date = $("#dateDisplay").text().split("/").join("-");
    // after all AJAX calls are made:
    $.when(getMetricTypes(userId), getEntriesForDate(userId, date)).done(function(typeList, entryList){

        var typeArray = typeList[0];
        var entriesForDateArray = entryList[0];

        console.log("TYPEARRAY: ", typeArray);

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
            var label;
            var scaleData = "";
            var nameData = 'data-name="' + typeArray[i].metricName + '"';
            var typeIdData = 'data-typeId="' + typeArray[i].metricTypeId + '"';
            console.log("TYPE ID DATA: ", typeIdData);

            // if type is subjective
            if (typeArray[i].unit === ""){
                label = "(1 - " + typeArray[i].scale + ")";
                scaleData = 'data-scale="' + typeArray[i].scale + '"';
            }
            else{
                label = "(" + typeArray[i].unit + ")";
                scaleData = 'data-scale="0"';
            }
            // if metricType has an entry
            if (entry != null){
                tableBody.append(
                    // label
                    '<tr id="' + entry.metricEntryId + '" ' + scaleData + ' ' + nameData + ' ' + typeIdData + '><td><label for="'+ metricNameNoWhitespace + '">' + metricName + " " + label + '</label></td>'
                    // input
                    + '<td><input onfocusout="validateEntryOnCursorLeave()" type="number" min="1" max="' + typeArray[i].scale +  '" id="' + metricNameNoWhitespace + '" class="form-control border-dark metricInput" value="' + entry.metricValue + '"</td>'
                );
            }
            // if metricType has no entry
            else{
                tableBody.append(
                    // label
                    '<tr id="' + metricNameNoWhitespace + 'Row" ' + scaleData + ' ' + nameData + ' ' + typeIdData + '><td><label for="' + metricNameNoWhitespace + '" class="control-label">' + metricName + " " + label + '</label></td>'
                    // input
                    + '<td><input onfocusout="validateEntryOnCursorLeave()" id="' + metricNameNoWhitespace + '" name="' + typeArray[i].metricTypeId + '" class="form-control border-dark metricInput" type="number" min="1" max="' + typeArray[i].scale + '"</td></tr>'
                );
            }
        }

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

// AJAX
function sendEntriesToApi(userId, updatedEntryArray, newEntryArray, date){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = {
            "date": date,
			"updatedEntries": updatedEntryArray,
			"newEntries": newEntryArray,
			"notes": ""
    };
    
    $(document).ajaxSend(function (e, xhr, options) {
      xhr.setRequestHeader(header, token);
    });
    
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/updateLog/" + userId,
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
            alert("Request status: " + xhr.status + " Status text: " + xhr.statusText + " " + xhr.responseText);
        }
    });
}

// waits for 'save changes' to be clicked
/*
    Creates an array of new entries(id, value) and an array of updated entries(type, value).
    Will send them to API in two arrays. Back-end will delete entries
    based on which ones don't exist for the DayLog.
*/
function updateEntries(){
    $('#saveChangesButton').click(function(){
        // array to hold previously existing entries
        var updatedEntryArray = new Array();

        // array to hold new entries
        var newEntryArray = new Array();
        
        // iterate through table rows
        $('tbody tr').each(function(index){

            // get the metricEntryId
            var entryId = $(this).attr('id');

            // get the metricTypeId
            var typeId = $(this).attr('data-typeId');

            // get the value of the entry
            var entryValue = $(this).find('input').val();

            // get the scale of the type if it exists
            var scale = 0;
            if ($(this).attr('data-scale') != 0){
                // check for negative inputs (done seperate here because I want a different message)
                if (entryValue < 0){
                    alert("You can't log a negative value for " + $(this).attr('data-name') + ". Hopefully that wasn't done on purpose.");
                    return;
                }
                scale = parseInt($(this).attr('data-scale'));
                // if scale is not 0, check that the metric's value does not exceed it
                if (entryValue > scale){
                    // NONESSENTIAL TODO: add error message below row
                    alert('Values for ' + $(this).attr('data-name') + ' must be between 1 and ' + scale + '.');
                    return;
                }
            }

            // check any non-subjective values for negative inputs
            if (entryValue < 0){
                alert("Logging a negative value for " + $(this).attr('data-name') + " doesn't make too much sense.");
                return;
            }

            // IF ENTRY ALREADY EXISTS
            if (!isNaN(entryId)){

                // create the updatedEntry object, push it real good
                var updatedEntry = {"entryId": entryId, "value": entryValue};
                updatedEntryArray.push(updatedEntry);
            }
            // IF ENTRY HAS A VALUE BUT IS A NEW ENTRY
            else if(isNaN(entryId) && entryValue != false){
                // create newEntry object, push it
                var newEntry = {"typeId": typeId, "value": entryValue};
                newEntryArray.push(newEntry);
            }
        });
        console.log("UPDATED ENTRY ARRAY: ", updatedEntryArray);
        console.log("NEW ENTRY ARRAY: ", newEntryArray);

        var userId = $('#userId').text();
        var date = $("#dateDisplay").text().split("/").join("-");
        sendEntriesToApi(userId, updatedEntryArray, newEntryArray, date);
    });
}

function validateEntryOnCursorLeave(){
}