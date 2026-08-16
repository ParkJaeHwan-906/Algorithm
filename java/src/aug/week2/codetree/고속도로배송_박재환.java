package aug.week2.codetree;

import java.util.*;
import java.io.*;

public class 고속도로배송_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static class Point implements Comparable<Point> {
        int position;
        int type;
        Point(int position, int type) {
            this.position = position;
            this.type = type;
        }
        public int compareTo(Point other) {
            return Integer.compare(this.position, other.position);
        }
    }

    static final long INF = Long.MAX_VALUE / 4;

    static int n;
    static int[] arrA;
    static int[] arrB;
    static int[] confuse;
    static void init(BufferedReader br) throws IOException {
        n = Integer.parseInt(br.readLine().trim());

        arrA = new int[n];
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arrA[i] = Integer.parseInt(st.nextToken());
        }

        arrB = new int[n];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arrB[i] = Integer.parseInt(st.nextToken());
        }

        confuse = new int[n + 1];
        st = new StringTokenizer(br.readLine());
        for (int congestion = 1; congestion <= n; congestion++) {
            confuse[congestion] = Integer.parseInt(st.nextToken());
        }

        System.out.println(solution());
    }

    static long solution() {
        Point[] points = new Point[2 * n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(arrA[i], 1);      // 창고
            points[n + i] = new Point(arrB[i], -1); // 매장
        }
        Arrays.sort(points);

        // dp[c] = 현재 좌표까지 처리했고, 이 좌표의 오른쪽을 지나는 트럭이 c대일 때 가능한 최소 비용
        long[] dp = new long[n + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        int difference = 0; // 처리한 창고 수 - 처리한 매장 수

        for (int i = 0; i < 2 * n; i++) {
            long[] next = new long[n + 1];
            Arrays.fill(next, INF);

            for (int congestion = 0; congestion <= n; congestion++) {
                if (dp[congestion] == INF) continue;

                int openWarehouses = (congestion + difference) / 2;
                int openStores = congestion - openWarehouses;

                // 현재 끝점을 새 트럭 경로의 시작점으로 삼는다.
                if (congestion < n) {
                    next[congestion + 1] = Math.min(
                            next[congestion + 1], dp[congestion]);
                }

                // 반대 종류의 열린 끝점이 있을 때 현재 끝점과 짝지어
                // 기존 트럭 경로를 끝낸다.
                boolean canClose = points[i].type == 1 ? openStores > 0 : openWarehouses > 0;
                if (canClose) {
                    next[congestion - 1] = Math.min(next[congestion - 1], dp[congestion]);
                }
            }
            difference += points[i].type;
            if (i + 1 < 2 * n) {
                long distance = (long) points[i + 1].position - points[i].position;
                for (int congestion = 1; congestion <= n; congestion++) {
                    if (next[congestion] != INF) {
                        next[congestion] += distance * confuse[congestion];
                    }
                }
            }
            dp = next;
        }

        return dp[0];
    }
}
