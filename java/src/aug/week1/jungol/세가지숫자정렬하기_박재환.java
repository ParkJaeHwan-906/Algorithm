package aug.week1.jungol;

import java.io.*;

public class 세가지숫자정렬하기_박재환 {
    static int n;
    static int[] arr;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static void init(BufferedReader br) throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        arr = new int[n];
        for(int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(br.readLine().trim());
        }
        System.out.println(solution());
    }

    static int solution() {
        int[] cnt = new int[4];
        for(int v : arr) cnt[v]++;      // 각 원소의 개수

        int[][] c = new int[4][4];
        for(int p = 0; p < n; p++) {
            int region = (p < cnt[1]) ? 1 : (p < cnt[1] + cnt[2]) ? 2 : 3;      // 각 원소들의 자리 prefix
            if(region != arr[p]) c[region][arr[p]]++;
        }

        int ans = 0;

        // swap
        for(int i = 1; i <= 3; i++) {
            for(int j = i + 1; j <= 3; j++) {
                int pair = Math.min(c[i][j], c[j][i]);
                ans += pair;
                c[i][j] -= pair;
                c[j][i] -= pair;
            }
        }

        // swap
        int rest = 0;
        for(int i = 1; i <= 3; i++)
            for(int j = 1; j <= 3; j++) rest += c[i][j];

        ans += rest / 3 * 2;

        return ans;
    }
}
