WITH DAILY_ORDERS AS (SELECT
    DATE (purchased_at) AS order_date
   , DATE_FORMAT(purchased_at
   , '%W') AS weekday
   , COUNT (DISTINCT transaction_id) AS num_orders_today
FROM transactions
WHERE purchased_at >= '2023-11-01'
  AND purchased_at
    < '2024-01-01'
  AND is_online_order = 1
GROUP BY
    DATE (purchased_at),
    DATE_FORMAT(purchased_at, '%W')
    )
SELECT order_date,
       weekday,
       num_orders_today,
       SUM(num_orders_today) OVER (
      ORDER BY order_date
      ROWS BETWEEN 1 PRECEDING AND CURRENT ROW
    ) AS num_orders_from_yesterday
FROM DAILY_ORDERS
ORDER BY order_date;