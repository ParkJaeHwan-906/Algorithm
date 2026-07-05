package jun.week5.programmers.기둥과보설치_박재환;

import java.util.*;

/**
 * AI 사용 여부 O
 * -> 처음엔 기둥, 보 2개의 배열로 처리하려했으나, 로직이 너무 복잡해지는 것 같았음
 * -> 설치 여부만 가지고 비트마스킹을 활용하여 문제 풀이
 */
public class 기둥과보설치_박재환 {
    public static void main(String[] args) {
        int n = 5;
        int[][] build_frame = {{1, 0, 0, 1}, {1, 1, 1, 1}, {2, 1, 0, 1}, {2, 2, 1, 1}, {5, 0, 0, 1}, {5, 1, 0, 1}, {4, 2, 1, 1}, {3, 2, 1, 1}};

        int[][] answer = new Solution().solution(n, build_frame);
        for(int[] arr : answer) System.out.println(Arrays.toString(arr));
    }
}

class Solution {
    /**
     * 기둥은 바닥 위에 있거나 보의 한쪽 끝 부분 위에 있거나, 다른 기둥 위에 있을 수 있음
     * 보느 한쪽 끝 부분이 기중 위에 있거나, 양쪽 끝 부분이 다른 도와 동시에 연결되어 있어야한다.
     */
    int n;
    final int 기둥 = 1, 보 = 1 << 1;
    Map<Integer, Integer> map;
    public int[][] solution(int n, int[][] build_frame) {
        set(n);

        for(int[] cmd : build_frame) {
            int x = cmd[0], y = cmd[1];
            int type = cmd[2], action = cmd[3];
            int bit = (type == 0) ? 기둥 : 보;   // cmd의 type: 0=기둥, 1=보

            if (action == 1) {              // 설치: 일단 추가 → 무너지면 롤백
                add(x, y, bit);
                if (!possible()) remove(x, y, bit);
            } else {                         // 삭제: 일단 제거 → 무너지면 롤백
                remove(x, y, bit);
                if (!possible()) add(x, y, bit);
            }
        }

        return toAnswer();
    }

    boolean possible() {
        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            int x = e.getKey() / (n + 7), y = e.getKey() % (n + 7);
            int val = e.getValue();

            if ((val & 기둥) != 0) {
                if (!(y == 0                        // 바닥
                        || has(x, y - 1, 기둥)    // 아래 기둥
                        || has(x - 1, y, 보)      // 보
                        || has(x, y, 보)))
                    return false;
            }
            if ((val & 보) != 0) {
                if (!(has(x, y - 1, 기둥)
                        || has(x + 1, y - 1, 기둥)
                        || (has(x - 1, y, 보) && has(x + 1, y, 보))))
                    return false;
            }
        }
        return true;
    }

    boolean has(int x, int y, int bit) {
        if (x < 0 || y < 0 || x > n || y > n) return false;
        return (map.getOrDefault(getKey(x, y), 0) & bit) != 0;
    }

    void add(int x, int y, int bit) {
        int key = getKey(x, y);
        map.put(key, map.getOrDefault(key, 0) | bit);
    }

    void remove(int x, int y, int bit) {
        int key = getKey(x, y);
        int val = map.getOrDefault(key, 0) & ~bit;
        if (val == 0) map.remove(key);
        else map.put(key, val);
    }

    int[][] toAnswer() {
        List<int[]> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            int x = e.getKey() / (n + 7), y = e.getKey() % (n + 7);
            int val = e.getValue();
            if ((val & 기둥) != 0) list.add(new int[]{x, y, 0});
            if ((val & 보) != 0) list.add(new int[]{x, y, 1});
        }
        // x → y → type 오름차순 정렬
        list.sort((a, b) -> {
            if(a[0] != b[0]) return Integer.compare(a[0], b[0]);
            if(a[1] != b[1]) return Integer.compare(a[1], b[1]);
            return Integer.compare(a[2], b[2]);
        });

        int[][] result = new int[list.size()][];
        for(int i = 0; i < list.size(); i++) result[i] = list.get(i);
        return result;
    }

    void set(int n) {
        this.n = n;
        this.map = new HashMap<>();
    }

    int getKey(int x, int y) {
        return x * (n + 7) + y;
    }
}