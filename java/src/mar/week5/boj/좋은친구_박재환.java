package mar.week5.boj;

import java.util.*;
import java.io.*;

public class 좋은친구_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        solution();
        br.close();
    }
    static StringTokenizer st;
    static int n, k;
    static int[] arr;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        arr = new int[n];
        for (int i = 0; i < n; i++) {
            String line = br.readLine().trim();
            arr[i] = line.length();
        }
    }
    static void solution() {
        int[] cnt = new int[21];
        long answer = 0;

        for (int i = 0; i < n; i++) {
            if (i > k) {
                cnt[arr[i - k - 1]]--;
            }

            answer += cnt[arr[i]];
            cnt[arr[i]]++;
        }

        System.out.println(answer);
    }
}
