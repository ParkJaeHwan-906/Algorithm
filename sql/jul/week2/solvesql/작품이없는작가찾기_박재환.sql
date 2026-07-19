SELECT
    artist.artist_id,
    artist.name
FROM artists artist
         LEFT JOIN artworks_artists works ON works.artist_id = artist.artist_id

WHERE works.artwork_id IS NULL
AND artist.death_year IS NOT NULL