package mar.week3.boj;

import java.util.*;
import java.io.*;

public class 함께블록쌓기_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static final int MOD = 10007;

    static StringTokenizer st;
    static int n, m, h;
    static int[][] blocks;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        h = Integer.parseInt(st.nextToken());

        blocks = new int[n+1][m];
        for(int i=1; i<n+1; i++) {
            Arrays.fill(blocks[i], -1);
            st = new StringTokenizer(br.readLine().trim());
            for(int j=0; j<m; j++) {
                if(st.hasMoreTokens()) blocks[i][j] = Integer.parseInt(st.nextToken());
                else break;
            }
        }

        System.out.println(solution());
    }
    static int solution() {
        /**
         * 각 학생의 선택지
         * - 선택 안함
         * - 1번 블록
         * - 2번 블록
         * ...
         */
        int[][] dp = new int[n+1][h+1];           // [i][j] : 앞에서 i명의 학생까지 고려했을 때, j를 만들 수 있는 경우의 수

        dp[0][0] = 1;       // 학생을 한명도 보지 않았을 때, 아무것도 선택을 안하는 경우
        for(int i=1; i<n+1; i++) {
            for(int v=0; v<h+1; v++) {
                dp[i][v] = (dp[i][v] + dp[i-1][v])%MOD;         // i 번째 학생이 아무것도 선택하지 않는 경우

                // 현재 가지고 있는 블록을 사용
                for(int j : blocks[i]) {
                    if(j == -1) break;
                    if(v >= j) dp[i][v] = (dp[i][v] + dp[i-1][v-j])%MOD;
                }
            }
        }

        return dp[n][h];
    }
}
