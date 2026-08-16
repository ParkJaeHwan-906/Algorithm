package aug.week2.programmers.양과늑대_박재환;

public class 양과늑대_박재환 {
    public static void main(String[] args) {
        int[] info = {0,0,1,1,1,0,1,0,1,0,1,1};
        int[][] edges = {{0,1},{1,2},{1,4},{0,8},{8,7},{9,10},{9,11},{4,3},{6,5},{4,6},{8,9}};
        Solution sol = new Solution();
        System.out.println(sol.solution(info, edges));
    }
}

class Solution {
    static final int SHEEP = 0;
    static final int WOLF = 1;

    class Node {
        int type;
        int pId;
        int l, r;
        Node(int type, int pId, int l, int r) {
            this.type = type;
            this.pId = pId;
            this.l = l;
            this.r = r;
        }
        void addChild(int id) {
            if(this.l == -1) this.l = id;
            else this.r = id;
        }
        void setParent(int id) { this.pId = id; }
    }

    int n;
    Node[] nodes;
    public int solution(int[] info, int[][] edges) {
        set(info, edges);
        int[] history = new int[(1 << n)];
        searchMaxSheepRoute(0, 0, history, 0, 0);
        return getMaxValue(history);
    }

    void set(int[] info, int[][] edges) {
        this.n = info.length;
        this.nodes = new Node[n];
        for(int i = 0; i < n; i++) {
            this.nodes[i] = new Node(info[i], -1, -1, -1);
        }
        for(int[] arr : edges) {
            int parent = arr[0];
            int child = arr[1];
            this.nodes[parent].addChild(child);
            this.nodes[child].setParent(parent);
        }
    }

    void searchMaxSheepRoute(int id, int state, int[] history, int sheep, int wolf) {
        int cur = 1 << id;
        // 현재 위치가 양인가? 늑대인가?
        if(nodes[id].type == SHEEP) sheep++;
        else if(nodes[id].type == WOLF) wolf++;

        // 양 <= 늑대 조건 확인
        if(sheep <= wolf) {     // 모든 양이 잡아먹힌다
            sheep = 0;
        }

        state |= cur;

        if(sheep <= wolf) return;           // 더 이상 탐색할 수 없음
        if(history[state] != 0) return;     // 이전에 이미 동일한 방문 기록이 있는 경우

        history[state] = sheep;

        for(int i = 0; i < n; i++) {
            int next = 1 << i;
            if ((state & next) == 0) continue;       // 이미 방문했던 경우는 제외
            Node nNode = nodes[i];
            if (nNode.l != -1 && (state & (1 << nNode.l)) == 0)
                searchMaxSheepRoute(nNode.l, state, history, sheep, wolf);
            if (nNode.r != -1 && (state & (1 << nNode.r)) == 0)
                searchMaxSheepRoute(nNode.r, state, history, sheep, wolf);
        }
    }

    int getMaxValue(int[] history) {
        int max = 0;
        for(int i : history) max = Math.max(i, max);
        return max;
    }
}