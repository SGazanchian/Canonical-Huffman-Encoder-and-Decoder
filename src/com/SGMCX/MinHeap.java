package com.SGMCX;


import java.util.*;





public class MinHeap {

    private ArrayList<Node> arr;
    private int size;
    private HashMap<Character, Integer> charMap;

    public MinHeap(HashMap<Character, Integer> charMap) {
        this.size = charMap.entrySet().size();
        arr = new ArrayList<>(size);
        this.charMap = charMap;
        populateArray();
        createMinHeap();
    }



    public Node getMinimum() {


        Node min = arr.get(0);
        arr.set(0, arr.get(size - 1));
        size--;
        Minheapify(0);
        return min;
    }

    public int getHeapSize() {

        return size;
    }

    private void createMinHeap() {
        for (int i = (size - 1) / 2; i >= 0; i--) {
            Minheapify(i);
        }

    }

    public void print() {
        arr.forEach(System.out::println);
    }

    public void print(Node node) {
        if (node.left != null) {
            print(node.left);
        }
        if (node.right != null) {
            print(node.right);
        }
        if (node.left == null && node.right == null) {
            System.out.println(node);
        }
    }

    public Node getRoot() {
        return arr.get(0);
    }

    private void populateArray() {
        for (Map.Entry<Character, Integer> entry : charMap.entrySet()) {
            Node node = new Node();
            node.character = entry.getKey();
            node.freq = entry.getValue();
            arr.add(node);
        }
    }

    public int getParentPos(int pos) {

        if (pos > 0) {
            return (int) Math.floor((pos - 1) / 2);
        } else
            return 0;
    }

    public int getLeftChildPos(int pos) {

        if ((2 * pos) + 1 < size) {
            return (2 * pos) + 1;
        } else return -1;
    }

    public int getRightChildPos(int pos) {

        if ((2 * pos) + 2 < size) {
            return (2 * pos) + 2;
        } else return -1;
    }


    public void insertNode(Node n) {

        size++;
        int i = size - 1;
        while (i == 1 && n.freq < arr.get((i - 1) / 2).freq) {

            arr.set(i, arr.get((i - 1) / 2));
            i = (i - 1) / 2;
        }
        if (i < size)
            arr.set(i, n);
        else
            arr.add(n);
    }


    private void swapNode(int pos1, int pos2) {
        Node n = arr.get(pos1);
        arr.set(pos1, arr.get(pos2));
        arr.set(pos2, n);
    }

    private void Minheapify(int pos) {
        int smallestPos = pos;
        int leftChild = getLeftChildPos(pos);
        int rightChild = getRightChildPos(pos);
        if (leftChild != -1 && arr.get(leftChild).freq < arr.get(smallestPos).freq) {
            smallestPos = leftChild;
        }
        if (rightChild != -1 && arr.get(rightChild).freq < arr.get(smallestPos).freq) {
            smallestPos = rightChild;
        }
        if (smallestPos != pos) {
            swapNode(pos, smallestPos);
            Minheapify(smallestPos);
        }
    }
}
