# --- !Ups

create table employee (
  id                        varchar(255) not null,
  employee_id               varchar(255), //not null,
  first_name                varchar(255),
  last_name                 varchar(255) not null,
  full_name                 varchar(255),
  nick_name                 varchar(255),
  email_id                  varchar(255),
  job_title                 varchar(255) not null,
  desk_location             varchar(255),
  manager_id                varchar(255), // not null,
  address                   varchar(255),
  city                      varchar(255),
  country                   varchar(255),
  mobile                    varchar(255) not null,
  constraint pk_employee primary key (id))
;


# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table employee;

SET FOREIGN_KEY_CHECKS=1;

