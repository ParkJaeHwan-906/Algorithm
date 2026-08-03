package jul.week1.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 5시간+
  AI 사용 여부: O
  이런 문제 나오지 말기를 빌어야 할 듯..나오면 디버깅하다가 끝남..
  AI 썼는데도 한참 걸림.. + 다른 풀이들보다 실행 시간도 오래 걸려서 다시 풀어봐야 함.
 */
public class 메두사와전사들_박서희 {
    static int n, m;
    static int sr, sc, er, ec;
    static ArrayList<Warrior> warriors = new ArrayList<>();
    static int[][] board;
    static int[][] dist;
    static StringBuilder sb = new StringBuilder();
    static int bestDir;
    static boolean[][] bestHided;

    static final int[] dx = {-1, 1, 0, 0};
    static final int[] dy = {0, 0, -1, 1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        sr = Integer.parseInt(st.nextToken());
        sc = Integer.parseInt(st.nextToken());
        er = Integer.parseInt(st.nextToken());
        ec = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < m; i++) {
            int wr = Integer.parseInt(st.nextToken());
            int wc = Integer.parseInt(st.nextToken());
            warriors.add(new Warrior(wr, wc));
        }

        board = new int[n][n];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        int[][] distToEnd = calculateDist(er, ec);
        if (dist[sr][sc] != -1) simulate();
        else System.out.println(-1);
    }


    public static int[][] calculateDist(int r, int c) {
        dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], -1);
        }

        Queue<int[]> queue = new LinkedList<>();
        dist[r][c] = 0;
        queue.add(new int[]{r, c});

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int cr = cur[0], cc = cur[1];

            for (int d = 0; d < 4; d++) {
                int nr = cr + dx[d], nc = cc + dy[d];

                if (!inRange(nr, nc)) continue;
                if (dist[nr][nc] != -1) continue;
                if (board[nr][nc] == 1) continue;

                dist[nr][nc] = dist[cr][cc] + 1;
                queue.add(new int[]{nr, nc});
            }
        }
        return dist;
    }

    public static void simulate() {
        while (true) {

            if (sr == er && sc == ec) {
                sb.append(0);
                break;
            }

            // 1. 메두사의 이동
            moveMedusa();

            if (sr == er && sc == ec) {
                sb.append(0);
                break;
            }

            // 2. 메두사의 시선
            int stonedWarriors = seeWarriors();
            // 3. 전사들의 이동
            int totalDist = moveWarriors();
            // 4. 전사의 공격
            int attackWarriors = attackMedusa();

            sb.append(totalDist).append(" ").append(stonedWarriors).append(" ").append(attackWarriors).append("\n");
        }
        System.out.println(sb);
    }

    static void moveMedusa() {
        for (int d = 0; d < 4; d++) {
            int nr = sr + dx[d];
            int nc = sc + dy[d];

            if (!inRange(nr, nc)) continue;
            if (dist[nr][nc] == -1) continue;

            if (dist[nr][nc] == dist[sr][sc] - 1) {
                sr = nr;
                sc = nc;

                for (int i = warriors.size() - 1; i >= 0; i--) {
                    Warrior w = warriors.get(i);
                    if (w.r == sr && w.c == sc) {
                        warriors.remove(i);
                    }
                }
                return;
            }
        }
    }

    static int seeWarriors() {
        for (Warrior w : warriors) w.isStone = false;

        int maxStonedCnt = -1;
        bestDir = -1;
        bestHided = new boolean[n][n];

        for (int d = 0; d < 4; d++) {
            boolean[][] hided = new boolean[n][n];
            int curStonedCnt = 0;   // 지금 방향에서 돌이 된 전사 인수

            Queue<int[]> q = new LinkedList<>();
            if (inRange(sr + dx[d], sc + dy[d]))
                q.add(new int[]{sr + dx[d], sc + dy[d]});

            boolean[][] visited = new boolean[n][n];
            if (inRange(sr + dx[d], sc + dy[d]))
                visited[sr + dx[d]][sc + dy[d]] = true;

            while (!q.isEmpty()) {
                int[] cur = q.poll();
                int cr = cur[0], cc = cur[1];

                if (!hided[cr][cc]) {
                    int boardWarriors = 0;
                    for (Warrior w : warriors) {
                        if (w.r == cr && w.c == cc) boardWarriors++;
                    }
                    if (boardWarriors > 0) {
                        curStonedCnt += boardWarriors;
                        calculateHide(cr, cc, hided, d);
                    }
                }

                for (int i = 0; i < 4; i++) {
                    int nr = cr + dx[i], nc = cc + dy[i];

                    if (!inRange(nr, nc)) continue;
                    if (visited[nr][nc]) continue;
                    if (!canSee(nr, nc, d)) continue;

                    visited[nr][nc] = true;
                    q.add(new int[]{nr, nc});
                }
            }

            if (curStonedCnt > maxStonedCnt) {
                maxStonedCnt = curStonedCnt;
                bestDir = d;
                for (int i = 0; i < n; i++) {
                    bestHided[i] = hided[i].clone();
                }
            }
        }
        for (Warrior w : warriors) {
            if (canSee(w.r, w.c, bestDir) && !bestHided[w.r][w.c]) {
                w.isStone = true;
            }
        }

        if (bestDir == -1) bestDir = 0;
        return Math.max(0, maxStonedCnt);
    }

    public static int[][] calculateWarriorDist() {
        int[][] wDist = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(wDist[i], -1);
        }

        Queue<int[]> queue = new LinkedList<>();
        wDist[sr][sc] = 0;
        queue.add(new int[]{sr, sc});

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int cr = cur[0], cc = cur[1];

            for (int d = 0; d < 4; d++) {
                int nr = cr + dx[d], nc = cc + dy[d];

                if (!inRange(nr, nc)) continue;
                if (wDist[nr][nc] != -1) continue;

                wDist[nr][nc] = wDist[cr][cc] + 1;
                queue.add(new int[]{nr, nc});
            }
        }
        return wDist;
    }

    static int moveWarriors() {
        int[][] distToMedusa = calculateWarriorDist();

        int totalDist = 0;

        // 상하좌우
        int[] firstIdx = {0, 1, 2, 3};
        // 좌우상하
        int[] secondIdx = {2, 3, 0, 1};

        for (Warrior w : warriors) {
            if (w.isStone) continue; // 돌이 된 전사는 못 움직임

            for (int t = 0; t < 2; t++) {
                if (w.r == sr && w.c == sc) break;

                int cur = distToMedusa[w.r][w.c];
                int[] order = (t == 0) ? firstIdx : secondIdx;

                boolean moved = false;

                for (int i = 0; i < 4; i++) {
                    int d = order[i];
                    int nr = w.r + dx[d], nc = w.c + dy[d];

                    if (!inRange(nr, nc)) continue;
                    if (distToMedusa[nr][nc] != cur - 1) continue;
                    if (canSee(nr, nc, bestDir) && !bestHided[nr][nc]) continue;

                    w.r = nr;
                    w.c = nc;
                    totalDist += 1;
                    moved = true;
                    break;
                }

                if (!moved) break;
            }
        }
        return totalDist;
    }

    static int attackMedusa() {
        int warriorsCnt = 0;
        for (int i = warriors.size() - 1; i >= 0; i--) {
            Warrior w = warriors.get(i);
            if (w.r == sr && w.c == sc) {
                warriors.remove(i);
                warriorsCnt += 1;
            }
        }
        return warriorsCnt;
    }

    static boolean canSee(int r, int c, int d) {
        int rowDiff = Math.abs(r - sr);
        int colDiff = Math.abs(c - sc);

        if (d == 0) return r < sr && colDiff <= rowDiff;      // 상
        if (d == 1) return r > sr && colDiff <= rowDiff;      // 하
        if (d == 2) return c < sc && rowDiff <= colDiff;      // 좌
        return c > sc && rowDiff <= colDiff;                  // 우
    }


    static void calculateHide(int cr, int cc, boolean[][] hided, int d) {
        int dr = cr - sr; // 메두사 기준 전사의 행 차이
        int dc = cc - sc; // 메두사 기준 전사의 열 차이

        // 그림자 -> 한 칸 나아갈 때마다 폭이 1씩 넓어지는 직각삼각형.
        if (d == 0) {                 // 상
            if (dc == 0) {
                for (int r = cr - 1; r >= 0; r--) hided[r][cc] = true;
            } else if (dc < 0) {      // 전사가 왼쪽 -> 왼쪽 위로
                for (int r = cr - 1; r >= 0; r--) {
                    int w = cr - r;
                    for (int c = cc - w; c <= cc; c++) if (inRange(r, c)) hided[r][c] = true;
                }
            } else {                  // 전사가 오른쪽 -> 오른쪽 위로
                for (int r = cr - 1; r >= 0; r--) {
                    int w = cr - r;
                    for (int c = cc; c <= cc + w; c++) if (inRange(r, c)) hided[r][c] = true;
                }
            }
        } else if (d == 1) {          // 하
            if (dc == 0) {
                for (int r = cr + 1; r < n; r++) hided[r][cc] = true;
            } else if (dc < 0) {
                for (int r = cr + 1; r < n; r++) {
                    int w = r - cr;
                    for (int c = cc - w; c <= cc; c++) if (inRange(r, c)) hided[r][c] = true;
                }
            } else {
                for (int r = cr + 1; r < n; r++) {
                    int w = r - cr;
                    for (int c = cc; c <= cc + w; c++) if (inRange(r, c)) hided[r][c] = true;
                }
            }
        } else if (d == 2) {          // 좌
            if (dr == 0) {
                for (int c = cc - 1; c >= 0; c--) hided[cr][c] = true;
            } else if (dr < 0) {      // 전사가 위쪽 -> 왼쪽 위로
                for (int c = cc - 1; c >= 0; c--) {
                    int w = cc - c;
                    for (int r = cr - w; r <= cr; r++) if (inRange(r, c)) hided[r][c] = true;
                }
            } else {                  // 전사가 아래쪽 -> 왼쪽 아래로
                for (int c = cc - 1; c >= 0; c--) {
                    int w = cc - c;
                    for (int r = cr; r <= cr + w; r++) if (inRange(r, c)) hided[r][c] = true;
                }
            }
        } else {                      // 우
            if (dr == 0) {
                for (int c = cc + 1; c < n; c++) hided[cr][c] = true;
            } else if (dr < 0) {
                for (int c = cc + 1; c < n; c++) {
                    int w = c - cc;
                    for (int r = cr - w; r <= cr; r++) if (inRange(r, c)) hided[r][c] = true;
                }
            } else {
                for (int c = cc + 1; c < n; c++) {
                    int w = c - cc;
                    for (int r = cr; r <= cr + w; r++) if (inRange(r, c)) hided[r][c] = true;
                }
            }
        }
    }


    static boolean inRange(int x, int y) {
        return 0 <= x && x < n && 0 <= y && y < n;
    }

    static class Warrior {
        int r, c;
        boolean isStone = false;

        public Warrior(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }
}
