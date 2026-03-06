package mar.week1.boj;

import java.util.*;
import java.io.*;

public class 어른상어_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    // [위, 아래, 왼쪽, 오른쪽]
    static final int[] dx = {-1,1,0,0};
    static final int[] dy = {0,0,-1,1};

    static class Shark implements Comparable<Shark> {
        int id;
        int x, y;
        int dir;

        Shark(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.dir = 0;
        }

        Shark(int id, int x, int y, int dir) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

        /**
         * 같은 칸에 있다면, id가 작은 상어가 우선 순위를 가짐
         */
        public int compareTo(Shark o) {
            return Integer.compare(this.id, o.id);
        }
    }
    static StringTokenizer st;
    static int n, m, k;
    static int[][] board;
    static int[][] lastUpdate;
    static Map<Integer, Shark> sharks;      // 상어 id, 상어
    static Map<Integer, Shark> sharkLoc;    // 위치, 상어
    static int[][][] movePriority;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 격자 크기
        m = Integer.parseInt(st.nextToken());       // 상어 수
        k = Integer.parseInt(st.nextToken());       // 냄새 지속 시간
        /**
         * 각 상어의 우선순위를 기록할 자료구조
         * 냄새 기록은 board 에 - 몇 번 상어의 냄새인지
         * 각 냄새의 지속 시간은 별도로 Queue 에 넣어서 관리 -> 시간 기준으로 시뮬레이션
         * 상어의 위치 -> Map<Inteter, List<Shark>> 관리?
         */
        board = new int[n][n];
        lastUpdate = new int[n][n];
        sharks = new HashMap<>();
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
                if(board[x][y] > 0) {
                    Shark shark = new Shark(board[x][y], x, y);
                    sharks.put(shark.id, shark);
                    board[x][y] = 0;
                }
            }
        }
        st = new StringTokenizer(br.readLine().trim());
        for(int i=1; i<m+1; i++) {
            Shark shark = sharks.get(i);
            shark.dir = Integer.parseInt(st.nextToken())-1;
        }
        movePriority = new int[m+1][4][4];        // [상어id][방향][우선순위]
        for(int i=1; i<m+1; i++) {
            for(int d=0; d<4; d++) {
                st = new StringTokenizer(br.readLine().trim());
                for(int pq=0; pq<4; pq++) movePriority[i][d][pq] = Integer.parseInt(st.nextToken())-1;
            }
        }
        System.out.println(lastShark());
    }
    /**
     * 1. 냄새 뿌리기
     * 2. 이동
     * 3. 갱신
     */
    static class Smell {
        int id;
        int x, y;
        int expire;

        Smell(int id, int x, int y, int expire) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.expire = expire;
        }
    }
    static Queue<Smell> smells;
    static int lastShark() {
        smells = new ArrayDeque<>();
        int time = 0;
        while(time <= 1000) {
            if(sharks.size() == 1) break;
            while(!smells.isEmpty() && smells.peek().expire <= time) {
                Smell s = smells.poll();
                if(s.expire - k == lastUpdate[s.x][s.y]) {
                    board[s.x][s.y] = 0;
                }
            }
            spreadSmell(time);
            moveShark();
            time++;
        }
        return time == 1001 ? -1 : time;
    }
    static void spreadSmell(int time) {
        /**
         * 현재 시간을 기준으로 냄새가 유지되는 시간을 포함해 기록
         * -> 냄새가 덮어씌어지는 경우가 있음
         */
        int expire = time + k;
        for(Shark shark : sharks.values()) {
            int id = shark.id;
            int x = shark.x;
            int y = shark.y;
            board[x][y] = id;
            lastUpdate[x][y] = time;
            Smell smell = new Smell(id, x, y, expire);
            smells.offer(smell);
        }
    }
    static final int HASH = 27;
    static void moveShark() {
        sharkLoc = new HashMap<>();
        for(Shark shark : sharks.values()) {
            int[] candidateDir = movePriority[shark.id][shark.dir];
            /**
             * 아무런 냄새가 없는 칸으로 이동이 1순위
             * 같은 냄새가 있는 칸으로 이동이 2순위
             */
            int[] noSmell = null;
            int[] sameSmell = null;
            for(int dir : candidateDir) {
                int nx = shark.x + dx[dir];
                int ny = shark.y + dy[dir];
                if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                if(board[nx][ny] == 0) {
                    noSmell = new int[] {nx, ny, dir};
                    break;
                } else if(sameSmell == null && board[nx][ny] == shark.id) {
                    sameSmell = new int[] {nx, ny, dir};
                }
            }
            if(noSmell != null) {
                int x =  noSmell[0];
                int y =  noSmell[1];
                int dir = noSmell[2];
                shark.x = x;
                shark.y = y;
                shark.dir = dir;
                int key = x * HASH + y;
                Shark prev = sharkLoc.get(key);
                if(prev == null) sharkLoc.put(key, shark);
                else {
                    if(prev.id > shark.id) {
                        sharkLoc.put(key, shark);
                    }
                }
            } else {
                int x =  sameSmell[0];
                int y =  sameSmell[1];
                int dir = sameSmell[2];
                shark.x = x;
                shark.y = y;
                shark.dir = dir;
                int key = x * HASH + y;
                Shark prev = sharkLoc.get(key);
                if(prev == null) sharkLoc.put(key, shark);
                else {
                    if(prev.id > shark.id) {
                        sharkLoc.put(key, shark);
                    }
                }
            }
        }

        sharks.clear();
        for(Shark s : sharkLoc.values()) sharks.put(s.id, s);
    }
}
