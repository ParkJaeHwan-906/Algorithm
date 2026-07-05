SELECT
    DATE(order_purchase_timestamp) AS purchase_date,
    SUM(order_estimated_delivery_date >= order_delivered_customer_date) AS success,
    SUM(order_estimated_delivery_date < order_delivered_customer_date) AS fail
FROM olist_orders_dataset

WHERE DATE_FORMAT(order_purchase_timestamp, '%Y-%m') = '2017-01'
  AND order_estimated_delivery_date IS NOT NULL
  AND order_delivered_customer_date IS NOT NULL

GROUP BY DATE(order_purchase_timestamp)

ORDER BY purchase_date;
