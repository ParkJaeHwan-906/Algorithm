SELECT
    a.id,
    a.name,
    GROUP_CONCAT(DISTINCT r.medal) AS medals
FROM records AS r
         JOIN athletes AS a ON r.athlete_id = a.id
         JOIN events AS e ON r.event_id = e.id
         JOIN teams AS t ON r.team_id = t.id
WHERE e.event = 'Volleyball Women''s Volleyball'
  AND t.team = 'KOR'
  AND r.medal IS NOT NULL
GROUP BY a.id
ORDER BY a.id;
