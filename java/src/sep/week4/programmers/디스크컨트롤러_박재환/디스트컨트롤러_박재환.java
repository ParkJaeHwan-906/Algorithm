package sep.week4.programmers.디스크컨트롤러_박재환;

import java.util.*;

public class 디스트컨트롤러_박재환 {
    public static void main(String[] args) {
        int[][] jobs = {{0, 3}, {1, 9}, {3, 5}};
        Solution sol = new Solution();
        System.out.printf("%d", sol.solution(jobs));
    }
}

class Solution {
    /*
        [작업] (id, inTime, needTime)
        => 작업 요청이 들어올 때, 대기 큐로 들어감

        하드디스크가 작업을 하지 않고 있다면, 작업을 돌릴 수 있음
        - 우선 순위가 높은 작업
            - 소요시간이 짧은 순
            - 요청 시간이 빠른 순
            - 작업 번호가 작은 순

        (순서) 하드디스크 작업 마무리 -> 대기 작업 할당
    */
    class Task {
        int taskId;
        int inTime;
        int needTime;

        int startTime;
        int endTime;
        Task(int taskId, int inTime, int needTime) {
            this.taskId = taskId;
            this.inTime = inTime;
            this.needTime = needTime;
            this.startTime = -1;
            this.endTime = -1;
        }
    }

    public int solution(int[][] jobs) {
        PriorityQueue<Task> tasks = new PriorityQueue<>((a, b) -> Integer.compare(a.inTime, b.inTime));     // 아직 들어오지 않은 작업들
        PriorityQueue<Task> waitTasks = new PriorityQueue<>((a, b) -> {
            if(a.needTime != b.needTime) {
                return Integer.compare(a.needTime, b.needTime);
            }
            if(a.inTime != b.inTime) {
                return Integer.compare(a.inTime, b.inTime);
            }
            return Integer.compare(a.taskId, b.taskId);
        }); // 대기 큐에 들어온 작업들
        set(tasks, jobs);
        // printState(tasks);
        int totalTime = 0;
        int time = 0;
        Task hdd = null;
        while(!tasks.isEmpty() || !waitTasks.isEmpty()) {
            // 1. 현재 진행 중인 업무가 있는지 확인
            if(hdd != null) {
                time = hdd.endTime;
                totalTime += (hdd.endTime - hdd.inTime);
                hdd = null;
                continue;
            }

            // 현재 시간 기점으로 대기 큐에 작업 삽입
            while(!tasks.isEmpty() && tasks.peek().inTime <= time) {
                waitTasks.offer(tasks.poll());
            }
            // 만약 대기 큐에 작업이 없다면 시간 점프
            if(waitTasks.isEmpty()) {
                time = tasks.peek().inTime;
                continue;
            }
            // System.out.printf("curTime : %d\n", time);
            // System.out.printf("tasks\n");
            // printState(tasks);
            // System.out.printf("waitTasks\n");
            // printState(waitTasks);
            // 대기 큐에 작업이 있음

            // 2. 대기중인 업무가 없다면 할당
            Task task = waitTasks.poll();
            hdd = task;
            hdd.endTime = time + hdd.needTime;
        }
        return (totalTime + (hdd.endTime - hdd.inTime)) / jobs.length;
    }

    void set(PriorityQueue<Task> tasks, int[][] jobs) {
        for(int i = 0; i < jobs.length; i++) {
            Task task = new Task(i + 1, jobs[i][0], jobs[i][1]);
            tasks.offer(task);
        }
    }

    void printState(PriorityQueue<Task> pq) {
        for(Task t : pq) {
            System.out.printf("[Task Id : %d, inTime : %d, needTime : %d]\n", t.taskId, t.inTime, t.needTime);
        }
    }
}