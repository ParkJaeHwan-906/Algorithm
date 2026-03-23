package mar.week3.codetree;

import java.util.*;
import java.io.*;

public class 해산물채취_박재환 {
    static BufferedReader br;
    static StringBuilder sb;

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }

    static StringTokenizer st;
    static int n, m;
    static int[][] board;

    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        board = new int[n][m];
        for (int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for (int y = 0; y < m; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }
        solution();
    }

    /**
     * N x M 격자
     * - 각 칸에는 채취할 수 있는 해산물의 양
     * <p>
     * 총 Q명의 사람 ( 1 ~ Q ) 순서대로 해산물을 채취하러 들어감
     * i 번재 사람은 멘헤튼 거리 d 이하의 격자 중 해산물이 가장 많은 격자로 이동
     * - 여러개인 경우, 행 - 열 작은 순
     * - 해당 격자에 해산물이 K만큼 있다면 K/2만큼 남기고 채취
     * <p>
     * i번째 사람이 채취를 마친 후 해류 발생 -> 일부 영역 해산물 위치 바뀜
     * - 시계 방향 / 반시계 방향
     */
    static void solution() throws IOException {
        int q = Integer.parseInt(br.readLine().trim());
        for (int i = 1; i <= 2 * q; i++) {
            st = new StringTokenizer(br.readLine().trim());
            if (i % 2 == 0) {      // 해류
                int x1 = Integer.parseInt(st.nextToken()) - 1;
                int y1 = Integer.parseInt(st.nextToken()) - 1;
                int x2 = Integer.parseInt(st.nextToken()) - 1;
                int y2 = Integer.parseInt(st.nextToken()) - 1;
                int a = Integer.parseInt(st.nextToken());

                rotate(x1, y1, x2, y2, a);
            } else {            // 채취
                int x = Integer.parseInt(st.nextToken()) - 1;
                int y = Integer.parseInt(st.nextToken()) - 1;
                int d = Integer.parseInt(st.nextToken());
                int takeV = take(x, y, d);
                sb.append(takeV).append('\n');
            }
        }
    }

    static int take(int x, int y, int d) {
        int bestV = board[x][y];
        int bestX = x;
        int bestY = y;

        for (int i = Math.max(0, x - d); i < Math.min(n, x + d + 1); i++) {
            for (int j = Math.max(0, y - d); j < Math.min(m, y + d + 1); j++) {
                if (getDist(x, y, i, j) > d) continue;

                if (bestV < board[i][j]) {
                    bestV = board[i][j];
                    bestX = i;
                    bestY = j;
                } else if (bestV == board[i][j]) {
                    if (bestX > i || (bestX == i && bestY > j)) {
                        bestX = i;
                        bestY = j;
                    }
                }
            }
        }

        board[bestX][bestY] = bestV / 2;
        return bestV - board[bestX][bestY];
    }

    static void rotate(int x1, int y1, int x2, int y2, int a) {
        int h = x2 - x1 + 1;
        int w = y2 - y1 + 1;
        int len = 2 * (h + w) - 4;

        int id = 0;
        int[] origin = new int[len];
        // 위
        for (int y = y1; y <= y2; y++) origin[id++] = board[x1][y];
        // 오른쪽
        for (int x = x1 + 1; x <= x2; x++) origin[id++] = board[x][y2];
        // 아래
        for (int y = y2 - 1; y >= y1; y--) origin[id++] = board[x2][y];
        // 왼쪽
        for (int x = x2 - 1; x > x1; x--) origin[id++] = board[x][y1];

        a %= len;
        if(a < 0) a += len;
        int start = (len-a)%len;
        id = 0;
        // 위
        for (int y = y1; y <= y2; y++) board[x1][y] = origin[(start + id++)%len];
        // 오른쪽
        for (int x = x1 + 1; x <= x2; x++) board[x][y2] = origin[(start + id++)%len];
        // 아래
        for (int y = y2 - 1; y >= y1; y--) board[x2][y] = origin[(start + id++)%len];
        // 왼쪽
        for (int x = x2 - 1; x > x1; x--) board[x][y1] = origin[(start + id++)%len];
    }

    //----------------------------------------------------------------------
    static int getDist(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}

/*
[input]
4 5
2 3 9 7 8
4 2 4 5 4
7 1 6 4 3
1 3 11 3 11
2
2 2 1
2 2 4 5 -2
3 3 2
1 1 3 3 1

[output]
2
5
----
[input]
5 7
8 10 27 34 40 7 8
25 10 25 10 39 43 2
11 25 17 51 51 49 38
13 12 14 15 48 35 99
77 68 2 3 40 37 56
3
3 5 2
2 1 4 4 -3
3 2 1
1 1 2 2 1
1 1 1
1 1 5 7 100

[output]
26
13
6
 */