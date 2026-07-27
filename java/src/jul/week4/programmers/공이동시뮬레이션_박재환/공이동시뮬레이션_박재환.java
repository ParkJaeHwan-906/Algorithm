package jul.week4.programmers.공이동시뮬레이션_박재환;

import java.util.*;

public class 공이동시뮬레이션_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        int[][] queries1 = {
                {2, 1}, {0, 1}, {1, 1}, {0, 1}, {2, 1}
        };
        System.out.println(solution.solution(2, 2, 0, 0, queries1));

        int[][] queries2 = {
                {3, 1}, {2, 2}, {1, 1}, {2, 3}, {0, 1}, {2, 1}
        };
        System.out.println(solution.solution(2, 5, 0, 1, queries2));
    }
}

class Solution {
    static final int DECREASE_Y = 0;
    static final int INCREASE_Y = 1;
    static final int DECREASE_X = 2;
    static final int INCREASE_X = 3;
    public long solution(int n, int m, int x, int y, int[][] queries) {
        long minX = x;
        long maxX = x;
        long minY = y;
        long maxY = y;

        for(int qId = queries.length - 1; qId >= 0; qId--) {
            int[] query = queries[qId];
            int type = query[0];
            int dist = query[1];

            if(type == DECREASE_Y) {
                if(minY != 0) minY += dist;
                maxY = Math.min(m - 1, maxY + dist);
            }

            else if(type == INCREASE_Y) {
                minY = Math.max(0, minY - dist);
                if(maxY != m - 1) maxY -= dist;
            }

            else if(type == DECREASE_X) {
                if(minX != 0) minX += dist;
                maxX = Math.min(n - 1, maxX + dist);
            }

            else if(type == INCREASE_X) {
                minX = Math.max(0, minX - dist);
                if(maxX != n - 1) maxX -= dist;
            }

            if(minX > maxX || minY > maxY) {
                return 0;
            }
        }
        return (maxX - minX + 1)
                * (maxY - minY + 1);
    }
}
