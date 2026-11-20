package nov.week3.leetcode.MinimumNumberofDaystoMakemBouquets_박재환;

public class MinimumNumberofDaystoMakemBouquets_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        int[] bloomDay1 = {1, 10, 3, 10, 2};
        System.out.println("Example 1: " + solution.minDays(bloomDay1, 3, 1) + " (expected: 3)");

        int[] bloomDay2 = {1, 10, 3, 10, 2};
        System.out.println("Example 2: " + solution.minDays(bloomDay2, 3, 2) + " (expected: -1)");

        int[] bloomDay3 = {7, 7, 7, 7, 12, 7, 7};
        System.out.println("Example 3: " + solution.minDays(bloomDay3, 2, 3) + " (expected: 12)");
    }
}

class Solution {
    public int minDays(int[] bloomDay, int m, int k) {
        int l = Integer.MAX_VALUE;
        int r = Integer.MIN_VALUE;
        for(int i : bloomDay) {
            l = Math.min(l, i);
            r = Math.max(r, i);
        }

        int minDay = Integer.MAX_VALUE;
        while(l <= r) {
            int mid = l + (r - l) / 2;
            if(isAvaliable(bloomDay, mid, m, k)) {
                minDay = Math.min(minDay, mid);
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }

        return minDay == Integer.MAX_VALUE ? -1 : minDay;
    }

    boolean isAvaliable(int[] bloomDay, int day, int m, int k) {
        int flower = 0;
        int bundle = 0;
        for(int i : bloomDay) {
            if(i <= day) {
                if(++flower == k) {
                    flower = 0;
                    if(++bundle == m)
                        return true;
                }
            } else {
                flower = 0;
            }
        }
        return false;
    }
}