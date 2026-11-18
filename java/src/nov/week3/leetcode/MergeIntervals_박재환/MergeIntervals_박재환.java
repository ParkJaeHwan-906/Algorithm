package nov.week3.leetcode.MergeIntervals_박재환;

import java.util.*;

public class MergeIntervals_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        int[][] intervals1 = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        System.out.println(Arrays.deepToString(solution.merge(intervals1))); // [[1, 6], [8, 10], [15, 18]]

        int[][] intervals2 = {{1, 4}, {4, 5}};
        System.out.println(Arrays.deepToString(solution.merge(intervals2))); // [[1, 5]]

        int[][] intervals3 = {{4, 7}, {1, 4}};
        System.out.println(Arrays.deepToString(solution.merge(intervals3))); // [[1, 7]]
    }
}

class Solution {
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        List<int[]> list = new ArrayList<>();
        int i = 0;
        while(i < intervals.length) {
            int start = intervals[i][0];
            int end = intervals[i][1];

            int j = i + 1;
            for (; j < intervals.length; j++) {
                int nextStart = intervals[j][0];
                int nextEnd = intervals[j][1];
                if (nextStart > end) {
                    break;
                }
                start = Math.min(start, nextStart);
                end = Math.max(end, nextEnd);
            }
            list.add(new int[] {start, end});
            i = j;
        }
        int[][] result = new int[list.size()][];
        for(i = 0; i < result.length; i++) {
            result[i] = list.get(i);
        }
        return result;
    }
}
