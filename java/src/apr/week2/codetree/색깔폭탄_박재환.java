package apr.week2.codetree;

import java.util.*;
import java.io.*;

public class 색깔폭탄_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m;
    static int[][] board;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) {
                /**
                 * -1 : 검정 돌(중력X)
                 * 0 : 빨강
                 * 이외 나머지
                 */
                board[x][y] = Integer.parseInt(st.nextToken());
            }
        }
        System.out.println(solution());
    }
    static int score;
    static int solution() {
        score = 0;
        while(true) {
            findGroups();
            if(!explore()) break;
            reArrange();
        }
        return score;
    }
    static class Group implements Comparable<Group> {
        int color;
        List<int[]> points;
        int redCount;
        int bestX, bestY;   // x 는 큰 것, y 는 작은 것

        Group(int color, int bestX, int bestY) {
            this.color = color;
            this.bestX = bestX;
            this.bestY = bestY;

            this.points = new ArrayList<>();
            this.redCount = 0;
        }

        public int compareTo(Group o) {
            if(this.points.size() != o.points.size())
                return Integer.compare(o.points.size(), this.points.size());

            if(this.redCount != o.redCount)
                return Integer.compare(this.redCount, o.redCount);

            if(this.bestX != o.bestX)
                return Integer.compare(o.bestX, this.bestX);

            return Integer.compare(this.bestY, o.bestY);
        }
    }
    static PriorityQueue<Group> groups;
    static void findGroups() {
        groups = new PriorityQueue<>();
        boolean[][] visited = new boolean[n][n];

        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(visited[x][y] || board[x][y] < 1) continue;

                Group group = findGroup(x, y, visited);
                groups.offer(group);
            }
        }
    }
    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};
    static Group findGroup(int x, int y, boolean[][] visited) {
        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] temp = new boolean[n][n];

        q.offer(new int[] {x, y});
        visited[x][y] = true;
        temp[x][y] = true;

        int color = board[x][y];
        Group group = new Group(color, x, y);
        group.points.add(new int[] {x, y});
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            for(int dir = 0; dir < 4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(isNotBoard(nx, ny)) continue;
                if(visited[nx][ny] || temp[nx][ny]) continue;
                if(board[nx][ny] != color && board[nx][ny] == 0) {        // 색이 다르다면 - 빨강만 가능
                    temp[nx][ny] = true;
                    q.offer(new int[] {nx, ny});
                    group.redCount++;
                    group.points.add(new int[] {nx, ny});
                    continue;
                }
                // 같은 색이라면
                if(board[nx][ny] == color) {
                    temp[nx][ny] = true;
                    visited[nx][ny] = true;
                    q.offer(new int[]{nx, ny});
                    group.points.add(new int[]{nx, ny});

                    if (group.bestX < nx || (group.bestX == nx && group.bestY > ny)) {
                        group.bestX = nx;
                        group.bestY = ny;
                    }
                }
            }
        }
        return group;
    }

    static boolean explore() {
        // 터뜨릴 수 있는 그룹만 남기기
        while(!groups.isEmpty()) {
            Group group = groups.peek();
            if(group.points.size() < 2) {
                groups.poll();
                continue;
            }
            break;
        }
        if(groups.isEmpty()) return false;

        Group group = groups.poll();        // 터뜨릴 대상
        /**
         * 빈 칸 처리
         * 0 : 빨강
         * -1 : 검정 돌
         * 
         * -> -2 로 표시
         */
        for(int[] point : group.points) {
            int x = point[0];
            int y = point[1];
            board[x][y] = -2;
        }

        score += (group.points.size() * group.points.size());

        return true;
    }

    static void reArrange() {
        drop();

        rotateCounterClockWise();

        drop();
    }
    static void drop() {
        for(int y = 0; y < n; y++) {
            for(int x = n - 1; x >= 0; x--) {
                if(board[x][y] == -2) {     // 현재 빈 칸
                    int j = x;
                    while(--j >= 0 && board[j][y] == -2);     // 떨어뜨릴 수 있는 칸 위치까지
                    if(j < 0) break;
                    if(board[j][y] == -1) {     // 돌이라면 떨어뜨릴 수 없음
                        // 점프
                        x = j;
                    } else {        // 떨어뜨릴 수 있음
                        board[x][y] = board[j][y];
                        board[j][y] = -2;
                    }
                }
            }
        }
    }
    static void rotateCounterClockWise() {
        int[][] temp = new int[n][n];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                temp[n - 1 - y][x] = board[x][y];
            }
        }
        board = temp;
    }
    // ===
    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}
