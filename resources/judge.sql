

-- name: judges-query
SELECT * FROM judges

---- name: add-challenge!
--insert into challenges( image_name, name, gender )
--values ( :image_name, :name, "M" )
--
---- name: delete-challenges!
--delete from challenges
--
---- name: insert-outcome!
--insert into outcomes ( user, round, image_name, answer, correct, moment )
--values ( :user, :round, :image_name, :answer, :correct, :moment )
--
---- name: get-current-round-outcomes
--select * from outcomes where round = (select max(round) from outcomes where user = :user)
--

-- name: you-judged
select count(*) as judged from summary where judge = :judge

-- name: who-can-i-judge
-- names of students who can I can judge next :judge judge
SELECT
   name,
   table_assignment,
   grade,
   position
FROM
   (
      SELECT
         *,
         COUNT(student) jmnts
      FROM
         students s
      LEFT JOIN
         judgements j
      ON
         s.name = j.student
      WHERE
         s.grade = (select grade from judges where name = :judge)

      GROUP BY
         name) x
WHERE
   jmnts<3
AND name NOT IN
   (
      SELECT
         student
      FROM
         judgements
      WHERE
         judge = :judge)


-- name: admin-summary
select count(*),
 sum(case when grade='K' then 1 else 0 end) K,
 sum(case when grade='1' then 1 else 0 end) First,
 sum(case when grade='2' then 1 else 0 end) Second,
 sum(case when grade='3' then 1 else 0 end) Third,
 sum(case when grade='4' then 1 else 0 end) Fourth
from judges

-- select 'judges' name, count(*) count from judges union select 'judgements', count(*) from judgements union select 'students', count(*) from students

-- name: insert-score!
insert into judgements values (  :student, :judge, :criteria_name, :score)

-- name: insert-summary!
insert into summary values ( :student, :judge, :score)


-- name: get-student-by-name
select * from students where name = :name


