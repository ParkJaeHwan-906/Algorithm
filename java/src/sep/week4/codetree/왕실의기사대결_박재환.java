package sep.week4.codetree;

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

    static class Knight {
        int id;
        int x, y;
        int h, w;
        int k;
        int damage;
        List<int[]> locs;
        boolean live;
        Knight(int id, int x, int y, int h, int w, int k, int damage) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.h = h;
            this.w = w;
            this.k = k;
            this.live = true;
            this.damage = damage;
            initLocs(x, y, h, w);
        }
        void initLocs(int x, int y, int h, int w) {
            locs = new ArrayList<>();
            for(int i = x; i < x + h; i++) {
                for(int j = y; j < y + w; j++) {
                    locs.add(new int[] {i, j});
                }
            }
        }
    }

    static int l, n, q;
    static int[][] board;
    static Knight[] knights;
    static int[][] knightBoard;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        l = Integer.parseInt(st.nextToken());       // 격자 크기
        n = Integer.parseInt(st.nextToken());       // 기사 수
        q = Integer.parseInt(st.nextToken());       // 명령 횟수

        board = new int[l][l];
        for(int x = 0; x < l; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < l; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
            }
        }

        knights = new Knight[n + 1];
        knightBoard = new int[l][l];
        for(int i = 1; i <= n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());
            knights[i] = new Knight(i, x, y, h, w, k, 0);
            for(int[] loc : knights[i].locs) {
                knightBoard[loc[0]][loc[1]] = i;
            }
        }

        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int kId = Integer.parseInt(st.nextToken());
            int dir = Integer.parseInt(st.nextToken());
            Set<Integer> moveKnights = new HashSet<>();
            if(knights[kId].live && moveKnight(knights[kId], dir, moveKnights)) {     // 기사 이동에 성공한 경우
                // 실제 이동
                for(int moveKnight : moveKnights) {
                    Knight knight = knights[moveKnight];
                    for(int[] loc : knight.locs) {
                        knightBoard[loc[0]][loc[1]] = 0;
                    }
                }
                for(int moveKnight : moveKnights) {
                    Knight knight = knights[moveKnight];
                    int nx = knight.x + dx[dir];
                    int ny = knight.y + dy[dir];
                    knight = new Knight(knight.id, nx, ny, knight.h, knight.w, knight.k, knight.damage);
                    for(int[] loc : knight.locs) {
                        knightBoard[loc[0]][loc[1]] = knight.id;
                    }
                    knights[moveKnight] = knight;
                }
                // 대결 대미지 계산
                for(int moveKnight : moveKnights) {
                    if(moveKnight == kId) {         // 명령을 받은 기사는 집계 대상에서 제외됨
                        continue;
                    }
                    Knight knight = knights[moveKnight];
                    int trapCount = trapCount(knight);
                    knight.damage += trapCount;
                    if(knight.k <= knight.damage) {             // 탈락
                        for(int[] loc : knight.locs) {
                            knightBoard[loc[0]][loc[1]] = 0;
                        }
                        knight.live = false;
                    }
                }
            }
        }
        System.out.print(getResult());
    }

    static int getResult() {
        int score = 0;
        for(int i = 1; i <= n; i++) {
            if(!knights[i].live) {
                continue;
            }
            score += knights[i].damage;
        }
        return score;
    }

    static int trapCount(Knight knight) {
        int count = 0;
        for(int x = knight.x; x < knight.x + knight.h; x++) {
            for(int y = knight.y; y < knight.y + knight.w; y++) {
                if(board[x][y] == TRAP) {
                    count++;
                }
            }
        }
        return count;
    }

    static boolean moveKnight(Knight knight, int dir, Set<Integer> moveKnights) {
        List<int[]> newLocs = isAvailable(knight, dir);
        if(newLocs == null) {
            return false;
        }
        for(int[] loc : newLocs) {
            if(knightBoard[loc[0]][loc[1]] != 0 && knightBoard[loc[0]][loc[1]] != knight.id && !moveKnights.contains(knightBoard[loc[0]][loc[1]])) {
                if(!moveKnight(knights[knightBoard[loc[0]][loc[1]]], dir, moveKnights)) {
                    return false;
                }
            }
        }
        moveKnights.add(knight.id);
        return true;
    }

    static List<int[]> isAvailable(Knight knight, int dir) {
        List<int[]> newLocs = new ArrayList<>();
        for(int[] loc : knight.locs) {
            int nx = loc[0] + dx[dir];
            int ny = loc[1] + dy[dir];
            if(isNotBoard(nx, ny) || board[nx][ny] == WALL) {
                return null;
            }
            newLocs.add(new int[] {nx, ny});
        }
        return newLocs;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= l || y >= l;
    }
}
