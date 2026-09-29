package sep.week4.codetree;

import java.util.*;
import java.io.*;

public class 루돌프의반란_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int INF = Integer.MAX_VALUE;
    static final int[] dx = {-1, 0, 1, 0, 1, 1, -1, -1};
    static final int[] dy = {0, 1, 0, -1, 1, -1, 1, -1};

    static class Rudolf {
        int x, y;
        Rudolf(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Santa {
        int x, y;
        int score;          // 획득 점수
        boolean out;        // 탈락 여부
        int rest;           // 기절 시간
        Santa(int x, int y) {
            this.x = x;
            this.y = y;
            this.score = 0;
            this.out = false;
            this.rest = 0;
        }
    }

    static int n, m, p, c, d;
    static Rudolf rudolf;
    static Map<Integer, Santa> locToSanta;
    static Santa[] santas;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 격자 크기
        m = Integer.parseInt(st.nextToken());       // 턴 수
        p = Integer.parseInt(st.nextToken());       // 산타 수
        c = Integer.parseInt(st.nextToken());       // 루돌프 -> 산타 충돌 시 획득 점수
        d = Integer.parseInt(st.nextToken());       // 산타 -> 루돌프 충돌 시 획득 점수

        st = new StringTokenizer(br.readLine().trim());
        rudolf = new Rudolf(
                Integer.parseInt(st.nextToken()) - 1,
                Integer.parseInt(st.nextToken()) - 1
        );

        santas = new Santa[p];
        locToSanta = new HashMap<>();
        for (int i = 0; i < p; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int id = Integer.parseInt(st.nextToken()) - 1;
            santas[id] = new Santa(
                    Integer.parseInt(st.nextToken()) - 1,
                    Integer.parseInt(st.nextToken()) - 1
            );
            locToSanta.put(locToKey(santas[id].x, santas[id].y), santas[id]);
        }
        System.out.print(solution());
    }

    static int outSantaCount;
    static String solution() {
        outSantaCount = 0;
        printState();
        while(m-- > 0 && outSantaCount < p) {
            // 0. 기절 시간 감소
            decreaseRest();
            // 1. 루돌프 이동
            moveRudolf();
            // 2. 산타 이동
            moveSantas();
            // 3. 점수 누적
            accScore();
            printState();
        }
        return printResult();
    }
    // ==========================================
    // 루돌프 이동
    // ==========================================
    static void moveRudolf() {
        Santa target = findNearestSanta();
        int dir = rodolfNextDir(target);
        rudolf.x += dx[dir];
        rudolf.y += dy[dir];
        // 충돌 여부 확인
        if(target.x == rudolf.x && target.y == rudolf.y) {
            target.score += c;
            target.rest = 2;
            collision(dir, target, c);
        }
    }

    static Santa findNearestSanta() {
        int bestDist = INF;
        Santa santa = new Santa(INF, INF);
        for(Santa s : santas) {
            if(s.out) {         // 탈락하지 않은 산타 중 선택
                continue;
            }
            int dist = getDist(rudolf.x, rudolf.y, s.x, s.y);
            if(bestDist > dist
                || (bestDist == dist && santa.x < s.x)
                    || (bestDist == dist && santa.x == s.x && santa.y < s.y)) {
                bestDist = dist;
                santa = s;
            }
        }
        return santa;
    }

    static int rodolfNextDir(Santa target) {
        int originDist = getDist(rudolf.x, rudolf.y, target.x, target.y);
        int minDiff = INF;
        int bestDir = -1;
        for(int dir = 0; dir < 8; dir++) {
            int nx = rudolf.x + dx[dir];
            int ny = rudolf.y + dy[dir];
            if(isNotBoard(nx, ny)) {
                continue;
            }
            int dist = getDist(nx, ny, target.x, target.y);
            if(originDist <= dist) {
                continue;
            }
            if(minDiff > dist) {
                minDiff = dist;
                bestDir = dir;
            }
        }
        return bestDir;
    }
    // ==========================================
    // 산타 이동
    // ==========================================
    static void moveSantas() {
        for(Santa s : santas) {
            if(s.out || s.rest > 0) {       // 탈락했거나, 기절한 산타는 움직이지 않음
                continue;
            }
            Node next = santaNextDir(s);
            if(next == null) {          // 이동할 수 없는 경우 움직이지 않음
                continue;
            }
            locToSanta.remove(locToKey(s.x, s.y));
            s.x = next.x;
            s.y = next.y;
            locToSanta.put(locToKey(s.x, s.y), s);
            if(s.x == rudolf.x && s.y == rudolf.y) {
                s.score += d;
                s.rest = 2;
                collision((next.dir + 2) % 4, s, d);
            }
        }
    }

    static class Node {
        int x, y;
        int dir;
        Node(int x, int y, int dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }
    }

    static Node santaNextDir(Santa santa) {
        Node best = null;
        int minDist = getDist(santa.x, santa.y, rudolf.x, rudolf.y);
        for (int dir = 0; dir < 4; dir++) {
            int nx = santa.x + dx[dir];
            int ny = santa.y + dy[dir];
            if (isNotBoard(nx, ny) || locToSanta.containsKey(locToKey(nx, ny))) {
                continue;
            }
            int nextDist = getDist(nx, ny, rudolf.x, rudolf.y);
            if (nextDist < minDist) {
                minDist = nextDist;
                best = new Node(nx, ny, dir);
            }
        }
        return best;
    }
    // ===============================================================
    // 충돌
    // ===============================================================
    static void collision(int dir, Santa target, int dist) {
        int nx = target.x + dx[dir] * dist;
        int ny = target.y + dy[dir] * dist;
        if(isNotBoard(nx, ny)) {        // 탈락하는 경우
            target.out = true;
            locToSanta.remove(locToKey(target.x, target.y));
            outSantaCount++;
            return;
        }
        // 이동하려는 위치에 다른 산타가 있는지
        if(locToSanta.containsKey(locToKey(nx, ny))) {
            collision(dir, locToSanta.get(locToKey(nx, ny)), 1);       // 첫 산타 이후 산타는 1칸씩 밀림
        }
        locToSanta.remove(locToKey(target.x, target.y));
        target.x = nx;
        target.y = ny;
        locToSanta.put(locToKey(target.x, target.y), target);
    }
    // ===============================================================
    // 공통
    // ===============================================================
    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
    static int getDist(int x1, int y1, int x2, int y2) {
        return ((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2));
    }
    static int locToKey(int x, int y) {
        return x * (n + 7) + y;
    }
    static void decreaseRest() {
        for(Santa s : santas) {
            if(s.out) {
                continue;
            }
            s.rest = Math.max(s.rest - 1, 0);
        }
    }
    static void accScore() {
        for(Santa s : santas) {
            if(s.out) {
                continue;
            }
            s.score++;
        }
    }
    static String printResult() {
        StringBuilder sb = new StringBuilder();
        for(Santa s : santas) {
            sb.append(s.score).append(' ');
        }
        return sb.toString();
    }
    static void printState() {
        String[][] board = new String[n][n];
        for(int i = 0; i < n; i++) Arrays.fill(board[i], "0");

        board[rudolf.x][rudolf.y] = "R";
        for(int i = 0; i < p; i++) {
            Santa s = santas[i];
            board[s.x][s.y] = "s"+(i + 1);
        }

        for(int i = 0; i < n; i++) System.out.println(Arrays.toString(board[i]));
        System.out.println();
    }
}
