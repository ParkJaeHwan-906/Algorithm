package mar.week2.codetree;

import java.util.*;
import java.io.*;

public class 왕실의기사대결_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    /**
     *  L x L 격자 (1,1) (L,L)
     *  - 빈칸
     *  - 함정
     *  - 벽 (격자 밖도 벽)
     *
     *  기사의 초기 위치 (r, c)
     *  (r, c)를 좌측 상단으로 해 h x w 크기의 직사각형 형태
     *  채력은 k
     */
    static StringTokenizer st;
    static int l, n, q;
    static int[][] board;
    static int[][] knightBoard;
    static Knight[] knights;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        l = Integer.parseInt(st.nextToken());       // 격자 크기
        n = Integer.parseInt(st.nextToken());       // 기사 수
        q = Integer.parseInt(st.nextToken());       // 질의 수

        board = new int[l][l];
        for(int x=0; x<l; x++) {
            st = new StringTokenizer(br.readLine().trim());
            /**
             * 0 : 빈칸
             * 1 : 함정
             * 2 : 벽
             */
            for(int y=0; y<l; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        knightBoard = new int[l][l];
        knights = new Knight[n+1];
        for(int i=1; i<n+1; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken())-1;
            int y = Integer.parseInt(st.nextToken())-1;
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());

            int x2 = x + h;
            int y2 = y + w;
            for(int nx=x; nx<x2; nx++) {
                for(int ny=y; ny<y2; ny++) {
                    knightBoard[nx][ny] = i;
                }
            }
            Knight knight = new Knight(x, y, x2, y2, k);
            knights[i] = knight;
        }
        for(int i=0; i<q; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int kId = Integer.parseInt(st.nextToken());
            int dir = Integer.parseInt(st.nextToken());
            moveKnight(kId, dir);
        }

        int sum = 0;
        for(int i=1; i<n+1; i++) {
            if(knights[i].isLive) sum += knights[i].damage;
        }
        System.out.println(sum);
    }
    static class Knight {
        int x1, y1;
        int x2, y2;
        int hp;
        int damage;
        boolean isLive;

        Knight(int x1, int y1, int x2, int y2, int hp) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.hp = hp;

            this.damage = 0;
            this.isLive = true;
        }

        void addDamage(int damage) {
            this.damage += damage;
            if(this.damage >= this.hp) this.isLive = false;
        }
    }
    static final int[] dx = {-1,0,1,0};
    static final int[] dy = {0,1,0,-1};
    static Set<Integer> moveKnights;
    static Set<Integer> damageKnights;
    static void moveKnight(int kId, int dir) {
        /**
         * 상하좌우 한 칸 이동
         * 이동하려는 위치에 다른 기사 -> 해당 기사 밀려남 (연쇄)
         * 기사가 이동하려는 방향 끝에 벽 -> 모든 기사 이동 x
         * 사라진 기사에게 명령 -> 반응 X
         */
        if(!knights[kId].isLive) return;
        moveKnights = new LinkedHashSet<>();
        damageKnights = new HashSet<>();
        if(move(kId, dir)) {
            for(int id : moveKnights) {
                Knight knight = knights[id];

                int nx1 = knight.x1 + dx[dir];
                int ny1 = knight.y1 + dy[dir];
                int nx2 = knight.x2 + dx[dir];
                int ny2 = knight.y2 + dy[dir];

                checkKnight(knight.x1, knight.y1, knight.x2, knight.y2, id, true);
                checkKnight(nx1, ny1, nx2, ny2, id, false);
                updateKnight(knight, nx1, ny1, nx2, ny2);
                damageKnights.add(id);
            }
            damageKnights.remove(kId);      // 움직임 시초는 데미지 X
            fight();
        }
    }
    static boolean move(int cur, int dir) {
        /**
         * 현재 기사를 움직인다. -> 이때 다른 기사와 충돌한다면 재귀로 다음 기사를 호출한다.
         * 리턴 값으로 true / false 를 받아 모든 기사가 이동 가능한지 불가능한지 판단한다.
         */
        Knight knight = knights[cur];
        // 새로운 이동 위치
        int nx1 = knight.x1 + dx[dir];
        int ny1 = knight.y1 + dy[dir];
        int nx2 = knight.x2 + dx[dir];
        int ny2 = knight.y2 + dy[dir];

        Set<Integer> collision = new HashSet<>();
        int search = isEmpty(nx1, ny1, nx2, ny2, cur, collision);
        if(search == EMPTY) {
            moveKnights.add(cur);
            return true;
        }
        else if(search == KNIGHT) {
            // 기사가 있는 경우, 밀어내기
            for(int next : collision) {
                if(!move(next, dir)) return false;
            }
            moveKnights.add(cur);
            return true;
        }
        else if(search == WALL) {
            return false;
        }
        return false;
    }
    static void updateKnight(Knight knight, int x1, int y1, int x2, int y2) {
        knight.x1 = x1;
        knight.x2 = x2;
        knight.y1 = y1;
        knight.y2 = y2;
    }
    static void checkKnight(int x1, int y1, int x2, int y2, int id, boolean remove) {
        for(int x=x1; x<x2; x++) {
            for(int y=y1; y<y2; y++) {
                knightBoard[x][y] = remove ? 0 : id;
            }
        }
    }
    static final int EMPTY = 0;
    static final int KNIGHT = 1;
    static final int WALL = 2;
    static int isEmpty(int nx1, int ny1, int nx2, int ny2, int id, Set<Integer> collision) {
        for(int x=nx1; x<nx2; x++) {
            for(int y=ny1; y<ny2; y++) {
                // 1. 벽이 아니여야 이동 가능
                if(x < 0 || y < 0 || x >= l || y >= l) return WALL;
                if(board[x][y] == 2) return WALL;
                // 2. 다른 기사가 있는 경우
                if(knightBoard[x][y] == id) continue;
                if(knightBoard[x][y] == 0) continue;
                // 다른 기사가 있거나, 벽이 있는 경우
                if(knightBoard[x][y] != id) collision.add(knightBoard[x][y]);
            }
        }
        return collision.isEmpty() ? EMPTY : KNIGHT;
    }
    static void fight() {
        /**
         * 밀려난 기사는 w x h  내에 놓인 함정 수 만큼 피해
         * 현재 체력 이상 데미지 -> 소멸
         * 명령을 받은 기사는 데미지 x -> 밀쳐진 기사만 데이지 o
         */
        for(int i : damageKnights) {
            Knight knight = knights[i];
            int trap = 0;
            for(int x=knight.x1; x<knight.x2; x++) {
                for(int y=knight.y1; y<knight.y2; y++) {
                    if(board[x][y] == 1) trap++;
                }
            }
            knight.addDamage(trap);
            // 기사가 죽었다면 지도에서 제거
            if(!knight.isLive) checkKnight(knight.x1, knight.y1, knight.x2, knight.y2, i, true);
        }
    }
}
