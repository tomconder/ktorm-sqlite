-- teardown runs with foreign keys off; a half-built database from a failed
-- run can still be dropped. Re-enabled before the inserts, which stay fully checked
pragma foreign_keys = off;
drop table if exists t_employee;
drop table if exists t_department;
pragma foreign_keys = on;

create table t_department(
  id        integer primary key autoincrement,
  name      text    not null,
  location  text    not null
);

create table t_employee(
  id            integer primary key autoincrement,
  name          text    not null,
  job           text    not null,
  hire_date     text    not null,
  salary        integer not null,
  department_id integer not null references t_department(id) on delete cascade
);

insert into t_department(name, location) values ('Tech', 'Memphis');
insert into t_department(name, location) values ('Finance', 'Dallas');
insert into t_department(name, location) values ('Sales', 'Tampa');

insert into t_employee(name, job, hire_date, salary, department_id)
values ('Vince', 'Engineer', '2021-01-02', 100, 1);
insert into t_employee(name, job, hire_date, salary, department_id)
values ('Sandy', 'Trainee', '2022-03-04', 50, 1);

insert into t_employee(name, job, hire_date, salary, department_id)
values ('Tom', 'Director', '2023-05-06', 400, 2);
insert into t_employee(name, job, hire_date, salary, department_id)
values ('Penny', 'Assistant', '2024-06-07', 100, 2);

insert into t_employee(name, job, hire_date, salary, department_id)
values ('Peggy', 'Sales Rep', '2026-07-08', 100, 3);
