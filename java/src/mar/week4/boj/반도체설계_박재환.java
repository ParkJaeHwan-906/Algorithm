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

//        System.out.println(solution());
        System.out.println(solution2());
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
    static int solution2() {
        List<Integer> lis = new ArrayList<>();
        for(int i : arr) {
            int insertId = findInsertId(i, lis);
            if(insertId == lis.size()) lis.add(i);
            else lis.set(insertId, i);
        }
        return lis.size();
    }
    static int findInsertId(int num, List<Integer> list) {
        /**
         * num 보다 크거나 같은 가장 첫 번째 위치
         */
        int l = 0, r = list.size();
        while(l < r) {
            int mid = l + (r - l)/2;
            if(list.get(mid) >= num) r = mid;
            else l = mid + 1;
        }
        return l;
    }
}
