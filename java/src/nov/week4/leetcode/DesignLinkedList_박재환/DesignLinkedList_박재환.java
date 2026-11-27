package nov.week4.leetcode.DesignLinkedList_박재환;

public class DesignLinkedList_박재환 {
    public static void main(String[] args) {
        MyLinkedList myLinkedList = new MyLinkedList();
        myLinkedList.addAtHead(1);
        myLinkedList.addAtTail(3);
        myLinkedList.addAtIndex(1, 2);
        System.out.println(myLinkedList.get(1)); // 2
        myLinkedList.deleteAtIndex(1);
        System.out.println(myLinkedList.get(1)); // 3
    }
}

class Node {
    int val;
    Node prev;
    Node next;
    public Node(int val, Node prev, Node next) {
        this.val = val;
        this.prev = prev;
        this.next = next;
    }
}

class MyLinkedList {
    int size;
    Node head;
    Node tail;

    public MyLinkedList() {
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    public int get(int index) {
        if(index < 0 || index >= size) {
            // throw new IllegalArgumentException("Index Out of Bound");
            return -1;
        }
        return nodeAt(index).val;
    }

    public void addAtHead(int val) {
        Node newNode = new Node(val, null, null);
        if(isEmpty()) {
            head = tail = newNode;
        } else {
            head.prev = newNode;
            newNode.next = head;
            head = newNode;
        }
        size++;
    }

    public void addAtTail(int val) {
        Node newNode = new Node(val, null, null);
        if(isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public void addAtIndex(int index, int val) {
        if(index < 0 || index > size) {
            // throw new IllegalArgumentException("Index Out of Bound");
            return;
        }
        if(index == size) {
            addAtTail(val);
        } else if(index ==0) {
            addAtHead(val);
        } else {
            Node newNode = new Node(val, null, null);
            Node cur = nodeAt(index);
            Node prev = cur.prev;

            prev.next = newNode;
            newNode.next = cur;
            cur.prev = newNode;
            newNode.prev = prev;
            size++;
        }
    }

    public void deleteAtIndex(int index) {
        if(isEmpty()) {
            // throw new IllegalArgumentException("Null Pointer Exception");
            return;
        }
        if(index < 0 || index >= size) {
            // throw new IllegalArgumentException("Index Out of Bound");
            return;
        }
        Node removed;
        if(index == 0) {
            removed = head;
            head = head.next;
            if(head == null)
                tail = null;
            else
                head.prev = null;
            removed.prev = null;
            removed.next = null;
        } else if(index == size - 1) {
            removed = tail;
            tail = tail.prev;
            if(tail == null)
                head = null;
            else
                tail.next = null;
            removed.prev = null;
            removed.next = null;
        } else {
            removed = nodeAt(index);
            Node prev = removed.prev;
            Node next = removed.next;
            if (prev != null)
                prev.next = next;
            if (next != null)
                next.prev = prev;
            removed.next = null;
            removed.prev = null;
        }
        size--;
    }

    private boolean isEmpty() {
        return head == null && tail == null;
    }

    private Node nodeAt(int index) {
        Node cur = head;
        for(int i = 0; i < index; i++)
            cur = cur.next;
        return cur;
    }
}
