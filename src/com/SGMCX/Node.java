package com.SGMCX;

public class Node {
    public char character;
    public int freq;
    public Node left, right;

    @Override
    public String toString() {
        return character + " " + freq;
    }

}
