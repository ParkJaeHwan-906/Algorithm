WITH ranked_edges AS (
    SELECT
        user_a_id,
        user_b_id,
        user_a_id + user_b_id AS id_sum,
        ROW_NUMBER() OVER (
        ORDER BY user_a_id + user_b_id
      ) AS ranking,
        COUNT(*) OVER () AS total_count
    FROM edges
)
SELECT
    user_a_id,
    user_b_id,
    id_sum
FROM ranked_edges
WHERE ranking * 1000 <= total_count
ORDER BY
    id_sum,
    user_a_id,
    user_b_id;