package mar.week2.codetree;

import java.util.*;
import java.io.*;

public class 디지털로직패턴검사_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static String line;
    static int k, m;
    static void init() throws IOException {
        line = br.readLine().trim();

        st = new StringTokenizer(br.readLine().trim());
        k = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        System.out.println(findPattern() ? 0 : 1);
    }
    static boolean findPattern() {
        Map<Long, Integer> map = new HashMap<>();
        StringBuilder pattern = new StringBuilder(line.substring(0, k-1));

        for(int i=k-1; i<line.length();) {
            pattern.append(line.charAt(i++));
            long key = Long.parseLong(pattern.toString(), 2);
            map.put(key, map.getOrDefault(key, 0) + 1);
            pattern.deleteCharAt(0);
        }
        return validation(map);
    }
    static boolean validation(Map<Long, Integer> patterns) {
        for(int i : patterns.values()) {
            if(i >= m) return false;
        }
        return true;
    }
}
