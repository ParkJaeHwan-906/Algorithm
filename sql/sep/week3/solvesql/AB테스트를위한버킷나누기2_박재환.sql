WITH USER_BUCKET AS (SELECT customer_id,
                            total_price,
                            CASE
                                WHEN customer_id % 10 = 0 THEN 'A'
                                ELSE 'B'
                                END AS bucket
                     FROM transactions
                     WHERE is_returned = 0),
     USER_STATS AS (SELECT bucket,
                           customer_id,
                           COUNT(*)         AS order_count,
                           SUM(total_price) AS revenue
                    FROM USER_BUCKET
                    GROUP BY bucket, customer_id)
SELECT bucket,
       COUNT(*)                   AS user_count,
       ROUND(AVG(order_count), 2) AS avg_orders,
       ROUND(AVG(revenue), 2)     AS avg_revenue
FROM USER_STATS
GROUP BY bucket
ORDER BY bucket;