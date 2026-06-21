package jun.week3.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 01:20:28
 * AI 사용 여부 O
 * => 초기에 대기중인 문제를 하나의 PQ 로 관리함 -> 시간초과
 *      => 각 도메인별로 문제를 분리해서 각각의 PQ 로 비교 -> start() 메서드에서 각 도메인의 대표 항목만 비교하도록 수정
 */
public class 코드트리채점기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static class Problem {
        String domain;      // 문제 도메인
        int pid;            // 문제 id

        int inTime;         // 문제가 들어온 시간
        int p;              // 문제의 우선순위

        int startTime;      // 문제 채점이 시작된 시간
        int endTime;        // 문제 체점이 끝난 시간

        Problem(String url, int inTime, int p) {
            String[] temp = url.split("/");
            this.domain = temp[0];
            this.pid = Integer.parseInt(temp[1]);

            this.inTime = inTime;
            this.p = p;

            this.startTime = 0;
            this.endTime = 0;
        }

        String getUrl() { return this.domain + "/" + this.pid; }
    }

    static final int SET = 100;
    static final int ADD = 200;
    static final int START = 300;
    static final int END = 400;

    static int n;

    static Map<String, PriorityQueue<Problem>> waitingProblems;
    static Set<String> waitProblemsSet;

    static PriorityQueue<Integer> availableJudgment;
    static Map<Integer, Problem> processingProblemsMap;
    static Set<String> processingProblemsSet;

    static Map<String, Problem> finishedProblemsMap;

    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();

        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());

            if(type == SET) { set(st); }
            else if(type == ADD) { add(st); }
            else if(type == START) { start(st); }
            else if(type == END) { end(st); }
            else { sb.append(waitProblemsSet.size()).append('\n'); }
        }
        System.out.println(sb);
    }

    static void set(StringTokenizer st) {
        waitingProblems = new HashMap<String, PriorityQueue<Problem>>();
        waitProblemsSet = new HashSet<>();

        availableJudgment = new PriorityQueue<>(Integer::compare);
        processingProblemsMap = new HashMap<>();
        processingProblemsSet = new HashSet<>();

        finishedProblemsMap = new HashMap<>();

        n = Integer.parseInt(st.nextToken());
        for(int i = 1; i <= n; i++) availableJudgment.add(i);
        String url = st.nextToken();
        Problem problem = new Problem(url, 0, -1);
        waitingProblems.computeIfAbsent(problem.domain, k -> new PriorityQueue<>((a, b) -> {
            /**
             * 우선 순위 낮은 순 -> 들어온 시간이 빠른 순
             */
            if(a.p != b.p) return Integer.compare(a.p, b.p);
            return Integer.compare(a.inTime, b.inTime);
        })).offer(problem);
        waitProblemsSet.add(problem.getUrl());
    }

    static void add(StringTokenizer st) {
        int inTime = Integer.parseInt(st.nextToken());
        int p = Integer.parseInt(st.nextToken());
        String domain = st.nextToken();

        Problem problem = new Problem(domain, inTime, p);
        if(!waitProblemsSet.add(problem.getUrl())) return;
        waitingProblems.computeIfAbsent(problem.domain, k -> new PriorityQueue<>((a, b) -> {
            /**
             * 우선 순위 낮은 순 -> 들어온 시간이 빠른 순
             */
            if(a.p != b.p) return Integer.compare(a.p, b.p);
            return Integer.compare(a.inTime, b.inTime);
        })).offer(problem);
    }

    static void start(StringTokenizer st) {
        if(availableJudgment.isEmpty() || waitingProblems.isEmpty()) return;
        int time = Integer.parseInt(st.nextToken());

        Problem cand = null;
        for(String domain : waitingProblems.keySet()) {
            PriorityQueue<Problem> temp = waitingProblems.get(domain);
            if(temp.isEmpty()) continue;
            Problem problem = temp.peek();
            if(processingProblemsSet.contains(domain)) continue;
            Problem prevProblem = finishedProblemsMap.get(domain);
            if(prevProblem != null) {
                int gap = prevProblem.endTime - prevProblem.startTime;
                if(prevProblem.startTime + gap * 3 > time) continue;
            }

            if(cand == null || cand.p > problem.p
                    || (cand.p == problem.p && cand.inTime > problem.inTime)) cand = problem;
        }

        if(cand == null) return;

        waitingProblems.get(cand.domain).poll();
        waitProblemsSet.remove(cand.getUrl());

        int judgmentId = availableJudgment.poll();
        cand.startTime = time;
        processingProblemsMap.put(judgmentId, cand);
        processingProblemsSet.add(cand.domain);
    }

    static void end(StringTokenizer st) {
        int time = Integer.parseInt(st.nextToken());
        int judgmentId = Integer.parseInt(st.nextToken());

        Problem problem = processingProblemsMap.get(judgmentId);
        if(problem == null) return;

        problem.endTime = time;
        processingProblemsMap.remove(judgmentId);
        processingProblemsSet.remove(problem.domain);

        finishedProblemsMap.put(problem.domain, problem);

        availableJudgment.add(judgmentId);
    }
}
