WITH sub_sales AS (
    SELECT
        category,
        sub_category,
        SUM(sales) AS sales_sub_category
    FROM records
    GROUP BY category, sub_category
)
SELECT
    category,
    sub_category,
    ROUND(sales_sub_category, 2) AS sales_sub_category,
    ROUND(SUM(sales_sub_category) OVER (PARTITION BY category), 2) AS sales_category,
    ROUND(SUM(sales_sub_category) OVER (), 2) AS sales_total,
    ROUND(
            sales_sub_category * 100.0
                / SUM(sales_sub_category) OVER (PARTITION BY category),
            2
    ) AS pct_in_category,
    ROUND(
            sales_sub_category * 100.0
                / SUM(sales_sub_category) OVER (),
            2
    ) AS pct_in_total
FROM sub_sales
ORDER BY category, sub_category;