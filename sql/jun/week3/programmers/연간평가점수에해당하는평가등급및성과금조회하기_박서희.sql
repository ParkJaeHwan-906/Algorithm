WITH HR_SCORE AS (
    SELECT
        EMP_NO,
        CASE
            WHEN AVG(SCORE) >= 96 THEN 'S'
            WHEN AVG(SCORE) >= 90 THEN 'A'
            WHEN AVG(SCORE) >= 80 THEN 'B'
            ELSE 'C'
        END AS GRADE
    FROM HR_GRADE
    GROUP BY EMP_NO
)

SELECT
    E.EMP_NO,
    E.EMP_NAME,
    S.GRADE,
    CASE
        WHEN S.GRADE = 'S' THEN E.SAL * 0.2
        WHEN S.GRADE = 'A' THEN E.SAL * 0.15
        WHEN S.GRADE = 'B' THEN E.SAL * 0.1
        ELSE 0
    END AS BONUS
FROM HR_EMPLOYEES E
LEFT JOIN HR_SCORE S
    ON E.EMP_NO = S.EMP_NO
ORDER BY EMP_NO;

-- 풀이 시간: 15분 , AI 사용: X
