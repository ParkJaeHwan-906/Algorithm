package jul.week2.codetree;

import java.util.*;
import java.io.*;

public class 색깔폭탄_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    /**
     * 1. 가장 큰 폭탄 묶음을 찾는다.
     *      - 빨간색이 가장 적게 포함된 것
     *      - 기준점 (빨강이 아니면서 행이 가장 큰 칸)
     *          - 가장 행이 큰 것
     *          - 가장 열이 작은 것
     * 2. 선택된 폭탄 모두 제거
     *      - 위에 있는 폭탄 떨어짐 (돌은 떨어지지 않음)
     * 3. 반시계 90도 회전
     * 4. 다시 중력
     *
     * 폭탄 묶음이 존재하지 않을 때까지 반복
     * 라운드마다 그룹크기**2 만큼의 점수 획득
     */
    static int n, m;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            /**
             * -1 : 돌
             * 0 : 빨강
             */
            for(int y = 0; y < n; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        System.out.println(solution());
    }

    static long solution() {
        long score = 0;
        while(true){
            List<Group> groups = findGroups();
            if(groups.isEmpty()) break;

            Group removed = groups.get(0);      // 사라질 그룹
            removeGroup(removed);               // 그룹 제거
            drop();                             // 떨어뜨림
            rotateCounterClockWise();           // 반시계 회전
            drop();                             // 떨어뜨림
            score += (removed.locs.size() * removed.locs.size());
        }
        return score;
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

    static void drop() {
        for(int y = 0; y < n; y++) {
            for(int x = n - 1; x >= 0; x--) {
                if(board[x][y] == -2) {     // 빈 칸이라면
                    int nx = x;
                    while(--nx >= 0) {
                        if(board[nx][y] >= 0) {      // 폭탄이 있다면
                            board[x][y] = board[nx][y];
                            board[nx][y] = -2;
                            break;
                        } else if(board[nx][y] == -1) break;
                    }
                }
            }
        }
    }

    static void removeGroup(Group group) {
        for(Loc loc : group.locs) {
            board[loc.x][loc.y] = -2;       // -2 를 빈 칸으로 취급
        }
    }

    static class Loc {
        int x, y;
        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static final int MAX_INF = Integer.MAX_VALUE;
    static final int MIN_INF = Integer.MIN_VALUE;

    static final Loc DUMMY = new Loc(MIN_INF, MAX_INF);

    static class Group {

        int id;         // 폭탄 색 id
        int redCount;   // 빨간 폭탄 개수
        int otherCount; // 이외 같은 색의 폭탄 개수
        List<Loc> locs; // 폭탄 좌표들
        Loc standard;   // 기준점 (빨간색은 될 수 없음)

        Group(int id) {
            this.id = id;
            this.redCount = 0;
            this.otherCount = 0;
            this.locs = new ArrayList<>();
            this.standard = DUMMY;
        }

        void add(Loc loc, int id) {
            if(id == 0) {
                locs.add(loc);
                redCount++;
            } else {
                locs.add(loc);
                otherCount++;
                if(standard.x < loc.x ||
                        (standard.x == loc.x && standard.y > loc.y)) standard = loc;
            }
        }
    }

    static List<Group> findGroups() {
        List<Group> groups = new ArrayList<>();
        boolean[][] visited = new boolean[n][n];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(board[x][y] == 0 || board[x][y] == -1 || board[x][y] == -2) continue;
                if(visited[x][y]) continue;

                Group g = findGroup(x, y, visited);
                if(g.locs.size() < 2) continue;
                groups.add(g);
            }
        }

        Collections.sort(groups, (a, b) -> {
            if(a.locs.size() != b.locs.size()) return Integer.compare(b.locs.size(), a.locs.size());
            if (a.redCount != b.redCount) return Integer.compare(a.redCount, b.redCount);
            if(a.standard.x != b.standard.x) return Integer.compare(b.standard.x, a.standard.x);
            return Integer.compare(a.standard.y, b.standard.y);
        });
        return groups;
    }

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static Group findGroup(int x, int y, boolean[][] visited) {
        int id = board[x][y];
        Group g = new Group(id);

        Queue<Loc> q = new ArrayDeque<>();
        boolean[][] tempVisited = new boolean[n][n];

        q.offer(new Loc(x, y));
        visited[x][y] = true;
        tempVisited[x][y] = true;

        while(!q.isEmpty()) {
            Loc cur = q.poll();
            g.add(cur, board[cur.x][cur.y]);

            for(int dir = 0; dir < 4; dir++) {
                int nx = cur.x + dx[dir];
                int ny = cur.y + dy[dir];
                if(isNotBaord(nx, ny)) continue;
                if(visited[nx][ny] || tempVisited[nx][ny]) continue;

                if(board[nx][ny] == g.id) {
                    visited[nx][ny] = true;
                    tempVisited[nx][ny] = true;
                    q.offer(new Loc(nx, ny));
                } else if(board[nx][ny] == 0) {
                    tempVisited[nx][ny] = true;
                    q.offer(new Loc(nx, ny));
                }
            }
        }
        return g;
    }

    static boolean isNotBaord(int x, int y) { return x < 0 || y < 0 || x >= n || y >= n; }
}
