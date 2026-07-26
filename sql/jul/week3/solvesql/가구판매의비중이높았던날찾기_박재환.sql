WITH DAILY_ORDERS AS (
    SELECT
        order_date,
        COUNT(DISTINCT order_id) AS total_count,
        COUNT(
                DISTINCT CASE
                             WHEN category = 'Furniture' THEN order_id
            END
        ) AS furniture
    FROM records
    GROUP BY order_date
)

SELECT
    order_date,
    furniture,
    ROUND(furniture * 100 / total_count, 2) AS furniture_pct
FROM DAILY_ORDERS
WHERE total_count >= 10
  AND furniture * 100 / total_count >= 40
ORDER BY furniture_pct DESC, order_date;
