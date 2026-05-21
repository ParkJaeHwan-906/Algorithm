import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());

        int[] a = new int[n];
        int[] b = new int[n];
        int[] aToB = new int[n - 1];
        int[] bToA = new int[n - 1];

        StringTokenizer st;
        for (int i = 0; i < n - 1; i++) {
            st = new StringTokenizer(br.readLine());
            a[i] = Integer.parseInt(st.nextToken());
            b[i] = Integer.parseInt(st.nextToken());
            aToB[i] = Integer.parseInt(st.nextToken());
            bToA[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine());
        a[n - 1] = Integer.parseInt(st.nextToken());
        b[n - 1] = Integer.parseInt(st.nextToken());

        int[][] dp = new int[n][2];
        dp[0][0] = a[0];
        dp[0][1] = b[0];

        for (int i = 0; i < n - 1; i++) {
            dp[i + 1][0] = Math.min(dp[i][0] + a[i + 1], dp[i][1] + bToA[i] + a[i + 1]);
            dp[i + 1][1] = Math.min(dp[i][1] + b[i + 1], dp[i][0] + aToB[i] + b[i + 1]);
        }

        System.out.println(Math.min(dp[n - 1][0], dp[n - 1][1]));
    }
}
