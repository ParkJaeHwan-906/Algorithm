package jul.week1.codetree;

import java.util.*;
import java.io.*;

public class 아기바다거북의대모험_박재환 {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static class Turtle {
        int id;
        int x, y;

        int outTime;
        boolean isLive;

        Turtle(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;

            this.outTime = -1;
            this.isLive = true;
        }
    }

    static class Volcano {
        int x, y;
        int p;          // 폭발 임계치
        int curP;       // 현재 압력

        boolean explosion;

        Volcano(int x, int y, int p, int curP) {
            this.x = x;
            this.y = y;
            this.p = p;
            this.curP = curP;

            this.explosion = false;
        }

        void addCurP(int i) {
            this.curP += i;
        }

        void reset() {
            if(this.explosion) {
                this.explosion = false;
                this.curP = 0;
            }
        }
    }

    static int n, m, k;
    static int[][] board;
    static List<Turtle> turtles;
    static List<Volcano> volcanoes;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());   // 격자 크기
        m = Integer.parseInt(st.nextToken());   // 거북이 수
        k = Integer.parseInt(st.nextToken());   // 화산 수

        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        turtles = new ArrayList<>();
        for(int id = 1; id <= m; id++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            turtles.add(new Turtle(id, x, y));
        }

        volcanoes = new ArrayList<>();
        for(int i = 0; i < k; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int p = Integer.parseInt(st.nextToken());
            volcanoes.add(new Volcano(x, y, p, 0));
        }
        System.out.println(solution());
    }

    static int outTurtle;
    static String solution() {
        outTurtle = 0;
        int time = 0;
        while(outTurtle < m && ++time <= 100) {     // 거북이가 모두 나가거나, 100턴을 초과하는 경우 중지
            // 거북이 이동
            moveTurtles(time);
            // 화산 압력 증가
            addPVolcanoes();
            // 화산 폭발
            int[][] explosionBoard = explosions();
            // 거북이 화석
            rockTurtle(explosionBoard);
            // 화산 초기화
            resetVolcanoes();
        }

        StringBuilder sb = new StringBuilder();
        for(Turtle turtle : turtles) sb.append(turtle.outTime).append('\n');
        return sb.toString();
    }

    static void moveTurtles(int time) {
        int[][] turtleBoard = new int[n][n];
        for(Turtle turtle : turtles) {
            if(turtle.outTime != -1) continue;      // 탈출한 거북이는 제외
            turtleBoard[turtle.x][turtle.y] = turtle.id;
        }

        for(Turtle turtle : turtles) {
            if(!turtle.isLive) continue;        // 화석이 된 거북이는 패스
            if(turtle.outTime != -1) continue;  // 탈출에 성공한 거북이인 경우

            Node next = nextTurtleLoc(turtle, turtleBoard);
            if(next == null) continue;      // 이동 불가
            turtleBoard[turtle.x][turtle.y] = 0;
            if(next.x == n - 1 && next.y == n - 1) {    // 안식처에 도달한 경우 바로 삭제 및 시간 갱신
                turtle.outTime = time;
            } else {                                    // 안식처에 도달하지 못한 경우 위치 갱신
                turtleBoard[next.x][next.y] = turtle.id;
            }
            turtle.x = next.x;
            turtle.y = next.y;
        }
    }

    static class Node {
        int x, y;
        Node prev;

        Node(int x, int y, Node prev) {
            this.x = x;
            this.y = y;
            this.prev = prev;
        }
    }

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};
    static Node nextTurtleLoc(Turtle turtle, int[][] turtleBoard) {
        Queue<Node> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];

        // 시작 위치
        q.offer(new Node(turtle.x, turtle.y, null));
        visited[turtle.x][turtle.y] = true;

        while(!q.isEmpty()) {
            Node cur = q.poll();

            if(cur.x == n - 1 && cur.y == n - 1) {      // 안식처에 도달한 경우
                return traceBack(cur);
            }

            for(int dir = 0; dir < 4; dir++) {
                int nx = cur.x + dx[dir];
                int ny = cur.y + dy[dir];
                if(isNotBoard(nx, ny)) continue;
                if(visited[nx][ny]) continue;
                if(board[nx][ny] == 1 || turtleBoard[nx][ny] > 0) continue;     // 산호가 있거나, 다른 거북이가 있는 경우 이동 불가

                visited[nx][ny] = true;
                q.offer(new Node(nx, ny, cur));
            }
        }

        return null;
    }

    static Node traceBack(Node cur) {
        while(cur.prev.prev != null) cur = cur.prev;
        return cur;
    }

    static void addPVolcanoes() {
        for(Volcano volcano : volcanoes) volcano.addCurP(10);
    }

    static int[][] explosions() {
        int[][] explosionBoard = new int[n][n];     // 열기 기록
        while(true) {
            List<Volcano> explosions = new ArrayList<>();       // 폭발할 화산 기록
            for(Volcano volcano : volcanoes) {
                if(volcano.explosion) continue;     // 이번턴에 이미 폭발한 화산은 제거
                if(volcano.curP + explosionBoard[volcano.x][volcano.y] >= volcano.p) explosions.add(volcano);
            }
            if(explosions.isEmpty()) break;     // 더 이상 폭발할 화산이 없다면 연쇄반응 종료
            explosion(explosions, explosionBoard);
        }
        return explosionBoard;
    }

    static void explosion(List<Volcano> explosions, int[][] explosionBoard) {
        for(Volcano volcano : explosions) {
            // 상하좌우로 폭발
            explosionBoard[volcano.x][volcano.y] = volcano.p;
            propagation(volcano.x, volcano.y, 0, volcano.p, explosionBoard);
            propagation(volcano.x, volcano.y, 1, volcano.p, explosionBoard);
            propagation(volcano.x, volcano.y, 2, volcano.p, explosionBoard);
            propagation(volcano.x, volcano.y, 3, volcano.p, explosionBoard);
            volcano.explosion = true;
        }
    }

    static void propagation(int x, int y, int dir, int p, int[][] explosionBoard) {
        while(true) {
            p /= 2;
            if(p == 0) break;
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if(isNotBoard(nx, ny)) break;       // 격자를 벗어나는 경우
            if(board[nx][ny] == 1) break;       // 산호초를 만나는 경우
            explosionBoard[nx][ny] += p;
        }
    }

    static void rockTurtle(int[][] explosionBoard) {
        for(Turtle turtle : turtles) {
            if(!turtle.isLive || turtle.outTime != -1) continue;        // 이미 죽었거나, 탈출한 거북이
            if(explosionBoard[turtle.x][turtle.y] >= 20) turtle.isLive = false;
        }
    }

    static void resetVolcanoes() {
        for(Volcano volcano : volcanoes) {
            if(volcano.explosion) volcano.reset();
        }
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}
