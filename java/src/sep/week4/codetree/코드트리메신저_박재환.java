package sep.week4.codetree;

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
    static final int CHANGE_POWER = 300;
    static final int CHANGE_PARENT = 400;
    static final int QUERY = 500;

    static class ChatRoom {
        int pId;                    // 부모 채팅방
        int authority;              // 권한세기
        int[] receivedAlert;        // 전파 가능한 알림 ( [i] : i 만큼 더 전파가 가능하다 )
        boolean alert;              // 알림 켜짐 여부
        ChatRoom(int pId) {
            this.pId = pId;
            this.authority = 0;
            this.alert = true;
            this.receivedAlert = new int[21];                   // 트리의 최대 깊이가 20
        }
        void setAuthority(int authority) {
            this.authority = Math.min(authority, 20);
        }
        void onOff() {
            this.alert = !this.alert;
        }
    }

    static int n, q;
    static ChatRoom[] rooms;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            if(type == SET) {
                set(st);
            }
            else if(type == ON_OFF) {
                onOff(st);
            }
            else if(type == CHANGE_POWER) {
                changeAuthority(st);
            }
            else if(type == CHANGE_PARENT) {
                changeParent(st);
            }
            else if(type == QUERY) {
                sb.append(query(st)).append('\n');
            }
        }
        System.out.print(sb);
    }

    static void set(StringTokenizer st) {
        /**
         * 0 ~ N 까지 N + 1 개의 채팅방이 있다.
         */
        rooms = new ChatRoom[n + 1];
        rooms[0] = new ChatRoom(-1);        // Root 채팅방
        for(int i = 1; i <= n; i++) {
            rooms[i] = new ChatRoom(Integer.parseInt(st.nextToken()));
        }
        for(int i = 1; i <= n; i++) {
            rooms[i].setAuthority(Integer.parseInt(st.nextToken()));
        }

        for (int i = 1; i <= n; i++) {
            ChatRoom cur = rooms[i];
            int remain = cur.authority;
            while(true) {
                cur.receivedAlert[remain]++;
                if(remain == 0 || cur.pId == -1) {
                    break;
                }
                cur = rooms[cur.pId];
                remain--;
            }
        }
    }

    static void onOff(StringTokenizer st) {
        int c = Integer.parseInt(st.nextToken());
        ChatRoom target = rooms[c];
        target.onOff();
        int value = (target.alert ? 1 : -1);
        ChatRoom parent = rooms[target.pId];
        int depth = 1;
        while(true) {
            for(int i = depth; i < 21; i++) {
                parent.receivedAlert[i - depth] += value * target.receivedAlert[i];
            }
            if(parent.pId == -1 || !parent.alert) {         // 더 이상 전파할 부모가 없거나, 전파가 불가능한 경우
                break;
            }
            parent = rooms[parent.pId];
            depth++;
        }
    }

    static void changeAuthority(StringTokenizer st) {
        int c = Integer.parseInt(st.nextToken());
        int power = Integer.parseInt(st.nextToken());
        ChatRoom target = rooms[c];
        // 기존 권한의 자기 알림 제거
        ChatRoom cur = target;
        int remain = target.authority;
        while(true) {
            cur.receivedAlert[remain]--;
            if(remain == 0 || cur.pId == -1 || !cur.alert) {
                break;
            }
            cur = rooms[cur.pId];
            remain--;
        }
        // 권한 변경
        target.setAuthority(power);
        cur = target;
        remain = target.authority;
        while(true) {
            cur.receivedAlert[remain]++;
            if(remain == 0 || cur.pId == -1 || !cur.alert) {
                break;
            }
            cur = rooms[cur.pId];
            remain--;
        }
    }

    static void changeParent(StringTokenizer st) {
        int c1 = Integer.parseInt(st.nextToken());
        int c2 = Integer.parseInt(st.nextToken());
        ChatRoom target1 = rooms[c1];
        ChatRoom target2 = rooms[c2];
        if(target1.alert) {
            ChatRoom parent = rooms[target1.pId];
            int depth = 1;
            while (true) {
                for (int i = depth; i < 21; i++) {
                    parent.receivedAlert[i - depth] -= target1.receivedAlert[i];
                }
                if (parent.pId == -1 || !parent.alert) {         // 더 이상 전파할 부모가 없거나, 전파가 불가능한 경우
                    break;
                }
                parent = rooms[parent.pId];
                depth++;
            }
        }
        if(target2.alert) {
            ChatRoom parent = rooms[target2.pId];
            int depth = 1;
            while (true) {
                for (int i = depth; i < 21; i++) {
                    parent.receivedAlert[i - depth] -= target2.receivedAlert[i];
                }
                if (parent.pId == -1 || !parent.alert) {         // 더 이상 전파할 부모가 없거나, 전파가 불가능한 경우
                    break;
                }
                parent = rooms[parent.pId];
                depth++;
            }
        }
        int originTarget1PId = target1.pId;
        int originTarget2PId = target2.pId;
        target1.pId = originTarget2PId;
        target2.pId = originTarget1PId;
        if(target1.alert) {
            ChatRoom parent = rooms[target1.pId];
            int depth = 1;
            while (true) {
                for (int i = depth; i < 21; i++) {
                    parent.receivedAlert[i - depth] += target1.receivedAlert[i];
                }
                if (parent.pId == -1 || !parent.alert) {         // 더 이상 전파할 부모가 없거나, 전파가 불가능한 경우
                    break;
                }
                parent = rooms[parent.pId];
                depth++;
            }
        }
        if(target2.alert) {
            ChatRoom parent = rooms[target2.pId];
            int depth = 1;
            while (true) {
                for (int i = depth; i < 21; i++) {
                    parent.receivedAlert[i - depth] += target2.receivedAlert[i];
                }
                if (parent.pId == -1 || !parent.alert) {         // 더 이상 전파할 부모가 없거나, 전파가 불가능한 경우
                    break;
                }
                parent = rooms[parent.pId];
                depth++;
            }
        }
    }

    static int query(StringTokenizer st) {
        int c = Integer.parseInt(st.nextToken());
        ChatRoom target = rooms[c];
        int alertCount = 0;
        for(int i : target.receivedAlert) {
            alertCount += i;
        }
        return alertCount - 1;
    }
}
