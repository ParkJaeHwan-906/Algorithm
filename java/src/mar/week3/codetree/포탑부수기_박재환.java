package mar.week3.codetree;

import java.util.*;
import java.io.*;

public class 포탑부수기_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    /**
     * N x M
     * 모든 위치에 포탑이 있음
     * 공격력이 0 이하가 된다면 부셔짐
     */
    static StringTokenizer st;
    static int n, m, k;
    static int[][] board;
    static int[][] lastAttack;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        board = new int[n][m];
        lastAttack = new int[n][m];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<m; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        solution();
    }
    static class Canon {
        int x, y;
        int power;
        int lastAttack;

        Canon(int x, int y, int power, int lastAttack) {
            this.x = x;
            this.y = y;
            this.power = power;
            this.lastAttack = lastAttack;
        }
    }
    static void solution() {
        for(int time=1; time <= k; time++) {
            if (countAlive() <= 1) break;
            Canon attacker = pickAttacker();
            Canon target = pickTarget(attacker);
            attackTarget(attacker, target, time);
            updateRemainCanon();
        }
        System.out.println(findMaxPower());
    }
    static void updateRemainCanon() {
        for(int x=0; x<n; x++) {
            for(int y=0; y<m; y++) {
                if(board[x][y] == 0) continue;
                if(!involved[x][y]) {
                    board[x][y]++;
                }
            }
        }
    }
    static Canon pickAttacker() {
        Canon best = null;
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                if(board[x][y] == 0) continue;
                Canon cand = new Canon(x, y, board[x][y], lastAttack[x][y]);
                if(best == null || isBetterAttacker(cand, best)) {
                    best = cand;
                }
            }
        }
        return best;
    }
    static Canon pickTarget(Canon attacker) {
        Canon best = null;
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                if(board[x][y] == 0) continue;
                if(x == attacker.x && y == attacker.y) continue;
                Canon cand = new Canon(x, y, board[x][y], lastAttack[x][y]);
                if(best == null || isBetterTarget(cand, best)) {
                    best = cand;
                }
            }
        }
        return best;
    }
    static boolean[][] involved;
    static void attackTarget(Canon attacker, Canon target, int time) {
        involved = new boolean[n][m];
        /**
         * 공격은 두 가지 방법으로 진행된다.
         * 1. 레이저
         *      최단 거리를 찾는다.
         *      최단 경로 내에 존재한 포탑도 공격 당한다.
         * 2. 포탄
         *      공격 대상 + 8방향 피해를 입는다.
         */
        attacker.power += (n + m);      // 핸디캡
        board[attacker.x][attacker.y] = attacker.power;
        lastAttack[attacker.x][attacker.y] = time;
        involved[attacker.x][attacker.y] = true;
        if(!lazerAttack(attacker, target)) {
            attackByCanon(attacker, target);
        }
    }
    static class Node {
        int x, y;
        Node prev;

        Node(int x, int y, Node prev) {
            this.x = x;
            this.y = y;
            this.prev = prev;
        }
    }
    static final int[] dx = {0,1,0,-1,1,1,-1,-1};
    static final int[] dy = {1,0,-1,0,1,-1,1,-1};
    static boolean lazerAttack(Canon attacker, Canon target) {
        Queue<Node> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][m];

        q.offer(new Node(attacker.x, attacker.y, null));
        visited[attacker.x][attacker.y] = true;

        while(!q.isEmpty()) {
            Node cur = q.poll();
            if(cur.x == target.x && cur.y == target.y) {
                recoverRoute(cur, attacker, target);
                return true;
            }

            for(int dir=0; dir<4; dir++) {
                int nx = cur.x + dx[dir];
                int ny = cur.y + dy[dir];
                if(nx < 0 || ny < 0 || nx >=n || ny >= m) {
                    /**
                     * 격자 밖으로 나가면 반대편 방향으로 나온다.
                     */
                    nx = (nx + n) % n;
                    ny = (ny + m) % m;
                }
                if(board[nx][ny] == 0) continue;
                if(visited[nx][ny]) continue;

                visited[nx][ny] = true;
                q.offer(new Node(nx, ny, cur));
            }
        }
        return false;
    }
    static void attackByCanon(Canon attacker, Canon target) {
        board[target.x][target.y] = Math.max(board[target.x][target.y] - attacker.power, 0);
        involved[target.x][target.y] = true;

        for(int dir = 0; dir < 8; dir++) {
            int nx = target.x + dx[dir];
            int ny = target.y + dy[dir];
            if(nx < 0 || ny < 0 || nx >=n || ny >= m) {
                /**
                 * 격자 밖으로 나가면 반대편 방향으로 나온다.
                 */
                nx = (nx + n) % n;
                ny = (ny + m) % m;
            }
            if(board[nx][ny] == 0) continue;
            if(nx == attacker.x && ny == attacker.y) continue;
            board[nx][ny] = Math.max(board[nx][ny] - (attacker.power / 2), 0);
            involved[nx][ny] = true;
        }
    }
    static void recoverRoute(Node cur, Canon attacker, Canon target) {
        board[target.x][target.y] = Math.max(board[target.x][target.y] - attacker.power, 0);
        involved[target.x][target.y] = true;

        cur = cur.prev; // target 바로 이전부터 시작
        while(cur != null && !(cur.x == attacker.x && cur.y == attacker.y)) {
            board[cur.x][cur.y] = Math.max(board[cur.x][cur.y] - (attacker.power / 2), 0);
            involved[cur.x][cur.y] = true;
            cur = cur.prev;
        }
    }
    // =================================================
    static boolean isBetterAttacker(Canon a, Canon b) {
        if(a.power != b.power) return a.power < b.power;
        if(a.lastAttack != b.lastAttack) return a.lastAttack > b.lastAttack;
        int aSum = a.x + a.y;
        int bSum = b.x + b.y;
        if(aSum != bSum) return aSum > bSum;
        return a.y > b.y;
    }
    static boolean isBetterTarget(Canon a, Canon b) {
        if(a.power != b.power) return a.power > b.power;
        if(a.lastAttack != b.lastAttack) return a.lastAttack < b.lastAttack;
        int aSum = a.x + a.y;
        int bSum = b.x + b.y;
        if(aSum != bSum) return aSum < bSum;
        return a.y < b.y;
    }
    static int countAlive() {
        int cnt = 0;
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                if (board[x][y] > 0) cnt++;
            }
        }
        return cnt;
    }
    static int findMaxPower() {
        int max = 0;
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                max = Math.max(max, board[x][y]);
            }
        }
        return max;
    }
}
