WITH prev_event AS (
    SELECT
        user_pseudo_id,
        event_timestamp_kst,
        LAG(event_timestamp_kst, 1) OVER (
      PARTITION BY user_pseudo_id
      ORDER BY event_timestamp_kst
    ) AS prev_event_timestamp_kst
    FROM ga
    WHERE user_pseudo_id = "S3WDQCqLpK"
),
new_session AS (
     SELECT
         user_pseudo_id,
         event_timestamp_kst,
         SUM(
             CASE
                 WHEN prev_event_timestamp_kst IS NULL THEN 1
                 WHEN TIMESTAMPDIFF(HOUR, prev_event_timestamp_kst, event_timestamp_kst) >= 1 THEN 1
                 ELSE 0
                 END
         ) OVER (
        ORDER BY event_timestamp_kst
        ) AS new_session_id
     FROM prev_event
)

SELECT
    'S3WDQCqLpK' AS user_pseudo_id,
    MIN(event_timestamp_kst) AS session_start,
    MAX(event_timestamp_kst) AS session_end
FROM new_session
GROUP BY new_session_id
ORDER BY session_start

-- 풀이 시간: 40분+, AI 사용: O
-- SUM 함수로 session_id를 새로 할당할 수 있다는 것을 알게 되었습니다. TIMESTAMPDIFF()함수도 몰랐습니다.
-- 이렇게 어렵게도 문제가 나오네요!
