package jul.week4.jungol;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:25:58
 * AI 사용 여부 O
 * => 처음에는 DFS 로 탐색했는데, 시간 초과가 발생
 * => DP 로 변경
 */
public class 돌다리건너기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static String required;
    static String devil;
    static String angel;

    static int[][][] history;

    static void init(BufferedReader br) throws IOException {
        required = br.readLine().trim();
        devil = br.readLine().trim();
        angel = br.readLine().trim();

        history = new int[2][devil.length() + 1][required.length() + 1];
        for(int[][] arr : history) {
            for(int[] a : arr) Arrays.fill(a, -1);
        }
        findRoute(0, 0, 0);
        findRoute(1, 0, 0);

        System.out.println(findRoute(0, 0, 0) + findRoute(1, 0, 0));
    }

    static int findRoute(int type, int loc, int pointer) {
        if(pointer == required.length()) {
            return 1;
        }
        if(history[type][loc][pointer] != -1) return history[type][loc][pointer];

        int routeCount = 0;
        char curTarget = required.charAt(pointer);
        if(type == 0) {         // 악마
            for(int i = loc; i < devil.length(); i++) {
                if(devil.length() - i < required.length() - pointer) break;
                if(devil.charAt(i) == curTarget) {
                    routeCount += findRoute(1, i + 1, pointer + 1);
                }
            }
        } else if(type == 1) {  // 천사
            for(int i = loc; i < angel.length(); i++) {
                if(angel.length() - i < required.length() - pointer) break;
                if(angel.charAt(i) == curTarget) {
                    routeCount += findRoute(0, i + 1, pointer + 1);
                }
            }
        }

        return history[type][loc][pointer] = routeCount;
    }
}
