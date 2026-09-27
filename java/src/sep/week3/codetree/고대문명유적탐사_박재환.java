package sep.week3.codetree;

import java.util.*;
import java.io.*;

public class 고대문명유적탐사_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        solution();
        br.close();
    }

    static final int SIZE = 5;
    static final int GRID_SIZE = 3;

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static int k, m;
    static int[][] board;
    static Queue<Integer> extraBlocks;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        k = Integer.parseInt(st.nextToken());       // 반복횟수
        m = Integer.parseInt(st.nextToken());       // 유물조각 수
        board = new int[SIZE][SIZE];
        for(int x = 0; x < SIZE; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < SIZE; y++) {
                /**
                 * 각 조각은 1 ~ 7
                 */
                board[x][y] = Integer.parseInt(st.nextToken());
            }
        }
        extraBlocks = new ArrayDeque<>();
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < m; i++) {
            extraBlocks.offer(Integer.parseInt(st.nextToken()));
        }
    }

    static void solution() {
        StringBuilder sb = new StringBuilder();
        while(k-- > 0) {            // K 만큼 반복
            Profit profit = findGrid();
            if(profit.removedLocs.isEmpty()) {
                break;
            }
            int total = profit.removedLocs.size();
            board = remove(profit);
            total += chainRemoved();

            if(sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(total);
        }
        System.out.print(sb);
    }

    static int chainRemoved() {
        int total = 0;
        while(true) {
            List<int[]> removedLocs = findRemovedLocs();
            if(removedLocs.isEmpty()) {
                break;
            }
            total += removedLocs.size();
            removedLocs.sort((a, b) -> {
                if(a[1] != b[1]) {
                    return Integer.compare(a[1], b[1]);
                }
                return Integer.compare(b[0], a[0]);
            });
            for(int[] loc : removedLocs) {
                board[loc[0]][loc[1]] = extraBlocks.poll();
            }
        }
        return total;
    }

    static List<int[]> findRemovedLocs() {
        List<int[]> removedLocs = new ArrayList<>();
        boolean[][] visited =new boolean[SIZE][SIZE];
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                if(visited[x][y]) {
                    continue;
                }
                List<int[]> removedCandidates = findAdj(x, y, board, visited);
                if(removedCandidates.size() < 3) {
                    continue;
                }
                removedLocs.addAll(removedCandidates);
            }
        }
        return removedLocs;
    }

    static int[][] remove(Profit profit) {
        int[][] newBoard = profit.tempBoard;
        profit.removedLocs.sort((a, b) -> {
            if(a[1] != b[1]) {
                return Integer.compare(a[1], b[1]);
            }
            return Integer.compare(b[0], a[0]);
        });
        for(int[] loc : profit.removedLocs) {
            newBoard[loc[0]][loc[1]] = extraBlocks.poll();
        }
        return newBoard;
    }

    static class Profit {
        int x, y;
        int rotateCount;
        List<int[]> removedLocs;
        int[][] tempBoard;
        Profit(int x, int y, int rotateCount, List<int[]> removedLocs, int[][] tempBoard) {
            this.x = x;
            this.y = y;
            this.rotateCount = rotateCount;
            this.removedLocs = removedLocs;
            this.tempBoard = copyBoard(tempBoard);
        }
        int[][] copyBoard(int[][] tempBoard) {
            int[][] newBoard = new int[SIZE][SIZE];
            for(int x = 0; x < SIZE; x++) {
                for(int y = 0; y < SIZE; y++) {
                    newBoard[x][y] = tempBoard[x][y];
                }
            }
            return newBoard;
        }
    }

    static Profit findGrid() {
        Profit bestP = new Profit(6, 6, 6, new ArrayList<>(), new int[SIZE][SIZE]);
        for(int x = 0; x <= SIZE - GRID_SIZE; x++) {
            for(int y = 0; y <= SIZE - GRID_SIZE; y++) {
                bestP = compareProfit(bestP, predictProfit(x, y));
            }
        }
        return bestP;
    }

    static Profit predictProfit(int r, int c) {
        int[][] grid = new int[GRID_SIZE][GRID_SIZE];
        for(int x = r; x < r + GRID_SIZE; x++) {
            for(int y = c; y < c + GRID_SIZE; y++) {
                grid[x - r][y - c] = board[x][y];
            }
        }

        int[][] tempBoard = new int[SIZE][SIZE];
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                tempBoard[x][y] = board[x][y];
            }
        }
        Profit p =new Profit(6, 6, 6, new ArrayList<>(), new int[SIZE][SIZE]);
        // 총 3번 90도씩 시계방향 회전
        for(int i = 1; i <= 3; i++) {
            grid = rotateClockWise(grid);      // grid 회전
            applyTempBoard(r, c, grid, tempBoard);
            List<int[]> removedLocs = gain(tempBoard);
            Profit newProfit = new Profit(r, c, i, removedLocs, tempBoard);
            p = compareProfit(p, newProfit);
        }
        return p;
    }

    static Profit compareProfit(Profit p1, Profit p2) {
        if (p1.removedLocs.size() != p2.removedLocs.size()) {
            return p1.removedLocs.size() > p2.removedLocs.size()
                    ? p1 : p2;
        }
        if (p1.rotateCount != p2.rotateCount) {
            return p1.rotateCount > p2.rotateCount
                    ? p2 : p1;
        }
        if (p1.y != p2.y) {
            return p1.y > p2.y
                    ? p2 : p1;
        }
        return p1.x > p2.x
                ? p2 : p1;
    }

    static int[][] rotateClockWise(int[][] grid) {
        int[][] tempGrid = new int[GRID_SIZE][GRID_SIZE];
        for(int x = 0; x < GRID_SIZE; x++) {
            for(int y = 0; y < GRID_SIZE; y++) {
                tempGrid[y][GRID_SIZE - 1 - x] = grid[x][y];
            }
        }
        return tempGrid;
    }

    static void applyTempBoard(int r, int c, int[][] grid, int[][] tempBoard) {
        for(int x = 0; x < GRID_SIZE; x++) {
            for(int y = 0; y < GRID_SIZE; y++) {
                tempBoard[x + r][y + c] = grid[x][y];
            }
        }
    }

    static List<int[]> gain(int[][] tempBoard) {
        List<int[]> removedLocs = new ArrayList<>();
        boolean[][] visited =new boolean[SIZE][SIZE];
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                if(visited[x][y]) {
                    continue;
                }
                List<int[]> removedCandidates = findAdj(x, y, tempBoard, visited);
                if(removedCandidates.size() < 3) {
                    continue;
                }
                removedLocs.addAll(removedCandidates);
            }
        }
        return removedLocs;
    }

    static List<int[]> findAdj(int r, int c, int[][] tempBoard, boolean[][] visited) {
        List<int[]> removedLocs = new ArrayList<>();
        int groupId = tempBoard[r][c];
        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[] {r, c});
        visited[r][c] = true;
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            removedLocs.add(cur);
            for(int d = 0; d < 4; d++) {
                int nx = cur[0] + dx[d];
                int ny = cur[1] + dy[d];
                if(isNotBoard(nx, ny)
                        || visited[nx][ny]
                        || tempBoard[nx][ny] != groupId) {
                    continue;
                }
                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny});
            }
        }
        return removedLocs;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= SIZE || y >= SIZE;
    }
}
