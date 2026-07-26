package jul.week3.jungol;

import java.util.*;
import java.io.*;

public class 지하철_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n, m;
    static int[][] dist;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken()) - 1;

        dist = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            // i -> j 까지 가는데 걸리는 시간
            for(int y = 0; y < n; y++) dist[x][y] = Integer.parseInt(st.nextToken());
        }

        System.out.println(solution());
    }

    static int[][] next;
    static String solution() {
        findAllPath();
        StringBuilder sb = new StringBuilder().append(dist[0][m]).append('\n');
        // 경로 복원
        int start = 0;
        sb.append(start + 1).append(' ');
        while(start != m) {
            start = next[start][m];
            sb.append(start + 1).append(' ');
        }
        return sb.toString();
    }

    static void findAllPath() {
        next = new int[n][n];
        for(int start = 0; start < n; start++) {
            for(int end = 0; end < n; end++) next[start][end] = end;
        }

        for(int mid = 0; mid < n; mid++) {
            for(int start = 0; start < n; start++) {
                for(int end = 0; end < n; end++) {
                    if(dist[start][end] > dist[start][mid] + dist[mid][end]) {
                        dist[start][end] = dist[start][mid] + dist[mid][end];
                        next[start][end] = next[start][mid];
                    }
                }
            }
        }
    }
}
