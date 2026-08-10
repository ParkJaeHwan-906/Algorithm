package aug.week2.codetree;

import java.util.*;
import java.io.*;

public class 코드트리메신저_재시도_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int MAX_LEVEL = 20;        // 트리의 높이는 최대 20

    static final int SET = 100;
    static final int ON_OFF = 200;
    static final int CHANGE_AUTHORITY = 300;
    static final int CHANGE_PARENT = 400;
    static final int QUERY = 500;

    static int n;
    static Chatroom[] chatrooms;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

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
                System.out.println(query(st));
            }
        }
    }

    static class Chatroom {
        int pId;        // 부모 채팅방
        int authority;  // 전파 강도
        int[] remains;  // i 칸 만큼 더 이동 가능한 알림의 수
        boolean on;     // 알림 on / off
        Chatroom(int pId, int authority) {
            this.pId = pId;
            this.authority = authority;
            this.remains = new int[MAX_LEVEL + 1];
            this.on = true;
        }
        void updateAuthority(int authority) { this.authority = Math.min(MAX_LEVEL, authority); }
        void onOff() { this.on = !this.on; }
        int getAvailableReceive() {
            int result = 0;
            for(int i : remains) result += i;
            return result - 1;
        }
    }

    static void applySinglePropagation(int cId, int flag) {
        /**
         * 한 개의 채팅방에 해당하는 전파를 수정한다.
         * - set
         * - changeAuthority
         */
        int pId = chatrooms[cId].pId;
        int authority = chatrooms[cId].authority;
        chatrooms[cId].remains[authority] += flag;      // 현재 채팅방부터 반영한다.
        if(!chatrooms[cId].on) return;                  // 전파가 더 이상 불가능한 경우
        while(pId != -1 && authority-- > 0) {           // 부모 채팅방이 존재하고, 계속 전파 가능할때까지
            chatrooms[pId].remains[authority] += flag;
            if(!chatrooms[pId].on) return;
            pId = chatrooms[pId].pId;
        }
    }

    static void applyMultiPropagation(int cId, int flag) {
        /**
         * 여러 개의 채팅방의 연쇄 전파를 수정한다.
         * - onOff
         * - changeParent
         */
        int pId = chatrooms[cId].pId;
        int level = 1;                                  // 현재 cId 로부터 얼마나 위에 있는지
        while(pId != -1 && level < MAX_LEVEL + 1) {
            for(int i = level; i < MAX_LEVEL + 1; i++) {
                int alerts = chatrooms[cId].remains[i];         // cId 가 i 위치로 보낼 수 있는 알림의 양
                chatrooms[pId].remains[i - level] += flag * alerts;
            }
            if(!chatrooms[pId].on) return;
            pId = chatrooms[pId].pId;
            level++;
        }
    }

    static void set(StringTokenizer st) {
        chatrooms = new Chatroom[n + 1];
        chatrooms[0] = new Chatroom(-1, 0);     // root
        for(int i = 1; i < n + 1; i++) {
            int pId = Integer.parseInt(st.nextToken());
            chatrooms[i] = new Chatroom(pId, 0);
        }
        for(int i = 1; i < n + 1; i++) {
            int authority = Integer.parseInt(st.nextToken());
            chatrooms[i].updateAuthority(authority);
            applySinglePropagation(i, 1);        // 초기 상태는 ON (전파 가능)
        }
    }

    static void onOff(StringTokenizer st) {
        int cId = Integer.parseInt(st.nextToken());
        if(chatrooms[cId].on) applyMultiPropagation(cId, -1);
        else applyMultiPropagation(cId, 1);
        chatrooms[cId].onOff();
    }

    static void changeAuthority(StringTokenizer st) {
        int cId = Integer.parseInt(st.nextToken());
        int authority = Integer.parseInt(st.nextToken());
        // 1. 기존의 전파를 삭제
        applySinglePropagation(cId, -1);
        // 2. 새로운 전파 반영
        chatrooms[cId].updateAuthority(authority);
        applySinglePropagation(cId, 1);
    }

    static void changeParent(StringTokenizer st) {
        int cId1 = Integer.parseInt(st.nextToken());
        int cId2 = Integer.parseInt(st.nextToken());
        int pId1 = chatrooms[cId1].pId;
        int pId2 = chatrooms[cId2].pId;

        // 기존 전파 제거
        if(chatrooms[cId1].on) applyMultiPropagation(cId1, -1);
        if(chatrooms[cId2].on) applyMultiPropagation(cId2, -1);
        // 새로운 전파
        chatrooms[cId1].pId = pId2;
        chatrooms[cId2].pId = pId1;
        if(chatrooms[cId1].on) applyMultiPropagation(cId1, 1);
        if(chatrooms[cId2].on) applyMultiPropagation(cId2, 1);
    }

    static int query(StringTokenizer st) {
        int cId = Integer.parseInt(st.nextToken());
        return chatrooms[cId].getAvailableReceive();
    }
}
