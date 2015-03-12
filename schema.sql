
drop database judge;
create database judge;
use judge;

create table judges (name char(100), grade char(1));

insert into judges values ('Alpha Smith', '1');
insert into judges values ('Beta Jones', '2');
insert into judges values ('Delta Smith', '3');
insert into judges values ('Epsilon Smith', '4');

insert into judges values ('K One', 'K');
insert into judges values ('K Two', 'K');
insert into judges values ('K Three', 'K');

create table students ( name char(100), table_assignment char(10), grade char(1), position integer, being_judged_by char(100), checked_in DATETIME );

insert into students values ('Tim Meyers',     'A', '3', 1, null, null);
insert into students values ('Tim Thomas',     'A', '3', 2, null, null);
insert into students values ('Tim Smith',      'A', '3', 3, null, null);

insert into students values ('Dana Thomas',    'B', '2', 3, null, null);
insert into students values ('Dana Smith',     'B', '2', 4, null, null);
insert into students values ('Dana Meyers'   , 'B', '2', 5, null, null);

insert into students values ('William Smith',  'J', '2', 6, null, null);
insert into students values ('William Thomas', 'J', '2', 5, null, null);
insert into students values ('William Meyers', 'J', '2', 4, null, null);

insert into students values ('Katie ABC',  'J', 'K', 1, null, null);
insert into students values ('Keith ABC', 'J', 'K', 2, null, null);


create table judgements (
   student char(100), judge char(100), criteria_name char(100), score integer
   );

create table summary (
   student char(100), judge char(100), score integer
   );
