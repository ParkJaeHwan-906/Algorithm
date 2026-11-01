package oct.week4.leetcode.CourseSchedule_박재환;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class CourseSchedule_DFS사이클_박재환 {
    public static void main(String[] args) {
        Solution2 solution = new Solution2();

        System.out.println(solution.canFinish(2, new int[][]{{1, 0}})); // 예상: true
        System.out.println(solution.canFinish(2, new int[][]{{1, 0}, {0, 1}})); // 예상: false
    }
}


class Solution2 {
    int[] state;
    List<Integer>[] connections;
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        set(numCourses, prerequisites);
        for (int i = 0; i < numCourses; i++) {
            if (state[i] == 0 && findCycle(i)) {
                return false;
            }
        }
        return true;
    }

    void set(int numCourses, int[][] prerequisites) {
        state = new int[numCourses];
        connections = new List[numCourses];
        for(int i = 0; i < numCourses; i++)
            connections[i] = new ArrayList<>();
        for(int[] prerequest : prerequisites) {
            int cur = prerequest[0];
            int pre = prerequest[1];
            connections[pre].add(cur);
        }
    }

    boolean findCycle(int cur) {
        if (state[cur] == 1) {
            return true;
        }
        if (state[cur] == 2) {
            return false;
        }
        state[cur] = 1;
        for (int next : connections[cur]) {
            if (findCycle(next)) {
                return true;
            }
        }
        state[cur] = 2;
        return false;
    }
}