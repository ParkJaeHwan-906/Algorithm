SELECT
  DATE_FORMAT(o.order_date, '%Y-%m') AS order_month,
  SUM(
    CASE
      WHEN o.order_id LIKE 'C%' THEN 0
      ELSE (oi.price * oi.quantity)
    END
  ) AS ordered_amount,
  SUM(
    CASE
      WHEN o.order_id LIKE 'C%' THEN (oi.price * oi.quantity)
      ELSE 0
    END
  ) AS canceled_amount,
  SUM(oi.price * oi.quantity) AS total_amount
FROM orders o
JOIN order_items oi ON o.order_id = oi.order_id
GROUP BY DATE_FORMAT(o.order_date, '%Y-%m')
ORDER BY order_month