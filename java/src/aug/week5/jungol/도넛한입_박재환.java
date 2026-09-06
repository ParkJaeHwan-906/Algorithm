package aug.week5.jungol;

import java.util.*;
import java.io.*;

public class 도넛한입_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int n;
    static int[] arr;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        n = Integer.parseInt(br.readLine().trim());
        arr = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());;
        }
        System.out.println(solution());
    }

    static long solution() {
        long[] dp = new long[n];
        dp[0] = arr[0];
        long max = dp[0];
        for(int i = 1; i < n; i++) {
            dp[i] = Math.max(dp[i - 1] + arr[i], arr[i]);
            max = Math.max(max, dp[i]);
        }

        if(n == 1) {
            return max;
        }

        long[] leftMax = new long[n];
        long sum = arr[0];
        leftMax[0] = arr[0];
        for (int i = 1; i < n; i++) {
            sum += arr[i];
            leftMax[i] = Math.max(leftMax[i - 1], sum);
        }

        long[] rightMax = new long[n];
        sum = arr[n - 1];
        rightMax[n - 1] = arr[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            sum += arr[i];
            rightMax[i] = Math.max(rightMax[i + 1], sum);
        }

        long circularMax = Long.MIN_VALUE;
        for (int i = 0; i < n - 1; i++) {
            circularMax = Math.max(
                    circularMax,
                    leftMax[i] + rightMax[i + 1]
            );
        }

        return Math.max(circularMax, max);
    }
}
