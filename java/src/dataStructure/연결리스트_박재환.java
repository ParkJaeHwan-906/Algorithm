package dataStructure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class 연결리스트_박재환 {

    static class SingleNode {
        int value;
        SingleNode next;
        SingleNode(int value, SingleNode next) {
            this.value = value;
            this.next = next;
        }
    }

    static class SingleLinkedList {
        SingleNode head;
        SingleNode tail;
        int size;

        SingleLinkedList() {
            this.head = null;
            this.tail = null;
            this.size = 0;
        }

        void append(int id, int value) {
            if (id == -1) id = size;
            if (id < 0 || id > size) {
                System.out.println("Index Out Of Bound");
                return;
            }
            SingleNode newNode = new SingleNode(value, null);
            if (size == 0) {
                head = tail = newNode;
            } else if (id == 0) {
                newNode.next = head;
                head = newNode;
            } else if (id == size) {
                tail.next = newNode;
                tail = newNode;
            } else {
                SingleNode prev = nodeAt(id - 1);
                newNode.next = prev.next;
                prev.next = newNode;
            }
            size++;
        }

        void delete(int id) {
            if (size == 0) {
                System.out.println("List is Empty");
                return;
            }
            if (id == -1) id = size - 1;
            if (id < 0 || id >= size) {
                System.out.println("Index Out Of Bound");
                return;
            }
            SingleNode removed;
            if (id == 0) {
                removed = head;
                head = head.next;
                if (size == 1)
                    tail = null;
            } else {
                SingleNode prev = nodeAt(id - 1);
                removed = prev.next;
                prev.next = removed.next;
                if (removed == tail) tail = prev;
            }
            removed.next = null;
            size--;
            System.out.println(removed.value);
        }

        void lookup(int id) {
            if (id < 0 || id >= size) {
                System.out.println("Index Out Of Bound");
                return;
            }
            System.out.println(nodeAt(id).value);
        }

        void printList() {
            SingleNode cur = head;
            System.out.print("[ ");
            while (cur != null) {
                System.out.print(cur.value + " ");
                cur = cur.next;
            }
            System.out.println("] (size=" + size + ")");
        }

        private SingleNode nodeAt(int id) {
            SingleNode cur = head;
            for (int i = 0; i < id; i++) {
                cur = cur.next;
            }
            return cur;
        }
    }

    static class DoubleNode {
        int value;
        DoubleNode prev;
        DoubleNode next;
        DoubleNode(int value, DoubleNode prev, DoubleNode next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    static class DoubleLinkedList {
        DoubleNode head;
        DoubleNode tail;
        int size;

        DoubleLinkedList() {
            this.head = null;
            this.tail = null;
            this.size = 0;
        }

        void append(int id, int value) {
            if (id == -1) id = size;
            if (id < 0 || id > size) {
                System.out.println("Index Out Of Bound");
                return;
            }
            DoubleNode newNode = new DoubleNode(value, null, null);
            if (size == 0) {
                head = tail = newNode;
            } else if (id == 0) {
                newNode.next = head;
                head.prev = newNode;
                head = newNode;
            } else if (id == size) {
                newNode.prev = tail;
                tail.next = newNode;
                tail = newNode;
            } else {
                DoubleNode cur = nodeAt(id);
                DoubleNode prev = cur.prev;
                prev.next = newNode;
                newNode.prev = prev;
                newNode.next = cur;
                cur.prev = newNode;
            }
            size++;
        }

        void delete(int id) {
            if (size == 0) {
                System.out.println("List is Empty");
                return;
            }
            if (id == -1) id = size - 1;
            if (id < 0 || id >= size) {
                System.out.println("Index Out Of Bound");
                return;
            }
            DoubleNode removed = nodeAt(id);
            DoubleNode prev = removed.prev;
            DoubleNode next = removed.next;
            if (prev == null) head = next;
            else prev.next = next;
            if (next == null) tail = prev;
            else next.prev = prev;
            removed.prev = null;
            removed.next = null;
            size--;
            System.out.println(removed.value);
        }

        void lookup(int id) {
            if(id < 0 || id >= size) {
                System.out.println("Index Out Of Bound");
                return;
            }
            System.out.println(nodeAt(id).value);
        }

        void printList() {
            DoubleNode cur = head;
            System.out.print("[ ");
            while (cur != null) {
                System.out.print(cur.value + " ");
                cur = cur.next;
            }
            System.out.println("] (size=" + size + ")");
        }

        DoubleNode nodeAt(int id) {
            DoubleNode cur = this.head;
            for(int i = 0; i < id; i++) {
                cur = cur.next;
            }
            return cur;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        SingleLinkedList singleList = new SingleLinkedList();
        DoubleLinkedList doubleList = new DoubleLinkedList();

        while (true) {
            System.out.println("\n리스트 선택: 1. 단일 연결 리스트  2. 이중 연결 리스트  0. 종료");
            int listType = Integer.parseInt(br.readLine().trim());
            if (listType == 0) break;

            System.out.println("명령 선택: 1. 삽입  2. 삭제  3. 조회");
            int command = Integer.parseInt(br.readLine().trim());
            if (command < 1 || command > 3) {
                System.out.println("지원하지 않는 명령입니다.");
                continue;
            }

            System.out.print(command == 1 ? "인덱스 (맨 뒤 삽입은 -1): " : "인덱스 (맨 뒤 삭제는 -1): ");
            int index = Integer.parseInt(br.readLine().trim());
            if (command == 1) {
                System.out.print("삽입할 값: ");
                int value = Integer.parseInt(br.readLine().trim());
                if (listType == 1) singleList.append(index, value);
                else if (listType == 2) doubleList.append(index, value);
                else {
                    System.out.println("지원하지 않는 리스트입니다.");
                    continue;
                }
            } else if (command == 2) {
                if (listType == 1) singleList.delete(index);
                else if (listType == 2) doubleList.delete(index);
                else {
                    System.out.println("지원하지 않는 리스트입니다.");
                    continue;
                }
            } else {
                if (listType == 1) singleList.lookup(index);
                else if (listType == 2) doubleList.lookup(index);
                else {
                    System.out.println("지원하지 않는 리스트입니다.");
                    continue;
                }
            }

            if (listType == 1) printSingleList(singleList);
            else if (listType == 2) doubleList.printList();
        }
    }

    private static void printSingleList(SingleLinkedList list) {
        SingleNode cur = list.head;
        System.out.print("[ ");
        while (cur != null) {
            System.out.print(cur.value + " ");
            cur = cur.next;
        }
        System.out.println("] (size=" + list.size + ")");
    }
}
