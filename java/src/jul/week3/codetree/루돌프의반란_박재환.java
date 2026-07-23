package jul.week3.codetree;

import java.util.*;
import java.io.*;

public class 루돌프의반란_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static class Loc {
        int x, y;
        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }

        boolean isSame(Loc o) {
            return this.x == o.x && this.y == o.y;
        }
    }

    static class Santa extends Loc {
        int score;
        int rest;
        boolean failed;
        Santa(int x, int y, int score, int rest) {
            super(x, y);
            this.score = score;
            this.rest = rest;
            this.failed = false;
        }
    }

    static int n, m, p, c, d;
    static Loc rudolf;
    static Santa[] santas;
    static Map<Integer, Santa> locSantas;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 격자크기
        m = Integer.parseInt(st.nextToken());       // 턴 수
        p = Integer.parseInt(st.nextToken());       // 산타 수
        c = Integer.parseInt(st.nextToken());       // 루돌프가 산타를 박았을 때 획득하는 점수
        d = Integer.parseInt(st.nextToken());       // 산타가 루돌프를 박았을 때 획득하는 점수

        st = new StringTokenizer(br.readLine().trim());
        int rx = Integer.parseInt(st.nextToken()) - 1;
        int ry = Integer.parseInt(st.nextToken()) - 1;
        rudolf = new Loc(rx, ry);

        santas = new Santa[p + 1];
        locSantas = new HashMap<>();
        for(int i = 1; i <= p; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int id = Integer.parseInt(st.nextToken());
            int sx = Integer.parseInt(st.nextToken()) - 1;
            int sy = Integer.parseInt(st.nextToken()) - 1;
            Santa santa = new Santa(sx, sy, 0, 0);
            santas[id] = santa;
            locSantas.put(getKey(santa.x, santa.y), santa);
        }

        System.out.println(solution());
    }
    // 상 우 하 좌
    static final int[] dx = {-1, 0, 1, 0, 1, 1, -1, -1};
    static final int[] dy = {0, 1, 0, -1, 1, -1, 1, -1};

    static int oppositeDir(int dir) {
        if(dir == 0) return 2;
        if(dir == 1) return 3;
        if(dir == 2) return 0;
        return 1;
    }

    static String solution() {
        while(m-- > 0 && !cantPlay()) {       // m 턴동안 진행
            // 1. 루돌프 이동
            moveRudolf();
            // 2. 산타 이동
            moveSantas();
            // 3. 생존 산타 점수 및 기절 시간 갱신
            finishTurn();
        }
        return getTotalScore();
    }

    static void finishTurn() {
        for(int i = 1; i <= p; i++) {
            Santa santa = santas[i];
            if(santa.failed) continue;

            santa.score++;
            if(santa.rest > 0) santa.rest--;
        }
    }

    static boolean cantPlay() {
        int islive = 0;
        for(int i = 1; i <= p; i++) {
            if(santas[i].failed) continue;
            islive++;
        }
        return islive == 0;
    }

    static String getTotalScore() {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= p; i++) sb.append(santas[i].score).append(" ");
        return sb.toString();
    }

    static void moveSantas() {
        for(int i = 1; i <= p; i++) {       // 1번 산타부터 p번으로
            Santa santa = santas[i];
            if(santa.failed) continue;      // 탈락한 산타는 패스
            if(santa.rest > 0) continue;    // 기절한 산타는 움직일 수 없음

            // 루돌프와 가장 가까워지는 위치로 이동
            moveSanta(santa);
        }
    }

    static void moveSanta(Santa santa) {
        int minDist = getDist(santa, rudolf);           // 기존 거리
        int moveDir = -1;                               // 최종 움직일 방향
        for(int dir = 0; dir < 4; dir++) {
            int nx = santa.x + dx[dir];
            int ny = santa.y + dy[dir];

            if(isNotBoard(nx, ny)) continue;            // 격자를 벗어나느 경우
            if(locSantas.containsKey(getKey(nx, ny))) continue;     // 다른 산타가 있는 경우
            int dist = getDist(new Loc(nx, ny), rudolf);
            if(dist < minDist) {                        // 거리가 더 가까워지는 경우
                minDist = dist;
                moveDir = dir;
            }
        }

        if(moveDir == -1) return;                   // 이동할 수 없는 경우 패스

        // 이동 가능
        int nx = santa.x + dx[moveDir];
        int ny = santa.y + dy[moveDir];
        locSantas.remove(getKey(santa.x, santa.y));     // 기존 위치 업데이트
        santa.x = nx;
        santa.y = ny;
        locSantas.put(getKey(santa.x, santa.y), santa);

        // 루돌프랑 충돌나는지 확인
        checkCollisionSantaToRudolf(santa, oppositeDir(moveDir));
    }

    static void checkCollisionSantaToRudolf(Santa santa, int dir) {
        if(santa.isSame(rudolf)) {      // 루돌프랑 충돌하는 경우
            // 0. locSantas에서 현재 산타 위치 제거
            locSantas.remove(getKey(santa.x, santa.y));
            // 0 - 1. 루돌프와 출돌한 산타는 기절
            santa.rest = 2;                 // 계속 업데이트? -> 충돌을 새롭게 할때마다 2초로 초기화?

            // 1. 산타는 D 만큼 점수를 획득한다.
            santa.score += d;
            // 2. 산타는 이동한 반대방향(dir)로 D만큼 밀려난다.
            int nsx = santa.x + dx[dir] * d;
            int nsy = santa.y + dy[dir] * d;
            santa.x = nsx;
            santa.y = nsy;
            // 3 - 1. 밀려난 위치가 격자 밖이라면 산타는 게임에서 탈락한다.
            if(isNotBoard(santa.x, santa.y)) {
                santa.failed = true;
                return;
            }
            // 3 - 2. 밀려난 위치에 다른 산타가 있다면 상호작용이 발생한다.
            if(locSantas.containsKey(getKey(santa.x, santa.y))) {       // 다른 산타가 있는 경우
                interaction(santa, dir);
            } else {            // 다른 산타가 없는 경우 -> 그냥 위치만 업데이트
                locSantas.put(getKey(santa.x, santa.y), santa);
            }
        }
    }

    static void moveRudolf() {
        Santa target = getNearSanta();

        int bDist = getDist(rudolf, target);
        int moveDir = -1;
        int bX = rudolf.x;
        int bY = rudolf.y;

        for (int dir = 0; dir < 8; dir++) {
            int nx = rudolf.x + dx[dir];
            int ny = rudolf.y + dy[dir];

            if (isNotBoard(nx, ny)) continue;

            int dist = getDist(new Loc(nx, ny), target);

            if (dist < bDist) {
                bDist = dist;
                bX = nx;
                bY = ny;
                moveDir = dir;
            }
        }

        rudolf.x = bX;
        rudolf.y = bY;

        // 산타와 충돌했는지 확인
        checkCollisionRudolfToSanta(target, moveDir);
    }

    static void checkCollisionRudolfToSanta(Santa santa, int dir) {
        if(santa.isSame(rudolf)) {     // 충돌한 경우 산타 밀어내기
            // 0. locSantas에서 현재 산타 위치 제거
            locSantas.remove(getKey(santa.x, santa.y));
            // 0 - 1. 루돌프와 출돌한 산타는 기절
            santa.rest = 2;                 // 계속 업데이트? -> 충돌을 새롭게 할때마다 2초로 초기화?

            // 1. 산타는 C 만큼 점수를 획득한다.
            santa.score += c;
            // 2. 산타는 루돌프 방향(dir)로 C만큼 밀려난다.
            int nsx = santa.x + dx[dir] * c;
            int nsy = santa.y + dy[dir] * c;
            santa.x = nsx;
            santa.y = nsy;
            // 3 - 1. 밀려난 위치가 격자 밖이라면 산타는 게임에서 탈락한다.
            if(isNotBoard(santa.x, santa.y)) {
                santa.failed = true;
                return;
            }
            // 3 - 2. 밀려난 위치에 다른 산타가 있다면 상호작용이 발생한다.
            if(locSantas.containsKey(getKey(santa.x, santa.y))) {       // 다른 산타가 있는 경우
                interaction(santa, dir);
            } else {            // 다른 산타가 없는 경우 -> 그냥 위치만 업데이트
                locSantas.put(getKey(santa.x, santa.y), santa);
            }
        }
    }

    static void interaction(Santa santa, int dir) {
        // santa : 밀려온 산타
        Santa target = locSantas.get(getKey(santa.x, santa.y));
        locSantas.put(getKey(santa.x, santa.y), santa);     // 현위치는 밀려온 산타로 업데이트

        // 밀려난 산타 위치 새로 계산
        int nsx = target.x + dx[dir];
        int nsy = target.y + dy[dir];
        if(isNotBoard(nsx, nsy)) {
            target.failed = true;
            return;
        }
        target.x = nsx;
        target.y = nsy;
        if(locSantas.containsKey(getKey(target.x, target.y))) {
            interaction(target, dir);
        } else {        // 다른 산타가 없는 경우 -> 그냥 위치만 업데이트
            locSantas.put(getKey(target.x, target.y), target);
        }
    }

    static Santa getNearSanta() {
        int bDist = Integer.MAX_VALUE;
        int bx = Integer.MIN_VALUE;
        int by = Integer.MIN_VALUE;

        for(int i = 1; i <= p; i++) {
            Santa santa = santas[i];
            if(santa.failed) continue;          // 이미 탈락한 산타는 제외
            int dist = getDist(santa, rudolf);
            if(bDist > dist ||
                    (bDist == dist) && (bx < santa.x || (bx == santa.x && by < santa.y))) {
                bDist = dist;
                bx = santa.x;
                by = santa.y;
            }
        }
        return locSantas.get(getKey(bx, by));
    }

    static int getDist(Loc a, Loc b) {
        int xDiff = a.x - b.x;
        int yDiff = a.y - b.y;
        return (xDiff * xDiff) + (yDiff * yDiff);
    }

    static int getKey(int x, int y) {
        return (x * 57) + y;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }

}
