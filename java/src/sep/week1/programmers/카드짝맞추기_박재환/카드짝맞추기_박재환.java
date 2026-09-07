package sep.week1.programmers.카드짝맞추기_박재환;

import java.util.*;

public class 카드짝맞추기_박재환 {
    public static void main(String[] args) {
        int[][] board = {{1, 0, 0, 3},
                         {2, 0, 0, 0},
                         {0, 0, 0, 2},
                         {3, 0, 1, 0}};
        int r = 1;
        int c = 0;
        Solution sol = new Solution();
        System.out.print(sol.solution(board, r, c));
    }
}

class Solution {

    static final int SIZE = 4;
    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static int[][] board;
    public int solution(int[][] board, int r, int c) {
        set(board);
        return searchSeq(r, c);
    }
    static void set(int[][] temp) {
        board = new int[SIZE][SIZE];
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                board[x][y] = temp[x][y];
            }
        }
    }
    // ===============================
    // 탐색 순서
    // ===============================
    static int searchSeq(int x, int y) {
        int answer = Integer.MAX_VALUE;
        for(int no = 1; no <= 6; no++) {
            List<Point> cards = findCards(no);
            if(cards.size() != 2) {
                continue;
            }
            Point c1 = cards.get(0);
            Point c2 = cards.get(1);

            int cost1 = findMinCommands(x, y, c1.x, c1.y) +
                    findMinCommands(c1.x, c1.y, c2.x, c2.y) + 2;
            board[c1.x][c1.y] = 0;
            board[c2.x][c2.y] = 0;
            int next1 = searchSeq(c2.x, c2.y);
            board[c1.x][c1.y] = no;
            board[c2.x][c2.y] = no;
            answer = Math.min(answer, cost1 + next1);
            int cost2 = findMinCommands(x, y, c2.x, c2.y) +
                    findMinCommands(c2.x, c2.y, c1.x, c1.y) + 2;
            board[c1.x][c1.y] = 0;
            board[c2.x][c2.y] = 0;
            int next2 = searchSeq(c1.x, c1.y);
            board[c1.x][c1.y] = no;
            board[c2.x][c2.y] = no;
            answer = Math.min(answer, cost2 + next2);
        }
        if(answer == Integer.MAX_VALUE) {
            return 0;
        }
        return answer;
    }
    // ===============================
    // 최소 명령어 찾기
    // ===============================
    static int findMinCommands(int x1, int y1, int x2, int y2) {
        if(x1 == x2 &&  y1 == y2) {
            return 0;
        }

        boolean[][] visited = new boolean[SIZE][SIZE];
        Queue<int[]> q = new ArrayDeque<>();

        q.offer(new int[] {x1, y1, 0});
        visited[x1][y1] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            if(cur[0] == x2  && cur[1] == y2) {
                return cur[2];
            }
            for(int dir = 0; dir < 4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(!isNotBoard(nx, ny) && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    q.offer(new int[]{nx, ny, cur[2] + 1});
                }

                int[] newLoc = ctrlMove(cur[0], cur[1], dir);
                nx = newLoc[0];
                ny = newLoc[1];
                if(!isNotBoard(nx, ny) && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    q.offer(new int[]{nx, ny, cur[2] + 1});
                }
            }
        }
        return -1;
    }
    static int[] ctrlMove(int x, int y, int dir) {
        while(true) {
            x += dx[dir];
            y += dy[dir];
            if(isNotBoard(x, y)) {
                x -= dx[dir];
                y -= dy[dir];
                break;
            }
            if(board[x][y] != 0) {
                break;
            }
        }
        return new int[] {x, y};
    }

    // ===============================
    // 공통
    // ===============================
    static class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    static List<Point> findCards(int no) {
        List<Point> cards = new ArrayList<>();
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                if(board[x][y] == no) {
                    cards.add(new Point(x, y));
                }
            }
        }
        return cards;
    }
    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= SIZE || y >= SIZE;
    }
}