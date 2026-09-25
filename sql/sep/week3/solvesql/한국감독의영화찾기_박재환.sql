SELECT a.name AS artist,
       w.title
FROM artists AS a
         JOIN artworks_artists AS aa
              ON a.artist_id = aa.artist_id
         JOIN artworks AS w
              ON aa.artwork_id = w.artwork_id
WHERE a.nationality = 'Korean'
  AND w.classification LIKE 'Film%';