package jun.week5.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:14:29
 * AI 사용 여부 X
 */
public class 동전프로모션_박재환 {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));;
        init(br);
        br.close();
    }

    static final int A = 1;
    static final int B = 2;

    static class Coin {
        int type;
        int value;

        Coin(int type, int value) {
            this.type = type;
            this.value = value;
        }
    }

    static int n, m;
    static Coin[] coins;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        coins = new Coin[n];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            String type = st.nextToken();
            int value = Integer.parseInt(st.nextToken());
            if(type.equals("A")) {
                coins[i] = new Coin(A,value);
            } else if(type.equals("B")) {
                coins[i] = new Coin(B,value);
            }
        }

        System.out.println(solution());
    }

    static final int INF = Integer.MAX_VALUE;

    static int solution() {
        int[] dp = new int[m + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;
        for(Coin c : coins) {
            if(c.type == A) {       // 여러번 사용 가능
                for(int i = c.value; i <= m; i++) {
                    if(dp[i - c.value] == INF) continue;
                    dp[i] = Math.min(dp[i], dp[i - c.value] + 1);
                }
            } else if(c.type == B) {    // 한 번만 사용 가능
                for(int i = m; i >= c.value; i--) {
                    if(dp[i - c.value] == INF) continue;
                    dp[i] = Math.min(dp[i], dp[i - c.value] + 1);
                }
            }
        }

        return dp[m] == INF ? -1 : dp[m];
    }
}
