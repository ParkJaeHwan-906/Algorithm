package jun.week3.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 풀다가 잠들어서 미상
  AI 사용 여부: O dp인 것은 알지만 잘 모르겠어서 참조 + 시간 초과나서 사용.
  시간 초과 해결: 1) 매번 dp를 돌리지 않고 dirty라는 boolean 값을 사용해 dp를 필요할 때만 돌림.
               2) compose에 3중 반복문을 사용했는데 2중 반복문 + O(1)로 수정함.
 */
public class 코디의향수공방_박서희 {

    static int[] sCnt = new int[3001];
    static Map<Integer, Integer> fragrance = new HashMap<>();
    static int nextId = 1;

    static int[] blendDp = null;
    static long[] suffixPair = null;    // 탑, 미들 합이 x 이상인 경우의 수
    static int[] availVals = new int[3001];
    static int availSz = 0;

    static boolean dirty = true;
    static final int INF = 10000000;
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());

        while (Q-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int work = Integer.parseInt(st.nextToken());
            switch (work) {
                case 1: prepare(st); break;
                case 2: add(st); break;
                case 3: del(st); break;
                case 4: blend(st); break;
                case 5: compose(st); break;
            }
        }
        System.out.print(sb);
    }

    static void prepare(StringTokenizer st) {
        int N = Integer.parseInt(st.nextToken());
        for (int i = 0; i < N; i++) {
            int s = Integer.parseInt(st.nextToken());
            fragrance.put(nextId++, s);
            sCnt[s]++;
        }
        dirty = true;
    }

    static void add(StringTokenizer st) {
        int v = Integer.parseInt(st.nextToken());
        fragrance.put(nextId++, v);
        sCnt[v]++;
        dirty = true;
    }

    static void del(StringTokenizer st) {
        int idx = Integer.parseInt(st.nextToken());
        Integer v = fragrance.remove(idx);
        if (v == null) {
            sb.append(-1).append('\n');
        } else {
            sCnt[v]--;
            sb.append(v).append('\n');
            dirty = true;
        }
    }

    static void rebuild() {
        if (!dirty) return;

        availSz = 0;
        for (int s = 1; s <= 3000; s++) {
            if (sCnt[s] > 0) availVals[availSz++] = s;
        }

        blendDp = new int[3001];
        Arrays.fill(blendDp, INF);
        blendDp[0] = 0;
        for (int i = 1; i <= 3000; i++) {
            for (int j = 0; j < availSz; j++) {
                int s = availVals[j];
                if (s > i) break;
                blendDp[i] = Math.min(blendDp[i], blendDp[i - s] + 1);
            }
        }

        long[] pairCnt = new long[6001];
        for (int i = 0; i < availSz; i++) {
            for (int j = 0; j < availSz; j++) {
                pairCnt[availVals[i] + availVals[j]]
                        += (long) sCnt[availVals[i]] * sCnt[availVals[j]];
            }
        }
        // 거꾸로
        suffixPair = new long[6002];
        for (int i = 6000; i >= 0; i--) {
            suffixPair[i] = suffixPair[i + 1] + pairCnt[i];
        }

        dirty = false;
    }

    static void blend(StringTokenizer st) {
        int K = Integer.parseInt(st.nextToken());
        rebuild();
        sb.append(blendDp[K] == INF ? -1 : blendDp[K]).append('\n');
    }

    static void compose(StringTokenizer st) {
        int K = Integer.parseInt(st.nextToken());
        rebuild();

        long cnt = 0;
        for (int i = 0; i < availSz; i++) {
            int c = availVals[i];   // 베이스노트 향도 고정
            int need = K - c;
            if (need <= 0)
                cnt += suffixPair[0] * sCnt[c];
            else
                cnt += suffixPair[need] * sCnt[c];
        }
        sb.append(cnt).append('\n');
    }
}
