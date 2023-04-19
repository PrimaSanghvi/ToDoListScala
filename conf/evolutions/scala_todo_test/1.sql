# Tasks schema

# --- !Ups

CREATE SEQUENCE task_id_seq;
CREATE TABLE task (
    id integer NOT NULL DEFAULT nextval('task_id_seq'),
    name varchar(255) NOT NULL,
    comments text,
    completed boolean NOT NULL DEFAULT false
);

# --- !Downs

DROP TABLE task;
DROP SEQUENCE task_id_seq;
