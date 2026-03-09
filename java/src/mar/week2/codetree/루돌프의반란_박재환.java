package mar.week2.codetree;

import java.util.*;
import java.io.*;

public class 루돌프의반란_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    /**
     * P명의 산타
     * N x N -> 좌상단 (1,1)
     *
     * M턴
     * - 각 턴마다 루돌프와 산타가 한 번씩 움직인다
     * - 루돌프가 1번 움직인 뒤, 1 ~ P 번 산타가 순서대로 움직임
     *      - 기절, 탈락한 산타는 움직일 수 없음
     *
     * 루돌프 움직임
     * 가까운 산타를 향해 1칸 돌진
     * - 둘 이상, r -> c 순으로 큰 산타
     * - 8방향 이동 가능
     *
     * 산타 움직임
     * - 1 ~ p 순으로 이동
     * - 산타는 루돌프에게 가까워지는 방향으로 움직임 (1칸)
     * - 겪자 밖, 다른 산타 있는 칸으로는 이동 X
     * - 움직일 수 있는 칸 X -> 이동 X
     * - 이동 O, 루돌프로 가까워질 수 없다면 이동 X
     * - 상우하좌 우선순위로 이동 가능
     *
     * 충돌
     * 산타와 루돌프 같은 칸 -> 충돌
     * 루돌프가 움직여 충돌 -> 산타는 c 만큼 점수 획득
     *      - 산타는 루돌프가 이동한 방향으로 c 칸만큼 밀려남
     * 산타가 움직여 충돌 -> 산타 d 점수 획뜩
     *      - 본인 이동 반대방향으로 d 칸 밀려남
     * 밀려나는것은 한칸씩 x, 점프
     * 밀려난 위치가 게임 밖 -> 탈락
     * 산타가 있다면 상호작용
     *
     * 상호작용
     * 원래 있던 산타가 1칸 밀려남 -> 연쇄작용
     *
     * 기절
     * 산타는 루돌프와 충돌 후 기절, 다다음턴 정상화
     * 기절해도 밀려날 수 있음
     * 루돌프가 기절한 산타를 또 타겟으로 할 수 있음
     *
     * 종료
     * 턴 끝
     * 산타 다 탈락
     * 매턴 탈락하지 않은 산타는 1점 추가 부여
     */
    static class Rudolph {
        int x, y;
        int dir;
        Rudolph(int x, int y) { this.x = x; this.y = y; this.dir = -1; }
    }
    static class Santa {
        int id;
        int x, y;
        int score;
        int sleep;
        boolean out;
        Santa(int id, int x, int y) { this.id = id; this.x = x; this.y = y; this.score = 0; this.sleep = -1; this.out = false; }
    }
    static StringTokenizer st;
    static int n, m, p, c, d;
    static int turn;
    static Rudolph rudolph;
    static int[][] board;
    static Santa[] santas;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 격자 크기
        m = Integer.parseInt(st.nextToken());       // 턴 수
        p = Integer.parseInt(st.nextToken());       // 산타 수
        c = Integer.parseInt(st.nextToken());       // 루돌프가 산타를 박았을 때
        d = Integer.parseInt(st.nextToken());       // 산타가 루돌프를 박았을 때

        st = new StringTokenizer(br.readLine().trim());
        int rx = Integer.parseInt(st.nextToken())-1;
        int ry = Integer.parseInt(st.nextToken())-1;
        rudolph = new Rudolph(rx, ry);

        board = new int[n][n];
        santas = new Santa[p+1];        // 1-based
        for(int i=1; i<p+1; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int id = Integer.parseInt(st.nextToken());
            int sx = Integer.parseInt(st.nextToken())-1;
            int sy = Integer.parseInt(st.nextToken())-1;
            Santa santa = new Santa(id, sx, sy);
            santas[id] = santa;
            board[sx][sy] = id;
        }
        for(turn = 0; turn<m; turn++) {
            findNearestSanta();
            moveSanta();
            increaseScoreLiveSanta();
        }

        for(int i=1; i<p+1; i++) sb.append(santas[i].score).append(' ');
    }
    /**
     * 1. 루돌프 이동
     * 2. 산타 이동
     */
    static void findNearestSanta() {
        int bestId = 0;
        int bestDist = Integer.MAX_VALUE;

        for(int i=1; i<p+1; i++) {
            if(santas[i].out) continue;
            int dist = getDist(rudolph, santas[i]);

            if(bestId == 0) {
                bestId = i;
                bestDist = dist;
                continue;
            }

            if(bestDist > dist) {
                bestId = i;
                bestDist = dist;
                continue;
            }

            if(bestDist == dist
                    && santas[bestId].x < santas[i].x) {
                bestId = i;
                continue;
            }

            if(bestDist == dist
                    && santas[bestId].x == santas[i].x
                    && santas[bestId].y < santas[i].y) {
                bestId = i;
                continue;
            }
        }
        moveRudolph(bestId);
        // 산타와 충돌했는지 확인
        checkCollisionToSanta();
    }
    // 루돌프
    static int[] rdx = {0,1,0,-1,1,1,-1,-1};
    static int[] rdy = {1,0,-1,0,1,-1,1,-1};
    static void moveRudolph(int bestId) {
        if(bestId == 0) return;
        Santa s = santas[bestId];

        if(rudolph.x == s.x) {       // 동일한 행에 있다면
            boolean left = rudolph.y > s.y;
            rudolph.y += (left ? -1 : 1);
            rudolph.dir = left ? 2 : 0;
            return;
        }

        if(rudolph.y == s.y) {       // 동일한 열에 있다면
            boolean up = rudolph.x > s.x;
            rudolph.x += (up ? -1 : 1);
            rudolph.dir = up ? 3 : 1;
            return;
        }
        // 대각선
        if(rudolph.x < s.x) { // (/ \)
            boolean left = rudolph.y > s.y;
            rudolph.x++;
            rudolph.y += (left ? -1 : 1);
            rudolph.dir = left ? 5 : 4;
            return;
        }

        if(rudolph.x > s.x) { // (\ /)
            boolean left = rudolph.y > s.y;
            rudolph.x--;
            rudolph.y += (left ? -1 : 1);
            rudolph.dir = left ? 7 : 6;
            return;
        }
    }
    static void checkCollisionToSanta() {
        int cur = board[rudolph.x][rudolph.y];
        if(cur == 0) return;        // 충돌하지 않음

        Santa s = santas[cur];      // 충돌한 산타, C 만큼 이동
        board[s.x][s.y] = 0;
        s.score += c;

        int nx = s.x + rdx[rudolph.dir] * c;
        int ny = s.y + rdy[rudolph.dir] * c;
        if(nx < 0 || ny < 0 || nx >= n || ny >= n) {
            s.out = true;
            return;
        }
        s.x = nx;
        s.y = ny;
        s.sleep = turn+1;

        // 연쇄작용 추가 필요
        chainCollisionByRudolph(s, rudolph.dir);
        board[s.x][s.y] = s.id;
    }
    static void chainCollisionByRudolph(Santa s, int dir) {
        if(board[s.x][s.y] == 0) return;        // 연쇄 X

        Santa o = santas[board[s.x][s.y]];
        board[o.x][o.y] = 0;

        int nx = o.x + rdx[dir];
        int ny = o.y + rdy[dir];
        if(nx < 0 || ny < 0 || nx >= n || ny >= n) {
            o.out = true;
            return;
        }
        o.x = nx;
        o.y = ny;

        // 연쇄작용 추가 필요
        chainCollisionByRudolph(o, dir);
        board[o.x][o.y] = o.id;
    }
    static int[] sdx = {-1,0,1,0};
    static int[] sdy = {0,1,0,-1};
    static void moveSanta() {
        for(int i=1; i<p+1; i++) {
            if(santas[i].out) continue;     // 탈락한 산타
            if(santas[i].sleep >= turn) {       // 기절한 산타
                continue;
            }

            Santa s = santas[i];
            int originDist = getDist(rudolph, s);
            // 상 우 하 좌 순에 맞춰 루돌프와 가까워지는 거리를 찾음
            int bestDir = -1;
            int bestDist = Integer.MAX_VALUE;
            for(int dir=0; dir<4; dir++) {
                int nx = s.x + sdx[dir];
                int ny = s.y + sdy[dir];
                if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                if(board[nx][ny] > 0) continue;
                int dist = getDist(rudolph, new Santa(s.id, nx, ny));
                if(dist >= originDist) continue;

                if(bestDist > dist) {
                    bestDir = dir;
                    bestDist = dist;
                }
            }

            if(bestDir != -1) {
                board[s.x][s.y] = 0;
                int nx = s.x + sdx[bestDir];
                int ny = s.y + sdy[bestDir];
                s.x = nx;
                s.y = ny;
                int dir = (bestDir+2)%4;
                collisionToRudolph(s, dir);
                if(!s.out) board[s.x][s.y] = s.id;
            }
        }
    }
    static void collisionToRudolph(Santa s, int dir) {
        if(s.x != rudolph.x || s.y != rudolph.y) return;

        // 충돌
        board[s.x][s.y] = 0;
        s.score += d;

        int nx = s.x + sdx[dir] * d;
        int ny = s.y + sdy[dir] * d;
        if(nx < 0 || ny < 0 || nx >= n || ny >= n) {
            s.out = true;
            return;
        }
        s.x = nx;
        s.y = ny;
        s.sleep = turn+1;

        // 연쇄작용 추가 필요
        chainCollisionBySanta(s, dir);
        if(!s.out) board[s.x][s.y] = s.id;
    }
    static void chainCollisionBySanta(Santa s, int dir) {
        if(board[s.x][s.y] == 0) return;        // 연쇄 X

        Santa o = santas[board[s.x][s.y]];
        board[o.x][o.y] = 0;

        int nx = o.x + sdx[dir];
        int ny = o.y + sdy[dir];
        if(nx < 0 || ny < 0 || nx >= n || ny >= n) {
            o.out = true;
            return;
        }
        o.x = nx;
        o.y = ny;

        // 연쇄작용 추가 필요
        chainCollisionBySanta(o, dir);
        board[o.x][o.y] = o.id;
    }
    static int getDist(Rudolph r, Santa s) {
        int xDiff = Math.abs(r.x - s.x);
        int yDiff = Math.abs(r.y - s.y);
        return xDiff * xDiff + yDiff * yDiff;
    }
    static void increaseScoreLiveSanta() {
        for(int i=1; i<p+1; i++) {
            if(santas[i].out) continue;
            santas[i].score++;
        }
    }
}
