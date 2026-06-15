package jun.week2.ngv;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:32:57
 * AI 사용 여부 O
 * => 역방향 그래프에서 경로를 탐색할 때 출발지와 목적지를 설정했어서 정확한 탐색이 되지 않았음
 *      -> 역방향에서는 목적지에 도착해도 탐색을 멈추지 않도록 -1 로 두어 이동 가능한 모든 노드를 탐색하도록함
 *      -> 문제에서 S -> T의 경우 S는 여러번 재방문이 가능하다. 라는 조건을 참조.
 */
public class 출퇴근길_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int n, m;
    static List<Integer>[] connections;
    static int s, t;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        connections = new List[n + 1];
        for(int i = 0; i < n + 1; i++) connections[i] = new ArrayList<>();

        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            connections[from].add(to);
        }

        st = new StringTokenizer(br.readLine().trim());
        s = Integer.parseInt(st.nextToken());
        t = Integer.parseInt(st.nextToken());

        System.out.println(solution());
    }

    static List<Integer>[] reverseConnections;
    static int solution() {
        set();

        boolean[] sToT = findAllRoute(connections, s, t);
        boolean[] tToSReverse = findAllRoute(reverseConnections, t, -1);

        boolean[] tToS = findAllRoute(connections, t, s);
        boolean[] sToTReverse = findAllRoute(reverseConnections, s, -1);

        return findDuplicateNode(sToT, tToSReverse, tToS, sToTReverse) - 2;
    }

    static void set() {
        /**
         * 역방향 그래프 구성
         */
        reverseConnections = new List[n + 1];
        for(int i = 0; i < n + 1; i++) reverseConnections[i] = new ArrayList<>();

        for(int to = 1; to < n + 1; to++) {
            for(int from : connections[to]) reverseConnections[from].add(to);
        }
    }

    static boolean[] findAllRoute(List<Integer>[] list, int s, int e) {
        boolean[] visited = new boolean[n + 1];
        visited[s] = true;
        query(list, s, visited, e);
        return visited;
    }

    static void query(List<Integer>[] list, int cur, boolean[] visited, int target) {
        for(int next : list[cur]) {
            if(visited[next]) continue;
            visited[next] = true;
            query(list, next, visited, target);
        }
    }

    static int findDuplicateNode(boolean[] a, boolean[] b, boolean[] c, boolean[] d) {
        int duplicated = 0;
        for(int i = 1; i < n + 1; i++) {
            if(a[i] && b[i] && c[i] && d[i]) duplicated++;
        }
        return duplicated;
    }
}
