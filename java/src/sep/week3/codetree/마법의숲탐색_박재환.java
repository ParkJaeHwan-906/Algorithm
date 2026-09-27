package sep.week3.codetree;

import java.util.*;
import java.io.*;

public class 마법의숲탐색_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    // 북 동 남 서
    static final int[] dx = {-1, 0, 1, 0};
    static final int[] dy = {0, 1, 0, -1};

    static class Golem {
        int x, y;       // 중심
        int d;          // 출구
        Golem(int x, int y, int d) {
            this.x = x;
            this.y = y;
            this.d = d;
        }
        int turnClockwise() {
            return (this.d + 1) % 4;
        }
        int turnCounterclockwise() {
            return (this.d - 1 + 4) % 4;
        }
    }

    static int golemId;
    static int score;
    static int r, c, k;
    static int[][] golemBoard;
    static boolean[][] isExit;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        r = Integer.parseInt(st.nextToken());           // 행
        c = Integer.parseInt(st.nextToken());           // 열
        k = Integer.parseInt(st.nextToken());           // 골렘 수
        golemBoard = new int[r + 3][c];                      // 골렘이 + 모양, 외부에서 들어온다 ( => x : 3 부터 실제 board )
        isExit = new boolean[r + 3][c];
        golemId = 0;
        score = 0;
        for(int i = 0; i < k; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int inputY = Integer.parseInt(st.nextToken()) - 1;
            int d = Integer.parseInt(st.nextToken());
            Golem golem = new Golem(0, inputY, d);
            score += putGolem(++golemId, golem);
        }
        System.out.print(score);
    }

    static int putGolem(int id, Golem golem) {
        while(true) {
            // 1. 남쪽으로 내려간다.
            if(moveSouth(golem)) {
                continue;
            }
            // 2. 서쪽(반시계)으로 회전해 내려간다.
            if(moveWest(golem)) {
                continue;
            }
            // 3. 동쪽(시계)으로 회전해 내려간다.
            if(moveEast(golem)) {
                continue;
            }
            break;          // 더 이상 이동할 수 없음
        }

        // 최종위치 기록
        if(golem.x < 4) {           // 몸의 일부가 격자 밖에 존재
            reset();
            return 0;
        }
        List<int[]> finalLocs = getGolemLocs(golem.x, golem.y);
        for(int[] arr : finalLocs) {
            golemBoard[arr[0]][arr[1]] = id;
        }
        isExit[golem.x + dx[golem.d]][golem.y + dy[golem.d]] = true;
        return getMaxRow(golem);
    }
    static boolean moveSouth(Golem golem) {
        int nx = golem.x + 1;
        int ny = golem.y;
        List<int[]> locs = getGolemLocs(nx, ny);
        if(!isPossible(locs)) {
            return false;
        }
        golem.x = nx;
        golem.y = ny;
        return true;
    }
    static boolean moveWest(Golem golem) {
        // 왼쪽 -> 아래 순으로 이동
        int nx = golem.x;
        int ny = golem.y - 1;
        List<int[]> locs = getGolemLocs(nx, ny);
        if(!isPossible(locs)) {
            return false;
        }
        nx += 1;
        locs = getGolemLocs(nx, ny);
        if(!isPossible(locs)) {
            return false;
        }
        golem.x = nx;
        golem.y = ny;
        golem.d = golem.turnCounterclockwise();
        return true;
    }

    static boolean moveEast(Golem golem) {
        // 오른쪽 -> 아래 순으로 이동
        int nx = golem.x;
        int ny = golem.y + 1;
        List<int[]> locs = getGolemLocs(nx, ny);
        if(!isPossible(locs)) {
            return false;
        }
        nx += 1;
        locs = getGolemLocs(nx, ny);
        if(!isPossible(locs)) {
            return false;
        }
        golem.x = nx;
        golem.y = ny;
        golem.d = golem.turnClockwise();
        return true;
    }

    // =========================================
    // 공통
    // =========================================
    static boolean isNotBoard(int x ,int y) {
        return x < 0 || y < 0 || x >= r + 3|| y >= c;
    }
    static List<int[]> getGolemLocs(int x, int y) {
        List<int[]> locs = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            locs.add(new int[] {nx, ny});
        }
        locs.add(new int[] {x, y});
        return locs;
    }
    static boolean isPossible(List<int[]> locs) {
        for(int[] loc : locs) {
            if(isNotBoard(loc[0], loc[1])
                    || golemBoard[loc[0]][loc[1]] != 0) {            // 격자를 벗어나거나, 이미 다른 골렘이 있는 경우
                return false;
            }
        }
        return true;
    }
    static void reset() {
        golemId = 0;
        golemBoard = new int[r + 3][c];
        isExit = new boolean[r + 3][c];
    }
    static int getMaxRow(Golem golem) {
        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[r + 3][c];
        q.offer(new int[] {golem.x, golem.y});
        int bestX = golem.x;
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            bestX = Math.max(bestX, cur[0]);
            for(int i = 0; i < 4; i++) {
                int nx = cur[0] + dx[i];
                int ny = cur[1] + dy[i];
                if(isNotBoard(nx, ny) || visited[nx][ny]) {
                    continue;
                }
                if (golemBoard[nx][ny] == 0) {
                    continue;
                }
                if(golemBoard[cur[0]][cur[1]] != golemBoard[nx][ny] && !isExit[cur[0]][cur[1]]) {
                    continue;
                }
                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny});
            }
        }
        return bestX - 2;
    }
}
