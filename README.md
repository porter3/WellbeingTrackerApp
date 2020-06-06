## Well-being Tracker

# Demo link: https://tinyurl.com/ycm6w4e6
At the moment, there's a bug that causes it to stall during the first login attempt and then work the second time you attempt to log in.

This application's purpose is to allow a user to choose certain health-related metrics to track on a regular basis
(i.e. total exercise time, hours of sleep, social time, etc.) and display them 
on a line graph next to each other. These metrics can be quantitative (i.e. hours of sleep), or 
subjective (i.e. mood), the latter of which is rated on a scale of 1-10. Users can 
create an account, add/remove metric types to track, and log metric entries for a given date.

Main page UI makes AJAX requests to a REST API built with Java/Spring Boot.

Still a work in progress. Future goals include further refactoring, recommendation engine, deployment, API performance improvements, 
progressive web app conversion, CSS changes to improve UI responsiveness, input validation 
prior to sending data, reading user nutrition data from an external API.