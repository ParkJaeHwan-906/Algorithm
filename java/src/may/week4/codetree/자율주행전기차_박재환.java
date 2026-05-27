package may.week4.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이시간]
 * 00:47:21
 * AI 사용 여부 X
 */
public class 자율주행전기차_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    /**
     * n x n 크기 격자
     * - 차가 지나갈 수 없는 벽의 위치
     * - m 명의 손님
     *
     * => 주어진 배터리 용량으로 승객을 모두 태워줄 수 있는지 알아보고자 함
     *
     * ---
     *
     * 자율주행 전기차는 승객을 태우러 출발지에 이동할 때나 목적지로 이동할 때 항상 최단거리로 이동합니다.
     * 한 칸 이동 시, 1만큼의 배터리를 소요합니다.
     * 승객을 목적지로 태워주면 이동하며 소모한 배터리 양의 두 배만큼 충전한 뒤 이동합니다.
     *
     * 이동 중 배터리가 모두 소모되면 종료됩니다.
     */
    static class Car {
        int x, y;
        int charge;
        int customer;

        Car(int x, int y, int charge, int customer) {
            this.x = x;
            this.y = y;
            this.charge = charge;
            this.customer = customer;
        }
    }
    static class Customer {
        int sx, sy;
        int ex, ey;

        Customer(int sx, int sy, int ex, int ey) {
            this.sx = sx;
            this.sy = sy;
            this.ex = ex;
            this.ey = ey;
        }
    }
    static int n, m, c;
    static int[][] board;
    static Car car;
    static Customer[] customers;
    static int[][] customerBoard;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());

        /**
         * 0 : 도로
         * 1 : 벽
         */
        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine().trim());
        int x = Integer.parseInt(st.nextToken()) - 1;
        int y = Integer.parseInt(st.nextToken()) - 1;
        car = new Car(x, y, c, 0);

        customers = new Customer[m + 1];
        customerBoard = new int[n][n];
        for(int i = 1; i < m + 1; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int sx = Integer.parseInt(st.nextToken()) - 1;
            int sy = Integer.parseInt(st.nextToken()) - 1;
            int ex = Integer.parseInt(st.nextToken()) - 1;
            int ey = Integer.parseInt(st.nextToken()) - 1;
            Customer customer = new Customer(sx, sy, ex, ey);
            customers[i] = customer;
            customerBoard[sx][sy] = i;
        }

        solution();

        if(car == null) System.out.println(-1);
        else System.out.println(car.charge);
    }

    static void solution() {
        while(car.customer < m) {
            // 1. 태울 승객 찾기
            car = findCustomer();
            if(car == null) break;
            // 2. 승객 이동
            car = moveCustomer(customerBoard[car.x][car.y]);
            if(car == null) break;
        }
    }

    static int[] dx = {0, 1, 0, -1};
    static int[] dy = {1, 0, -1, 0};
    static Car findCustomer() {
        int sx = car.x, sy = car.y;       // 택시 초기 위치

        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];

        q.offer(new int[] {sx, sy, 0});       // (x, y, dist)
        visited[sx][sy] = true;

        int bestX = Integer.MAX_VALUE;
        int bestY = Integer.MAX_VALUE;
        int bestDist = Integer.MAX_VALUE;
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int dist = cur[2];

            if(customerBoard[x][y] > 0) {       // 손님이 있는 경우
                if(bestDist > dist ||
                        (bestDist == dist && (bestX > x ||
                                (bestX == x && bestY > y)))) {
                    bestDist = dist;
                    bestX = x;
                    bestY = y;
                }
                continue;
            }

            if(dist >= bestDist) continue;

            for(int dir = 0; dir < 4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];

                if(isNotBoard(nx, ny)) continue;
                if(visited[nx][ny]) continue;
                if(board[nx][ny] == 1) continue;

                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny, dist + 1});
            }
        }

        if(bestDist >= car.charge) return null;

        return new Car(bestX, bestY, car.charge - bestDist, car.customer);
    }

    static Car moveCustomer(int customerId) {
        Customer customer = customers[customerId];
        int sx = car.x, sy = car.y;
        int ex = customer.ex, ey = customer.ey;

        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];

        q.offer(new int[] {sx, sy, 0});       // (x, y, dist)
        visited[sx][sy] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int dist = cur[2];

            if(x == ex && y == ey) {        // 목적지에 도착한 경우
                if(dist > car.charge) break;

                int newCharge = car.charge + dist;
                customerBoard[car.x][car.y] = 0;
                return new Car(x, y, newCharge, car.customer + 1);
            }


            for(int dir = 0; dir < 4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];

                if(isNotBoard(nx, ny)) continue;
                if(visited[nx][ny]) continue;
                if(board[nx][ny] == 1) continue;

                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny, dist + 1});
            }
        }

        return null;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}
