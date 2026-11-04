package nov.week1.leetcode.Subsets_박재환;

import java.util.*;

public class Subsets_박재환 {
    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println(sol.subsets(new int[]{1, 2, 3}));
        System.out.println(sol.subsets(new int[]{0}));
    }
}

class Solution {
    List<List<Integer>> resultList;
    public List<List<Integer>> subsets(int[] nums) {
        resultList = new ArrayList<>();
        makeCombination(0, nums, new ArrayList<>());
        return resultList;
    }

    void makeCombination(int id, int[] nums, List<Integer> set) {
        if(id == nums.length) {
            resultList.add(set);
            return;
        }

        makeCombination(id + 1, nums, set);
        List<Integer> newList = clone(set);
        newList.add(nums[id]);
        makeCombination(id + 1, nums, newList);

    }

    List<Integer> clone(List<Integer> list) {
        return new ArrayList<>(list);
    }
}
