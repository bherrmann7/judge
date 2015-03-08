
drop database judge;
create database judge;
use judge;

create table judges (name char(100), grade char(1));

insert into judges values ('Alpha Smith', '1');
insert into judges values ('Beta Jones', '2');
insert into judges values ('Delta Smith', '3');
insert into judges values ('Epsilon Smith', '4');
insert into judges values ('Gamma Jones', 'K');

create table students ( name char(100), table_assignment char(10), grade char(1), position integer);

insert into students values ('Tim Meyers',     'A', '3', 1);
insert into students values ('Tim Thomas',     'A', '3', 2);
insert into students values ('Tim Smith',      'A', '3', 3);

insert into students values ('Dana Thomas',    'B', '2', 3);
insert into students values ('Dana Smith',     'B', '2', 4);
insert into students values ('Dana Meyers'   , 'B', '2', 5);

insert into students values ('William Smith',  'J', '2', 6);
insert into students values ('William Thomas', 'J', '2', 5);
insert into students values ('William Meyers', 'J', '2', 4);

create table judgements (
   student char(100), judge char(100), criteria_name char(100), score integer
   );

create table summary (
   student char(100), judge char(100), score integer
   );
