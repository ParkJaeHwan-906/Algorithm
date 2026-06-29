package jun.week3.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: AI로 풀어서 시간 X
  AI 사용 여부: O 문제도 이해가 어려웠음. 어려운 알고리즘이 들어가는게 아니라서 복잡함을 이겨내고 푸는게 중요할듯..
 */
public class 코드트리채점기_박서희 {

    static int N;
    static PriorityQueue<Integer> restMarkers = new PriorityQueue<>();

    static Map<String, PriorityQueue<Task>> waitingQueue = new HashMap<>();
    static Map<String, Integer> nextAvailableTime = new HashMap<>();
    static Set<String> waitingUrlSet = new HashSet<>();
    static Set<String> markingDomainSet = new HashSet<>();
    static StringBuilder sb = new StringBuilder();

    static String[] markerDomain;
    static int[] markerStartTime;
    static int totalWaitingCount = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());

        while (Q-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int command = Integer.parseInt(st.nextToken());

            switch (command) {
                case 100: ready(st); break;
                case 200: requestMarking(st); break;
                case 300: tryMarking(st); break;
                case 400: exitMarking(st); break;
                case 500: checkQueue(st); break;
            }
        }
        System.out.print(sb);
    }

    static void ready(StringTokenizer st) {
        N = Integer.parseInt(st.nextToken());
        String u0 = st.nextToken();

        markerDomain = new String[N + 1];
        markerStartTime = new int[N + 1];

        for (int i = 1; i <= N; i++) {
            restMarkers.add(i);
        }

        Task initialTask = new Task(1, 0, u0);
        String domain = initialTask.domain;

        if (!waitingQueue.containsKey(domain)) {
            waitingQueue.put(domain, new PriorityQueue<>());
        }

        waitingQueue.get(domain).add(initialTask);
        waitingUrlSet.add(u0);
        totalWaitingCount = 1;
    }

    static void requestMarking(StringTokenizer st) {
        int t = Integer.parseInt(st.nextToken());
        int p = Integer.parseInt(st.nextToken());
        String u = st.nextToken();

        if (waitingUrlSet.contains(u)) return;

        Task task = new Task(p, t, u);
        String domain = task.domain;

        if (!waitingQueue.containsKey(domain)) waitingQueue.put(domain, new PriorityQueue<>());
        waitingQueue.get(domain).add(task);
        waitingUrlSet.add(u);
        totalWaitingCount++;
    }

    static void tryMarking(StringTokenizer st) {
        int t = Integer.parseInt(st.nextToken());

        if (restMarkers.isEmpty()) return; // 쉬고 있는 채점기가 없으면 return

        PriorityQueue<Task> candidatePQ = new PriorityQueue<>();

        for (String domain : waitingQueue.keySet()) {
            PriorityQueue<Task> pq = waitingQueue.get(domain);

            if (pq.isEmpty()) continue;

            if (markingDomainSet.contains(domain)) continue;
            if (nextAvailableTime.getOrDefault(domain, 0) > t) continue;

            Task candidate = pq.peek();
            candidatePQ.offer(candidate);
        }

        if (candidatePQ.isEmpty()) return;

        Task toDoTask = candidatePQ.poll();

        String targetDomain = toDoTask.domain;
        waitingQueue.get(targetDomain).poll();
        waitingUrlSet.remove(toDoTask.url);
        totalWaitingCount--;

        int markerId = restMarkers.poll();
        markerDomain[markerId] = targetDomain;
        markerStartTime[markerId] = t;

        markingDomainSet.add(targetDomain);
    }

    static void exitMarking(StringTokenizer st) {
        int t = Integer.parseInt(st.nextToken());
        int markerId = Integer.parseInt(st.nextToken());

        if (markerDomain[markerId] == null) return;

        String domain = markerDomain[markerId];
        int startTime = markerStartTime[markerId];
        int gap = t - startTime;
        nextAvailableTime.put(domain, startTime + 3 * gap);
        markingDomainSet.remove(domain);

        markerDomain[markerId] = null;
        markerStartTime[markerId] = 0;
        restMarkers.add(markerId);
    }

    static void checkQueue(StringTokenizer st) {
        int t = Integer.parseInt(st.nextToken());
        sb.append(totalWaitingCount).append("\n");
    }

    static class Task implements Comparable<Task> {
        int p, t;   // 우선순위, 시간
        String url;
        String domain;

        public Task(int p, int t, String url) {
            this.p = p;
            this.t = t;
            this.url = url;
            this.domain = url.split("/")[0];
        }

        @Override
        public int compareTo(Task o) {
            if (this.p != o.p) {
                return Integer.compare(this.p, o.p);
            }
            return Integer.compare(this.t, o.t);
        }
    }
}
