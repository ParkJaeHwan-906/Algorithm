package jun.week5.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 3시간+
  AI 사용 여부: O 세그먼트트리를 너무 간만에 써봐서 이것도 AI에 찾아보면서 어찌저찌 풀었는데.. 시간초과가 계속 났다.
                Query를 할 때마다 새로운 세그먼트트리를 만들어서 그랬던 것..재환이가 롤백의 힌트를 줘서 그걸로 AI와 어떻게든 풀었는데..어렵다😥
 */
public class 코드트리등산게임_박서희 {

    static ArrayList<Node> lisList = new ArrayList<>();
    static HashMap<Integer, Deque<Node>> history = new HashMap<>();
    static ArrayList<Integer> mountains = new ArrayList<>();
    static SegmentTree segmentTree = new SegmentTree();
    static StringBuilder sb = new StringBuilder();

    static final int SCORE = 1_000_000;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());

        segmentTree.init(1, 1, 1_000_000);

        while (Q-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int command = Integer.parseInt(st.nextToken());
            if (command == 100) {
                bigbang(st);
            } else if (command == 200) {
                moveMountain(st);
            } else if (command == 300) {
                earthquake();
            } else if (command == 400) {
                climbMountain(st);
            }
        }
        System.out.print(sb);
    }

    static void bigbang(StringTokenizer st) {
        int n = Integer.parseInt(st.nextToken());
        for (int i = 0; i < n; i++) {
            addMountain(Integer.parseInt(st.nextToken()));
        }
    }

    static void moveMountain(StringTokenizer st) {
        int h = Integer.parseInt(st.nextToken());
        addMountain(h);
    }

    private static void addMountain(int h) {
        mountains.add(h);

        Node prevBest = segmentTree.query(1, 1, 1_000_000, 1, h - 1);

        int myCount = prevBest.count + 1;
        int myFinalHeight = h;

        Node currentNode = new Node(myCount, myFinalHeight);
        segmentTree.update(1, 1, 1_000_000, h, currentNode);
        lisList.add(currentNode);
        history.computeIfAbsent(h, k -> new ArrayDeque<>()).push(currentNode);
    }

    static void earthquake() {
        if (mountains.isEmpty()) return;

        int lastH = mountains.remove(mountains.size() - 1);
        lisList.remove(lisList.size() - 1);
        history.get(lastH).pop();

        if (history.get(lastH).isEmpty()) {
            history.remove(lastH);
            segmentTree.update(1, 1, 1_000_000, lastH, new Node(0, 0));
        } else {
            segmentTree.update(1, 1, 1_000_000, lastH, history.get(lastH).peek());
        }
    }

    static void climbMountain(StringTokenizer st) {
        int mIdx = Integer.parseInt(st.nextToken()) - 1;

        Node cableStartNode = lisList.get(mIdx);
        Node totalBestNode = segmentTree.tree[1];

        long finalAnswer = 0;

        if (cableStartNode.count > 0) {
            long beforeCableScore = (long) (cableStartNode.count - 1) * SCORE;
            long afterCableScore = totalBestNode.count > 0 ? (long) (totalBestNode.count - 1) * SCORE : 0;
            finalAnswer = beforeCableScore + SCORE + afterCableScore + totalBestNode.height;
        }

        long noCableScore = totalBestNode.count > 0 ? (long) (totalBestNode.count - 1) * SCORE : 0;
        noCableScore += totalBestNode.height;

        sb.append(Math.max(finalAnswer, noCableScore)).append("\n");
    }

    static class SegmentTree {
        private Node[] tree = new Node[4 * 1_000_001];

        public void init(int node, int start, int end) {
            tree[node] = new Node(0, 0);
            if (start == end) {
                return;
            }
            int mid = (start + end) / 2;
            init(2 * node, start, mid);
            init(2 * node + 1, mid + 1, end);
        }

        private Node merge(Node leftChild, Node rightChild) {
            if (leftChild.count > rightChild.count) return leftChild;
            if (leftChild.count < rightChild.count) return rightChild;
            return leftChild.height > rightChild.height ? leftChild : rightChild;
        }

        public void update(int node, int start, int end, int idx, Node val) {
            if (start == end) {
                tree[node] = val;
                return;
            }

            int mid = (start + end) / 2;
            if (idx <= mid) {
                update(2 * node, start, mid, idx, val);
            } else {
                update(2 * node + 1, mid + 1, end, idx, val);
            }

            tree[node] = merge(tree[2 * node], tree[2 * node + 1]);
        }

        public Node query(int node, int start, int end, int left, int right) {
            if (start > right || end < left) {
                return new Node(0, 0);
            }

            if (left <= start && end <= right) {
                return tree[node];
            }

            int mid = (start + end) / 2;
            Node leftResult = query(2 * node, start, mid, left, right);
            Node rightResult = query(2 * node + 1, mid + 1, end, left, right);

            return merge(leftResult, rightResult);
        }
    }

    static class Node {
        int count;  // 등산 횟수
        int height; // 최종 산의 높이

        public Node(int count, int height) {
            this.count = count;
            this.height = height;
        }
    }
}
