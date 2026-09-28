package sep.week4.codetree;

import java.util.*;
import java.io.*;

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
        int time;
        int loc;
        Sushi(int time, int loc) {
            this.time = time;
            this.loc = loc;
        }
    }

    static class Customer {
        int time;
        int loc;
        int count;
        Customer(int time, int loc, int count) {
            this.time = time;
            this.loc = loc;
            this.count = count;
        }
    }

    static class Eat implements Comparable<Eat> {
        String name;
        int time;
        Eat(String name, int time) {
            this.name = name;
            this.time = time;
        }
        @Override
        public int compareTo(Eat o) {
            return Integer.compare(this.time, o.time);
        }
    }

    static int l, q;
    static Map<String, List<Sushi>> sushi;
    static int sushiCount;
    static Map<String, Customer> customers;
    static int customerCount;
    static PriorityQueue<Eat> eats;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        st = new StringTokenizer(br.readLine().trim());
        l = Integer.parseInt(st.nextToken());           // 벨트 길이
        q = Integer.parseInt(st.nextToken());           // 명령어 수

        sushi = new HashMap<>();
        sushiCount = 0;
        customers = new HashMap<>();
        customerCount = 0;
        eats = new PriorityQueue<>();
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            if(type == MAKE) {
                make(st);
            }
            else if(type == COME) {
                come(st);
            }
            else if(type == TAKE) {
                sb.append(take(st));
            }
        }
        System.out.print(sb);
    }

    static void make(StringTokenizer st) {
        int t = Integer.parseInt(st.nextToken());
        int x = Integer.parseInt(st.nextToken());
        String name = st.nextToken();
        Sushi s = new Sushi(t, x);
        if(customers.containsKey(name)) {       // 이미 손님이 대기중이라면
            int eatableTime = t + (customers.get(name).loc - s.loc + l) % l;
            Eat eat = new Eat(name, eatableTime);
            eats.offer(eat);
        } else {                                // 아직 손님이 없는 경우
            sushi.computeIfAbsent(name, k -> new ArrayList<>()).add(s);
        }
        sushiCount++;
    }

    static void come(StringTokenizer st) {
        int t = Integer.parseInt(st.nextToken());
        int x = Integer.parseInt(st.nextToken());
        String name = st.nextToken();
        int n = Integer.parseInt(st.nextToken());
        Customer c = new Customer(t, x, n);
        if(sushi.containsKey(name)) {       // 미리 대기중인 초밥이 있는 경우
            for(Sushi s : sushi.get(name)) {
                int eatableTime = t + (c.loc - ((s.loc + (t - s.time)) % l) + l) % l;
                Eat eat = new Eat(name, eatableTime);
                eats.offer(eat);
            }
            sushi.remove(name);
        }
        customers.put(name, c);
        customerCount++;
    }

    static String take(StringTokenizer st) {
        int t = Integer.parseInt(st.nextToken());
        while(!eats.isEmpty() && eats.peek().time <= t) {
            Eat eat = eats.poll();
            if(customers.containsKey(eat.name)) {
                Customer c = customers.get(eat.name);
                if(--c.count == 0) {
                    customers.remove(eat.name);
                    customerCount--;
                }
                sushiCount--;
            }
        }
        return String.format("%d %d\n", customerCount, sushiCount);
    }
}
