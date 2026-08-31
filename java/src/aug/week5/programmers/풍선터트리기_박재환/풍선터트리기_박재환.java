package aug.week5.programmers.풍선터트리기_박재환;

public class 풍선터트리기_박재환 {
    public static void main(String[] args) {
        int[] a = {-16, 27, 65, -2, 58, -92, -71, -68, -61, -33};

        Solution solution = new Solution();
        System.out.println(solution.solution(a)); // 기대값: 6
    }
}

class Solution {
    public int solution(int[] a) {
        int n = a.length;

        // 풍선이 1~2개라면 모든 풍선을 마지막까지 남길 수 있습니다.
        if (n <= 2) {
            return n;
        }

        // rightMin[i]에는 i번 풍선부터 마지막 풍선까지의 최솟값을 저장합니다.
        int[] rightMin = new int[n];
        rightMin[n - 1] = a[n - 1];

        // 오른쪽에서 왼쪽으로 이동하면서 rightMin을 완성합니다.
        // rightMin[i]는 a[i]와 rightMin[i + 1] 중 더 작은 값입니다.
        for(int i = n - 2; i >= 0; i--) {
            rightMin[i] = Math.min(rightMin[i + 1], a[i]);
        }

        // 양 끝 풍선은 비교할 풍선이 한쪽에만 있으므로 항상 생존 가능합니다.
        int answer = 2;

        // 현재 위치보다 왼쪽에 있는 풍선들의 최솟값입니다.
        int leftMin = a[0];

        for (int i = 1; i < n - 1; i++) {
            // a[i]의 양쪽에 모두 a[i]보다 작은 풍선이 있는지 확인합니다.
            // 왼쪽 최솟값: leftMin
            // 오른쪽 최솟값: rightMin[i + 1]
            // 양쪽 모두에 더 작은 값이 있으면 작은 풍선을 두 번 터뜨려야 하므로
            // a[i]는 마지막까지 살아남을 수 없습니다.
            boolean hasSmallerLeft = a[i] > leftMin;
            boolean hasSmallerRight = a[i] > rightMin[i + 1];

            // a[i]가 살아남을 수 있다면 answer를 1 증가시킵니다.
            if (!(hasSmallerLeft && hasSmallerRight)) {
                answer++;
            }
            // 다음 위치로 이동하기 전에 leftMin을 갱신합니다.
            leftMin = Math.min(leftMin, a[i]);
        }

        return answer;
    }
}
