WITH all_edges AS (SELECT user_a_id AS user_id,
                          user_b_id AS friend_id
                   FROM edges

                   UNION ALL

                   SELECT user_b_id AS user_id,
                          user_a_id AS friend_id
                   FROM edges),
     friend_counts AS (SELECT user_id,
                              COUNT(*) AS friends
                       FROM all_edges
                       GROUP BY user_id),
     friends_of_friends AS (SELECT ae.user_id,
                                   SUM(fc.friends) AS friends_of_friends
                            FROM all_edges AS ae
                                     INNER JOIN friend_counts AS fc
                                                ON ae.friend_id = fc.user_id
                            GROUP BY ae.user_id)
SELECT fc.user_id,
       fc.friends,
       fof.friends_of_friends,
       ROUND(fof.friends_of_friends / fc.friends, 2) AS ratio
FROM friend_counts AS fc
         INNER JOIN friends_of_friends AS fof
                    ON fc.user_id = fof.user_id
WHERE fc.friends >= 100
ORDER BY ratio DESC LIMIT 5;