package mar.week5.boj;

import java.util.*;
import java.io.*;

public class 냅색문제_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, c;
    static long[] items;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());

        items = new long[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n; i++) items[i] = Long.parseLong(st.nextToken());

        solution();
    }
    static List<Long> r, l;
    static void solution() {
        r = new ArrayList<>();
        l = new ArrayList<>();

        int mid = n / 2;

        makeSum(0, mid, 0, l);
        makeSum(mid, n, 0, r);

        long answer = 0;
        r.sort(Long::compare);
        for(long i : l) {
            long remain = c - i;
            answer += binarySearch(r, remain);
        }

        System.out.println(answer);
    }
    static void makeSum(int id, int end, long sum, List<Long> list) {
        if(sum > c) return;
        if(id == end) {
            list.add(sum);
            return;
        }
        makeSum(id+1, end, sum, list);
        makeSum(id+1, end, sum + items[id], list);
    }
    static int binarySearch(List<Long> list, long target) {
        int left = 0, right = list.size();
        while(left < right) {
            int mid = left + (right - left) / 2;
            if(list.get(mid) > target) right = mid;
            else left = mid + 1;
        }
        return left;
    }
}
