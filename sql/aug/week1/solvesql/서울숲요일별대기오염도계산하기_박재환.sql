WITH weekday_measurements AS (SELECT WEEKDAY(measured_at) AS weekday_no,
                                     no2,
                                     o3,
                                     co,
                                     so2,
                                     pm10,
                                     pm2_5
                              FROM measurements)

SELECT CASE weekday_no
           WHEN 0 THEN '월요일'
           WHEN 1 THEN '화요일'
           WHEN 2 THEN '수요일'
           WHEN 3 THEN '목요일'
           WHEN 4 THEN '금요일'
           WHEN 5 THEN '토요일'
           WHEN 6 THEN '일요일'
           END              AS weekday,
       ROUND(AVG(no2), 4)   AS no2,
       ROUND(AVG(o3), 4)    AS o3,
       ROUND(AVG(co), 4)    AS co,
       ROUND(AVG(so2), 4)   AS so2,
       ROUND(AVG(pm10), 4)  AS pm10,
       ROUND(AVG(pm2_5), 4) AS pm2_5
FROM weekday_measurements
GROUP BY weekday_no
ORDER BY weekday_no;