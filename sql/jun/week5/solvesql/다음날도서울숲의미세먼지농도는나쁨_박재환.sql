SELECT
    t.measured_at AS today,
    t.NEXT_DAY AS next_day,
    t.pm10,
    t.NEXT_PM10 AS next_pm10
FROM (
         SELECT
             *,
             LEAD(measured_at) OVER (ORDER BY measured_at) AS NEXT_DAY,
             LEAD(pm10) OVER (ORDER BY measured_at) AS NEXT_PM10
         FROM measurements
     ) t
WHERE NEXT_PM10 > pm10;