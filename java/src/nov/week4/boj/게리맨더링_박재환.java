package nov.week4.boj;

import java.util.*;
import java.io.*;

public class 게리맨더링_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(init(br));
        br.close();
    }

    static final int INF = Integer.MAX_VALUE / 2;

    static int n;
    static int[] cities;
    static List<Integer>[] connections;
    static int init(BufferedReader br) throws IOException {
        StringTokenizer st;
        n = Integer.parseInt(br.readLine().trim());
        cities = new int[n + 1];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 1; i <= n; i++)
            cities[i] = Integer.parseInt(st.nextToken());

        connections = new List[n + 1];
        for(int i = 1; i <= n; i++) {
            connections[i] = new ArrayList<>();
            st = new StringTokenizer(br.readLine().trim());
            int m = Integer.parseInt(st.nextToken());
            while(m-- > 0)
                connections[i].add(Integer.parseInt(st.nextToken()));
        }

        return solution();
    }

    static int FULL;
    static int[] dp;
    static int solution() {
        FULL = ((1 << (n + 1)) - 1) ^ (1 << 0);
        dp = new int[FULL + 1];
        int result = findBestCombi(1, 0, 0);
        return result == INF ? -1 : result;
    }

    static int findBestCombi(int cur, int state, int acc) {
        if(cur == n + 1) {
            if(state == 0 || state == FULL)
                return INF;
            int groupA = state;
            int groupB = FULL ^ state;
            if(!isConnected(groupA) || !isConnected(groupB))
                return INF;
            return Math.abs(acc);
        }

        int include = findBestCombi(cur + 1, state | (1 << cur), acc + cities[cur]);
        int exclude = findBestCombi(cur + 1, state, acc - cities[cur]);
        return Math.min(include, exclude);
    }

    static boolean isConnected(int state) {
        Queue<Integer> q = new ArrayDeque<>();
        int visited = 0;

        for(int i = 1; i <= n; i++) {
            if((state & (1 << i)) == 0)
                continue;
            q.offer(i);
            visited |= (1 << i);
            break;
        }

        while (!q.isEmpty()) {
            int cur = q.poll();
            for (int next : connections[cur]) {
                if ((visited & (1 << next)) != 0)
                    continue;
                if ((state & (1 << next)) == 0) {
                    continue;
                }
                visited |= (1 << next);
                q.offer(next);
            }
        }
        return visited == state;
    }
}
