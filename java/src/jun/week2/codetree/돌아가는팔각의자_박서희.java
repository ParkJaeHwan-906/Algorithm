package jun.week2.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 00:56:19
  AI 사용 여부: X
 */
public class 돌아가는팔각의자_박서희 {

    static LinkedList<Integer>[] chairs;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        chairs = new LinkedList[4];
        for (int i = 0; i < 4; i++) {
            chairs[i] = new LinkedList<>();
            String s = br.readLine();
            for (int j = 0; j < 8; j++) {
                chairs[i].add(s.charAt(j) - '0');
            }
        }

        int k = Integer.parseInt(br.readLine());
        while (k-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken()) - 1;
            int d = Integer.parseInt(st.nextToken()); // 시계: 1 반시계: -1
            rotate(n, d, -1);
        }

        int answer = 1 * (chairs[0].get(0)) + 2 * (chairs[1].get(0)) + 4 * (chairs[2].get(0)) + 8 * (chairs[3].get(0));
        System.out.println(answer);
    }

    static void rotate(int n, int d, int prev) {
        if (n <= 2 && prev != n + 1 && chairs[n].get(2) != chairs[n + 1].get(6)) {
            rotate(n + 1, -d, n);
        }
        if (n >= 1 && prev != n - 1 && chairs[n - 1].get(2) != chairs[n].get(6)) {
            rotate(n - 1, -d, n);
        }

        if (d == 1) {
            chairs[n].addFirst(chairs[n].removeLast());
        } else {
            chairs[n].add(chairs[n].removeFirst());
        }
    }
}
