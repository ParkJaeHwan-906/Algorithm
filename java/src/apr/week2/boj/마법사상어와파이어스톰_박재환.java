package apr.week2.boj;

import java.util.*;
import java.io.*;

public class 마법사상어와파이어스톰_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static StringTokenizer st;
    static int n, q;
    static int size;
    static int[][] board;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());

        size = 1;
        for(int i = 0; i < n; i++) {
            size *= 2;
        }
        board = new int[size][size];
        for(int x = 0; x < size; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < size; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
            }
        }

        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < q; i++) {
            int l = Integer.parseInt(st.nextToken());
            int lsize = 1;
            for(int j = 0; j < l; j++) lsize *= 2;
            solution(lsize);
        }

        int sum = getSum();
        int maxSize = getMaxSize();
        sb.append(sum).append('\n').append(maxSize).append('\n');
    }

    static void solution(int l) {
        // 1. 격자 회전
        for(int x = 0; x < board.length; x+=l) {
            for(int y = 0; y < board[x].length; y+=l) {
                rotateGrid(x, y, l);
            }
        }

        // 2. 얼음 녹이기
        meltIce();
    }
    static void rotateGrid(int x, int y, int l) {
        int[][] origin = new int[l][l];
        for(int i = x; i < (x + l); i++) {
            for(int j = y; j < (y + l); j++) {
                origin[i - x][j - y] = board[i][j];
            }
        }

        // 회전
        int[][] temp = new int[l][l];
        for(int i = 0; i < l; i++) {
            for(int j = 0; j < l; j++) {
                temp[j][l - 1 - i] = origin[i][j];
            }
        }
        // 반영
        for(int i = x; i < (x + l); i++) {
            for(int j = y; j < (y + l); j++) {
                board[i][j] = temp[i - x][j - y];
            }
        }
    }
    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};
    static void meltIce() {
        boolean[][] melt = new boolean[size][size];
        for(int x = 0; x < board.length; x++) {
            for(int y = 0; y < board[x].length; y++) {
                if(board[x][y] == 0) continue;
                int iceCount = 0;
                for(int dir = 0; dir < 4; dir++) {
                    int nx = x + dx[dir];
                    int ny = y + dy[dir];
                    if(isNotBoard(nx, ny)) continue;
                    if(board[nx][ny] == 0) continue;
                    iceCount++;
                }

                if(iceCount < 3) melt[x][y] = true;
            }
        }

        for(int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if(melt[x][y]) board[x][y]--;
            }
        }
    }
    static int getSum() {
        int sum = 0;
        for(int x = 0; x < board.length; x++) {
            for(int y = 0; y < board[x].length; y++) {
                sum += board[x][y];
            }
        }
        return sum;
    }
    static int getMaxSize() {
        int maxSize = 0;
        boolean[][] visited = new boolean[size][size];
        for(int x = 0; x < board.length; x++) {
            for(int y = 0; y < board[x].length; y++) {
                if(visited[x][y] || board[x][y] == 0) continue;
                int size = findGroup(x, y, visited);
                maxSize = Math.max(size, maxSize);
            }
        }
        return maxSize;
    }
    static int findGroup(int x, int y, boolean[][] visited) {
        Queue<int[]> q = new ArrayDeque<>();

        int size = 1;
        q.offer(new int[] {x, y});
        visited[x][y] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();

            for(int dir = 0; dir < 4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(isNotBoard(nx, ny)) continue;
                if(board[nx][ny] == 0) continue;
                if(visited[nx][ny]) continue;

                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny});
                size++;
            }
        }
        return size;
    }
    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= size || y >= size;
    }
}

