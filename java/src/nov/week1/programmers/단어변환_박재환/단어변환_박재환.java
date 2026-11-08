package nov.week1.programmers.단어변환_박재환;

public class 단어변환_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.solution("hit", "cog",
                new String[]{"hot", "dot", "dog", "lot", "log", "cog"}));
        System.out.println(solution.solution("hit", "cog",
                new String[]{"hot", "dot", "dog", "lot", "log"}));
    }
}

class Solution {
    int minCount;
    public int solution(String begin, String target, String[] words) {
        minCount = Integer.MAX_VALUE;
        swap(begin, 0, target, words, new boolean[words.length]);
        return minCount == Integer.MAX_VALUE ? 0 : minCount;
    }

    void swap(String cur, int count, String target, String[] words, boolean[] used) {
        if(cur.equals(target)) {
            minCount = Math.min(minCount, count);
            return;
        }
        if(count >= minCount)
            return;
        for(int i = 0; i < words.length; i++) {
            if(used[i])
                continue;
            if(!canSwap(cur, words[i]))
                continue;
            used[i] = true;
            swap(words[i], count + 1, target, words, used);
            used[i] = false;
        }
    }

    boolean canSwap(String cur, String next) {
        int diff = 0;
        for(int i = 0; i < cur.length(); i++) {
            if(cur.charAt(i) == next.charAt(i))
                continue;
            if(++diff > 1)
                return false;
        }
        return true;
    }
}
