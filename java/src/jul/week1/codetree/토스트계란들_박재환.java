package jul.week1.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:41:26
 * AI 사용 여부 X
 */
public class 토스트계란들_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n, l, r;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        l = Integer.parseInt(st.nextToken());
        r = Integer.parseInt(st.nextToken());

        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        System.out.println(solution());
    }

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static int solution() {
        int time = 0;
        while(true) {
            boolean[][][] checked = new boolean[n][n][4];       // [x][y][4방향 선 제거]
            findRemoveGrid(checked);
            Queue<Group> groups = findGroups(checked);
            if(groups.size() == (n * n)) break;                 // 더 이상 움직이지 않아도 됨
            remakeBoard(groups);
            ++time;
        }
        return time;
    }

    static void remakeBoard(Queue<Group> groups) {
        int[][] temp = new int[n][n];
        while(!groups.isEmpty()) {
            Group group = groups.poll();
            int nValue = group.value / group.locs.size();
            for(int[] loc : group.locs) {
                temp[loc[0]][loc[1]] = nValue;
            }
        }
        board = temp;
    }

    static class Group {
        List<int[]> locs;
        int value;

        Group() {
            this.locs = new ArrayList<>();
            this.value = 0;
        }
    }

    static Queue<Group> findGroups(boolean[][][] checked) {
        Queue<Group> groups = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(visited[x][y]) continue;
                Group group = makeGroup(x, y, visited, checked);
                groups.offer(group);
            }
        }
        return groups;
    }

    static Group makeGroup(int x, int y, boolean[][] visited, boolean[][][] checked) {
        Queue<int[]> q = new ArrayDeque<>();
        Group group = new Group();

        q.offer(new int[] {x, y});
        visited[x][y] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            group.locs.add(cur);
            group.value += board[cur[0]][cur[1]];
            for(int dir = 0; dir < 4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];

                if(isNotBoard(nx, ny)) continue;
                if(visited[nx][ny]) continue;
                if(!checked[cur[0]][cur[1]][dir] || !checked[nx][ny][oppositeDir(dir)]) continue;

                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny});
            }
        }
        return group;
    }

    static void findRemoveGrid(boolean[][][] checked) {
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                for(int dir = 0; dir < 4; dir++) {
                    int nx = x + dx[dir];
                    int ny = y + dy[dir];

                    if(isNotBoard(nx, ny)) continue;                                    // 격자를 벗어남
                    if(checked[nx][ny][oppositeDir(dir)] || checked[x][y][dir]) continue;  // 이미 없어질 격자

                    int diff = Math.abs(board[x][y] - board[nx][ny]);
                    if(diff < l || diff > r) continue;                                  // 사이 값이 아닌 경우

                    checked[nx][ny][oppositeDir(dir)] = true;
                    checked[x][y][dir] = true;
                }
            }
        }
    }

    static boolean isNotBoard(int x, int y) { return x < 0 || y < 0 || x >= n || y >= n;}

    static int oppositeDir(int dir) { return (dir + 2) % 4; }
}
