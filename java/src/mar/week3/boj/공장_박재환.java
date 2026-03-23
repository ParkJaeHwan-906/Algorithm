package mar.week3.boj;

import java.util.*;
import java.io.*;

public class 공장_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    /**
     * 2N 개의 기계가 2열에 걸쳐 N개씩 배치
     * 각 열에 있는 기계는 짝을 이루어 연결되어 있음
     * 식별번호 N개
     */
    static StringTokenizer st;
    static int n;
    static int[] arr1, arr2;
    static Map<Integer, Integer> map;
    static void init() throws IOException {
        map = new HashMap<>();

        n = Integer.parseInt(br.readLine().trim());
        arr1 = new int[n];
        arr2 = new int[n];

        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n;) arr1[i++] = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n; i++) {
            arr2[i] = Integer.parseInt(st.nextToken());
            map.put(arr2[i], i);
        }

        System.out.println(solution());
    }
    static long[] tree;
    static long solution() {
        bindLocation();
        /**
         * 현재 구간의 왼쪽에서, 현재 값보다 큰 값을 찾는다.
         * => 세그먼트 트리로 구간을 빠르게 조회한다.
         */
        long result = 0;
        tree = new long[4*n];
        for(int i : loc) {
            /**
             * 각 노드 : 해당 범위에 등장한 값의 개수
             *
             * query : 현재 값(i) 기준, i+1 ~ n-1 까지의 수가 나온 횟수
             * update : 현재 값 i 이 나온 개수를 업데이트
             */
            result += query(1, 0, n-1, i+1, n-1);
            update(1, 0, n-1, i);
        }
        return result;
    }
    static int[] loc;
    static void bindLocation() {
        loc = new int[n];
        for(int i=0; i<n; i++) loc[i] = map.get(arr1[i]);
    }
    static long query(int id, int l, int r, int s, int e)  {
        if(r < s || l > e) return 0;
        if(l >= s && r <= e) return tree[id];
        int mid = l + (r - l)/2;
        return query(2*id, l, mid, s, e)
                + query(2*id+1, mid+1, r, s, e);
    }
    static void update(int id, int l, int r, int target) {
        if(r < target || l > target) return ;
        if(l == r) {
            tree[id]++;
            return;
        }
        int mid = l + (r - l)/2;
        update(2*id, l, mid, target);
        update(2*id+1, mid+1, r, target);
        tree[id] = tree[2*id] + tree[2*id+1];
    }
}
