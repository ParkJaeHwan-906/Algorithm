package sep.week3.codetree;

import java.util.*;
import java.io.*;

public class 코드트리투어_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int INF = Integer.MAX_VALUE;

    static final int SET = 100;
    static final int CREATE = 200;
    static final int DELETE = 300;
    static final int SELL = 400;
    static final int CHANGE = 500;

    static class Edge implements Comparable<Edge> {
        int to;
        int val;
        Edge(int to, int val) {
            this.to = to;
            this.val = val;
        }
        @Override
        public int compareTo(Edge o) {
            return Integer.compare(this.val, o.val);
        }
    }

    static int n, m;
    static List<Edge>[] connections;
    static int[] routes;
    static Map<Integer, Item> items;
    static PriorityQueue<Item> priorityItems;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            if(type == SET) {
                set(st);
            }
            else if(type == CREATE) {
                create(st);
            }
            else if (type == DELETE) {
                delete(st);
            }
            else if (type == SELL) {
                sb.append(sell()).append('\n');
            }
            else if (type == CHANGE) {
                change(st);
            }
        }
        System.out.print(sb);
    }

    static void set(StringTokenizer st) {
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        connections = new List[n];
        for(int i = 0; i < n; i++) {
            connections[i] = new ArrayList<>();
        }
        for(int i = 0; i < m; i++) {
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int val = Integer.parseInt(st.nextToken());
            connections[from].add(new Edge(to, val));
            connections[to].add(new Edge(from, val));
        }
        calcRoute(0);               // 초기 시작 위치는 0

        items = new HashMap<>();
        priorityItems = new PriorityQueue<>();
    }

    static class Item implements Comparable<Item> {
        int id;
        int revenue;
        int dest;
        int realCost;
        boolean removed;
        Item(int id, int revenue, int dest, int realCost) {
            this.id = id;
            this.revenue = revenue;
            this.dest = dest;
            this.realCost = realCost;
            this.removed = false;
        }
        @Override
        public int compareTo(Item o) {
            int thisProfit = this.revenue - this.realCost;
            int otherProfit = o.revenue - o.realCost;
            if(thisProfit != otherProfit) {
                return Integer.compare(otherProfit, thisProfit);
            }
            return Integer.compare(this.id, o.id);
        }
    }

    static void create(StringTokenizer st) {
        int id = Integer.parseInt(st.nextToken());
        int revenue = Integer.parseInt(st.nextToken());
        int dest =  Integer.parseInt(st.nextToken());
        Item item = new Item(id, revenue, dest, routes[dest]);
        items.put(id, item);
        priorityItems.offer(item);
    }

    static void delete(StringTokenizer st) {
        int id = Integer.parseInt(st.nextToken());
        Item item = items.get(id);
        if(item == null) {
            return;
        }
        item.removed = true;
        items.remove(id);
    }

    static int sell() {
        // 쓰레기 정리 (삭제된 상품)
        while(!priorityItems.isEmpty() && priorityItems.peek().removed) {
            priorityItems.poll();
        }
        if(priorityItems.isEmpty()) {
            return -1;
        }
        Item item = priorityItems.peek();
        int profit = item.revenue - item.realCost;
        if(profit < 0) {
            return -1;
        }
        priorityItems.poll();
        items.remove(item.id);
        return item.id;
    }

    static void change(StringTokenizer st) {
        int s = Integer.parseInt(st.nextToken());
        calcRoute(s);
        // 상품 갱신
        priorityItems.clear();
        for(Item item : items.values()) {
            item.realCost = routes[item.dest];
            priorityItems.offer(item);
        }
    }

    // ======================================================
    // 경로 계산
    // ======================================================
    static void calcRoute(int s) {
        routes = new int[n];
        Arrays.fill(routes, INF);

        PriorityQueue<Edge> pq = new PriorityQueue<>();
        pq.offer(new Edge(s, 0));
        routes[s] = 0;
        while(!pq.isEmpty()) {
            Edge cur = pq.poll();
            if(routes[cur.to] < cur.val) {      // 이전의 최적해가 있는 경우
                continue;
            }
            for(Edge next : connections[cur.to]) {
                if(routes[next.to] > cur.val + next.val) {
                    routes[next.to] = cur.val + next.val;
                    pq.offer(new Edge(next.to, routes[next.to]));
                }
            }
        }
    }
}
