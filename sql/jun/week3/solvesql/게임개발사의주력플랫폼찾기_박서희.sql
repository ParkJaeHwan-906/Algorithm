WITH sum_game AS (
    SELECT
        c.name AS developer,
        p.name AS platform,
        SUM(g.sales_eu + g.sales_jp + g.sales_na + g.sales_other) AS sales
    FROM games g
    JOIN companies c ON g.developer_id = c.company_id
    JOIN platforms p ON g.platform_id = p.platform_id
    GROUP BY c.name, p.name
),

ranked_game AS (
    SELECT
        developer,
        platform,
        sales,
        DENSE_RANK() OVER (PARTITION BY developer ORDER BY sales DESC) AS rnk
    FROM sum_game
)

SELECT
    developer,
    platform,
    sales
FROM ranked_game
WHERE rnk = 1;

-- 풀이 시간: 14분 , AI 사용: O
