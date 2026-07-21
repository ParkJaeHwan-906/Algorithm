package jul.week2.codetree;

import java.util.*;
import java.io.*;

public class 자율주행자동차_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    /**
     * 1. 현재 방향을 기준으로 왼쪽으로 방향으로 가본 적 없다면 좌회전 후 이동
     * 2. 왼쪽이 인도이거나, 이미 방문한 경우 다시 자회전해서 반복
     * 3. 4방향 모두 이동할 수 없다면 바라보는 방향을 유지한채로 후진한 뒤 다시 1번부터 시도
     * 4. 3번하려했는데 인도인 경우 작동을 멈춤
     */

    // 상 우 하 좌 (왼쪽으로 회전은 -1 씩)
    static final int[] dx = {-1, 0, 1, 0};
    static final int[] dy = {0, 1, 0, -1};

    static class Loc {
        int x, y;
        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean equals(Object o) {
            if(this == o) return true;
            if(!(o instanceof Loc)) return false;
            Loc oLoc = (Loc) o;
            return x == oLoc.x && y == oLoc.y;
        }

        public int hashCode() { return Objects.hash(this.x, this.y); }
    }

    static class Car extends Loc {
        int dir;
        Car(int x, int y, int dir) {
            super(x, y);
            this.dir = dir;
        }
        void turnLeft() {
            if(this.dir == 0) this.dir = 3;
            else if(this.dir == 1) this.dir = 0;
            else if(this.dir == 2) this.dir = 1;
            else if(this.dir == 3) this.dir = 2;
        }
        int oppositeDir() {
            return (this.dir + 2) % 4;
        }
    }

    static int n, m;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine().trim());
        int cx = Integer.parseInt(st.nextToken());
        int cy = Integer.parseInt(st.nextToken());
        int cd = Integer.parseInt(st.nextToken());
        Car car = new Car(cx, cy, cd);

        board = new int[n][m];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < m; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }
        System.out.println(solution(car));
    }

    static Set<Loc> visited;
    static int solution(Car car) {
        visited = new HashSet<>();
        while(true) {
            // 현 위치 방문 처리
            visited.add(new Loc(car.x, car.y));
            // 회전
            if(search4WayDir(car)) continue;
            // 후진
            if(moveBackward(car)) continue;
            break;
        }
        return visited.size();
    }

    static boolean search4WayDir(Car car) {
        /**
         * 현재 위치를 기준으로 4방향 모두 확인
         */
        for(int seq = 0; seq < 4; seq++) {
            car.turnLeft();                                 // 좌회전

            int nx = car.x + dx[car.dir];
            int ny = car.y + dy[car.dir];

            if(isNotBoard(nx, ny)) continue;                // 격자를 벗어나는 경우
            if(board[nx][ny] == 1) continue;                // 인도인 경우
            if(visited.contains(new Loc(nx, ny))) continue; // 이전에 방문한적이 있는 경우

            car.x = nx; car.y = ny;                         // 위치 업데이트
            return true;
        }
        return false;
    }

    static boolean moveBackward(Car car) {
        int dir = car.oppositeDir();
        int nx = car.x + dx[dir];
        int ny = car.y + dy[dir];
        if(isNotBoard(nx, ny)) return false;
        if(board[nx][ny] == 1) return false;
        car.x = nx; car.y = ny;
        return true;
    }

    static boolean isNotBoard(int x, int y) { return x < 0 || y < 0 || x >= n || y >= m; }
}
