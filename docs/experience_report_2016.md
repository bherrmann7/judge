

# An Experience Report

On Saturday, April 2nd, 2016 the Judge application was used by the Groton Dunstable Elementary School's Science Fair team
to judge.  More details about the fair and its information and rules are at http://gdesciencefair.org
I, Bob Herrmann, am the developer for the judge application and was responsible for ensuring it ran correctly on the
day of the fair.

Overall the app was a success.   All the students were judged with a minimum of mayhem and fuss.


## Training - Prior to the Fair

In preparation for the fair, there were two training sessions for judges.   The fair had 26 judges for 94
projects.  During training the judges were encouraged to connect to the judge application and score a project.
Accessing the application required:  1. join the "sciencefair" wifi network.  2. navigate to the provided IP address.
3. choose the judge name from the dropdown  4. type in the password

Judge traing went well.  There was a setup issue in the first training session, but we were still able to
step the judges through the process with a projection screen.   The second training session went well.

ACTION: Make sure the hardware setup works well for judge training.

## Check-in at beginning of Fair

Very similar to last year.  I ended up helping check in students.   I really could have benefited from a small script
or overview of topics to cover with each student.   I welcomed them, wrote their table position down, and
sent them into the gym.   I also handed them the passport and stickers, although I wasnt entirely clear on
how they were to be used.

Checkin was supposed to start at about 12:45pm, but it almost got to 1pm before we were checking people in.
There as a long line - almost out the door.   I think this could have
been improved with a little more attention.  Although nobody seemed to be upset about the wait.
Perhaps we should "train" checkin people.

ACTION: make check-in smoother.   Perhaps a script or talking points.   Start at 12:45pm.

# Issues

## Failure to find students in directory

There was a student who during check-in could not be found.  It turned out the names for the students
were taken from the scienefair site, copied into a "program for the science fair" document, then then
cut and pasted back into the Judge app.  During this manual process, a student with a middle name got
their last name dropped.

ACTION: Only move student data directly from the sciencefair site into the judge app directly.   Do not
go through an intermediate document.

## Another student

Apparently another student had trouble or never completed signing up and showed up with cash in hand to be added to the
system.   I accomplished this by doing an insert from the mysql command prompt into the database.

ACTION: add a way to add students via the admin app (ie. w/o using mysql)


## Judging

Some issues during actual judging at the fair.

1.  Judges are randomly assigned a project from the pool of projects available to be judged.  In theory the list is
shuffled randomly (using clojure's shuffle method.)   In practice this didn't seem to work so well.   Some of the
projects didn't seem to be judged at all until the surrounding projects were completely judged.    This was pretty
awkward because students neighbors could have been fully judged without a single judge visiting the effected student.
In particular two students did not appear to get any judges until the very end,

ACTION: investigate the shuffle and/or use a weighted random process.

ACTION: Consider a judge override - which would let a judge specifically jump to a specific student.  We trust the
judges.   They can do this be constantly pressing cancel .. but the seems barbaric.

2. On the judging screen, partner names were missing.

I do not know how this got missed.   Carl said it was a problem last year too.    I took a look at the code and fixed
the proble in about 10 minutes time.    This stunk because it meant judges had to remember to ask if there were two
students.

ACTION: none - this issue is fixed.  Perhaps test more?

Judge balancing (spreading judges evenly so all grades finish in nearly the same time, could have been better.)
We waited a while for 4th grade to finish.

ACTION: Spread judges across more evenly based on the number of students per grade.   This was a little tricky
as some judges had last minute cancellations.

ACTION: For post analysis, would be nice if the judging app recorded times that judges started and stopped with
each project

ACTION: Would be nice if awards page showed that all judging was complete per grade.


## The Setup Used

I ran the application on an Apple Macbook.  I used a stock Verizon router for wireless.  Using the Verizon router
made setup really simple.   Prior to using the router, I tried to get the computers to act as hotspots.  While
possible (OSX with internet sharing, Ubuntu with some tweaks) - it was something of a unnatural act that required
a lot of fiddling.  Using the Verizon router was much easier.  I configured it to broadcast as "sciencefair"
and made sure it had no wifi password.   And that was all that needed to be done.

The laptop was nice because it was easy to bring in and out.  It is also independent of local power (just in case
there is an outage.)  It would be nice to have a second system ready to take over at anytime.

ACTION: practice keeping two machines syncronized and ready to swap back and forth should there be a hardware
issue.   Also a second wifi router... probably configured as "sciencefair2"


