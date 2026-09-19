WITH yearly AS (SELECT DISTINCT author,
    year
FROM books
WHERE genre = 'Fiction'
    )
    , grouped AS (
SELECT
    author, year, year - ROW_NUMBER() OVER (
    PARTITION BY author
    ORDER BY year
    ) AS grp
FROM yearly
    )
SELECT author,
       MAX(year) AS year,
      COUNT(*) AS depth
FROM grouped
GROUP BY author, grp
HAVING COUNT (*) >= 5;