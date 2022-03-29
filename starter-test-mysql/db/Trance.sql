SELECT es_transaction_execution.transaction_execution_uid                       AS transaction_execution_uid,
       es_transaction_execution.executor_type                                   AS transaction_execution_executor_type,
       es_transaction_execution.execution_mode                                  AS execution_mode,
       es_transaction_execution.final_status                                    AS final_status,
       es_transaction_execution.flat_order                                      AS flat_order,
       es_transaction_execution.has_errors                                      AS has_errors,
       es_executor.executor_class                                               AS executor_class,
       es_executor.executor_type                                                AS executor_type,
       es_transaction_revert_sub_execution.sub_order                            AS sub_order,
       es_transaction_process_execution.core_order                              AS core_order,
       es_transaction_process_execution.transaction_process_execution_uid       AS transaction_process_execution_uid,
       sub_revert_execution.transaction_process_execution_uid                   AS sub_revert_execution_transaction_process_execution_uid,
       es_transaction_execution_tryout.start_datetime                           AS last_tryout_start_datetime,
       es_transaction_execution_tryout.end_datetime                             AS last_tryout_end_datetime,
       es_transaction_execution_tryout.`status`                                 AS last_tryout_status,
       es_transaction_execution_tryout.transaction_execution_tryout_uid         AS transaction_execution_tryout_uid,
       es_transaction_revert_sub_execution.transaction_revert_sub_execution_uid as transaction_revert_sub_execution_uid,
       try_outs.tryout_count,
       parent_transaction_revert_execution.transaction_revert_execution_uid,
       parent_execuor.executor_class
FROM es_transaction_execution
         INNER JOIN es_transaction ON es_transaction_execution.transaction_uid = es_transaction.transaction_uid
         INNER JOIN es_executor_aggregator_version ON es_transaction_execution.executor_aggregator_version_uid =
                                                      es_executor_aggregator_version.executor_aggregator_version_uid
         INNER JOIN es_executor ON es_executor_aggregator_version.executor_uid = es_executor.executor_uid
         LEFT JOIN es_transaction_revert_sub_execution ON es_transaction_execution.transaction_execution_uid =
                                                          es_transaction_revert_sub_execution.transaction_execution_uid
         LEFT JOIN es_transaction_process_execution ON es_transaction_execution.transaction_execution_uid =
                                                       es_transaction_process_execution.transaction_execution_uid
         LEFT JOIN es_transaction_revert_execution AS sub_revert_execution
                   ON es_transaction_revert_sub_execution.transaction_revert_execution_uid =
                      sub_revert_execution.transaction_revert_execution_uid
         LEFT JOIN es_transaction_execution_tryout ON es_transaction_execution.transaction_execution_uid =
                                                      es_transaction_execution_tryout.transaction_execution_uid
         INNER JOIN
     (
         SELECT MAX(es_transaction_execution_tryout.start_datetime)                     AS start_datetime,
                es_transaction_execution.transaction_execution_uid,
                COUNT(es_transaction_execution_tryout.transaction_execution_tryout_uid) AS tryout_count
         FROM es_transaction_execution_tryout
                  INNER JOIN es_transaction_execution ON es_transaction_execution_tryout.transaction_execution_uid =
                                                         es_transaction_execution.transaction_execution_uid
                  INNER JOIN es_transaction ON es_transaction_execution.transaction_uid = es_transaction.transaction_uid
         WHERE es_transaction.revert_access_token = '25aefdb2-1b88-4629-aa08-b603c5ed25b2'
         GROUP BY es_transaction_execution.transaction_execution_uid
     )
         AS try_outs ON es_transaction_execution_tryout.start_datetime = try_outs.start_datetime
         AND es_transaction_execution_tryout.transaction_execution_uid = try_outs.transaction_execution_uid

         LEFT JOIN es_transaction_revert_execution AS parent_transaction_revert_execution
                   ON es_transaction_revert_sub_execution.transaction_revert_execution_uid =
                      parent_transaction_revert_execution.transaction_revert_execution_uid
         LEFT JOIN es_transaction_process_execution AS parent_transaction_process_execution
                   ON parent_transaction_revert_execution.transaction_process_execution_uid =
                      parent_transaction_process_execution.transaction_process_execution_uid
         LEFT JOIN es_transaction_execution AS parent_transaction_execution
                   ON parent_transaction_process_execution.transaction_execution_uid =
                      parent_transaction_execution.transaction_execution_uid
         LEFT JOIN es_executor_aggregator_version AS parent_executor_aggregator_version
                   ON parent_transaction_execution.executor_aggregator_version_uid =
                      parent_executor_aggregator_version.executor_aggregator_version_uid
         LEFT JOIN es_executor AS parent_execuor
                   ON parent_executor_aggregator_version.executor_uid = parent_execuor.executor_uid
WHERE es_transaction.revert_access_token = '25aefdb2-1b88-4629-aa08-b603c5ed25b2'
ORDER BY es_transaction_process_execution.core_order IS NULL ASC,
         es_transaction_process_execution.core_order ASC