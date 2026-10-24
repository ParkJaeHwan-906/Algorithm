WITH Emp_Top AS (
    SELECT
        RANK() OVER (PARTITION BY departmentId
            ORDER BY salary DESC) AS RNK,
        name,
        departmentId,
        salary
    FROM Employee
)

SELECT
    d.name AS Department,
    et.name AS Employee,
    et.salary AS Salary
FROM Emp_Top et
         JOIN Department d ON et.departmentId = d.id
WHERE et.RNK = 1;