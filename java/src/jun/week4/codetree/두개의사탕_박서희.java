package jun.week4.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 01:25:26
  AI 사용 여부: O 사탕이 부딪치는 로직과 무한루프를 돌지 않게 하는 부분을 생각해내기 어려웠다.
                무한루프를 돌지 않게 하기 위해 visited[red.x][red.y][blue.x][blue.y]를 사용했고,
                사탕이 부딪치면 멈추는 로직을 구현하기 위해서 각 캔디가 움직인 거리를 구했다.
 */
public class 두개의사탕_박서희 {
    static int N, M;
    static int[][] board;
    static int[][][][] visited;

    static int endX, endY;
    static Candy blue;
    static Candy red;

    static int answer = Integer.MAX_VALUE;
    static final int[] dx = {0, -1, 0, 1};
    static final int[] dy = {1, 0, -1, 0};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        visited = new int[N][M][N][M];

        board = new int[N][M];
        for (int i = 0; i < N; i++) {
            String s = br.readLine();
            for (int j = 0; j < M; j++) {
                board[i][j] = s.charAt(j);
                if (board[i][j] == 'O') {
                    endX = i;
                    endY = j;
                }
                if (board[i][j] == 'B') {
                    blue = new Candy(i, j);
                    board[i][j] = '.';
                }
                if (board[i][j] == 'R') {
                    red = new Candy(i, j);
                    board[i][j] = '.';
                }
            }
        }

        simulate(0);

        System.out.println(answer == Integer.MAX_VALUE ? -1 : answer);
    }

    private static void simulate(int depth) {
        if (depth > 10 || depth >= answer) {
            return;
        }

        if (blue.x == endX && blue.y == endY) {
            return;
        }

        if (red.x == endX && red.y == endY) {
            answer = Math.min(answer, depth);
            return;
        }

        for (int d = 0; d < 4; d++) {
            int originRedX = red.x, originRedY = red.y;
            int originBlueX = blue.x, originBlueY = blue.y;

            int redCandyMove = moveCandy(red, d);
            int blueCandyMove = moveCandy(blue, d);

            if (red.x == blue.x && red.y == blue.y && board[red.x][red.y] != 'O') {
                if (redCandyMove > blueCandyMove) {
                    red.x -= dx[d];
                    red.y -= dy[d];
                } else {
                    blue.x -= dx[d];
                    blue.y -= dy[d];
                }
            }

            if (visited[red.x][red.y][blue.x][blue.y] == 0) {
                visited[red.x][red.y][blue.x][blue.y] = 1;
                simulate(depth + 1);
                visited[red.x][red.y][blue.x][blue.y] = 0;
            }

            red.x = originRedX;
            red.y = originRedY;
            blue.x = originBlueX;
            blue.y = originBlueY;
        }
    }

    private static int moveCandy(Candy candy, int d) {
        int distance = 0;

        while (true) {
            int nx = candy.x + dx[d];
            int ny = candy.y + dy[d];

            if (!inRange(nx, ny) || board[nx][ny] == '#') break;

            candy.x = nx;
            candy.y = ny;
            distance++;

            if (board[nx][ny] == 'O') break;
        }

        return distance;
    }

    private static boolean inRange(int x, int y) {
        return 0 <= x && x < N && 0 <= y && y < M;
    }

    static class Candy {
        int x, y;

        public Candy(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
