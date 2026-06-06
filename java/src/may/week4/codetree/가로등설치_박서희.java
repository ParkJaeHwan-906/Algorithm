package may.week4.codetree;

import java.util.*;
import java.io.*;

/*
  문제풀이 시간: 1시간 55분
  AI 사용 여부: O (treeset 함수 관련(lower,higher) + 틀렸을 때 가로수 제거가 번호 기준인걸 몰랐었음. 위치라고 문제 품.)
  생각의 흐름: N 만큼의 int 배열은 메모리 크기 초과로 안 될 것 같고 (N이 최대 10^9니까)
            매번 모든 가로수를 탐색하기에는 질의 횟수가 100,000 이니까 안될 거 같고
            가로수 간의 거리의 최대값을 바로 볼 수 있는 우선순위 큐를 사용해보는 걸로
            최댓값과 왼쪽과 오른쪽 끝 가로수 위치를 알면 r을 구할 수 있음. 왼쪽과 오른쪽 가로수 위치를 알기 위해서 treeset 사용
            treeset을 사용하면 가로수를 바로 지울 수도 있음.
 */
public class 가로등설치_박서희 {

    static final int ADD = 200;
    static final int DEL = 300;
    static final int CAL = 400;

    static int N;
    static int cnt = 1;
    static TreeSet<Integer> lights = new TreeSet<>();
    static Map<Integer, Integer> numLights = new HashMap<>();
    static PriorityQueue<int[]> pq;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder answer = new StringBuilder();

        int Q = Integer.parseInt(br.readLine());

        StringTokenizer st = new StringTokenizer(br.readLine());
        st.nextToken();
        N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        for (int i = 0; i < M; i++) {
            int light = Integer.parseInt(st.nextToken());
            lights.add(light);
            numLights.put(cnt++, light);
        }

        // 가로수 간격 저장 규칙 간격이 넓은 것부터, 간격이 같다면 좌표가 작은 순으로
        pq = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0]) {
                return b[0] - a[0];
            }
            return a[1] - b[1];
        });

        int prev = -1;
        for (int cur : lights) {
            if (prev != -1) {
                pq.add(new int[]{cur - prev, prev, cur});
            }
            prev = cur;
        }

        for (int i = 0; i < Q - 1; i++) {
            st = new StringTokenizer(br.readLine());
            int command = Integer.parseInt(st.nextToken());
            if (command == ADD) {
                addLight();
            } else if (command == DEL) {
                int targetNum = Integer.parseInt(st.nextToken());
                delLight(targetNum);
            } else if (command == CAL) {
                int r = calR();
                answer.append(r).append("\n");
            }
        }

        System.out.print(answer.toString());
    }

    static void addLight() {
        int[] gap = existsLightGap();
        if (gap[0] == -1) return;

        int leftLight = gap[1];
        int rightLight = gap[2];
        int newLight = (leftLight + rightLight + 2 - 1) / 2;

        pq.add(new int[]{newLight - leftLight, leftLight, newLight});
        pq.add(new int[]{rightLight - newLight, newLight, rightLight});

        lights.add(newLight);
        numLights.put(cnt++, newLight);
    }

    static void delLight(int targetNum) {
        int targetLight = numLights.get(targetNum);
        Integer leftToTarget = lights.lower(targetLight);
        Integer rightToTarget = lights.higher(targetLight);

        lights.remove(targetLight);

        if (leftToTarget != null && rightToTarget != null) {
            pq.add(new int[] {rightToTarget - leftToTarget, leftToTarget, rightToTarget});
        }
    }

    static int calR() {
        int leftLight = lights.first();
        int rightLight = lights.last();

        int[] gap = existsLightGap();

        // pq에서 삭제했기 때문에 다시 넣어줌.
        pq.add(gap);

        int r = Math.max((leftLight - 1) * 2, (N - rightLight) * 2);
        r = Math.max(r, gap[0]);

        return r;
    }

    static int[] existsLightGap() {
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            if (lights.contains(cur[1]) && lights.contains(cur[2]))
                if (lights.higher(cur[1]) != null && lights.higher(cur[1]) == cur[2])
                    return cur;
        }
        return new int[]{-1, -1, -1};
    }
}
