-- alter session set "_ORACLE_SCRIPT"=true; THIS IS NOT REQUIRED for Oracle 11c, but REQUIRED for Oracle 19+

CREATE TABLESPACE tbs_perm_01 DATAFILE 'tbs_kc_perm_01.dbf' SIZE 512M;
CREATE TEMPORARY TABLESPACE tbs_temp_02 TEMPFILE 'tbs_kc_temp_01.dbf' SIZE 128M;

CREATE USER KNOWLEDGE_CENTRE
  IDENTIFIED BY KNOWLEDGE_PASSWORD
  DEFAULT TABLESPACE tbs_kc_perm_01
--  QUOTA 128M on tbs_kc_perm_01
  TEMPORARY TABLESPACE tbs_kc_temp_01;
--  QUOTA 32M on tbs_kc_temp_01;

GRANT create session TO KNOWLEDGE_CENTRE;
GRANT create table TO KNOWLEDGE_CENTRE;
GRANT create view TO KNOWLEDGE_CENTRE;
GRANT create any trigger TO KNOWLEDGE_CENTRE;
GRANT create any procedure TO KNOWLEDGE_CENTRE;
GRANT create sequence TO KNOWLEDGE_CENTRE;
GRANT create synonym TO KNOWLEDGE_CENTRE;

ALTER USER KNOWLEDGE_CENTRE quota 512M on tbs_kc_perm_01; -- ORA-01950

COMMIT;

select username,account_status from dba_users where login = 'KNOWLEDGE_CENTRE';
alter user KNOWLEDGE_CENTRE account unlock;
COMMIT;