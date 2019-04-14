CREATE TABLE "{tableName}" (
"id" NUMBER(11) NOT NULL PRIMARY KEY,
"log_time" NUMBER(20) NULL ,
"event" NVARCHAR2(32) NULL ,
"node_type" NVARCHAR2(16) NULL ,
"cluster_name" NVARCHAR2(64) NULL ,
"ip" NVARCHAR2(16) NULL ,
"port" NUMBER(11) NULL ,
"host_name" NVARCHAR2(64) NULL ,
"group" NVARCHAR2(64) NULL ,
"create_time" NUMBER(20) NULL ,
"threads" NUMBER(11) NULL ,
"identity" NVARCHAR2(64) NULL ,
"http_cmd_port" NUMBER(11) NULL 
)