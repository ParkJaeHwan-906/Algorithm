package mar.week2.boj;

import java.util.*;
import java.io.*;

public class 상어중학교_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    /**
     * N x N 격자
     *
     * 블록
     * - 검은색 블록 : -1
     * - 무지개 : 0
     * - 일반 : 1 ~ M
     *
     * 상하좌우 인접
     *
     * 블록 그룹 : 연결된 블록 집합 (일반 블록이 적어도 하나 포함, 일반블록 색은 모두 같아야함, 검은색은 포함 X)
     * 그룹에 속한 블록 개수는 2보다 크거나 같아야한다.
     * 그룹 기준 : 일반 블록 중, 행 - 열 번호가 가장 작은 블록
     *
     * 1. 크기가 가장 큰 블록 찾기
     * - 같다면 무지개 블록 수가 많은 그룹
     * - 같다면 기준 블록 행 - 열 큰 순
     * 2. 1에서 찾은 블록 그룹 제거 (블록 수 ** 2 점수 획득)
     * 3. 중력 작용
     * 4. 격자 반시계 회전
     * 5. 다시 중력
     *
     * 중력 : 검은 블록을 제외한 모든 블록이 행의 번호가 큰 칸으로 이동
     */
    static final int BLACK = -1;
    static final int RAINBOW = 0;
    static final int EMPTY = -2;

    static StringTokenizer st;
    static int n, m;
    static int[][] board;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        board = new int[n][n];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        autoPlay();
        System.out.println(score);
    }
    static int score;
    static boolean play;
    static void autoPlay() {
        score = 0;
        play = true;

        while(true) {
            findLargestGroup();
            if(!play) break;
            applyGravity();
            rotateCounterClockWise();;
            applyGravity();
        }
    }
    static class Group {
        int standardColor;
        int rainbowCnt;
        int normalCnt;
        int x, y;
        List<int[]> points;

        Group(int standardColor, int x, int y) {
            this.standardColor = standardColor;
            this.x = x;
            this.y = y;

            this.points = new ArrayList<>();
            this.rainbowCnt = 0;
            this.normalCnt = 1;
        }

        int getSize() {
            return this.normalCnt + this.rainbowCnt;
        }
    }
    static Group largestGroup;
    static void findLargestGroup() {
        /**
         * 일반 블록이 적어도 하나 있어야한다.
         * 포함된 일반 블록 색은 모두 같아야한다.
         * 검은색 블록은 포함할 수 없다.
         */
        largestGroup = null;
        boolean[][] checked = new boolean[n][n];
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(board[x][y] > 0 && !checked[x][y]) {     // 일반 블록이고, 아직 체크되지 않음
                    findGroup(x, y, checked);
                }
            }
        }
        if(largestGroup == null) {
            play = false;
            return;
        }
        getScore();
    }
    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static void findGroup(int x, int y, boolean[][] checked) {
        Queue<int[]> q = new ArrayDeque<>();
        List<int[]> rainbow = new ArrayList<>();

        int standardColor = board[x][y];
        Group group = new Group(standardColor, x, y);

        q.offer(new int[] {x, y});
        checked[x][y] = true;
        group.points.add(new int[] {x, y});

        while(!q.isEmpty()) {
            int[] cur = q.poll();

            for(int dir=0; dir<4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                if(checked[nx][ny]) continue;
                if(board[nx][ny] == BLACK || board[nx][ny] == EMPTY) continue;
                if(board[nx][ny] == RAINBOW) {        // 무지개 블록
                    group.rainbowCnt++;
                    checked[nx][ny] = true;
                    q.offer(new int[] {nx, ny});
                    group.points.add(new int[] { nx, ny});
                    rainbow.add(new int[]{nx,ny});
                } else {                        // 일반 블록
                    if(board[nx][ny] != standardColor) continue;
                    group.normalCnt++;
                    checked[nx][ny] = true;
                    q.offer(new int[] {nx, ny});
                    group.points.add(new int[] {nx, ny});
                    // 기준 블록 갱신
                    if(group.x > nx) {
                        group.x = nx;
                        group.y = ny;
                    } else if(group.x == nx && group.y > ny) {
                        group.y = ny;
                    }
                }
            }
        }
        // 무지개 블록은 공용 -> 늘 visited 초기화
        for(int[] r : rainbow) {
            checked[r[0]][r[1]] = false;
        }

        if(group.getSize() < 2) return;

        if(largestGroup == null) largestGroup = group;
        else if((largestGroup.getSize() < group.getSize())
        || ((largestGroup.getSize() == group.getSize()) && (largestGroup.rainbowCnt < group.rainbowCnt))
        || ((largestGroup.getSize() == group.getSize()) && (largestGroup.rainbowCnt == group.rainbowCnt) && (largestGroup.x < group.x))
        || ((largestGroup.getSize() == group.getSize()) && (largestGroup.rainbowCnt == group.rainbowCnt) && (largestGroup.x == group.x) && (largestGroup.y < group.y))
        ) largestGroup = group;
    }
    static void getScore() {
        score += (largestGroup.getSize() * largestGroup.getSize());
        for(int[] point : largestGroup.points) {
            board[point[0]][point[1]] = EMPTY;
        }
    }
    static void applyGravity() {
        for(int y = 0; y < n; y++) {
            for(int x = n-2; x >= 0; x--) {
                if(board[x][y] == BLACK || board[x][y] == EMPTY) continue;

                int nx = x;
                while(true) {
                    if(nx + 1 >= n) break;
                    if(board[nx+1][y] != EMPTY) break;

                    board[nx+1][y] = board[nx][y];
                    board[nx][y] = EMPTY;
                    nx++;
                }
            }
        }
    }
    static void rotateCounterClockWise() {
        int[][] temp = new int[n][n];
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) temp[n-y-1][x] = board[x][y];
        }
        board = temp;
    }
}
