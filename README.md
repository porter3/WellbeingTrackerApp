# Well-being Tracker

### Demo link: https://tinyurl.com/ycm6w4e6
*At the moment, there's a bug that causes it to stall during the first login attempt and then work the second time you attempt to log in.*

#### For any potential employers:
*Not all, but a lot of this code (particularly the JavaScript) is not designed well. 
It was originally written under a tight deadline with not much focus on maintainability. 
Also, my knowledge of good design patterns has progressed quite a bit since this was made.*

This application's purpose is to allow a user to choose certain health-related metrics to track on a regular basis
(i.e. total exercise time, hours of sleep, social time, etc.) and display them 
on a line graph next to each other. These metrics can be quantitative (i.e. hours of sleep), or 
subjective (i.e. mood), the latter of which is rated on a scale of 1-10. Users can 
create an account, add/remove metric types to track, and log metric entries for a given date.

Main page UI makes AJAX requests to a REST API built with Java/Spring Boot.

Still a work in progress. Future goals include redoing UI with React (and potentially changing some API methods), validation after an input's change, 
recommendation engine, progressive web app conversion, reading user nutrition data from an external API.