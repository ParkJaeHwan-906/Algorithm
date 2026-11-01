package oct.week4.leetcode.CountofRangeSum_박재환;

import java.util.*;

public class CountofRangeSum_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.countRangeSum(new int[]{-2, 5, -1}, -2, 2)); // 3
        System.out.println(solution.countRangeSum(new int[]{0}, 0, 0)); // 1
    }
}


class Solution {

    int n;
    long[] trees;
    long[] prefix;
    long[] sorted;
    public int countRangeSum(int[] nums, int lower, int upper) {
        set(nums);
        long count = 0;
        for (long sum : prefix) {
            int s = lowerBound(sum - upper);
            int e = upperBound(sum - lower) - 1;
            if (s <= e) {
                count += query(1, 0, n - 1, s, e);
            }
            // 현재 누적 합의 등장 횟수를 1 증가
            int target = lowerBound(sum);
            update(1, 0, n - 1, target, 1);
        }
        return (int) count;
    }

    void set(int[] nums) {
        int len = nums.length;
        this.prefix = new long[len + 1];
        for (int i = 0; i < len; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        this.sorted = prefix.clone();
        Arrays.sort(sorted);
        // 중복 제거 (좌표 압축)
        int size = 0;
        for (int i = 0; i < sorted.length; i++) {
            if (size == 0 || sorted[size - 1] != sorted[i]) {
                sorted[size++] = sorted[i];
            }
        }
        this.n = size;
        this.trees = new long[4 * n];
    }

    void update(int id, int l, int r, int target, long value) {
        if (l > target || r < target)
            return;
        if (l == r) {
            trees[id] += value;
            return;
        }
        int mid = l + (r - l) / 2;
        update(2 * id, l, mid, target, value);
        update(2 * id + 1, mid + 1, r, target, value);
        trees[id] = trees[2 * id] + trees[2 * id + 1];
    }

    long query(int id, int l, int r, int s, int e) {
        if (e < l || s > r)
            return 0;
        if (s <= l && r <= e)
            return trees[id];
        int mid = l + (r - l) / 2;
        return query(2 * id, l, mid, s, e)
                + query(2 * id + 1, mid + 1, r, s, e);
    }

    int lowerBound(long target) {
        int l = 0;
        int r = n;
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (sorted[mid] < target)
                l = mid + 1;
            else
                r = mid;
        }
        return l;
    }

    int upperBound(long target) {
        int l = 0;
        int r = n;
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (sorted[mid] <= target)
                l = mid + 1;
            else
                r = mid;
        }
        return l;
    }
}
