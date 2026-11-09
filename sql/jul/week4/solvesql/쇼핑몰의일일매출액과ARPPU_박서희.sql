SELECT
    DATE(o.order_purchase_timestamp) AS dt,
    COUNT(DISTINCT o.customer_id) AS pu,
    ROUND(SUM(p.payment_value), 2) AS revenue_daily,
    ROUND(SUM(p.payment_value) / COUNT(DISTINCT o.customer_id), 2) AS arppu
FROM olist_orders_dataset AS o
INNER JOIN olist_order_payments_dataset AS p
ON o.order_id = p.order_id
WHERE DATE(o.order_purchase_timestamp) >= '2018-01-01'
GROUP BY DATE(o.order_purchase_timestamp)
ORDER BY DATE(order_purchase_timestamp)

-- 풀이 시간: 10분, AI 사용 : X
