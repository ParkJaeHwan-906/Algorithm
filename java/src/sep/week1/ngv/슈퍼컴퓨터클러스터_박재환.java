package sep.week1.ngv;

import java.util.*;
import java.io.*;

public class 슈퍼컴퓨터클러스터_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int n;
    static long b;
    static int[] arr;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());           // 컴퓨터 대수
        b = Long.parseLong(st.nextToken());           // 예산

        arr = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        System.out.print(solution());
    }

    static int solution() {
        int l = Integer.MAX_VALUE, r = Integer.MIN_VALUE;
        for(int i = 0; i < n; i++) {
            l = Math.min(l, arr[i]);
            r = Math.max(r, arr[i]);
        }
        r += (int) Math.sqrt(b);        // 성능이 가장 높은 컴퓨터를 최대치로 올리는 가용
        while(l < r) {
            int mid = l + (r - l + 1) / 2;
            if(isPossible(mid)) {       // 성능을 더 높일 수 있음
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        return l;
    }

    static boolean isPossible(int target) {
        long totalCost = 0;
        for(int i : arr) {
            if(i < target) {
                long diff = (long) target - i;
                totalCost += diff * diff;
                if(totalCost > b) {
                    return false;
                }
            }
        }
        return true;
    }
}
