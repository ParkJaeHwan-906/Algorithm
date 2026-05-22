package may.week3.programmers;

import java.util.*;
import java.io.*;

public class 기차선로_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static void init(BufferedReader br) throws IOException {
        int n = Integer.parseInt(br.readLine().trim());
        int m = Integer.parseInt(br.readLine().trim());

        StringTokenizer st;
        int[][] grid = new int[n][m];
        for(int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int j = 0; j < m; j++) {
                grid[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        Solution solution = new Solution();
        System.out.println(solution.solution(grid));
    }
}

class Solution {

    void init(int[][] grid) {
        this.n = grid.length;
        this.m = grid[0].length;
        this.grid = grid;
    }

    final int BLOCKED = -1;
    final int TOP = 0;
    final int LEFT = 1;
    final int BOTTOM = 2;
    final int RIGHT = 3;

    final int[] dx = {-1, 0, 1, 0};
    final int[] dy = {0, -1, 0, 1};

    final int[][] rails = {
            {},
            {BLOCKED, RIGHT, BLOCKED, LEFT},          // 1
            {BOTTOM, BLOCKED, TOP, BLOCKED},          // 2
            {BOTTOM, RIGHT, TOP, LEFT},               // 3
            {LEFT, TOP, BLOCKED, BLOCKED},            // 4
            {RIGHT, BLOCKED, BLOCKED, TOP},           // 5
            {BLOCKED, BLOCKED, RIGHT, BOTTOM},        // 6
            {BLOCKED, BOTTOM, LEFT, BLOCKED}          // 7
    };

    int n, m;
    int[][] grid;
    int allCombi;
    public int solution(int[][] grid) {
        allCombi = 0;
        /**
         * grid
         * 0 : 빈칸
         * 1 ~ 7 : 선로
         * -1 : 장애물
         */
        init(grid);

        // 격자의 1, 1에는 1번 선로가 놓여 있음
        // 즉 (0, 0) -> (0, 1) 이동만 가능
        boolean[][] installed = new boolean[n][m];
        installed[0][0] = true;
        getAllCombi(0, 0, 1, LEFT, installed);

        return allCombi;
    }

    void getAllCombi(int x, int y, int railId, int inDir, boolean[][] installed) {
        if(x == n - 1 && y == m - 1) {
            allCombi++;
            return;
        }

        // 현 위치에서 이동 가능한 방향
        for(int nextDir : rails[railId]) {
            if(nextDir == inDir) continue;      // 무한루프 방지
            if(nextDir == -1) continue;         // 이동할 수 없는 방향

            int nx = x + dx[nextDir];
            int ny = y + dy[nextDir];

            // 격자 밖 또는 장애물이 있는 또는 이미 설치된 선로가 있는 경우
            if(isNotBoard(nx, ny)) continue;
            if(grid[nx][ny] == -1) continue;
            if(installed[nx][ny]) continue;

            installed[nx][ny] = true;
            // 철로를 설치할 수 있음
            for(int id = 1; id < 8; id++) {
                int[] rail = rails[id];
                if(rail[(nextDir + 2) % 4] == BLOCKED) continue;      // 이어질 수 없는 선로

                getAllCombi(nx, ny, id, (nextDir + 2) % 4, installed);
            }
            installed[nx][ny] = false;
        }
    }

    boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= m;
    }
}

/**
3
3
1 0 -1
0 0 7
0 0 2
 */