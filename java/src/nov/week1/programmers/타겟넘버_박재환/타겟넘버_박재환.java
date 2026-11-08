package nov.week1.programmers.타겟넘버_박재환;

public class 타겟넘버_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.solution(new int[]{1, 1, 1, 1, 1}, 3));
        System.out.println(solution.solution(new int[]{4, 1, 2, 1}, 4));
    }
}

class Solution {
    int count;
    public int solution(int[] numbers, int target) {
        count = 0;
        makeAllCombi(0, numbers, new boolean[numbers.length], target);
        return count;
    }

    void makeAllCombi(int id, int[] numbers, boolean[] negative, int target) {
        if(id == numbers.length) {
            int sum = 0;
            for(int i = 0; i < numbers.length; i++) {
                sum += (negative[i] ? -1 : 1) * numbers[i];
            }
            if(sum == target)
                count++;
            return;
        }
        negative[id] = true;
        makeAllCombi(id + 1, numbers, negative, target);
        negative[id] = false;
        makeAllCombi(id + 1, numbers, negative, target);
    }
}
