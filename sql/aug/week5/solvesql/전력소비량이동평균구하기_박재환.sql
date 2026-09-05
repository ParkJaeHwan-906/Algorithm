SELECT
    DATE_ADD(measured_at, INTERVAL 10 MINUTE) AS end_at,

    ROUND(
            AVG(zone_quads) OVER (
        ORDER BY measured_at
        ROWS BETWEEN 5 PRECEDING AND CURRENT ROW
      ),
            2
    ) AS zone_quads,

    ROUND(
            AVG(zone_smir) OVER (
        ORDER BY measured_at
        ROWS BETWEEN 5 PRECEDING AND CURRENT ROW
      ),
            2
    ) AS zone_smir,

    ROUND(
            AVG(zone_boussafou) OVER (
        ORDER BY measured_at
        ROWS BETWEEN 5 PRECEDING AND CURRENT ROW
      ),
            2
    ) AS zone_boussafou

FROM power_consumptions
WHERE measured_at >= '2017-01-01'
  AND measured_at <  '2017-02-01'
ORDER BY measured_at;