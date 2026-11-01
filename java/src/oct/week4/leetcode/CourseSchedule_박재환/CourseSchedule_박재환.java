package oct.week4.leetcode.CourseSchedule_박재환;

import java.util.*;

public class CourseSchedule_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        System.out.println(solution.canFinish(2, new int[][]{{1, 0}})); // 예상: true
        System.out.println(solution.canFinish(2, new int[][]{{1, 0}, {0, 1}})); // 예상: false
    }
}

class Solution {
    int[] inEdges;
    List<Integer>[] connections;
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        set(numCourses, prerequisites);
        Queue<Integer> q = new ArrayDeque<>();
        for(int i = 0; i < numCourses; i++) {
            if(inEdges[i] == 0)
                q.offer(i);
        }
        while(!q.isEmpty()) {
            int cur = q.poll();
            for(int next : connections[cur]) {
                if(--inEdges[next] == 0)
                    q.offer(next);
            }
            numCourses--;
        }
        return numCourses == 0;
    }

    void set(int numCourses, int[][] prerequisites) {
        inEdges = new int[numCourses];
        connections = new List[numCourses];
        for(int i = 0; i < numCourses; i++)
            connections[i] = new ArrayList<>();
        for(int[] prerequest : prerequisites) {
            int cur = prerequest[0];
            int pre = prerequest[1];
            inEdges[cur]++;
            connections[pre].add(cur);
        }
    }
}