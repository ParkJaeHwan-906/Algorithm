package jun.week3.programmers.다단계칫솔판매_박재환;

import java.util.*;

/**
 * [풀이 시간]
 * 00:36:33
 * AI 사용 여부 X
 * => 이게 왜 되는지 모르겠음
 *      enroll이 최대 10,000, seller은 최대 100,000
 *      -> 편향트리라고 가정하고, 항상 말단 노드만 Seller를 한다면 10,000 x 100,000 = 1,000,000,000 인데?
 *
 *      => AI : amount 의 원소 최대값이 100이므로, price 는 최대 10,000
 *              -> 10% 씩 분배한다고 했을 때 ( 10,000 -> 1,000 -> 100 -> 10 -> 1 ) 최대 5회 전파이기 때문에 가능
 */
public class 다단계칫솔판매_박재환 {
    public static void main(String[] args) {
        String[] enroll = {"john", "mary", "edward", "sam", "emily", "jaimie", "tod", "young"};
        String[] referral = {"-", "-", "mary", "edward", "mary", "mary", "jaimie", "edward"};
        String[] seller = {"young", "john", "tod", "emily", "mary"};
        int[] amount = {12, 4, 2, 5, 10};

        Solution sol = new Solution();

        System.out.println(Arrays.toString(sol.solution(enroll, referral, seller, amount)));
    }
}

class Solution {
    final int TOOTH_BRUSH = 100;

    class Member {
        String name;        // 이름
        int total;         // 총 액수

        String referral;    // 추천인

        Member(String name, int total, String referral) {
            this.name = name;
            this.total = total;
            this.referral = referral;
        }

        int distribute(int price) {
            int sub =  price / 10;

            this.total += (price - sub);
            return sub;
        }
    }
    Map<String, Member> nameToMember;
    public int[] solution(String[] enroll, String[] referral, String[] seller, int[] amount) {
        set(enroll, referral);

        for(int i = 0; i < seller.length; i++) {
            String name = seller[i];
            Member member = nameToMember.get(name);
            propagation(amount[i], member);
        }

        int[] result = new int[enroll.length];
        for(int i = 0; i < enroll.length; i++) {
            result[i] = nameToMember.get(enroll[i]).total;
        }
        return result;
    }

    void set(String[] enroll, String[] referral) {
        nameToMember = new HashMap<>();
        for(int i = 0; i < enroll.length; i++){
            String cur = enroll[i];
            String prev = referral[i];

            Member member = new Member(cur, 0, prev);
            nameToMember.put(cur, member);
        }
    }

    void propagation(int amount, Member member) {
        int price = amount * TOOTH_BRUSH;

        while(true) {
            int sub = member.distribute(price);
            if(sub < 1) break;

            price = sub;
            if(member.referral.equals("-")) break;

            Member prev = nameToMember.get(member.referral);
            if(prev == null) break;
            member = prev;
        }
    }
}
