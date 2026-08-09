WITH station_events AS (
    -- 대여 기록
    SELECT rent_station_id AS station_id,
           rent_at         AS used_at
    FROM rental_history
    WHERE rent_at >= '2018-10-01'
      AND rent_at < '2018-11-01'

    UNION ALL

    SELECT rent_station_id AS station_id,
           rent_at         AS used_at
    FROM rental_history
    WHERE rent_at >= '2019-10-01'
      AND rent_at < '2019-11-01'

    UNION ALL

    -- 반납 기록
    SELECT return_station_id AS station_id,
           return_at         AS used_at
    FROM rental_history
    WHERE return_at >= '2018-10-01'
      AND return_at < '2018-11-01'

    UNION ALL

    SELECT return_station_id AS station_id,
           return_at         AS used_at
    FROM rental_history
    WHERE return_at >= '2019-10-01'
      AND return_at < '2019-11-01'),

     usage_counts AS (SELECT station_id,
                             SUM(
                                     CASE
                                         WHEN used_at >= '2018-10-01'
                                             AND used_at < '2018-11-01'
                                             THEN 1
                                         ELSE 0
                                         END
                             ) AS usage_2018,
                             SUM(
                                     CASE
                                         WHEN used_at >= '2019-10-01'
                                             AND used_at < '2019-11-01'
                                             THEN 1
                                         ELSE 0
                                         END
                             ) AS usage_2019
                      FROM station_events
                      GROUP BY station_id)

SELECT s.station_id,
       s.name,
       s.local,
       ROUND(u.usage_2019 * 100.0 / u.usage_2018, 2) AS usage_pct
FROM usage_counts AS u
         JOIN station AS s
              ON s.station_id = u.station_id
WHERE u.usage_2018 > 0
  AND u.usage_2019 > 0
  AND u.usage_2019 * 100.0 / u.usage_2018 <= 50;