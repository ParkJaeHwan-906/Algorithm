SELECT old.station_id,
       old.name
FROM station AS old
         JOIN station AS newer
              ON old.station_id <> newer.station_id
                  AND old.updated_at < newer.updated_at
                  AND 2 * 6356 * ASIN(
                          SQRT(
                                  SIN((RADIANS(old.lat) - RADIANS(newer.lat)) / 2)
                                      * SIN((RADIANS(old.lat) - RADIANS(newer.lat)) / 2)
                                      + COS(RADIANS(old.lat))
                                      * COS(RADIANS(newer.lat))
                                      * SIN((RADIANS(old.lng) - RADIANS(newer.lng)) / 2)
                                      * SIN((RADIANS(old.lng) - RADIANS(newer.lng)) / 2)
                          )
                                 ) <= 0.3
GROUP BY old.station_id,
         old.name
HAVING COUNT(*) >= 5;