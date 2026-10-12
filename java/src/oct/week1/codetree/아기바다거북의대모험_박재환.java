package oct.week1.codetree;

import java.util.*;
import java.io.*;

public class 아기바다거북의대모험_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static class Loc {
        int x, y;
        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public boolean equals(Object o) {
            if(this == o) {
                return true;
            }
            if(!(o instanceof Loc)) {
                return false;
            }
            Loc loc = (Loc)o;
            return x == loc.x && y == loc.y;
        }
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    static class Turtle extends Loc {
        int id;
        int outTime;
        boolean dead;
        Turtle(int id, int x, int y) {
            super(x, y);
            this.id = id;
            this.outTime = -1;
            this.dead = false;
        }
    }

    static class Volcano extends Loc {
        int p;
        int curP;
        Volcano(int x, int y, int p) {
            super(x, y);
            this.p = p;
            this.curP = 0;
        }
    }

    static int n, m, k;
    static int[][] board;
    static Map<Integer, Turtle> idToTurtles;
    static Map<Loc, Turtle> locToTurtles;
    static Map<Loc, Volcano> locToVolcanos;
    static Loc exit;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 격자 크기
        m = Integer.parseInt(st.nextToken());       // 거북이 수
        k = Integer.parseInt(st.nextToken());       // 화산 수
        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) {
                /**
                 * 0 : 빈 공간
                 * 1 : 산호초 (이동 불가)
                 */
                board[x][y] = Integer.parseInt(st.nextToken());
            }
        }
        exit = new Loc(n - 1, n - 1);
        idToTurtles = new HashMap<>();
        locToTurtles = new HashMap<>();
        for(int i = 1; i <= m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            Turtle turtle = new Turtle(i, x, y);
            idToTurtles.put(i, turtle);
            locToTurtles.put(new Loc(x, y), turtle);
        }
        locToVolcanos = new HashMap<>();
        for(int i = 0; i < k; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int p = Integer.parseInt(st.nextToken());
            Volcano volcano = new Volcano(x, y, p);
            locToVolcanos.put(new Loc(x, y), volcano);
        }
        System.out.print(solution());
    }

    static int outTurtles;
    static int deadTurtles;
    static String solution() {
        outTurtles = 0;
        deadTurtles = 0;
        int time = 0;
        while(outTurtles + deadTurtles < m && time <= 100) {
            time++;
            // 1. 거북이 이동
            moveTurtles(time);
            // 2. 화산 압력 증가
            accP();
            // 3, 화산 분출
            // 3-1. 거북이 화석화
            explosions();

            printState();
        }
        return result();
    }

    static void moveTurtles(int time) {
        for(int id = 1; id <= m; id++) {
            Turtle turtle = idToTurtles.get(id);
            if(turtle.outTime != -1 || turtle.dead) {
                continue;
            }
            Node nextNode = moveTurtle(turtle);
            if(nextNode == null) {
                continue;
            }
            locToTurtles.remove(new Loc(turtle.x, turtle.y));
            turtle.x = nextNode.x;
            turtle.y = nextNode.y;
            if(exit.equals(new Loc(turtle.x, turtle.y))) {
                outTurtles++;
                turtle.outTime = time;
                continue;
            }
            locToTurtles.put(new Loc(turtle.x, turtle.y), turtle);
        }
    }

    static class Node extends Loc{
        Node prev;
        Node(int x, int y, Node prev) {
            super(x, y);
            this.prev = prev;
        }
    }

    static Node moveTurtle(Turtle turtle) {
        Queue<Node> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];
        q.offer(new Node(turtle.x, turtle.y, null));
        visited[turtle.x][turtle.y] = true;
        while(!q.isEmpty()) {
            Node cur = q.poll();
            if(cur.equals(exit)) {
                return findNextNode(cur);
            }
            for(int dir = 0; dir < 4; dir++) {
                int nx = cur.x + dx[dir];
                int ny = cur.y + dy[dir];
                if(isNotBoard(nx, ny)) {
                    continue;
                }
                if(locToTurtles.containsKey(new Loc(nx, ny))
                    || board[nx][ny]  == 1) {
                    continue;
                }
                if(visited[nx][ny]) {
                    continue;
                }
                visited[nx][ny] = true;
                q.offer(new Node(nx, ny, cur));
            }
        }
        return null;
    }

    static Node findNextNode(Node cur) {
        while(cur.prev.prev != null) {
            cur = cur.prev;
        }
        return cur;
    }

    static void accP() {
        for(Volcano volcano : locToVolcanos.values()) {
            volcano.curP += 10;
        }
    }

    static void explosions() {
        Set<Volcano> alreadyExplosion = new HashSet<>();
        int[][] extraP = new int[n][n];
        while(true) {
            boolean explosion = false;
            for(Volcano volcano : locToVolcanos.values()) {
                if(alreadyExplosion.contains(volcano)) {
                    continue;
                }
                if(extraP[volcano.x][volcano.y] + volcano.curP >= volcano.p) {
                    System.out.printf("[Volcano] (%d, %d)\n", volcano.x, volcano.y);
                    explosion = true;
                    explosion(volcano, extraP);
                    alreadyExplosion.add(volcano);
                }
            }
            if(!explosion) {
                break;
            }
        }
        deadTurtles(extraP);
        for(Volcano volcano : alreadyExplosion) {
            volcano.curP = 0;
        }
    }

    static void explosion(Volcano volcano, int[][] extra) {
        int[][] temp = new int[n][n];
        for(int dir = 0; dir < 4; dir++) {
            int x = volcano.x;
            int y = volcano.y;
            int p = volcano.p;
            while(p > 0) {
                p /= 2;
                int nx = x + dx[dir];
                int ny = y + dy[dir];
                if(isNotBoard(nx, ny) || board[nx][ny] == 1) {
                    break;
                }
                temp[nx][ny] += p;
                x = nx;
                y = ny;
            }
        }
        temp[volcano.x][volcano.y] = volcano.p;
        merge(extra, temp);
    }

    static void merge(int[][] origin, int[][] temp) {
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                origin[x][y] += temp[x][y];
            }
        }
    }

    static void deadTurtles(int[][] extra) {
        for(int[] arr : extra) System.out.println(Arrays.toString(arr));
        for(Turtle turtle : locToTurtles.values()) {
            if(turtle.outTime != -1 || turtle.dead) {
                continue;
            }
            if(extra[turtle.x][turtle.y] >= 20) {
                turtle.dead = true;
                deadTurtles++;
            }
        }
    }

    static String result() {
        StringBuilder sb = new StringBuilder();
        for(int id = 1; id <= m; id++) {
            sb.append(idToTurtles.get(id).outTime).append('\n');
        }
        return sb.toString();
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }

    static void printState() {
        for(Turtle turtle : idToTurtles.values()) {
            System.out.printf("[Turtle %d] loc : (%d, %d), outTime : %d, stone : %s\n", turtle.id, turtle.x, turtle.y, turtle.outTime, turtle.dead);
        }
        System.out.println();
    }
}
