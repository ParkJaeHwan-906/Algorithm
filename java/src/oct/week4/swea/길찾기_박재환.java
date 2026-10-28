package oct.week4.swea;

import java.util.*;
import java.io.*;

public class 길찾기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        for(int tc = 1; tc <= 10; tc++) {
            sb.append(init(br)).append('\n');
        }
        System.out.print(sb);
        br.close();
    }

    static List<Integer>[] connections;
    static String init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        st = new StringTokenizer(br.readLine().trim());
        int tc = Integer.parseInt(st.nextToken());
        int n = Integer.parseInt(st.nextToken());
        connections = new List[100];
        for(int i = 0; i < 100; i++)
            connections[i] = new ArrayList<>();
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n; i++) {
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            connections[from].add(to);
        }
        return sb.append('#').append(tc).append(' ').append(solution()).toString();
    }

    static int[] visited;
    static int solution() {
        visited = new int[100];
        return findAllRoute(0) ? 1 : 0;
    }

    static boolean findAllRoute(int cur) {
        if(cur == 99)
            return true;
        visited[cur] = 1;
        for(int next : connections[cur]) {
            if(visited[next] == 0 && findAllRoute(next))
                return true;
        }
        visited[cur] = 2;
        return false;
    }
}
