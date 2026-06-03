package jun.week1.ngv;

import java.io.*;
import java.util.*;

public class 업무처리_박재환 {
    static Queue<Integer>[][] tasks;
    static int node;
    static int leaf;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine().trim());

        int h = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());
        int r = Integer.parseInt(st.nextToken());

        node = (int) Math.pow(2, h + 1) - 1;
        leaf = (int) Math.pow(2, h);

        tasks = new Queue[node][2];
        for(int i = 0; i < node; i++) {
            for(int j = 0; j < 2; j++) {
                tasks[i][j] = new LinkedList<>();
            }
        }

        for(int i = node - leaf; i < node; i++) {
            st = new StringTokenizer(br.readLine().trim());

            for(int j = 0; j < k; j++) {
                tasks[i][0].add(Integer.parseInt(st.nextToken()));
            }
        }

        System.out.println(solution(r));
    }

    static long answer;
    static long solution(int r) {
        answer = 0;

        for(int day = 1; day <= r; day++) {
            int leftRight = day % 2 == 0 ? 1 : 0;

            if(!tasks[0][leftRight].isEmpty()) {
                answer += tasks[0][leftRight].poll();
            }

            for(int i = 1; i < node - leaf; i++) {
                int manager = (i - 1) / 2;

                if(!tasks[i][leftRight].isEmpty()) {
                    int cur = tasks[i][leftRight].poll();

                    if(i % 2 == 1) {
                        tasks[manager][0].offer(cur);
                    } else {
                        tasks[manager][1].offer(cur);
                    }
                }
            }

            for(int i = node - leaf; i < node; i++) {
                int manager = (i - 1) / 2;

                if(!tasks[i][0].isEmpty()) {
                    int cur = tasks[i][0].poll();

                    if(i % 2 == 1) {
                        tasks[manager][0].offer(cur);
                    } else {
                        tasks[manager][1].offer(cur);
                    }
                }
            }
        }

        return answer;
    }
}
