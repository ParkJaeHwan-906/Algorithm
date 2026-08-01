package jun.week5.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 01:03:44
  AI 사용 여부: O 디버깅 때 사용.
 */
public class 격자숫자놀이_박서희 {
    static int r, c, k;

    static int answer = 0;
    static int maxR = 2;
    static int maxC = 2;
    static int[][] board = new int[100][100];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        r = Integer.parseInt(st.nextToken()) - 1;
        c = Integer.parseInt(st.nextToken()) - 1;
        k = Integer.parseInt(st.nextToken());

        for (int i = 0; i < 3; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 3; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        game();

        System.out.println(answer > 100 ? -1 : answer);
    }

    public static void game() {
        while (true) {
            if (board[r][c] == k) return;
            if (answer > 100) return;

            answer++;
            if (maxR >= maxC) {
                int nextC = 0;
                for (int i = 0; i <= maxR; i++) {
                    int[] count = new int[101];
                    for (int j = 0; j <= maxC; j++) {
                        if (board[i][j] == 0) continue;
                        count[board[i][j]]++;
                    }

                    ArrayList<int[]> list = new ArrayList<>();
                    for (int j = 1; j <= 100; j++) {
                        if (count[j] > 0) list.add(new int[]{j, count[j]});
                    }

                    list.sort((a, b) -> {
                        if (a[1] != b[1]) return Integer.compare(a[1], b[1]);
                        return Integer.compare(a[0], b[0]);
                    });

                    Arrays.fill(board[i], 0);

                    int jdx = 0;
                    for (int[] cc : list) {
                        if (jdx >= 100) break;
                        board[i][jdx++] = cc[0];
                        board[i][jdx++] = cc[1];
                    }
                    nextC = Math.max(nextC, jdx);
                }
                maxC = nextC - 1;

            } else {
                int nextR = 0;
                for (int j = 0; j <= maxC; j++) {
                    int[] count = new int[101];
                    for (int i = 0; i <= maxR; i++) {
                        if (board[i][j] == 0) continue;
                        count[board[i][j]]++;
                        board[i][j] = 0;
                    }
                    ArrayList<int[]> list = new ArrayList<>();
                    for (int i = 1; i <= 100; i++) {
                        if (count[i] > 0) list.add(new int[]{i, count[i]});
                    }

                    list.sort((a, b) -> {
                        if (a[1] != b[1]) return Integer.compare(a[1], b[1]);
                        return Integer.compare(a[0], b[0]);
                    });
                    int idx = 0;
                    for (int[] cc : list) {
                        if (idx >= 100) break;
                        board[idx++][j] = cc[0];
                        board[idx++][j] = cc[1];
                    }
                    nextR = Math.max(idx, nextR);
                }
                maxR = nextR - 1;
            }
        }
    }
}
