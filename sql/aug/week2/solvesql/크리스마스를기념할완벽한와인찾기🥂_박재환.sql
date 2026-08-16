WITH avg_wines AS (
    SELECT
        AVG(density) AS dense_avg,
        AVG(residual_sugar) AS sugar_avg
    FROM wines
),
     avg_white_wines AS (
         SELECT
             AVG(ph) AS ph_avg,
             AVG(citric_acid) AS citric_avg
         FROM wines
         WHERE color = 'white'
     )

SELECT
    wines.*
FROM wines, avg_wines, avg_white_wines
WHERE wines.color = 'white'
  AND wines.quality > 6
  AND wines.residual_sugar > avg_wines.sugar_avg
  AND wines.density > avg_wines.dense_avg
  AND wines.ph < avg_white_wines.ph_avg
  AND wines.citric_acid > avg_white_wines.citric_avg