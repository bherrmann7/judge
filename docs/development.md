
# What kind of code is Judge?

Judge is a web application written in clojure.   It uses [mysql](http://mysql.com) as its data store.  Judge has
been developed on OSX and Ubuntu.

Mysql was selected because it is fairly developer friendly.   Very robust.  Easy to install.

Clojure was selected primarily because the primary developer, Bob Herrmann, believes its an interesting platform
for developing new web applications.

# Getting the application running on your machine

## Create Database Using

Install mysql  - technique varies per OS
On ubuntu
   $  sudo apt-get install mysql-server mysql-client

Creating the application schema

   mysql> create user 'judge'@'localhost' IDENTIFIED BY 'buttercup';
   Query OK, 0 rows affected (0.01 sec)

   mysql> grant all PRIVILEGES on judge.* to 'judge'@'localhost';
   Query OK, 0 rows affected (0.00 sec)


   $  mysql -ujudge -pbuttercup < schema.sql

## Compile/Run

Install Java

Download Leiningen from http://leiningen.org/

To start a web server for the application, run:

    $ lein ring server

