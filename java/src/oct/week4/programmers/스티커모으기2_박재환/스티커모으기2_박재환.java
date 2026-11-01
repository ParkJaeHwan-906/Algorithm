package oct.week4.programmers.스티커모으기2_박재환;

public class 스티커모으기2_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        System.out.println(solution.solution(new int[]{14, 6, 5, 11, 3, 9, 2, 10})); // 예상: 36
        System.out.println(solution.solution(new int[]{1, 3, 2, 5, 4})); // 예상: 8
    }
}

class Solution {
    int n;
    public int solution(int sticker[]) {
        set(sticker);

        if(n < 3) {
            return max(sticker);
        }

        int[] caseSelectedFirst = new int[n];
        int[] caseNonselectedFirst = new int[n];
        caseSelectedFirst[0] = sticker[0];
        caseSelectedFirst[1] = sticker[0];
        caseNonselectedFirst[1] = sticker[1];
        for(int i = 2; i < n; i++) {
            if(i < n - 1) {
                caseSelectedFirst[i] = Math.max(
                        caseSelectedFirst[i - 2] + sticker[i],
                        caseSelectedFirst[i - 1]
                );
            }
            caseNonselectedFirst[i] = Math.max(
                    caseNonselectedFirst[i - 2] + sticker[i],
                    caseNonselectedFirst[i - 1]
            );
        }
        return Math.max(caseNonselectedFirst[n - 1], caseSelectedFirst[n - 2]);
    }

    void set(int[] sticker) {
        this.n = sticker.length;
    }

    int max(int[] best) {
        int max = best[0];
        for(int i = 1; i < n; i++) {
            max = Math.max(max, best[i]);
        }
        return max;
    }
}
