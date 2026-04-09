package apr.week1.boj;

import java.util.*;
import java.io.*;

public class 선수과목_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m;
    static List<Integer>[] connections;
    static int[] inEdges;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        connections = new List[n + 1];
        inEdges = new int[n + 1];
        for(int i = 0; i < n + 1; i++) connections[i] = new ArrayList<>();

        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int prev = Integer.parseInt(st.nextToken());
            int next = Integer.parseInt(st.nextToken());
            connections[prev].add(next);
            inEdges[next]++;
        }

        StringBuilder sb = new StringBuilder();
        int[] answer = topologySort();
        for(int i = 1; i < n + 1; i++) sb.append(answer[i]).append(' ');
        System.out.println(sb);
    }

    static int[] topologySort() {
        Queue<Integer> q = new ArrayDeque<>();

        for(int i = 1; i < n + 1; i++) {
            if(inEdges[i] == 0) q.offer(i);
        }
        int[] result = new int[n + 1];
        int season = 1;
        while(!q.isEmpty()) {
            Queue<Integer> temp = new ArrayDeque<>();
            while(!q.isEmpty()) {
                int cur = q.poll();
                result[cur] = season;
                for (int next : connections[cur]) {
                    if (--inEdges[next] == 0) temp.offer(next);
                }
            }
            q = temp;
            season++;
        }
        return result;
    }
}
