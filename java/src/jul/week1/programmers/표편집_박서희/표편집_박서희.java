package jul.week1.programmers.표편집_박서희;

import java.util.Stack;
import java.util.TreeSet;

/*
  문제풀이 시간: 30분 정도
  AI 사용 여부: O
 */
public class 표편집_박서희 {
    public static void main(String[] args) {
        int n = 8, k = 2;
        String[] cmd = {"D 2", "C", "U 3", "C", "D 4", "C", "U 2", "Z", "Z"};

        Solution solution = new Solution();
        String answer = solution.solution(n, k, cmd);
        System.out.println(answer);
    }
}

/*
"U X": 현재 선택된 행에서 X칸 위에 있는 행을 선택합니다.
"D X": 현재 선택된 행에서 X칸 아래에 있는 행을 선택합니다.
"C" : 현재 선택된 행을 삭제한 후, 바로 아래 행을 선택합니다. 단, 삭제된 행이 가장 마지막 행인 경우 바로 윗 행을 선택합니다.
"Z" : 가장 최근에 삭제된 행을 원래대로 복구합니다. 단, 현재 선택된 행은 바뀌지 않습니다.
 */
class Solution {
    static int n;
    static int k;

    public String solution(int n, int k, String[] cmd) {
        this.n = n;
        this.k = k;

        TreeSet<Integer> treeSet = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            treeSet.add(i);
        }

        Stack<Integer> stack = new Stack<>();

        for (String c : cmd) {
            String[] temp = c.split(" ");
            if (temp[0].equals("U")) {
                int up = Integer.parseInt(temp[1]);
                while (up-- > 0) {
                    int prev = treeSet.lower(k);
                    k = prev;
                }
            } else if (temp[0].equals("D")) {
                int down = Integer.parseInt(temp[1]);
                while (down-- > 0) {
                    int next = treeSet.higher(k);
                    k = next;
                }
            } else if (temp[0].equals("C")) {
                stack.push(k);
                int nextK = (treeSet.higher(k) != null) ? treeSet.higher(k) : treeSet.lower(k);
                treeSet.remove(k);
                k = nextK;
            } else if (temp[0].equals("Z")) {
                if (!stack.isEmpty()) {
                    int save = stack.pop();
                    treeSet.add(save);
                }
            }
        }

        StringBuilder answer = new StringBuilder();
        for (int i = 0; i < n; i++) {
            answer.append("O");
        }

        while (!stack.isEmpty()) {
            int idx = stack.pop();
            answer.setCharAt(idx, 'X');
        }

        return answer.toString();
    }
}
