package mar.week3.boj;

import java.util.*;
import java.io.*;

public class 최종순위_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    /**
     * n 개의 팀 (1 ~ N)
     * 작년에 비해서 상대적인 순위가 바뀐 팀의 순위만 알려준다.
     * => 작년에 팀 13이 팀6보다 순위가 높았는데, 올해 팀 6이 13보다 순위가 높다면 (6, 13)
     */
    static StringTokenizer st;
    static void init() throws IOException {
        int t = Integer.parseInt(br.readLine().trim());
        while(t-- > 0) {
            int n = Integer.parseInt(br.readLine().trim());
            int[] rank = new int[n+1];
            st = new StringTokenizer(br.readLine().trim());
            for(int i=1; i<n+1;) rank[i++] = Integer.parseInt(st.nextToken());
            getInEdge(n, rank);

            int q = Integer.parseInt(br.readLine().trim());
            while(q-- > 0) {
                st = new StringTokenizer(br.readLine().trim());
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                swap(a, b);
            }

            System.out.println(getRank(n));
        }
    }
    static boolean[][] connections;
    static int[] inEdge;
    static void getInEdge(int n, int[] rank) {
        connections = new boolean[n+1][n+1];
        inEdge = new int[n+1];

        for(int i=1; i<n+1; i++) {
            int h = rank[i];
            for(int j=i+1; j<n+1; j++) {
                int l = rank[j];
                if(!connections[h][l]) {
                    connections[h][l] = true;
                    inEdge[l]++;
                }
            }
        }
    }
    static void swap(int a, int b) {
        if (connections[a][b]) {
            connections[a][b] = false;
            inEdge[b]--;
            connections[b][a] = true;
            inEdge[a]++;
        } else {
            connections[b][a] = false;
            inEdge[a]--;
            connections[a][b] = true;
            inEdge[b]++;
        }
    }
    static String getRank(int n) {
        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 1; i <= n; i++) {
            if (inEdge[i] == 0) q.offer(i);
        }

        StringBuilder order = new StringBuilder();
        boolean ambiguous = false;
        int count = 0;

        while (!q.isEmpty()) {
            if (q.size() > 1) {
                ambiguous = true;
            }

            int cur = q.poll();
            order.append(cur).append(' ');
            count++;

            for (int i = 1; i <= n; i++) {
                if (connections[cur][i]) {
                    if (--inEdge[i] == 0) q.offer(i);
                }
            }
        }

        if (count < n) return "IMPOSSIBLE";
        if (ambiguous) return "?";
        return order.toString().trim();
    }
}
