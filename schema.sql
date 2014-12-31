
create table judges (name char(100), grade char(1));

insert into judges values ('Crystal H', '1');
insert into judges values ('Max H', '2');
insert into judges values ('Liam H', '3');
insert into judges values ('Stan K', '4');
insert into judges values ('Peter S', 'K');

create table students ( name char(100), table_assignment char(10), grade char(1), position integer);

insert into students values ('Tim Meyers', 'A', '3', 3);
insert into students values ('Dana Meyers', 'B', '7', 6);
insert into students values ('William Meyers', 'J', '2', 1);
insert into students values ('Tim Thomas', 'A', '3', 3);
insert into students values ('Dana Thomas', 'B', '7', 6);
insert into students values ('William Thomas', 'J', '2', 1);
insert into students values ('Tim Smith', 'A', '3', 3);
insert into students values ('Dana Smith', 'B', '7', 6);
insert into students values ('William Smith', 'J', '2', 1);

create table judgements ( student char(100), judge char(100),
    problem integer,
    research integer,
    hypothesis integer,
    experiment integer,
    observations integer
)

