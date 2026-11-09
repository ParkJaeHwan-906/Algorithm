package nov.week1.leetcode.Subsets_박재환.Subsets_비트마스킹_박재환;

import java.util.*;

public class Subsets_비트마스킹_박재환 {
    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println(sol.subsets(new int[]{1, 2, 3}));
        System.out.println(sol.subsets(new int[]{0}));
    }
}

class Solution {

    int n;
    int[] nums;
    List<List<Integer>> result;
    public List<List<Integer>> subsets(int[] nums) {
        set(nums);
        result = new ArrayList<>();
        findSubsets(0, 0);
        return result;
    }

    void set(int[] nums) {
        this.n = nums.length;
        this.nums = nums;
    }

    void findSubsets(int id, int state) {
        if(id == n) {
            List<Integer> list = new ArrayList<>();
            for(int i = 0; i < n; i++) {
                if(((1 << i) & state) != 0)
                    list.add(nums[i]);
            }
            result.add(list);
            return;
        }
        findSubsets(id + 1, state | (1 << id));
        findSubsets(id + 1, state);
    }
}