package apr.week2.codetree;

import java.util.*;
import java.io.*;

public class 코디의향수공방_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static final int SET = 1;
    static final int ADD = 2;
    static final int DEL = 3;
    static final int QUERY_1 = 4;
    static final int QUERY_2 = 5;

    static StringTokenizer st;
    static void init() throws IOException {
        int q = Integer.parseInt(br.readLine().trim());

        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());

            if(cmd == SET) { set(); }
            else if(cmd == ADD) { add(); }
            else if(cmd == DEL) {
                int result = del();
                sb.append(result).append('\n');
            }
            else if(cmd == QUERY_1) {
                int result = query1();
                sb.append(result).append('\n');
            }
            else if(cmd == QUERY_2) {
                int result = query2();
                sb.append(result).append('\n');
            }
        }
    }

    static class Perfume {
        int power;
        boolean removed;

        Perfume(int power) {
            this.power = power;
            this.removed = false;
        }
    }
    static int n;
    static List<Perfume> perfumes;
    static void set() {
        perfumes = new ArrayList<>();
        n = Integer.parseInt(st.nextToken());
        // 1 - based 맞추기
        perfumes.add(new Perfume(-1));
        for(int i = 0; i < n; i++) {
            int power = Integer.parseInt(st.nextToken());
            Perfume p = new Perfume(power);
            perfumes.add(p);
        }
    }
    static void add() {
        int power = Integer.parseInt(st.nextToken());
        Perfume p = new Perfume(power);
        perfumes.add(p);
        n++;
    }
    static int del() {
        int id = Integer.parseInt(st.nextToken());
        // 존재하지 않거나, 이미 폐기된 향수
        if(id > n || perfumes.get(id).removed) return -1;
        Perfume p = perfumes.get(id);
        p.removed = true;
        return p.power;
    }
    static int query1() {
        int k = Integer.parseInt(st.nextToken());
        /**
         * 정확히 k 가 되도록 향료를 선택
         * 필요한 형료의 최소 개수 출력
         * 같은 번호의 향료 여러번 사용 가능
         */
        int[] dp = new int[k + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for(int i = 1; i <= n; i++) {
            Perfume p = perfumes.get(i);
            if(p.removed) continue;
            for(int v = p.power; v <= k; v++) {
                if(dp[v - p.power] == Integer.MAX_VALUE) continue;      // 만들 수 있는 방법 X

                if(dp[v] > dp[v - p.power] + 1) {
                    dp[v] = dp[v - p.power] + 1;
                }
            }
        }
        return dp[k] == Integer.MAX_VALUE ? -1 : dp[k];
    }
    static int query2() {
        int k = Integer.parseInt(st.nextToken());

        List<Perfume> availablePerfumes = new ArrayList<>();
        for(int i = 1; i <= n; i++) {
            if(perfumes.get(i).removed) continue;
            availablePerfumes.add(perfumes.get(i));
        }

        int size = availablePerfumes.size();
        Collections.sort(availablePerfumes,
                (a, b) -> Integer.compare(a.power, b.power));

        int count = 0;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                int sum = availablePerfumes.get(i).power +
                        availablePerfumes.get(j).power;

                // k 이상이 되기까지 남은 power
                int diff = k - sum;
                int lastId = findLastId(availablePerfumes, diff);
                if(lastId < size) count += (size - lastId);
            }
        }
        return count;
    }
    static int findLastId(List<Perfume> list, int target) {
        // target 보다 크거나 같은 가장 작은 값
        int l = 0, r = list.size();
        while(l < r) {
            int mid = l + (r - l) / 2;
            if(list.get(mid).power >= target) r = mid;
            else l = mid + 1;
        }
        return l;
    }
}
