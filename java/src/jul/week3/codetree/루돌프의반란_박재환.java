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
        int rx = Integer.parseInt(st.nextToken());
        int ry = Integer.parseInt(st.nextToken());
        rudolf = new Loc(rx, ry);

        santas = new Santa[p + 1];
        locSantas = new HashMap<>();
        for(int i = 1; i <= p; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int sx = Integer.parseInt(st.nextToken());
            int sy = Integer.parseInt(st.nextToken());
            Santa santa = new Santa(sx, sy, 0, 0);
            santas[i] = santa;
            locSantas.put(getKey(santa.x, santa.y), santa);
        }


    }

    static final int[] dx = {-1, 0, 1, 0, 1, 1, -1, -1};
    static final int[] dy = {0, 1, 0, -1, 1, -1, 1, -1};

    static void solution() {
        while(m-- > 0) {       // m 턴동안 진행
            // 1. 루돌프 이동
            moveRudolf();
            // 2. 산타 이동
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

            // 1. 산타는 C 만큼 점수를 획득한다.
            santa.score += c;
            // 2. 산타는 루돌프 방향(dir)로 C만큼 밀려난다.
            int nsx = santa.x + dx[dir] * c;
            int nsy = santa.y + dy[dir] * c;
            // 3 - 1. 밀려난 위치가 격자 밖이라면 산타는 게임에서 탈락한다.
            if(isNotBoard(nsx, nsy)) santa.failed = true;
            // 3 - 2. 밀려난 위치에 다른 산타가 있다면 상호작용이 발생한다.
            santa.x = nsx;
            santa.y = nsy;
            if(locSantas.containsKey(getKey(santa.x, santa.y))) {       // 다른 산타가 있는 경우
                interaction(santa, dir);
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
        if(isNotBoard(nsx, nsy)) target.failed = true;      // 밀려난 산타가 격자 밖으로 나가는 경우
        target.x = nsx;
        target.y = nsy;
        if(locSantas.containsKey(getKey(target.x, target.y))) {
            interaction(target, dir);
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
