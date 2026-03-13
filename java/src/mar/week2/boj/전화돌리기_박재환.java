package mar.week2.boj;

import java.util.*;
import java.io.*;

public class 전화돌리기_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m;
    static List<Integer>[] connections;
    static List<Integer>[] reverseConnections;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        connections = new List[n+1];
        reverseConnections = new List[n+1];
        for(int i=1; i<n+1; i++) {
            connections[i] = new ArrayList<>();
            reverseConnections[i] = new ArrayList<>();
        }

        for(int i=0; i<m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            connections[from].add(to);
            reverseConnections[to].add(from);
        }
        System.out.println(solution());
    }
    /**
     * 그래프가 사이클을 포함하지 않도록
     */
    static int solution() {
        edgeCount();
        /**
         * 역방향 그래프 + 위상정렬
         *
         * 역방향으로 그래프를 구성
         * outEdge 가 0인 그래프부터 시작 (절대 사이클이 생길 수 없음)
         * -> 연결 차수를 하나씩 줄임
         */
        Queue<Integer> q = new ArrayDeque<>();
        for(int i=1; i<n+1; i++) {      // 현재
            if(outEdge[i] == 0) q.offer(i);
        }
        int answer = 0;
        while(!q.isEmpty()) {
            int cur = q.poll();
            answer++;
            for(int next : reverseConnections[cur]) {
                if(--outEdge[next] == 0) q.offer(next);
            }
        }
        return answer;
    }
    static int[] outEdge;
    static void edgeCount() {
        /**
         * 현재 노드에서 밖으로 이어져있는 간선의 개수를 구함
         */
        outEdge = new int[n+1];
        for(int node=1; node<n+1; node++) {
            outEdge[node] = connections[node].size();
        }
    }
}
