WITH rnk AS (
    SELECT
        RANK() OVER(PARTITION BY day ORDER BY total_bill DESC) AS RNK,
        tips.*
    FROM tips
)

SELECT total_bill, tip, sex, smoker, day, time, size FROM rnk
WHERE rnk = 1