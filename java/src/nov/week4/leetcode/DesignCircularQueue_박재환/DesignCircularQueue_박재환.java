package nov.week4.leetcode.DesignCircularQueue_박재환;

public class DesignCircularQueue_박재환 {
    public static void main(String[] args) {
        MyCircularQueue myCircularQueue = new MyCircularQueue(3);
        System.out.println(myCircularQueue.enQueue(1)); // true
        System.out.println(myCircularQueue.enQueue(2)); // true
        System.out.println(myCircularQueue.enQueue(3)); // true
        System.out.println(myCircularQueue.enQueue(4)); // false
        System.out.println(myCircularQueue.Rear());     // 3
        System.out.println(myCircularQueue.isFull());   // true
        System.out.println(myCircularQueue.deQueue());  // true
        System.out.println(myCircularQueue.enQueue(4)); // true
        System.out.println(myCircularQueue.Rear());     // 4
    }
}

class MyCircularQueue {
    int[] vals;
    int head, tail;
    int size;

    public MyCircularQueue(int k) {
        vals = new int[k];
        head = 0;
        tail = 0;
        size = 0;
    }

    public boolean enQueue(int value) {
        if(isFull())
            return false;
        vals[tail] = value;
        tail = (tail + 1) % vals.length;
        size++;
        return true;
    }

    public boolean deQueue() {
        if(isEmpty())
            return false;
        head = (head + 1) % vals.length;
        size--;
        return true;
    }

    public int Front() {
        if(isEmpty())
            return -1;
        return vals[head];
    }

    public int Rear() {
        if(isEmpty())
            return -1;
        return vals[(tail - 1 + vals.length) % vals.length];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == vals.length;
    }
}
