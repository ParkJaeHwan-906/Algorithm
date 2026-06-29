SELECT
    a.name
FROM records r
         JOIN games g ON r.game_id    = g.id
         JOIN teams t ON r.team_id    = t.id
         JOIN athletes a ON r.athlete_id = a.id

WHERE g.year >= 2000
  AND r.medal IS NOT NULL

GROUP BY r.athlete_id
HAVING COUNT(DISTINCT t.team) >= 2
ORDER BY a.name;