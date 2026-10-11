package oct.week1.codetree;

import java.util.*;
import java.io.*;

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

    static class Smell {
        int id;
        int s;
        boolean removed;
        Smell(int id, int s) {
            this.id = id;
            this.s = s;
            this.removed = false;
        }
        Smell(boolean removed) {
            this.id = -1;
            this.s = -1;
            this.removed = removed;
        }
    }

    static final Smell DUMMY = new Smell(true);

    static int n;
    static List<Smell> smells;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            if(type == SET) {
                set(st);
            }
            else if(type == ADD) {
                add(st);
            }
            else if(type == DEL) {
                sb.append(del(st)).append('\n');
            }
            else if(type == BLAND) {
                sb.append(bland(st)).append('\n');
            }
            else if(type == COMPOSE) {
                sb.append(compose(st)).append('\n');
            }
        }
        System.out.print(sb);
    }

    static void set(StringTokenizer st) {
        n = Integer.parseInt(st.nextToken());
        smells = new ArrayList<>();
        smells.add(DUMMY);              // 1 - based 처리
        for(int i = 1; i <= n; i++) {
            Smell smell = new Smell(
                    i,
                    Integer.parseInt(st.nextToken())
            );
            smells.add(smell);
        }
    }

    static void add(StringTokenizer st) {
        int s = Integer.parseInt(st.nextToken());
        Smell smell = new Smell(smells.size(), s);
        smells.add(smell);
    }

    static int del(StringTokenizer st) {
        int id = Integer.parseInt(st.nextToken());
        if(id >= smells.size()) {
            return -1;
        }
        if(smells.get(id).removed) {
            return -1;
        }
        smells.get(id).removed = true;
        return smells.get(id).s;
    }

    static int bland(StringTokenizer st) {
        int k = Integer.parseInt(st.nextToken());
        int[] dp = new int[k + 1];
        Arrays.fill(dp, Integer.MAX_VALUE / 2);
        dp[0] = 0;
        for(Smell smell : smells) {
            if(smell.removed) {
                continue;
            }
            for(int i = smell.s; i <= k; i++) {
                dp[i] = Math.min(dp[i], dp[i - smell.s] + 1);
            }
        }
        return dp[k] == Integer.MAX_VALUE / 2
                ? -1 : dp[k];
    }

    static int compose(StringTokenizer st) {
        int k = Integer.parseInt(st.nextToken());
        List<Smell> availableSmells = new ArrayList<>();
        for(Smell smell : smells) {
            if(smell.removed) {
                continue;
            }
            availableSmells.add(smell);
        }
        int totalCount = 0;
        availableSmells.sort((a, b) -> Integer.compare(a.s, b.s));
        for(int i = 0; i < availableSmells.size(); i++) {
            for(int j = 0; j < availableSmells.size(); j++) {
                int sum = availableSmells.get(i).s + availableSmells.get(j).s;
                int idx = lowerIdx(availableSmells, k - sum);
                totalCount += (availableSmells.size() - idx);
            }
        }
        return totalCount;
    }

    static int lowerIdx(List<Smell> list, int target) {
        /**
         * target 보다 크거나 같은 가장 작은 수
         */
        int l = 0, r = list.size();
        while(l < r) {
            int mid = l + (r - l) / 2;
            if(list.get(mid).s >= target) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }
}
