SELECT
    CONCAT('/home/grep/src/', ugb.BOARD_ID, '/', ugf.FILE_ID, ugf.FILE_NAME, ugf.FILE_EXT) AS FILE_PATH
FROM (
         SELECT
             RANK() OVER (ORDER BY VIEWS DESC) AS RNK,
             BOARD_ID
         FROM USED_GOODS_BOARD
     ) AS ugb
         JOIN USED_GOODS_FILE ugf ON ugb.BOARD_ID = ugf.BOARD_ID
WHERE ugb.RNK = 1
ORDER BY ugf.FILE_ID DESC