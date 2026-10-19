
package oct.week3.programmers.피로도_박재환;

public class 피로도_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        int k = 80;
        int[][] dungeons = {
                {80, 20},
                {50, 40},
                {30, 10}
        };

        int result = solution.solution(k, dungeons);

        System.out.println("결과: " + result);
        System.out.println("기댓값: 3");
        System.out.println(result == 3 ? "PASS" : "FAIL");
    }
}

class Solution {
    int maxVisitedDungeons;

    public int solution(int k, int[][] dungeons) {
        maxVisitedDungeons = 0;
        adventure(k, dungeons, new boolean[dungeons.length], 0);
        return maxVisitedDungeons;
    }

    void adventure(int curHp, int[][] dungeons, boolean[] visited, int visitedDungeons) {
        for(int i = 0; i < dungeons.length; i++) {
            if(visited[i])
                continue;
            int needs = dungeons[i][0];
            int use = dungeons[i][1];
            if(curHp < needs)
                continue;
            visited[i] = true;
            adventure(curHp - use, dungeons, visited, visitedDungeons + 1);
            visited[i] = false;
        }
        maxVisitedDungeons = Math.max(maxVisitedDungeons, visitedDungeons);
    }
}
