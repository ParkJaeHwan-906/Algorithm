package dataStructure;

import java.io.*;

public class 원형큐_박재환 {

    static final int ENQUEUE = 1;
    static final int DEQUEUE = 2;
    static final int PEEK = 3;

    static class CircularQueue {
        int head;
        int tail;
        int[] elements;
        CircularQueue() {
            this.head = 0;
            this.tail = 0;
            this.elements = new int[10];
        }

        void enqueue(int value) {
            if((this.tail + 1) % this.elements.length == this.head) {       // 큐가 가득 차 있음
                grow();
            }

            this.elements[this.tail] = value;
            this.tail = (this.tail + 1) % this.elements.length;
        }

        void dequeue() {
            if(this.head == this.tail) {
                System.out.println("Queue is empty");
                return;
            }
            int value = this.elements[this.head];
            this.head = (this.head + 1) % this.elements.length;
            System.out.println(value);
        }

        void peek() {
            if(this.head == this.tail) {
                System.out.println("Queue is empty");
                return;
            }
            int value = this.elements[this.head];
            System.out.println(value);
        }

        private void grow() {
            int oldLen = this.elements.length;
            int size = (this.tail - this.head + oldLen) % oldLen;
            int[] newArray = new int[oldLen * 2];
            for(int i = 0; i < size; i++) {
                newArray[i] = this.elements[(this.head + i) % oldLen];
            }
            this.elements = newArray;
            this.head = 0;
            this.tail = size;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        CircularQueue queue = new CircularQueue();
        System.out.println("CircularQueue");
        while (true) {
            System.out.print("[TYPE] 1: ENQUEUE, 2: DEQUEUE, 3: PEEK (빈 줄: 종료) : ");
            String line = br.readLine();
            if (line == null || line.isBlank()) {
                break;
            }

            try {
                int type = Integer.parseInt(line.trim());
                if (type == ENQUEUE) {
                    System.out.print("[ENQUEUE] : ");
                    String valueLine = br.readLine();
                    if (valueLine == null) {
                        break;
                    }
                    queue.enqueue(Integer.parseInt(valueLine.trim()));
                } else if (type == DEQUEUE) {
                    queue.dequeue();
                } else if (type == PEEK) {
                    queue.peek();
                } else {
                    System.out.println("Invalid type");
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요.");
            }
        }
        br.close();
    }


}
