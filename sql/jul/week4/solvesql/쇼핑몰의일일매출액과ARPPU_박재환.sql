SELECT
  DATE(ood.order_purchase_timestamp) AS dt,
  COUNT(DISTINCT ood.customer_id) AS pu,
  ROUND(SUM(oopd.payment_value), 2) AS revenue_daily,
  ROUND(SUM(oopd.payment_value) / COUNT(DISTINCT ood.customer_id), 2) AS arppu
FROM olist_orders_dataset ood
JOIN olist_order_payments_dataset oopd ON ood.order_id = oopd.order_id
WHERE ood.order_purchase_timestamp >= '2018-01-01'
GROUP BY DATE(ood.order_purchase_timestamp)
ORDER BY DATE(ood.order_purchase_timestamp)