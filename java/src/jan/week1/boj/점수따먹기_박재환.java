package jan.week1;

import java.util.*;
import java.io.*;

public class 점수따먹기_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    static StringTokenizer st;
    static int n, m;
    static int[][] arr;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        arr = new int[n][m];
        for(int i=0; i<n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int j=0; j<m; j++) {
                arr[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        /**
         * 2차원 행렬에서의 최대 누적합을 구함
         * => 연속된 행, 연속된 열을 보장
         * => 행을 압축
         */
        System.out.println(solution());
    }

    static long solution() {
        long maxSubArrSum = Long.MIN_VALUE;
        for(int top=0; top<n; top++) {    // limit 행까지만 사용하여 압축
            int[] zipArr = new int[m];
            for(int bottom=top; bottom<n; bottom++) {
                for(int y=0; y<m; y++) {
                    zipArr[y] += arr[bottom][y];
                }
                maxSubArrSum = Math.max(maxSubArrSum, getSubArrSum(zipArr));
            }
        }

        return maxSubArrSum;
    }

    static long getSubArrSum(int[] subArr) {
        int localMax = subArr[0];
        int globalMax = localMax;
        for(int i=1; i<m; i++) {
            localMax = Math.max(subArr[i], localMax + subArr[i]);
            globalMax = Math.max(globalMax, localMax);
        }
        return globalMax;
    }
}
