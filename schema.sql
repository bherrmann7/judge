
drop database judge;
create database judge;
use judge;

create table judges (name char(100), grade char(1));

create table students ( name char(100), table_assignment char(10), grade char(1), position integer, being_judged_by char(100), checked_in DATETIME,
 partner char(100), had_newbie_judge bool);

create table judgements (
   student char(100), judge char(100), criteria_name char(100), score float
   );

create table summary (
   student char(100), judge char(100), score float
   );
