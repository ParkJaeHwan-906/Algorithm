WITH ranking AS (
    SELECT
        day,
        time,
        total_bill,
        sex,
        DENSE_RANK() OVER(PARTITION BY day ORDER BY total_bill DESC) AS r
    FROM tips
)

SELECT
    day,
    time,
    sex,
    total_bill
FROM ranking
WHERE r <= 3;

-- 풀이 시간: 9분 , AI 사용: X
