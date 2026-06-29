package jun.week5.programmers.연속펄스부분수열의합_박재환;

/**
 * [풀이 시간]
 * 00:18:49
 * AI 사용 여부 O
 * -> sequence의 길이가 50만 -> O(n ** 2) 풀이는 불가하다고 판단
 *      -> 아이디어 질문
 */
public class 연속펄스부분수열의합_박재환 {
    public static void main(String[] args) {
        int[] sequence = {2, 3, -6, 1, 3, -1, 2, 4};
        Solution solution = new Solution();
        System.out.println(solution.solution(sequence));
    }
}

class Solution {
    int n;
    long[] plus, minus;     // 펄스의 종류는 2가지
    public long solution(int[] sequence) {
        set(sequence);

        long pMax = plus[0];
        long[] accPlus = new long[n];
        accPlus[0] = pMax;
        long mMax = minus[0];
        long[] accMinus = new long[n];
        accMinus[0] = mMax;
        for(int i = 1; i < n; i++) {
            accPlus[i] = Math.max(plus[i], accPlus[i - 1] + plus[i]);
            accMinus[i] = Math.max(minus[i], accMinus[i - 1] + minus[i]);

            pMax = Math.max(pMax, accPlus[i]);
            mMax = Math.max(mMax, accMinus[i]);
        }

        return Math.max(pMax, mMax);
    }

    void set(int[] arr) {
        n = arr.length;
        plus = new long[n];
        minus = new long[n];

        int f = 1;
        for(int i = 0; i < n; i++) {
            plus[i] = arr[i] * f;
            minus[i] = arr[i] * f * -1;
            f *= -1;
        }
    }
}