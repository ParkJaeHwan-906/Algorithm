package jul.week2.codetree;

import java.util.*;
import java.io.*;

public class 포탑부수기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static class Loc {
        int x, y;

        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Tower extends Loc {
        int power;
        int lastAttacked;   // 마지막으로 공격에 가담한 시점(턴)
        boolean active;     // 이번 턴에 공격에 가담했는지 여부

        Tower(int x, int y, int power, int lastAttacked) {
            super(x, y);
            this.power = power;
            this.lastAttacked = lastAttacked;
            this.active = false;
        }
    }

    static int n, m, k;
    static Tower[][] board;       // board = [n][m];
    static PriorityQueue<Tower> strong;
    static PriorityQueue<Tower> weak;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        board = new Tower[n][m];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < m; y++) {
                int power = Integer.parseInt(st.nextToken());
                Tower tower = new Tower(x, y, power, 0);
                board[x][y] = tower;
            }
        }

        System.out.println(solution());
    }

    static int solution() {
        for(int time = 1; time <= k; time++) {
            setTurn();
            if(strong.size() < 2) break;                // 부서지지 않은 포탑이 1개 이하면 종료

            Tower attacker = pickAttacker(time);        // 공격자 선정
            Tower target = pickTarget(attacker);        // 공격대상 선정

            attack(attacker, target, time);             // 공격

            reset();                                    // 정비
        }
        return getMaxPower();
    }

    static int getMaxPower() {
        int max = 0;
        for(int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                max = Math.max(max, board[x][y].power);
            }
        }
        return max;
    }

    static void reset() {
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                if(board[x][y].power == 0) continue;
                if(board[x][y].active) {
                    board[x][y].active = false;
                    continue;
                }
                board[x][y].power += 1;
            }
        }
    }

    static void setTurn() {
        strong = new PriorityQueue<>((a, b) -> {
            if(a.power != b.power) return Integer.compare(b.power, a.power);                                // 공격력 높은 순
            if(a.lastAttacked != b.lastAttacked) return Integer.compare(a.lastAttacked, b.lastAttacked);    // 오래전에 공격한 순
            if((a.x + a.y) != (b.x + b.y)) return Integer.compare((a.x + a.y), (b.x + b.y));                // 행열 합 작은 순
            return Integer.compare(a.y, b.y);                                                               // 열 작은 순
        });
        weak = new PriorityQueue<>((a, b) -> {
            if(a.power != b.power) return Integer.compare(a.power, b.power);                                // 공격력 낮은 순
            if(a.lastAttacked != b.lastAttacked) return Integer.compare(b.lastAttacked, a.lastAttacked);    // 최근에 공격한 순
            if((a.x + a.y) != (b.x + b.y)) return Integer.compare((b.x + b.y), (a.x + a.y));                // 행열 합 큰 순
            return Integer.compare(b.y, a.y);                                                               // 열 큰 순
        });

        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                if(board[x][y].power <= 0) continue;        // 부서진 포탑은 선정 대상에서 제외
                strong.offer(board[x][y]);
                weak.offer(board[x][y]);
            }
        }
    }

    static void attack(Tower attacker, Tower target, int time) {
        if(laser(attacker, target, time)) return;   // laser 공격 성공
        attackByCanon(attacker, target, time);      // 포탄 공격
    }

    static void attackByCanon(Tower attacker, Tower target, int time) {
        for(int dir = 0; dir < 8; dir++) {
            int nx = (target.x + dx[dir] + n) % n;
            int ny = (target.y + dy[dir] + m) % m;

            if(nx == attacker.x && ny == attacker.y) continue;      // 공격자는 피해를 받지 않음

            damage(board[nx][ny], attacker.power / 2, time);
        }

        damage(target, attacker.power, time);
    }

    // 우 하 좌 상 / 대각선
    static final int[] dx = {0, 1, 0, -1, 1, 1, -1, -1};
    static final int[] dy = {1, 0, -1, 0, 1, -1, 1, -1};

    static class Node extends Loc {
        Node prev;
        Node(int x, int y, Node prev) {
            super(x, y);
            this.prev = prev;
        }
    }

    static boolean laser(Tower attacker, Tower target, int time) {
        boolean[][] visited = new boolean[n][m];
        Queue<Node> q = new ArrayDeque<>();

        q.offer(new Node(attacker.x, attacker.y, null));
        visited[attacker.x][attacker.y] = true;

        while(!q.isEmpty()) {
            Node cur = q.poll();
            if(cur.x == target.x && cur.y == target.y) {    // 목적지에 도착 가능
                attackByLaser(cur, attacker, target, time);
                return true;
            }

            for(int dir = 0; dir < 4; dir++) {              // 우 하 좌 상 우선순위
                int nx = (cur.x + dx[dir] + n) % n;         // 반대편으로 감싸기
                int ny = (cur.y + dy[dir] + m) % m;

                if(visited[nx][ny]) continue;               // 이미 방문
                if(board[nx][ny].power <= 0) continue;      // 부서진 포탑은 이동 불가

                visited[nx][ny] = true;
                q.offer(new Node(nx, ny, cur));
            }
        }

        return false;
    }

    static void attackByLaser(Node targetNode, Tower attacker, Tower target, int time) {
        Node cur = targetNode.prev;
        while(cur.prev != null) {
            damage(board[cur.x][cur.y], attacker.power / 2, time);
            cur = cur.prev;
        }

        damage(target, attacker.power, time);
    }

    static void damage(Tower tower, int amount, int time) {
        tower.power = Math.max(0, tower.power - amount);
        tower.active = true;
    }

    static Tower pickTarget(Tower attacker) {
        return pickStrongTower(attacker);
    }

    static Tower pickStrongTower(Tower attacker) {
        removePrevTower(strong, attacker);      // 공격자는 대상이 될 수 없음
        return strong.poll();
    }

    static Tower pickAttacker(int time) {
        Tower weakTower = pickWeakTower();      // 약한 포탑 뽑기
        weakTower.power += (n + m);
        weakTower.active = true;
        weakTower.lastAttacked = time;          // 공격자도 이번 턴에 가담
        return weakTower;
    }

    static Tower pickWeakTower() {
        return weak.poll();
    }

    static void removePrevTower(PriorityQueue<Tower> pq, Tower pickedTower) {
        while(!pq.isEmpty() && pq.peek() == pickedTower) pq.poll();
    }
}
