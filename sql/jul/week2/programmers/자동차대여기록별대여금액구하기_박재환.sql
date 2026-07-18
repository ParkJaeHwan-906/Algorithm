WITH RENT AS (SELECT HIS.HISTORY_ID                                       AS HISTORY_ID,
                     CAR.CAR_ID                                           AS CAR_ID,
                     CAR.CAR_TYPE                                         AS CAR_TYPE,
                     TIMESTAMPDIFF(DAY, HIS.START_DATE, HIS.END_DATE) + 1 AS PERIOD,
                     CAR.DAILY_FEE                                        AS DAILY_FEE
              FROM CAR_RENTAL_COMPANY_RENTAL_HISTORY HIS
                       JOIN CAR_RENTAL_COMPANY_CAR CAR ON CAR.CAR_ID = HIS.CAR_ID),
     DISCOUNT AS (SELECT CAR_TYPE,
                         SUBSTRING_INDEX(DURATION_TYPE, '일', 1) + 0 AS PERIOD,
                         SUBSTRING_INDEX(DISCOUNT_RATE, '%', 1) + 0 AS DISCOUNT_RATE
                  FROM CAR_RENTAL_COMPANY_DISCOUNT_PLAN),
     POLICY AS (SELECT CAR_TYPE,
                       PERIOD AS    START,
                       LEAD(PERIOD) OVER (PARTITION BY CAR_TYPE ORDER BY PERIOD) AS END, DISCOUNT_RATE
FROM DISCOUNT
    )

SELECT R.HISTORY_ID,
       ROUND((R.PERIOD * (R.DAILY_FEE - (R.DAILY_FEE * (IFNULL(P.DISCOUNT_RATE, 0) / 100)))), 0) AS FEE
FROM RENT R

         LEFT JOIN POLICY P ON R.CAR_TYPE = P.CAR_TYPE
    AND (R.PERIOD >= P.START AND (R.PERIOD < P.END OR P.END IS NULL))

WHERE R.CAR_TYPE = '트럭'

ORDER BY FEE DESC, R.HISTORY_ID DESC