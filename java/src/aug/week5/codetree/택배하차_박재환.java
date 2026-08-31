package aug.week5.codetree;

import java.util.*;
import java.io.*;

public class 택배하차_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static class Box {
        int no;         // 택배 번호
        int h, w;       // 세로 크기, 가로 크기
        int col;        // 좌측 좌표
        Box(int no, int h, int w, int col) {
            this.no = no;
            this.h = h;
            this.w = w;
            this.col = col;
        }
    }

    static final int INF = Integer.MAX_VALUE;
    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static int n, m;
    static List<Box> boxes;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        boxes = new ArrayList<>();
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int no = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int col = Integer.parseInt(st.nextToken()) - 1;
            boxes.add(new Box(no, h, w, col));
        }
        System.out.println(solution());
    }

    static Set<Integer> outBoxes;
    static String solution() {
        outBoxes = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        while(outBoxes.size() < m) {            // 모든 상자를 하차할 때까지 반복
            // 박스 쌓기
            stackBox();
            // 좌측 빼기
            int lId = popLeft();
            if(lId == INF) continue;        // 뽑을 수 있는 박스가 없는 경우 -> 종료 조건으로 이동
            sb.append(lId).append("\n");
            outBoxes.add(lId);
            // 박스 쌓기
            stackBox();
            // 우측 빼기
            int rId = popRight();
            if(rId == INF) continue;        // 뽑을 수 있는 박스가 없는 경우 -> 종료 조건으로 이동
            sb.append(rId).append("\n");
            outBoxes.add(rId);
        }
        return sb.toString();
    }

    // ===========================
    // 박스 쌓기
    // ===========================
    static void stackBox() {
        board = new int[n][n];      // 매 턴 새롭게 쌓는다.
        for(Box box : boxes) {
            if(outBoxes.contains(box.no)) {
                continue;
            }
            int maxDepth = getMaxDepth(box);
            putBox(maxDepth, box);
        }
    }

    static int getMaxDepth(Box box) {
        // 제일 깊게 내려갈 수 있는 위치를 찾는다.
        for(int x = 0; x < n; x++) {
            boolean isPossible = true;
            for(int y = box.col; y < (box.col + box.w); y++) {
                if(board[x][y] == 0) continue;      // 쌓을 수 있는 공간
                isPossible = false;
                break;
            }
            if(!isPossible) return x - 1;
        }
        return n - 1;       // 끝가지 내려올 수 있는 경우
    }

    static void putBox(int depth, Box box) {
        for(int x = depth; x > depth - box.h; x--) {
            for(int y = box.col; y < (box.col + box.w); y++) {
                board[x][y] = box.no;
            }
        }
    }

    // ===========================
    // 좌측 하차
    // ===========================
    static int popLeft() {
        int minId = INF;
        boolean[][] visited = new boolean[n][n];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(board[x][y] == 0 || visited[x][y]) {
                    continue;
                }
                // 현재 칸에 상자가 있는 경우
                int no = board[x][y];
                if(findLeftLine(no, x, y, visited)) {
                    minId = Math.min(minId, no);
                }
            }
        }
        return minId;
    }

    static boolean findLeftLine(int no, int x, int y, boolean[][] visited) {
        int minX = x, maxX = x;
        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{x, y});
        visited[x][y] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            for(int dir = 0; dir < 4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(isNotBoard(nx, ny)) continue;
                if(visited[nx][ny]) continue;
                if(board[nx][ny] != no) continue;

                visited[nx][ny] = true;
                q.offer(new int[]{nx, ny});

                minX = Math.min(minX, nx);
                maxX = Math.max(maxX, nx);
            }
        }
        return canPopLeft(minX, maxX, y);
    }

    static boolean canPopLeft(int minX, int maxX, int y) {
        for(int x = minX; x <= maxX; x++) {
            for(int j = y - 1; j >= 0; j--) {
                if(board[x][j] == 0) continue;
                return false;
            }
        }
        return true;
    }

    // ===========================
    // 우측 하차
    // ===========================
    static int popRight() {
        int minId = INF;
        boolean[][] visited = new boolean[n][n];
        for(int x = 0; x < n; x++) {
            for(int y = n - 1; y >= 0; y--) {
                if(board[x][y] == 0 || visited[x][y]) {
                    continue;
                }
                // 현재 칸에 상자가 있는 경우
                int no = board[x][y];
                if(findRightLine(no, x, y, visited)) {
                    minId = Math.min(minId, no);
                }
            }
        }
        return minId;
    }

    static boolean findRightLine(int no, int x, int y, boolean[][] visited) {
        int minX = x, maxX = x;
        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{x, y});
        visited[x][y] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            for(int dir = 0; dir < 4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(isNotBoard(nx, ny)) continue;
                if(visited[nx][ny]) continue;
                if(board[nx][ny] != no) continue;

                visited[nx][ny] = true;
                q.offer(new int[]{nx, ny});

                minX = Math.min(minX, nx);
                maxX = Math.max(maxX, nx);
            }
        }
        return canPopRight(minX, maxX, y);
    }

    static boolean canPopRight(int minX, int maxX, int y) {
        for(int x = minX; x <= maxX; x++) {
            for(int j = y + 1; j < n; j++) {
                if(board[x][j] == 0) continue;
                return false;
            }
        }
        return true;
    }

    // ===========================
    // 공통
    // ===========================
    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
    static void printState() {
        for(int[] arr : board) System.out.println(Arrays.toString(arr));
    }
    static void printBox(Box box) {
        System.out.printf("no : %d, h : %d, w : %d, col: %d\n", box.no, box.h, box.w, box.col);
    }
}
