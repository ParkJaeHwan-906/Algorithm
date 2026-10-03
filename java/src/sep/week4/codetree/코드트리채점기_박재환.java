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
        int startTime;
        int endTime;
        Problem(int inTime, int priority, String url) {
            this.inTime = inTime;
            this.priority = priority;
            this.url = url;
            seperateUrl(url);
            this.startTime = -1;
            this.endTime = -1;
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
    static Problem[] judges;
    static PriorityQueue<Integer> availableJudges;
    static Map<String, Problem> lastHistory;
    static Set<String> progressingProblems;
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
            else if(type == TRY) {
                tryJudge(st);
            }
            else if(type == END) {
                end(st);
            }
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
        judges = new Problem[n + 1];
        availableJudges = new PriorityQueue<>();
        for(int i = 1; i <= n; i++) {
            availableJudges.offer(i);
        }
        lastHistory = new HashMap<>();
        progressingProblems = new HashSet<>();
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

    static void tryJudge(StringTokenizer st) {
        int t = Integer.parseInt(st.nextToken());
        if(availableJudges.isEmpty()) {
            return;
        }
        Problem candProblem = null;
        for(String domain : waitQueue.keySet()) {
            if(progressingProblems.contains(domain)) {
                continue;
            }
            Problem lastProblem = lastHistory.get(domain);
            if(lastProblem != null) {
                int gap = lastProblem.endTime - lastProblem.startTime;
                if(t < lastProblem.startTime + (gap * 3)) {
                    continue;
                }
            }
            Problem problem = waitQueue.get(domain).peek();
            if(candProblem == null
                || candProblem.priority > problem.priority
                || (candProblem.priority == problem.priority && candProblem.inTime > problem.inTime)) {
                candProblem = problem;
            }
        }
        if(candProblem == null) {
            return;
        }
        int jId = availableJudges.poll();
        judges[jId] = candProblem;
        candProblem.startTime = t;
        progressingProblems.add(candProblem.domain);
        waitQueue.get(candProblem.domain).poll();
        waitUrls.remove(candProblem.url);
        if(waitQueue.get(candProblem.domain).isEmpty()) {
            waitQueue.remove(candProblem.domain);
        }
    }

    static void end(StringTokenizer st) {
        int t = Integer.parseInt(st.nextToken());
        int jId =  Integer.parseInt(st.nextToken());
        if(judges[jId] == null) {
            return;
        }
        Problem problem = judges[jId];
        judges[jId] = null;
        problem.endTime = t;
        lastHistory.put(problem.domain, problem);
        progressingProblems.remove(problem.domain);
        availableJudges.add(jId);
    }

    static int qry(StringTokenizer st) {
        int t = Integer.parseInt(st.nextToken());
        return waitUrls.size();
    }
}
