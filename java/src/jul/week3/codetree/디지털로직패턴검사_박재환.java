package jul.week3.codetree;

import java.util.*;
import java.io.*;

public class 디지털로직패턴검사_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        String line = br.readLine().trim();
        st = new StringTokenizer(br.readLine().trim());
        int k = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        System.out.println(solution(line, k, m) ? 0 : 1);
    }

    static boolean solution(String line, int k, int m) {
        Map<Long, Integer> patterns = new HashMap<>();
        StringBuilder pattern = new StringBuilder(line.substring(0, k - 1));
        for(int i = k - 1; i < line.length(); i++) {
            pattern.append(line.charAt(i));
            Long l = Long.parseLong(pattern.toString(), 2);     // 2진수로 치환
            patterns.put(l, patterns.getOrDefault(l, 0) + 1);
            if(patterns.get(l) >= m) return false;
            pattern.deleteCharAt(0);            // 길이를 k로 유지
        }
        return true;
    }
}
