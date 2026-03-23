package mar.week3.codetree;

import java.util.*;
import java.io.*;

public class 민트초코우유_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static final int MINT = 1 << 0;
    static final int CHOCO = 1 << 1;
    static final int MILK = 1 << 2;

    static StringTokenizer st;
    static int n, t;
    static int[][] foodBoard;
    static int[][] believeBoard;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        t = Integer.parseInt(st.nextToken());
        // 신앙 음식
        foodBoard = new int[n][n];
        for(int x=0; x<n; x++) {
            String line = br.readLine().trim();
            for(int y=0; y<n; y++) {
                 char food = line.charAt(y);
                 if(food == 'T') foodBoard[x][y] = MINT;
                 else if(food == 'C') foodBoard[x][y] = CHOCO;
                 else if(food == 'M') foodBoard[x][y] = MILK;
            }

        }
        // 신앙심
        believeBoard = new int[n][n];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) believeBoard[x][y] = Integer.parseInt(st.nextToken());
        }

        solution();
    }

    /**
     * 민트 : T
     * 초코 : C
     * 우유 : M
     * => 비트 마스킹을 활용해서 표현
     * 민트 : 001
     * 초코 : 010
     * 우유 : 100
     */
    static void solution() {
        while(t-- > 0) {
            /**
             * [아침시간]
             * 모든 학생들은 신앙심을 1씩 얻는다.
             * => 점심시간과 통합
             */
            afternoon();
            evening();
            finishDay();
        }
    }
    static class Master implements Comparable<Master> {
        int x, y;
        int food;
        int believe;

        Master(int x, int y, int food, int believe) {
            this.x = x;
            this.y = y;
            this.food = food;
            this.believe = believe;
        }

        public int compareTo(Master o) {
            int thisBitCount = Integer.bitCount(this.food);
            int otherBitCount = Integer.bitCount(o.food);

            if(thisBitCount == otherBitCount) {
                if(this.believe != o.believe) return Integer.compare(o.believe, this.believe);
                if(this.x != o.x) return Integer.compare(this.x, o.x);
                return Integer.compare(this.y, o.y);
            }
            return Integer.compare(thisBitCount, otherBitCount);
        }
    }
    static PriorityQueue<Master> pq;
    static void afternoon() {
        /**
         * [점심시간]
         * 인접한 학생들과 그룹을 형성한다.
         * -> 신봉음식이 완전하게 같은 경우에만 그룸을 형성한다.
         *
         * 그룹에서 대표자 한 명을 선정한다.
         * - 신앙심이 가장 큰 사람
         * - 행, 열이 가장 작은 사람
         *
         * 대표자를 제외한 그룹원들은 각자 신앙심을 1씩 대표자에게 넘긴다.
         * 대표자는 그룹원 수 - 1 만큼 신앙심을 획득한다.
         */
        pq = new PriorityQueue<>();
        boolean[][] checked = new boolean[n][n];
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(checked[x][y]) continue;
                // 아직 그룹을 형성하지 않은 학생이라면
                // 인접영역을 탐색한다.
                Master master =makeGroup(x, y, checked);
                pq.offer(master);
            }
        }
    }
    static final int[] dx = {-1,1,0,0};
    static final int[] dy = {0,0,-1,1};
    static Master makeGroup(int x, int y, boolean[][] checked) {
        Queue<int[]> q = new ArrayDeque<>();
        int food = foodBoard[x][y];

        Master master = new Master(x, y, food, believeBoard[x][y]);
        int groupSize = 1;
        q.offer(new int[] {x, y});
        checked[x][y] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();

            for(int d=0; d<4; d++) {
                int nx = cur[0] + dx[d];
                int ny = cur[1] + dy[d];
                if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                if(foodBoard[nx][ny] != food) continue;
                if(checked[nx][ny]) continue;

                q.offer(new int[] {nx, ny});
                checked[nx][ny] = true;
                groupSize++;

                /**
                 * 그룹의 대표자 구하기
                 */
                if(master.believe < believeBoard[nx][ny]) {
                    master = new Master(nx, ny, food, believeBoard[nx][ny]);
                } else if(master.believe == believeBoard[nx][ny] &&
                        (master.x > nx || (master.x == nx && master.y > ny))) {
                    master = new Master(nx, ny, food, believeBoard[nx][ny]);
                }
            }
        }

        believeBoard[master.x][master.y] += groupSize;
        master.believe = believeBoard[master.x][master.y];
        return master;
    }
    static void evening() {
        boolean[][] defense = new boolean[n][n];
        while(!pq.isEmpty()) {
            Master master = pq.poll();
            if(defense[master.x][master.y]) continue;       // 방어 상태라면 전파를 하지 않음

            // 신앙심을 1만 남기고 간절함으로 변경
            int begging = master.believe - 1;
            int dir = master.believe%4;
            believeBoard[master.x][master.y] = 1;

            spread(master, begging, dir, defense);
        }
    }
    static void spread(Master master, int begging, int dir, boolean[][] defense) {
        Queue<int[]> q = new ArrayDeque<>();

        q.offer(new int[] {master.x, master.y});
        while(!q.isEmpty() && begging > 0) {
            int[] cur = q.poll();

            int nx = cur[0] + dx[dir];
            int ny = cur[1] + dy[dir];
            if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
            if(foodBoard[nx][ny] != master.food) {      // 음식이 다를때만 전파, 같다면 패스하고 다음칸
                int targetBelieve = believeBoard[nx][ny];

                if (begging > targetBelieve) {       // 강한 전파 성공
                    foodBoard[nx][ny] = master.food;
                    believeBoard[nx][ny]++;
                    begging -= (targetBelieve + 1);

                } else if (begging <= targetBelieve) {       // 약한 전파 성공
                    foodBoard[nx][ny] |= master.food;
                    believeBoard[nx][ny] += begging;
                    begging = 0;
                }
                defense[nx][ny] = true;
            }
            q.offer(new int[] {nx, ny});
        }
    }
    static void finishDay() {
        int[] answer = new int[(1<<3)];
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) answer[foodBoard[x][y]] += believeBoard[x][y];
        }

        // 출력 순서
        // 민트초코우유, 민트초코, 민트우유, 초코우유, 우유, 초코, 민트
        sb.append(String.format("%d %d %d %d %d %d %d\n",
                answer[(MINT | CHOCO | MILK)],
                answer[(MINT | CHOCO)],
                answer[(MINT | MILK)],
                answer[(MILK | CHOCO)],
                answer[(MILK)],
                answer[(CHOCO)],
                answer[(MINT)]
                ));
    }
}
