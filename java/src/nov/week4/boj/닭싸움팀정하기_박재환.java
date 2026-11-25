package nov.week4.boj;

import java.util.*;
import java.io.*;

public class 닭싸움팀정하기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(init(br));
        br.close();
    }

    static int n;
    static int[] parents;
    static List<Integer>[] enemies;
    static int init(BufferedReader br) throws IOException {
        StringTokenizer st;
        n = Integer.parseInt(br.readLine().trim());

        int m = Integer.parseInt(br.readLine().trim());
        make();
        enemies = new List[n + 1];
        for(int i = 1; i <= n; i++)
            enemies[i] = new ArrayList<>();
        while(m-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            String type = st.nextToken();
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            if(type.equals("F")) {
                union(a, b);
            } else {
                enemies[a].add(b);
                enemies[b].add(a);
                for(int f : enemies[b])
                    union(a, f);
                for(int f : enemies[a])
                    union(b, f);
            }
        }

        Set<Integer> group = new HashSet<>();
        for(int i = 1; i <= n; i++) {
            int id = find(i);
            group.add(id);
        }
        return group.size();
    }

    static void make() {
        parents = new int[n + 1];
        for(int i = 1; i <= n; i++)
            parents[i] = i;
    }

    static int find(int a) {
        if(a == parents[a])
            return a;
        return parents[a] = find(parents[a]);
    }

    static void union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if(rootA == rootB)
            return;
        parents[rootA] = rootB;
    }
}
