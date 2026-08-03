package jun.week3.ngv;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 00:18:52
  AI 사용 여부: X
 */
public class 나무조경_박서희 {
    static int N;
    static int[][] board;
    static int[][] visited;
    static int answer = 0;

    static int[] dx = {0, -1, 0, 1};
    static int[] dy = {1, 0, -1, 0};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        board = new int[N][N];
        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        visited = new int[N][N];

        dfs(0, 0);

        System.out.println(answer);

    }

    static void dfs(int depth, int beauty) {
        answer = Math.max(answer, beauty);

        if (depth == 4) {
            return;
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (visited[i][j] == 1) continue;
                visited[i][j] = 1;
                for (int d = 0; d < 4; d++) {
                    int pairX = i + dx[d], pairY = j + dy[d];
                    if (!inRange(pairX, pairY)) continue;
                    if (visited[pairX][pairY] == 1) continue;
                    visited[pairX][pairY] = 1;
                    dfs(depth + 1, beauty + board[i][j] + board[pairX][pairY]);
                    visited[pairX][pairY] = 0;
                }
                visited[i][j] = 0;
            }
        }
    }

    static boolean inRange(int x, int y) {
        return 0 <= x && x < N && 0 <= y && y < N;
    }
}
