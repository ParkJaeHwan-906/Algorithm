package jun.week3.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 00:44:45
  AI 사용 여부: X
 */
public class 개구리의여행_박서희 {

    static int INF = 1000000;
    static int[] dx = {0, 1, 0, -1};
    static int[] dy = {-1, 0, 1, 0};

    static int N;
    static int[][] board;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        board = new int[N][N];
        // .이라면 안전한 돌이, S라면 미끄러운 돌이, #이라면 천적이 사는 돌
        for (int i = 0; i < N; i++) {
            String s = br.readLine();
            for (int j = 0; j < N; j++) {
                if (s.charAt(j) == '.') board[i][j] = 0;
                else if (s.charAt(j) == 'S') board[i][j] = 1;
                else if (s.charAt(j) == '#') board[i][j] = 2;
            }
        }

        int Q = Integer.parseInt(br.readLine());
        StringBuilder sb = new StringBuilder();
        while (Q-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int r1 = Integer.parseInt(st.nextToken()) - 1;
            int c1 = Integer.parseInt(st.nextToken()) - 1;
            int r2 = Integer.parseInt(st.nextToken()) - 1;
            int c2 = Integer.parseInt(st.nextToken()) - 1;
            sb.append(solution(r1, c1, r2, c2)).append("\n");
        }

        System.out.println(sb);
    }

    static int solution(int r1, int c1, int r2, int c2) {
        int[][][] dist = new int[6][N][N];
        for (int jump = 1; jump <= 5; jump++) {
            for (int i = 0; i < N; i++) {
                Arrays.fill(dist[jump][i], INF);
            }
        }

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[3]));

        pq.add(new int[]{r1, c1, 1, 0});
        dist[1][r1][c1] = 0;

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int r = curr[0], c = curr[1], jump = curr[2], time = curr[3];

            if (r == r2 && c == c2) {
                return time;
            }

            if (dist[jump][r][c] < time) continue;

            // 점프
            for (int d = 0; d < 4; d++) {
                int nr = r + dx[d] * jump, nc = c + dy[d] * jump;

                if (!inRange(nr, nc)) continue;
                if (!canJump(r, c, nr, nc, d)) continue;
                if (board[nr][nc] == 1) continue;

                if (time + 1 < dist[jump][nr][nc]) {
                    dist[jump][nr][nc] = time + 1;
                    pq.add(new int[]{nr, nc, jump, time + 1});
                }
            }

            // 점프력 증가
            if (jump < 5) {
                int nj = jump + 1, nt = time + nj * nj;

                if (nt < dist[nj][r][c]) {
                    dist[nj][r][c] = nt;
                    pq.add(new int[]{r, c, nj, nt});
                }
            }

            // 점프력 감소
            for (int j = 1; j < jump; j++) {
                int nt = time + 1;

                if (nt < dist[j][r][c]) {
                    dist[j][r][c] = nt;
                    pq.add(new int[]{r, c, j, nt});
                }
            }

        }

        return -1;
    }

    private static boolean canJump(int r, int c, int nr, int nc, int d) {
        while (r != nr || c != nc) {
            r += dx[d];
            c += dy[d];
            if (board[r][c] == 2) return false;
        }
        return true;
    }

    private static boolean inRange(int x, int y) {
        return 0 <= x && x < N && 0 <= y && y < N;
    }
}
