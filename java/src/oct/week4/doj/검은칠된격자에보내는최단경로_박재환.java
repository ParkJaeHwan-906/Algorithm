
package oct.week4.doj;

import java.util.*;
import java.io.*;

public class 검은칠된격자에보내는최단경로_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int INF = 1_000_000_000;

    static final int[] dx = {-1, 1, 0, 0};
    static final int[] dy = {0, 0, -1, 1};

    static int n, m;
    static char[][] board;
    static int sx, sy, ex, ey;
    static int targetDist;
    static int[][] rank;
    static int dotCount;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        board = new char[n][m];
        rank = new int[n][m];
        for(int x = 0; x < n; x++) {
            String line = br.readLine().trim();
            for(int y = 0; y < m; y++) {
                board[x][y] = line.charAt(y);
                if(board[x][y] == '.') {
                    rank[x][y] = dotCount++;
                }
            }
        }
        st = new StringTokenizer(br.readLine().trim());
        sx = Integer.parseInt(st.nextToken()) - 1;
        sy = Integer.parseInt(st.nextToken()) - 1;
        ex = Integer.parseInt(st.nextToken()) - 1;
        ey = Integer.parseInt(st.nextToken()) - 1;
        targetDist = Integer.parseInt(br.readLine().trim());
        solution();
    }

    static void solution() {
        int maxT = dotCount * 9;
        int maxDist = findRoute(0);
        int minDist = findRoute(maxT);

        if(minDist == INF || targetDist < minDist || targetDist > maxDist) {
            System.out.println(-1);
            return;
        }

        int left = 0;
        int right = maxT;

        while(left < right) {
            int mid = (left + right) / 2;
            int currentDist = findRoute(mid);

            if(currentDist <= targetDist) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        printAnswer(left);
    }

    static int getWeight(int x, int y, int full, int remain) {
        char c = board[x][y];
        if(c == '#') return -1;
        if(c != '.') return c - '0';
        int order = rank[x][y];
        if(order < full) return 0;
        if(order == full) return 9 - remain;
        return 9;
    }

    static int findRoute(int t) {
        int full = t / 9;
        int remain = t % 9;
        int[][] dist = new int[n][m];
        for(int[] row : dist)
            Arrays.fill(row, INF);
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[2], b[2]));
        int startWeight = getWeight(sx, sy, full, remain);
        dist[sx][sy] = startWeight;
        pq.offer(new int[]{sx, sy, startWeight});
        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int x = cur[0];
            int y = cur[1];
            int cost = cur[2];
            if(cost > dist[x][y]) continue;
            if(x == ex && y == ey) return cost;
            for(int d = 0; d < 4; d++) {
                int nx = x + dx[d];
                int ny = y + dy[d];
                if(nx < 0 || nx >= n || ny < 0 || ny >= m) continue;
                if(board[nx][ny] == '#') continue;
                int nextCost = cost + getWeight(nx, ny, full, remain);
                if(nextCost >= dist[nx][ny]) continue;
                dist[nx][ny] = nextCost;
                pq.offer(new int[]{nx, ny, nextCost});
            }
        }
        return INF;
    }

    static void printAnswer(int t) {
        int full = t / 9;
        int remain = t % 9;
        StringBuilder sb = new StringBuilder();
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                if(board[x][y] == '.') {
                    int weight = getWeight(x, y, full, remain);
                    sb.append(weight);
                } else {
                    sb.append(board[x][y]);
                }
            }
            sb.append('\n');
        }
        System.out.print(sb);
    }
}
