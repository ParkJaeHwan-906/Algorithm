package sep.week2.programmers.디스크컨트롤러_박재환;

import java.util.PriorityQueue;

public class 디스크컨트롤러_박재환 {
    public static void main(String[] args) {
        int[][] jobs = {
                {0, 3},
                {1, 9},
                {3, 5}
        };

        Solution sol = new Solution();
        System.out.println(sol.solution(jobs));
    }
}

class Solution {
    class Task implements Comparable<Task> {
        int id;
        int inTime;
        int needTime;
        Task(int id, int inTime, int needTime) {
            this.id = id;
            this.inTime = inTime;
            this.needTime = needTime;
        }

        @Override
        public int compareTo(Task o) {
            if(this.needTime != o.needTime) {
                return Integer.compare(this.needTime, o.needTime);
            }
            if(this.inTime != o.inTime) {
                return Integer.compare(this.inTime, o.inTime);
            }
            return Integer.compare(this.id, o.id);
        }
    }

    public int solution(int[][] jobs) {
        int[] returnTime = new int[jobs.length];
        PriorityQueue<Task> taskPq = set(jobs);
        PriorityQueue<Task> waitPq = new PriorityQueue<>();
        int curTime = 0;
        while(!taskPq.isEmpty() || !waitPq.isEmpty()) {
            while(!taskPq.isEmpty() && curTime >= taskPq.peek().inTime) {
                waitPq.offer(taskPq.poll());
            }
            if(waitPq.isEmpty() && !taskPq.isEmpty()) {
                curTime = taskPq.peek().inTime;
                continue;
            }
            Task task = waitPq.poll();
            int finishTime = curTime + task.needTime;
            returnTime[task.id] = finishTime - task.inTime;
            curTime = finishTime;

        }
        return getAvgReturnTime(returnTime);
    }

    PriorityQueue<Task> set(int[][] jobs) {
        PriorityQueue<Task> taskPq = new PriorityQueue<>((a, b) -> Integer.compare(a.inTime, b.inTime));
        for(int i = 0; i < jobs.length; i++) {
            taskPq.offer(new Task(i, jobs[i][0], jobs[i][1]));
        }
        return taskPq;
    }

    int getAvgReturnTime(int[] returnTime) {
        int sum = 0;
        for(int i = 0; i < returnTime.length; i++) {
            sum += returnTime[i];
        }
        return sum / returnTime.length;
    }
}