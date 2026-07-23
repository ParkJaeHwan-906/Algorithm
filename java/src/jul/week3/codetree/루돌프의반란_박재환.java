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

    static void solution() {
        while(m-- > 0) {       // m 턴동안 진행
            // 1. 루돌프 이동

            // 2. 산타 이동
        }
    }

    static void moveRudolf() {

    }

    static int getDist(Loc a, Loc b) {
        int xDiff = a.x - b.x;
        int yDiff = a.y - b.y;
        return (xDiff * xDiff) + (yDiff * yDiff);
    }

    static int getKey(int x, int y) {
        return (x * 57) + y;
    }

}
