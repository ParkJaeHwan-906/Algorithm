package mar.week1.boj;

import java.io.*;
import java.util.*;

// 출력할 때는 자료구조 건들지말자 dfs 면.. 꺠질 수 있음
public class N과M_5_이다예 {

    static BufferedReader br;
    static StringTokenizer st;
    static int N,M;
    static List<Integer> numbers;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        numbers = new ArrayList<>();

        st = new StringTokenizer(br.readLine());
        for(int i = 0 ; i < N ; i++){
            int number = Integer.parseInt(st.nextToken());
            numbers.add(number);
        }
        numbers.sort((a,b) -> a - b);
        Deque<Integer> num = new ArrayDeque<>();
        boolean[] visited = new boolean[N];

        for(int i = 0; i < numbers.size(); i++){
            num.add(numbers.get(i));
            visited[i] = true;
            dfs(num,visited);
            num.pollLast();
            visited[i] = false;
        }
    }

    static void dfs(Deque<Integer> num, boolean[] visited) {
        if (num.size() == M) {
            for (int nowNum : num) {
                System.out.print(nowNum);
                System.out.print(" ");
            }
            System.out.println();
        }
        for(int i = 0 ; i < numbers.size() ; i ++){
            if(visited[i]) continue;
            num.add(numbers.get(i));
            visited[i] = true;
            dfs(num,visited);
            num.pollLast();
            visited[i] = false;
        }
    }



 }

