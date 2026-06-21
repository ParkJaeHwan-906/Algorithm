package jun.week3.programmers.불량사용자_박재환;

import java.util.HashSet;
import java.util.Set;

/**
 * AI 사용 여부 O
 * => duplicated 애서 값이 정확하게 기록되지 않는 문제가 있었음
 *      => duplicated.add(selected) 로 저장했었는데, 백트래킹 과정에서 연쇄적으로 반영되며 정확하게 기록되지 않았음
 *          => new HashSet 으로 새로운 객체로 생성해서 해결
 */
public class 불량사용자_박재환 {
    public static void main(String[] args) {
        String[] user_id = {"frodo", "fradi", "crodo", "abc123", "frodoc"};
        String[] banned_id = {"fr*d*", "abc1**"};

        Solution solution = new Solution();
        System.out.println(solution.solution(user_id, banned_id));
    }
}

class Solution {
    public int solution(String[] user_id, String[] banned_id) {
        Set<String>[] set = new Set[banned_id.length];

        for(int i = 0; i < banned_id.length; i++) {
            set[i] = new HashSet<>();
            String banned = banned_id[i];
            for(String id : user_id) {
                if(set[i].contains(id) || !isBan(banned, id)) continue;
                set[i].add(id);
            }
        }

        Set<Set<String>> duplicated = new HashSet<>();
        combi(0, set, new HashSet<>(), duplicated);
        return duplicated.size();
    }

    boolean isBan(String banned, String id) {
        if(banned.length() != id.length()) return false;

        for(int i = 0; i < banned.length(); i++) {
            if(banned.charAt(i) != '*' &&
                    banned.charAt(i) != id.charAt(i)) return false;
        }

        return true;
    }

    void combi(int bid, Set<String>[] set, Set<String> selected, Set<Set<String>> duplicatedSet) {
        if(bid == set.length) {
            duplicatedSet.add(new HashSet<>(selected));
            return;
        }

        for(String id : set[bid]) {
            if(!selected.add(id))  continue;

            combi(bid + 1, set, selected, duplicatedSet);
            selected.remove(id);
        }
    }
}
