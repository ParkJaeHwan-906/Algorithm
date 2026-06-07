package jun.week1.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 03:05:57
 * AI 사용 여부 O
 * -> WEST, EAST 를 거꾸로 작성해서 디버깅 할 때 사용
 * -> setTimeBoardExit() 메서드에서 북쪽 좌표 계산 시 y 좌표 거꾸로 계산하는 부분 놓쳐서 디버깅 시 사용
 */
public class 미지의공간탈출_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static class Anormaly {
        int x, y;
        int d;
        int v;

        boolean stop;

        Anormaly(int x, int y, int d, int v) {
            this.x = x;
            this.y = y;
            this.d = d;
            this.v = v;

            this.stop = false;
        }

        void update(int x, int y) {
            this.x = x;
            this.y = y;
        }

        void stop() {
            this.stop = true;
        }
    }

    static final int TOP = 4;
    static final int EAST = 0;
    static final int WEST = 1;
    static final int SOUTH = 2;
    static final int NORTH = 3;

    static int n, m, f;
    static int[][] mijiBoard;
    static int[][][] timeBoard;
    static Anormaly[] anormalies;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        f = Integer.parseInt(st.nextToken());

        mijiBoard = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) mijiBoard[x][y] = Integer.parseInt(st.nextToken());
        }

        timeBoard = new int[5][m][m];
        for(int i = 0; i < 5; i++) {
            // 동 서 남 북 위
            for(int x = 0; x < m; x++) {
                st = new StringTokenizer(br.readLine().trim());
                for(int y = 0; y < m; y++) timeBoard[i][x][y] = Integer.parseInt(st.nextToken());
            }
        }

        anormalies = new Anormaly[f];
        for(int i = 0; i < f; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            Anormaly anormaly = new Anormaly(x, y, d, v);
            anormalies[i] = anormaly;
        }

        int result = solution();
        System.out.printf("%d", result);
    }

    static class TimeMachine {
        int x, y;
        int surface;

        TimeMachine(int x, int y, int surface) {
            this.x = x;
            this.y = y;
            this.surface = surface;
        }
    }

    static class TimeBoardExit {
        int x, y;
        int surface;

        int mx, my;

        TimeBoardExit(int x, int y, int surface, int mx, int my) {
            this.x = x;
            this.y = y;
            this.surface = surface;

            this.mx = mx;
            this.my = my;
        }
    }

    static void setTimeMachine() {
        for(int x = 0; x < m; x++) {
            for(int y = 0; y < m; y++) {
                if(timeBoard[TOP][x][y] == 2) {
                    timeMachine = new TimeMachine(x, y, TOP);
                    return;
                }
            }
        }
    }

    static void setAnormalyBoard() {
        anormalyBoard = new boolean[n][n];
        for(Anormaly a : anormalies) {
            anormalyBoard[a.x][a.y] = true;
        }
    }

    static void setTimeBoardExit() {
        int minX = n + 1, minY = n + 1;
        int maxX = -1, maxY = -1;

        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(mijiBoard[x][y] == 3) {
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);

                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }
        // minX, minY 기준 > v (북, 서)
        for(int y = minY; y <= maxY; y++) {     // 북
            int nx = minX - 1;
            if(isNotMijiBoard(nx, y)) continue;
            if(mijiBoard[nx][y] == 0) {
                timeBoardExit = new TimeBoardExit(m - 1, maxY - y, NORTH, nx, y);
                return;
            }
        }

        for(int x = minX; x <= maxX; x++) {     // 서
            int ny = minY - 1;
            if(isNotMijiBoard(x, ny)) continue;
            if(mijiBoard[x][ny] == 0) {
                timeBoardExit = new TimeBoardExit(m - 1, x - minX, WEST, x, ny);
                return;
            }
        }

        // maxX, maxY 기준 < ^ (남, 동)
        for(int y = maxY; y >= minY; y--) {     // 남
            int nx = maxX + 1;
            if(isNotMijiBoard(nx, y)) continue;
            if(mijiBoard[nx][y] == 0) {
                timeBoardExit = new TimeBoardExit(m - 1, y - minY, SOUTH, nx, y);
                return;
            }
        }

        for(int x = maxX; x >= minX; x--) {     // 동
            int ny = maxY + 1;
            if(isNotMijiBoard(x, ny)) continue;
            if(mijiBoard[x][ny] == 0) {
                timeBoardExit = new TimeBoardExit(m - 1, maxX - x, EAST, x, ny);
                return;
            }
        }
    }

    static int[] dx = {0, 0, 1, -1};
    static int[] dy = {1, -1, 0, 0};

    static TimeMachine timeMachine;
    static TimeBoardExit timeBoardExit;
    static boolean[][] anormalyBoard;
    static int solution() {
        setTimeMachine();
        setAnormalyBoard();
        setTimeBoardExit();

        int time = exitTimeBoard();

        if(time == -1) return -1;

        // 확산 맞추기
        for(int i = 1; i <= time; i++) spreadTimeAnormaly(i);

        if(anormalyBoard[timeBoardExit.mx][timeBoardExit.my]) return -1;

        return exitBoard(time);
    }

    static void spreadTimeAnormaly(int time) {
        for(Anormaly a : anormalies) {
            if(a.stop) continue;

            if(time % a.v != 0) continue;

            // d 방향으로 한 칸 확산
            int nx = a.x + dx[a.d];
            int ny = a.y + dy[a.d];
            if(isNotMijiBoard(nx, ny) ||        // 격자를 벗어나는 경우
                mijiBoard[nx][ny] != 0          // 빈 칸이 아닌 경우
            ) {
                a.stop();
                continue;
            }

            a.update(nx, ny);
            anormalyBoard[a.x][a.y] = true;
        }
    }

    // 시간의 벽에서 최단 거리 탈출
    static int exitTimeBoard() {
        int INF = Integer.MAX_VALUE;
        int[][][] time = new int[5][m][m];
        Queue<int[]> q = new ArrayDeque<>();
        for(int i = 0; i < 5; i++) {
            for(int x = 0; x < m; x++) {
                Arrays.fill(time[i][x], INF);
            }
        }

        // 초기 위치 설정
        time[timeMachine.surface][timeMachine.x][timeMachine.y] = 0;
        q.offer(new int[] {timeMachine.x, timeMachine.y, timeMachine.surface, 0});

        while(!q.isEmpty()) {
            int[] cur = q.poll();

            if(cur[2] == timeBoardExit.surface && cur[0] == timeBoardExit.x && cur[1] == timeBoardExit.y) {
                return cur[3];
            }

            if(time[cur[2]][cur[0]][cur[1]] < cur[3]) continue;

            for(int dir = 0; dir < 4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];

                // 면 이동 X
                if(nx >= 0 && ny >= 0 && nx < m && ny < m && timeBoard[cur[2]][nx][ny] == 0) {
                    if(time[cur[2]][nx][ny] > cur[3] + 1) {
                        time[cur[2]][nx][ny] = cur[3] + 1;
                        q.offer(new int[] {nx, ny, cur[2], time[cur[2]][nx][ny]});
                    }
                } else {        // 면 이동
                    if(nx < 0) {        // TOP 으로
                        int newSurface = TOP;
                        if(cur[2] == WEST) {
                            if(timeBoard[newSurface][ny][0] == 0 && time[newSurface][ny][0] > cur[3] + 1) {
                                time[newSurface][ny][0] = cur[3] + 1;
                                q.offer(new int[] {ny, 0, newSurface, time[newSurface][ny][0]});
                            }
                        } else if(cur[2] == EAST) {
                            if(timeBoard[newSurface][(m - 1) - ny][m - 1] == 0 && time[newSurface][(m - 1) - ny][m - 1] > cur[3] + 1) {
                                time[newSurface][(m - 1) - ny][m - 1] = cur[3] + 1;
                                q.offer(new int[] {(m - 1) - ny, m - 1, newSurface, time[newSurface][(m - 1) - ny][m - 1]});
                            }
                        } else if(cur[2] == SOUTH) {
                            if(timeBoard[newSurface][m - 1][ny] == 0 && time[newSurface][m - 1][ny] > cur[3] + 1) {
                                time[newSurface][m - 1][ny] = cur[3] + 1;
                                q.offer(new int[] {m - 1, ny, newSurface, time[newSurface][m - 1][ny]});
                            }
                        } else if(cur[2] == NORTH) {
                            if(timeBoard[newSurface][0][(m - 1) - ny] == 0 && time[newSurface][0][(m - 1) - ny] > cur[3] + 1) {
                                time[newSurface][0][(m - 1) - ny] = cur[3] + 1;
                                q.offer(new int[] {0, (m - 1) - ny, newSurface, time[newSurface][0][(m - 1) - ny]});
                            }
                        } else if(cur[2] == TOP) {
                            newSurface = NORTH;
                            if (timeBoard[newSurface][0][(m - 1) - ny] == 0 && time[newSurface][0][(m - 1) - ny] > cur[3] + 1) {
                                time[newSurface][0][(m - 1) - ny] = cur[3] + 1;
                                q.offer(new int[] {0, (m - 1) - ny, newSurface, time[newSurface][0][(m - 1) - ny]});
                            }
                        }
                    } else if(nx >= m) {    // BOTTOM 으로 -> 이거 방지할 듯?
                        if(cur[2] == TOP) {
                            int newSurface = SOUTH;
                            if(timeBoard[newSurface][0][ny] == 0 && time[newSurface][0][ny] > cur[3] + 1) {
                                time[newSurface][0][ny] = cur[3] + 1;
                                q.offer(new int[] {0, ny, newSurface, time[newSurface][0][ny]});
                            }
                        }
                    } else if(ny < 0) {
                        if(cur[2] == WEST) {
                            int newSurface = NORTH;
                            if(timeBoard[newSurface][nx][m - 1] == 0 && time[newSurface][nx][m - 1] > cur[3] + 1) {
                                time[newSurface][nx][m - 1] = cur[3] + 1;
                                q.offer(new int[] {nx, m - 1, newSurface, time[newSurface][nx][m - 1]});
                            }
                        } else if(cur[2] == EAST) {
                            int newSurface = SOUTH;
                            if(timeBoard[newSurface][nx][m - 1] == 0 && time[newSurface][nx][m - 1] > cur[3] + 1) {
                                time[newSurface][nx][m - 1] = cur[3] + 1;
                                q.offer(new int[] {nx, m - 1, newSurface, time[newSurface][nx][m - 1]});
                            }
                        }  else if(cur[2] == SOUTH) {
                            int newSurface = WEST;
                            if(timeBoard[newSurface][nx][m - 1] == 0 && time[newSurface][nx][m - 1] > cur[3] + 1) {
                                time[newSurface][nx][m - 1] = cur[3] + 1;
                                q.offer(new int[] {nx, m - 1, newSurface, time[newSurface][nx][m - 1]});
                            }
                        } else if(cur[2] == NORTH) {
                            int newSurface = EAST;
                            if(timeBoard[newSurface][nx][m - 1] == 0 && time[newSurface][nx][m - 1] > cur[3] + 1) {
                                time[newSurface][nx][m - 1] = cur[3] + 1;
                                q.offer(new int[] {nx, m - 1, newSurface, time[newSurface][nx][m - 1]});
                            }
                        } else if(cur[2] == TOP) {
                            int newSurface = WEST;
                            if(timeBoard[newSurface][0][nx] == 0 && time[newSurface][0][nx] > cur[3] + 1) {
                                time[newSurface][0][nx] = cur[3] + 1;
                                q.offer(new int[] {0, nx, newSurface, time[newSurface][0][nx]});
                            }
                        }
                    } else if(ny >= m) {
                        if(cur[2] == WEST) {
                            int newSurface = SOUTH;
                            if(timeBoard[newSurface][nx][0] == 0 && time[newSurface][nx][0] > cur[3] + 1) {
                                time[newSurface][nx][0] = cur[3] + 1;
                                q.offer(new int[] {nx, 0, newSurface, time[newSurface][nx][0]});
                            }
                        } else if(cur[2] == EAST) {
                            int newSurface = NORTH;
                            if(timeBoard[newSurface][nx][0] == 0 && time[newSurface][nx][0] > cur[3] + 1) {
                                time[newSurface][nx][0] = cur[3] + 1;
                                q.offer(new int[] {nx, 0, newSurface, time[newSurface][nx][0]});
                            }
                        } else if(cur[2] == SOUTH) {
                            int newSurface = EAST;
                            if(timeBoard[newSurface][nx][0] == 0 && time[newSurface][nx][0] > cur[3] + 1) {
                                time[newSurface][nx][0] = cur[3] + 1;
                                q.offer(new int[] {nx, 0, newSurface, time[newSurface][nx][0]});
                            }
                        } else if(cur[2] == NORTH) {
                            int newSurface = WEST;
                            if(timeBoard[newSurface][nx][0] == 0 && time[newSurface][nx][0] > cur[3] + 1) {
                                time[newSurface][nx][0] = cur[3] + 1;
                                q.offer(new int[] {nx, 0, newSurface, time[newSurface][nx][0]});
                            }
                        } else if(cur[2] == TOP) {
                            int newSurface = EAST;
                            if(timeBoard[newSurface][0][(m - 1) - nx] == 0 && time[newSurface][0][(m - 1) - nx] > cur[3] + 1) {
                                time[newSurface][0][(m - 1) - nx] = cur[3] + 1;
                                q.offer(new int[] {0, (m - 1) - nx, newSurface, time[newSurface][0][(m - 1) - nx]});
                            }
                        }
                    }
                }
            }
        }

        return -1;
    }

    static int exitBoard(int time) {
        spreadTimeAnormaly(++time);
        if(anormalyBoard[timeBoardExit.mx][timeBoardExit.my]) return -1;
        int INF = Integer.MAX_VALUE;
        int[][] mijiTime = new int[n][n];

        for(int x = 0; x < n; x++) Arrays.fill(mijiTime[x], INF);
        mijiTime[timeBoardExit.mx][timeBoardExit.my] = time;

        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[] {timeBoardExit.mx, timeBoardExit.my, time});
        while(!q.isEmpty()) {
            ++time;
            spreadTimeAnormaly(time);

            // 타임머신 이동
            Queue<int[]> temp = new ArrayDeque<>();
            while(!q.isEmpty()) {
                int[] cur = q.poll();
                int x = cur[0];
                int y = cur[1];
                int accTime = cur[2];

                if(mijiBoard[x][y] == 4) {
                    return accTime;
                }

                if(mijiTime[x][y] < accTime) continue;

                for(int dir = 0; dir < 4; dir++) {
                    int nx = x + dx[dir];
                    int ny = y + dy[dir];

                    if(isNotMijiBoard(nx, ny)) continue;
                    if(mijiBoard[nx][ny] == 1 || mijiBoard[nx][ny] == 3) continue;
                    if(anormalyBoard[nx][ny]) continue;

                    if(mijiTime[nx][ny] > accTime + 1) {
                        mijiTime[nx][ny] = accTime + 1;
                        temp.offer(new int[] {nx, ny, mijiTime[nx][ny]});
                    }
                }
            }

            q = temp;
        }
        return -1;
    }

    static boolean isNotMijiBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}