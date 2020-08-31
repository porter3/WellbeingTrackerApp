$(document).ready(function () {
    $('#sidebarCollapse').on('click', () => {
        $('#sidebar').toggleClass('active');
    });

    // URL variables - only change localBuild
    const localBuild = false;
    const server = localBuild ? 'localhost:8080' : 'wbt-war-rdsConnected-env.eba-fwuvtiv7.us-east-1.elasticbeanstalk.com';
    const apiUrl = 'http://' + server + '/api';
    const userId = $('#userId').text();
    
    displayTodaysDate();
    
    $.when(getMetricTypes(apiUrl, userId)).done((typeArray) => {
        moveDateBack(apiUrl, userId, typeArray);
        moveDateForward(apiUrl, userId, typeArray);
        getDataDisplayGraph(apiUrl, typeArray);
        displayEntryTable(apiUrl, userId, typeArray);
        updateEntriesAndNotes(apiUrl, userId, typeArray);
    });
});

function displayTodaysDate(){
    let today = new Date();
    
    // format today's date
    // padStart pads the string with a certain character until it reaches the desired length
    const dd = String(today.getDate()).padStart(2, '0');
    const mm = String(today.getMonth() + 1).padStart(2, '0'); // January is 0
    const yyyy = today.getFullYear();

    today = mm + '/' + dd + '/' + yyyy;
    
    $("#dateDisplay").text(today);
}

function moveDate(apiUrl, userId, typeArray, dateChangeDirection){
    const dateComponents = $("#dateDisplay").text().split("/");
    
        let newDate = new Date(
            parseInt(dateComponents[2]),
            parseInt(dateComponents[0] - 1),
            parseInt(dateComponents[1])
        );
        switch(dateChangeDirection){
            case 'back':
                newDate.setDate(newDate.getDate() - 1);
                break;
            case 'forward':
                newDate.setDate(newDate.getDate() + 1);
                break;
            default:
                alert('You broke the date button somehow');
        }
        const dd = String(newDate.getDate()).padStart(2, '0');
        const mm = String(newDate.getMonth() + 1).padStart(2, '0'); // January is 0
        const yyyy = newDate.getFullYear();
        $('#dateDisplay').text(mm + '/' + dd + '/' + yyyy);

        displayEntryTable(apiUrl, userId, typeArray);
}

function moveDateBack(apiUrl, userId, typeArray){
    $('#backButton').click(() => { 
        moveDate(apiUrl, userId, typeArray, 'back');
    });
}

function moveDateForward(apiUrl, userId, typeArray){
    $('#forwardButton').click(() => { 
        moveDate(apiUrl, userId, typeArray, 'forward');
    });
}

// AJAX
function getNotes(apiUrl, userId, date){
    return $.ajax({
        type: "GET",
        url: apiUrl + '/notes/' + userId + '/' + date,
        success: () => {},
        error: (xhr) => {
            alert("Request status: " + xhr.status + " Status text: " + xhr.statusText + " " + xhr.responseText + ' - NOTES NOT RETRIEVED');
        }
    });
}

// AJAX
function getDates(apiUrl, userId){
    return $.ajax({
        type: "GET",
        url: apiUrl + '/dates/' + userId,
        success: () => {},
        error: (xhr) => {
            alert("Request status: " + xhr.status + " Status text: " + xhr.statusText + " " + xhr.responseText + ' - DATES NOT RETRIEVED');
        }
    });
}

// AJAX
function getMetricTypes(apiUrl, userId){
    console.log('getMetricTypes is running');
    return $.ajax({
        type: "GET",
        url: apiUrl + '/metrictypes/' + userId,
        startTime: performance.now(),
        success: (response) => { console.log('success? : ' , response) },
        error: (xhr) => {
            alert("Request status: " + xhr.status + " Status text: " + xhr.statusText + " " + xhr.responseText +" - TYPES NOT RETRIEVED - " + apiUrl + '/metrictypes/' + userId);
        }
    });
}

// AJAX
function getAllMetricEntries(apiUrl, userId){
    return $.ajax({
        type: "GET",
        url: apiUrl + '/metricentries/' + userId,
        startTime: performance.now(),
        success: function(){
            // performance logging
            const timeToComplete = performance.now() - this.startTime;
            console.log('getAllMetricEntries() took ', (timeToComplete / 1000).toFixed(3), ' seconds to complete');
        },
        error: (xhr) => {
            alert("Request status: " + xhr.status + " Status text: " + xhr.statusText + " " + xhr.responseText + ' - METRIC ENTRIES NOT RETRIEVED - ');
        }
    });
}

// AJAX
function getEntriesForDate(apiUrl, userId, date){
    return $.ajax({
        type: "GET",
        url: apiUrl + '/metricentries/' + userId + '/' + date,
        success: () => {},
        error: (xhr) => {
            alert("Request status: " + xhr.status + " Status text: " + xhr.statusText + " " + xhr.responseText + ' - ENTRIES FOR DATE NOT RETRIEVED');
        }
    });
}

function displayGraph(labels, dataSets, yAxes){
    // get the canvas element- for whatever reason, using the jQuery selector here screws this up

    const ctxLine = document.getElementById("lineGraph").getContext("2d");
    // prevents the graph from glitching / reverting to previous state when moving cursor over it
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

function getDataDisplayGraph(apiUrl, typeArray){

    // get the current user's ID  to pass into AJAX calls
    const userId = $('#userId').text();
    
    $.when(getDates(apiUrl, userId), getAllMetricEntries(apiUrl, userId)).done((dates, parentList) => {

        const dateArray = dates[0];
        const parentArrayForEntries = parentList[0]; // this array's child arrays are sorted by metricType 

        const subjectiveColors = [
            '#FFEB3B',
            '#FBC02D',
            '#FFCA28',
            '#FF8F00',
            '#FFEE58',
            '#FFF59D'
        ];
        const quantitativeColors = [
            "#e6194B", // red
            "#3cb44b", // green
            "#4363d8", // blue
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
        let dataSetList = new Array();

        // counters for determining which color to assign
        let quantitativeColorCounter = 0;
        let subjectiveColorCounter = 0;

        // Iterating over MetricTypes
        // for each MetricType, create a new dataSet
        for (i = 0; i < parentArrayForEntries.length; i++){
            // get the index/typeList in parentList
            const childList = parentArrayForEntries[i];

            let dataPoints = new Array();

            // Iterating over entries in a type-specific list
            /* 
            for each date that exists for the user, check if it equals the date
            contained within a child entry. If there is no child entry for that date,
            add a null value instead.
            */
            let childListIndex = 0;

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

            // set each dataSet's label
            const dataSet = {
                label: typeArray[i].metricName,
                yAxisID: i,
                data: dataPoints,
                spanGaps: true,
                fill: false,
                pointRadius: 6,
                pointHoverRadius: 5,
                lineTension: .25
            };

            // setting line colors - subjective data sets are a shade of orange/yellow
            if (typeArray[i].scale != 0){
                dataSet.backgroundColor = subjectiveColors[subjectiveColorCounter];
                dataSet.borderColor = subjectiveColors[subjectiveColorCounter];
                subjectiveColorCounter++;
            }
            else{
                dataSet.backgroundColor = quantitativeColors[quantitativeColorCounter];
                dataSet.borderColor = quantitativeColors[quantitativeColorCounter];
                quantitativeColorCounter++;
            }
            dataSetList.push(dataSet);
        }

        // Y-AXES
        let yAxes = new Array();
        for (k = 0; k < dataSetList.length; k++){
            const yAxis = {
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
        let dateComponents;
        for (x = 0; x < dateArray.length; x++){
            dateComponents = dateArray[x].split("-");
            dateArray[x] = dateComponents[1] + "/" + dateComponents[2];
        }

        // display graph
        displayGraph(dateArray, dataSetList, yAxes);
    });
}

function displayEntryTable(apiUrl, userId, typeArray){

    const date = $("#dateDisplay").text().split("/").join("-");
    $.when(getEntriesForDate(apiUrl, userId, date), getNotes(apiUrl, userId, date)).done((entryList, notes) => {
        
        // get all the entries for the date currently displayed
        const entriesForDateArray = entryList[0];

        const tableBody = $('#entryTableBody');
        // clear entry table
        tableBody.empty();

        // display today's notes
        $('#notesArea').val(notes[0]);

        // for all the metricTypes in typeArray, append a row to the table body
        for (i = 0; i < typeArray.length; i++){
            const metricName = typeArray[i].metricName;

            // determine if metricType[i] has an entry
            let entry = null;
            for (j = 0; j < entriesForDateArray.length; j++){
                // if the current metricType has an entry, assign it to entry
                if (typeArray[i].metricTypeId === entriesForDateArray[j].metricType.metricTypeId){
                    entry = entriesForDateArray[j];
                }
            }

            const metricNameNoWhitespace = metricName.replace(/\s/g,'');
            let label;
            let scaleData = "";
            const nameData = 'data-name="' + typeArray[i].metricName + '"';
            const typeIdData = 'data-typeId="' + typeArray[i].metricTypeId + '"';

            // if type is subjective, set label appropriately and make scale != 0
            if (typeArray[i].unit === ""){
                label = "(1 - " + typeArray[i].scale + ")";
                scaleData = 'data-scale="' + typeArray[i].scale + '"';
            }
            // if type is subjective, set label appropriately and make scale == 0
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
                    + '<td><input type="number" min="1" max="' + typeArray[i].scale +  '" id="' + metricNameNoWhitespace + '" class="form-control border-dark metricInput" value="' + entry.metricValue + '"</td>'
                );
            }
            // if metricType has no entry
            else{
                tableBody.append(
                    // label
                    '<tr id="' + metricNameNoWhitespace + 'Row" ' + scaleData + ' ' + nameData + ' ' + typeIdData + '><td><label for="' + metricNameNoWhitespace + '" class="control-label">' + metricName + " " + label + '</label></td>'
                    // input
                    + '<td><input id="' + metricNameNoWhitespace + '" name="' + typeArray[i].metricTypeId + '" class="form-control border-dark metricInput" type="number" min="1" max="' + typeArray[i].scale + '"</td></tr>'
                );
            }
        }
    });
}

// AJAX
function sendEntriesToApi(apiUrl, userId, updatedEntryArray, newEntryArray, date, notes, typeArray){
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    const data = {
            "date": date,
			"updatedEntries": updatedEntryArray,
			"newEntries": newEntryArray,
			"notes": notes
    };
    
    $(document).ajaxSend((e, xhr) => {
        xhr.setRequestHeader(header, token);
    });
    
    $.ajax({
        type: "POST",
        url: apiUrl + '/updateLog/' + userId,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(data),
        success: () => {
            getDataDisplayGraph(apiUrl, typeArray);
            displayEntryTable(apiUrl, userId, typeArray);
        },
        error: (xhr) => {
            alert("Request status: " + xhr.status + " Status text: " + xhr.statusText + " " + xhr.responseText);
        }
    });
}

// waits for 'save changes' to be clicked
/*
    Creates an array of new entries(id, value) and an array of updated entries(type, value).
    Will send them to API in two arrays. Back-end will delete entries
    based on which ones don't exist for the DayLog (a dumb semantic for date).
*/
function updateEntriesAndNotes(apiUrl, userId, typeArray){
    $('#saveChangesButton').click(() => {
        // array to hold previously existing entries
        let updatedEntryArray = new Array();

        // array to hold new entries
        let newEntryArray = new Array();
        
        // iterate through table rows
        $('tbody tr').each(function(index){

            // get the metricEntryId
            const entryId = $(this).attr('id');

            // get the metricTypeId
            const typeId = $(this).attr('data-typeId');

            // get the value of the entry
            const entryValue = $(this).find('input').val();

            // declare boolean isEmpty - this exists because Spring will convert blank entryValues to 0
            let valueIsEmpty = false;

            // get the scale of the type if it exists
            let scale = 0;
            if ($(this).attr('data-scale') != 0){
                // check for negative inputs (done seperately here for the sake of different error messages for subjective/quantitative types)
                if (entryValue < 0){
                    alert("You can't log a negative value for " + $(this).attr('data-name') + ". Hopefully that wasn't done on purpose.");
                    return;
                }
                scale = parseInt($(this).attr('data-scale'));
                // if scale is not 0, check that the metric's value does not exceed it
                if (entryValue > scale || (entryValue <= 0 && entryValue != '')){
                    // TODO: add error message below row instead of using alert
                    alert('Values for ' + $(this).attr('data-name') + ' must be between 1 and ' + scale + '.');
                    return;
                }
            }

            // check any non-subjective values for negative inputs
            if (entryValue < 0){
                alert("Logging a negative value for " + $(this).attr('data-name') + " doesn't make too much sense.");
                return;
            }

            // if entryValue is blank, add a boolean to mark it for deletion
            if (entryValue === ''){
                valueIsEmpty = true;
            }

            // IF ENTRY ALREADY EXISTS
            if (!isNaN(entryId)){

                // create the updatedEntry object, push it real good
                const updatedEntry = {"entryId": entryId, "value": entryValue, valueIsEmpty: valueIsEmpty};
                updatedEntryArray.push(updatedEntry);
            }
            // IF ENTRY HAS A VALUE BUT IS A NEW ENTRY
            else if(isNaN(entryId) && entryValue !== ''){
                // create newEntry object, push it
                const newEntry = {"typeId": typeId, "value": entryValue};
                newEntryArray.push(newEntry);
            }
        });

        const notes = $('#notesArea').val();
        const date = $("#dateDisplay").text().split("/").join("-");
        sendEntriesToApi(apiUrl, userId, updatedEntryArray, newEntryArray, date, notes, typeArray);
    });
}