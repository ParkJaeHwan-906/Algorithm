package nov.week1.leetcode.MinimumTimetoRevertWordtoInitialStateI_박재환;

public class MinimumTimetoRevertWordtoInitialStateI_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        System.out.println("Example 1: " + solution.minimumTimeToInitialState("abacaba", 3) + " (expected: 2)");
        System.out.println("Example 2: " + solution.minimumTimeToInitialState("abacaba", 4) + " (expected: 1)");
        System.out.println("Example 3: " + solution.minimumTimeToInitialState("abcbabcd", 2) + " (expected: 4)");
    }
}

class Solution {
    public int minimumTimeToInitialState(String word, int k) {
        int time = 0;
        while(true) {
            time++;
            if(time * k >= word.length())
                break;
            String copy = word.substring(time * k);
            String compare = word.substring(0, word.length() - time * k);
            if(copy.equals(compare))
                break;
        }
        return time;
    }
}
