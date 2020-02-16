$(document).ready(function () {

    // collapse the sidebar when the button is clicked
    $('#sidebarCollapse').on('click', function() {
        $('#sidebar').toggleClass('active');
    });

    var predefinedSleepTypes = [
        {
            metricName: "sleep time",
            scale: "",
            unit: "minhours",
        },
        {
            metricName: "sleep quality",
            scale: "10",
            unit: "min",
        }
    ];
    var predefinedNutritionTypes = [
        {
            metricName: "calories",
            scale: "",
            unit: "minkcal",
        },
        {
            metricName: "carbohydrates",
            scale: "",
            unit: "ming",
        },
        {
            metricName: "protein",
            scale: "",
            unit: "ming",
        },
        {
            metricName: "total fat",
            scale: "",
            unit: "ming",
        },
        {
            metricName: "fiber",
            scale: "",
            unit: "ming",
        },
        {
            metricName: "saturated fat",
            scale: "",
            unit: "ming",
        },
        {
            metricName: "sugar",
            scale: "",
            unit: "ming",
        },
        {
            metricName: "sodium",
            scale: "",
            unit: "ming",
        }
    ];
    var predefinedExerciseTypes = [
        {
            metricName: "total exercise",
            scale: "",
            unit: "minmin",
        },
        {
            metricName: "resistance training",
            scale: "",
            unit: "minmin",
        },
        {
            metricName: "cardiovascular training",
            scale: "",
            unit: "minmin",
        }
    ];
    var predefinedMentalTypes = [
        {
            metricName: "social time",
            scale: "",
            unit: "min",
        },
        {
            metricName: "meditation",
            scale: "",
            unit: "min",
        },
        {
            metricName: "prayer",
            scale: "",
            unit: "min",
        },
        {
            metricName: "silence",
            scale: "",
            unit: "min",
        },
        {
            metricName: "time outdoors",
            scale: "",
            unit: "min",
        },
        {
            metricName: "reading",
            scale: "",
            unit: "min",
        },
        {
            metricName: "studying",
            scale: "",
            unit: "min",
        },
        {
            metricName: "watching tv",
            scale: "",
            unit: "min",
        },
        {
            metricName: "playing video games",
            scale: "",
            unit: "min",
        }
    ];
    var predefinedSubjectiveTypes = [
        {
            metricName: "mood",
            scale: "10",
            unit: "",
        },
        {
            metricName: "energy",
            scale: "10",
            unit: "",
        },
        {
            metricName: "sociability",
            scale: "10",
            unit: "",
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

// AJAX: HAS HARDCODED USER ID
function getMetricTypes(){
    return $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/metrictypes/" + "1",
        success: function (metricTypesForUser) {
            
        },
        error: function(xhr){
            alert("Request status: " + xhr.status + " Status text: " + xhr.statusText + " " + xhr.responseText);
        }
    });
}

function displayMetricTypes(allPredefinedTypes){
    $.when(getMetricTypes()).done(function(metricTypesForUser){

        console.log("PREDEFINED TYPES: ", allPredefinedTypes);
        console.log("METRIC TYPES FOR USER: ", metricTypesForUser);

        var metricDisplayArea = $('#metricAddingArea');

            // iterate over all of the type-arrays in allPredefinedTypes
            for (i = 0; i < allPredefinedTypes.length; i++){
                // 'print' type headers when appropriate. can refactor this later by adding names for each parent type in allPredfinedTypes. No repeating code then.
                if (i == 0){
                    metricDisplayArea.append('<div id="physical" class="col"><h4 class="typeHeader">physical</h4>');
                }
                if (i == 3){
                    metricDisplayArea.append('<div id="mental" class="col"><h4 class="typeHeader">mental</h4>');
                }
                if (i == 4){
                    metricDisplayArea.append('<div id="subjective" class="col"><h4 class="typeHeader">subjective</h4>');
                }

                // 'print' the subTypeHeader to the HTML
                metricDisplayArea.append('<h5 class="subTypeHeader">' + allPredefinedTypes[i].name + '</h5></ul>');

                // iterate over all the predefined types in a type-array
                for (j = 0; j < allPredefinedTypes[i].types.length; j++){
                    var predefinedTypeName = allPredefinedTypes[i].types[j].metricName;

                    // iterate over each type a user has
                    var userHasType = false;
                    for (k = 0; k < metricTypesForUser.length; k++){

                        if (predefinedTypeName === metricTypesForUser[k].metricName){
                            userHasType = true;
                        }
                    }
                    if (!userHasType){
                        metricDisplayArea.append('<p>' + predefinedTypeName + '</p>');
                    }
                    else{
                        console.log("USER HAS THIS TYPE: ", metricTypesForUser[k-1].metricName);
                    }
                }
                metricDisplayArea.append('</ul>');
                if (j == 0 || j == 3 || j == 4){
                    metricDisplayArea.append('</div>');
                }

            }

    });
}