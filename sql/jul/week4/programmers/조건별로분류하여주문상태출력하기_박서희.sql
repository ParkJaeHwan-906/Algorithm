SELECT
    ORDER_ID,
    PRODUCT_ID,
    OUT_DATE,
    CASE
        WHEN OUT_DATE IS NULL THEN "출고미정"
        WHEN OUT_DATE <= '2022-05-01' THEN "출고완료"
        ELSE "출고대기" END
        AS 출고여부
FROM FOOD_ORDER
ORDER BY ORDER_ID;

-- 풀이 시간: 10분, AI 사용: X
-- null은 공백으로 출력해야 하는 줄 알고 IFNULL을 썼는데 틀렸었다.. null은 그냥 null로 출력하는걸로..
