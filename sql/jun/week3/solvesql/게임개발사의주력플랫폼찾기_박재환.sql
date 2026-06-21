WITH company_game AS (
    SELECT
        c.name AS developer,
        p.name AS platform,
        SUM(g.sales_na + g.sales_eu + g.sales_jp + g.sales_other) AS sales
    FROM companies c
    JOIN games g ON c.company_id = g.developer_id
    JOIN platforms p ON g.platform_id = p.platform_id
    GROUP BY g.developer_id, p.platform_id
),
apply_rank AS (
    SELECT
        DENSE_RANK() OVER (PARTITION BY developer ORDER BY sales DESC) AS RNK,
        company_game.*
    FROM company_game
)

SELECT developer, platform, sales
FROM apply_rank
WHERE rnk = 1
ORDER BY developer, platform;