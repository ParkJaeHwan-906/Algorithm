package sep.week2.codetree;

import java.util.*;
import java.io.*;

public class 민트초코우유_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int MINT = 1;
    static final int CHOCO = (1 << 1);
    static final int MILK = (1 << 2);

    static final int[] dx = {-1, 1, 0, 0};
    static final int[] dy = {0, 0, -1, 1};

    static int n, t;
    static int[][] foodBoard;
    static int[][] pietyBoard;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        t = Integer.parseInt(st.nextToken());

        foodBoard = new int[n][n];
        for(int x = 0; x < n; x++) {
            String line = br.readLine().trim();
            for(int y = 0; y < n; y++) {
                char food = line.charAt(y);
                if(food == 'T') {
                    foodBoard[x][y] = MINT;
                } else if(food == 'C') {
                    foodBoard[x][y] = CHOCO;
                } else if (food == 'M') {
                    foodBoard[x][y] = MILK;
                }
            }
        }
        pietyBoard = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) {
                pietyBoard[x][y] = Integer.parseInt(st.nextToken());
            }
        }
        System.out.println(solution());
    }

    static String solution() {
        StringBuilder sb = new StringBuilder();
        while(t-- > 0) {
            List<Rep> reps = morningAndLunch();
            dinner(reps);
            sb.append(getResult());
        }
        return sb.toString();
    }

    static List<Rep> morningAndLunch() {
        List<Rep> reps = new ArrayList<>();
        boolean[][] visited = new boolean[n][n];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(visited[x][y]) {
                    continue;
                }
                Rep rep = makeGroups(x, y, visited);
                reps.add(rep);
            }
        }
        return reps;
    }

    static class Rep {
        int x, y;
        int food;
        int piety;
        Rep(int x, int y, int food, int piety) {
            this.x = x;
            this.y = y;
            this.food = food;
            this.piety = piety;
        }
    }

    static Rep makeGroups(int x, int y, boolean[][] visited) {
        Rep rep = new Rep(x, y, foodBoard[x][y], pietyBoard[x][y]);

        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{x, y});
        visited[x][y] = true;

        int groupSize = 0;
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            groupSize++;
            for(int dir = 0; dir < 4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(isNotBoard(nx, ny) || visited[nx][ny] || foodBoard[nx][ny] != rep.food) {
                    continue;
                }

                // 대표자 갱신
                if(rep.piety < pietyBoard[nx][ny] ||
                        (rep.piety == pietyBoard[nx][ny] && rep.x > nx) ||
                        (rep.piety == pietyBoard[nx][ny] && rep.x == nx && rep.y > ny)) {
                    rep = new Rep(nx, ny, rep.food, pietyBoard[nx][ny]);
                }

                visited[nx][ny] = true;
                q.offer(new int[]{nx, ny});
            }
        }

        // 신앙심 이전
        rep.piety += groupSize;
        pietyBoard[rep.x][rep.y] += groupSize;
        return rep;
    }

    static void dinner(List<Rep> reps) {
        reps.sort((a, b) -> {
            int aBits = Integer.bitCount(a.food);
            int bBits = Integer.bitCount(b.food);
            if(aBits != bBits) {
                return Integer.compare(aBits, bBits);
            }
            if(a.piety != b.piety) {
                return Integer.compare(b.piety, a.piety);
            }
            if(a.x != b.x) {
                return Integer.compare(a.x, b.x);
            }
            return Integer.compare(a.y, b.y);
        });

        boolean[][] defense = new boolean[n][n];
        for(Rep rep : reps) {
            if(defense[rep.x][rep.y]) {               // 이미 이전에 전파를 받은 상태 -> 전파를 진행하지 않음
                continue;
            }
            propagation(rep, defense);
        }
    }

    static void propagation(Rep rep, boolean[][] defense) {
        int beg = rep.piety - 1;        // B - 1
        int dir = rep.piety % 4;        // B % 4
        int x = rep.x;
        int y = rep.y;

        while(beg > 0) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if(isNotBoard(nx, ny)) {        // 격자 밖으로 나가는 경우 전파 종료
                break;
            }
            // 위치 갱신
            x = nx;
            y = ny;
            if(foodBoard[nx][ny] == rep.food) { // 신봉 음식이 완전하게 같은 경우 넘어감
                continue;
            }

            // 전파 가능
            if(beg > pietyBoard[nx][ny]) {              // 강한 전파
                beg -= (pietyBoard[nx][ny] + 1);        // 간절함 -= (y + 1)
                pietyBoard[nx][ny]++;                   // 신앙심 + 1
                foodBoard[nx][ny] = rep.food;           // 완전히 동화
            } else if(beg <= pietyBoard[nx][ny]) {
                pietyBoard[nx][ny] += beg;              // 신앙심 + x
                foodBoard[nx][ny] |= rep.food;          // 음식에 추가로 관심
                beg = 0;                                // 간절함 0
            }
            defense[nx][ny] = true;
        }

        // B = 1 로 갱신
        rep.piety = 1;
        pietyBoard[rep.x][rep.y] = 1;
    }

    static String getResult() {
        int[] sum = new int[(MINT | CHOCO | MILK) + 1];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                sum[foodBoard[x][y]] += pietyBoard[x][y];
            }
        }
        return String.format("%d %d %d %d %d %d %d\n",
                sum[MINT | CHOCO | MILK],
                sum[MINT | CHOCO],
                sum[MINT | MILK],
                sum[CHOCO | MILK],
                sum[MILK],
                sum[CHOCO],
                sum[MINT]);
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}
