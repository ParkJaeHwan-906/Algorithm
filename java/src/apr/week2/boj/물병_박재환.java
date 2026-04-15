package apr.week2.boj;

import java.util.*;
import java.io.*;

public class 물병_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, k;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 물병 개수
        k = Integer.parseInt(st.nextToken());       // 한 번에 옮길 수 있는 물병의 수

        System.out.println(solution());
    }
    static int solution() {
        int newCount = 0;
        while(Integer.bitCount(n) > k) {
            n++;
            newCount++;
        }

        return newCount;
    }
}
