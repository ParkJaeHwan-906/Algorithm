package sep.week2.codetree;

import java.util.*;
import java.io.*;

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
    static final long SCORE = 1_000_000L;

    static class Command {
        int cmd;
        int i;
        Command(int cmd, int i) {
            this.cmd = cmd;
            this.i = i;
        }
        Command(int cmd) {
            this.cmd = cmd;
        }
    }

    static int n;
    static Queue<Command> cmds;
    static List<Integer> addList;                   // 초기 경로 압축을 위한 임시 리스트
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());
            if(cmd == SET) {
                set(st);
            }
            else if(cmd == ADD) {
                add(st);
            }
            else if(cmd == DEL) {
                del();
            }
            else if(cmd == QUERY) {
                query(st);
            }
        }
        System.out.print(solution());
    }

    static int size;
    static Map<Integer, Integer> compressed;
    static Map<Integer, Deque<Integer>> history;
    static List<Integer> mountains;
    static List<Integer> lisList;
    static int[] trees;
    static int[] heightTrees;
    static String solution() {
        StringBuilder sb = new StringBuilder();

        compress();
        trees = new int[4 * size];
        heightTrees = new int[4 * size];
        history = new HashMap<>();
        mountains = new ArrayList<>();
        lisList = new ArrayList<>();
        while(!cmds.isEmpty()) {
            Command cmd = cmds.poll();
            if(cmd.cmd == ADD) {
                add(cmd);
            } else if(cmd.cmd == DEL) {
                delLast();
            } else if(cmd.cmd == QUERY) {
                long result = query(cmd);
                sb.append(result).append("\n");
            }
        }
        return sb.toString();
    }

    // ======================================================
    // 배열 압축
    // ======================================================
    static void compress() {
        compressed = new HashMap<>();
        addList.sort(Integer :: compareTo);
        for(int i : addList) {
            if(!compressed.containsKey(i)) {
                compressed.put(i, ++size);
            }
        }
    }
    // ======================================================
    // add
    // ======================================================
    static void add(Command cmd) {
        int curSeq = compressed.get(cmd.i);
        int prevBest = query(1, 1, size, 1, curSeq - 1);
        int curLis = prevBest + 1;
        update(1, 1, size, curSeq, curLis, cmd.i);
        mountains.add(cmd.i);
        lisList.add(curLis);
        history.computeIfAbsent(cmd.i, k -> new ArrayDeque<>()).offerLast(curLis);
    }
    // ======================================================
    // del
    // ======================================================
    static void delLast() {
        int lastIndex = mountains.size() - 1;
        int last = mountains.remove(lastIndex);
        lisList.remove(lastIndex);
        int delSeq = compressed.get(last);
        Deque<Integer> deque = history.get(last);
        deque.pollLast();
        int rollBackLen = deque.isEmpty() ? 0 : deque.peekLast();
        update(1, 1, size, delSeq, rollBackLen, rollBackLen == 0 ? 0 : last);
    }
    // ======================================================
    // query
    // ======================================================
    static long query(Command cmd) {
        int cableCarBest = lisList.get(cmd.i - 1);
        int totalBest = trees[1];
        int finalHeight = heightTrees[1];
        return (cableCarBest + totalBest - 1L) * SCORE + finalHeight;
    }
    // ======================================================
    // Segment Tree
    // ======================================================
    static int query(int id, int l, int r, int s, int e) {
        if(r < s || l > e) {        // 탐색 범위가 아님
            return 0;
        }
        if(l >= s && r <= e) {
            return trees[id];
        }
        int mid = l + (r - l) / 2;
        return Math.max(
                query(2 * id, l, mid, s, e),
                query(2 * id + 1, mid + 1, r, s, e)
        );
    }
    static void update(int id, int l, int r, int target, int value, int height) {
        if(l > target || r < target) {
            return;
        }
        if(l == r) {
            trees[id] = value;
            heightTrees[id] = height;
            return;
        }
        int mid = l + (r - l) / 2;
        update(2 * id, l, mid, target, value, height);
        update(2 * id + 1, mid + 1, r, target, value, height);

        if(trees[2 * id] > trees[2 * id + 1]) {
            trees[id] = trees[2 * id];
            heightTrees[id] = heightTrees[2 * id];
        } else if(trees[2 * id] < trees[2 * id + 1]) {
            trees[id] = trees[2 * id + 1];
            heightTrees[id] = heightTrees[2 * id + 1];
        } else {
            trees[id] = trees[2 * id];
            heightTrees[id] = Math.max(heightTrees[2 * id], heightTrees[2 * id + 1]);
        }
    }
    // ======================================================
    // Commands Queue
    // ======================================================

    static void set(StringTokenizer st) {
        cmds = new ArrayDeque<>();
        addList = new ArrayList<>();
        n = Integer.parseInt(st.nextToken());
        for(int i = 0; i < n; i++) {
            int h = Integer.parseInt(st.nextToken());
            cmds.offer(new Command(ADD, h));
            addList.add(h);
        }
    }

    static void add(StringTokenizer st) {
        int h = Integer.parseInt(st.nextToken());
        cmds.offer(new Command(ADD, h));
        addList.add(h);
    }

    static void del() {
        cmds.offer(new Command(DEL));
    }

    static void query(StringTokenizer st) {
        int id = Integer.parseInt(st.nextToken());
        cmds.offer(new Command(QUERY, id));
    }
}
