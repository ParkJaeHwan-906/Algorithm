package may.week3.codetree;

import java.util.*;
import java.io.*;

public class 바이러스검사_박서희 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());

        int[] customers = new int[n];
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++){
            customers[i] = Integer.parseInt(st.nextToken());
        }

        int[] capability = new int[2];
        st = new StringTokenizer(br.readLine());
        capability[0] = Integer.parseInt(st.nextToken());
        capability[1] = Integer.parseInt(st.nextToken());

        long answer = 0;

        for (int i = 0; i < n; i++) {
            answer++;
            int remain = customers[i] - capability[0];

            if (remain > 0) {
                answer+=(remain/capability[1]);
                if (remain%capability[1]!= 0)
                    answer++;
            }
        }

        System.out.println(answer);
    }
}
