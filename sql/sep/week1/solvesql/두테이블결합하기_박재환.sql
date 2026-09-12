SELECT
  rc.athlete_id
FROM events ev
JOIN records rc ON ev.id = rc.event_id
WHERE sport LIKE "Golf"
GROUP BY rc.athlete_id;