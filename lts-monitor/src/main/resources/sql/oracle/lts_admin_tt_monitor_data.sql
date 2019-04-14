CREATE TABLE "lts_admin_tt_monitor_data" (
"id" NUMBER(11) NOT NULL PRIMARY KEY,
"gmt_created" NUMBER(20) NULL ,
"node_group" NVARCHAR2(64) NULL ,
"identity" NVARCHAR2(64) NULL ,
"exe_success_num" NUMBER(20) NULL,
"exe_failed_num" NUMBER(11) NULL,
"exe_later_num" NUMBER(20) NULL,
"exe_exception_num" NUMBER(20) NULL,
"total_running_time" NUMBER(20) NULL,
"timestamp" NUMBER(20) NULL
)