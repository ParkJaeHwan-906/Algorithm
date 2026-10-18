package oct.week2.codetree;

import java.util.*;
import java.io.*;

public class 식량보급_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int SET = 100;
    static final int ADD = 200;
    static final int UPDATE = 300;
    static final int QUERY = 400;

    static class Cmd {
        int type;
        long num;
        Cmd(int type, long num) {
            this.type = type;
            this.num = num;
        }
    }

    static Queue<Cmd> cmds;
    static long extraNum;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        cmds = new ArrayDeque<>();
        List<Long> nums = new ArrayList<>();
        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            if(type == SET)
                cmdSet(st, nums);
            else if(type == ADD)
                cmds.offer(cmdAdd(st, nums));
            else if(type == UPDATE)
                cmds.offer(cmdUpdate(st));
            else if(type == QUERY)
                cmds.offer(cmdQuery(st));
        }
        System.out.print(solution(nums));
    }

    static void cmdSet(StringTokenizer st, List<Long> nums) {
        int n = Integer.parseInt(st.nextToken());
        for(int i = 0; i < n; i++)
            cmds.offer(cmdAdd(st, nums));
    }

    static Cmd cmdAdd(StringTokenizer st, List<Long> nums) {
        long num = Long.parseLong(st.nextToken());
        nums.add(num - extraNum);
        return new Cmd(ADD, num - extraNum);
    }

    static Cmd cmdUpdate(StringTokenizer st) {
        long p = Long.parseLong(st.nextToken());
        extraNum += p;
        return new Cmd(UPDATE, p);
    }

    static Cmd cmdQuery(StringTokenizer st) {
        long p = Long.parseLong(st.nextToken());
        return new Cmd(QUERY, p);
    }

    static int size;
    static Map<Long, Integer> numToId;
    static long[] countTree;
    static long[] sumTree;
    static long queryCount;
    static long querySum;
    static long min, max;
    static String solution(List<Long> nums) {
        StringBuilder sb = new StringBuilder();
        compress(nums);
        extraNum = 0;
        min = Long.MAX_VALUE;
        max = Long.MIN_VALUE;
        countTree = new long[4 * size];
        sumTree = new long[4 * size];
        while(!cmds.isEmpty()) {
            Cmd cmd = cmds.poll();
            if(cmd.type == ADD)
                add(cmd);
            else if(cmd.type == UPDATE)
                update(cmd);
            else if(cmd.type == QUERY)
                sb.append(query(cmd, nums)).append('\n');
        }
        return sb.toString();
    }

    static void add(Cmd cmd) {
        int id = numToId.get(cmd.num);
        min = Math.min(min, cmd.num);
        max = Math.max(max, cmd.num);
        updateTrees(1, 1, size, id, cmd.num);
    }

    static void update(Cmd cmd) {
        extraNum += cmd.num;
    }

    static long query(Cmd cmd, List<Long> nums) {
        long l = min;
        long r = max + cmd.num;
        while(l < r) {
            long mid = l + (r - l + 1) / 2;
            if(isPossible(mid, nums, cmd.num))
                l = mid;
            else
                r = mid - 1;
        }
        return l + extraNum;
    }

    static boolean isPossible(long num, List<Long> nums, long limit) {
        int l = 0, r = nums.size();
        while(l < r) {
            int mid = l + (r - l) / 2;

            if(nums.get(mid) >= num)
                r = mid;
            else
                l = mid + 1;
        }
        if(l == 0)
            return true;
        int id = numToId.get(nums.get(l - 1));
        queryCount = 0;
        querySum = 0;
        queryTrees(1, 1, size, 1, id);
        return (queryCount * num) - querySum <= limit;
    }

    static void queryTrees(int id, int l, int r, int s, int e) {
        if(r < s || l > e)
            return;
        if(s <= l && r <= e) {
            queryCount += countTree[id];
            querySum += sumTree[id];
            return;
        }
        int mid = l + (r - l) / 2;
        queryTrees(2 * id, l, mid, s, e);
        queryTrees(2 * id + 1, mid + 1, r, s, e);
    }

    static void updateTrees(int id, int l, int r, int target, long value) {
        if(l > target || r < target)
            return;
        if(l == r) {
            countTree[id]++;
            sumTree[id] += value;
            return;
        }
        int mid = l + (r - l) / 2;
        updateTrees(2 * id, l, mid, target, value);
        updateTrees(2 * id + 1, mid + 1, r, target, value);
        countTree[id] = countTree[2 * id] + countTree[2 * id + 1];
        sumTree[id] = sumTree[2 * id] + sumTree[2 * id + 1];
    }

    static void compress(List<Long> nums) {
        size = 0;
        nums.sort(Long::compareTo);
        numToId = new HashMap<>();
        for(long num : nums) {
            if(numToId.containsKey(num))
                continue;
            numToId.put(num, ++size);
        }
    }
}