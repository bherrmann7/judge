

# An Experience Report

On Saturday, March 28th, the Judge application was used by the Groton Dunstable Elementary School's Science Fair team
to judge.  More details about the fair and its information and rules are at http://gdesciencefair.org
I, Bob Herrmann, am the developer for the judge application and was responsible for ensuring it ran correctly on the
day of the fair.

# What is the Judge application



# Setup

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


# Prior the Fair

The application was made accessible to other members of the team at a private URL so that
the application could be tested and reviewed.

In preparation for the fair, the


# check-in

# judging

# problems

# for next year

# final thoughts

## judging at all

## judge scaling

## was this a good idea (ie better than paper?)

## why clojure?



