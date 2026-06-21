package jun.week3.ngv;

import java.util.*;
import java.io.*;

public class 효도음식_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n;
    static int[] items;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        n = Integer.parseInt(br.readLine().trim());
        items = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n; i++) items[i] = Integer.parseInt(st.nextToken());

        System.out.println(solution());
    }
    static int[] maxSum;
    static int[] maxSumIncluded;
    static int[] ReverseMaxSum;
    static int[] ReverseMaxSumIncluded;
    static int solution() {
        maxSum = new int[n];
        ReverseMaxSum = new int[n];

        makeMaxSum();
//        System.out.println(Arrays.toString(maxSum));
        makeReverseMaxSum();
//        System.out.println(Arrays.toString(ReverseMaxSum));

        int max = Integer.MIN_VALUE;
        for(int i = 0; i < n - 2; i++) {
            int sum = maxSum[i] + ReverseMaxSum[i + 2];
            max = Math.max(max, sum);
        }

        return max;
    }

    /**
     * 비교 대상
     * - 현재위치까지의 누적합
     * - 현재 위치의 단일 값
     * - 현재까지의 최대 누적합
     */
    static void makeMaxSum() {
        maxSum = new int[n];
        maxSumIncluded = new int[n];

        maxSum[0] = items[0];
        maxSumIncluded[0] = items[0];

        for(int i = 1; i < n; i++) {
            maxSumIncluded[i] = Math.max(maxSumIncluded[i - 1] + items[i], items[i]);
            maxSum[i] = Math.max(maxSum[i - 1], maxSumIncluded[i]);
        }
    }

    static void makeReverseMaxSum() {
        ReverseMaxSum = new int[n];
        ReverseMaxSumIncluded = new int[n];

        ReverseMaxSum[n-1] = items[n-1];
        ReverseMaxSumIncluded[n-1] = items[n-1];
        for(int i = n - 2; i >= 0; i--) {
            ReverseMaxSumIncluded[i] = Math.max(ReverseMaxSumIncluded[i + 1] + items[i], items[i]);
            ReverseMaxSum[i] = Math.max(ReverseMaxSum[i + 1], ReverseMaxSumIncluded[i]);
        }
    }
}
