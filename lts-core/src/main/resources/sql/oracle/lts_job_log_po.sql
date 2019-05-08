CREATE TABLE "lts_job_log_po" (
"id" NUMBER(20) NOT NULL PRIMARY KEY,
"gmt_created" NUMBER(20) NULL ,
"log_time" NUMBER(20) NULL ,
"log_type" NVARCHAR2(32) NULL ,
"success" NUMBER(4) NULL ,
"msg" NCLOB NULL ,
"code" NVARCHAR2(32) NULL ,
"job_type" NVARCHAR2(32) NULL ,
"task_tracker_identity" NVARCHAR2(64) NULL ,
"level" NVARCHAR2(32) NULL ,
"task_id" NVARCHAR2(64) NULL ,
"real_task_id" NVARCHAR2(64) NULL ,
"job_id" NVARCHAR2(64) NULL ,
"priority" NUMBER(11) NULL ,
"submit_node_group" NVARCHAR2(64) NULL ,
"task_tracker_node_group" NVARCHAR2(64) NULL ,
"ext_params" NCLOB NULL ,
"internal_ext_params" NCLOB NULL ,
"need_feedback" NUMBER(4) NULL ,
"cron_expression" NVARCHAR2(128) NULL ,
"trigger_time" NUMBER(20) NULL ,
"retry_times" NUMBER(11) NULL ,
"max_retry_times" NUMBER(11) NULL ,
"rely_on_prev_cycle" NUMBER(4) NULL ,
"repeat_count" NUMBER(11) NULL ,
"repeated_count" NUMBER(11) NULL ,
"repeat_interval" NUMBER(20) NULL 
)
