SELECT a.first_name,
       a.last_name,
       SUM(p.amount) AS total_revenue
FROM actor AS a
         JOIN film_actor AS fa
              ON a.actor_id = fa.actor_id
         JOIN inventory AS i
              ON fa.film_id = i.film_id
         JOIN rental AS r
              ON i.inventory_id = r.inventory_id
         JOIN payment AS p
              ON r.rental_id = p.rental_id
GROUP BY a.actor_id, a.first_name, a.last_name
ORDER BY total_revenue DESC LIMIT 5;