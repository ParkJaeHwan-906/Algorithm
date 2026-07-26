SELECT '1Q'                                                 AS QUARTER,
       SUM(
               CASE
                   WHEN MONTH ( DIFFERENTIATION_DATE) BETWEEN 1 AND 3 THEN 1
              ELSE 0
          END
      ) AS ECOLI_COUNT
FROM ECOLI_DATA

UNION ALL

SELECT '2Q'                                                 AS QUARTER,
       SUM(
               CASE
                   WHEN MONTH ( DIFFERENTIATION_DATE) BETWEEN 4 AND 6 THEN 1
              ELSE 0
          END
      ) AS ECOLI_COUNT
FROM ECOLI_DATA

UNION ALL

SELECT '3Q'                                                 AS QUARTER,
       SUM(
               CASE
                   WHEN MONTH ( DIFFERENTIATION_DATE) BETWEEN 7 AND 9 THEN 1
              ELSE 0
          END
      ) AS ECOLI_COUNT
FROM ECOLI_DATA

UNION ALL

SELECT '4Q'                                                 AS QUARTER,
       SUM(
               CASE
                   WHEN MONTH ( DIFFERENTIATION_DATE) BETWEEN 10 AND 12 THEN 1
              ELSE 0
          END
      ) AS ECOLI_COUNT
FROM ECOLI_DATA

ORDER BY QUARTER;