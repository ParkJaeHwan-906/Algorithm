package mar.week3.codetree;

import java.util.*;
import java.io.*;

public class 미생물연구_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static StringTokenizer st;
    static int n, q;
    static int[][] board;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());

        initBoard();
        for(int id=1; id<q+1; id++) {
            put(id);
            findGroup();
            moveNewBoard();
            sb.append(getScore()).append('\n');
        }
    }
    static void initBoard() { board = new int[n][n]; }
    static void put(int id) throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        int lx = Integer.parseInt(st.nextToken());
        int ly = Integer.parseInt(st.nextToken());
        int rx = Integer.parseInt(st.nextToken());
        int ry = Integer.parseInt(st.nextToken());

        for(int x=lx; x<rx; x++) {
            for(int y=ly; y<ry; y++) board[x][y] = id;
        }
    }
    static class Group implements Comparable<Group> {
        int id;
        int lx, ly;
        int rx, ry;
        List<int[]> list;

        Group(int id, int lx, int ly, int rx, int ry) {
            this.id = id;
            this.lx = lx;
            this.ly = ly;
            this.rx = rx;
            this.ry = ry;
            list = new ArrayList<>();
        }
        public int compareTo(Group o) {
            if(this.list.size() != o.list.size()) return Integer.compare(o.list.size(), this.list.size());
            return Integer.compare(this.id, o.id);
        }
    }
    static Map<Integer, Integer> groupCount;
    static PriorityQueue<Group> groups;
    static void findGroup() {
        groupCount = new HashMap<>();
        groups = new PriorityQueue<>();
        /**
         * board 의 최대 크기가 15 x 15
         * => full scan 가능
         */
        boolean[][] visited = new boolean[n][n];
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(board[x][y] == 0 || visited[x][y]) continue;
                scanGroup(x, y, visited);
            }
        }
    }
    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static void scanGroup(int x, int y, boolean[][] visited) {
        Queue<int[]> q = new ArrayDeque<>();

        int id = board[x][y];
        Group group = new Group(id, x, y, x, y);
        groupCount.put(id, groupCount.getOrDefault(id, 0)+1);
        group.list.add(new int[] {x, y});
        q.offer(new int[] {x, y});
        visited[x][y] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            for(int dir=0; dir<4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                if(visited[nx][ny]) continue;
                if(board[nx][ny] != id) continue;

                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny});
                group.list.add(new int[] {nx, ny});
                group.lx = Math.min(group.lx, nx);
                group.ly = Math.min(group.ly, ny);
                group.rx = Math.max(group.rx, nx);
                group.ry = Math.max(group.ry, ny);
            }
        }
        groups.offer(group);
    }
    static void moveNewBoard() {
        int[][] temp = new int[n][n];
        while(!groups.isEmpty()) {
            Group group = groups.poll();
            if(groupCount.get(group.id) > 1) continue;      // 둘로 쪼개진 그룹은 패스

            int[] newLocation = findNewLocation(temp, group);
            if(newLocation == null) continue;
            /**
             * 새로운 위치와, 좌하단 위치의 상대거리를 구해서 모두 이동
             */
            int mx = group.lx - newLocation[0];
            int my = group.ly - newLocation[1];

            for(int[] point : group.list) {
                int nx = point[0] - mx;
                int ny = point[1] - my;
                temp[nx][ny] = group.id;
            }
        }
        board = temp;
    }
    static int[] findNewLocation(int[][] temp, Group group) {
        int h = group.rx - group.lx + 1;
        int w = group.ry - group.ly + 1;

        for(int x=0; x + h <= n; x++) {
            for(int y=0; y + w <= n; y++) {
//                if(temp[x][y] != 0) continue;         => lx, ly, rx, ry 를 실제 그룹이 차지하는 칸으로 기준을 잡지 않았기 때문
                int mx = group.lx - x;
                int my = group.ly - y;
                boolean canPut = true;
                for(int[] point : group.list) {
                    int nx = point[0] - mx;
                    int ny = point[1] - my;
                    if(temp[nx][ny] != 0) {
                        canPut = false;
                        break;
                    }
                }

                if(canPut) return new int[] {x, y};
            }
        }
        return null;
    }
    static int getScore() {
        /**
         * 각 그룹의 모든 좌표레서 상하좌우 인접한 그룹이 있는지 확인
         */
        Map<Integer, Integer> groupSize = new HashMap<>();
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(board[x][y] == 0) continue;
                int id = board[x][y];
                groupSize.put(id, groupSize.getOrDefault(id, 0) + 1);
            }
        }
        int score = 0;
        boolean[][] adj = new boolean[q+1][q+1];
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(board[x][y] == 0) continue;
                int id = board[x][y];
                for(int dir=0; dir<4; dir++) {
                    int nx = x + dx[dir];
                    int ny = y + dy[dir];
                    if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                    if(board[nx][ny] == 0) continue;
                    if(board[nx][ny] == id) continue;
                    int oId = board[nx][ny];
                    if(adj[id][oId] || adj[oId][id]) continue;
                    adj[id][oId] = true;
                    adj[oId][id] = true;
                    score += (groupSize.get(id) * groupSize.get(oId));
                }
            }
        }
        return score;
    }
}
