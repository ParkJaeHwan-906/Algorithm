package jun.week4.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 01:09:51
 * AI 사용 여부 O
 * 문제 조건 중
 * l : 10억
 * t : 10억
 * t의 경우 l과 나머지 연산을 통해 압축이 가능하지만, l의 경우 시프트하게 되는 경우 시간 초과가 예상됨
 * => 해결방법 : l을 자료구조로 두는 것이 아닌 수식으로 손님에게 도착할 수 있는 시간을 구해서 O(1)로 처리가 가능
 * => 먹을 수 있는 시간을 기록해서 예약을 생성 한 뒤, [사진] 이벤트에서 일괄 처리
 */
public class 코드트리오마카세_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int MAKE = 100;
    static final int COME = 200;
    static final int TAKE = 300;

    static class Sushi {
        int inTime;
        int loc;
        String owner;

        Sushi(int inTime, int loc, String owner) {
            this.inTime = inTime;
            this.loc = loc;
            this.owner = owner;
        }
    }

    static class Customer {
        int inTime;
        int loc;
        String name;
        int n;

        Customer(int inTime, int loc, String name, int n) {
            this.inTime = inTime;
            this.loc = loc;
            this.name = name;
            this.n = n;
        }
    }

    static class Remove implements Comparable<Remove>{
        int time;
        String name;

        Remove(int time, String name) {
            this.time = time;
            this.name = name;
        }

        public int compareTo(Remove o) {
            return Integer.compare(this.time, o.time);
        }
    }

    static int l;
    static int sushiCount, customerCount;
    static Map<String, List<Sushi>> sushi;
    static Map<String, Customer> customers;
    static PriorityQueue<Remove> removes;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();

        st = new StringTokenizer(br.readLine().trim());
        l = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());

        sushiCount = 0;
        customerCount = 0;
        sushi = new HashMap<String, List<Sushi>>();
        customers = new HashMap<String, Customer>();
        removes = new PriorityQueue<Remove>();
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());

            int type = Integer.parseInt(st.nextToken());
            if(type == MAKE) { make(st); }
            else if(type == COME) { come(st); }
            else if(type == TAKE) {
                String result = take(st);
                sb.append(result).append('\n');
            }
        }

        System.out.println(sb);
    }

    static void make(StringTokenizer st) {
        int inTime = Integer.parseInt(st.nextToken());
        int loc = Integer.parseInt(st.nextToken());
        String owner = st.nextToken();
        Sushi s = new Sushi(inTime, loc, owner);

        Customer customer = customers.get(owner);
        if(customer != null) {      // 대기중인 손님이 있다면
            int remainTime = getDistBetweenCustomerAndSushi(s.loc, customer.loc);
            int eatableTime = inTime + remainTime;        // 현재 손님이 음식을 먹을 수 있는 시간
            // 삭제 예약
            registRemove(customer.name, eatableTime);
        } else {
            sushi.computeIfAbsent(owner, k -> new ArrayList<>()).add(s);
        }
        sushiCount++;
    }

    static void come(StringTokenizer st) {
        int inTime = Integer.parseInt(st.nextToken());
        int loc = Integer.parseInt(st.nextToken());
        String customer = st.nextToken();
        int n = Integer.parseInt(st.nextToken());
        Customer c = new Customer(inTime, loc, customer, n);
        customers.put(customer, c);
        customerCount++;

        // 해당하는 초밥
        List<Sushi> sushiList = sushi.get(customer);
        if(sushiList != null) {
            for(Sushi s : sushiList) {
                int curLoc = (s.loc + (inTime - s.inTime)) % l;
                int remainTime = getDistBetweenCustomerAndSushi(curLoc, c.loc);
                int eatableTime = inTime + remainTime;
                // 삭제 예약
                registRemove(c.name, eatableTime);
            }
            sushi.remove(c.name);
        }
    }

    static String take(StringTokenizer st) {
        int inTime = Integer.parseInt(st.nextToken());

        // 삭제 예약 반영
        while(!removes.isEmpty() && removes.peek().time <= inTime) {
            Remove remove = removes.poll();

            sushiCount--;
            Customer c = customers.get(remove.name);
            if(c == null)  continue;        // 동일한 손님이 다시 방문하는 경우는 존재하지 않음
            if(--c.n == 0) {
                customers.remove(remove.name);
                customerCount--;
            }
        }

        return String.format("%d %d", customerCount, sushiCount);
    }

    static void registRemove(String name, int time) {
        removes.add(new Remove(time, name));
    }

    static int getDistBetweenCustomerAndSushi(int s, int c) {
        int dist = c - s;       // 손님으로부터 초밥이 떨어진 거리
        if(dist < 0) dist += l; // 한 바퀴 돌아야하는 경우
        return dist;
    }

}
