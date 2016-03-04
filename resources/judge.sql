
-- name: all-judges
select * from (SELECT j.*, s.name judging FROM judges j left join students s on j.name = s.being_judged_by) x group by name, grade;

-- name: all-students
SELECT * FROM students;

-- name: all-summary
SELECT
    case when students.partner = ''  then
        students.name
    else
        concat(students.name, ' / ' , students.partner)
    end name,
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

-- name: all-students-floor
SELECT
    students.*,
    COUNT(summary.student)  judged
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


-- name: you-judged-count
select count(*) as judged from summary where judge = :judge;

-- name: you-judged
select student, score from summary where judge = :judge;


-- name: who-can-i-judge
-- names of students who can I can judge next :judge judge
SELECT
   name,
   table_assignment,
   grade,
   position,
   judgements
FROM
   (
      SELECT
         *,
         COUNT(student) judgements
      FROM
         students s
      LEFT JOIN
         summary j
      ON
         s.name = j.student
      WHERE
         s.grade = (select grade from judges where name = :judge )
         AND
            s.being_judged_by is null
         AND
            checked_in is not null
         AND
            checked_in < (NOW()-INTERVAL 20 MINUTE)
      GROUP BY
         name) x
WHERE
   judgements<3
AND name NOT IN
   (
      SELECT
         student
      FROM
         summary
      WHERE
         judge = :judge )
and (
      ((select count(*) from summary where judge = :judge ) = 0 and  x.had_newbie_judge = 0  )
             or
       (select count(*) from summary where judge = :judge ) > 0
      );

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

-- name: update-summary!
update summary set score=:score where student=:student and judge=:judge;

-- name: get-student-by-name
select s.*, count(j.student) judgements from students s left outer join summary j on s.name = j.student where name = :name

-- name: assign-judge-to-student!
update students set being_judged_by=:judge where name=:student;

-- name: unassign-judge-from-any-students!
update students set being_judged_by=null, had_newbie_judge =
    case when (select count(*) from summary where judge = being_judged_by ) = 0 then 1 else 0 end  where being_judged_by=:judge;

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

-- name: delete-from-students!
delete from students;

-- name: delete-from-judges!
delete from judges;

-- name: delete-from-summary!
delete from summary;

-- name: delete-from-judgements!
delete from judgements;


-- name: insert-student!
insert into students values (:name, :table_assignment, :grade, :position, null, null, :partner, false);

-- name: insert-judge!
insert into judges values (:name, :grade);

-- name: students-not-checked-in
select name, case when students.partner = ''  then
     students.name
   else
     concat(students.name, ' / ' , students.partner)
   end team,
   students.grade
    from students where checked_in is null order by name;

-- name: checkin-student!
update students set checked_in = now() where name = :name;

-- name: get-hightest
(select name, partner, grade, sum(score) total, count(score) judge_counts from summary sm join students st on st.name = sm.student where grade = '4' group by name order by total desc limit 5)
union
(select name, partner, grade, sum(score) total, count(score) from summary sm join students st on st.name = sm.student where grade = '3' group by name order by total desc limit 5)
union
(select name, partner, grade, sum(score) total, count(score) from summary sm join students st on st.name = sm.student where grade = '2' group by name order by total desc limit 5)
union
(select name, partner, grade, sum(score) total, count(score) from summary sm join students st on st.name = sm.student where grade = '1' group by name order by total desc limit 5)
union
(select name, partner, grade, sum(score) total, count(score) from summary sm join students st on st.name = sm.student where grade = 'K' group by name order by total desc limit 5)
;

