package jul.week2.codetree;

import java.util.*;
import java.io.*;

public class 로봇이지나간경로_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int h, w;
    static char[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        h = Integer.parseInt(st.nextToken());
        w = Integer.parseInt(st.nextToken());
        board = new char[h][w];
        for(int x = 0; x < h; x++) {
            String line = br.readLine().trim();
            /**
             * # : 방문함
             * . : 방문하지 않음
             */
            for(int y = 0; y < w; y++) board[x][y] = line.charAt(y);
        }

        solution();
    }

    static class Loc {
        int x, y;
        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static final int INF = Integer.MAX_VALUE;
    static int minCommandCnt;
    static Loc startLoc;
    static int startDir;
    static String command;
    static List<Loc> startLocCands;
    static void solution() {
        /**
         * 1. 처음 로봇을 어떤 칸에, 어떤 방향으로 두어야 하는가
         * 2. 이후 로봇에 어떤 명령어를 어떤 순서로 입력해야하는가
         */
        findStartLoc();

        minCommandCnt = INF;
        startLoc = new Loc(INF, INF);
        for(Loc loc : startLocCands) {
            for(int dir = 0; dir < 4; dir++) {
                boolean[][] visited = new boolean[h][w];
                visited[loc.x][loc.y] = true;
                simulation(loc, loc, 1, 0, dir, dir, visited, new StringBuilder());
            }
        }

        System.out.println((startLoc.x + 1) + " " + (startLoc.y + 1));
        System.out.println(dirChar[startDir]);
        System.out.println(command);
    }

    static final int[] dx = {-1, 0, 1, 0};
    static final int[] dy = {0, 1, 0, -1};
    static final char[] dirChar = {'^', '>', 'v', '<'};

    static void simulation(Loc loc, Loc origin, int moveCount, int commandCnt,
                           int prev, int originDir, boolean[][] visited, StringBuilder commands) {
        if(moveCount == startLocCands.size()) {         // 모든 지점 방문 완료
            if(minCommandCnt > commandCnt ||
                    (minCommandCnt == commandCnt && (startLoc.x < origin.x ||
                            (startLoc.x == origin.x && startLoc.y < origin.y)))) {
                minCommandCnt = commandCnt;
                startLoc = new Loc(origin.x, origin.y);
                startDir = originDir;
                command = commands.toString();
            }
            return;
        }
        if(commandCnt >= minCommandCnt) return;

        // 1. 기존 방향으로 이동
        move(loc, origin, moveCount, commandCnt, prev, originDir, prev, 0, visited, commands);

        // 2. 좌회전 후 이동
        int left = turnLeft(prev);
        move(loc, origin, moveCount, commandCnt, prev, originDir, left, 1, visited, commands);

        // 3. 우회전 후 이동
        int right = turnRight(prev);
        move(loc, origin, moveCount, commandCnt, prev, originDir, right, 1, visited, commands);
    }

    static void move(Loc loc, Loc origin, int moveCount, int commandCnt,
                     int prev, int originDir, int next, int turnCount,
                     boolean[][] visited, StringBuilder commands) {
        int mx = loc.x + dx[next];
        int my = loc.y + dy[next];
        int nx = loc.x + dx[next] * 2;
        int ny = loc.y + dy[next] * 2;

        if(isNotBoard(mx, my) || isNotBoard(nx, ny)) return;
        if(board[mx][my] != '#' || board[nx][ny] != '#') return;
        if(visited[mx][my] || visited[nx][ny]) return;

        int commandLength = commands.length();
        if(turnCount == 1) {
            if(turnLeft(prev) == next) commands.append('L');
            else commands.append('R');
        }
        commands.append('A');
        visited[mx][my] = true;
        visited[nx][ny] = true;

        simulation(new Loc(nx, ny), origin, moveCount + 2,
                commandCnt + turnCount + 1, next, originDir, visited, commands);

        visited[mx][my] = false;
        visited[nx][ny] = false;
        commands.setLength(commandLength);
    }

    static int turnLeft(int dir) {
        return (dir + 3) % 4;
    }

    static int turnRight(int dir) {
        return (dir + 1) % 4;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= h || y >= w;
    }

    static void findStartLoc() {
        startLocCands = new ArrayList<>();
        for(int x = 0; x < h; x++) {
            for(int y = 0; y < w; y++) {
                if(board[x][y] == '#') startLocCands.add(new Loc(x, y));
            }
        }
    }
}
