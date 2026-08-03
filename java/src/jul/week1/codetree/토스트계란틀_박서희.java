package jul.week1.codetree;

import java.util.*;
import java.io.*;

/*
  문제풀이 시간: 20분 정도
  AI 사용 여부: X
  이전에 백준에서 거의 유사한 문제를 풀었어서 금방 품.
 */
public class 토스트계란틀_박서희 {

    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int L = Integer.parseInt(st.nextToken());
        int R = Integer.parseInt(st.nextToken());

        int[][] board = new int[n][n];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        int cnt = 0;
        while (true) {
            boolean isChange = false;
            boolean[][] visited = new boolean[n][n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (visited[i][j]) continue;

                    Queue<int[]> queue = new ArrayDeque<>();
                    List<int[]> list = new ArrayList<>();
                    int sum = 0;
                    queue.offer(new int[]{i, j});
                    list.add(new int[]{i, j});
                    visited[i][j] = true;

                    while (!queue.isEmpty()) {
                        int[] cur = queue.poll();
                        int curV = board[cur[0]][cur[1]];
                        sum += curV;
                        for (int d = 0; d < 4; d++) {
                            int nx = cur[0] + dx[d];
                            int ny = cur[1] + dy[d];

                            if (!inRange(nx, ny, n)) continue;
                            if (visited[nx][ny]) continue;
                            int diff = Math.abs(curV - board[nx][ny]);
                            if (diff >= L && diff <= R) {
                                list.add(new int[]{nx, ny});
                                visited[nx][ny] = true;
                                queue.offer(new int[]{nx, ny});
                            }
                        }
                    }

                    if (list.size() >= 2) isChange = true;
                    int nextV = sum / list.size();
                    for (int[] pos : list) {
                        board[pos[0]][pos[1]] = nextV;
                    }
                }
            }
            if (!isChange) break;
            cnt++;
        }

        System.out.println(cnt);
    }

    private static boolean inRange(int x, int y, int n) {
        return 0 <= x && x < n && 0 <= y && y < n;
    }
}

