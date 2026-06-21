package jun.week3.codetree;

import java.util.*;
import java.io.*;

/**
 * AI 사용 여부 O
 * => 처음에 Binary Search를 Lower Bound가 아닌 Upper Bound로 설정한 것이 문제였음
 * UpperBound로 했을 때, 완벽하게 target이랑 일치하는 값에 대해서 확인할 수가 없었음
 */
public class 코디의향수공방_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int SET = 1;
    static final int ADD = 2;
    static final int DEL = 3;
    static final int BLAND = 4;
    static final int COMPOSE = 5;

    static class Perfume {
        int id;
        int power;
        boolean active;

        Perfume(int id, int power, boolean active) {
            this.id = id;
            this.power = power;
            this.active = active;
        }
    }

    static final Perfume DUMMY = new Perfume(-1, -1, false);

    static int n;
    static List<Perfume> perfumes;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();

        int q = Integer.parseInt(br.readLine().trim());

        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());

            int type = Integer.parseInt(st.nextToken());

            if(type == SET) { set(st); }
            else if(type == ADD) { add(st); }
            else if(type == DEL) {
                int result = del(st);
                sb.append(result).append("\n");
            }
            else if(type == BLAND) {
                int result = bland(st);
                sb.append(result).append("\n");
            }
            else if(type == COMPOSE) {
                int result = compose(st);
                sb.append(result).append("\n");
            }
        }

        System.out.println(sb);
    }

    static void set(StringTokenizer st) {
        n = Integer.parseInt(st.nextToken());

        perfumes = new ArrayList<>();
        perfumes.add(DUMMY);        // 0 - based

        for(int i = 1; i < n + 1; i++) {
            int power = Integer.parseInt(st.nextToken());
            Perfume perfume = new Perfume(i, power, true);
            perfumes.add(perfume);
        }
    }

    static void add(StringTokenizer st) {
        int id = perfumes.size();
        int power = Integer.parseInt(st.nextToken());
        Perfume perfume = new Perfume(id, power, true);
        perfumes.add(perfume);
    }

    static int del(StringTokenizer st) {
        int delId = Integer.parseInt(st.nextToken());

        if(delId >= perfumes.size() || !perfumes.get(delId).active) return -1;

        Perfume perfume = perfumes.get(delId);
        perfume.active = false;
        return perfume.power;
    }

    static final int INF = Integer.MAX_VALUE;
    static int bland(StringTokenizer st) {
        int k = Integer.parseInt(st.nextToken());

        int[] dp = new int[k + 1];
        Arrays.fill(dp, INF);

        dp[0] = 0;
        for(Perfume perfume : perfumes) {
            if(!perfume.active) continue;

            for(int i = perfume.power; i <= k; i++) {
                if(dp[i - perfume.power] == INF) continue;
                dp[i] = Math.min(dp[i], dp[i - perfume.power] + 1);
            }
        }

        return dp[k] == INF ? -1 : dp[k];
    }

    static int compose(StringTokenizer st) {
        int k = Integer.parseInt(st.nextToken());

        List<Perfume> activePerfumes = new ArrayList<>();
        for(int i = 1; i < perfumes.size(); i++) {
            if(!perfumes.get(i).active) continue;
            activePerfumes.add(perfumes.get(i));
        }

        activePerfumes.sort((a, b) -> Integer.compare(a.power, b.power));

        int result = 0;
        for(int top = 0; top < activePerfumes.size(); top++) {
            for(int mid = 0; mid < activePerfumes.size(); mid++) {
                int sum = activePerfumes.get(top).power + activePerfumes.get(mid).power;
                int diff = k - sum;
                if(diff <= 0) result += activePerfumes.size();
                else result += (activePerfumes.size() - findLastId(activePerfumes, diff));
            }
        }
        return result;
    }

    static int findLastId(List<Perfume> list, int target) {
        int l = 0, r = list.size();
        /**
         * target 이상인 첫 인덱스
         */
        while(l < r) {
            int mid = l + (r - l) / 2;
            if(list.get(mid).power >=  target) r = mid;
            else l = mid + 1;
        }
        return l;
    }
}
