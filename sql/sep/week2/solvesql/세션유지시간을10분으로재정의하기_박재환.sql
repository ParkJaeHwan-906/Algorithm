WITH USER_EVENT AS (SELECT user_pseudo_id,
                           event_name,
                           event_timestamp_kst,
                           ga_session_id,
                           LAG(event_timestamp_kst) OVER (
        ORDER BY event_timestamp_kst
      ) AS prev_event_time
                    FROM ga
                    WHERE user_pseudo_id = 'a8Xu9GO6TB'),
     GROUP_RESET AS (SELECT user_pseudo_id,
                            event_name,
                            event_timestamp_kst,
                            ga_session_id,
                            CASE
                                WHEN prev_event_time IS NULL
                                    OR TIMESTAMPDIFF(
                                           MINUTE, prev_event_time,
                                                   event_timestamp_kst
                                       ) > 10
                                    THEN 1
                                ELSE 0
                                END AS new_group
                     FROM USER_EVENT),
     REGROUPING AS (SELECT user_pseudo_id,
                           event_timestamp_kst,
                           event_name,
                           ga_session_id,
                           SUM(new_group) OVER (
        ORDER BY event_timestamp_kst
      ) AS new_session_id
                    FROM GROUP_RESET)
SELECT user_pseudo_id,
       event_timestamp_kst,
       event_name,
       ga_session_id,
       new_session_id
FROM REGROUPING
ORDER BY event_timestamp_kst;