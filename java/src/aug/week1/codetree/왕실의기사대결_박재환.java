package aug.week1.codetree;

import java.util.*;
import java.io.*;

public class 왕실의기사대결_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int EMPTY = 0;
    static final int TRAP = 1;
    static final int WALL = 2;

    static final int[] dx = {-1, 0, 1, 0};
    static final int[] dy = {0, 1, 0, -1};

    static class Loc {
        int x, y;
        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean equals(Object o) {
            if(o == this) return true;
            if(!(o instanceof Loc)) return false;
            Loc oLoc = (Loc) o;
            return this.x == oLoc.x && this.y == oLoc.y;
        }

        public int hashCode() {
            return Objects.hash(this.x, this.y);
        }
    }

    static class Knight extends Loc {
        int h, w;           // 방패
        int k;              // 체력
        int accDamage;
        Knight(int x, int y, int h, int w, int k) {
            super(x, y);
            this.h = h;
            this.w = w;
            this.k = k;

            this.accDamage = 0;
        }
        boolean decreaseK(int i) {
            this.k -= i;
            accDamage += i;
            if(k <= 0) return false;
            return true;
        }
    }

    static final Knight DUMMY = new Knight(-1, -1, -1, -1, -1);

    static int l, n, q;
    static int[][] board;
    static List<Knight> knights;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim(), " ");
        l = Integer.parseInt(st.nextToken());       // 격자 크기
        n = Integer.parseInt(st.nextToken());       // 기사 수
        q = Integer.parseInt(st.nextToken());       // 턴 수

        board = new int[l][l];
        for(int x = 0; x < l; x++){
            st = new StringTokenizer(br.readLine().trim(), " ");
            /**
             * 0 : 빈칸
             * 1 : 함정
             * 2 : 벽
             */
            for(int y = 0; y < l; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        knights = new ArrayList<>();
        knights.add(DUMMY);        // DUMMY
        for(int i = 1; i < n + 1; i++) {
            st = new StringTokenizer(br.readLine().trim(), " ");
            // 1 - based -> 0 - based
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());

            Knight knight = new Knight(x, y, h, w, k);
            knights.add(knight);
        }

        // solution
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim(), " ");
            int id = Integer.parseInt(st.nextToken());
            int dir = Integer.parseInt(st.nextToken());
            moveKnight(id, dir);
        }

        int accDamage = 0;
        for(Knight knight : knights) {
            if(knight == DUMMY) continue;
            accDamage += knight.accDamage;
        }
        System.out.println(accDamage);
    }

    static boolean moveKnight(int id, int dir) {
        // 이미 사라진 기사라면 명령 무시
        if(knights.get(id) == DUMMY) return false;

        Queue<Integer> queue = new ArrayDeque<>();
        List<Integer> moveIds = new ArrayList<>();
        boolean[] selected = new boolean[n + 1];

        queue.offer(id);
        selected[id] = true;

        /*
         * 1단계
         * 이동해야 하는 기사들을 모두 찾으면서
         * 이동 가능한지 검사한다.
         */
        while(!queue.isEmpty()) {
            int curId = queue.poll();
            Knight cur = knights.get(curId);

            int nx = cur.x + dx[dir];
            int ny = cur.y + dy[dir];

            // 기사의 이동 후 전체 영역이 격자를 벗어나거나 벽과 충돌
            if(disableMove(cur, nx, ny)) return false;

            moveIds.add(curId);

            for(int nextId = 1; nextId <= n; nextId++) {
                if(selected[nextId]) continue;

                Knight next = knights.get(nextId);
                if(next == DUMMY) continue;

                if(isOverlap(nx, ny, cur.h, cur.w, next)) {
                    selected[nextId] = true;
                    queue.offer(nextId);
                }
            }
        }

        for(int moveId : moveIds) {
            Knight knight = knights.get(moveId);

            knight.x += dx[dir];
            knight.y += dy[dir];
        }

        for(int moveId : moveIds) {
            if(moveId == id) continue;

            Knight knight = knights.get(moveId);
            int traps = searchNearTrap(knight);

            if(!knight.decreaseK(traps)) {
                knights.set(moveId, DUMMY);
            }
        }

        return true;
    }

    static boolean isOverlap(int x, int y, int h, int w, Knight other) {
        boolean overlapX = x < other.x + other.h && other.x < x + h;
        boolean overlapY = y < other.y + other.w && other.y < y + w;

        return overlapX && overlapY;
    }

    static int searchNearTrap(Knight knight) {
        int count = 0;
        for(int x = knight.x; x < knight.x + knight.h; x++) {
            for(int y = knight.y; y < knight.y + knight.w; y++) {
                if(board[x][y] == TRAP) count++;
            }
        }
        return count;
    }

    static boolean disableMove(Knight knight, int nx, int ny) {
        for(int x = nx; x < nx + knight.h; x++) {
            for(int y = ny; y < ny + knight.w; y++) {
                if(x < 0 || y < 0 || x >= l || y >= l) return true;
                if(board[x][y] == WALL) return true;
            }
        }
        return false;
    }
}
