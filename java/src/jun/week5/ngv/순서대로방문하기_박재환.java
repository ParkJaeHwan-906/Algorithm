package jun.week5.ngv;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:20:12
 * AI 사용 여부 X
 */
public class 순서대로방문하기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n, m;
    static int[][] board;
    static int[][] places;
    static Set<Integer> placeSet;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        places = new int[m][];
        placeSet = new HashSet<>();
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            places[i] = new int[] {x, y};
            placeSet.add(convertKey(x, y));
        }

        System.out.println(solution());
    }

    static int routeCount;
    static int solution() {
        routeCount = 0;
        boolean[][] visited = new boolean[n][n];
        visited[places[0][0]][places[0][1]] = true;
        visit(places[0][0], places[0][1], 1, visited);
        return routeCount;
    }

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    /**
     * 순서가 틀어지는 경우
     */
    static void visit(int x, int y, int pid, boolean[][] visited) {
        if(pid == m) {      // 모든 위치 방문
            routeCount++;
            return;
        }

        for(int dir = 0; dir < 4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if(nx < 0 || nx >= n || ny < 0 || ny >= n) continue;
            if(visited[nx][ny] || board[nx][ny] == 1) continue;

            visited[nx][ny] = true;
            if(nx == places[pid][0] && ny == places[pid][1]) {
                visit(nx, ny, pid + 1, visited);
            } else if(!placeSet.contains(convertKey(nx, ny))) {
                visit(nx, ny, pid, visited);
            }
            visited[nx][ny] = false;
        }
    }

    static int convertKey(int x, int y) {
        return x * (n + 7) + y;
    }
}
