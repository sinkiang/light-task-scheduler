create index idx_lts_admin_noo_log_1 on "lts_admin_noo_log" ("log_time");
create index idx_lts_admin_noo_log_2 on "lts_admin_noo_log" ("event");
create index idx_lts_admin_noo_log_3 on "lts_admin_noo_log" ("identity");
create index idx_lts_admin_noo_log_4 on "lts_admin_noo_log" ("group");

create index idx_lts_admin_jc_md_1 on "lts_admin_jc_monitor_data" ("timestamp");
create index idx_lts_admin_jc_md_2 on "lts_admin_jc_monitor_data" ("identity");
create index idx_lts_admin_jc_md_3 on "lts_admin_jc_monitor_data" ("node_group");

create index idx_lts_admin_jt_md_1 on "lts_admin_jt_monitor_data" ("timestamp");
create index idx_lts_admin_jt_md_2 on "lts_admin_jt_monitor_data" ("identity");

create index lts_admin_jvm_gc_1 on "lts_admin_jvm_gc" ("timestamp");
create index lts_admin_jvm_gc_2 on "lts_admin_jvm_gc" ("identity");

create index lts_admin_jvm_memory_1 on "lts_admin_jvm_memory" ("timestamp");
create index lts_admin_jvm_memory_2 on "lts_admin_jvm_memory" ("identity");

create index lts_admin_jvm_thread_1 on "lts_admin_jvm_thread" ("timestamp");
create index lts_admin_jvm_thread_2 on "lts_admin_jvm_thread" ("identity");

create index idx_lts_admin_tt_md_1 on "lts_admin_tt_monitor_data" ("timestamp");
create index idx_lts_admin_tt_md_2 on "lts_admin_tt_monitor_data" ("identity");
create index idx_lts_admin_tt_md_3 on "lts_admin_tt_monitor_data" ("node_group");

create unique index udx_lts_cron_job_queue_1 on "lts_cron_job_queue" ("job_id");
create unique index udx_lts_cron_job_queue_2 on "lts_cron_job_queue" ("task_id", "task_tracker_node_group");
create index idx_lts_cron_job_queue_1 on "lts_cron_job_queue" ("real_task_id", "task_tracker_node_group");
create index idx_lts_cron_job_queue_2 on "lts_cron_job_queue" ("rely_on_prev_cycle", "last_generate_trigger_time");

-- create unique index udx_lts_executable_job_queue_1 on "lts_executable_job_queue" ("job_id");
-- create unique index udx_lts_executable_job_queue_2 on "lts_executable_job_queue" ("task_id", "task_tracker_node_group");
-- create index idx_lts_executable_job_queue_1 on "lts_executable_job_queue" ("task_tracker_identity");
-- create index idx_lts_executable_job_queue_2 on "lts_executable_job_queue" ("job_type");
-- create index idx_lts_executable_job_queue_3 on "lts_executable_job_queue" ("real_task_id", "task_tracker_node_group");
-- create index idx_lts_executable_job_queue_4 on "lts_executable_job_queue" ("priority", "trigger_time", "gmt_created");
-- create index idx_lts_executable_job_queue_5 on "lts_executable_job_queue" ("is_running");

create unique index udx_lts_executing_job_queue_1 on "lts_executing_job_queue" ("job_id");
create unique index udx_lts_executing_job_queue_2 on "lts_executing_job_queue" ("task_id", "task_tracker_node_group");
create index idx_lts_executing_job_queue_1 on "lts_executing_job_queue" ("job_type");
create index idx_lts_executing_job_queue_2 on "lts_executing_job_queue" ("real_task_id", "task_tracker_node_group");
create index idx_lts_executing_job_queue_3 on "lts_executing_job_queue" ("task_tracker_identity");
create index idx_lts_executing_job_queue_4 on "lts_executing_job_queue" ("gmt_created");

-- create index idx_lts_job_feedback_queue_1 on "lts_job_feedback_queue" ("gmt_created");

create index idx_lts_job_log_po_1 on "lts_job_log_po" ("log_time");
create index idx_lts_job_log_po_2 on "lts_job_log_po" ("task_id", "task_tracker_node_group");
create index idx_lts_job_log_po_3 on "lts_job_log_po" ("real_task_id", "task_tracker_node_group");

create unique index udx_lts_repeat_job_queue_1 on "lts_repeat_job_queue" ("job_id");
create unique index udx_lts_repeat_job_queue_2 on "lts_repeat_job_queue" ("task_id", "task_tracker_node_group");
create index idx_lts_repeat_job_queue_1 on "lts_repeat_job_queue" ("real_task_id", "task_tracker_node_group");
create index idx_lts_repeat_job_queue_2 on "lts_repeat_job_queue" ("rely_on_prev_cycle", "last_generate_trigger_time");

create unique index udx_lts_suspend_job_queue_1 on "lts_suspend_job_queue" ("job_id");
create unique index udx_lts_suspend_job_queue_2 on "lts_suspend_job_queue" ("task_id", "task_tracker_node_group");
create index idx_lts_suspend_job_queue_1 on "lts_suspend_job_queue" ("real_task_id", "task_tracker_node_group");
create index idx_lts_suspend_job_queue_2 on "lts_suspend_job_queue" ("rely_on_prev_cycle", "last_generate_trigger_time");