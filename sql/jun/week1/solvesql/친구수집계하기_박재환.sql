SELECT
    u.user_id,
    COUNT(t.user_id) AS num_friends
FROM users u
LEFT JOIN (
    SELECT user_a_id AS user_id
    FROM edges
    UNION ALL
    SELECT user_b_id AS user_id
    FROM edges
) t
ON u.user_id = t.user_id
GROUP BY u.user_id
ORDER BY num_friends DESC, u.user_id ASC;