SELECT
    u.user_id,
    IFNULL(a.friends, 0) + IFNULL(b.friends, 0) AS num_friends
FROM users u
LEFT JOIN (
    SELECT
        user_a_id,
        COUNT(*) AS friends
    FROM edges
    GROUP BY user_a_id
) AS a ON a.user_a_id = u.user_id
LEFT JOIN (
    SELECT
        user_b_id,
        COUNT(*) AS friends
    FROM edges
    GROUP BY user_b_id
) AS b ON b.user_b_id = u.user_id
ORDER BY num_friends DESC, user_id ASC
-- 풀이 시간: 10분, AI 사용: X
