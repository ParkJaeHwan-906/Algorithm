package may.week4.ngv;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:31:54
 * AI 사용 여부 X (이전에 풀었었음)
 */
public class 거리합구하기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n;
    static List<int[]>[] connections;
    static long[] subtrees;
    static long[] subtreesDist;
    static void init(BufferedReader br) throws IOException {
        n = Integer.parseInt(br.readLine().trim());

        connections = new List[n + 1];
        subtrees = new long[n + 1];
        subtreesDist = new long[n + 1];
        for(int i = 0; i < n + 1; i++) connections[i] = new ArrayList<>();

        StringTokenizer st;
        for(int i = 1; i < n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());
            connections[a].add(new int[] {b, cost});
            connections[b].add(new int[] {a, cost});
        }
        System.out.println(solution());
    }

    static String solution() {
        getSubTree(1, -1);
        getAllDist(1, -1);

        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < n + 1; i++) sb.append(String.format("%d\n", subtreesDist[i]));
        return sb.toString();
    }

    static void getSubTree(int cur, int prev) {
        subtrees[cur] = 1;      // 자기 자신
        for(int[] connection : connections[cur]) {
            int next = connection[0];
            int cost = connection[1];

            if(next == prev) continue;      // 사이클 X

            getSubTree(next, cur);
            subtrees[cur] += subtrees[next];
            subtreesDist[cur] += subtreesDist[next] + cost * subtrees[next];        // 서브트리 내 노드들의 거리 합
        }
    }

    static void getAllDist(int cur, int prev) {
        for(int[] connection : connections[cur]) {
            int next = connection[0];
            int cost = connection[1];

            if(next == prev) continue;      // 사이클 X

            subtreesDist[next] = subtreesDist[cur] + (cost * (n - subtrees[next]))
                    - (cost * (subtrees[next]));
            getAllDist(next, cur);
        }
    }
}
