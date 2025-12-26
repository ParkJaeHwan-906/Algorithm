package dec_4.ngv;

import java.util.*;
import java.io.*;

public class 효도음식_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * 요리를 2개 만들려고 한다.
     * n개의 재료가 있다.
     *
     * 재료는 1~n번
     * - 요리는 연속한 재료로만 만들 수 있다. 이때 최소 1개 이상의 재료를 선택해야한다.
     * - 서로 다른 요리에 사용되는 재료끼리 겹치거나, 인접해도 안된다.
     */
    static StringTokenizer st;
    static int n;
    static int[] arr;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        st = new StringTokenizer(br.readLine().trim());
        arr = new int[n];
        for(int i=0; i<n; i++) arr[i] = Integer.parseInt(st.nextToken());
        System.out.println(getMaxSubArr());
    }

    static int getMaxSubArr() {
        int[] left = getLeftSubArr();
        int[] right = getRightSubArr();
//        System.out.println(Arrays.toString(left));
//        System.out.println(Arrays.toString(right));
        int maxCombi = Integer.MIN_VALUE;
        for(int i=0; i<n-2; i++) {
            maxCombi = Math.max(maxCombi, (left[i] + right[i+2]));
        }
        return maxCombi;
    }

    static int[] getLeftSubArr() {
        int[] leftSubArr = new int[n];
        int localMax =  arr[0];
        leftSubArr[0] = localMax;
        for(int i=1; i<n; i++) {
            localMax = Math.max(arr[i], arr[i] + localMax);
            leftSubArr[i] = localMax;
        }
        return leftSubArr;
    }

    static int[] getRightSubArr() {
        int[] rightSubArr = new int[n];
        int localMax =  arr[n-1];
        int globalMax = localMax;
        rightSubArr[n-1] = globalMax;
        for(int i=n-2; i>-1; i--) {
            localMax = Math.max(arr[i], arr[i] + localMax);
            globalMax = Math.max(globalMax, localMax);
            rightSubArr[i] = globalMax;
        }
        return rightSubArr;
    }
}
