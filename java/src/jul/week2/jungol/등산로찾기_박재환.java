package jul.week2.jungol;

import java.util.*;
import java.io.*;

public class 등산로찾기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static class Loc {
        int x, y;
        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static int n;
    static int tx, ty;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        n = Integer.parseInt(br.readLine().trim());
        st = new StringTokenizer(br.readLine().trim());
        tx = Integer.parseInt(st.nextToken()) - 1;
        ty = Integer.parseInt(st.nextToken()) - 1;

        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n;) board[x][y++] = Integer.parseInt(st.nextToken());
        }
        System.out.println(solution());
    }

    /**
     * N x N 격자가 존재
     * - 각 칸은 해당 위치의 높이
     *
     * 산의 바깥 지역에서 정상까지 도달하기 위한 경제적인 루트를 구함
     * [상 하 좌 우] 이동이 가능하다.
     * - 같은 높이로 이동 : 힘이 들지 않음
     * - 낮은 산으로 이동 : 높이 차 만큼 이히 음
     * - 높은 산으로 이동 : 높이 차 ** 2 만큼 힘이 듬
     */
    static final int INF = Integer.MAX_VALUE;
    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};
    static int solution() {
        // 역으로 top 에서 격자를 벗어나는 경로 찾기
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[2], b[2]));       // 힘이 적게 드는 경로 우선
        int[][] visited = new int[n][n];
        for(int x = 0; x < n;) Arrays.fill(visited[x++], INF);

        // 초기값
        visited[tx][ty] = 0;
        pq.offer(new int[] {tx, ty, 0});
        int max = INF;
        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            if(isExit(cur[0], cur[1])) {
                max = Integer.min(max, cur[2] + (board[cur[0]][cur[1]] * board[cur[0]][cur[1]]));
            }
            if(visited[cur[0]][cur[1]] < cur[2]) continue;
            for(int dir = 0; dir < 4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];

                if(isNotBoard(nx, ny)) continue;        // 격자를 벗어나는 경우

                int prev = board[cur[0]][cur[1]];
                int next = board[nx][ny];

                int tired = cur[2];
                int diff = prev - next;
                if(diff < 0) tired += (-diff);
                else if(diff > 0) tired += (diff * diff);

                if(visited[nx][ny] > tired) {
                    visited[nx][ny] = tired;
                    pq.offer(new int[] {nx, ny, visited[nx][ny]});
                }
            }
        }

        return max;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }

    static boolean isExit(int x, int y) {
        return x == 0 || y == 0 || x == n - 1 || y == n - 1;
    }
}
