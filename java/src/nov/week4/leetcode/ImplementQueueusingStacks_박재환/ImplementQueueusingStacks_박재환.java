package nov.week4.leetcode.ImplementQueueusingStacks_박재환;

public class ImplementQueueusingStacks_박재환 {
    public static void main(String[] args) {
        MyQueue myQueue = new MyQueue();
        myQueue.push(1);
        myQueue.push(2);

        System.out.println(myQueue.peek());  // 1
        System.out.println(myQueue.pop());   // 1
        System.out.println(myQueue.empty()); // false
    }
}

class MyQueue {
    int[] elements;
    int head, tail;

    public MyQueue() {
        this.elements = new int[10];
        this.head = 0;                  // poll 위치
        this.tail = 0;                  // offer 위치
    }

    public void push(int x) {
        resize();
        elements[tail] = x;
        tail = (tail + 1) % elements.length;
    }

    public int pop() {
        int x = elements[head];
        head = (head + 1) % elements.length;
        return x;
    }

    public int peek() {
        return elements[head];
    }

    public boolean empty() {
        return head == tail;
    }

    private void resize() {
        int len = (tail - head + elements.length) % elements.length;
        if(len <= elements.length) {
            return;
        }
        int[] newArr = new int[elements.length * 2];
        for(int i = 0; i < len; i++) {
            newArr[i] = elements[(head + i) % elements.length];
        }
        elements = newArr;
        head = 0;
        tail = len;
    }
}
