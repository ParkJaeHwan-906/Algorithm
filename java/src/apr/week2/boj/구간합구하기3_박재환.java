package apr.week2.boj;

import java.util.*;
import java.io.*;

public class 구간합구하기3_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }

    static final int SWAP = 0;
    static final int QUERY = 1;

    static class Command {
        int type;
        int x, y, v;
        int x1, y1, x2, y2;

        Command(int type, int x, int y, int v) {
            this.type = type;
            this.x = x;
            this.y = y;
            this.v = v;
        }

        Command(int type, int x1, int y1, int x2, int y2) {
            this.type = type;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

    static StringTokenizer st;
    static int n, m;
    static int[][] board;
    static Queue<Command> cmds;

    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        board = new int[n + 1][n + 1];
        for (int x = 1; x < n + 1; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for (int y = 1; y < n + 1; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
            }
        }

        cmds = new ArrayDeque<>();
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            if (type == SWAP) {
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                int v = Integer.parseInt(st.nextToken());
                Command cmd = new Command(type, x, y, v);
                cmds.offer(cmd);
            } else if (type == QUERY) {
                int x1 = Integer.parseInt(st.nextToken());
                int y1 = Integer.parseInt(st.nextToken());
                int x2 = Integer.parseInt(st.nextToken());
                int y2 = Integer.parseInt(st.nextToken());
                Command cmd = new Command(type, x1, y1, x2, y2);
                cmds.offer(cmd);
            }
        }

        solution();
    }
    static int[][] tree;
    static void solution() {
        tree = new int[4 * n][4 * n];
        buildX(1, 1, n);

        while(!cmds.isEmpty()) {
            Command cmd = cmds.poll();
            if(cmd.type == SWAP) {
                int x = cmd.x;
                int y = cmd.y;
                int v = cmd.v;
                updateX(1, 1, n, x, y, v);
            } else if(cmd.type == QUERY) {
                int x1 = cmd.x1;
                int y1 = cmd.y1;
                int x2 = cmd.x2;
                int y2 = cmd.y2;
                int result = queryX(1, 1, n, x1, y1, x2, y2);
                sb.append(result).append('\n');
            }
        }
    }
    // BUILD
    static void buildX(int nodeX, int startX, int endX) {
        if(startX == endX) {
            buildY(nodeX, startX, endX, 1, 1, n);
        } else {
            int mid = (startX + endX) / 2;
            buildX(nodeX * 2, startX, mid);
            buildX(nodeX * 2 + 1, mid + 1, endX);
            buildY(nodeX, startX, endX, 1, 1, n);
        }
    } 

    static void buildY(int nodeX, int startX, int endX, int nodeY, int startY, int endY) {
        if(startY == endY) {
            if(startX == endX) {        // 리프노드
                tree[nodeX][nodeY] = board[startX][startY];
            } else {
                tree[nodeX][nodeY] =
                        tree[nodeX * 2][nodeY] + tree[nodeX * 2 + 1][nodeY];
            }
        } else {
            int mid = (startY + endY) / 2;
            buildY(nodeX, startX, endX, nodeY * 2, startY, mid);
            buildY(nodeX, startX, endX, nodeY * 2 + 1, mid + 1, endY);

            tree[nodeX][nodeY] = tree[nodeX][nodeY * 2] + tree[nodeX][nodeY  * 2 + 1];
        }
    }

    // UPDATE
    static void updateX(int nodeX, int startX, int endX, int x, int y, int v) {
        if(startX == endX) {
            updateY(nodeX, startX, endX, 1, 1, n, x, y, v);
        } else {
            int mid = (startX + endX) / 2;
            if(x <= mid) {
               updateX(nodeX * 2, startX, mid, x, y, v);
            } else {
                updateX(nodeX * 2 + 1, mid + 1, endX, x, y, v);
            }
            updateY(nodeX, startX, endX, 1, 1, n, x, y, v);
        }
    }
    static void updateY(int nodeX, int startX, int endX, int nodeY, int startY, int endY, int x, int y, int v) {
        if(startY == endY) {
            if(startX == endX) {
                tree[nodeX][nodeY] = v;
            } else {
                tree[nodeX][nodeY] = tree[nodeX * 2][nodeY] + tree[nodeX * 2 + 1][nodeY];
            }
        } else {
            int mid = (startY + endY) / 2;
            if(y <= mid) {
                updateY(nodeX, startX, endX, nodeY * 2, startY, mid, x, y, v);
            } else {
                updateY(nodeX, startX, endX, nodeY * 2 + 1, mid + 1, endY, x, y, v);
            }
            tree[nodeX][nodeY] = tree[nodeX][nodeY * 2] + tree[nodeX][nodeY  * 2 + 1];
        }
    }

    // QUERY
    static int queryX(int nodeX, int startX, int endX, int x1, int y1, int x2, int y2) {
        if(x2 < startX || endX < x1) return 0;
        if(x1 <= startX && endX <= x2) {
            return queryY(nodeX, 1, 1, n, y1, y2);
        }

        int mid = (startX + endX) / 2;
        return queryX(nodeX * 2, startX, mid, x1, y1, x2, y2)
                + queryX(nodeX * 2 + 1, mid + 1, endX, x1, y1, x2, y2);
    }

    static int queryY(int nodeX, int nodeY, int startY, int endY, int y1, int y2) {
        if(y2 < startY || endY < y1) return 0;
        if(y1 <= startY && endY <= y2) {
            return tree[nodeX][nodeY];
        }

        int mid = (startY + endY) / 2;
        return queryY(nodeX, nodeY * 2, startY, mid, y1, y2)
                + queryY(nodeX, nodeY * 2 + 1, mid + 1, endY, y1, y2);
    }
}
