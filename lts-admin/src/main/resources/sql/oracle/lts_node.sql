CREATE TABLE "lts_node" (
    "identity" NVARCHAR2(64) NOT NULL PRIMARY KEY,
    "available" NUMBER(4) NULL,
    "cluster_name" NVARCHAR2(64) NULL,
    "node_type" NVARCHAR2(64) NULL,
    "ip" NVARCHAR2(16) NULL,
    "port" NUMBER(11) NULL,
    "node_group" NVARCHAR2(64) NULL,
    "create_time" NUMBER(20) NULL,
    "threads" NUMBER(11) NULL,
    "host_name" NVARCHAR2(64) NULL,
    "http_cmd_port" NUMBER(11) NULL
)
