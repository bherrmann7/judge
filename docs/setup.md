
# Setup

## Create Database Using

Install mysql

Creating the application schema

    $ mysql -u dataBaseUser -p databasePassword < schema.sql

## Running

Install Java 1.8

Download Leiningen from http://leiningen.org/

To start a web server for the application, run:

    $ lein ring server
