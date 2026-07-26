package jul.week3.jungol;

import java.util.*;
import java.io.*;

/**
 * [오답]
 * -> 메모리 최적화가 안되는뎁쇼..
 */
public class 대표선수_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n, m;
    static int[][] students;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        students = new int[n][m];
        for(int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int j = 0; j < m;) students[i][j++] = Integer.parseInt(st.nextToken());
        }

        System.out.println(solution());
    }

    static void set() {
        for(int i = 0; i < n; i++) Arrays.sort(students[i]);
    }

    static int solution() {
        set();
        int[] ptr = new int[n];           // 각 반의 현재 인덱스
        // {능력치, 반번호}, 능력치 기준 최소 힙
        PriorityQueue<int[]> pq = new PriorityQueue<>((x, y) -> x[0] - y[0]);
        int curMax = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            pq.offer(new int[]{students[i][0], i});
            curMax = Math.max(curMax, students[i][0]);
        }

        int ans = Integer.MAX_VALUE;
        while (true) {
            int[] top = pq.poll();
            int val = top[0], cls = top[1];
            ans = Math.min(ans, curMax - val);   // 현재 조합의 범위

            ptr[cls]++;
            if (ptr[cls] == m) break;            // 이 반 소진 → 종료
            int next = students[cls][ptr[cls]];
            pq.offer(new int[]{next, cls});
            curMax = Math.max(curMax, next);
        }
        return ans;
    }
}
