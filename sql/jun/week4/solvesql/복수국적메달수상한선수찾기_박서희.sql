SELECT
    DISTINCT a.name
FROM athletes a
         JOIN (
    SELECT athlete_id
    FROM records
    WHERE game_id in (SELECT id FROM games WHERE year >= 2000) AND medal IS NOT NULL
GROUP BY athlete_id
HAVING COUNT(DISTINCT team_id) >= 2
    ) r
ON a.id = r.athlete_id
ORDER BY name;

-- 풀이 시간: 15분 , AI 사용: O 참가한 팀이 2개 이상 -> COUNT(DISTINCT team_id) >= 2
