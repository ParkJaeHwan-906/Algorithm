package jul.week2.codetree;

import java.util.*;
import java.io.*;

public class 외주수익최대화하기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static class Work {
        int s, e;
        int p;

        Work(int s, int e, int p) {
            this.s = s;
            this.e = e;
            this.p = p;
        }
    }

    static int n;
    static List<Work> works;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        n = Integer.parseInt(br.readLine().trim());
        works = new ArrayList<>();
        for(int s = 1; s <= n; s++) {
            st = new StringTokenizer(br.readLine().trim());
            int during = Integer.parseInt(st.nextToken());
            int p = Integer.parseInt(st.nextToken());
            int e = s + during - 1;
            Work work = new Work(s, e, p);
            works.add(work);
        }
        System.out.println(solution());
    }

    static int maxProfit;
    static int solution() {
        /**
         * 일은 동시에 처리하지 못함
         * => 최대 수익을 얻을 수 있는 경우
         */
        maxProfit = 0;
        findMaxProfit(1, 0);
        return maxProfit;
    }

    static void findMaxProfit(int day, int profit) {
        if(day > n) {
            maxProfit = Math.max(maxProfit, profit);
            return;
        }

        Work work = works.get(day - 1);     // 시작할 수 있는 날짜

        // 업무하지 않는 경우
        findMaxProfit(day + 1, profit);
        // 업무 하는 경우
        if(work.e <= n) findMaxProfit(work.e + 1, profit + work.p);
    }
}
