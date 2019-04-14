CREATE TABLE "lts_admin_jc_monitor_data" (
"id" NUMBER(11) NOT NULL PRIMARY KEY,
"gmt_created" NUMBER(20) NULL ,
"node_group" NVARCHAR2(64) NULL ,
"identity" NVARCHAR2(64) NULL ,
"submit_success_num" NUMBER(20)  NULL,
"submit_failed_num" NUMBER(11) NULL,
"fail_store_num" NUMBER(20) NULL,
"submit_fail_store_num" NUMBER(20) NULL,
"handle_feedback_num" NUMBER(20) NULL,
"timestamp" NUMBER(20) NULL
)