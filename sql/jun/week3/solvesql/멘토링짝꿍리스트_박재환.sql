WITH mentees AS (
    SELECT
        *
    FROM employees
    WHERE TIMESTAMPDIFF(MONTH, join_date, '2021-12-31') BETWEEN 0 AND 3
), mentors AS (
    SELECT
        *
    FROM employees
    WHERE TIMESTAMPDIFF(YEAR, join_date, '2021-12-31') >= 2
)

SELECT
    mentee.employee_id AS mentee_id,
    mentee.name AS mentee_name,
    mentor.employee_id AS mentor_id,
    mentor.name AS mentor_name
FROM mentees mentee
LEFT JOIN mentors mentor ON mentee.department <> mentor.department
ORDER BY mentee_id ASC, mentor_id ASC;