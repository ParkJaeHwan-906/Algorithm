  SELECT
    ab.user_a_id AS user_a_id,
    ab.user_b_id AS user_b_id,
    ac.user_b_id AS user_c_id
  FROM edges AS ab
  JOIN edges AS ac
    ON ab.user_a_id = ac.user_a_id
  JOIN edges AS bc
    ON ab.user_b_id = bc.user_a_id
   AND ac.user_b_id = bc.user_b_id
  WHERE ab.user_a_id < ab.user_b_id
    AND ab.user_b_id < ac.user_b_id
    AND (
      ab.user_a_id = 3820
      OR ab.user_b_id = 3820
      OR ac.user_b_id = 3820
    );