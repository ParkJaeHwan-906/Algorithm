package mar.week2.codetree;

import java.util.*;
import java.io.*;
/**
 * N개의 채점기 (1~N)
 * 문제 url = 도메인/문제ID
 *
 * 0초에 채점 우선순위 1인 문제가 들어오게 된다.
 * 채점 task는 채점 대기 큐에 들어간다.
 *
 * t초에 채점 우선순위가 p이며, url이 u인 문제에 대한 채점 요청이 들어온다.
 * 채점 task는 채점 대기 뮤에 들어간다.
 * 채점 대기 큐에 있는 task중 같은 url이 있다면 넘어간다.
 *
 * t초에 채점 대기 큐에서 즉시 채점이 불가능한 경우를 제외하고,
 * 남은 task중 우선 순위가 가장 높은 task를 골라 채점한다.
 * - 해당 task의 도메인이 현재 재첨중인 도메인중 하나라면 불가능
 * - 해당 task의 도메인과 정확히 일치하는 도메인에 대해
 *      가장 최근 채점 시작 시간이 start
 *      종료 시간이 start + gap
 *      현재 시간 t가 start + 3 * gap 보다 작다면 불가하다
 *   우선 순위가 동일하다면, 들어온 시간 순으로 채점한다.
 *
 * t초에 재점기 채점이 종료된다. -> 쉬는 상태가 된다.
 * 없는 채점기의 명령이 주어질 수도 있다.
 *
 * 대기 큐에 있는 채점 task 수를 출력한다.
 *
 */
public class 코드트리채점기_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static final int INIT = 100;
    static final int REQ = 200;
    static final int TRY = 300;
    static final int END = 400;
    static final int QUERY = 500;
    static class Problem implements Comparable<Problem> {
        int priority;       // 우선순위
        String domain;      // 도메인
        int pId;            // 문제번호

        int s, e;
        int finished;      // 0 : 대기중, 1 : 채점중, 2 : 채점 완료

        Problem(int priority, String domain, int pId, int s) {
            this.priority = priority;
            this.domain = domain;
            this.pId = pId;
            this.s = s;

            this.e = -1;
            this.finished = 0;
        }

        String getUrl() {
            return this.domain + "/" + this.pId;
        }

        public int compareTo(Problem o) {
            if(this.priority == o.priority) return Integer.compare(this.s, o.s);
            return Integer.compare(this.priority, o.priority);
        }
    }
    static class Judger {
        int id;
        boolean process;
        Problem problem;

        Judger(int id) {
            this.id = id;

            this.process = false;
            this.problem = null;
        }
    }
    static StringTokenizer st;
    static Judger[] judgers;
    static Map<String, Problem> inWaitQ;
    static Map<String, Problem> inJudgerQ;
    static Map<String, Problem> finishedQ;
    static Map<String, PriorityQueue<Problem>> waitP;
    static PriorityQueue<Integer> waitJ;
    static int waitCount;
    static void init() throws IOException {
        waitCount = 0;
        int q = Integer.parseInt(br.readLine().trim());

        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());

            if(cmd == INIT) {
                int n = Integer.parseInt(st.nextToken());
                String url = st.nextToken();

                setInit(n, url);
            }
            else if(cmd == REQ) {
                int t = Integer.parseInt(st.nextToken());       // 시간
                int p = Integer.parseInt(st.nextToken());       // 우선순위
                String url = st.nextToken();

                req(t, p, url);
            }
            else if(cmd == TRY) {
                int t = Integer.parseInt(st.nextToken());       // 시간

                tryJudge(t);
            }
            else if(cmd == END) {
                int t = Integer.parseInt(st.nextToken());       // 시간
                int jId = Integer.parseInt(st.nextToken());       // 시간

                end(t, jId);
            }
            else if(cmd == QUERY) {
                int t = Integer.parseInt(st.nextToken());       // 시간

                sb.append(waitCount).append('\n');
            }


        }
    }
    static void setInit(int n, String url) {
        inWaitQ = new HashMap<>();
        inJudgerQ = new HashMap<>();
        finishedQ = new HashMap<>();

        waitP = new HashMap<>();
        waitJ = new PriorityQueue<>(Integer::compareTo);

        // 초기 채점기는 모두 쉬고 있는 상태
        judgers = new Judger[n+1];      // 1-based
        for(int i=1; i<n+1; i++) {
            judgers[i] = new Judger(i);
            waitJ.offer(i);
        }

        // 문제를 대키 큐에 삽입
        Problem problem = mapProblem(-1, 0, url);
        inWaitQ.put(url, problem);
        waitP.computeIfAbsent(problem.domain, k -> new PriorityQueue<>()).offer(problem);
        waitCount++;
    }

    static void req(int time, int priority, String url) {
        Problem problem = mapProblem(priority, time, url);
        /**
         * 대기 큐에 있는 문제 중 url이 완벽하게 일치한다면 처리하지 않음
         */
        if(inWaitQ.get(url) != null) return;
        inWaitQ.put(url, problem);
        waitP.computeIfAbsent(problem.domain, k -> new PriorityQueue<>()).offer(problem);
        waitCount++;
    }

    static void tryJudge(int time) {
        allocatedJudger(time);
    }

    static void end(int time, int jId) {
        Judger judger = judgers[jId];
        if(!judger.process) return;         // 채점중이지 않음
        // 채점기 상태 변경
        judger.process = false;
        Problem problem = judger.problem;
        judger.problem = null;
        // 문제 상태 갱신
        problem.finished = 2;
        problem.e = time;
        // 채점 가능 상태 변경
        waitJ.offer(jId);
        // 채점 진행 -> 완료
        inJudgerQ.remove(problem.domain);
        finishedQ.put(problem.domain, problem);
    }

    static Problem mapProblem(int priority, int inputTime, String url) {
        StringTokenizer tempSt = new StringTokenizer(url, "/");
        String domain = tempSt.nextToken();
        int pId = Integer.parseInt(tempSt.nextToken());
        return new Problem(priority, domain, pId, inputTime);
    }


    static void allocatedJudger(int time) {
        /**
         * 채점기 배정
         * - 쉬고 있는 채점기 중 가장 번호가 작은 채점기
         */
        if(waitJ.isEmpty()) return;         // 쉬고있는 채점기가 없음

        PriorityQueue<Problem> temp = new PriorityQueue<>();
        for(String domain : waitP.keySet()) {
            PriorityQueue<Problem> pq = waitP.get(domain);
            if(pq.isEmpty()) continue;
            Problem problem = pq.peek();

            if(canJudge(time, problem)) {
                temp.offer(problem);
            }
        }

        // temp 내부에는 현재 시간에 풀 수 있는 문제들이 있음
        if(temp.isEmpty()) return;
        Problem problem = temp.poll();
        waitP.get(problem.domain).poll();
        // 시작 시간 갱신, 상태 변경
        problem.s = time;
        problem.finished = 1;

        // 채점기 문제 할당, 상태 변경
        int jId = waitJ.poll();
        judgers[jId].process = true;
        judgers[jId].problem = problem;

        // 대기맵, 진행맵 갱신
        inWaitQ.remove(problem.getUrl());
        inJudgerQ.put(problem.domain, problem);

        waitCount--;
    }
    static boolean canJudge(int time, Problem problem) {
        if(inJudgerQ.get(problem.domain) != null) return false;         // 같은 도메인 채점 중

        Problem prev = finishedQ.get(problem.domain);
        if(prev == null) return true;           // 이전에 채점 이력 없음

        int start = prev.s;
        int gap = prev.e - prev.s;
        if(start + 3 * gap > time) return false;

        return true;
    }
}
