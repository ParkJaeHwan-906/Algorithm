package jun.week1.ngv;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:37:46
 * AI 사용 여부 O
 * => LCS 구하는 과정에서 일부 TC 시간초과 발생 -> 경로 생성 후 LCS 계산 방식을 누적 DP 로 바꿈
 */
public class 효도여행_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n, m;
    static char[] like;
    static List<Integer>[] connections;
    static char[][] connectionsAlpha;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        like = new char[m];
        String line = br.readLine().trim();
        for(int i = 0; i < m;) like[i] = line.charAt(i++);

        connections = new List[n + 1];
        connectionsAlpha = new char[n + 1][n + 1];
        for(int i = 0; i < n + 1; i++) connections[i] = new ArrayList<>();

        for(int i = 0; i < n - 1; i ++) {
            st = new StringTokenizer(br.readLine().trim());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            char c = st.nextToken().charAt(0);

            connectionsAlpha[u][v] = c;
            connectionsAlpha[v][u] = c;
            connections[u].add(v);
            connections[v].add(u);
        }

        System.out.println(solution());
    }

    static int bestLCS;
    static int solution() {
        bestLCS = 0;

        search(-1, 1, new int[m + 1]);

        return bestLCS;
    }

    static void search(int prev, int cur, int[] dp) {
//        lcs(route);       // 매번 LCS 계산 시 시간 초과

//        if(prev != -1 && connections[cur].size() == 1) {      // 리프노드에서만 LCS 계산하는 방법 : TC2 만 시간초과
//            lcs(route);
//            return;
//        }

        bestLCS = Math.max(bestLCS, dp[m]);

        for(int next : connections[cur]) {
            if(next == prev) continue;      // 사이클 방지

            char c = connectionsAlpha[cur][next];
            int[] nextDp = new int[m + 1];
            /**
             * 하단 LCS 함수를 분리
             */
            for(int j = 1; j <= m; j++) {
                if(c == like[j - 1]) {
                    nextDp[j] = dp[j - 1] + 1;
                } else {
                    nextDp[j] = Math.max(dp[j], nextDp[j - 1]);
                }
            }

            search(cur, next, nextDp);
        }

    }

//    static void lcs(String state) {
//        char[] temp = new char[state.length()];
//        for(int i = 0; i < temp.length; i++) temp[i] = state.charAt(i);
//
//        char[] a = like.length > temp.length ? like : temp;
//        char[] b = like.length > temp.length ? temp : like;
//
//        int[][] lcs = new int[b.length + 1][a.length + 1];
//        for(int i = 1; i < b.length + 1; i++) {
//            for(int j = 1; j < a.length + 1; j++) {
//                if(a[j - 1] == b[i - 1]) {
//                    lcs[i][j] = lcs[i - 1][j - 1] + 1;
//                } else {
//                    lcs[i][j] = Math.max(lcs[i - 1][j], lcs[i][j - 1]);
//                }
//            }
//        }
//
//        bestLCS = Math.max(bestLCS, lcs[b.length][a.length]);
//    }
}

