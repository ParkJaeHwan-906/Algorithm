package may.week4.ngv;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 15분
  AI 사용 여부: X
  설명: 누적합
 */
public class 성적평균_박서희 {

    static int[] prefixSum;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder ans = new StringBuilder();

        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());

        prefixSum = new int[N + 1];

        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= N; i++) {
            int score = Integer.parseInt(st.nextToken());
            prefixSum[i] = prefixSum[i - 1] + score;
        }

        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());
            ans.append(String.format("%.2f", getAvg(start, end))).append("\n");
        }

        System.out.print(ans.toString());
    }

    static double getAvg(int start, int end) {
        int sumScore = prefixSum[end] - prefixSum[start - 1];
        int n = end - start + 1;

        return sumScore * 1.0 / n;
    }
}
