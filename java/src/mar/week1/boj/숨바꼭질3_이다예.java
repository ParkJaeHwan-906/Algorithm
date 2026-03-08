package mar.week1.boj;

import java.io.*;
import java.util.*;

// 숨바꼭질3 핵심 포인트
// 이 문제는 일반 BFS가 아니라 "0-1 BFS" 문제이다.
// 이동 비용이 서로 다르기 때문이다.
//
// x -> x*2 : 비용 0
// x -> x+1 : 비용 1
// x -> x-1 : 비용 1
//
// 이런 경우 일반 Queue BFS를 쓰면 안 되고
// Deque을 이용한 0-1 BFS를 사용한다.
//
// 비용이 0인 이동은 addFirst()
// 비용이 1인 이동은 addLast()
// 이렇게 하면 항상 최소 시간 순서로 탐색된다.

public class 숨바꼭질3_이다예 {
    static BufferedReader br;
    static StringTokenizer st;
    static int N,K;
    static int result;
    static boolean[] visited;

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        visited = new boolean[100001];

        result = 0;
        bfs(N,K);
        System.out.println(result);
    }

    static void bfs(int n, int k) {

        Deque<Now> dq = new ArrayDeque<>();

        dq.add(new Now(n,0));

        while(!dq.isEmpty()){
            Now now = dq.pollFirst();

            if(visited[now.now]) continue;
            visited[now.now] = true;

            if(now.now == k){
                result = now.time;
                return;
            };

            if(now.now*2 <= 100000) dq.addFirst(new Now(now.now*2,now.time));
            if(now.now+1 <= 100000) dq.addLast(new Now(now.now+1,now.time+1));
            if(now.now-1 >= 0) dq.addLast(new Now(now.now-1,now.time+1));

        }

    }
    static class Now{
        int now;
        int time;

        Now(int now , int time){
            this.now = now;
            this.time = time;
        }
    }
}
