package aug.week1.codetree;

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
    static final int JUDGE = 300;
    static final int END = 400;
    static final int QUERY = 500;

    static class Problem implements Comparable<Problem> {
        int inTime;
        int priority;
        String url;
        String domain;
        int start;
        int end;
        Problem(int inTime, int priority, String url) {
            this.inTime = inTime;
            this.priority = priority;
            this.url = url;
            this.domain = url == null ? null : url.substring(0, url.indexOf('/'));

            this.start = -1;
            this.end = -1;
        }
        String getDomain() {
            return domain;
        }
        String endPoint() {
            return url.split("/")[1];
        }
        public int compareTo(Problem p) {
            if(this.priority != p.priority) return Integer.compare(this.priority, p.priority);
            return Integer.compare(this.inTime, p.inTime);
        }
    }

    static class Judge {
        Problem assignProblem;
        Judge(Problem assignProblem) {
            this.assignProblem = assignProblem;
        }
        void assign(Problem problem, int t) {
            problem.start = t;
            this.assignProblem = problem;
        }
        Problem end(int t) {
            Problem p = assignProblem;
            assignProblem = null;
            p.end = t;
            return p;
        }
    }

    static int n;
    // 대기
    static Map<String, PriorityQueue<Problem>> waitProblems;
    static Set<String> waitProblemSet;
    // 채점
    static PriorityQueue<Integer> availableJudges;
    static Judge[] judges;
    static Set<String> judgingProblemSet;
    // 완료
    static Map<String, Problem> finishedProblems;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            if(type == SET) { set(st); }
            else if(type == REQ) { req(st); }
            else if(type == JUDGE) { judge(st); }
            else if(type == END) { end(st); }
            else if(type == QUERY) {
                int result = query();
                sb.append(result).append('\n');
            }
        }
        System.out.println(sb);
    }

    static void set(StringTokenizer st) {
        waitProblems = new HashMap<>();             // domain / List<P>
        waitProblemSet = new HashSet<>();           // url

        finishedProblems = new HashMap<>();
        /**
         *  1. N 개의 채점기가 준비된다.
         *  2. 초기 문제가 주어진다.
         *      - 0 초
         *      - 우선순위 1
         *      - url
         */
        n = Integer.parseInt(st.nextToken());
        judges = new Judge[n + 1];          // 1 - based
        availableJudges = new PriorityQueue<>();
        for(int i = 1; i < n + 1; i++) {
            judges[i] = new Judge(null);
            availableJudges.offer(i);
        }
        judgingProblemSet = new HashSet<>();

        String url = st.nextToken();
        Problem problem = new Problem(0, 1, url);
        addWaitProblem(problem);
    }

    static void req(StringTokenizer st) {
        /**
         * t초에 채점 우선순위가 p이면서 url이 u인 문제 채점 요청이 들어온다.
         * 1. 문제는 채점 대기큐에 들어간다.
         *  - url이 완벽하게 일치하는 문제가 단 한개라도 있다면 넘어간다.
         */
        int inTime = Integer.parseInt(st.nextToken());
        int priority = Integer.parseInt(st.nextToken());
        String url = st.nextToken();
        Problem problem = new Problem(inTime, priority, url);
        addWaitProblem(problem);
    }

    static void addWaitProblem(Problem problem) {
        if(waitProblemSet.contains(problem.url)) return;

        waitProblems.computeIfAbsent(problem.getDomain(), k -> new PriorityQueue<>()).add(problem);
        waitProblemSet.add(problem.url);
    }

    static void judge(StringTokenizer st) {
        /**
         * t 초에 대기 큐에서 즉시 채점이 불가능한 경우를 제외하고 남은 task 중 우선순위가 가장 높은 task 를 고른다.
         * - task 의 도메인이 현재 채점을 진행중인 도메인 중 하나라면 불가
         * - task 의 도메인과 정확히 일치하는 도메인에 대해 가장 최근 진행된 채점 시작 시간이 start, 종료 시간이 start + gap
         *      현재 t 가 start + 3 * gap 보다 작다면 불가
         *
         * 우선순위
         * - p 가 작을 수록
         * - 동일 시, inTime 이 빠른 순
         */
        int t = Integer.parseInt(st.nextToken());
        if(availableJudges.isEmpty()) return;
        Problem candProblem = null;
        PriorityQueue<Problem> candQueue = null;
        String candDomain = null;

        // 1. 사용가능한 채점기가 있는가
        // 2. 문제 선정
        for(PriorityQueue<Problem> pq : waitProblems.values()) {
            if(pq.isEmpty()) continue;
            Problem p = pq.peek();
            // 2 - 1. <불가> 현재 채점중인 도메인인 경우
            if(judgingProblemSet.contains(p.getDomain())) continue;
            // 2 - 2. <불가> 해당 task 가 부정채점인경우
            Problem prev = finishedProblems.get(p.getDomain());
            if(prev != null) {
                long availableTime = (long) prev.start + 3L * (prev.end - prev.start);
                if(t < availableTime) continue;
            }

            // 2 - 3. <가능>
            // 우선순위가 크거나
            // 같다면 대기시간이 작은 순
            if(candProblem == null || p.compareTo(candProblem) < 0) {
                candProblem = p;
                candQueue = pq;
                candDomain = p.getDomain();
            }
        }
        if(candProblem == null) return;

        candQueue.poll();
        if(candQueue.isEmpty()) waitProblems.remove(candDomain);
        waitProblemSet.remove(candProblem.url);

        int jId = availableJudges.poll();
        Judge judge = judges[jId];
        judge.assign(candProblem, t);
        judgingProblemSet.add(candProblem.getDomain());
    }

    static void end(StringTokenizer st) {
        int t = Integer.parseInt(st.nextToken());
        int jId = Integer.parseInt(st.nextToken());
        Judge judge = judges[jId];
        if(judge.assignProblem == null) return;
        Problem p = judge.end(t);
        finishedProblems.put(p.getDomain(), p);
        judgingProblemSet.remove(p.getDomain());
        availableJudges.offer(jId);
    }

    static int query() {
        return waitProblemSet.size();
    }
}
