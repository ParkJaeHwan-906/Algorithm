package jul.week4.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 1시간+
  AI 사용 여부: O
  ?일 때 나올 수 있는 결과가 (, ) 두 가지여서 dp를 의심했는데 점화식을 어떻게 세워야 할지 몰라서 dp 아닌줄..
  AI 도움으로 DP 점화식 세웠는데 이해하는데 좀 걸림..
 */
public class 보안담당자_박서희 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine().trim());
        String s = br.readLine().trim();

        if (s.length() % 2 == 1) {
            System.out.println("No");
            return;
        }

        boolean[] cur = new boolean[N + 1];
        boolean[] next = new boolean[N + 1];
        cur[0] = true;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j <= i; j++) {
                if (!cur[j]) continue;
                if ((s.charAt(i) == '(' || s.charAt(i) == '?') && j + 1 <= N)
                    next[j + 1] = true;
                if ((s.charAt(i) == ')' || s.charAt(i) == '?') && j - 1 >= 0)
                    next[j - 1] = true;
            }
            System.arraycopy(next, 0, cur, 0, N + 1);
            Arrays.fill(next, false);
        }

        System.out.println(cur[0] ? "Yes" : "No");

        br.close();
    }
}
