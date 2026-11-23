package nov.week3.programmers.아이템줍기_박재환;

import java.util.*;

public class 아이템줍기_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        int[][][] rectangles = {
        {{1, 1, 7, 4}, {3, 2, 5, 5}, {4, 3, 6, 9}, {2, 6, 8, 8}},
        {{1, 1, 8, 4}, {2, 2, 4, 9}, {3, 6, 9, 8}, {6, 3, 7, 7}},
        {{1, 1, 5, 7}},
        {{2, 1, 7, 5}, {6, 4, 10, 10}},
        {{2, 2, 5, 5}, {1, 3, 6, 4}, {3, 1, 4, 6}}
        };
        int[][] positions = {
        {1, 3, 7, 8},
        {9, 7, 6, 1},
        {1, 1, 4, 7},
        {3, 1, 7, 10},
        {1, 4, 6, 3}
        };
        int[] expected = {17, 11, 9, 15, 10};

        for (int i = 0; i < rectangles.length; i++) {
            int[] position = positions[i];
            int actual = solution.solution(rectangles[i], position[0], position[1], position[2], position[3]);
            System.out.println("TC" + (i + 1) + ": " + actual + " (예상: " + expected[i] + ")");
        }
    }
}

class Solution {
    public int solution(int[][] rectangle, int characterX, int characterY, int itemX, int itemY) {
        int[][] board = new int[101][101];
        boolean[][] checked = new boolean[101][101];

        // 1. 사각형 내부 채우기
        for(int[] arr : rectangle) {
            int minX = arr[0] * 2;
            int minY = arr[1] * 2;
            int maxX = arr[2] * 2;
            int maxY = arr[3] * 2;

            for(int x = minX; x <= maxX; x++) {
                for(int y = minY; y <= maxY; y++) {
                    if(!(x == minX || x == maxX || y == minY || y == maxY))
                        checked[x][y] = true;
                }
            }
        }
        // 2. 테두리 채우기
        for(int[] arr : rectangle) {
            int minX = arr[0] * 2;
            int minY = arr[1] * 2;
            int maxX = arr[2] * 2;
            int maxY = arr[3] * 2;

            for(int x = minX; x <= maxX; x++) {
                for(int y = minY; y <= maxY; y++) {
                    if(!checked[x][y] && (x == minX || x == maxX || y == minY || y == maxY))
                        board[x][y] = 1;
                }
            }
        }

        // 3. 캐릭터 이동
        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[101][101];

        q.offer(new int[] {2 * characterX, 2 * characterY, 0});
        visited[2 * characterX][2 * characterY] = true;

        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int dist = cur[2];
            if(x == 2 * itemX && y == 2 * itemY) {
                return dist / 2;
            }
            for(int dir = 0; dir < 4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];
                if(nx < 0 || ny < 0 || nx >= 101 || ny >= 101)
                    continue;
                if(visited[nx][ny] || board[nx][ny] == 0)
                    continue;
                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny, dist + 1});
            }
        }
        return -1;
    }
}