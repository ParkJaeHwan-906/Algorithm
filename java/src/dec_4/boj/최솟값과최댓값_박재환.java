package dec_4.boj;

import java.util.*;
import java.io.*;

public class 최솟값과최댓값_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }

    /**
     * N 개의 정수들이 있다.
     * [a, b] 구간에서 제일 작은 정수, 제일 큰 정수를 찾는다.
     * => 세그먼트 트리로, 가장 작은 값 큰 값을 기록한다.
     */
    static StringTokenizer st;
    static int n, m;
    static int[] arr;
    static Node[] tree;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        arr = new int[n];
        tree = new Node[4*n];
        for(int i=0; i<n; i++) arr[i] = Integer.parseInt(br.readLine().trim());
        init(0, n-1, 1);
        while(m-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int l = Integer.parseInt(st.nextToken())-1;
            int r = Integer.parseInt(st.nextToken())-1;
            Node result = query(0, n-1, 1, l, r);
            sb.append(result.min).append(' ').append(result.max).append('\n');
        }
    }

    static class Node {
        int min;
        int max;

        public Node(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }

    static Node init(int start, int end, int idx) {
        if(start == end) return tree[idx] = new Node(arr[start], arr[start]);

        int mid = start + (end-start)/2;
        Node left = init(start, mid, idx*2);
        Node right = init(mid+1, end, idx*2+1);
        return tree[idx] = new Node(
                Math.min(left.min, right.min),
                Math.max(left.max, right.max)
        );
    }

    static Node query(int start, int end, int idx, int l, int r) {
        if(start > r || end < l) return new Node(Integer.MAX_VALUE, Integer.MIN_VALUE);   // 질의 범위에 포함되지 않는 경우
        if(l <= start && r >= end) return tree[idx];    // 질의 내부에 범위가 완전하게 포함되는 경우

        int mid = start + (end-start)/2;
        Node left = query(start, mid, idx*2, l, r);
        Node right = query(mid+1, end, idx*2+1, l, r);
        return new Node(
                Math.min(left.min, right.min),
                Math.max(left.max, right.max)
        );
    }
}
