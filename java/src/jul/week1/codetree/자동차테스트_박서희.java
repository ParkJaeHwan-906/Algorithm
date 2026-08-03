package jul.week1.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 10분 정도?
  AI 사용 여부: X
  오늘 시간이 없어서 쉬워 보이는거로 편식..
 */
public class 자동차테스트_박서희 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());

        int[] arr = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        Arrays.sort(arr);

        StringBuilder sb = new StringBuilder();
        while (q-- > 0) {
            int m = Integer.parseInt(br.readLine());
            int idx = Arrays.binarySearch(arr, m);
            if (idx <= 0) sb.append(0).append("\n");
            else {
                int count = idx * (n - 1 - idx);
                sb.append(count).append("\n");
            }
        }
        System.out.println(sb);
    }
}
