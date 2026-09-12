WITH first_order_dates AS (
  SELECT
    customer_id,
    MIN(order_date) AS first_order_date
  FROM records
  GROUP BY customer_id
)

SELECT
    r.category,
    r.sub_category,
    COUNT(DISTINCT r.order_id) AS cnt_orders
  FROM records AS r
  JOIN first_order_dates AS f
    ON r.customer_id = f.customer_id
   AND r.order_date = f.first_order_date
  GROUP BY
    r.category,
    r.sub_category
  ORDER BY
    cnt_orders DESC;