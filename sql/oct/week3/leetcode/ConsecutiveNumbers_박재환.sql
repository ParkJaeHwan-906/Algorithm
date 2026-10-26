WITH lead_num AS (
    SELECT
        num,
        LEAD(num, 1) OVER (ORDER BY id) AS next1,
        LEAD(num, 2) OVER (ORDER BY id) AS next2
    FROM Logs
)
SELECT
    DISTINCT num AS ConsecutiveNums
FROM lead_num
WHERE num = next1 AND num = next2;
