
drop database judge;
create database judge;
use judge;

create table judges (name char(100), grade char(1));

insert into judges values ('oneK', 'K');
insert into judges values ('twoK', 'K');
insert into judges values ('threeK', 'K');
insert into judges values ('fourK', 'K');
insert into judges values ('fiveK', 'K');
insert into judges values ('sixK', 'K');
insert into judges values ('one1', '1');
insert into judges values ('two1', '1');
insert into judges values ('three1', '1');
insert into judges values ('four1', '1');
insert into judges values ('five1', '1');
insert into judges values ('six1', '1');
insert into judges values ('one2', '2');
insert into judges values ('two2', '2');
insert into judges values ('three2', '2');
insert into judges values ('four2', '2');
insert into judges values ('five2', '2');
insert into judges values ('six2', '2');
insert into judges values ('one3', '3');
insert into judges values ('two3', '3');
insert into judges values ('three3', '3');
insert into judges values ('four3', '3');
insert into judges values ('five3', '3');
insert into judges values ('six3', '3');
insert into judges values ('one4', '4');
insert into judges values ('two4', '4');
insert into judges values ('three4', '4');
insert into judges values ('four4', '4');
insert into judges values ('five4', '4');
insert into judges values ('six4', '4');

create table students ( name char(100), table_assignment char(10), grade char(1), position integer, being_judged_by char(100), checked_in DATETIME , partner char(100) );

-- insert into students values ('Tim Meyers',     'A', '3', 1, null, null);

create table judgements (
   student char(100), judge char(100), criteria_name char(100), score float
   );

create table summary (
   student char(100), judge char(100), score float
   );
