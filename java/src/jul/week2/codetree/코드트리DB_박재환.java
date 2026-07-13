package jul.week2.codetree;

import java.util.*;
import java.io.*;

/**
 * AI 사용 여부 O
 * => sum() 메서드에서 이분탐색 시 values 를 기준으로 탐색했음. > 중복 데이터를 모두 포함했기 때문에 정확한 탐색이 되지 않았음
 */
public class 코드트리DB_박재환 {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    /**
     *  Query Type
     *
     *  1. init
     *  기존 table 을 초기화 합니다.
     *
     *  2. insert
     *  `insert name value` 형태로 제공됩니다.
     *  table에 새로운 Row를 추가합니다. (name, value) - name, value는 각각 unique 합니다.
     *
     *  3. delete
     *  `delete name` 형태로 제공됩니다.
     *  table에서 name에 해당하는 row를 제거합니다.
     *  - 존재한다면 value 출력
     *  - 존재하지 않는다면 0 출력
     *
     *  4. rank
     *  `rank k` 형태로 제공됩니다.
     *  k번째로 작은 value를 가진 row의 name을 출력합니다.
     *  - table이 가진 row수가 k보다 작다면 'None'을 출력합니다.
     *
     *  5. sum
     *  `sum k` 형태로 제공됩니다.
     *  k 이하의 value를 가진 모든 row의 value를 합한 값을 출력합니다.
     */

    static final int INSERT = 200;
    static final int DELETE = 300;
    static final int RANK = 400;
    static final int SUM = 500;

    static class Command {
        int type;

        String name;
        int value;

        int k;

        Command(int type) {     // INIT
            this.type = type;
        }

        Command(int type, String name, int value) {        // INSERT
            this.type = type;
            this.name = name;
            this.value = value;
        }

        Command(int type, String name) {       //  DELETE
            this.type = type;
            this.name = name;
        }

        Command(int type, int k) {       //  RANK, SUM
            this.type = type;
            this.k = k;
        }
    }

    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        /**
         * 한 table의 최대 row 는 100,000
         * -> name과 value는 unique key : map 또는 set 으로 관리
         *
         * rank, sum 이 각 최대 100,000 번 호출 -> 완탐 시 100,000 ** 2
         * => 둘 다 value 에 대한 query
         * Segtree..?
         */
        int q = Integer.parseInt(br.readLine().trim());
        Queue<Command> commands = new ArrayDeque<>();
        while(q-- > 0) {
            /**
             * init 단위로 하나의 solution 수행
             */
            st = new StringTokenizer(br.readLine().trim());
            String type = st.nextToken();

            Command command = null;
            if(type.equals("init")) {
                if(!commands.isEmpty()) {       // 쿼리가 누적되어 있는 경우
                    solution(commands, sb);
                }
            }
            else if(type.equals("insert")) {
                String name = st.nextToken();
                int value = Integer.parseInt(st.nextToken());
                command = new Command(INSERT, name, value);
            }
            else if(type.equals("delete")) {
                String name = st.nextToken();
                command = new Command(DELETE, name);
            }
            else if(type.equals("rank")) {
                int k = Integer.parseInt(st.nextToken());
                command = new Command(RANK, k);
            }
            else if(type.equals("sum")) {
                int k = Integer.parseInt(st.nextToken());
                command = new Command(SUM, k);
            }

            if(command != null) commands.offer(command);
        }

        if(!commands.isEmpty()) solution(commands, sb);

        System.out.println(sb);
    }

    static int size;
    static List<Integer> values;
    static Map<Integer, Integer> numberToSeq;
    static Map<Integer, Integer> seqToNumber;
    static Map<String, Integer> nameToValue;
    static Map<Integer, String> valueToName;
    static int[] rankTree;
    static long[] sumTree;
    static void solution(Queue<Command> commands, StringBuilder sb) {
        flat(commands);

        rankTree = new int[4 * size];
        sumTree = new long[4 * size];
        nameToValue = new HashMap<>();
        valueToName = new HashMap<>();
        while(!commands.isEmpty()) {
            Command command = commands.poll();

            if(command.type == INSERT) {
                String name = command.name;
                int value = command.value;
                if(nameToValue.containsKey(name) || valueToName.containsKey(value)) sb.append(0).append('\n');
                else {
                    // dict 업데이트
                    nameToValue.put(name, value);
                    valueToName.put(value, name);
                    // tree 업데이트
                    int seq = numberToSeq.get(value);
                    updateRankTree(1, 1 , size, seq, 1);
                    updateSumTree(1, 1, size, seq, value);
                    // 출력
                    sb.append(1).append('\n');
                }
            }
            else if(command.type == DELETE) {
                String name = command.name;
                if(!nameToValue.containsKey(name)) sb.append(0).append('\n');
                else {
                    int value = nameToValue.get(name);
                    // dict 업데이트
                    nameToValue.remove(name);
                    valueToName.remove(value);
                    // tree 업데이트
                    int seq = numberToSeq.get(value);
                    updateRankTree(1, 1 , size, seq, 0);
                    updateSumTree(1, 1, size, seq, 0);
                    // 출력
                    sb.append(value).append('\n');
                }
            }
            else if(command.type == RANK) {
                if(size == 0 || rankTree[1] < command.k) {
                    sb.append("None").append('\n');
                } else {
                    int seq = queryRankTree(1, 1, size, command.k);
                    int number = seqToNumber.get(seq);
                    sb.append(valueToName.get(number)).append('\n');
                }
            }
            else if(command.type == SUM) {
                if(size == 0) {
                    sb.append(0).append('\n');
                } else {
                    int upperSeq = getUpperSeq(command.k);
                    long sum = querySumTree(1, 1, size, 1, upperSeq - 1);
                    sb.append(sum).append('\n');
                }
            }
        }
    }

    static int getUpperSeq(int k) {
        int l = 1, r = size + 1;

        while(l < r) {
            int mid = l + (r - l) / 2;
            if(seqToNumber.get(mid) <= k) l = mid + 1;
            else r = mid;
        }

        return l;
    }

    static void updateRankTree(int id, int l, int r, int targetId, int value) {
        if(l > targetId || r < targetId) return;
        if(l == r) {
            rankTree[id] = value;
            return;
        }
        int mid = l + (r - l) / 2;
        updateRankTree(2 * id, l, mid, targetId, value);
        updateRankTree(2 * id + 1, mid + 1, r, targetId, value);
        rankTree[id] = rankTree[2 * id] + rankTree[2 * id + 1];
    }

    static void updateSumTree(int id, int l, int r, int targetId, int value) {
        if(l > targetId || r < targetId) return;
        if(l == r) {
            sumTree[id] = value;
            return;
        }
        int mid = l + (r - l) / 2;
        updateSumTree(2 * id, l, mid, targetId, value);
        updateSumTree(2 * id + 1, mid + 1, r, targetId, value);
        sumTree[id] = sumTree[2 * id] + sumTree[2 * id + 1];
    }

    static int queryRankTree(int id, int l, int r, int k) {
        if(l == r) return l;
        int mid = l + (r - l) / 2;
        int lCount = rankTree[2 * id];
        if(lCount >= k) return queryRankTree(2 * id, l, mid, k);
        return queryRankTree(2 * id + 1, mid + 1, r, k - lCount);
    }

    static long querySumTree(int id, int l, int r, int s, int e) {
        if(l > e || r < s) return 0;
        if(s <= l && e >= r) return sumTree[id];
        int mid = l + (r - l) / 2;
        long lSum = querySumTree(2 * id, l, mid, s, e);
        long rSum = querySumTree(2 * id + 1, mid + 1, r, s, e);
        return lSum + rSum;
    }

    static void flat(Queue<Command> commands) {
        size = 0;
        values = new ArrayList<>();
        for(Command command : commands) {
            if(command.type == INSERT) values.add(command.value);
        }

        Collections.sort(values);
        numberToSeq = new HashMap<>();
        seqToNumber = new HashMap<>();
        for(int i : values) {
            if(numberToSeq.containsKey(i)) continue;
            numberToSeq.put(i, ++size);
            seqToNumber.put(size, i);
        }
    }
}
