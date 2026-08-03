package jun.week2.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 00:51:03
  AI 사용 여부: X
 */
public class 바이러스백신_박서희 {

    static int N, M;
    static int answer = Integer.MAX_VALUE;
    static int[][] board;
    static int[] selected;
    static ArrayList<int[]> hospital = new ArrayList<>();

    static final int[] dx = {0, -1, 0, 1};
    static final int[] dy = {1, 0, -1, 0};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        // 0은 바이러스, 1은 벽, 2는 병원
        board = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
                if (board[i][j] == 2) hospital.add(new int[]{i, j});
            }
        }

        selected = new int[hospital.size()];

        solution(0, 0);

        System.out.println(answer == Integer.MAX_VALUE ? -1 : answer);

    }

    static void solution(int idx, int cnt) {
        if (cnt == M) {
            int[][] dist = new int[N][N];
            for (int i = 0; i < N; i++) {
                Arrays.fill(dist[i], -1);
            }

            Queue<int[]> q = new LinkedList<>();
            for (int i = 0; i < selected.length; i++) {
                if (selected[i] == 0) continue;
                int x = hospital.get(i)[0], y = hospital.get(i)[1];
                q.add(new int[]{x, y});
                dist[x][y] = 0;
            }

            while (!q.isEmpty()) {
                int[] cur = q.poll();
                int x = cur[0], y = cur[1];

                for (int d = 0; d < 4; d++) {
                    int nx = x + dx[d];
                    int ny = y + dy[d];

                    if (!inRange(nx, ny)) continue;
                    if (dist[nx][ny] != -1) continue;
                    if (board[nx][ny] == 1) continue;

                    if (board[nx][ny] == 0) {
                        dist[nx][ny] = dist[x][y] + 1;
                        q.add(new int[]{nx, ny});
                    } else if (board[nx][ny] == 2 && dist[nx][ny] != 0) {
                        dist[nx][ny] = dist[x][y] + 1;
                        q.add(new int[]{nx, ny});
                    }
                }
            }

            int distMax = 0;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (board[i][j] == 0 && dist[i][j] == -1)
                        return;
                    if (board[i][j] == 0)
                        distMax = Math.max(distMax, dist[i][j]);
                }
            }
            answer = Math.min(answer, distMax);
        }

        for (int i = idx; i < hospital.size(); i++) {
            selected[i] = 1;
            solution(i + 1, cnt + 1);
            selected[i] = 0;
        }
    }

    static boolean inRange(int x, int y) {
        return 0 <= x && x < N && 0 <= y && y < N;
    }
}
