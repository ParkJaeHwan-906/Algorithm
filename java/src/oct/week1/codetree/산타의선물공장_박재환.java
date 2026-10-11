package oct.week1.codetree;

import java.util.*;
import java.io.*;

public class 산타의선물공장_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int SET = 100;
    static final int DEL_FIRST = 200;
    static final int DEL_ID = 300;
    static final int GIFT_QRY = 400;
    static final int BROKE = 500;

    static class Gift {
        int id;
        int weight;
        Gift prev;
        Gift next;
        Gift(int id, int weight) {
            this.id = id;
            this.weight = weight;
            this.prev = null;
            this.next = null;
        }
        void reset() {
            prev = null;
            next = null;
        }
    }

    static class Belt {
        Gift head;
        Gift tail;
        boolean broke;
        Set<Integer> giftIds;
        Belt() {
            this.head = null;
            this.tail = null;
            this.broke = false;
            this.giftIds = new HashSet<>();
        }
        void offerLast(Gift gift) {
            if(head == null) {
                head = gift;
            } else {
                tail.next = gift;
                gift.prev = tail;
            }
            tail = gift;
            giftIds.add(gift.id);
        }
        Gift pollFirst() {
            if(head == null) {
                return null;
            }
            if(head == tail) {
                Gift gift = head;
                reset();
                return gift;
            }
            Gift curHead = head;
            Gift nextHead = head.next;
            nextHead.prev = null;
            head = nextHead;
            curHead.reset();
            giftIds.remove(curHead.id);
            return curHead;
        }

        void reset() {
            head = null;
            tail = null;
            giftIds.clear();
        }
    }

    static int n, m;
    static Map<Integer, Gift> gifts;
    static Belt[] belts;
    static Set<Integer> removedGiftId;
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
            else if(type == DEL_FIRST) {
                sb.append(delFirst(st)).append('\n');
            }
            else if(type == DEL_ID) {
                sb.append(delId(st)).append('\n');
            }
            else if(type == GIFT_QRY) {
                sb.append(giftQry(st)).append('\n');
            }
            else if(type == BROKE) {
                sb.append(broke(st)).append('\n');
            }
        }
        System.out.print(sb);
    }

    static void set(StringTokenizer st) {
        removedGiftId = new HashSet<>();
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        belts = new Belt[m + 1];
        for(int i = 1; i <= m; i++) {
            belts[i] = new Belt();
        }
        gifts = new HashMap<>();
        int[] ids = new int[n];
        for(int i = 0; i < n; i++) {
            ids[i] = Integer.parseInt(st.nextToken());
        }
        int[] weights = new int[n];
        for(int i = 0; i < n; i++) {
            weights[i] = Integer.parseInt(st.nextToken());
        }
        for(int i = 0; i < n; i++) {
            int id = ids[i];
            int weight = weights[i];
            Gift gift = new Gift(id, weight);
            gifts.put(id, gift);
            belts[(i / (n / m)) + 1].offerLast(gift);
        }
    }

    static long delFirst(StringTokenizer st) {
        long totalWeight = 0L;
        int weight = Integer.parseInt(st.nextToken());
        for(int i = 1; i <= m; i++) {
            if(belts[i].head == null) {
                continue;
            }
            Gift gift = belts[i].pollFirst();
            if(gift.weight <= weight) {
                totalWeight += gift.weight;
                gifts.remove(gift.id);
                removedGiftId.add(gift.id);
            } else {
                belts[i].offerLast(gift);
            }
        }
        return totalWeight;
    }

    static int delId(StringTokenizer st) {
        int giftId = Integer.parseInt(st.nextToken());
        if(!gifts.containsKey(giftId)) {
            return -1;
        }
        Gift gift = gifts.get(giftId);
        Gift prev = gift.prev;
        Gift next = gift.next;
        if(prev != null) {
            prev.next = next;
        }
        if(next != null) {
            next.prev = prev;
        }
        applyRemovedGift(gift.id);
        gift.reset();
        return gift.id;
    }

    static int giftQry(StringTokenizer st) {
        int giftId = Integer.parseInt(st.nextToken());
        for(int i = 1; i <= m; i++) {
            if(belts[i].broke) {
                continue;
            }
            if(belts[i].giftIds.contains(giftId)) {
                Gift newHead = gifts.get(giftId);
                if(newHead != belts[i].head) {
                    Gift newTail = gifts.get(giftId).prev;
                    Gift curHead = belts[i].head;
                    Gift curTail = belts[i].tail;

                    newHead.prev = null;
                    belts[i].head = newHead;
                    curHead.prev = curTail;
                    curTail.next = curHead;
                    newTail.next = null;
                    belts[i].tail = newTail;
                }
                return i;
            }
        }
        return -1;
    }

    static int broke(StringTokenizer st) {
        int beltId = Integer.parseInt(st.nextToken());
        if(belts[beltId].broke) {
            return -1;
        }
        Belt brokeBelt = belts[beltId];
        if(!brokeBelt.giftIds.isEmpty()) {
            for (int i = 1; i <= m; i++) {
                if (belts[((beltId - 1) + i) % m + 1].broke) {
                    continue;
                }

                Belt belt = belts[((beltId - 1) + i) % m + 1];
                if(belt.giftIds.isEmpty()) {
                    belt.head = brokeBelt.head;
                    belt.tail = brokeBelt.tail;
                } else {
                    belt.tail.next = brokeBelt.head;
                    brokeBelt.head.prev = belt.tail;
                    belt.tail = brokeBelt.tail;
                }
                belt.giftIds.addAll(brokeBelt.giftIds);
                break;
            }
        }
        brokeBelt.reset();
        brokeBelt.broke = true;
        return beltId;
    }

    static void printState() {
        for(int i = 1; i <= m; i++) {
            System.out.printf("[belt %d] : %s\n", i, belts[i].giftIds);
        }
    }

    static void applyRemovedGift(int giftId) {
        for(int i = 1; i <= m; i++) {
            if(belts[i].broke) {
                continue;
            }
            if(belts[i].giftIds.contains(giftId)) {
                belts[i].giftIds.remove(giftId);
                gifts.remove(giftId);
                if(belts[i].head.id == giftId) {
                    belts[i].head = belts[i].head.next;
                }
                if(belts[i].tail.id == giftId) {
                    belts[i].tail = belts[i].tail.prev;
                }
                break;
            }
        }
    }
}
