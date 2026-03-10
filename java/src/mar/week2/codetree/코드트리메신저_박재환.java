package mar.week2.codetree;

import java.util.*;
import java.io.*;

public class 코드트리메신저_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static final int READY = 100;
    static final int SET_ALERT = 200;
    static final int SET_AUTH = 300;
    static final int CHANGE_PARENT = 400;
    static final int QUERY = 500;
    /**
     * 채팅방은 이진트리 형태로 관리된다. -> 높이 : O(log n)
     * => 하지만 Worst Case 의 경우 (편향트리) : O(n)
     *
     * 문제에서 주어진 조건 : 이진트리의 최대 깊이는 20
     */
    static StringTokenizer st;
    static int n, q;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());

        for(int i=0; i<q; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());
            if(cmd == READY) { ready(); }
            else if(cmd == SET_ALERT) { setAlert(); }
            else if(cmd == SET_AUTH) { setAuth(); }
            else if(cmd == CHANGE_PARENT) { setPId(); }
            else if(cmd == QUERY) { sb.append(query()).append('\n'); }
        }
    }
    static class ChatRoom {
        int id;
        int pId;
        int l, r;       // 자식
        int auth;
        boolean on;

        int[] alertUp; // 자기 전파 범위, 현재 노드로 부터 d 칸 만큼 올라갈 수 있는 알림수

        ChatRoom(int id, int pId) {
            this.id = id;
            this.pId = pId;

            this.on = true;
            this.l = 0;
            this.r = 0;
            this.alertUp = new int[21];       // 트리의 높이가 최대 20
        }
        void setAuth(int auth) {
            if (auth < 0) auth = 0;
            if (auth > 20) auth = 20;
            this.auth = auth;

        }
        void onOff() {
            this.on = (!this.on);
        }
        void setPId(int pId) {
            this.pId = pId;
        }
    }
    static ChatRoom[] chatRooms;
    static void ready() {
        chatRooms = new ChatRoom[n+1];
        for(int i=1; i<n+1; i++) {
            int pId = Integer.parseInt(st.nextToken());
            ChatRoom chatRoom = new ChatRoom(i, pId);
            chatRooms[i] = chatRoom;
        }
        for(int i=1; i<n+1; i++) addChild(chatRooms[i].pId, i);
        for(int i=1; i<n+1; i++) {
            int auth = Integer.parseInt(st.nextToken());
            chatRooms[i].setAuth(auth);
        }

        buildChatRoom();
    }
    static void buildChatRoom() {
        for(int i=1; i<n+1; i++) {
            if(chatRooms[i].pId == 0) build(i);
        }
    }
    static void build(int cur) {
        int l = chatRooms[cur].l;
        int r = chatRooms[cur].r;
        if(l != 0) build(l);
        if(r != 0) build(r);
        recomputeChatRoom(cur);
    }
    static void setAlert() {
        int id = Integer.parseInt(st.nextToken());
        chatRooms[id].onOff();
        recomputeToRoot(chatRooms[id].pId);
    }
    static void setAuth() {
        int id = Integer.parseInt(st.nextToken());
        int auth = Integer.parseInt(st.nextToken());
        chatRooms[id].setAuth(auth);
        recomputeToRoot(id);
    }
    static void setPId() {
        int id1 = Integer.parseInt(st.nextToken());
        int pId1 = chatRooms[id1].pId;
        int id2 = Integer.parseInt(st.nextToken());
        int pId2 = chatRooms[id2].pId;

        change(pId1, id1, id2);
        change(pId2, id2, id1);

        chatRooms[id1].setPId(pId2);
        chatRooms[id2].setPId(pId1);

        recomputeToRoot(pId1);
        recomputeToRoot(pId2);
    }
    static void change(int pId, int prev, int next) {
        if(pId == 0) return;
        ChatRoom chatRoom = chatRooms[pId];
        if(chatRoom.l == prev) chatRoom.l = next;
        else chatRoom.r = next;
    }
    static int query() {
        int id = Integer.parseInt(st.nextToken());
        int alert = 0;
        for(int i=0; i<21; i++) alert += chatRooms[id].alertUp[i];
        return alert-1;
    }
    static void recomputeToRoot(int cur) {
        while (cur != 0) {
            recomputeChatRoom(cur);
            cur = chatRooms[cur].pId;
        }
    }
    static void recomputeChatRoom(int cur) {
        Arrays.fill(chatRooms[cur].alertUp, 0);

        int range = chatRooms[cur].auth;
        chatRooms[cur].alertUp[range] = 1;

        int l = chatRooms[cur].l;
        int r = chatRooms[cur].r;
        if (l != 0 && chatRooms[l].on) {
            for (int d = 1; d <= 20; d++) chatRooms[cur].alertUp[d - 1] += chatRooms[l].alertUp[d];
        }
        if (r != 0 && chatRooms[r].on) {
            for (int d = 1; d <= 20; d++) chatRooms[cur].alertUp[d - 1] += chatRooms[r].alertUp[d];
        }
    }
    static void addChild(int pId, int id) {
        if(pId == 0) return;
        ChatRoom chatRoom = chatRooms[pId];
        if(chatRoom.l == 0) chatRoom.l = id;
        else chatRoom.r = id;
    }
}
