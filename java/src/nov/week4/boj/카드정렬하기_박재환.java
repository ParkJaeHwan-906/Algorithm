package nov.week4.boj;

import java.util.*;
import java.io.*;

public class 카드정렬하기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(init(br));
        br.close();
    }

    static long init(BufferedReader br) throws IOException {
        int n = Integer.parseInt(br.readLine().trim());
        int[] arr = new int[n];
        for(int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(br.readLine().trim());
        }
        return solution(arr);
    }

    static long solution(int[] arr) {
        PriorityQueue<Long> pq = set(arr);
        long sum = 0;
        while(!pq.isEmpty() && pq.size() > 1) {
            long a = pq.poll();
            long b = pq.poll();
            sum += (a + b);
            pq.offer(a + b);
        }
        return sum;
    }

    static PriorityQueue<Long> set(int[] arr) {
        PriorityQueue<Long> pq = new PriorityQueue<>(Long::compareTo);
        for(int i : arr)
            pq.offer((long) i);
        return pq;
    }
}
