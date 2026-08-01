WITH total_sales AS (
    SELECT
        category,
        sub_category,
        SUM(sales) AS sales_sub_cat,
        SUM(SUM(sales)) OVER(PARTITION BY category) AS sales_cat,
        SUM(SUM(sales)) OVER() AS sales_to
    FROM records
    GROUP BY category, sub_category
)

SELECT
    category,
    sub_category,
    ROUND(sales_sub_cat, 2) AS sales_sub_category,
    ROUND(sales_cat, 2) AS sales_category,
    ROUND(sales_to, 2) AS sales_total,
    ROUND(sales_sub_cat/ sales_cat * 100, 2) AS pct_in_category,
    ROUND(sales_sub_cat/sales_to * 100, 2) AS pct_in_total
FROM total_sales

-- 풀이 시간: 20분 , AI 사용: O
-- 어렵다 어렵다..
-- SUM 세 번 중첩은 불가능하다는 것을 알게 됨.
