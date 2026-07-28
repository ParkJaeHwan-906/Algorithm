package jul.week4.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 01:01:33
 * AI 사용 여부 X
 */
public class 산타의선물공장2_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static class Gift {
        int id;
        Gift prev;
        Gift next;

        Gift(int id, Gift prev, Gift next) {
            this.id = id;
            this.prev = prev;
            this.next = next;
        }

        void reset() {
            this.prev = null;
            this.next = null;
        }
    }

    static class Belt {
        int id;
        int gifts;
        Gift head;
        Gift tail;

        Belt(int id, int gifts, Gift head, Gift tail) {
            this.id = id;
            this.gifts = gifts;
            this.head = head;
            this.tail = tail;
        }

        void addLast(Gift g) {      // 맨 뒤에 선물을 추가하는 경우
            if(isEmpty()) {
                head = g;
            } else {
                tail.next = g;
                g.prev = tail;
            }
            tail = g;
            gifts++;
        }

        void addFirst(Gift g) {     // 맨 앞에 선물을 추가하는 경우
            if(isEmpty()) {
                tail = g;
            } else {
                head.prev = g;
                g.next = head;
            }
            head = g;
            gifts++;
        }

        Gift removeFirst() {            // 맨 앞의 선물을 제거하는 경우
            if(isEmpty()) return null;

            Gift oldHead = head;
            Gift newHead = head.next;

            if(newHead != null) {       // 새로운 head 후보가 있는 경우
                newHead.prev = null;
            } else {                    // 없는 경우
                tail = null;
            }

            gifts--;
            head = newHead;
            oldHead.reset();
            return oldHead;
        }

        Gift removeLast() {
            if(isEmpty()) return null;

            Gift oldTail = tail;
            Gift newTail = tail.prev;

            if(newTail != null) {
                newTail.next = null;
            } else {
                head = null;
            }

            gifts--;
            tail = newTail;
            oldTail.reset();
            return oldTail;
        }

        boolean isEmpty() {
            return head == null && tail == null && gifts == 0;
        }

        void init() {
            head = null;
            tail = null;
            gifts = 0;
        }
    }

    static final int INIT = 100;
    static final int MOVE_ALL = 200;
    static final int CHANGE_FRONT = 300;
    static final int DIVIDE = 400;
    static final int QUERY_GIFT = 500;
    static final int QUERY_BELT = 600;

    static int n, m;
    static Belt[] belts;
    static Gift[] gifts;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());

            if(type == INIT) { init(st); }
            else if(type == MOVE_ALL) {
                int result = moveAll(st);
                sb.append(result).append("\n");
            }
            else if(type == CHANGE_FRONT) {
                int result = changeFront(st);
                sb.append(result).append("\n");
            }
            else if(type == DIVIDE) {
                int result = divide(st);
                sb.append(result).append("\n");
            }
            else if(type == QUERY_GIFT) {
                int result = QueryGift(st);
                sb.append(result).append("\n");
            }
            else if(type == QUERY_BELT) {
                int result = QueryBelt(st);
                sb.append(result).append("\n");
            }
        }
        System.out.print(sb);
    }

    static void init(StringTokenizer st) {
        n = Integer.parseInt(st.nextToken());       // 벨트 수
        m = Integer.parseInt(st.nextToken());       // 선물 수
        belts = new Belt[n + 1];
        for(int i = 1; i < n + 1; i++) belts[i] = new Belt(i, 0, null, null);
        gifts = new Gift[m + 1];
        for(int i = 1; i < m + 1; i++) {
            int bId = Integer.parseInt(st.nextToken());
            gifts[i] = new Gift(i, null, null);
            belts[bId].addLast(gifts[i]);
        }
    }

    static int moveAll(StringTokenizer st) {
        int src = Integer.parseInt(st.nextToken());
        int dst = Integer.parseInt(st.nextToken());
        // 하나씩 옮기면 안되고, 포인터 번경
        if(belts[src].isEmpty()) return belts[dst].gifts;
        Gift srcHead = belts[src].head;
        Gift srcTail = belts[src].tail;

        if(belts[dst].isEmpty()) {
            belts[dst].head = srcHead;
            belts[dst].tail = srcTail;
            belts[dst].gifts = belts[src].gifts;
        } else {
            belts[dst].head.prev = srcTail;
            srcTail.next = belts[dst].head;
            belts[dst].head = srcHead;
            belts[dst].gifts += belts[src].gifts;
        }

        belts[src].init();
        return belts[dst].gifts;
    }

    static int changeFront(StringTokenizer st) {
        int src = Integer.parseInt(st.nextToken());
        int dst = Integer.parseInt(st.nextToken());

        Gift srcHead = belts[src].removeFirst();
        Gift dstHead = belts[dst].removeFirst();

        if(srcHead != null) belts[dst].addFirst(srcHead);
        if(dstHead != null) belts[src].addFirst(dstHead);

        return belts[dst].gifts;
    }

    static int divide(StringTokenizer st) {
        int src = Integer.parseInt(st.nextToken());
        int dst = Integer.parseInt(st.nextToken());

        int cnt = belts[src].gifts / 2;
        if(cnt == 0) return belts[dst].gifts;

        Gift[] temp = new Gift[cnt];
        for(int i = 0; i < cnt; i++) {
            temp[i] = belts[src].removeFirst();
        }
        for(int i = cnt - 1; i >= 0; i--) {
            belts[dst].addFirst(temp[i]);
        }

        return belts[dst].gifts;
    }

    static int QueryGift(StringTokenizer st) {
        int gId = Integer.parseInt(st.nextToken());
        Gift g = gifts[gId];
        int a = g.prev == null ? -1 : g.prev.id;
        int b = g.next == null ? -1 : g.next.id;
        return a + 2 * b;
    }

    static int QueryBelt(StringTokenizer st) {
        int bId = Integer.parseInt(st.nextToken());
        Belt belt = belts[bId];
        int a = belt.head == null ? -1 : belt.head.id;
        int b = belt.tail == null ? -1 : belt.tail.id;
        int c = belt.gifts;
        return a + 2 * b + 3 * c;
    }
}
