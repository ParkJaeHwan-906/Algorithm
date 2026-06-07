-- 정답 X --
-- 6 / 7 개가 정답인데, 1가지 케이스를 못찾겠습니다. --
WITH SESSION AS (
    SELECT
        LAG(event_timestamp_kst, 1) OVER (
      ORDER BY event_timestamp_kst ASC
    ) AS PREV,
        event_timestamp_kst AS CUR
    FROM ga
    WHERE user_pseudo_id = 'S3WDQCqLpK'
),
     NEW_SESSION AS (
         SELECT
             CUR,
             SUM(
                     CASE
                         WHEN PREV IS NULL THEN 1
                         WHEN TIMESTAMPDIFF(HOUR, PREV, CUR) >= 1 THEN 1
                         ELSE 0
                         END
             ) OVER (
      ORDER BY CUR
      ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
    ) AS SESSION_NUM
         FROM SESSION
     )

SELECT
    'S3WDQCqLpK' AS user_pseudo_id ,
    MIN(CUR) AS session_start,
    MAX(CUR) AS session_end
FROM NEW_SESSION
GROUP BY SESSION_NUM
ORDER BY session_start;