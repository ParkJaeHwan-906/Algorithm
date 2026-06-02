package jun.week1.ngv;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:22:48
 * AI 사용 여부 x
 */
public class 순서대로방문하기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n, m;
    static int[][] board;
    static int[][] goals;
    static Set<Integer> keys;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
            }
        }

        goals = new int[m][];
        keys = new HashSet<>();
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            goals[i] = new int[] {x, y};
            keys.add(convertKey(x, y));
        }
        System.out.println(solution());
    }
    /**
     * n x n
     * - 0 : 빈 칸
     * - 1 : 벽
     *
     * m 개의 지점을 순서대로 방문
     * - 이동 : 상 하 좌 우
     * - 재방문 X
     *
     * [조건]
     * n : 최대 4
     * m : 최대 16
     */
    static int result;
    static int solution() {
        boolean[][] visited = new boolean[n][n];
        result = 0;
        visited[goals[0][0]][goals[0][1]] = true;
        findRoute(goals[0][0], goals[0][1], 1, visited);
        return result;
    }

    static int[] dx = {0, 1, 0, -1};
    static int[] dy = {1, 0, -1, 0};
    static void findRoute(int x, int y, int goalId, boolean[][] visited) {
        if(goalId == m) {       // 모든 목적지 방문 완료
            result++;
            return;
        }

        for(int dir = 0; dir < 4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];

            if(isNotBoard(nx, ny)) continue;
            if(board[nx][ny] == 1) continue;
            if(visited[nx][ny]) continue;

            visited[nx][ny] = true;
            if(goals[goalId][0] == nx && goals[goalId][1] == ny) {
                findRoute(nx, ny, goalId + 1, visited);
            } else if(!keys.contains(convertKey(nx, ny))) {
                findRoute(nx, ny, goalId, visited);
            }
            visited[nx][ny] = false;
        }
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }

    static int convertKey(int x, int y) {
        return x * n + y;
    }
}
