SELECT
    mentee.employee_id AS mentee_id,
    mentee.name AS mentee_name,
    mentor.employee_id AS mentor_id,
    mentor.name AS mentor_name
FROM employees mentee -- 매칭 가능한 멘토가 없어도 멘티는 출력
LEFT JOIN employees mentor
ON mentee.department != mentor.department AND mentor.join_date <= '2019-12-31' -- 멘토 조건
WHERE mentee.join_date BETWEEN '2021-10-01' AND '2021-12-31' -- 멘티 조건
ORDER BY mentee_id, mentor_id;

-- 풀이 시간: 모르겠어서 AI로 품.. , AI 사용: O
