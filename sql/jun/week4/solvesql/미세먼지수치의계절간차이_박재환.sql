WITH CONVERT_SEASON AS (
    SELECT
        CASE
            WHEN MONTH(measured_at) BETWEEN 3 AND 5  THEN 'spring'
      WHEN MONTH(measured_at) BETWEEN 6 AND 8  THEN 'summer'
      WHEN MONTH(measured_at) BETWEEN 9 AND 11 THEN 'autumn'
      ELSE 'winter'
END AS SEASON,
    pm10
  FROM measurements
),
RANKED AS (
  SELECT
    SEASON,
    pm10,
    ROW_NUMBER() OVER (PARTITION BY SEASON ORDER BY pm10) AS rn,
    COUNT(*)     OVER (PARTITION BY SEASON)               AS cnt
  FROM CONVERT_SEASON
),
MEDIAN AS (
  SELECT
    SEASON,
    AVG(pm10) AS median_pm10
  FROM RANKED
  WHERE rn IN (FLOOR((cnt + 1) / 2), CEIL((cnt + 1) / 2))
  GROUP BY SEASON
)
SELECT
    c.SEASON AS season,
    ROUND(AVG(c.pm10), 2)   AS pm10_average,
    ROUND(m.median_pm10, 2) AS pm10_median
FROM CONVERT_SEASON c
         JOIN MEDIAN m ON c.SEASON = m.SEASON
GROUP BY c.SEASON, m.median_pm10