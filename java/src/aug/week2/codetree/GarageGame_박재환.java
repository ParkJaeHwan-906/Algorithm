package aug.week2.codetree;

import java.util.*;
import java.io.*;

public class GarageGame_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int n;
    static int[][][] boards;
    static Group[][] groups;
    static boolean[][] visited;
    static int[] q;

    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        n = Integer.parseInt(br.readLine().trim());
        boards = new int[4][3 * n][n];

        for(int x = 3 * n - 1; x >= 0; x--) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) boards[0][x][y] = Integer.parseInt(st.nextToken());
        }

        groups = new Group[3][n * n];
        for(int depth = 0; depth < 3; depth++) {
            for(int i = 0; i < n * n; i++) groups[depth][i] = new Group();
        }

        visited = new boolean[n][n];
        q = new int[n * n];

        System.out.println(solution());
    }

    static int solution() {
        return dfs(0);
    }

    static int dfs(int depth) {
        if(depth == 3) return 0;

        int groupCount = findSameGroups(depth);
        int maxScore = 0;

        for(int i = 0; i < groupCount; i++) {
            Group group = groups[depth][i];

            copyBoard(depth, depth + 1);
            removeCars(group, boards[depth + 1]);
            drop(boards[depth + 1]);

            int score = group.groupSize + group.rectangleArea + dfs(depth + 1);
            maxScore = Math.max(maxScore, score);
        }

        return maxScore;
    }

    static void copyBoard(int originDepth, int nextDepth) {
        for(int x = 0; x < 3 * n; x++) {
            System.arraycopy(boards[originDepth][x], 0, boards[nextDepth][x], 0, n);
        }
    }

    static void removeCars(Group group, int[][] board) {
        int originColor = board[group.startX][group.startY];
        int front = 0;
        int rear = 0;

        q[rear++] = group.startX * n + group.startY;
        board[group.startX][group.startY] = 0;

        while(front < rear) {
            int cur = q[front++];
            int x = cur / n;
            int y = cur % n;

            for(int dir = 0; dir < 4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];

                if(isNotBoard(nx, ny)) continue;
                if(board[nx][ny] != originColor) continue;

                board[nx][ny] = 0;
                q[rear++] = nx * n + ny;
            }
        }
    }

    static void drop(int[][] board) {
        for(int y = 0; y < n; y++) {
            int writeX = 0;

            for(int x = 0; x < 3 * n; x++) {
                if(board[x][y] == 0) continue;

                if(writeX != x) {
                    board[writeX][y] = board[x][y];
                    board[x][y] = 0;
                }
                writeX++;
            }

            while(writeX < 3 * n) board[writeX++][y] = 0;
        }
    }

    static int findSameGroups(int depth) {
        int[][] board = boards[depth];

        for(int x = 0; x < n; x++) Arrays.fill(visited[x], false);

        int groupCount = 0;
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(visited[x][y]) continue;

                Group group = findSameColor(x, y, board);
                groups[depth][groupCount++].set(
                        group.startX,
                        group.startY,
                        group.groupSize,
                        group.rectangleArea
                );
            }
        }
        return groupCount;
    }

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static class Group {
        int startX;
        int startY;
        int groupSize;
        int rectangleArea;

        Group() {}

        Group(int startX, int startY, int groupSize, int rectangleArea) {
            set(startX, startY, groupSize, rectangleArea);
        }

        void set(int startX, int startY, int groupSize, int rectangleArea) {
            this.startX = startX;
            this.startY = startY;
            this.groupSize = groupSize;
            this.rectangleArea = rectangleArea;
        }
    }

    static Group findSameColor(int x, int y, int[][] board) {
        int groupSize = 0;
        int minX = x;
        int maxX = x;
        int minY = y;
        int maxY = y;
        int front = 0;
        int rear = 0;

        q[rear++] = x * n + y;
        visited[x][y] = true;

        int originColor = board[x][y];
        while(front < rear) {
            int cur = q[front++];
            int currentX = cur / n;
            int currentY = cur % n;
            groupSize++;

            minX = Math.min(minX, currentX);
            maxX = Math.max(maxX, currentX);
            minY = Math.min(minY, currentY);
            maxY = Math.max(maxY, currentY);

            for(int dir = 0; dir < 4; dir++) {
                int nx = currentX + dx[dir];
                int ny = currentY + dy[dir];

                if(isNotBoard(nx, ny)) continue;
                if(visited[nx][ny]) continue;
                if(board[nx][ny] != originColor) continue;

                visited[nx][ny] = true;
                q[rear++] = nx * n + ny;
            }
        }

        int rectangleArea = (maxX - minX + 1) * (maxY - minY + 1);
        return new Group(x, y, groupSize, rectangleArea);
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}
