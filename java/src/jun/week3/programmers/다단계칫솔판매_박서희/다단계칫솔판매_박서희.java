package jun.week3.programmers.다단계칫솔판매_박서희;

import java.util.*;

/*
  문제풀이 시간: 01:04:15
  AI 사용 여부: X
  판매한 내용을 한 번에 모았다가 풀었더니 틀려서 한 판매 기록마다 계산하는 것으이 수정했음.
 */
public class 다단계칫솔판매_박서희 {
    public static void main(String[] args) {
        String[] enroll = {"john", "mary", "edward", "sam", "emily", "jaimie", "tod", "young"};
        String[] referral = {"-", "-", "mary", "edward", "mary", "mary", "jaimie", "edward"};
        String[] seller = {"young", "john", "tod", "emily", "mary"};
        int[] amount = {12, 4, 2, 5, 10};

        Solution solution = new Solution();
        int[] result = solution.solution(enroll, referral, seller, amount);
        System.out.println(Arrays.toString(result));
    }
}

class Solution {
    String[] enroll;
    Map<String, Person> people;

    public int[] solution(String[] enroll, String[] referral, String[] seller, int[] amount) {
        int[] answer = new int[enroll.length];
        this.enroll = enroll;
        people = new HashMap<>();
        for (int i = 0; i < enroll.length; i++) {
            String name = enroll[i];
            String parent = referral[i];
            people.put(name, new Person(parent));
        }

        for (int i = 0; i < seller.length; i++) {
            dfs(seller[i], 100 * amount[i]);
        }

        for (int i = 0; i < enroll.length; i++) {
            Person now = people.get(enroll[i]);
            answer[i] = now.money;
        }

        return answer;
    }

    private void dfs(String cur, int bonus) {
        int percent10 = (int) (bonus * 0.1);
        if (percent10 >= 1) {
            people.get(cur).money += (bonus - percent10);
            if (!people.get(cur).parent.equals("-")) {
                dfs(people.get(cur).parent, percent10);
            }
        } else {
            people.get(cur).money += bonus;
        }

    }

    class Person {
        int money;
        String parent;

        public Person(String parent) {
            this.money = 0;
            this.parent = parent;
        }
    }
}
