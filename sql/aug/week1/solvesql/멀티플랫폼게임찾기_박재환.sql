WITH major_platform_games AS (SELECT g.name,
                                     CASE
                                         WHEN p.name IN ('PS3', 'PS4', 'PSP', 'PSV')
                                             THEN 'Sony'
                                         WHEN p.name IN ('Wii', 'WiiU', 'DS', '3DS')
                                             THEN 'Nintendo'
                                         WHEN p.name IN ('X360', 'XONE')
                                             THEN 'Microsoft'
                                         END AS platform_group
                              FROM games AS g
                                       JOIN platforms AS p
                                            ON p.platform_id = g.platform_id
                              WHERE g.year >= 2012
                                AND p.name IN (
                                               'PS3', 'PS4', 'PSP', 'PSV',
                                               'Wii', 'WiiU', 'DS', '3DS',
                                               'X360', 'XONE'
                                  ))

SELECT name
FROM major_platform_games
GROUP BY name
HAVING COUNT(DISTINCT platform_group) >= 2;