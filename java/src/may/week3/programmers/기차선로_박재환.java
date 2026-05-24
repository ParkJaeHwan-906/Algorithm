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
        this.allCombi = 0;
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
        /**
         * grid
         * 0 : 빈칸
         * 1 ~ 7 : 선로
         * -1 : 장애물
         */
        init(grid);

        // 격자의 1, 1에는 1번 선로가 놓여 있음
        // 즉 (0, 0) -> (0, 1) 이동만 가능
        int[][] installed = new int[n][m];
        installed[0][0] = 1;
        getAllCombi(0, 0, 1, LEFT, installed);

        return allCombi;
    }

    void getAllCombi(int x, int y, int railId, int inDir, int[][] installed) {
        int outDir = rails[railId][inDir];      // inDir 로 들어왔을 때, 나가는 방향

        if(outDir == BLOCKED) return;       // 이동 불가한 방향

        int used = (1 << inDir) | (1 << outDir);            // 사용하는 방향

        if((installed[x][y] & used) == used) return;        // 이미 이전에 방문한 기록이 있음 - 중복

        installed[x][y] |= used;

        // 현 위치가 도착지임
        if (x == n - 1 && y == m - 1) {
            if (isAllSatisfied(installed)) {
                allCombi++;
            }

            installed[x][y] ^= used;
            return;
        }

        // 이동 후 위치
        int nx = x + dx[outDir];
        int ny = y + dy[outDir];

        if (!isNotBoard(nx, ny) && grid[nx][ny] != BLOCKED) {
            int nextInDir = opposite(outDir);

            if (grid[nx][ny] > 0) {     // 이미 선로가 놓여있는 경우
                int nextRailId = grid[nx][ny];

                if (rails[nextRailId][nextInDir] != BLOCKED) {
                    getAllCombi(nx, ny, nextRailId, nextInDir, installed);
                }
            }

            else if (grid[nx][ny] == 0) {
                for (int id = 1; id <= 7; id++) {
                    if (rails[id][nextInDir] == BLOCKED) {
                        continue;
                    }

                    grid[nx][ny] = id;
                    getAllCombi(nx, ny, id, nextInDir, installed);
                    grid[nx][ny] = 0;
                }
            }
        }

        installed[x][y] ^= used;
    }

    boolean isAllSatisfied(int[][] installed) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int railId = grid[i][j];

                if (railId <= 0) {
                    continue;
                }

                int needMask = getNeedMask(railId);

                if ((installed[i][j] & needMask) != needMask) {
                    return false;
                }
            }
        }

        return true;
    }

    int getNeedMask(int railId) {
        int mask = 0;

        for (int dir = 0; dir < 4; dir++) {
            if (rails[railId][dir] != BLOCKED) {
                mask |= (1 << dir);
            }
        }

        return mask;
    }

    int opposite(int dir) {
        return (dir + 2) % 4;
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