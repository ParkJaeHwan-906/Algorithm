package apr.week1.codetree;

import java.util.*;
import java.io.*;

public class 아기바다거북의대모험_해저화산지대_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static class Turtle {
        int id;
        int x, y;
        boolean exit;
        boolean stone;

        Turtle(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;

            this.exit = false;
            this.stone = false;
        }
    }
    static class Volcano {
        int id;
        int x, y;
        int power;
        int charge;

        Volcano(int id, int x, int y, int power) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.power = power;

            this.charge = 0;
        }
    }
    static StringTokenizer st;
    static int n, m, k;
    static int[][] board;
    static Turtle[] turtles;
    static int[][] turtleBoard;
    static Volcano[] volcanoes;
    static int[][] volcanoBoard;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 격자 크기
        m = Integer.parseInt(st.nextToken());       // 거북이 수
        k = Integer.parseInt(st.nextToken());       // 화산 수

        // 격자 정보
        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        // 거북이 정보
        turtles = new Turtle[m];
        turtleBoard = new int[n][n];
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            Turtle turtle = new Turtle(i + 1, x, y);
            turtles[i] = turtle;
            turtleBoard[x][y] = i + 1;
        }
        
        // 화산 정보
        volcanoes = new Volcano[k];
        volcanoBoard = new int[n][n];
        for(int i = 0; i < k; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int power = Integer.parseInt(st.nextToken());
            Volcano volcano = new Volcano(i + 1, x, y, power);
            volcanoes[i] = volcano;
            volcanoBoard[x][y] = i + 1;
        }

        // solution
        int[] result = solution();
        for(int i : result) sb.append(i).append('\n');
    }
    static int[] outTime;
    static int[] solution() {
        outTime = new int[m];
        Arrays.fill(outTime, -1);

        int time = 0;
        while(++time <= 100) {
            // 1. 거북이 이동
            moveTurtles(time);
            // 2. 압력 증가
            increasePressure();
            // 3. 화산 분출
            shootVolcanoes();
        }

        return outTime;
    }

    static void moveTurtles(int time) {
        for(Turtle t : turtles) {
            if(t.stone || t.exit) continue;     // 돌이 되었거나, 탈출한 거북이는 계산 X

            // 현재 거북이 이동
            turtleBoard[t.x][t.y] = 0;
            int[] nextLoc = moveTurtle(t);
            if(nextLoc == null) {
                turtleBoard[t.x][t.y] = t.id;
                continue;       // 이동 불가
            }

            // 이동 가능
            t.x = nextLoc[0];
            t.y = nextLoc[1];

            if(t.x == n - 1 && t.y == n - 1) {      // 탈출
                t.exit = true;
                outTime[t.id - 1] = time;
                continue;
            }

            // 다시 격자에 표시
            turtleBoard[t.x][t.y] = t.id;
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
    static int[] moveTurtle(Turtle t) {
        Queue<Node> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];

        int sx = t.x;
        int sy = t.y;
        q.offer(new Node(sx, sy, null));
        visited[sx][sy] = true;

        while(!q.isEmpty()) {
            Node cur = q.poll();

            if(cur.x == n -1 && cur.y == n - 1) {
                return nextLocation(cur);
            }

            for(int dir = 0; dir < 4; dir++) {
                int nx = cur.x + dx[dir];
                int ny = cur.y + dy[dir];
                if(isNotBoard(nx, ny)) continue;        // 격자를 벗어나는 경우
                if(visited[nx][ny]) continue;           // 이미 방문했던 칸인 경우
                if(board[nx][ny] == 1) continue;        // 벽으로 막힌 경우
                if(turtleBoard[nx][ny] != 0) continue;  // 다른 거북이가 있는 경우

                visited[nx][ny] = true;
                q.offer(new Node(nx, ny, cur));
            }
        }
        return null;
    }
    static int[] nextLocation(Node cur) {
        while(cur.prev.prev != null) cur = cur.prev;
        return new int[] {cur.x, cur.y};
    }
    static void increasePressure() {
        for(Volcano v : volcanoes) {
            v.charge += 10;
        }
    }
    static void shootVolcanoes() {
        int[][] shootState = new int[n][n];

        Queue<Volcano> q = new ArrayDeque<>();
        boolean[] shoot = new boolean[k];

        for(Volcano v : volcanoes) {
            if(v.charge >= v.power) {
                q.offer(v);
                shoot[v.id - 1] = true;
            }
        }

        while(!q.isEmpty()) {
            Volcano v = q.poll();
            shootVolcano(v, shootState);

            // 새로운 화산 갱신
            for(Volcano v1 : volcanoes) {
                if(shoot[v1.id - 1]) continue;

                if(v1.charge + shootState[v1.x][v1.y] >= v1.power) {
                    q.offer(v1);
                    shoot[v1.id - 1] = true;
                }
            }
        }

        for(Turtle t : turtles) {
            if (t.stone || t.exit) continue;     // 돌이 되었거나, 탈출한 거북이는 계산 X
            if(shootState[t.x][t.y] >= 20) {
                t.stone = true;
            }
        }

        for(int i = 0; i < k; i++) {
            if(shoot[i]) volcanoes[i].charge = 0;
        }
    }
    static void shootVolcano(Volcano v, int[][] shootState) {
        int[][] temp = new int[n][n];
        temp[v.x][v.y] = v.power;

        // 위
        int j = v.x;
        while(--j >= 0) {
            if(board[j][v.y] == 1) break;
            temp[j][v.y] += (temp[j + 1][v.y] / 2);
        }

        // 아래
        j = v.x;
        while(++j < n) {
            if(board[j][v.y] == 1) break;
            temp[j][v.y] += (temp[j - 1][v.y] / 2);
        }

        // 오른쪽
        j = v.y;
        while(--j >= 0) {
            if(board[v.x][j] == 1) break;
            temp[v.x][j] += (temp[v.x][j + 1] / 2);
        }

        // 왼쪽
        j = v.y;
        while(++j < n) {
            if(board[v.x][j] == 1) break;
            temp[v.x][j] += (temp[v.x][j - 1] / 2);
        }

        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) shootState[x][y] += temp[x][y];
        }

//        for(int[] arr : shootState) System.out.println(Arrays.toString(arr));
//        System.out.println();
    }
    // ===
    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}