package jun.week3.programmers.불량사용자_박서희;

import java.util.*;

/*
  문제풀이 시간: 00:24:13
  AI 사용 여부: O dfs에서 중복적으로 세게 되는걸 Set으로 해결
 */
public class 불량사용자_박서희 {
    public static void main(String[] args) {
        String[] user_id = {"frodo", "fradi", "crodo", "abc123", "frodoc"};
        String[] banned_id = {"fr*d*", "abc1**"};

        Solution solution = new Solution();
        int answer = solution.solution(user_id, banned_id);
        System.out.println(answer);
    }
}

class Solution {

    static String[] user_id;
    static String[] banned_id;

    static boolean[] selected;
    static Set<String> result = new HashSet<>();

    public int solution(String[] user_id, String[] banned_id) {
        this.user_id = user_id;
        this.banned_id = banned_id;

        selected = new boolean[user_id.length];
        dfs(0);
        return result.size();
    }

    private void dfs(int depth) {
        if (depth == banned_id.length) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    sb.append(i);
                }
            }
            result.add(sb.toString());
            return;
        }

        // 가능한 문자열 찾기
        String cur = banned_id[depth];
        List<Integer> idx = new ArrayList<>();
        for (int i = 0; i < user_id.length; i++) {
            if (user_id[i].length() != cur.length()) continue;
            boolean canMatch = true;
            for (int j = 0; j < user_id[i].length(); j++) {
                if (cur.charAt(j) == '*') continue;
                if (cur.charAt(j) != user_id[i].charAt(j)) canMatch = false;
            }
            if (canMatch) idx.add(i);
        }

        for (Integer i : idx) {
            if (selected[i] == true) continue;
            selected[i] = true;
            dfs(depth + 1);
            selected[i] = false;
        }
    }
}
