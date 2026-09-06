WITH yearly_acquisitions AS (
    SELECT
    YEAR(acquisition_date) AS acquisition_year,
    COUNT(*) AS flow
FROM artworks
WHERE acquisition_date IS NOT NULL
GROUP BY YEAR(acquisition_date)
    )
SELECT
    acquisition_year AS `Acquisition year`,
    flow AS `New acquisitions this year (Flow)`,
    SUM(flow) OVER (
      ORDER BY acquisition_year
    ) AS `Total collection size (Stock)`
FROM yearly_acquisitions
ORDER BY acquisition_year;
