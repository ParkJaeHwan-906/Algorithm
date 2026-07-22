package jul.week3.codetree;

import java.util.*;
import java.io.*;

public class Sam의피자학교_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n, k;
    static int[] arr;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        arr = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n;) arr[i++] = Integer.parseInt(st.nextToken());

        System.out.println(solution());
    }

    static int solution() {
        int turn = 0;
        while(!satisfy()) {
            addFlour();
            roll();
            adjFlour();
            flatten();
            fold();
            adjFlour();
            flatten();
            ++turn;
        }
        return turn;
    }

    static boolean satisfy() {
        /**
         * 최솟값과 최댓값의 차이가 k 이하인지 확인
         *
         * O(N)
         */
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for(int i : arr) {
            max = Math.max(max, i);
            min = Math.min(min, i);
        }
        return max - min <= k;
    }

    static void addFlour() {
        /**
         * 밀가루가 가장 적은 위치에 밀가루를 + 1 한다.
         * 적은 칸이 여러 곳이면 모두 적용한다.
         *
         * O(2N)
         */
        int min = Integer.MAX_VALUE;
        for(int i = 0; i < n; i++) min = Math.min(min, arr[i]);
        for(int i = 0; i < n; i++) {
            if(arr[i] == min) arr[i]++;
        }
    }

    static void roll() {
        /**
         * 도우를 말아올린다.
         */
        board = new int[n][n];
        for(int i = 0; i < n; i++) board[0][i] = arr[i];     // 초기 상태는 하단에만 위치

        // 1. 가장 왼쪽 도우를 올린다.
        board[1][1] = board[0][0];
        board[0][0] = 0;

        // 2. 말기
        int s = 1;
        int h = 2;
        int w = 1;

        while(true) {
            if(s + w + h > n) break;        // 말았을 때 기존 범위를 벗어남

            int[][] temp = new int[w][h];   // 회전하면 w, h가 서로 바뀜
            // 회전
            for(int x = 0; x < h; x++) {
                for(int y = 0; y < w; y++) {
                    temp[w - 1 - y][x] = board[x][s + y];
                    board[x][s + y] = 0;
                }
            }

            s += w;
            for(int x = 0; x < w; x++) {
                for(int y = 0; y < h; y++) board[x + 1][s + y] = temp[x][y];        // 한 칸 위부터 쌓아야함 (x + 1)
            }
            int nh = w + 1;
            int nw = h;
            w = nw;
            h = nh;
        }
    }

    static final int[] dx = {0, 1};
    static final int[] dy = {1, 0};

    static void adjFlour() {
        /**
         * 인접한 도우끼리 밀가루 양을 조절
         *
         * change 배열에 변화량을 기록한 뒤 한꺼번에 반영한다.
         * v > 방향으로만 탐색 -> 중복 탐색 X
         */
        int[][] change = new int[n][n];

        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(board[x][y] == 0) continue;

                for(int d = 0; d < 2; d++) {
                    int nx = x + dx[d];
                    int ny = y + dy[d];

                    if(isNotBoard(nx, ny)) continue;
                    if(board[nx][ny] == 0) continue;

                    int diff = Math.abs(board[x][y] - board[nx][ny]);
                    int move = diff / 5;

                    if(move == 0) continue;

                    if(board[x][y] > board[nx][ny]) {
                        change[x][y] -= move;
                        change[nx][ny] += move;
                    } else {
                        change[x][y] += move;
                        change[nx][ny] -= move;
                    }
                }
            }
        }

        // 변화량을 동시에 반영
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(board[x][y] == 0) continue;

                board[x][y] += change[x][y];
            }
        }
    }

    static void flatten() {
        /**
         * 말아 올린 도우를 다시 일렬로 펼친다.
         *
         * 왼쪽 열부터 오른쪽 열까지 탐색하고,
         * 각 열에서는 아래에서 위 순서로 꺼낸다.
         */
        int idx = 0;
        for(int y = 0; y < n; y++) {
            for(int x = 0; x < n; x++) {
                if(board[x][y] == 0) continue;

                arr[idx++] = board[x][y];
            }
        }
    }

    static void fold() {
        /**
         * 일렬로 펼쳐진 도우를 두 번 접는다.
         *
         * 최종 크기:
         * 높이 4
         * 너비 n / 4
         */
        board = new int[n][n];

        int half = n / 2;
        // 첫 번째 접기
        for(int y = 0; y < half; y++) {
            board[0][y] = arr[half + y];
        }
        for(int y = 0; y < half; y++) {
            board[1][y] = arr[half - 1 - y];
        }

        // 두 번째 접기
        int quarter = n / 4;
        int[][] temp = new int[4][quarter];
        for(int x = 0; x < 2; x++) {
            for(int y = 0; y < quarter; y++) {
                temp[x][y] = board[x][quarter + y];
            }
        }

        // 180도 회전
        for(int x = 0; x < 2; x++) {
            for(int y = 0; y < quarter; y++) {
                temp[4 - 1 - x][quarter - 1 - y] = board[x][y];
            }
        }

        board = new int[n][n];

        for(int x = 0; x < 4; x++) {
            for(int y = 0; y < quarter; y++) {
                board[x][y] = temp[x][y];
            }
        }
    }

    static boolean isNotBoard(int x, int y) { return x < 0 || y < 0 || x >= n || y >= n; }
}
