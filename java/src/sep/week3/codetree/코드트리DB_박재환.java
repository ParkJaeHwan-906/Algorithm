package sep.week3.codetree;

import java.util.*;
import java.io.*;

public class 코드트리DB_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final String INIT = "init";
    static final String INSERT = "insert";
    static final String DELETE = "delete";
    static final String RANK = "rank";
    static final String SUM = "sum";

    static class Command {
        String type;
        String str;
        int value;
        Command(String type, String str, int value) {
            this.type = type;
            this.str = str;
            this.value = value;
        }
        Command(String type, int value) {
            this.type = type;
            this.value = value;
        }
        Command(String type, String name) {
            this.type = type;
            this.str = name;
        }
    }

    static Queue<Command> commands;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        int q = Integer.parseInt(br.readLine().trim());
        commands = new ArrayDeque<>();
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            String type = st.nextToken();
            if(type.equals(INIT)) {
                if(!commands.isEmpty()) {
                    sb.append(solution());
                }
                commands.clear();
            } else if(type.equals(INSERT)) {
                String name = st.nextToken();
                int value = Integer.parseInt(st.nextToken());
                commands.add(new Command(INSERT, name, value));
            } else if(type.equals(DELETE)) {
                String name = st.nextToken();
                commands.add(new Command(DELETE, name));
            } else if(type.equals(RANK)) {
                int k = Integer.parseInt(st.nextToken());
                commands.add(new Command(RANK, k));
            } else if(type.equals(SUM)) {
                int k = Integer.parseInt(st.nextToken());
                commands.add(new Command(SUM, k));
            }
        }
        if(!commands.isEmpty()) {
            sb.append(solution());
        }
        System.out.print(sb);
    }

    static class Node {
        long accValues;
        int accRanks;
        Node(long accValues, int accRanks) {
            this.accValues = accValues;
            this.accRanks = accRanks;
        }
    }

    static int size;
    static Map<Integer, Integer> valueToId;
    static Map<String, Integer> nameToValue;
    static Map<Integer, String> valueToName;
    static List<Integer> sortedValues;
    static Node[] trees;
    static String solution() {
        nameToValue = new HashMap<>();
        valueToName = new HashMap<>();
        compressed();
        StringBuilder sb = new StringBuilder();
        trees = new Node[4 * size];
        for(int i = 0; i < 4 * size; i++) {
            trees[i] = new Node(0, 0);
        }
        while(!commands.isEmpty()) {
            Command command = commands.poll();
            if(command.type.equals(INSERT)) {
                sb.append(insert(command)).append('\n');
            } else if(command.type.equals(DELETE)) {
                sb.append(delete(command)).append('\n');
            } else if(command.type.equals(RANK)) {
                sb.append(rank(command)).append('\n');
            } else if(command.type.equals(SUM)) {
                sb.append(sum(command)).append('\n');
            }
        }
        return sb.toString();
    }

    static void compressed() {
        TreeSet<Integer> values = new TreeSet<>();
        for(Command command : commands) {
            if(command.type.equals(INSERT)) {
                values.add(command.value);
            }
        }
        valueToId = new HashMap<>();
        sortedValues = new ArrayList<>(values);
        size = sortedValues.size();
        for(int i = 0; i < size; i++) {
            int value = sortedValues.get(i);
            valueToId.put(value, i + 1);
        }
    }

    static int insert(Command command) {
        String name = command.str;
        int value = command.value;

        if(nameToValue.containsKey(name) || valueToName.containsKey(value)) {        // name 과 value 는 UNIQUE
            return 0;
        }

        int id = valueToId.get(value);
        update(1, 1, size, id, value);
        nameToValue.put(name, value);
        valueToName.put(value, name);
        return 1;
    }

    static int delete(Command command) {
        String name = command.str;
        Integer value = nameToValue.get(name);
        if(value == null) {
            return 0;
        }
        int id = valueToId.get(value);
        update(1, 1, size, id, -value);
        nameToValue.remove(name);
        valueToName.remove(value);
        return value;
    }

    static String rank(Command command) {
        if(size == 0) {
            return "None";
        }
        int k = command.value;
        int id = queryRank(1, 1, size, k);
        if(id == -1) {
            return "None";
        }
        int value = sortedValues.get(id - 1);
        return valueToName.get(value);
    }

    static long sum(Command command) {
        int k  = command.value;
        int id = findInsert(k);
        if(id == -1) {
            return 0;
        }
        return querySum(1, 1, size, 1, id + 1);
    }

    static int findInsert(int k) {
        int l = 0, r = sortedValues.size();
        while(l < r) {
            int mid = l + (r - l) / 2;
            if(sortedValues.get(mid) <= k) {     // 찾고자 하는 값보다 작을 때 -> 더 큰 범위 탐색 가능
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l - 1;
    }


    static void update(int id, int l, int r, int target, int value) {
        if(target < l || target > r) {
            return;
        }
        if(l == r) {
            trees[id].accValues += value;
            trees[id].accRanks += value > 0
                    ? 1 : -1;
            return;
        }
        int mid = l + (r - l) / 2;
        update(2 * id, l, mid, target, value);
        update(2 * id + 1, mid + 1, r, target, value);
        trees[id].accValues = trees[2 * id].accValues + trees[2 * id + 1].accValues;
        trees[id].accRanks = trees[2 * id].accRanks + trees[2 * id + 1].accRanks;
    }

    static int queryRank(int id, int l, int r, int target) {
        if (target > trees[id].accRanks) {
            return -1;
        }
        if (l == r) {
            return l;
        }
        int mid = l + (r - l) / 2;
        int leftCount = trees[2 * id].accRanks;
        if (target <= leftCount) {
            return queryRank(2 * id, l, mid, target);
        }
        return queryRank(
                2 * id + 1, mid + 1, r, target - leftCount
        );
    }

    static long querySum(int id, int l, int r, int s, int e) {
        if(l > e || r < s) {
            return 0;
        }
        if(l >= s && r <= e) {
            return trees[id].accValues;
        }
        int mid = l + (r - l) / 2;
        return querySum(2 * id, l, mid, s, e) + querySum(2 * id + 1, mid + 1, r, s, e);
    }
}
