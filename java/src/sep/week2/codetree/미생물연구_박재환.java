package sep.week2.codetree;

import java.util.*;
import java.io.*;

public class 미생물연구_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static int id;
    static int n, q;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());

        id = 0;
        board = new int[n][n];
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int x1 = Integer.parseInt(st.nextToken());
            int y1 = Integer.parseInt(st.nextToken());
            int x2 = Integer.parseInt(st.nextToken());
            int y2 = Integer.parseInt(st.nextToken());
            long result = solution(x1, y1, x2, y2);
            sb.append(result).append("\n");
        }
        System.out.print(sb);
    }

    static long solution(int x1, int y1, int x2, int y2) {
        ++id;
        Map<Integer, Group> groups = input(x1, y1, x2, y2);
        board = migration(groups);
        return query(groups);
    }

    // =======================================================
    // 미생물 투입
    // =======================================================
    static Map<Integer, Group> input(int x1, int y1, int x2, int y2) {
        for(int x = x1; x < x2; x++) {
            for(int y = y1; y < y2; y++) {
                board[x][y] = id;
            }
        }
        // 둘 이상의 무리로 나누어진 그룹이 있다면 삭제처리
        return removed();
    }
    static class Group {
        int id;
        int x, y;       // 기준 좌표
        List<int[]> locs;
        Group(int id) {
            this.id = id;
            this.x = Integer.MAX_VALUE;
            this.y = Integer.MAX_VALUE;
            locs = new ArrayList<>();
        }
        void add(int x, int y) {
            this.x = Math.min(this.x, x);
            this.y = Math.min(this.y, y);
            this.locs.add(new int[] {x, y});
        }
    }
    static Map<Integer, Group> removed() {
        Map<Integer, Group> groups = new HashMap<>();
        Set<Integer> removeTargets = new HashSet<>();
        boolean[][] visited = new boolean[n][n];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(board[x][y] == 0 || visited[x][y]) {     // 빈 칸이거나, 이미 확인한 부분인 경우
                    continue;
                }
                if(groups.containsKey(board[x][y])) {       // 둘 이상으로 나뉜 그룹의 경우
                    removeTargets.add(board[x][y]);
                    continue;
                }
                Group group = makeGroup(x, y, visited);
                groups.put(board[x][y], group);
            }
        }

        for(int removeTarget : removeTargets) {
            groups.remove(removeTarget);
        }
        return groups;
    }
    static Group makeGroup(int x, int y, boolean[][] visited) {
        int id = board[x][y];
        Group group = new Group(id);
        Queue<int[]> q = new ArrayDeque<>();

        q.offer(new int[] {x, y});
        visited[x][y] = true;

        while(!q.isEmpty()) {
            int[] loc = q.poll();
            group.add(loc[0], loc[1]);

            for(int dir = 0; dir < 4; dir++) {
                int nx = loc[0] + dx[dir];
                int ny = loc[1] + dy[dir];
                if(isNotBoard(nx, ny) || visited[nx][ny]) {
                    continue;
                }
                if(board[nx][ny] != id) {
                    continue;
                }
                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny});
            }
        }
        return group;
    }
    // =======================================================
    // 배양 용기 이동
    // =======================================================
    static int[][] migration(Map<Integer, Group> groups) {
        int[][] newBoard = new int[n][n];
        List<Group> groupList = new ArrayList<>(groups.values());
        groupList.sort((a, b) -> {
            if(a.locs.size() != b.locs.size()) {
                return Integer.compare(b.locs.size(), a.locs.size());
            }
            return Integer.compare(a.id, b.id);
        });

        for(Group group : groupList) {
            migrationGroup(newBoard, group);
        }
        return newBoard;
    }
    static void migrationGroup(int[][] newBoard, Group group) {
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(canMigration(newBoard, x, y, group)) {
                    return;
                }
            }
        }
    }
    static boolean canMigration(int[][] newBoard, int x, int y, Group group) {
        int diffX = group.x - x;
        int diffY = group.y - y;

        for(int[] loc : group.locs) {
            int nx = loc[0] - diffX;
            int ny = loc[1] - diffY;
            if (isNotBoard(nx, ny) || newBoard[nx][ny] != 0) {
                return false;
            }
        }

        for(int[] loc : group.locs) {
            int nx = loc[0] - diffX;
            int ny = loc[1] - diffY;
            newBoard[nx][ny] = group.id;
        }
        return true;
    }
    // =======================================================
    // 결과 기록
    // =======================================================
    static long query(Map<Integer, Group> groups) {
        boolean[][] adj = new boolean[id + 1][id + 1];
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (board[x][y] == 0) {
                    continue;
                }
                for (int dir = 0; dir < 4; dir++) {
                    int nx = x + dx[dir];
                    int ny = y + dy[dir];

                    if (isNotBoard(nx, ny)) {
                        continue;
                    }
                    int a = board[x][y];
                    int b = board[nx][ny];
                    if (b != 0 && a != b) {
                        adj[Math.min(a, b)][Math.max(a, b)] = true;
                    }
                }
            }
        }

        long result = 0;
        for (int a = 1; a <= id; a++) {
            for (int b = a + 1; b <= id; b++) {
                if (adj[a][b]) {
                    result += (long) groups.get(a).locs.size()
                            * groups.get(b).locs.size();
                }
            }
        }
        return result;
    }
    // =======================================================
    // 공통
    // =======================================================
    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}
