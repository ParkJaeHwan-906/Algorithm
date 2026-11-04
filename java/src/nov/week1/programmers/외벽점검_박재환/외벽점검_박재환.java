package nov.week1.programmers.외벽점검_박재환;

public class 외벽점검_박재환 {
    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println(sol.solution(12, new int[]{1, 5, 6, 10}, new int[]{1, 2, 3, 4}));
        System.out.println(sol.solution(12, new int[]{1, 3, 4, 9, 10}, new int[]{3, 5, 7}));
    }
}

class Solution {
    int weakSize;
    int[] weak;
    int[] extendedWeak;
    int minResult;
    public int solution(int n, int[] weak, int[] dist) {
        set(n, weak);
        findDistCombi(dist, new boolean[dist.length], 0, new int[dist.length]);
        return minResult == Integer.MAX_VALUE ? -1 : minResult;
    }

    void set(int n, int[] weak) {
        weakSize = weak.length;
        extendedWeak = new int[weakSize * 2];

        for(int i = 0; i < weakSize; i++) {
            extendedWeak[i] = weak[i];
            extendedWeak[i + weakSize] = weak[i] + n;
        }

        minResult = Integer.MAX_VALUE;
    }

    void findDistCombi(int[] dist, boolean[] used, int selectedId, int[] selected) {
        if(selectedId == selected.length) {
            for(int i = 0; i < weakSize; i++) {
                minResult = Math.min(minResult, available(i, dist, selected));
            }
            return;
        }
        for(int i = 0; i < dist.length; i++) {
            if(used[i])
                continue;
            used[i] = true;
            selected[selectedId] = i;
            findDistCombi(dist, used, selectedId + 1, selected);
            used[i] = false;
        }
    }

    int available(int start, int[] dist, int[] selected) {
        int end = start + weakSize;
        int count = 0;
        int lastLoc = extendedWeak[start] + dist[selected[count++]];
        for(int i = start; i < end; i++) {
            if(extendedWeak[i] > lastLoc) {
                if(++count > dist.length)
                    return Integer.MAX_VALUE;
                lastLoc = extendedWeak[i] + dist[selected[count - 1]];
            }
        }
        return count;
    }
}
