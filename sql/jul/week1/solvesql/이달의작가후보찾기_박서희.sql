SELECT author
FROM books
WHERE genre = "Fiction"
GROUP BY author
HAVING COUNT(author) >= 2
   AND AVG(user_rating) >= 4.5
   AND AVG(reviews) >= (SELECT AVG(reviews)
                        FROM books
                        WHERE genre = "Fiction")
ORDER BY author;

-- 풀이 시간: 10 , AI 사용: X
