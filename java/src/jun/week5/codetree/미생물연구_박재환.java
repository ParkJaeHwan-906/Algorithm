package jun.week5.codetree;

import java.util.*;
import java.io.*;

public class 미생물연구_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static class Command {
        int r1, c1;
        int r2, c2;

        Command(int r1, int c1, int r2, int c2) {
            this.r1 = r1;
            this.c1 = c1;
            this.r2 = r2;
            this.c2 = c2;
        }
    }

    static int n;
    static Queue<Command> cmds;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());
        cmds = new ArrayDeque<>();
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int r1 = Integer.parseInt(st.nextToken());
            int c1 = Integer.parseInt(st.nextToken());
            int r2 = Integer.parseInt(st.nextToken());
            int c2 = Integer.parseInt(st.nextToken());
            Command cmd = new Command(r1, c1, r2, c2);
            cmds.offer(cmd);
        }

        int[][] board = new int[n][n];
        System.out.println(solution(board));
    }

    static class Group {
        int id;
        List<int[]> locs;

        boolean valid;

        Group(int id) {
            this.id = id;
            locs = new ArrayList<>();

            this.valid = true;
        }
    }

    static String solution(int[][] board) {
        int id = 0;
        StringBuilder sb = new StringBuilder();
        Map<Integer, Group> removedGroup = new HashMap<>();
        while(!cmds.isEmpty()) {
            removedGroup.clear();
            Command cmd = cmds.poll();

            put(++id, cmd.r1, cmd.c1, cmd.r2, cmd.c2, board);

            findGroup(removedGroup, board);

            board = newBoard(new ArrayList<>(removedGroup.values()), removedGroup);

            long result = getScore(id, removedGroup, board);
            sb.append(result).append("\n");
        }

        return sb.toString();
    }

    static void put(int id, int r1, int c1, int r2, int c2, int[][] board) {
        for(int x = r1; x < r2; x++) {
            for(int y = c1; y < c2; y++) board[x][y] = id;
        }
    }

    static void findGroup(Map<Integer, Group> map, int[][] board) {
        boolean[][] visited = new boolean[n][n];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(visited[x][y] || board[x][y] == 0) continue;
                if(map.containsKey(board[x][y])) map.get(board[x][y]).valid = false;        // 두 개 이상의 그룹으로 나뉜다면
                else {
                    Group group = new Group(board[x][y]);
                    group.locs = grouping(x, y, board, visited);
                    map.put(group.id, group);
                }
            }
        }
    }

    static int[] dx = {0, 1, 0, -1};
    static int[] dy = {1, 0, -1, 0};

    static List<int[]> grouping(int x, int y, int[][] board, boolean[][] visited) {
        int id = board[x][y];
        List<int[]> list = new ArrayList<>();

        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[] {x, y});
        visited[x][y] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            list.add(cur);

            for(int dir = 0; dir < 4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(isNotBoard(nx, ny)) continue;
                if(visited[nx][ny]) continue;
                if(board[nx][ny] != id) continue;

                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny});
            }
        }

        return list;
    }

    static int[][] newBoard(List<Group> list, Map<Integer, Group> map) {
        int[][] temp = new int[n][n];
        Collections.sort(list, (a, b) -> {
            if(a.locs.size() != b.locs.size()) return Integer.compare(b.locs.size(), a.locs.size());
            return Integer.compare(a.id, b.id);
        });

        for(Group group : list) {
            if(!group.valid) {
                map.remove(group.id);
                continue;
            }

            int minX = n, minY = n;
            for(int[] loc : group.locs) {
                minX = Math.min(minX, loc[0]);
                minY = Math.min(minY, loc[1]);
            }

            List<int[]> newLocs = new ArrayList<>();
            boolean put = false;
            for(int x = 0; x < n; x++) {
                for(int y = 0; y < n; y++) {

                    boolean canPut = true;
                    for(int[] loc : group.locs) {
                        int nx = loc[0] - minX + x;
                        int ny = loc[1] - minY + y;
                        if(isNotBoard(nx, ny) || temp[nx][ny] != 0) {
                            canPut = false;
                            break;
                        }
                    }

                    if(canPut) {
                        put = true;
                        for(int[] loc : group.locs) {
                            int nx = loc[0] - minX + x;
                            int ny = loc[1] - minY + y;
                            newLocs.add(new int[] {nx, ny});
                            temp[nx][ny] = group.id;
                        }
                        break;
                    }
                    if(put) break;
                }
                if(put) break;
            }


            group.locs = newLocs;
        }

        return temp;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }

    static long getScore(int id, Map<Integer, Group> map, int[][] board) {
        long score = 0;
        boolean[][] checked = new boolean[id + 1][id + 1];
        for(Group group : map.values()) {
            for(int[] loc : group.locs) {
                for(int dir = 0; dir < 4; dir++) {
                    int nx = loc[0] + dx[dir];
                    int ny = loc[1] + dy[dir];
                    if(isNotBoard(nx, ny)) continue;
                    if(board[nx][ny] == group.id || board[nx][ny] == 0) continue;
                    if(checked[group.id][board[nx][ny]] || checked[board[nx][ny]][group.id]) continue;

                    checked[group.id][board[nx][ny]] = checked[board[nx][ny]][group.id] = true;
                    score += (group.locs.size() * map.get(board[nx][ny]).locs.size());
                }
            }
        }
        return score;
    }
}
