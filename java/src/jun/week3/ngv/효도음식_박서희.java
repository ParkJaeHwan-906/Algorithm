package jun.week3.ngv;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 00:35:13
  AI 사용 여부: O 무조건 사용하지 않은 재료를 고정하고 반복문 돌리는 것까지는 생각했지만 양 옆에 연속합 최대값을 구하는 부분을 생각하기 어려웠음.
 */
public class 효도음식_박서희 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());

        int[] arr = new int[N];
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        int[] left = new int[N];
        left[0] = arr[0];
        int leftMax = arr[0];
        for (int i = 1; i < N; i++) {
            leftMax = Math.max(leftMax + arr[i], arr[i]);
            left[i] = Math.max(left[i - 1], leftMax);
        }

        int[] right = new int[N];
        right[N - 1] = arr[N - 1];
        int rightMax = arr[N - 1];
        for (int i = N - 2; i >= 0; i--) {
            rightMax = Math.max(rightMax + arr[i], arr[i]);
            right[i] = Math.max(right[i + 1], rightMax);
        }

        int answer = Integer.MIN_VALUE;
        //사용 안하는 재료 하나 무조건 고정
        for (int i = 1; i < N - 1; i++) {
            answer = Math.max(answer, left[i - 1] + right[i + 1]);
        }

        System.out.println(answer);
        br.close();
    }
}
