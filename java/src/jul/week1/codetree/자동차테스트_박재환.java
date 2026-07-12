package jul.week1.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:20:47
 * AI 사용 여부 X
 */
public class 자동차테스트_박재환 {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n, q;
    static int[] cars;
    static Map<Integer, Integer> carMap;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());

        cars = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n; i++) cars[i] = Integer.parseInt(st.nextToken());

        Arrays.sort(cars);
        carMap = new HashMap<>();
        for(int i = 0; i < n; i++) carMap.put(cars[i], i);

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < q; i++) {
            int m = Integer.parseInt(br.readLine().trim());
            Integer idx = carMap.get(m);
            if(idx == null) {               // 중간값이 없는 경우
                sb.append(0).append('\n');
                continue;
            }

            long left = idx;
            long right = n - idx - 1L;
            sb.append(left * right).append('\n');
        }

        System.out.print(sb);
    }
}
