WITH Furniture AS (SELECT region,
                          COUNT(DISTINCT order_id) AS Furniture
                   FROM records
                   WHERE category = 'Furniture'
                   GROUP BY region),
     Office_Supplies AS (SELECT region,
                                COUNT(DISTINCT order_id) AS `Office Supplies`
                         FROM records
                         WHERE category = 'Office Supplies'
                         GROUP BY region),
     Technology AS (SELECT region,
                           COUNT(DISTINCT order_id) AS Technology
                    FROM records
                    WHERE category = 'Technology'
                    GROUP BY region)
SELECT T.region AS Region,
       F.Furniture,
       O.`Office Supplies`,
       T.Technology
FROM Furniture F
         JOIN Office_Supplies O ON F.region = O.region
         JOIN Technology T ON F.region = T.region