 WITH avg_tables AS (
    SELECT
      genre_id,
      ROUND(AVG(critic_score), 3) AS critic_score,
      CEIL(AVG(critic_count)) AS critic_count,
      ROUND(AVG(user_score), 3) AS user_score,
      CEIL(AVG(user_count)) AS user_count
    FROM games
    GROUP BY genre_id
  )

  SELECT
    g.game_id,
    g.name,
    IFNULL(g.critic_score, a.critic_score) AS critic_score,
    IFNULL(g.critic_count, a.critic_count) AS critic_count,
    IFNULL(g.user_score, a.user_score) AS user_score,
    IFNULL(g.user_count, a.user_count) AS user_count
  FROM games AS g
  JOIN avg_tables AS a
    ON g.genre_id = a.genre_id
  WHERE g.year >= 2015
    AND (
      g.critic_score IS NULL
      OR g.user_score IS NULL
    );