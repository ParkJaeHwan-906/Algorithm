package aug.week1.codetree;

import java.util.*;
import java.io.*;

public class 동전이부족해_박재환 {
    /**
     * 거스름돈 M원을 줄 때, 되도록 여러 종류의 동전을 균형있게 사용하고 싶다.
     *
     * N 개 종류의 동전이 있다.
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int n, m;
    static int[] coins;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        coins = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n;) coins[i++] = Integer.parseInt(st.nextToken());

        System.out.println(solution());
    }

    static int solution() {
        int left = 0;
        int right = m;

        if (!canMake(right)) return -1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (canMake(mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        return left;
    }

    static boolean canMake(int limit) {
        int[] dp = new int[m + 1];
        Arrays.fill(dp, -1);
        dp[0] = 0;

        for (int coin : coins) {
            for (int amount = 0; amount <= m; amount++) {
                if (dp[amount] >= 0) {
                    dp[amount] = limit;
                } else if (amount < coin || dp[amount - coin] <= 0) {
                    dp[amount] = -1;
                } else {
                    dp[amount] = dp[amount - coin] - 1;
                }
            }
        }

        return dp[m] >= 0;
    }
}
