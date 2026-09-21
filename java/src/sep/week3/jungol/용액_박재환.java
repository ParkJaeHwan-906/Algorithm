package sep.week3.jungol;

import java.util.*;
import java.io.*;

public class 용액_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int n;
    static long[] arr;
    static void init(BufferedReader br) throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        arr = new long[n];
        StringTokenizer st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n; i++) {
            arr[i] = Long.parseLong(st.nextToken());
        }
        long[] result = solution();
        System.out.printf("%d %d", result[0], result[1]);
    }

    static long[] solution() {
        // 오름차순으로 주어짐
        int l = 0;
        int r = n - 1;

        long bestSum = Long.MAX_VALUE;
        long bestL = Long.MAX_VALUE;
        long bestR = Long.MAX_VALUE;
        while(l < r) {
            long sum = arr[l] + arr[r];
            if(bestSum > Math.abs(sum)) {
                bestSum = Math.abs(sum);
                bestL = arr[l];
                bestR = arr[r];
                if(bestSum == 0) {
                    break;
                }
            }

            if(sum > 0) {
                r--;
            } else {
                l++;
            }
        }

        return new long[] {bestL, bestR};
    }
}
