package aug.week1.codetree;

import java.util.*;
import java.io.*;

public class 디버깅_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    /**
     * i번 줄의 결과는 무조건 i번으로 이동해야한다.
     */
    static int n, m, h;
    static boolean[][][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        h = Integer.parseInt(st.nextToken());

        board = new boolean[n][n][h];
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int a = Integer.parseInt(st.nextToken()) - 1;
            int b = Integer.parseInt(st.nextToken()) - 1;
            board[b][b + 1][a] = true;
            board[b + 1][b][a] = true;
        }

        System.out.println(solution());
    }

    static int minPutCount;
    static int solution() {
        minPutCount = 5;            // 최대 3개 까지만 둘 수 있음
        putLine(0, 0, 0);
        return minPutCount;
    }

    static void putLine(int col, int level, int putCount) {
        if(minPutCount <= putCount) return;     // 이전의 최적해가 존재할 경우
        if (isValid()) {
            minPutCount = putCount;
            return;
        }
        if(putCount == 3) return;

        for(int l = level; l < h; l++) {
            int y = (l == level) ? col : 0;
            for(; y < n - 1; y++) {
                if(!canPut(y, l)) continue;

                board[y][y + 1][l] = true;
                board[y + 1][y][l] = true;
                putLine(y, l, putCount + 1);
                board[y][y + 1][l] = false;
                board[y + 1][y][l] = false;
            }
        }
    }

    static boolean down(int sId, int col, int level) {
        if(level == h) return sId == col;

        for(int i = 0; i < n; i++) {
            if(board[col][i][level]) return down(sId, i, level + 1);
        }
        return down(sId, col, level + 1);
    }

    static boolean isValid() {
        for (int i = 0; i < n; i++) {
            if (!down(i, i, 0)) return false;
        }
        return true;
    }

    static boolean canPut(int col, int level) {
        // 이미 설치되어 있는지
        if (board[col][col + 1][level]) return false;
        // 동일선상에 선이 있는지
        if (col > 0 && board[col - 1][col][level]) return false;
        if (col + 1 < n - 1 && board[col + 1][col + 2][level]) return false;
        return true;
    }
}
