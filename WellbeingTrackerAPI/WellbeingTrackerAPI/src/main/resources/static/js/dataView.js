$(document).ready(function () {

    // collapse the sidebar when the button is clicked
   $('#sidebarCollapse').on('click', function() {
       $('#sidebar').toggleClass('active');
   });

   displayDummyGraph();
   getDataDisplayGraph();
   updateGraph();

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
                yAxes: [{
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

function getDates(){
    return $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/dates/1",
        success: function (dates) {
        },
        error: function(xhr){
            alert("Request status: " + xhr.status + " Status text: " + xhr.statusText + " " + xhr.responseText);
        }
    });
}

function getDataDisplayGraph(){
    // get the current user's ID (will just use a hardcoded one for jakep310 right now)

    var dateArray= new Array();
    var typeArray = new Array();
    var parentArrayForEntries = new Array();

    // populate dateArray
    $.when(getDates().done(function(response){
        $.each(response, function (index, date) { 
             dateArray.push(date);
        });
        console.log("DATE ARRAY: " + dateArray.toString());
    }));

    // populate typeArray
    
}

// same as display but waits for button click
function updateGraph(){
    $('#updateGraphButton').click(function(){
        alert('update graph button clicked');
        displayGraph();
    });
}