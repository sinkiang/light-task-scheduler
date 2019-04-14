CREATE TABLE "lts_admin_jvm_gc" (
"id" NUMBER(11) NOT NULL PRIMARY KEY,
"gmt_created" NUMBER(20) NULL ,
"identity" NVARCHAR2(64) NULL ,
"timestamp" NUMBER(20) NULL ,
"node_type" NVARCHAR2(32) NULL ,
"node_group" NVARCHAR2(64) NULL ,
"young_gc_collection_count" NUMBER(20) NULL ,
"young_gc_collection_time" NUMBER(20) NULL ,
"full_gc_collection_count" NUMBER(20) NULL ,
"full_gc_collection_time" NUMBER(20) NULL ,
"span_young_gc_collection_count" NUMBER(20) NULL ,
"span_young_gc_collection_time" NUMBER(20) NULL ,
"span_full_gc_collection_count" NUMBER(20) NULL ,
"span_full_gc_collection_time" NUMBER(20) NULL 
)