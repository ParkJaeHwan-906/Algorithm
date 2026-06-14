package jun.week1.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 이틀..?
  AI 사용 여부: OOO AI 없으면 못 풀었다..일단 3차원끼리 이동 매핑도 눈 빠지는줄 알았다.. 그리고 turn 마다 이상현상 일으키는 부분도 어려워서 도움을 받았다.
  이상 현상이 시간의 벽을 타고 확산되는 과정이 있을수도 생각해서 아주 복잡하게 생각했는데 토론 탭에서 그 부분은 생각하지 않아도 된다고 한다.
  시간의 벽에서 평면으로 내려올 수 있는 출구가 하나라 여기가 막히면 영영 탈출할 수 없어서 평면 상에서 확산만 생각하면 된다고 한다.
 */
public class 미지의공간탈출_박서희 {
    static final int EAST = 0, WEST = 1, SOUTH = 2, NORTH = 3, TOP = 4, BOTTOM = 5;
    static int[] dx = {0, 0, 1, -1};
    static int[] dy = {1, -1, 0, 0};

    static TM tm;
    static int N, M;
    static int[][] grid;
    static int[][][] grid3D;
    static List<int[]> strange;

    static int[] exit = new int[2];
    static int[] startGrid3D = new int[]{-1, -1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        int F = Integer.parseInt(st.nextToken());

        grid = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                grid[i][j] = Integer.parseInt(st.nextToken());
                if (grid[i][j] == 4) exit = new int[]{i, j};
                if (grid[i][j] == 3 && startGrid3D[0] == -1) {
                    startGrid3D = new int[]{i, j};
                }
            }
        }

        grid3D = new int[5][M][M];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < M; j++) {
                st = new StringTokenizer(br.readLine());
                for (int k = 0; k < M; k++) {
                    grid3D[i][j][k] = Integer.parseInt(st.nextToken());
                    if (grid3D[i][j][k] == 2) tm = new TM(j, k, TOP);
                }
            }
        }

        strange = new ArrayList<>();
        for (int i = 0; i < F; i++) {
            int[] temp = new int[5];
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 4; j++) {
                temp[j] = Integer.parseInt(st.nextToken());
            }
            temp[4] = BOTTOM;
            strange.add(temp);
            grid[temp[0]][temp[1]] = 9;
        }

        bfs();
    }

    static int[] move3D(int currentBoard, int r, int c, int dir) {
        int nextR = r + dx[dir];
        int nextC = c + dy[dir];

        if (inRange(nextR, nextC, M, M)) {
            return new int[]{currentBoard, nextR, nextC};
        }

        switch (currentBoard) {
            case TOP:
                if (dir == NORTH) return new int[]{NORTH, 0, M - 1 - c};
                if (dir == SOUTH) return new int[]{SOUTH, 0, c};
                if (dir == WEST) return new int[]{WEST, 0, r};
                if (dir == EAST) return new int[]{EAST, 0, M - 1 - r};
                break;

            case EAST:
                if (dir == NORTH) return new int[]{TOP, M - 1 - c, M - 1};
                if (dir == SOUTH) return new int[]{BOTTOM, startGrid3D[0] + M - 1 - c, startGrid3D[1] + M};
                if (dir == WEST) return new int[]{SOUTH, r, M - 1};
                if (dir == EAST) return new int[]{NORTH, r, 0};
                break;

            case WEST:
                if (dir == NORTH) return new int[]{TOP, c, 0};
                if (dir == SOUTH) return new int[]{BOTTOM, startGrid3D[0] + c, startGrid3D[1] - 1};
                if (dir == WEST) return new int[]{NORTH, r, M - 1};
                if (dir == EAST) return new int[]{SOUTH, r, 0};
                break;

            case SOUTH:
                if (dir == NORTH) return new int[]{TOP, M - 1, c};
                if (dir == SOUTH) return new int[]{BOTTOM, startGrid3D[0] + M, startGrid3D[1] + c};
                if (dir == WEST) return new int[]{WEST, r, M - 1};
                if (dir == EAST) return new int[]{EAST, r, 0};
                break;

            case NORTH:
                if (dir == NORTH) return new int[]{TOP, 0, M - 1 - c};
                if (dir == SOUTH) return new int[]{BOTTOM, startGrid3D[0] - 1, startGrid3D[1] + M - 1 - c};
                if (dir == WEST) return new int[]{EAST, r, M - 1};
                if (dir == EAST) return new int[]{WEST, r, 0};
                break;
        }

        return null;
    }

    static void moveStrange(List<int[]> strange, int turn) {
        for (int[] s : strange) {
            int r = s[0];
            int c = s[1];
            int dir = s[2];
            int v = s[3];
            int board = s[4];

            if (turn % v == 0) {
                if (board == BOTTOM) {
                    int nextR = r + dx[dir];
                    int nextC = c + dy[dir];

                    if (!inRange(nextR, nextC, N, N)) continue;
                    if (grid[nextR][nextC] == 4) continue;

                    if (dir == EAST && nextC == startGrid3D[1] && nextR >= startGrid3D[0] && nextR < startGrid3D[0] + M) {
                        s[4] = WEST;
                        s[0] = M - 1;
                        s[1] = nextR - startGrid3D[0];
                        grid3D[s[4]][s[0]][s[1]] = 9;
                    } else if (dir == NORTH && nextR == startGrid3D[0] + M - 1 && nextC >= startGrid3D[1] && nextC < startGrid3D[1] + M) {
                        s[4] = SOUTH;
                        s[0] = M - 1;
                        s[1] = nextC - startGrid3D[1];
                        grid3D[s[4]][s[0]][s[1]] = 9;
                    } else if (dir == SOUTH && nextR == startGrid3D[0] && nextC >= startGrid3D[1] && nextC < startGrid3D[1] + M) {
                        s[4] = NORTH;
                        s[0] = M - 1;
                        s[1] = startGrid3D[1] + M - 1 - nextC;
                        grid3D[s[4]][s[0]][s[1]] = 9;
                    } else if (dir == WEST && nextC == startGrid3D[1] + M - 1 && nextR >= startGrid3D[0] && nextR < startGrid3D[0] + M) {
                        s[4] = EAST;
                        s[0] = M - 1;
                        s[1] = startGrid3D[0] + M - 1 - nextR;
                        grid3D[s[4]][s[0]][s[1]] = 9;
                    } else {
                        s[0] = nextR;
                        s[1] = nextC;
                        grid[s[0]][s[1]] = 9;
                    }
                } else {
                    int[] next = move3D(board, r, c, dir);

                    if (next != null) {
                        if (next[0] == BOTTOM && grid[next[1]][next[2]] == 4) continue;
                        s[4] = next[0];
                        s[0] = next[1];
                        s[1] = next[2];

                        if (s[4] == BOTTOM) {
                            grid[s[0]][s[1]] = 9;
                        } else {
                            grid3D[s[4]][s[0]][s[1]] = 9;
                        }
                    }
                }
            }
        }
    }

    static void bfs() {
        Queue<int[]> q = new LinkedList<>();

        boolean[][][] visited3D = new boolean[5][M][M];
        boolean[][] visitedGrid = new boolean[N][N];

        q.add(new int[]{tm.board, tm.r, tm.c, 0});
        visited3D[tm.board][tm.r][tm.c] = true;

        int lastUpdatedTime = -1;

        while (!q.isEmpty()) {
            int[] curr = q.poll();
            int cb = curr[0];
            int cr = curr[1];
            int cc = curr[2];
            int cTime = curr[3];

            if (cb == BOTTOM && cr == exit[0] && cc == exit[1]) {
                System.out.println(cTime);
                return;
            }

            if (lastUpdatedTime < cTime) {
                moveStrange(strange, cTime + 1);
                lastUpdatedTime = cTime;
            }

            for (int i = 0; i < 4; i++) {
                if (cb == BOTTOM) {
                    int nr = cr + dx[i];
                    int nc = cc + dy[i];

                    if (!inRange(nr, nc, N, N)) continue;

                    if (i == EAST && nc == startGrid3D[1] && nr >= startGrid3D[0] && nr < startGrid3D[0] + M) {
                        int nb = WEST;
                        int nr3 = M - 1;
                        int nc3 = nr - startGrid3D[0];
                        if (!visited3D[nb][nr3][nc3] && grid3D[nb][nr3][nc3] != 1 && grid3D[nb][nr3][nc3] != 9) {
                            visited3D[nb][nr3][nc3] = true;
                            q.add(new int[]{nb, nr3, nc3, cTime + 1});
                        }
                    } else if (i == NORTH && nr == startGrid3D[0] + M - 1 && nc >= startGrid3D[1] && nc < startGrid3D[1] + M) {
                        int nb = SOUTH;
                        int nr3 = M - 1;
                        int nc3 = nc - startGrid3D[1];
                        if (!visited3D[nb][nr3][nc3] && grid3D[nb][nr3][nc3] != 1 && grid3D[nb][nr3][nc3] != 9) {
                            visited3D[nb][nr3][nc3] = true;
                            q.add(new int[]{nb, nr3, nc3, cTime + 1});
                        }
                    } else if (i == SOUTH && nr == startGrid3D[0] && nc >= startGrid3D[1] && nc < startGrid3D[1] + M) {
                        int nb = NORTH;
                        int nr3 = M - 1;
                        int nc3 = M - 1 - nc + startGrid3D[1];
                        if (!visited3D[nb][nr3][nc3] && grid3D[nb][nr3][nc3] != 1 && grid3D[nb][nr3][nc3] != 9) {
                            visited3D[nb][nr3][nc3] = true;
                            q.add(new int[]{nb, nr3, nc3, cTime + 1});
                        }
                    } else if (i == WEST && nc == startGrid3D[1] + M - 1 && nr >= startGrid3D[0] && nr < startGrid3D[0] + M) {
                        int nb = EAST;
                        int nr3 = M - 1;
                        int nc3 = startGrid3D[0] + M - 1 - nr;
                        if (!visited3D[nb][nr3][nc3] && grid3D[nb][nr3][nc3] != 1 && grid3D[nb][nr3][nc3] != 9) {
                            visited3D[nb][nr3][nc3] = true;
                            q.add(new int[]{nb, nr3, nc3, cTime + 1});
                        }
                    } else {
                        if (grid[nr][nc] != 1 && grid[nr][nc] != 9 && grid[nr][nc] != 3 && !visitedGrid[nr][nc]) {
                            visitedGrid[nr][nc] = true;
                            q.add(new int[]{BOTTOM, nr, nc, cTime + 1});
                        }
                    }
                } else {
                    int[] next = move3D(cb, cr, cc, i);
                    if (next == null) continue;

                    int nb = next[0];
                    int nr = next[1];
                    int nc = next[2];

                    if (nb == BOTTOM) {
                        if (grid[nr][nc] != 1 && grid[nr][nc] != 9 && !visitedGrid[nr][nc]) {
                            visitedGrid[nr][nc] = true;
                            q.add(new int[]{nb, nr, nc, cTime + 1});
                        }
                    } else {
                        if (grid3D[nb][nr][nc] != 1 && grid3D[nb][nr][nc] != 9 && !visited3D[nb][nr][nc]) {
                            visited3D[nb][nr][nc] = true;
                            q.add(new int[]{nb, nr, nc, cTime + 1});
                        }
                    }
                }
            }
        }
        System.out.println(-1);
    }

    static boolean inRange(int x, int y, int sizeX, int sizeY) {
        return 0 <= x && x < sizeX && 0 <= y && y < sizeY;
    }

    static class TM {
        int r, c, board;

        public TM(int r, int c, int board) {
            this.r = r;
            this.c = c;
            this.board = board;
        }
    }
}
