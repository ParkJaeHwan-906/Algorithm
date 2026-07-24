package jul.week3.codetree;

import java.util.*;
import java.io.*;

public class 도로보수로봇_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int n, k;
    static int[] holes;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // n 개의 구멍
        k = Integer.parseInt(st.nextToken());       // k 개의 패치
        holes = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n; i++) holes[i] = Integer.parseInt(st.nextToken());

        System.out.println(solution());
    }

    static int solution() {
        int l = 1, r = holes[n - 1] - holes[0] + 1;
        while(l < r) {
            int mid = l + (r - l) / 2;
            if(isPossible(mid)) r = mid;
            else l = mid + 1;
        }
        return r;
    }

    static boolean isPossible(int l) {
        int needs = 1;
        int lastId = 0;
        for(int i = 1; i < n; i++) {
            int diff = holes[i] - holes[lastId] + 1;
            if(diff > l) {
                if(++needs > k) return false;
                lastId = i;
            }
        }
        return needs <= k;
    }
}
