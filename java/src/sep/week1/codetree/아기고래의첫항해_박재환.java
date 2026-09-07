package sep.week1.codetree;

import java.util.*;
import java.io.*;

public class 아기고래의첫항해_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    // 상 하 좌 우
    static final int[] nearPriority = {2, 1, 3, 0}; // 좌, 하, 우, 상
    static final int[] dx = {-1, 1, 0, 0};
    static final int[] dy = {0, 0, -1, 1};

    static class Whale {
        int x, y;
        int dir;
        Whale(int x, int y, int dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

        int rotateCounterClockwise() {
            if(dir == 0) return 2;              // 상 -> 좌
            if(dir == 1) return 3;              // 하 -> 우
            if(dir == 2) return 1;              // 좌 -> 하
            return 0;                           // 우 -> 상
        }

        int rotateClockwise() {
            if(dir == 0) return 3;              // 상 -> 우
            if(dir == 1) return 2;              // 하 -> 좌
            if(dir == 2) return 0;              // 좌 -> 상
            return 1;                           // 우 -> 하
        }

        int counterDir() {
            if(dir == 0) return 1;              // 상 -> 하
            if(dir == 1) return 0;              // 하 -> 상
            if(dir == 2) return 3;              // 좌 -> 우
            return 2;                           // 우 -> 좌
        }
    }

    static int n, r, c, d;
    static int[][] board;
    static boolean[][] visited;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        r = Integer.parseInt(st.nextToken()) - 1;
        c = Integer.parseInt(st.nextToken()) - 1;
        d = Integer.parseInt(st.nextToken()) - 1;

        Whale whale = new Whale(r, c, d);

        board = new int[n][n];
        visited = new boolean[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            /**
             * 0 : 바다
             * 1 : 암초
             */
            for(int y = 0; y < n; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
            }
        }

        System.out.print(solution(whale));
    }

    static String solution(Whale whale) {
        StringBuilder sb = new StringBuilder();
        visited[whale.x][whale.y] = true;
        sb.append(String.format("%d %d", whale.x + 1, whale.y + 1)).append('\n');
        while(true) {
            // 인접 바다 탐색
            SearchResult adjSearchResult = adjSearch(whale);
            if(adjSearchResult.find) {
                whale.x = adjSearchResult.x;
                whale.y = adjSearchResult.y;
                whale.dir = adjSearchResult.dir;
                visited[whale.x][whale.y] = true;
                sb.append(String.format("%d %d", whale.x + 1, whale.y + 1)).append('\n');
                continue;
            }
            // 가장 가까운 바다 탐색
            SearchResult nearSearchResult = nearSearch(whale);
            if(nearSearchResult.find) {
                whale.x = nearSearchResult.x;
                whale.y = nearSearchResult.y;
                whale.dir = nearSearchResult.dir;
                visited[whale.x][whale.y] = true;
                sb.append(String.format("%d %d", whale.x + 1, whale.y + 1)).append('\n');
                continue;
            }
            break;
        }
        return sb.toString();
    }
    // ================================
    // 인접 바다 탐색
    // ================================
    static SearchResult adjSearch(Whale whale) {
        int x = whale.x, y = whale.y;
        int nx, ny;
        int dir = whale.dir;

        // 직진
        nx = x + dx[dir];
        ny = y + dy[dir];
        if(!isNotBoard(nx, ny) && !visited[nx][ny] && board[nx][ny] == 0) {
            return new SearchResult(true, nx, ny, dir);
        }
        // 반시계
        dir = whale.rotateCounterClockwise();
        nx = x + dx[dir];
        ny = y + dy[dir];
        if(!isNotBoard(nx, ny) && !visited[nx][ny] && board[nx][ny] == 0) {
            return new SearchResult(true, nx, ny, dir);
        }
        // 시계
        dir = whale.rotateClockwise();
        nx = x + dx[dir];
        ny = y + dy[dir];
        if(!isNotBoard(nx, ny) && !visited[nx][ny] && board[nx][ny] == 0) {
            return new SearchResult(true, nx, ny, dir);
        }
        // 정반대
        dir = whale.counterDir();
        nx = x + dx[dir];
        ny = y + dy[dir];
        if(!isNotBoard(nx, ny) && !visited[nx][ny] && board[nx][ny] == 0) {
            return new SearchResult(true, nx, ny, dir);
        }
        return new SearchResult(false);
    }
    // ================================
    // 가까운 바다 탐색
    // ================================
    static SearchResult nearSearch(Whale whale) {
        int x = whale.x, y = whale.y;

        int bestDist = Integer.MAX_VALUE;
        int bestX = Integer.MAX_VALUE, bestY = Integer.MAX_VALUE;
        int bestDir = -1;

        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] temp = new boolean[n][n];

        q.offer(new int[] {x, y, 0, -1});
        temp[x][y] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int curX = cur[0], curY = cur[1], curDist = cur[2], curDir = cur[3];
            if(!visited[curX][curY]) {      // 이동 가능한 칸
                if(bestDist > curDist ||
                        (bestDist == curDist && bestX > curX) ||
                        (bestDist == curDist && bestX == curX && bestY > curY)) {
                    bestDist = curDist;
                    bestX = curX;
                    bestY = curY;
                    bestDir = curDir;
                }
                continue;
            }
            if(curDist >= bestDist) {
                continue;
            }
            for(int d = 0; d < 4; d++) {
                int dir = nearPriority[d];
                int nx = curX + dx[dir];
                int ny = curY + dy[dir];
                if(isNotBoard(nx, ny) || temp[nx][ny] || board[nx][ny] == 1) {
                    continue;
                }
                temp[nx][ny] = true;
                q.offer(new int[] {nx, ny, curDist + 1, dir});
            }
        }
        return bestDist == Integer.MAX_VALUE ?
                new SearchResult(false) :
                new SearchResult(true, bestX, bestY, bestDir);
    }
    // ================================
    // 공통
    // ================================
    static class SearchResult {
        boolean find;
        int x, y;
        int dir;
        SearchResult(boolean find, int x, int y, int dir) {
            this.find = find;
            this.x = x;
            this.y = y;
            this.dir = dir;
        }
        SearchResult(boolean find) {
            this.find = find;
        }
    }
    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}
