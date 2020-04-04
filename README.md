# **Well-being Tracker**

This application's purpose is to allow a user to choose certain health-related metrics to track on a regular basis
(i.e. total exercise time, hours of sleep, social time, etc.) and display them 
on a line graph next to each other. These metrics can be quantitative (i.e. hours of sleep), or 
subjective (i.e. mood), the latter of which is rated on a scale of 1-10. Users can 
create an account, add/remove metric types to track, and log metric entries for a given date.

Main page UI makes AJAX calls to a Java/Spring Boot REST API to get/post data.

Still a work in progress. Future goals include deployment, further refactoring, API performance improvements, 
converting to a progressive web app, CSS improvements to improve UI responsiveness, input validation 
prior to sending data, reading user data from external APIs.