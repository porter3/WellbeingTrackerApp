# Well-being Tracker

### Demo link: https://tinyurl.com/ycm6w4e6

*Current focus at the moment is redoing the UI with React and some minor refactoring on the back-end.*

How to use:
1. Create an account
2. Select "add metrics" from the menu and select the metrics you want to track
3. Add/edit your entries for a given date and click "save changes" to have the entry values reflected
4. If you wish to not track a metric anymore, select "remove metrics" from the menu and select the metric you want to delete

This app allows a user to choose certain health-related metrics to track on a regular basis
(i.e. total exercise time, hours of sleep, social time, etc.) and display them 
on a line graph next to each other. These metrics can be quantitative (i.e. hours of sleep), or 
subjective (i.e. mood), the latter of which is rated on a scale of 1-10.

#### *What's the point of this?*
Many people are well-aware of what's good for their health. Getting quality sleep, social interaction, and
eating nutritious food are some examples of habits we know are important. For many of us, there's a roadblock 
that can keep us from being diligent in practicing healthy habits. We may be aware of how we feel on a given 
day, but we don't pay much attention to why we feel that way. **This app is a tool to help facilitate self-awareness 
about how certain behaviors may make us feel. Understanding that a certain behavior makes you feel better or worse 
will hopefully motivate you to be diligent about repeating or avoiding that behavior.**

#### *But correlation doesn't equal causation, right?*
That's right, it doesn't. This app is not intended to be a foolproof way of knowing why you feel the way to do. However, 
using the app for a long-enough period of time is likely to display some significant relationship between certain healthy habits 
and your subjective wellbeing. Changing your behavior based on a false pattern is unlikely to be harmful if you use 
common sense (i.e. choosing to sleep less if a negative correlation exists between sleep time and energy levels), and some 
false patterns may even be beneficial if acted upon (i.e. choosing to eat less sugar when a nonsignificant positive correlation
exists between sugar intake and stress).

--------------------

Still a work in progress. Future goals include a redone UI with React, recommendation engine, 
progressive web app conversion, and reading user nutrition data from an external API.