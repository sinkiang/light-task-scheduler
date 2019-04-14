CREATE TABLE "lts_admin_jvm_thread" (
"id" NUMBER(11) NOT NULL PRIMARY KEY,
"gmt_created" NUMBER(20) NULL ,
"identity" NVARCHAR2(64) NULL ,
"timestamp" NUMBER(20) NULL ,
"node_type" NVARCHAR2(32) NULL ,
"node_group" NVARCHAR2(64) NULL ,
"daemon_thread_count" NUMBER(11) NULL ,
"thread_count" NUMBER(11) NULL ,
"total_started_thread_count" NUMBER(20) NULL ,
"dead_locked_thread_count" NUMBER(11) NULL ,
"process_cpu_time_rate" NUMBER NULL 
)