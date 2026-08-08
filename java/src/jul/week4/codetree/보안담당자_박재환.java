package jul.week4.codetree;

import java.util.*;
import java.io.*;

public class 보안담당자_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n;
    static String logs;
    static void init(BufferedReader br) throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        logs = br.readLine().trim();
        if (n % 2 == 1) {
            System.out.println("No");
            return;
        }

        int open = 0, wildCard = 0;
        for (char c : logs.toCharArray()) {
            if (c == '(') open++;
            else if (c == '?') wildCard++;
        }

        int requiredOpen = n / 2 - open;           // 필요한 ( 개수
        if (requiredOpen < 0 || requiredOpen > wildCard) {
            System.out.println("No");
            return;
        }

        int flag = 0;
        for (char c : logs.toCharArray()) {
            if (c == '(') {
                flag++;
            } else if (c == ')') {
                flag--;
            } else {
                if (requiredOpen > 0) {     // ( 부터 처리
                    flag++;
                    requiredOpen--;
                } else {
                    flag--;
                }
            }

            if (flag < 0) {         // ) 가 더 많을 수 없음
                System.out.println("No");
                return;
            }
        }
        System.out.println(flag == 0 ? "Yes" : "No");
    }
}
