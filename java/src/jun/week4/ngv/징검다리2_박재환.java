package jun.week4.ngv;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:37:48
 * AI 사용 여부 X
 */
public class 징검다리2_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n;
    static int[] arr;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        n = Integer.parseInt(br.readLine().trim());
        arr = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        System.out.println(solution());
    }

    static int solution() {
        compress();

        int[] lisTree = new int[4 * size];
        int[] lis = new int[n];
        for(int i = 0; i < n; i++) {
            int prevBest = query(1, 1, size, 1, compressed[i] - 1, lisTree);
            update(1, 1, size, compressed[i], compressed[i], prevBest + 1, lisTree);
            lis[i] = prevBest + 1;
        }


        int[] ldsTree = new int[4 * size];
        int[] lds = new int[n];
        for(int i = n - 1; i >= 0; i--) {
            int prevBest = query(1, 1, size, 1, compressed[i] - 1, ldsTree);
            update(1, 1, size, compressed[i], compressed[i], prevBest + 1, ldsTree);
            lds[i] = prevBest + 1;
        }

        int max = 0;
        for(int i = 0; i < n; i++) {
            max = Math.max(max, lis[i] + lds[i] - 1);
        }
        return max;
    }

    static int size;
    static int[] compressed;
    static void compress() {
        size = 0;
        int[] temp = new int[n];
        for(int i = 0; i < n; i++) temp[i] = arr[i];
        Arrays.sort(temp);

        Map<Integer, Integer> map = new HashMap<>();
        for(int i : temp) {
            if(map.containsKey(i)) continue;
            map.put(i, ++size);
        }

        compressed = new int[n];
        for(int i = 0; i < n; i++) compressed[i] = map.get(arr[i]);
    }

    static void update(int id, int l, int r, int s, int e, int v, int[] tree) {
        if(e < l || s > r) return;
        if(l >= s && r <= e) {
            tree[id] = v;
            return;
        }

        int mid = l + (r - l) / 2;
        update(2 * id, l, mid, s, e, v, tree);
        update(2 * id + 1, mid + 1, r, s, e, v, tree);
        tree[id] = Math.max(tree[2 * id], tree[2 * id + 1]);
    }

    static int query(int id, int l, int r, int s, int e, int[] tree) {
        if (e < l || s > r) return 0;
        if (l >= s && r <= e) return tree[id];

        int mid = l + (r - l) / 2;
        int left = query(2 * id, l, mid, s, e, tree);
        int right = query(2 * id + 1, mid + 1, r, s, e, tree);
        return Math.max(left, right);
    }
}
