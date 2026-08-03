package jun.week4.ngv;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 한 20분?
  AI 사용 여부: O -> dp 알고리즘인가..? 했는데 AI가 그리디라고 알려줬음..
 */
public class 스마트물류_박서희 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int K = sc.nextInt();

        String s = sc.next();
        char[] arr = s.toCharArray();

        int hIdx = -1;
        int answer = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 'H') continue;

            int left = Math.max(hIdx + 1, i - K);
            int right = Math.min(i + K, N - 1);
            for (int j = left; j <= right; j++) {
                if (arr[j] == 'H') {
                    hIdx = j;
                    answer++;
                    break;
                }
            }
        }
        System.out.println(answer);
    }
}
