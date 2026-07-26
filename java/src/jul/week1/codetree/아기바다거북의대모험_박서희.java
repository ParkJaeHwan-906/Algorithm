package jul.week1.codetree;

import java.io.*;
import java.util.*;

public class 아기바다거북의대모험_박서희 {
    static int N, M, K;
    static int[][] board;
    static int[][] fire;
    static List<Turtle> turtles = new ArrayList<>();
    static List<Mountain> mountains = new ArrayList();

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    public static void main(String[] args) throws IOException {
        init();
        simulate();
        printAnswer();
    }

    public static void simulate() {
        int cnt = 1;
        while (true) {
            // 1. 바다거북 이동
            moveTurtle(cnt);
            // 2. 화산 압력 증가
            increasePressure();
            // 3. 화산 분출 및 연쇄 반응
            volcano();
            //  4. 환경 초기화
            clearEnv();
            if (cnt == 100) break;
            cnt++;
        }
    }

    public static void printAnswer() {
        StringBuilder sb = new StringBuilder();
        for (Turtle t : turtles) {
            System.out.println(t.escapeTime == 0 ? -1 : t.escapeTime);
        }
    }

    public static void moveTurtle(int cnt) {
        for (Turtle t : turtles) {
            if (t.isStoned) continue;
            if (t.escapeTime > 0) continue;

            int[][] dist = new int[N][N];
            for (int i = 0; i < N; i++) {
                Arrays.fill(dist[i], -1);
            }

            Queue<int[]> queue = new ArrayDeque<>();
            queue.add(new int[]{N - 1, N - 1});
            dist[N - 1][N - 1] = 0;

            while (!queue.isEmpty()) {
                int[] cur = queue.poll();
                int cx = cur[0], cy = cur[1];

                for (int d = 0; d < 4; d++) {
                    int nx = cx + dx[d], ny = cy + dy[d];
                    if (!inRange(nx, ny)) continue;
                    if (dist[nx][ny] != -1) continue;
                    if (board[nx][ny] == 1 || board[nx][ny] == 2) continue;

                    dist[nx][ny] = dist[cx][cy] + 1;
                    queue.add(new int[]{nx, ny});
                }
            }

            int minDist = Integer.MAX_VALUE;
            int bestDir = -1;

            for (int d = 0; d < 4; d++) {
                int nx = t.x + dx[d], ny = t.y + dy[d];

                if (inRange(nx, ny) && dist[nx][ny] != -1) {
                    if (minDist > dist[nx][ny]) {
                        minDist = dist[nx][ny];
                        bestDir = d;
                    }
                }
            }

            if (bestDir != -1) {
                board[t.x][t.y] = 0;

                t.x += dx[bestDir];
                t.y += dy[bestDir];

                if (t.x != N - 1 || t.y != N - 1) {
                    board[t.x][t.y] = 2;
                } else {
                    t.escapeTime = cnt;
                }
            }
        }
    }


    public static void increasePressure() {
        for (int i = 0; i < mountains.size(); i++) {
            Mountain m = mountains.get(i);
            m.pressure += 10;
        }
    }

    public static void volcano() {

        while (true) {
            boolean isMoved = false;
            for (int i = 0; i < mountains.size(); i++) {
                Mountain m = mountains.get(i);
                if (m.thisTurn) continue;
                if (m.pressure >= m.P || m.pressure + fire[m.x][m.y] >= m.P) {
                    fire[m.x][m.y] += m.P;
                    m.thisTurn = true;
                    isMoved = true;

                    boolean[][] visited = new boolean[N][N];
                    Queue<int[]> queue = new ArrayDeque<>();
                    queue.add(new int[]{m.x, m.y, m.P});
                    visited[m.x][m.y] = true;

                    while (!queue.isEmpty()) {
                        int[] cur = queue.poll();
                        int cx = cur[0], cy = cur[1];
                        int cp = cur[2];


                        for (int d = 0; d < 4; d++) {
                            int nx = cx + dx[d], ny = cy + dy[d];
                            if (!inRange(nx, ny)) continue;
                            if (visited[nx][ny]) continue;
                            if (board[nx][ny] == 1) continue;

                            int spreadFire = cp / 2;
                            if (spreadFire == 0) continue;
                            fire[nx][ny] += spreadFire;
                            queue.add(new int[]{nx, ny, spreadFire});
                            visited[nx][ny] = true;
                        }
                    }
                }
            }
            if (!isMoved) break;
        }

        // 거북이 화석되기
        for (Turtle t : turtles) {
            if (fire[t.x][t.y] >= 20) {
                t.isStoned = true;
            }
        }
    }

    public static void clearEnv() {
        fire = new int[N][N];
        for (Mountain m : mountains) {
            if (m.thisTurn) m.pressure = 0;
            m.thisTurn = false;
        }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());   // 격자 크기
        M = Integer.parseInt(st.nextToken());   // 바다거북 수
        K = Integer.parseInt(st.nextToken());   // 해저 화산 수

        board = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            turtles.add(new Turtle(x, y));
            board[x][y] = 2;
        }

        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int P = Integer.parseInt(st.nextToken());
            mountains.add(new Mountain(x, y, P));
        }

        fire = new int[N][N];

    }

    static boolean inRange(int x, int y) {
        return 0 <= x && x < N && 0 <= y && y < N;
    }

    static class Turtle {
        int x, y;
        boolean isStoned = false;
        int escapeTime;

        public Turtle(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Mountain {
        int x, y;
        int pressure;
        int P;
        boolean thisTurn;

        public Mountain(int x, int y, int P) {
            this.x = x;
            this.y = y;
            this.P = P;
        }
    }
}
