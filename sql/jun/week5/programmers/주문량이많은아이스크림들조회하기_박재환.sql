SELECT
    T.FLAVOR
FROM (
         SELECT
             FLAVOR,
             TOTAL_ORDER
         FROM FIRST_HALF

         UNION

         SELECT
             FLAVOR,
             TOTAL_ORDER
         FROM JULY
     ) T
GROUP BY T.FLAVOR
ORDER BY SUM(T.TOTAL_ORDER) DESC
    LIMIT 3;
