$(document).ready(function () {
    $('#sidebarCollapse').on('click', function() {
        $('#sidebar').toggleClass('active');
    });

    var predefinedSleepTypes = [
        {
            metricName: "sleep time",
            scale: 0,
            unit: "hours",
        },
        {
            metricName: "sleep quality",
            scale: 10,
            unit: "",
        }
    ];
    var predefinedNutritionTypes = [
        {
            metricName: "calories",
            scale: 0,
            unit: "kcal",
        },
        {
            metricName: "carbohydrates",
            scale: 0,
            unit: "g",
        },
        {
            metricName: "protein",
            scale: 0,
            unit: "g",
        },
        {
            metricName: "total fat",
            scale: 0,
            unit: "g",
        },
        {
            metricName: "fiber",
            scale: 0,
            unit: "g",
        },
        {
            metricName: "saturated fat",
            scale: 0,
            unit: "g",
        },
        {
            metricName: "sugar",
            scale: 0,
            unit: "g",
        },
        {
            metricName: "sodium",
            scale: "",
            unit: "g",
        }
    ];
    var predefinedExerciseTypes = [
        {
            metricName: "total exercise",
            scale: 0,
            unit: "min",
        },
        {
            metricName: "resistance training",
            scale: 0,
            unit: "min",
        },
        {
            metricName: "cardiovascular training",
            scale: 0,
            unit: "min",
        }
    ];
    var predefinedMentalTypes = [
        {
            metricName: "social time",
            scale: 0,
            unit: "min"
        },
        {
            metricName: "meditation",
            scale: 0,
            unit: "min"
        },
        {
            metricName: "prayer",
            scale: 0,
            unit: "min"
        },
        {
            metricName: "silence",
            scale: 0,
            unit: "min"
        },
        {
            metricName: "time outdoors",
            scale: 0,
            unit: "min"
        },
        {
            metricName: "reading",
            scale: 0,
            unit: "min"
        },
        {
            metricName: "studying",
            scale: 0,
            unit: "min"
        },
        {
            metricName: "watching tv",
            scale: 0,
            unit: "min"
        },
        {
            metricName: "playing video games",
            scale: 0,
            unit: "min"
        }
    ];
    var predefinedSubjectiveTypes = [
        {
            metricName: "mood",
            scale: 10,
            unit: ""
        },
        {
            metricName: "energy",
            scale: 10,
            unit: ""
        },
        {
            metricName: "sociability",
            scale: 10,
            unit: ""
        },
        {
            metricName: "stress",
            scale: 10,
            unit: ""
        }
    ];
    var allPredefinedTypes = [
        {name: "sleep", types: predefinedSleepTypes},
        {name: "nutrition", types: predefinedNutritionTypes}, 
        {name: "exercise", types: predefinedExerciseTypes}, 
        {name: "", types: predefinedMentalTypes}, 
        {name: "", types: predefinedSubjectiveTypes}
    ];

    displayMetricTypes(allPredefinedTypes);
});

// AJAX
function getMetricTypes(userId){
    console.log("USER ID: ", userId);
    return $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/metrictypes/" + userId,
        success: function (metricTypesForUser) {
            
        },
        error: function(xhr){
            alert("Request status: " + xhr.status + " Status text: " + xhr.statusText + " " + xhr.responseText);
        }
    });
}

// AJAX
function sendAddedMetricTypesToApi(userId, metricTypeArray){

    $('#addMetricsButton').click(function (event) { 
        event.preventDefault();
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
        
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/api/addmetrictypes/" + userId,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: JSON.stringify(metricTypeArray),
            success: function (response) {
                window.location.replace("content");
            },
            error: function(xhr){
                alert("Request status: " + xhr.status + "Status text: " + xhr.statusText + xhr.responseText);
            }
        });
    });
}

function displayMetricTypes(allPredefinedTypes){
    var userId = $('#userId').text();

    $.when(getMetricTypes(userId)).done(function(metricTypesForUser){

        var metricDisplayArea = $('#metricDisplayArea');
        var typesUserDoesntHave = new Array();
        var typesToAddForUser = new Array();
        var html = '';

            // iterate over all of the type-arrays in allPredefinedTypes
            for (i = 0; i < allPredefinedTypes.length; i++){
                // 'print' type headers when appropriate. can refactor this later by adding names for each parent type in allPredfinedTypes. No repeating code then.
                if (i == 0){
                    html += '<div id="physical" class="typeHeader col"><h3>physical</h3>';
                }
                if (i == 3){
                    html += '</div><div id="mental" class="typeHeader col"><h3>mental</h3>';
                }
                if (i == 4){
                    html += '</div><div id="subjective" class="typeHeader col"><h3>subjective</h3>';
                }

                // 'print' the subTypeHeader to the HTML
                html += '<h5 class="subtypeHeader">' + allPredefinedTypes[i].name + '</h5>';

                // iterate over all the predefined types in a type-array
                for (j = 0; j < allPredefinedTypes[i].types.length; j++){
                    var predefinedTypeName = allPredefinedTypes[i].types[j].metricName;
                    var predefinedTypeScale = allPredefinedTypes[i].types[j].scale;
                    var predefinedTypeUnit = allPredefinedTypes[i].types[j].unit;

                    // iterate over each type a user has
                    var userHasType = false;
                    for (k = 0; k < metricTypesForUser.length; k++){

                        if (predefinedTypeName === metricTypesForUser[k].metricName){
                            userHasType = true;
                        }
                    }
                    if (!userHasType){
                        // if type is subjective (i.e. has a scale)
                        if (predefinedTypeUnit === ''){
                            html += '<a class="type" id="' + predefinedTypeName + '"><p class="item">' + predefinedTypeName + ' (1 - ' + predefinedTypeScale + ')</p></a>';
                        }
                        else{
                            html += '<a class="type" id="' + predefinedTypeName + '"><p class="item">' + predefinedTypeName + ' (' + predefinedTypeUnit + ')</p></a>';
                        }
                        // add each type that user doesn't have to an array
                        typesUserDoesntHave.push(allPredefinedTypes[i].types[j]);
                    }
                    else{
                        console.log("USER HAS THIS TYPE: ", metricTypesForUser[k-1].metricName);
                    }                    
                }
            }
            console.log("TYPES USER DOESN'T HAVE: ", typesUserDoesntHave);
            metricDisplayArea.append(html);


            // add items to metricAddingArea, return them as types in an array
            $('a.type').click(function (element) { 
                element.preventDefault();
                
                $('#metricAddingArea').append('<p id="' + $(this).attr('id') + '">' + $(this).text() + '</p>');

                // add the element in typesUserDoesntHave with the name of the element selected
                for (l = 0; l < typesUserDoesntHave.length; l++){

                    if ($(this).attr('id') === typesUserDoesntHave[l].metricName){
                        typesToAddForUser.push(typesUserDoesntHave[l]);
                        console.log("types to add for user: ", typesToAddForUser);

                        typesToAddForUser.forEach(function(incompleteType){
                            incompleteType.metricTypeId = 0;
                            incompleteType.user = {
                            userAccountId: 0,
                            username: "",
                            password: "",
                            firstName: "",
                            lastName: "",
                            email: "",
                            creationTime: "",
                            timeZone: ""
                            };
                        });
                        console.log('COMPLETE TYPES TO ADD: ', typesToAddForUser);
                    }
                }
                $(this).remove();
            });

            // convert types in typesToAddForUser into full MetricTypes for the API to use
            typesToAddForUser.forEach(function(incompleteType){
                incompleteType.metricTypeId = 0;
                incompleteType.user = {
                userAccountId: 0,
                username: "",
                password: "",
                firstName: "",
                lastName: "",
                email: "",
                creationTime: "",
                timeZone: ""
                };
            });

            // send metricTypes to API
            sendAddedMetricTypesToApi(userId, typesToAddForUser);
    });
}