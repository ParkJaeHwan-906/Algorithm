package jun.week5.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:55:30
 * AI 사용 여부 X
 */
public class 민트초코우유_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static final int MINT = 1;
    static final int CHOCO = 1 << 1;
    static final int MILK = 1 << 2;

    static class Student implements Comparable<Student> {
        int x, y;
        int food;
        int believe;

        Student(int x, int y, int food, int believe) {
            this.x = x;
            this.y = y;

            this.food = food;
            this.believe = believe;
        }

        public int compareTo(Student o) {
            int aFood = Integer.bitCount(this.food);
            int bFood = Integer.bitCount(o.food);

            if(aFood != bFood) return Integer.compare(aFood, bFood);
            if(this.believe != o.believe) return Integer.compare(o.believe, this.believe);
            if(this.x != o.x) return Integer.compare(this.x, o.x);
            return Integer.compare(this.y, o.y);
        }
    }
    static class Rep implements Comparable<Rep> {
        int x, y;
        int food;
        int believe;

        Rep(int x, int y, int food, int believe) {
            this.x = x;
            this.y = y;
            this.food = food;
            this.believe = believe;
        }

        public int compareTo(Rep o) {
            int aFood = Integer.bitCount(this.food);
            int bFood = Integer.bitCount(o.food);

            if(aFood != bFood) return Integer.compare(aFood, bFood);
            if(this.believe != o.believe) return Integer.compare(o.believe, this.believe);
            if(this.x != o.x) return Integer.compare(this.x, o.x);
            return Integer.compare(this.y, o.y);
        }
    }
    static int n;
    static Student[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st  = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        int t = Integer.parseInt(st.nextToken());

        board = new Student[n][n];
        for(int x = 0; x < n; x++) {
            String line = br.readLine().trim();
            for(int y = 0; y < n; y++) {
                board[x][y] = new Student(x, y, 0, 0);

                char food = line.charAt(y);
                if(food == 'T') {
                    board[x][y].food = MINT;
                } else if(food == 'C') {
                    board[x][y].food = CHOCO;
                } else if(food == 'M') {
                    board[x][y].food = MILK;
                }
            }
        }

        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) {
                board[x][y].believe = Integer.parseInt(st.nextToken());
            }
        }

        System.out.println(solution(t));
    }

    static final int[] dx = {-1, 1, 0, 0};
    static final int[] dy = {0, 0, -1, 1};

    static PriorityQueue<Rep> reps;
    static String solution(int t) {
        StringBuilder sb = new StringBuilder();

        while(t-- > 0) {
            grouping();
            foodPropagation();
            getResult(sb);
        }

        return sb.toString();
    }

    static void grouping() {
        reps = new PriorityQueue<>();
        boolean[][] visited = new boolean[n][n];

        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(visited[x][y]) continue;
                Rep rep = findGroup(x, y, visited);
                reps.offer(rep);
            }
        }
    }

    static Rep findGroup(int x, int y, boolean[][] visited) {
        Student rep = board[x][y];
        Queue<Student> q = new ArrayDeque<>();

        q.offer(rep);
        visited[rep.x][rep.y] = true;

        int groupSize = 0;
        while(!q.isEmpty()) {
            Student cur = q.poll();
            groupSize++;
            // 대표자 교체
            if(rep.believe < cur.believe ||
                    (rep.believe == cur.believe && rep.x > cur.x) ||
                    (rep.believe == cur.believe && rep.x == cur.x && rep.y > cur.y)) {
                rep = cur;
            }

            for(int dir = 0; dir < 4; dir++) {
                int nx = cur.x + dx[dir];
                int ny = cur.y + dy[dir];
                if(isNotBoard(nx, ny)) continue;
                if(visited[nx][ny]) continue;
                if(cur.food != board[nx][ny].food) continue;
                visited[nx][ny] = true;
                q.offer(board[nx][ny]);
            }
        }

        rep.believe += groupSize;
        return new Rep(rep.x, rep.y, rep.food, rep.believe);
    }

    static void foodPropagation() {
        boolean[][] defense = new boolean[n][n];
        while(!reps.isEmpty()) {
            Rep rep = reps.poll();
            if(defense[rep.x][rep.y]) continue;

            doPropagation(rep, defense);
        }
    }

    static void doPropagation(Rep rep, boolean[][] defense) {
        int x = rep.x, y = rep.y;
        int begging = rep.believe - 1;      // 간절함
        int propagationDir = rep.believe % 4;

        while(begging > 0) {
            int nx = x + dx[propagationDir];
            int ny = y + dy[propagationDir];

            if(isNotBoard(nx, ny)) break;

            Student cur = board[nx][ny];
            if(cur.food != rep.food) {              // 신봉 음식이 다름 -> 전파
                if (cur.believe < begging) {        // 강한 전파
                    cur.believe++;
                    cur.food = rep.food;
                    begging -= cur.believe;
                } else {                            // 약한 전파
                    cur.believe += begging;
                    cur.food |= rep.food;
                    begging = 0;
                }
                defense[nx][ny] = true;
            }
            x = nx; y = ny;
        }

        board[rep.x][rep.y].believe = 1;
    }

    static void getResult(StringBuilder sb) {
        int[] result = new int[(1 << 4) - 1];

        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                Student cur = board[x][y];
                result[cur.food] += cur.believe;
            }
        }

        sb.append(String.format("%d %d %d %d %d %d %d\n",
                result[MINT | CHOCO | MILK],
                result[MINT | CHOCO], result[MINT | MILK], result[CHOCO | MILK],
                result[MILK], result[CHOCO], result[MINT]));
    }

    static boolean isNotBoard(int x, int y) { return x < 0 || y < 0 || x >= n || y >= n; }
}
