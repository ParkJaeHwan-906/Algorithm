package oct.week3.leetcode;

import java.util.*;

public class MinimumInitialEnergyToFinishTasks_박재환 {
    public static void main(String[] args) {
        Solution sol = new Solution();
        int[][][] examples = {
                {{1, 2}, {2, 4}, {4, 8}},
                {{1, 3}, {2, 4}, {10, 11}, {10, 12}, {8, 9}},
                {{1, 7}, {2, 8}, {3, 9}, {4, 10}, {5, 11}, {6, 12}}
        };

        for (int[][] tasks : examples) {
            System.out.println(sol.minimumEffort(tasks));
        }
    }
}


class Solution {
    class Task implements Comparable<Task> {
        int spend;
        int need;
        Task(int spend, int need) {
            this.spend = spend;
            this.need = need;
        }
        @Override
        public int compareTo(Task o) {
            return Integer.compare(o.need - o.spend, this.need - this.spend);
        }
    }

    public int minimumEffort(int[][] tasks) {
        int minStartEnergy = 0;                 // 필요한 최소 에너지
        int spentSum = 0;                       // 누적 사용
        PriorityQueue<Task> taskPq = set(tasks);
        while(!taskPq.isEmpty()) {
            Task cur = taskPq.poll();
            minStartEnergy = Math.max(minStartEnergy, spentSum + cur.need);
            spentSum += cur.spend;
        }
        return minStartEnergy;
    }

    PriorityQueue<Task> set(int[][] tasks) {
        PriorityQueue<Task> taskPq = new PriorityQueue<>();
        for(int[] task : tasks) {
            int spend = task[0];
            int need = task[1];
            taskPq.offer(new Task(spend, need));
        }
        return taskPq;
    }
}
