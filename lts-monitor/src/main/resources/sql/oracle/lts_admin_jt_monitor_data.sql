CREATE TABLE "lts_admin_jt_monitor_data" (
"id" NUMBER(11) NOT NULL PRIMARY KEY,
"gmt_created" NUMBER(20) NULL ,
"identity" NVARCHAR2(64) NULL ,
"receive_job_num" NUMBER(20) NULL,
"push_job_num" NUMBER(20) NULL,
"exe_success_num" NUMBER(20) NULL,
"exe_failed_num" NUMBER(11) NULL,
"exe_later_num" NUMBER(20) NULL,
"exe_exception_num" NUMBER(20) NULL,
"fix_executing_job_num" NUMBER(20) NULL,
"timestamp" NUMBER(20) NULL
)