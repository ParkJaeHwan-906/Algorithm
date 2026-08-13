WITH appearances AS (
    SELECT DISTINCT
        r.athlete_id,
        g.year
    FROM records AS r
             JOIN games AS g
                  ON r.game_id = g.id
             JOIN events AS e
                  ON r.event_id = e.id
             JOIN teams AS t
                  ON r.team_id = t.id
    WHERE e.event = 'Volleyball Women''s Volleyball'
      AND t.team = 'KOR'
)
SELECT DISTINCT
    a.id,
    a.name
FROM appearances AS current_game
         JOIN appearances AS next_game
              ON current_game.athlete_id = next_game.athlete_id
                  AND current_game.year + 4 = next_game.year
         JOIN athletes AS a
              ON a.id = current_game.athlete_id
ORDER BY a.id;
