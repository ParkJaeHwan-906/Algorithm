package jun.week1.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 1:09:16
  AI 사용 여부: O -> 회전할 때 반복문 인덱스 도움 받음.😵‍💫
 */
public class 시공의돌풍_박서희 {
    static int n, m, t;
    static int[][] grid;

    static int tornadoRow = -1;

    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        t = Integer.parseInt(st.nextToken());

        grid = new int[n][m];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < m; j++) {
                grid[i][j] = Integer.parseInt(st.nextToken());
                if (grid[i][j] == -1 && tornadoRow == -1) {
                    tornadoRow = i;
                }
            }
        }

        while (t-- > 0) {
            moveDust(); // 먼지 확산
            clean();    // 청소
        }

        System.out.println(getTotalDust());
    }


    static void moveDust() {
        int[][] nextGrid = new int[n][m];
        // 돌풍 위치
        nextGrid[tornadoRow][0] = -1;
        nextGrid[tornadoRow + 1][0] = -1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] <= 0) continue;

                int move = grid[i][j] / 5;
                int cnt = 0;

                for (int d = 0; d < 4; d++) {
                    int nx = i + dx[d], ny = j + dy[d];

                    if (!inRange(nx, ny)) continue;
                    if (grid[nx][ny] == -1) continue;

                    nextGrid[nx][ny] += move;
                    cnt++;
                }
                nextGrid[i][j] += (grid[i][j] - (move * cnt));
            }
        }
        grid = nextGrid;
    }


    // 위쪽 돌풍 - 반시계, 아래쪽 돌풍 - 시계
    static void clean() {

        // 위쪽 돌풍
        // ⬇️
        for (int i = tornadoRow - 1; i > 0; i--) grid[i][0] = grid[i - 1][0];
        // ⬅️
        for (int i = 0; i < m - 1; i++) grid[0][i] = grid[0][i + 1];
        // ⬆️
        for (int i = 0; i < tornadoRow; i++) grid[i][m - 1] = grid[i + 1][m - 1];
        // ➡️
        for (int i = m - 1; i > 1; i--) grid[tornadoRow][i] = grid[tornadoRow][i - 1];
        grid[tornadoRow][1] = 0;

        // 아래쪽 돌풍
        // ⬆️
        for (int i = tornadoRow + 1 + 1; i < n - 1; i++) grid[i][0] = grid[i + 1][0];
        // ⬅️
        for (int i = 0; i < m - 1; i++) grid[n - 1][i] = grid[n - 1][i + 1];
        // ⬇️
        for (int i = n - 1; i > tornadoRow + 1; i--) grid[i][m - 1] = grid[i - 1][m - 1];
        // ➡️
        for (int i = m - 1; i > 1; i--) grid[tornadoRow + 1][i] = grid[tornadoRow + 1][i - 1];
        grid[tornadoRow + 1][1] = 0;

    }


    static int getTotalDust() {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] > 0) {
                    sum += grid[i][j];
                }
            }
        }
        return sum;
    }

    static boolean inRange(int x, int y) {
        return 0 <= x && x < n && 0 <= y && y < m;
    }
}
