package nov.week2.programmers.퍼즐조각채우기_박재환;

import java.util.*;

public class 퍼즐조각채우기_박재환 {
    public static void main(String[] args) {
        int[][] gameBoard1 = {
                {1, 1, 0, 0, 1, 0},
                {0, 0, 1, 0, 1, 0},
                {0, 1, 1, 0, 0, 1},
                {1, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 1, 0},
                {0, 1, 1, 1, 0, 0}
        };
        int[][] table1 = {
                {1, 0, 0, 1, 1, 0},
                {1, 0, 1, 0, 1, 0},
                {0, 1, 1, 0, 1, 1},
                {0, 0, 1, 0, 0, 0},
                {1, 1, 0, 1, 1, 0},
                {0, 1, 0, 0, 0, 0}
        };
        int[][] gameBoard2 = {
                {0, 0, 0},
                {1, 1, 0},
                {1, 1, 1}
        };
        int[][] table2 = {
                {1, 1, 1},
                {1, 0, 0},
                {0, 0, 0}
        };

        Solution solution = new Solution();
        System.out.println(solution.solution(gameBoard1, table1));
        System.out.println(solution.solution(gameBoard2, table2));
    }
}

class Solution {

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    public int solution(int[][] game_board, int[][] table) {
        List<int[][]> blocks = findSpace(table, 1);
        List<int[][]> emptys = findSpace(game_board, 0);

        Map<String, Integer> blockCount = new HashMap<>();
        for (int[][] block : blocks) {
            String key = normalize(block);
            blockCount.put(key, blockCount.getOrDefault(key, 0) + 1);
        }

        int answer = 0;
        for (int[][] empty : emptys) {
            String key = normalize(empty);
            if (blockCount.getOrDefault(key, 0) == 0)
                continue;
            blockCount.put(key, blockCount.get(key) - 1);
            for (int[] row : empty) {
                for (int value : row)
                    answer += value;
            }
        }
        return answer;
    }

    List<int[][]> findSpace(int[][] arr, int target) {
        boolean[][] visited = new boolean[arr.length][arr.length];
        List<int[][]> blocks = new ArrayList<>();
        for(int x = 0; x < arr.length; x++) {
            for(int y = 0; y < arr[x].length; y++) {
                if(arr[x][y] != target || visited[x][y])
                    continue;

                int minX = x, minY = y, maxX = x, maxY = y;
                Queue<int[]> q = new ArrayDeque<>();
                List<int[]> cand = new ArrayList<>();
                q.offer(new int[] {x, y});
                visited[x][y] = true;
                while(!q.isEmpty()) {
                    int[] cur = q.poll();
                    cand.add(cur);
                    for(int dir = 0; dir < 4; dir++) {
                        int nx = cur[0] + dx[dir];
                        int ny = cur[1] + dy[dir];
                        if(isNotBoard(nx, ny, arr) || visited[nx][ny] || arr[nx][ny] != target)
                            continue;
                        visited[nx][ny] = true;
                        q.offer(new int[] {nx, ny});
                        minX = Math.min(minX, nx);
                        minY = Math.min(minY, ny);
                        maxX = Math.max(maxX, nx);
                        maxY = Math.max(maxY, ny);
                    }
                }
                int[][] block = new int[maxX - minX + 1][maxY - minY + 1];
                for(int[] point : cand)
                    block[point[0] - minX][point[1] - minY] = 1;
                blocks.add(block);
            }
        }
        return blocks;
    }

    String normalize(int[][] block) {
        String minPattern = null;
        for (int i = 0; i < 4; i++) {
            String pattern = toPattern(block);
            if (minPattern == null || pattern.compareTo(minPattern) < 0)
                minPattern = pattern;
            block = rotate(block);
        }
        return minPattern;
    }

    int[][] rotate(int[][] block) {
        int n = block.length;
        int m = block[0].length;
        int[][] rotated = new int[m][n];
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++)
                rotated[y][n - 1 - x] = block[x][y];
        }
        return rotated;
    }

    String toPattern(int[][] block) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : block) {
            for (int value : row)
                sb.append(value);
            sb.append('/');
        }
        return sb.toString();
    }

    boolean isNotBoard(int x, int y, int[][] arr) {
        return x < 0 || y < 0 || x >= arr.length || y >= arr[x].length;
    }
}
