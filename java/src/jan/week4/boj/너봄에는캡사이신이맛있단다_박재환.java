package jan.week4.boj;

import java.io.*;
import java.util.*;

public class 너봄에는캡사이신이맛있단다_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * [5, 2, 8] -> max - min = 6 만큼의 매운맛을 느낀다.
     * 목표는 모든 조합을 먹어보는 것 -> 중복은 허용하지 않는다.
     */
    static final long DIV = 1_000_000_007;

    static StringTokenizer st;
    static int n;
    static int[] arr;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        arr = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n; i++) arr[i] = Integer.parseInt(st.nextToken());
        Arrays.sort(arr);       // 오름차순 정렬
        System.out.println(getMaxPainScore());
    }

    static long getMaxPainScore() {
        /**
         * 배열을 정렬 상태로 만든다.
         *
         * 고통 지수 = max - min
         * -> 부분 집합의 모든 max - 부분 집합의 모든 min
         *
         * A[i] 에 대해서
         * A[i] 가 max 인 부분 집합의 개수
         * A[i] 가 min 인 부분 집합의 개수
         * => for 문으로 한 번에 계산 가능 O(N)
         *
         * A[i] 가 max 일 때
         * -> A[i] 가 반드시 부분 집합에 포함되어야 함
         * -> 0 ~ i-1 까지는 자유롭게 부분집합에 포함시킬수도, 안시킬수도 있음
         * -> i+1 ~ N 까지는 절대로 포함시킬 수 없음
         *
         * A[i] 가 min 일 때
         * -> A[i] 가 반드시 부분 집합에 포함되어야 함
         * -> 0 ~ i-1 까지는 절대로 포함시킬 수 없음
         * -> i+1 ~ N 까지는 자유롭게 부분집합에 포함시킬수도, 안시킬수도 있음
         */
        long max = 0;
        long min = 0;
        initPowArr(n);
        for(int i=0; i<n; i++) {
            // A[i] 결정
            max = (max + getMaxSubArrCount(arr[i], i))%DIV;
            min = (min + getMinSubArrCount(arr[i], i))%DIV;
        }

        return (max - min + DIV) % DIV;
    }

    static long[] pows;
    static void initPowArr(int n) {
        pows = new long[n+1];
        pows[0] = 1;
        pows[1] = 2;
        for(int i=2; i<n+1; i++) pows[i] = (pows[i-1] * 2)%DIV;
    }

    static long getMaxSubArrCount(int max, int idx) {
        return (pows[idx] * max)%DIV;
    }

    static long getMinSubArrCount(int min, int idx) {
        return (pows[n-idx-1] * min)%DIV;
    }
}

