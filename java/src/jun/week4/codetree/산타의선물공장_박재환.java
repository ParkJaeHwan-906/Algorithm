package jun.week4.codetree;

import java.util.*;
import java.io.*;

/**
 * AI 사용 여부 O
 * => broke() 메서드에서 고장나는 벨트에 선물이 없는 경우를 고려하지 못함 => NullPointExcetpion
 * => takeOff() 메서드 반환값을 int 로 해서 오버플로우발생
 */
public class 산타의선물공장_박재환 {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int SET = 100;
    static final int TAKE_OFF = 200;
    static final int REMOVE = 300;
    static final int CHECK = 400;
    static final int BROKE = 500;

    static class Belt {
        Gift head;
        Gift tail;

        boolean isBroke;

        Belt() {
            this.head = null;
            this.tail = null;
            this.isBroke = false;
        }

        void offerLast(Gift g) {
            if(this.head == null) {     // 비어있는 상태
                this.head = g;
                this.tail = g;
                return;
            }

            // 비어있지 않다면 연결
            Gift tail = this.tail;
            tail.next = g;
            g.prev = tail;
            this.tail = g;
        }

        Gift pollFirst() {
            Gift head = this.head;
            Gift nextHead = head.next;
            head.reset();
            if(nextHead == null) {      // 현재 선물이 1개 밖에 없는 경우
                this.tail = null;
            } else {
                nextHead.prev = null;
            }
            this.head = nextHead;
            return head;
        }

        boolean isEmpty() {
            return this.head == null;
        }

        void reset() {
            this.head = null;
            this.tail = null;
        }
    }

    static class Gift {
        int id;
        int weight;

        int beltId;

        Gift prev;
        Gift next;

        Gift(int id, int weight, int beltId) {
            this.id = id;
            this.weight = weight;
            this.beltId = beltId;
        }

        void reset() {
            this.next = null;
            this.prev = null;
        }
    }

    static int n, m;
    static Belt[] belts;
    static Map<Integer, Gift> idToGift;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();

        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());

            int type = Integer.parseInt(st.nextToken());
            if(type == SET) { set(st); }
            else if(type == TAKE_OFF) {
                long result = takeOff(st);
                sb.append(result).append('\n');
            }
            else if(type == REMOVE) {
                int result = remove(st);
                sb.append(result).append('\n');
            }
            else if(type == CHECK) {
                int result = check(st);
                sb.append(result).append('\n');
            }
            else if(type == BROKE) {
                int result = broke(st);
                sb.append(result).append('\n');
            }
        }

//        printBelt();
        System.out.println(sb);
    }

    static void set(StringTokenizer st) {
        n = Integer.parseInt(st.nextToken());       // 선물의 개수
        m = Integer.parseInt(st.nextToken());       // 벨트 개수

        belts = new Belt[m];
        for(int i = 0; i < m; i++) belts[i] = new Belt();
        idToGift = new HashMap<>();

        int[] ids = new int[n];
        for(int i = 0; i < n; i++) {
            ids[i] = Integer.parseInt(st.nextToken());
        }
        int[] weights = new int[n];
        for(int i = 0; i < n; i++) {
            weights[i] = Integer.parseInt(st.nextToken());
        }

        int limit = n / m;
        for(int i = 0; i < n; i++) {
            int beltId = i / limit;
            Gift gift = new Gift(ids[i], weights[i], beltId);
            idToGift.put(gift.id, gift);

            Belt belt = belts[beltId];
            belt.offerLast(gift);
        }
    }

    static long takeOff(StringTokenizer st) {
        int wMax = Integer.parseInt(st.nextToken());
        long totalWeight = 0;
        for(int i = 0; i < m; i++) {
            if(belts[i].isBroke || belts[i].isEmpty()) continue;
            Gift gift = belts[i].pollFirst();
            if(gift.weight <= wMax) {
                totalWeight += gift.weight;
                idToGift.remove(gift.id);
            }
            else belts[i].offerLast(gift);
        }
        return totalWeight;
    }

    static int remove(StringTokenizer st) {
        int rid = Integer.parseInt(st.nextToken());
        Gift gift = idToGift.get(rid);
        if(gift == null) return -1;

        Gift prev = gift.prev;
        Gift next = gift.next;

        // 서로 연결
        if(prev != null) prev.next = next;
        else belts[gift.beltId].head = next;

        if(next != null) next.prev = prev;
        else belts[gift.beltId].tail = prev;

        idToGift.remove(gift.id);
        gift.reset();
        return gift.id;
    }

    static int check(StringTokenizer st) {
        int fid = Integer.parseInt(st.nextToken());
        Gift gift = idToGift.get(fid);
        if(gift == null) return -1;

        // gift부터 맨 앞으로 끌고옴
        Belt belt = belts[gift.beltId];
        if(belt.head == gift) return gift.beltId + 1;       // 옮길 필요가 없음
        Gift oldHead = belt.head;
        Gift oldTail = belt.tail;

        Gift newTail = gift.prev;

        oldHead.prev = oldTail;
        oldTail.next = oldHead;

        newTail.next = null;
        gift.prev = null;

        belt.tail = newTail;
        belt.head = gift;

        return gift.beltId + 1;     // 0 - based 보정
    }

    static int broke(StringTokenizer st) {
        int bid = Integer.parseInt(st.nextToken()) - 1;     // 0 - based 보정
        if(belts[bid].isBroke) return -1;
        belts[bid].isBroke = true;
        if(belts[bid].isEmpty()) return bid + 1;
        for(int i = 1; i < m; i++) {
            if(belts[(i + bid) % m].isBroke) continue;

            // 옮길 수 있음
            Gift brokeBeltHead = belts[bid].head;
            Gift brokeBeltTail = belts[bid].tail;

            if(belts[(i + bid) % m].isEmpty()) {
                belts[(i + bid) % m].head = brokeBeltHead;
                belts[(i + bid) % m].tail = brokeBeltTail;
            } else {
                Gift newBeltHead = belts[(i + bid) % m].head;
                Gift newBeltTail = belts[(i + bid) % m].tail;

                newBeltTail.next = brokeBeltHead;
                brokeBeltHead.prev = newBeltTail;
                belts[(i + bid) % m].tail = brokeBeltTail;
            }
            // Gifts beltId 반영
            while(brokeBeltHead != null) {
                brokeBeltHead.beltId = (i + bid) % m;
                brokeBeltHead = brokeBeltHead.next;
            }
            belts[bid].reset();
            break;
        }
        return bid + 1;     // 0 - based 보정
    }

    static void printBelt() {
        for(int i = 0; i < m; i++) {
            System.out.printf("벨트 id : %d\n", i + 1);
            if(belts[i].head == null) System.out.println("{ EMPTY }");
            else {
                Gift head = belts[i].head;

                while(head.next != null) {
                    System.out.printf("선물 id : %d, 무게 : %d  ->  ", head.id, head.weight);
                    head = head.next;
                }
                System.out.printf("선물 id : %d, 무게 : %d", head.id, head.weight);
                System.out.println();
            }
            System.out.println();
        }
    }
}
