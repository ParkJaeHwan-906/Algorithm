package oct.week4.programmers.금과은운반하기_박재환;

import java.util.*;

public class 금과은운반하기_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        System.out.println(solution.solution(
                10, 10,
                new int[]{100}, new int[]{100},
                new int[]{7}, new int[]{10}
        )); // 예상: 50

        System.out.println(solution.solution(
                90, 500,
                new int[]{70, 70, 0}, new int[]{0, 0, 500},
                new int[]{100, 100, 2}, new int[]{4, 8, 1}
        )); // 예상: 499
    }
}

class Solution {

    class City {
        int gold;
        int silver;
        City(int gold, int silver) {
            this.gold = gold;
            this.silver = silver;
        }
    }

    class Truck {
        int weight;
        int time;
        Truck(int weight, int time) {
            this.weight = weight;
            this.time = time;
        }
    }

    int n;
    int maxTime;
    City[] cities;
    Truck[] trucks;
    public long solution(int a, int b, int[] g, int[] s, int[] w, int[] t) {
        set(g, s, w, t);
        long minTime = Long.MAX_VALUE;
        long l = 0, r = ((long)a + b) * 2 * maxTime;
        while(l <= r) {
            long mid = l + (r - l) / 2;
            if(isPossible(mid, a, b)) {
                r = mid - 1;
                minTime = Math.min(minTime, mid);
            } else {
                l = mid + 1;
            }
        }
        return minTime;
    }

    boolean isPossible(long mid, int a, int b) {
        long totalGold = 0;
        long totalSilver = 0;
        long totalLoad = 0;
        for(int i = 0; i < n; i++) {
            long repeatableCount = (mid / ((long)trucks[i].time * 2))
                    + (mid % ((long)trucks[i].time * 2) >= trucks[i].time ? 1 : 0);
            totalGold += Math.min(cities[i].gold, trucks[i].weight * repeatableCount);
            totalSilver += Math.min(cities[i].silver, trucks[i].weight  * repeatableCount);
            totalLoad += Math.min((long)cities[i].silver + cities[i].gold, trucks[i].weight  * repeatableCount);

            if(totalGold >= a
                && totalSilver >= b
                && totalLoad >= a + b)
                return true;
        }
        return totalGold >= a && totalSilver >= b && totalLoad >= (long) a + b;
    }

    void set(int[] g, int[] s, int[] w, int[] t) {
        this.maxTime = 0;
        this.n = g.length;
        this.cities = new City[n];
        this.trucks = new Truck[n];
        for(int i = 0; i < n; i++ ) {
            this.cities[i] = new City(g[i], s[i]);
            this.trucks[i] = new Truck(w[i], t[i]);
            maxTime = Math.max(maxTime, trucks[i].time);
        }
    }
}
