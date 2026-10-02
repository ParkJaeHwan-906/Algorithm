package sep.week4.codetree;

import java.util.*;
import java.io.*;

public class 코드트리채점기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int SET = 100;
    static final int REQ = 200;
    static final int TRY = 300;
    static final int END = 400;
    static final int QRY = 500;

    static class Problem implements Comparable<Problem> {
        int inTime;     // 진입 시점
        int priority;   // 우선순위
        String url;     // url
        String domain;
        int pId;
        Problem(int inTime, int priority, String url) {
            this.inTime = inTime;
            this.priority = priority;
            this.url = url;
            seperateUrl(url);
        }
        void seperateUrl(String url) {
            String[] arr = url.split("/");
            this.domain = arr[0];
            this.pId = Integer.parseInt(arr[1]);
        }
        @Override
        public int compareTo(Problem o) {
            if(this.priority != o.priority) {
                return Integer.compare(this.priority, o.priority);
            }
            return Integer.compare(this.inTime, o.inTime);
        }
    }

    static int n;
    static Map<String, PriorityQueue<Problem>> waitQueue;
    static Set<String> waitUrls;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            if(type == SET) {
                set(st);
            }
            else if(type == REQ) {
                req(st);
            }
            else if(type == TRY) {}
            else if(type == END) {}
            else if(type == QRY) {
                sb.append(qry(st)).append('\n');
            }
        }
        System.out.print(sb);
    }

    static void set(StringTokenizer st) {
        waitQueue = new HashMap<>();
        waitUrls = new HashSet<>();
        n = Integer.parseInt(st.nextToken());
        String url = st.nextToken();
        Problem initProblem = new Problem(0, 1, url);
        waitQueue.computeIfAbsent(initProblem.domain, k -> new PriorityQueue<>()).offer(initProblem);
        waitUrls.add(initProblem.url);
    }

    static void req(StringTokenizer st) {
        int t = Integer.parseInt(st.nextToken());
        int p = Integer.parseInt(st.nextToken());
        String u = st.nextToken();
        if(waitUrls.contains(u)) {
            return;
        }
        Problem problem = new Problem(t, p, u);
        waitQueue.computeIfAbsent(problem.domain, k -> new PriorityQueue<>()).offer(problem);
        waitUrls.add(problem.url);
    }

    static int qry(StringTokenizer st) {
        int t = Integer.parseInt(st.nextToken());
        return waitUrls.size();
    }
}
