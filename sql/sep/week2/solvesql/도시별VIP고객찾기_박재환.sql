WITH CUSTOMER_SPENT AS (SELECT city_id,
                               customer_id,
                               SUM(total_price - discount_amount) AS total_spent
                        FROM transactions
                        WHERE is_returned = 0
                        GROUP BY city_id,
                                 customer_id),
     RANKED_CUSTOMERS AS (SELECT city_id,
                                 customer_id,
                                 total_spent,
                                 RANK() OVER (
        PARTITION BY city_id
        ORDER BY total_spent DESC
      ) AS ranking
                          FROM CUSTOMER_SPENT)
SELECT city_id,
       customer_id,
       total_spent
FROM RANKED_CUSTOMERS
WHERE ranking = 1;