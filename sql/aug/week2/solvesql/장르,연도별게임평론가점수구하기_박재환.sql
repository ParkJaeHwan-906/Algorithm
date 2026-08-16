SELECT
    g.name AS genre,
    ROUND(AVG(
                  CASE WHEN game.year = 2011 THEN game.critic_score END
          ), 2) AS score_2011,
    ROUND(AVG(
                  CASE WHEN game.year = 2012 THEN game.critic_score END
          ), 2) AS score_2012,
    ROUND(AVG(
                  CASE WHEN game.year = 2013 THEN game.critic_score END
          ), 2) AS score_2013,
    ROUND(AVG(
                  CASE WHEN game.year = 2014 THEN game.critic_score END
          ), 2) AS score_2014,
    ROUND(AVG(
                  CASE WHEN game.year = 2015 THEN game.critic_score END
          ), 2) AS score_2015
FROM games AS game
         JOIN genres AS g
              ON game.genre_id = g.genre_id
WHERE game.year BETWEEN 2011 AND 2015
GROUP BY
    g.genre_id,
    g.name
ORDER BY g.name;