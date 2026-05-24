package may.week3.ngv;

import java.util.*;
import java.io.*;

public class 징검다리_박재환_2 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int n;
    static int[] stones;
    static void init(BufferedReader br) throws IOException {
        n = Integer.parseInt(br.readLine().trim());

        StringTokenizer st;
        stones = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n;) stones[i++] = Integer.parseInt(st.nextToken());

        System.out.println(solution());
    }

    static int size;
    static int[] flatArr;
    static int[] tree;
    static int solution() {
        flatArr = flat();

        buildTree();
        for(int i : flatArr) {
            int prevLis = query(1, 1, size, 0, i - 1);
            update(1, 1, size, i, prevLis + 1);
        }

        return tree[1];
    }

    static int[] flat() {
        size = 0;

        int[] temp = stones.clone();
        Arrays.sort(temp);

        Map<Integer, Integer> tempMap = new HashMap<>();
        for(int i : temp) {
            if(tempMap.get(i) == null) tempMap.put(i, ++size);
        }

        for(int i = 0; i < n; i++) {
            temp[i] = tempMap.get(stones[i]);
        }

        return temp;
    }

    static void buildTree() {
        tree = new int[4 * size];
    }

    static int query(int id, int l, int r, int s, int e) {
        if(l > e || r < s) return 0;
        if(l >= s && r <= e) return tree[id];

        int mid = l + (r - l) / 2;
        return Math.max(
                query(2 * id, l, mid, s, e),
                query(2 * id + 1, mid + 1, r, s, e)
        );
    }

    static void update(int id, int l, int r, int targetId, int targetValue) {
        if(l > targetId || r < targetId) return;
        if(l == r) {
            tree[id] = Math.max(tree[id], targetValue);
            return;
        }

        int mid = l + (r - l) / 2;
        update(2 * id, l, mid, targetId, targetValue);
        update(2 * id + 1, mid + 1, r, targetId, targetValue);
        tree[id] = Math.max(tree[2 * id], tree[2 * id + 1]);
    }
}
