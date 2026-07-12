package jul.week1.programmers.이중우선순위큐_박재환;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * [풀이 시간]
 * 00:10:07
 * AI 사용 여부 X
 */
public class 이중우선순위큐_박재환 {
    public static void main(String[] args) {
        String[] operations = {"I 16", "I -5643", "D -1", "D 1", "D 1", "I 123", "D -1"};
        Solution solution = new Solution();
        int[] result = solution.solution(operations);
        System.out.println(Arrays.toString(result));
    }
}

class Solution {
    class Node {
        int num;
        boolean del;

        Node(int num) {
            this.num = num;
            this.del = false;
        }
    }

    public int[] solution(String[] operations) {
        PriorityQueue<Node> asc = new PriorityQueue<>((a, b) -> Integer.compare(a.num, b.num));
        PriorityQueue<Node> desc = new PriorityQueue<>((a, b) -> Integer.compare(b.num, a.num));

        for(String operation : operations){
            if(operation.charAt(0) == 'I') {
                int num = Integer.parseInt(operation.substring(2));
                Node node = new Node(num);
                asc.add(node);
                desc.add(node);
            } else if(operation.charAt(0) == 'D') {
                int type = Integer.parseInt(operation.substring(2));
                if(type == 1) {     // 최댓값 삭제
                    while(!desc.isEmpty() && desc.peek().del) desc.poll();
                    if(!desc.isEmpty()) {
                        Node node = desc.poll();
                        node.del = true;
                    }
                }
                else if(type == -1) {   // 최솟값 삭제
                    while(!asc.isEmpty() && asc.peek().del) asc.poll();
                    if(!asc.isEmpty()) {
                        Node node = asc.poll();
                        node.del = true;
                    }
                }
            }
        }

        int[] result = new int[2];
        while(!desc.isEmpty() && desc.peek().del) desc.poll();
        if(!desc.isEmpty()) result[0] = desc.poll().num;
        while(!asc.isEmpty() && asc.peek().del) asc.poll();
        if(!asc.isEmpty()) result[1] = asc.poll().num;
        return result;
    }
}