SELECT SUBSTRING_INDEX(address, ' ', 1)                           AS sido,
       SUBSTRING_INDEX(SUBSTRING_INDEX(address, ' ', 2), ' ', -1) AS sigungu,
       COUNT(cafe_id)                                             AS cnt
FROM cafes

GROUP BY sido, sigungu

ORDER BY cnt DESC