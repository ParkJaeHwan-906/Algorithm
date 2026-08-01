SELECT
    ID,
    EMAIL,
    FIRST_NAME,
    LAST_NAME
FROM
    DEVELOPERS D
WHERE EXISTS (
    SELECT 1
    FROM SKILLCODES S
    WHERE S.CATEGORY = 'Front End'
      AND (D.SKILL_CODE & S.CODE) > 0
)
ORDER BY ID;

-- 풀이 시간: 12분 , AI 사용: O
-- SQL에서도 비트연산이 되는지 몰랐다. 그리고 EXISTS 문도 도움을 받았다.
