package mar.week2.boj;

import java.util.*;
import java.io.*;

public class _4cycle_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m;
    static List<Integer>[] connections;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        connections = new List[n+1];
        for(int i=0; i<n+1; i++) connections[i] = new ArrayList<>();
        while(m-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            connections[a].add(b);
            connections[b].add(a);
        }
        System.out.println(findCycle());
    }
    static int findCycle() {
        int answer = 0;
        Map<Long, Integer> map = new HashMap<>();
        for(int i=1; i<n+1; i++) {
            List<Integer> adj = connections[i];
            for(int a=0; a<adj.size(); a++) {
                for(int b=a+1; b<adj.size(); b++) {
                    int from = adj.get(a);
                    int to = adj.get(b);
                    /**
                     * 항상 같은 값으로 만들기 위해
                     * ex) (2,5), (5,2) 는 같은 간선
                     */
                    if(from > to) {
                        int temp = from;
                        from = to;
                        to = temp;
                    }

                    long key = ((long)from << 32) | to;
                    int cnt = map.getOrDefault(key,0);

                    answer += cnt;

                    map.put(key, cnt+1);
                }
            }
        }
        return answer/2;
    }
}
