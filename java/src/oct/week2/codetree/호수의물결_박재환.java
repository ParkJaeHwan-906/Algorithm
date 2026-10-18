package oct.week2.codetree;

import java.util.*;
import java.io.*;

public class 호수의물결_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static int n, m, k;
    static List<Catapult> catapults;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        int t = Integer.parseInt(br.readLine().trim());
        for(int tc = 1; tc <= t; tc++) {
            st = new StringTokenizer(br.readLine().trim());
            set(st);
            for(int i = 0; i < k; i++) {
                st = new StringTokenizer(br.readLine().trim());
                addCatapult(st);
            }
            sb.append('#').append(tc).append(' ').append(solution()).append('\n');
        }
        System.out.print(sb);
    }

    static class Catapult {
        int col;
        int startTime;
        int between;
        int weight;
        int dist;
        int stones;
        int throwTIme;
        Catapult(int col, int startTime, int between, int weight, int dist, int stones) {
            this.col = col;
            this.startTime = startTime;
            this.between = between;
            this.weight = weight;
            this.dist = dist;
            this.stones = stones;
            this.throwTIme = startTime;
        }
    }

    static void set(StringTokenizer st) {
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        catapults = new ArrayList<>();
    }

    static void addCatapult(StringTokenizer st) {
        int col = Integer.parseInt(st.nextToken());
        int startTime = Integer.parseInt(st.nextToken());
        int between = Integer.parseInt(st.nextToken());
        int weight = Integer.parseInt(st.nextToken());
        int dist = Integer.parseInt(st.nextToken());
        int stones = Integer.parseInt(st.nextToken());
        catapults.add(new Catapult(col, startTime, between, weight, dist, stones));
    }

    static class Stone {
        int x, y;
        int weight;
        int dist;
        Stone(int x, int y, int weight, int dist) {
            this.x = x;
            this.y = y;
            this.weight = weight;
            this.dist = dist;
        }
        void move() {
            x = Math.max(0, x - 1);
            dist--;
        }
    }

    static int finishedCatapult;
    static int progressStone;
    static List<Stone> stones;
    static int[][] currentBoard;
    static int[][] muddyBoard;
    static String solution() {
        StringBuilder sb = new StringBuilder();
        stones = new  ArrayList<>();
        muddyBoard = new int[n][m];
        finishedCatapult = 0;
        progressStone = 0;
        int turn = 0;
        while(++turn <= 50 && (finishedCatapult < k || progressStone > 0)) {
            // 1. 돌 이동
            moveStones();
            // 2. 돌 발사
            throwStones(turn);
            // 3. 돌 낙하
            dropStones();
            // 4. 점수 계산
            sb.append(getScore()).append(' ');
            // 5. 흙탕물 가라앉기
            sinkMuddy();
            // 6. 물살
            flowMuddy();
            // 7. 흙탕물 일기
            makeMuddy();
        }
        return sb.toString();
    }

    static void moveStones() {
        for(Stone stone : stones) {
            if(stone.dist == -1)
                continue;
            stone.move();
        }
    }

    static void throwStones(int turn) {
        for(Catapult catapult : catapults) {
            if(catapult.stones == 0)
                continue;
            if(catapult.throwTIme != turn)
                continue;
            Stone stone = new Stone(n - 1, catapult.col, catapult.weight, catapult.dist - 1);
            stones.add(stone);
            progressStone++;
            if(--catapult.stones == 0) {
                finishedCatapult++;
                continue;
            }
            catapult.throwTIme = catapult.throwTIme + catapult.between;
        }
    }

    static void dropStones() {
        currentBoard = new int[n][m];
        for(Stone stone : stones) {
            if(stone.dist == 0) {
                progressStone--;
                stone.dist = -1;
                spread(stone);
            }
        }
    }

    static void spread(Stone stone) {
        int origin = 3 * stone.weight;
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                int dist = Math.abs(stone.x - x) + Math.abs(stone.y - y);
                currentBoard[x][y] += Math.max(
                        0,
                        (origin - (5 * dist))
                );
            }
        }
    }

    static int getScore() {
        int total = 0;
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                total += Math.max(0, currentBoard[x][y] - muddyBoard[x][y]);
            }
        }
        return total;
    }

    static void sinkMuddy() {
        int[][] temp = new int[n][m];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                if(muddyBoard[x][y] == 0)
                    continue;
                temp[x][y] = Math.max(
                        0,
                        muddyBoard[x][y] - (getAdjMuddy(x, y) ? 1 : 2)
                );
            }
        }
        muddyBoard = temp;
    }

    static boolean getAdjMuddy(int x, int y) {
        for(int dir = 0; dir < 4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if(isNotBoard(nx, ny))
                return false;
            if(muddyBoard[nx][ny] == 0)
                return false;
        }
        return true;
    }

    static void flowMuddy() {
        int[][] temp = new int[n][m];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m - 1; y++) {
                temp[x][y] = muddyBoard[x][y + 1];
            }
        }
        muddyBoard = temp;
    }

    static void makeMuddy() {
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                if(currentBoard[x][y] == 0)
                    continue;
                muddyBoard[x][y] += getAvg(x, y);
            }
        }
    }

    static int getAvg(int x, int y) {
        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][m];
        q.offer(new int[]{x, y});
        visited[x][y] = true;
        int sum = currentBoard[x][y];
        int count = 1;
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            for(int dir = 0; dir < 4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(isNotBoard(nx, ny) || visited[nx][ny])
                    continue;
                if(currentBoard[nx][ny] == 0)
                    continue;
                sum += currentBoard[nx][ny];
                count++;
                visited[nx][ny] = true;
                q.offer(new int[]{nx, ny});
            }
        }
        return sum / count;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= m;
    }
}
