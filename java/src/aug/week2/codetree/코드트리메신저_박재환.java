package aug.week2.codetree;

import java.util.*;
import java.io.*;

public class 코드트리메신저_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int SET = 100;
    static final int ON_OFF = 200;
    static final int CHANGE_AUTHORITY = 300;
    static final int CHANGE_PARENT = 400;
    static final int QUERY = 500;
    static final int MAX_AUTHORITY = 20;        // 최대 트리 깊이 20

    static int n;
    static ChatRoom[] rooms;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());

        while(q-- > 0) {
             st = new StringTokenizer(br.readLine().trim());
             int type = Integer.parseInt(st.nextToken());
             if(type == SET) { set(st); }
             else if(type == ON_OFF) { onOff(st); }
             else if(type == CHANGE_AUTHORITY) { changeAuthority(st); }
             else if(type == CHANGE_PARENT) { changeParent(st); }
             else if(type == QUERY) {
                 int result = query(st);
                 sb.append(result).append("\n");
             }
        }

        System.out.print(sb);
    }

    static class ChatRoom {
        int pId;            // 부모 id
        int authority;      // 권한 세기
        boolean on;         // 알림망 설정
        int[] remains;      // 남은 권한별 전파 가능한 알림 수
        int received;       // 전파 받는 알림 수

        ChatRoom(int pId, int authority) {
            this.pId = pId;
            this.authority = authority;
            this.on = true;
            this.remains = new int[MAX_AUTHORITY + 1];
            this.received = 0;
        }

        void updateAuthority(int authority) {
            this.authority = authority;
            this.authority = Math.min(this.authority, MAX_AUTHORITY);
        }

        void onOff() {
            this.on = !this.on;
        }

        void updatePId(int pId) {
            this.pId = pId;
        }
    }

    static void updatePropagation(int cId, int flag) {
        /**
         * 전체 알림 전파
         */
        int pId = rooms[cId].pId;
        int level = 1;

        while(pId != -1 && level <= MAX_AUTHORITY) {
            for(int i = level; i < MAX_AUTHORITY + 1; i++) {
                int count = rooms[cId].remains[i];
                // [cId] 에서 [i] 칸 만큼 더 이동할 수 있는 알림의 수
                rooms[pId].remains[i - level] += flag * count;
                rooms[pId].received += flag * count;
            }

            // 현재 부모까지는 알림이 도착하고, 그 위부터 전파가 중단됩니다.
            if(!rooms[pId].on) break;
            pId = rooms[pId].pId;
            level++;
        }
    }

    static void updateOnePropagation(int cId, int authority, int flag) {
        /**
         * 현재 채팅방 알림 하나만 전파
         */
        if(!rooms[cId].on) return;      // 더 이상 전파하지 않는 경우

        int pId = rooms[cId].pId;
        int remains = authority - 1;

        while(pId != -1 && remains >= 0) {
            rooms[pId].remains[remains] += flag;
            rooms[pId].received += flag;

            if(!rooms[pId].on) break;
            pId = rooms[pId].pId;
            remains--;
        }
    }

    static void set(StringTokenizer st) {
        rooms = new ChatRoom[n + 1];
        rooms[0] = new ChatRoom(-1, 0);     // root 는 항상 0
        for(int i = 1; i < n + 1; i++) {
            int pId = Integer.parseInt(st.nextToken());
            rooms[i] = new ChatRoom(pId, 0);
        }
        for(int i = 1; i < n + 1; i++) {
            int authority = Integer.parseInt(st.nextToken());
            rooms[i].updateAuthority(authority);
            rooms[i].remains[rooms[i].authority]++;
        }

        // 각 채팅방에서 발생한 알림을 하나씩 전파
        for(int i = 1; i < n + 1; i++) {
            updateOnePropagation(i, rooms[i].authority, 1);
        }
    }

    static void onOff(StringTokenizer st) {
        int cId = Integer.parseInt(st.nextToken());
        if(rooms[cId].on) {
            updatePropagation(cId, -1);
        } else {
            updatePropagation(cId, 1);
        }
        rooms[cId].onOff();
    }

    static void changeAuthority(StringTokenizer st) {
        int cId = Integer.parseInt(st.nextToken());
        int authority = Integer.parseInt(st.nextToken());
        int beforeAuthority = rooms[cId].authority;

        // 현재 채팅방에서 발생한 기존 알림을 제거
        updateOnePropagation(cId, beforeAuthority, -1);
        rooms[cId].remains[beforeAuthority]--;

        rooms[cId].updateAuthority(authority);
        rooms[cId].remains[rooms[cId].authority]++;

        // 변경된 권한으로 현재 채팅방의 알림을 다시 전파합니다.
        updateOnePropagation(cId, rooms[cId].authority, 1);
    }

    static void changeParent(StringTokenizer st) {
        int c1 = Integer.parseInt(st.nextToken());
        int c2 = Integer.parseInt(st.nextToken());
        int c1PId = rooms[c1].pId;
        int c2PId = rooms[c2].pId;

        // 기존 부모 경로에서 두 채팅방의 전체 알림을 제거합니다.
        if(rooms[c1].on) updatePropagation(c1, -1);
        if(rooms[c2].on) updatePropagation(c2, -1);

        rooms[c1].updatePId(c2PId);
        rooms[c2].updatePId(c1PId);

        // 변경된 부모 경로로 두 채팅방의 전체 알림을 전파합니다.
        if(rooms[c1].on) updatePropagation(c1, 1);
        if(rooms[c2].on) updatePropagation(c2, 1);
    }

    static int query(StringTokenizer st) {
        int cId = Integer.parseInt(st.nextToken());
        return rooms[cId].received;
    }
}
