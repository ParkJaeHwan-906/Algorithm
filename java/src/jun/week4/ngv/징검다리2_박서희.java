package jun.week4.ngv;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 00:21:47
  AI 사용 여부: X
 */
public class 징검다리2_박서희 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        int[] arr = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();

        int[] leftDp = new int[arr.length];
        int[] rightDp = new int[arr.length];

        List<Integer> lis = new ArrayList<>();
        lis.add(arr[0]);
        leftDp[0] = 1;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > lis.get(lis.size() - 1)) lis.add(arr[i]);
            else {
                int idx = Collections.binarySearch(lis, arr[i]);
                lis.set(-idx - 1, arr[i]);
            }
            leftDp[i] = Math.max(leftDp[i - 1], lis.size());
        }

        lis.clear();
        lis.add(arr[N - 1]);
        rightDp[N - 1] = 1;
        for (int i = N - 2; i >= 0; i--) {
            if (arr[i] > lis.get(lis.size() - 1)) lis.add(arr[i]);
            else {
                int idx = Collections.binarySearch(lis, arr[i]);
                lis.set(-idx - 1, arr[i]);
            }
            rightDp[i] = Math.max(rightDp[i + 1], lis.size());
        }

        int answer = 0;
        for (int i = 0; i < N; i++) {
            answer = Math.max(leftDp[i] + rightDp[i] - 1, answer);
        }

        System.out.println(answer);
    }
}
