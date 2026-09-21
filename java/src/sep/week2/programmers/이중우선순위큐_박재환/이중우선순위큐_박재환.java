package sep.week2.programmers.이중우선순위큐_박재환;

import java.util.Arrays;
import java.util.PriorityQueue;

public class 이중우선순위큐_박재환 {
    public static void main(String[] args) {
        String[] operations = {"I 16", "I -5643", "D -1", "D 1", "D 1", "I 123", "D -1"};
        Solution sol = new Solution();
        System.out.print(Arrays.toString(sol.solution(operations)));
    }
}

class Solution {
    class Component {
        int num;
        boolean deleted;
        Component(int num) {
            this.num = num;
            this.deleted = false;
        }
    }
    public int[] solution(String[] operations) {
        PriorityQueue<Component> asc = new PriorityQueue<>((a, b) -> Integer.compare(a.num, b.num));
        PriorityQueue<Component> desc = new PriorityQueue<>((a, b) -> Integer.compare(b.num, a.num));
        for(String operation : operations) {
            if(operation.charAt(0) == 'I') {
                int num = Integer.parseInt(operation.substring(2));
                Component component = new Component(num);
                asc.offer(component);
                desc.offer(component);
            } else if(operation.charAt(0) == 'D') {
                int type = Integer.parseInt(operation.substring(2));
                if(type == 1) {
                    while(!desc.isEmpty() && desc.peek().deleted) {
                        desc.poll();
                    }
                    if(!desc.isEmpty()) {
                        Component component = desc.poll();
                        component.deleted = true;
                    }
                } else if(type == -1) {
                    while(!asc.isEmpty() && asc.peek().deleted) {
                        asc.poll();
                    }
                    if(!asc.isEmpty()) {
                        Component component = asc.poll();
                        component.deleted = true;
                    }
                }
            }
        }

        while(!desc.isEmpty() && desc.peek().deleted) {
            desc.poll();
        }
        while(!asc.isEmpty() && asc.peek().deleted) {
            asc.poll();
        }
        return (desc.isEmpty() && asc.isEmpty()) ? new int[] {0, 0} : new int[] {desc.peek().num, asc.peek().num};
    }
}