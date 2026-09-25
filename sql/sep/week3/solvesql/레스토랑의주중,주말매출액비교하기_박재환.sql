WITH CONVERT_WEEK AS (
    SELECT
        CASE
            WHEN day IN ('Sat', 'Sun') THEN 'weekend'
    ELSE 'weekday'
END AS week,
      total_bill
    FROM tips
  )
SELECT
    week,
    SUM(total_bill) AS sales
FROM CONVERT_WEEK
GROUP BY week
ORDER BY sales DESC;