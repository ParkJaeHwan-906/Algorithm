package sep.week3.jungol;

import java.util.*;
import java.io.*;

public class 용액_이분탐색_박재환 {
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
        for (int i = 0; i < n; i++) {
            arr[i] = Long.parseLong(st.nextToken());
        }

        long[] result = solution();
        System.out.printf("%d %d", result[0], result[1]);
    }

    static long[] solution() {
        long bestSum = Long.MAX_VALUE;
        long bestL = 0;
        long bestR = 0;

        for (int i = 0; i < n - 1; i++) {
            long target = -arr[i];
            int idx = lowerBound(i + 1, n, target);
            if (idx < n) {
                long absSum = Math.abs(arr[i] + arr[idx]);

                if (absSum < bestSum) {
                    bestSum = absSum;
                    bestL = arr[i];
                    bestR = arr[idx];
                }
            }

            if (idx - 1 > i) {
                long absSum = Math.abs(arr[i] + arr[idx - 1]);

                if (absSum < bestSum) {
                    bestSum = absSum;
                    bestL = arr[i];
                    bestR = arr[idx - 1];
                }
            }

            // 합이 0이면 최적이므로 종료
            if (bestSum == 0) {
                break;
            }
        }

        return new long[]{bestL, bestR};
    }

    /**
     * [left, right) 범위에서
     * target 이상인 첫 번째 위치를 반환
     */
    static int lowerBound(int left, int right, long target) {
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return left;
    }
}
