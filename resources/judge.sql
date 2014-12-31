

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

-- name: insert-judgements!
insert into judgements values (:student, :judge, :problem, :research, :hypothesis, :experiment, :observations )

-- name: you-judged
select count(*) as judged from judgements where judge = :judge

-- name: who-can-i-judge
-- names of students who can I can judge next :judge judge
select name from
   (select name,count(student) jmnts from students s left join judgements j on s.name = j.student where grade = 1 group by name) x
    where jmnts<3 and name not in (select student from judgements where judge = :judge)

