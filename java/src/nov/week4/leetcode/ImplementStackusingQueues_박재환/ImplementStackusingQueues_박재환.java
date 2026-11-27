package nov.week4.leetcode.ImplementStackusingQueues_박재환;

public class ImplementStackusingQueues_박재환 {
    public static void main(String[] args) {
        MyStack myStack = new MyStack();
        myStack.push(1);
        myStack.push(2);

        System.out.println(myStack.top());   // 2
        System.out.println(myStack.pop());   // 2
        System.out.println(myStack.empty()); // false
    }
}

class MyStack {
    int[] elements;
    int tail;

    public MyStack() {
        this.elements = new int[10];
        this.tail = 0;
    }

    public void push(int x) {
        resize();
        elements[tail++] = x;
    }

    public int pop() {
        int x = elements[--tail];
        elements[tail] = 0;
        return x;
    }

    public int top() {
        return elements[tail - 1];
    }

    public boolean empty() {
        return tail == 0;
    }

    private void resize() {
        if(tail >= elements.length) {
            int[] newArr = new int[elements.length * 2];
            for(int i = 0; i < tail; i++)
                newArr[i] = elements[i];
            elements = newArr;
        }
    }
}
