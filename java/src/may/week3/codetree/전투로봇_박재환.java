package may.week3.codetree;

import java.util.*;
import java.io.*;

public class 전투로봇_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    static StringTokenizer st;
    static int n;
    static int[][] board;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());

        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
            }
        }

        System.out.println(solution());
    }

    /**
     *     n x n 격자에 m 개의 몬스터와 하나의 전투로봇이 존재
     *         - 한 탄에는 한 개의 몬스터만 존재
     *
     *     전투로봇과 몬스터 모두 자연수인 레벨을 갖는다.
     *         - 초기 전투로봇의 레벨은 2
     *             - 전투로봇은 1초에 상하좌우 한 칸씩 이동
     *
     *     전투로봇은 자신의 레벨보다 큰 몬스터가 있는 칸은 지나칠 수 없다.
     *     나머지 칸은 모두 지날 수 있다.
     *     전투로봇은 자신의 레벨보다 낮은 몬스터만 없앨 수 있다.
     *         - 레벨이 같은 경우 없앨 수 는 없지만, 지나칠 수는 있음
     *
     *     [이동규칙]
     *     - 없앨 수 있는 몬스터가 있다면 해당 몬스터를 없애러 간다.
     *     - 없앨 수 있는 몬스터가 하나 이상이라면, 거리가 가장 가까운 몬스터를 없애러 간다.
     *         - 거리 : 멘헤튼 거리
     *         - 거리가 같다면 가장 왼쪽 위에 있는 몬스터가 우선 순위를 갖는다.
     *     - 없앨 수 있는 몬스터가 없다면 일을 끝낸다.
     *
     *     전투로봇은 본인의 레벨과 같은 수의 몬스터를 없앨 때마다 레벨이 상승합니다.
     */
    static class BattleRobot {
        int x, y;
        int level;
        int remove;

        BattleRobot(int x, int y, int level) {
            this.x = x;
            this.y = y;
            this.level = level;
            this.remove = 0;
        }

        void remove() {
            if(++remove == level) {
                remove = 0;
                level++;
            }
        }
    }

    static BattleRobot battleRobot;
    static int solution() {
        setInit();

        int time = 0;
        while (true) {
            int[] next = nextMonster();

            if(next == null) break;

            // {nextX, nextY, nextD}
//            System.out.printf("다음대상 : { x : %d, y : %d, dist : %d}\n", next[0], next[1], next[2]);
            time += next[2];        // 대상이 있는 칸으로 이동
            battleRobot.x = next[0];
            battleRobot.y = next[1];
            board[next[0]][next[1]] = 0;
            battleRobot.remove();
        }

        return time;
    }

    static void setInit() {
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(board[x][y] == 9) {
                    battleRobot = new BattleRobot(x, y, 2);
                    board[x][y] = 0;
                    return;
                }
            }
        }
    }

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static int[] nextMonster() {
        /**
         * 무조건 멘헤튼 거리가 될 수 없음
         * -> BFS 로 가장 가까운 좌상단 위치를 찾아야함
         */
        // 다음 만나는 대상 몬스터
        int nextD = Integer.MAX_VALUE;
        int nextX = Integer.MAX_VALUE;
        int nextY = Integer.MAX_VALUE;

        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];

        q.offer(new int[] {battleRobot.x, battleRobot.y, 0});
        visited[battleRobot.x][battleRobot.y] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int dist = cur[2];

            for(int dir = 0; dir < 4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];

                if(isNotBoard(nx, ny) || visited[nx][ny]) continue;

                if(board[nx][ny] == 0 || board[nx][ny] == battleRobot.level) {
                    // 빈 칸인 경우 바로 이동 가능
                    visited[nx][ny] = true;
                    q.offer(new int[] {nx, ny, dist + 1});
                } else if(board[nx][ny] < battleRobot.level) {
                    // 전투로봇보다 더 작은 레벨인 경우
                    int nDist = dist + 1;

                    if(nextD > nDist || (nextD == nDist && (nextX > nx || (nextX == nx && nextY > ny)))) {
                        nextD = nDist;
                        nextX = nx;
                        nextY = ny;
                    }
                }
            }
        }

        return nextD == Integer.MAX_VALUE ? null : new int[] {nextX, nextY, nextD};
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}
