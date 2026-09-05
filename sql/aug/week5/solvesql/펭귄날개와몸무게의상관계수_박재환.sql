WITH deviations AS (
    SELECT
        species,

        flipper_length_mm
            - AVG(flipper_length_mm) OVER (
            PARTITION BY species
          ) AS flipper_dev,

        body_mass_g
            - AVG(body_mass_g) OVER (
            PARTITION BY species
          ) AS mass_dev

    FROM penguins
    WHERE flipper_length_mm IS NOT NULL
      AND body_mass_g IS NOT NULL
)
SELECT
    species,
    ROUND(
            SUM(flipper_dev * mass_dev)
                /
            SQRT(
                    SUM(POWER(flipper_dev, 2))
                        * SUM(POWER(mass_dev, 2))
            ),
            3
    ) AS corr
FROM deviations
GROUP BY species;
