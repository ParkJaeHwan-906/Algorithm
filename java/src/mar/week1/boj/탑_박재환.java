package mar.week1.boj;

import java.util.*;
import java.io.*;

public class 탑_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n;
    static int[] arr;
    static int[] result;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());

        arr = new int[n];
        result = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i < n; i++) arr[i] = Integer.parseInt(st.nextToken());

        solution();
        StringBuilder sb = new StringBuilder();
        for(int i : result) sb.append(i).append(' ');
        System.out.println(sb);
    }
    static void solution() {
        Deque<int[]> stack = new ArrayDeque<>();
        for(int i=0; i<n; i++) {
            /**
             * 0 ~ n 순서대로 확인
             * 1. 왼쪽에 있는 탑이, 오른쪽에 있는 탑 보다 낮다면 -> 문제에서 요구하는 오른쪽에서 레이저를 쐈을 때, 맞출 수 없음
             * 2. stack 에 들어있는 탑보다 낮다면, stack.peek() 에 레이저를 쏠 수 있음
             */
            while(!stack.isEmpty() && stack.peek()[1] < arr[i]) {
                stack.pop();
            }
            result[i] = stack.isEmpty() ? 0 : stack.peek()[0];
            stack.push(new int[] {i+1, arr[i]});
        }
    }
}
