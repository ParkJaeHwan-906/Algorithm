SELECT
    ITEM_ID,
    ITEM_NAME,
    RARITY
FROM ITEM_INFO
WHERE ITEM_ID NOT IN (
    SELECT DISTINCT
        PARENT_ITEM_ID
    FROM ITEM_TREE
    WHERE PARENT_ITEM_ID IS NOT NULL
)
ORDER BY ITEM_ID DESC;

-- 풀이 시간: 10분, AI 사용: O
-- WHERE PARENT_ITEM_ID IS NOT NULL을 안 썼을 때는 결과가 아무것도 안 나왔다. NOT IN (NULL)이 되면 True가 아니라 Unknown이 되고
-- Unknown이 되면 True가 아니라서 결과값이 아무것도 안 나온다고 한다. -> NOT IN (NULL)은 사용하면 안 된다는 것을 알게 됨.