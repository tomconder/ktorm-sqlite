drop table if exists t_department;
drop table if exists t_employee;

create table t_department(
  id        integer primary key autoincrement,
  name      text    not null,
  location  text    not null
);

create table t_employee(
  id            integer primary key autoincrement,
  name          text    not null,
  job           text    not null,
  manager_id    integer null,
  hire_date     integer not null,
  salary        integer not null,
  department_id integer not null
);

insert into t_department(name, location) values ('Tech', 'Memphis');
insert into t_department(name, location) values ('Finance', 'Dallas');

insert into t_employee(name, job, manager_id, hire_date, salary, department_id)
values ('vince', 'engineer', null, 1514736000000, 100, 1);
insert into t_employee(name, job, manager_id, hire_date, salary, department_id)
values ('mary', 'trainee', 1, 1546272000000, 50, 1);

insert into t_employee(name, job, manager_id, hire_date, salary, department_id)
values ('tom', 'director', null, 1514736000000, 200, 2);
insert into t_employee(name, job, manager_id, hire_date, salary, department_id)
values ('penny', 'assistant', 3, 1546272000000, 100, 2);
