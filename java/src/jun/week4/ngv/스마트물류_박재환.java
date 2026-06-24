package jun.week4.ngv;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:18:49
 * AI 사용 여부 X
 */
public class 스마트물류_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n, k;
    static String line;
    static List<Integer> robots;
    static List<Integer> items;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 라인 길이
        k = Integer.parseInt(st.nextToken());       // 가동 범위

        line = br.readLine().trim();
        robots = new ArrayList<>();
        items = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            if(line.charAt(i) == 'P') {
                robots.add(i);
            } else if(line.charAt(i) == 'H') {
                items.add(i);
            }
        }

        System.out.println(solution());
    }

    static int solution() {
        int robotCount = 0;
        // 최대한 한 로봇이 1개씩 집는 것이 최대?
        int robotId = 0, itemId = 0;
        while(robotId < robots.size() &&  itemId < items.size()) {
            int r =  robots.get(robotId);
            int i = items.get(itemId);

            if(Math.abs(r - i) <= k) {      // 현재 로봇이 집을 수 있음
                robotId++;
                itemId++;
                robotCount++;
            } else if(r < i) {      // 집을 수 없고, 아이템이 너무 먼 경우 -> 다음 로봇
                robotId++;
            } else if(r > i) {      // 집을 수 없고, 로봇이 너무 먼 경우 -> 다음 아이템
                itemId++;
            }
        }
        return  robotCount;
    }
}
