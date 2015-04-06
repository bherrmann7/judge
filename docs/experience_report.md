

# An Experience Report

On Saturday, March 28th, the Judge application was used by the Groton Dunstable Elementary School's Science Fair team
to judge.  More details about the fair and its information and rules are at http://gdesciencefair.org
I, Bob Herrmann, am the developer for the judge application and was responsible for ensuring it ran correctly on the
day of the fair.


## The Setup Used

I ran the application on an Ubuntu 14.04 desktop machine (from system76.com.)  The Ubuntu served as a wifi
infrastructure mode hotspot using a "Panda 300Mbps Wireless-N USB Adapter - 802.11 n, 2.4GHz - w/ High Gain Antenna."
I set it up using instructions at http://ubuntuhandbook.org/index.php/2014/09/3-ways-create-wifi-hotspot-ubuntu/
I set up the wifi hotspot name as "sciencefair."   The network was setup like this as we presumed that we couldnt
count on any other networking facility (aka the schools shuts its wifi off on the weekends.)   Also owning this
means we didnt have to worry about the "internet" being slow during the fair.

The judge application is a web application.  Once devices are connected to the "sciencefair" network, they could then
go to their browser and then enter the IP address of the application (10.42.0.1 in this case.)

Choosing to do a web application meant that Android phones/tablets and iphone/ipads could easily be used.

I had modifed the rc.local to start up the application on boot and direct traffic on port 80 to port 3000,

    :::: last lines of /etc/rc.local
    /sbin/iptables -t nat -I PREROUTING -p tcp --destination-port 80 -j REDIRECT --to-ports 300
    /sbin/iptables -t nat -A OUTPUT -p tcp -d 10.42.0.1 --dport 80 -j  REDIRECT --to-port 3000
    sudo nohup su - bob /home/bob/judge/start.sh </dev/null &

This way, I could just plug the box in and let it boot and run, and then use a laptop to access the machine.

I would have preferred to use a laptop as the primary machine, but I only had access to a macbook and getting an
infrastructure hotspot working on it proved problematic.    Using a desktop machine instead causes one to be concerned
about possible power outages and/or cords getting kicked out.


## Training - Prior the Fair

The application was made accessible to other members of the team at a private URL so that
the application could be tested and reviewed.

In preparation for the fair, there were two training sessions for judges.   The fair used over 20 judges for around 80
or so projects.   During training the judges connected to the judge application and scored a project.   This let
them familurize with the application.  There were only two steps, 1 join the "sciencefair" wifi network, and then
navigate to 10.42.0.1

Feeback:
This seemed to go well and their were no problems with using the application.

## check-in

Students lined up to check in when the arrived.  A table with a person running a laptop, and two other staff members
checked students in.   The laptop person would select the students name from a list, then click the check in button to
check them in.   The recorded their arrival time in the system.   A judge would not be able to judge a student until
they had been in the system for at least 20 minutes.

Feedback:  I was the one looking up students.   This felt very awkward, trying to find the student by name was tricky
because sometimes they would offer their last name (the list was sorted by first name.)   The list could also have been
broken down by grades, that would have speeded things up too.

## Judging

Judging went pretty smoothly.   There was a student who believes they were only judged once.   Perhaps a judge went
to the wrong location?

There was a judge who couldnt post a score.  The judge application showed the error page with the red text asking
the judge to seek out tech support.   I fiddled with the trying to re-submit - and kept getting the red error page.
The red error page is supposed to prevent multiple submissions on the scoring screen.   I'm not sure why there was
a problem with this single iphone user.


## was this a good idea (ie better than paper?)

Two of the returning judges said this was better than the paper system. All feedback about the judge application
was positive.



