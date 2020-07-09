alter session set "_ORACLE_SCRIPT"=true;

CREATE TABLESPACE tbs_perm_01 DATAFILE 'tbs_perm_01.dbf' SIZE 256M;
CREATE TEMPORARY TABLESPACE tbs_temp_02 TEMPFILE 'tbs_temp_02.dbf' SIZE 64M;

CREATE USER spring_user
  IDENTIFIED BY spring_password
  DEFAULT TABLESPACE tbs_perm_01
--  QUOTA 128M on tbs_perm_01
  TEMPORARY TABLESPACE tbs_temp_02;
--  QUOTA 32M on tbs_temp_02;

GRANT create session TO spring_user;
GRANT create table TO spring_user;
GRANT create view TO spring_user;
GRANT create any trigger TO spring_user;
GRANT create any procedure TO spring_user;
GRANT create sequence TO spring_user;
GRANT create synonym TO spring_user;

ALTER USER spring_user quota 256M on tbs_perm_01; -- ORA-01950

COMMIT;

select username,account_status from dba_users;
alter user spring_user account unlock;
COMMIT;


