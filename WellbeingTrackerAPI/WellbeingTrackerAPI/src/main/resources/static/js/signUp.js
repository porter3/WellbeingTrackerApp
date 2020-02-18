$(document).ready(function () {
    
});

// function validateUsername(){
//     var username = $('#username').val();
//     var submitButton = $('#signUpButton');
//     console.log("username: ", username);

//     $.ajax({
//         type: "POST",
//         url: "http://localhost:8080/api/checkusername/" + username,
//         headers: {
//             'Accept': 'application/json',
//             'Content-Type': 'application/json'
//         },
//         success: function (isTaken) {
//             if (isTaken){
//                 $('#usernameGroup').append('<small hidden id="usernameError" class="text-danger">username is already taken</small>');
//             }
//             else{
//                 alert('all good');
//             }
//         },
//         error: function(){
//             alert('username lookup failed');
//         }
//     });
// }