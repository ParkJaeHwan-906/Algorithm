package apr.week1.boj;

import java.util.*;
import java.io.*;

public class 숨바꼭질2_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static final int MAX = 100_000;
    static final int INF = 987654321;
    static StringTokenizer st;
    static int n, k;
    static int[] arr;
    static int[] countArr;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 수빈 위치
        k = Integer.parseInt(st.nextToken());       // 동생 위치

        arr = new int[MAX + 1];
        countArr = new int[MAX + 1];
        Arrays.fill(arr, INF);
        solution();
    }
    static void solution() {
        Queue<int[]> q = new ArrayDeque<>();
        // 수빈의 최초 위치
        arr[n] = 0;
        countArr[n] = 1;
        q.offer(new int[] {n, 0});

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int loc = cur[0];
            int time = cur[1];
            if(arr[loc] < time) continue;

            // 이동
            int backMove = loc - 1;
            if(backMove >= 0 && arr[backMove] > time + 1) {
                arr[backMove] = time + 1;
                countArr[backMove] = countArr[loc];
                q.offer(new int[] {backMove, time + 1});
            } else if(backMove >= 0 && arr[backMove] == time + 1) {
                countArr[backMove] += countArr[loc];
            }

            int frontMove = loc + 1;
            if(frontMove <= MAX && arr[frontMove] > time + 1) {
                arr[frontMove] = time + 1;
                countArr[frontMove] = countArr[loc];
                q.offer(new int[] {frontMove, time + 1});
            } else if(frontMove <= MAX && arr[frontMove] == time + 1) {
                countArr[frontMove] += countArr[loc];
            }

            // 순간이동
            int jumpMove = 2 * loc;
            if(jumpMove <= MAX && arr[jumpMove] > time + 1) {
                arr[jumpMove] = time + 1;
                countArr[jumpMove] = countArr[loc];
                q.offer(new int[] {jumpMove, time + 1});
            } else if(jumpMove <= MAX && arr[jumpMove] == time + 1) {
                countArr[jumpMove] += countArr[loc];
            }
        }
        System.out.printf("%d\n%d", arr[k], countArr[k]);
    }
}
