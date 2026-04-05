package mar.week5.boj;

import java.util.*;
import java.io.*;

public class 스택2_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static StringTokenizer st;
    static void init() throws IOException {
        int n = Integer.parseInt(br.readLine().trim());
        Deque<Integer> stack = new ArrayDeque<>();
        while(n-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());

            if(cmd == 1) {
                int num = Integer.parseInt(st.nextToken());
                stack.push(num);
            } else if(cmd == 2) {
                sb.append(stack.isEmpty() ? -1 : stack.pop()).append('\n');
            } else if(cmd == 3) {
                sb.append(stack.size()).append('\n');
            } else if(cmd == 4) {
                sb.append(stack.isEmpty() ? 1 : 0).append('\n');
            } else if(cmd == 5) {
                sb.append(stack.isEmpty() ? -1 : stack.peek()).appendCodePoint('\n');
            }
        }
    }
}
