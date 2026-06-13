SELECT
    t.day,
    t.time,
    t.sex,
    t.total_bill
FROM (
    SELECT
        RANK() OVER(PARTITION BY DAY ORDER BY total_bill DESC) AS RNK,
        day,
        time,
        sex,
        total_bill
    FROM tips
    ) t
WHERE t.RNK <= 3