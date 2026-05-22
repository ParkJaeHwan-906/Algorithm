package may.week3.codetree;

import java.util.*;
import java.io.*;

public class 바이러스검사_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    /**
     * 식당의 개수 : n
     * 검사 팀장과 검사 팀원 존재
     * 검사 팀장과 팀원이 검사할 수 있는 고객 수가 다름
     *
     * 가게 별 팀장은 오직 1명, 팀원은 여러면 ( 가게 당 팀장 한 명은 필수, 팀원은 없을 수 있음 )
     */
    static StringTokenizer st;
    static int n;
    static int[] rest;
    static int leaderMax, followerMax;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());

        rest = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for (int i = 0; i < n; i++) {
            rest[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine().trim());
        leaderMax = Integer.parseInt(st.nextToken());
        followerMax = Integer.parseInt(st.nextToken());

        System.out.println(solution());
    }
    static long solution() {
        long result = 0L;

        for(int r : rest) {
            result += need(r);
        }

        return result;
    }
    static long need(int r) {
        long l = 1;
        long f = 0;

        r = Math.max(0, r - leaderMax);
        f += (r / followerMax + (r % followerMax == 0 ? 0 : 1));
        return l + f;
    }
}
