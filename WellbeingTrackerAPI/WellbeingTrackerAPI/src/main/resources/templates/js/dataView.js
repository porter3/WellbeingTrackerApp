$(document).ready(function () {

    // collapse the sidebar when the button is clicked
   $('#sidebarCollapse').on('click', function() {
       $('#sidebar').toggleClass('active');
   });

   displayGraph();
   updateGraph();

});

function displayGraph(){

}

// same as display but waits for button click
function updateGraph(){
    $('#updateGraphButton').click(function(){
        alert('update graph button clicked');
        displayGraph();
    });
}