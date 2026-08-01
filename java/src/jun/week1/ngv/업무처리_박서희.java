package jun.week1.ngv;

import java.io.*;
import java.util.*;


/*
  문제풀이 시간: 00:44:58+
  AI 사용 여부: O DP로 풀려다가 아닌거 같아서 AI 사용함. 갈 길이 멀다~
 */
public class 업무처리_박서희 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int H = Integer.parseInt(st.nextToken());   // 높이
        int K = Integer.parseInt(st.nextToken());   // 각각 처리해야 하는 업무 수
        int R = Integer.parseInt(st.nextToken());   // 업무가 진행되는 날짜

        int totalNodes = (int) Math.pow(2, H + 1);
        int leafNodes = (int) Math.pow(2, H);
        Node[] nodes = new Node[totalNodes];
        for (int i = 1; i < totalNodes; i++) {
            nodes[i] = new Node();
        }

        for (int i = leafNodes; i < totalNodes; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < K; j++)
                nodes[i].leafQ.add(Integer.parseInt(st.nextToken()));
        }

        int answer = 0, day = 0;
        // 홀수 번째 날짜에는 왼쪽 부하 직원이 올린 업무를, 짝수 번째 날짜에는 오른쪽 부하 직원이 올린 업무를 처리한다.
        while (day < R) {
            day++;
            if (day % 2 == 1) {
                if (!nodes[1].leftQ.isEmpty()) answer += nodes[1].leftQ.poll();
            } else {
                if (!nodes[1].rightQ.isEmpty()) answer += nodes[1].rightQ.poll();
            }

            for (int i = 2; i < leafNodes; i++) {
                int parent = i / 2;
                boolean isLeftChild = (i % 2 == 0) ? true : false;

                if (day % 2 == 1) {
                    if (!nodes[i].leftQ.isEmpty()) {
                        int work = nodes[i].leftQ.poll();
                        if (isLeftChild) nodes[parent].leftQ.add(work);
                        else nodes[parent].rightQ.add(work);
                    }
                } else {
                    if (!nodes[i].rightQ.isEmpty()) {
                        int work = nodes[i].rightQ.poll();
                        if (isLeftChild) nodes[parent].leftQ.add(work);
                        else nodes[parent].rightQ.add(work);
                    }
                }
            }

            for (int i = leafNodes; i < totalNodes; i++) {
                int parent = i / 2;
                boolean isLeftChild = (i % 2 == 0) ? true : false;

                if (!nodes[i].leafQ.isEmpty()) {
                    int work = nodes[i].leafQ.poll();
                    if (isLeftChild) nodes[parent].leftQ.add(work);
                    else nodes[parent].rightQ.add(work);
                }

            }
        }
        System.out.println(answer);
    }

    static class Node {
        Queue<Integer> leftQ = new LinkedList<>();  // 왼쪽 부하가 올린 업무들
        Queue<Integer> rightQ = new LinkedList<>(); // 오른쪽 부하가 올린 업무들
        Queue<Integer> leafQ = new LinkedList<>();  // 내가 말단일 때 사용하는 업무들
    }
}
