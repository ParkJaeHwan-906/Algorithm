package jul.week4.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 01:09:56
 * AI 사용 여부 X
 */
public class 나무박멸_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n, m, k, c;
    static int[][] treeBoard;
    static int[][] killerBoard;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());

        treeBoard = new int[n][n];
        killerBoard = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            /**
             * -1 : 벽
             * 0 : 빈칸
             */
            for(int y = 0; y < n; y++) treeBoard[x][y] = Integer.parseInt(st.nextToken());
        }

        System.out.println(solution());
    }

    static final int[] dx = {0, 1, 0, -1, 1, 1, -1, -1};
    static final int[] dy = {1, 0, -1, 0, 1, -1, 1, -1};

    static int solution() {
        int totalKillTrees = 0;
        while(m-- > 0) {        // m 년동안 박멸 진행
            decreaseKiller();
            // 1. 나무 성장
            growTrees();
            // 2. 나무 번식
            propagationTrees();
            // 3. 제초제 뿌리기
            totalKillTrees += spreadKiller();
        }
        return totalKillTrees;
    }

    static void growTrees() {
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(treeBoard[x][y] == 0 || treeBoard[x][y] == -1) continue;

                // 나무가 있는 칸
                treeBoard[x][y] += getAdjTrees(x, y);
            }
        }
    }

    static int getAdjTrees(int x, int y) {
        int count = 0;
        for(int dir = 0; dir < 4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if(isNotBoard(nx, ny)) continue;
            if(treeBoard[nx][ny] > 0) count++;
        }
        return count;
    }

    static void propagationTrees() {
        int[][] tempTree = new int[n][n];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(treeBoard[x][y] == 0 || treeBoard[x][y] == -1) continue;

                // 나무가 있는 칸
                int count = getAdjEmpty(x, y);      // 주변 빈 칸(벽X, 다른 나무 X, 제초제X)
                if(count == 0) continue;
                int newTrees = treeBoard[x][y] / count;
                propagationTree(x, y, newTrees, tempTree);
            }
        }

        // 합치기
        applyTempTree(tempTree);
    }

    static int getAdjEmpty(int x, int y) {
        int count = 0;
        for(int dir = 0; dir < 4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if(isNotBoard(nx, ny)) continue;
            if(treeBoard[nx][ny] == 0 && killerBoard[nx][ny] == 0) count++;
        }
        return count;
    }

    static void propagationTree(int x, int y, int newTrees, int[][] tempTree) {
        for(int dir = 0; dir < 4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if(isNotBoard(nx, ny)) continue;
            if(treeBoard[nx][ny] == 0 && killerBoard[nx][ny] == 0) tempTree[nx][ny] += newTrees;
        }
    }

    static void applyTempTree(int[][] tempTree) {
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                treeBoard[x][y] += tempTree[x][y];
            }
        }
    }

    static int spreadKiller() {
        int bX = n + 7;
        int bY = n + 7;
        int bAcc = -1;

        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(treeBoard[x][y] == 0 || treeBoard[x][y] == -1) continue;       // 나무가 없는 칸에 뿌리면 전파되지 않음

                int acc = getMaxTrees(x, y);
                if(acc > bAcc ||
                        (acc == bAcc && bX > x ||
                                (acc == bAcc && bX == x && bY > y))) {
                    bAcc = acc;
                    bX = x;
                    bY = y;
                }
            }
        }
        // 실제 제초제 살포
        if(bAcc == -1) return 0;
        putKiller(bX, bY);
        return bAcc;
    }

    static int getMaxTrees(int x, int y) {
        int count = treeBoard[x][y];
        for(int dir = 4; dir < 8; dir++) {
            int nx = x;
            int ny = y;
            for (int dist = 1; dist <= k; dist++) {
                nx += dx[dir];
                ny += dy[dir];

                if (isNotBoard(nx, ny)) break;
                if (treeBoard[nx][ny] <= 0) break;

                count += treeBoard[nx][ny];
            }
        }
        return count;
    }

    static void putKiller(int x, int y) {
        killerBoard[x][y] = c + 1;
        treeBoard[x][y] = 0;
        for (int dir = 4; dir < 8; dir++) {
            int nx = x;
            int ny = y;
            for (int dist = 1; dist <= k; dist++) {
                nx += dx[dir];
                ny += dy[dir];

                if (isNotBoard(nx, ny)) break;
                if (treeBoard[nx][ny] == -1) break;

                killerBoard[nx][ny] = c + 1;
                if (treeBoard[nx][ny] == 0) break;
                treeBoard[nx][ny] = 0;
            }
        }
    }

    static boolean isNotBoard(int x, int y) { return x < 0 || y < 0 || x >= n || y >= n; }

    static void decreaseKiller() {
        for(int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if(killerBoard[x][y] == 0) continue;
                killerBoard[x][y]--;
            }
        }
    }
}
