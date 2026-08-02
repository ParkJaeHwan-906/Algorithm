package jul.week4.codetree;

import java.util.*;
import java.io.*;

public class 테트리스블럭안의합최대화하기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int n, m;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        board = new int[n][m];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < m; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }
        System.out.println(solution());
    }

    static int maxScore;
    static int solution() {
        maxScore = 0;
        boolean[][] visited = new boolean[n][m];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                visited[x][y] = true;
                findMaxScore(x, y, board[x][y], 1, visited);
                visited[x][y] = false;
            }
        }
        return maxScore;
    }

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};
    static void findMaxScore(int x, int y, int score, int blocks, boolean[][] visited) {
        if(blocks == 4) {
            if(maxScore < score) {
                maxScore = score;
//                for(boolean[] arr : visited) System.out.println(Arrays.toString(arr));
//                System.out.println();
            }
            return;
        }

        for(int dir = 0; dir < 3; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if(isNotBoard(nx, ny)) continue;
            if(visited[nx][ny]) continue;

            visited[nx][ny] = true;
            if(blocks == 2) {   // T 자 모형 만들기
                findMaxScore(x, y, score + board[nx][ny], blocks + 1, visited);
            }
            findMaxScore(nx, ny, score + board[nx][ny], blocks + 1, visited);
            visited[nx][ny] = false;
        }
    }

    static boolean isNotBoard(int x, int y) { return x < 0 || y < 0 || x >= n || y >= m; };
}
