
-- name: all-judges
select * from (SELECT j.*, s.name judging FROM judges j left join students s on j.name = s.being_judged_by) x group by name, grade;

-- name: all-students
SELECT * FROM students;

-- name: all-summary
SELECT
    students.name,
    COUNT(summary.student)  judged,
    group_concat(judge) judged_by,
    students.being_judged_by
FROM
    students
LEFT JOIN
    summary
ON
    students.name = summary.student
GROUP BY
    students.name
ORDER BY
    judged;


-- name: you-judged
select count(*) as judged from summary where judge = :judge;

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
         summary j
      ON
         s.name = j.student
      WHERE
         s.grade = (select grade from judges where name = :judge)
         AND
             s.being_judged_by is null
      GROUP BY
         name) x
WHERE
   jmnts<3
AND name NOT IN
   (
      SELECT
         student
      FROM
         summary
      WHERE
         judge = :judge);


-- name: judge-summary
select 'Judges' as name, count(*) as count,
 sum(case when grade='K' then 1 else 0 end) K,
 sum(case when grade='1' then 1 else 0 end) First,
 sum(case when grade='2' then 1 else 0 end) Second,
 sum(case when grade='3' then 1 else 0 end) Third,
 sum(case when grade='4' then 1 else 0 end) Fourth
from judges;

-- name: student-summary
select 'Students' as name, count(*) as count,
       sum(case when grade='K' then 1 else 0 end) K,
       sum(case when grade='1' then 1 else 0 end) First,
       sum(case when grade='2' then 1 else 0 end) Second,
       sum(case when grade='3' then 1 else 0 end) Third,
       sum(case when grade='4' then 1 else 0 end) Fourth
from students;

-- name: judgement-summary
select 'Judgements' as name, count(*) as count,
       sum(case when grade='K' then 1 else 0 end) K,
       sum(case when grade='1' then 1 else 0 end) First,
       sum(case when grade='2' then 1 else 0 end) Second,
       sum(case when grade='3' then 1 else 0 end) Third,
       sum(case when grade='4' then 1 else 0 end) Fourth
from summary join students on summary.student = students.name;

-- name: insert-score!
insert into judgements values (:student, :judge, :criteria_name, :score);

-- name: insert-summary!
insert into summary values ( :student, :judge, :score);

-- name: get-student-by-name
select * from students where name = :name;

-- name: assign-judge-to-student!
update students set being_judged_by=:judge where name=:student;

-- name: judge-stats-summary
select grade, count(cnts.name) total_students,
    sum(case when checked_in IS NOT null then 1 else 0 end) checked_in,
    sum(case when judged = 0 then 1 else 0 end) judged0,
    sum(case when judged = 1 then 1 else 0 end) judged1,
    sum(case when judged = 2 then 1 else 0 end) judged2,
    sum(case when judged = 3 then 1 else 0 end) judged3
 from (select stu.name, stu.checked_in, count(summary.student) judged, stu.grade from students stu left join
   summary on summary.student = stu.name where stu.grade =
    (select grade from judges where name = :judge_name ) group by stu.name order by judged) cnts;
