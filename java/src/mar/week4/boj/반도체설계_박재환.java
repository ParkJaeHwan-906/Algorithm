package mar.week4.boj;

import java.util.*;
import java.io.*;

public class 반도체설계_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n;
    static int[] arr;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());

        st = new StringTokenizer(br.readLine().trim());
        arr = new int[n];
        for(int i=0; i<n;) arr[i++] = Integer.parseInt(st.nextToken());

        System.out.println(solution());
    }
    static int solution() {
        int[] lis = new int[n];

        for(int i=0; i<n; i++) {
            lis[i] = 1;     // 자기자신
            for(int j=0; j<i; j++) {
                if(arr[i] > arr[j]) {
                    lis[i] = Math.max(lis[i], lis[j] + 1);
                }
            }
        }

        return arrMax(lis);
    }
    static int arrMax(int[] arr) {
        int max = -1;
        for(int i : arr) max = Math.max(i, max);
        return max;
    }
}
