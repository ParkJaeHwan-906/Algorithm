package jun.week5.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:55:17
 * AI 사용 여부 O
 * => qeury() 에서 cid 로 tree에서 값을 가져왔음 -> 위치를 고려한 값이 아닌 높이만 가지고 비교한 값
 * -> List로 각 위치의 LIS를 관리
 */
public class 코드트리등산게임_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int SET = 100;
    static final int ADD = 200;
    static final int DEL = 300;
    static final int QUERY = 400;

    static class Command {
        int type;

        // SET, ADD
        int h;

        // QUERY
        int cid;

        Command(int type) {     // DEL
            this.type = type;
        }

        Command(int type, int i) {  // QUERY, ADD, SET
            this.type = type;
            if(type == QUERY) this.cid = i;
            else this.h = i;
        }
    }

    static int n;
    static List<Integer> mountains;
    static Map<Integer, Deque<Integer>> history;
    static Queue<Command> commands;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        int q = Integer.parseInt(br.readLine().trim());
        commands = new ArrayDeque<>();
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            if(type == SET) { set(st); }
            else if(type == ADD) { add(st); }
            else if(type == DEL) { del(); }
            else if(type == QUERY) { query(st); }
        }

        System.out.println(solution());
    }

    static void set(StringTokenizer st) {
        n = Integer.parseInt(st.nextToken());
        history = new HashMap<>();
        mountains = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            add(st);
        }
    }

    static void add(StringTokenizer st) {
        int h = Integer.parseInt(st.nextToken());
        mountains.add(h);
        commands.offer(new Command(ADD, h));
    }

    static void del() {
        commands.offer(new Command(DEL));
    }

    static void query(StringTokenizer st) {
        int cid = Integer.parseInt(st.nextToken());
        commands.offer(new Command(QUERY, cid));
    }

    // ====
    static final int SCORE = 1_000_000;

    static int size;
    static int[] tree;
    static int[] hTree;
    static List<Integer> lis;
    static Map<Integer, Integer> compressed;
    static String solution() {
        StringBuilder sb = new StringBuilder();

        compress();
        set();
        while(!commands.isEmpty()) {
            Command cmd = commands.poll();
            if(cmd.type == ADD) {
                solutionAdd(cmd);
            } else if(cmd.type == DEL) {
                solutionDel();
            } else if(cmd.type == QUERY) {
                int result = solutionQuery(cmd);
                sb.append(result).append('\n');
            }
        }

        return sb.toString();
    }

    static void compress() {
        int[] temp = new int[mountains.size()];
        for(int i = 0; i < mountains.size(); i++) {
            temp[i] = mountains.get(i);
        }

        size = 0;
        Arrays.sort(temp);
        compressed = new HashMap<>();
        for(int i : temp) {
            if(compressed.containsKey(i)) continue;
            compressed.put(i, ++size);
        }
    }

    static void set() {
        tree = new int[4 * size];
        hTree = new int[4 * size];
        lis = new ArrayList<>();
        mountains.clear();
    }

    static void solutionAdd(Command command) {
        int h = command.h;
        int compressedId = compressed.get(h);
        int prevLis = query(1, 1, size, 1, compressedId - 1);
        update(1, 1, size, compressedId, prevLis + 1, h);
        history.computeIfAbsent(h, k -> new ArrayDeque<>()).push(prevLis + 1);
        mountains.add(h);
        lis.add(prevLis + 1);
    }

    static void solutionDel() {
        int h = mountains.get(mountains.size() - 1);
        mountains.remove(mountains.size() - 1);

        history.get(h).pop();
        if(history.get(h).isEmpty()) {
            history.remove(h);
            update(1, 1, size, compressed.get(h), 0, h);
        } else {
            update(1, 1, size, compressed.get(h), history.get(h).peek(), h);
        }
        lis.remove(lis.size() - 1);
    }

    static int solutionQuery(Command command) {
        int cid = command.cid;
        return (lis.get(cid - 1) + tree[1] - 1) * SCORE + hTree[1];
    }

    static int query(int id, int l, int r, int s, int e) {
        if(r < s || l > e) return 0;
        if(l >= s && r <= e) return tree[id];

        int mid = l + (r - l) / 2;
        int lLis =  query(2 * id, l, mid, s, e);
        int rLis =  query(2 * id + 1, mid + 1, r, s, e);
        return Math.max(lLis, rLis);
    }

    static void update(int id, int l, int r, int t, int v, int h) {
        if(t < l || r < t) return;
        if(l == r) {
            tree[id] = v;
            hTree[id] = h;
            return;
        }

        int mid = l + (r - l) / 2;
        update(2 * id, l, mid, t, v, h);
        update(2 * id + 1, mid + 1, r, t, v, h);
        if (tree[2 * id] > tree[2 * id + 1]) {
            tree[id] = tree[2 * id];
            hTree[id] = hTree[2 * id];
        } else if (tree[2 * id] < tree[2 * id + 1]) {
            tree[id] = tree[2 * id + 1];
            hTree[id] = hTree[2 * id + 1];
        } else {
            tree[id] = tree[2 * id];
            hTree[id] = Math.max(hTree[2 * id], hTree[2 * id + 1]);
        }
    }
}
