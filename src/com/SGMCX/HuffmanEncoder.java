package com.SGMCX;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


class Data {
    char character;
    String rep;
}

public class HuffmanEncoder {



    private final static byte ClusterBytes = (byte)0xff;
    private final char BEGIN = 0;
    private final char END = 127;
    private byte length[] = new byte[END - BEGIN + 1];
    private ArrayList<Data> LookUpTable = new ArrayList<>();
    private static final char MERGE_CHARACTER = 129;
    //Fields defined
    private String path;
    private FileInputStream inputText;
    private HashMap<Character, Integer> charMap = new HashMap<>();
    private HashMap<Character, String> canonicalMap = new HashMap<>();
    private MinHeap minHeap;
    private String totalText;

    public HuffmanEncoder(String path) throws FileNotFoundException {
        this.path = path;
        inputText = new FileInputStream(path);
    }

    public void encode() throws IOException {
        if (inputText == null) {
            System.out.println("No input provided -> exit code 1");
            return;
        }
        calculateFrequncey();
        minHeap = new MinHeap(charMap);
        System.out.println("Min heap created");
        createHuffmanTree();

        System.out.println("Huffman tree created");
//        minHeap.print(minHeap.getRoot());
        System.out.println("Printing huffman tree");
        StringBuilder sb = new StringBuilder();
        printCode(minHeap.getRoot(), sb);
        sortData();
        generateCanonicalCode();
        populateBitLengthArray();
    }


    public void saveBinaryFile(String path) throws IOException {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path));
        byte counter = 0;
        //writing codebook
        for (int i = 0; i < END - BEGIN + 1; i++) {
            if(length[i] == 0) {
                counter++;
            }
            else if(counter >= 2){
                bufferedOutputStream.write(ClusterBytes);//tag for clustering
                bufferedOutputStream.write(counter);//number of elements in cluster
                counter = 0;
                bufferedOutputStream.write(length[i]);
            } else {
                byte[] arrbyte = new byte[counter];
                for (int j = 0; j < counter; j++) {
                    arrbyte[j] = (byte)0;
                }
                bufferedOutputStream.write(arrbyte);
                bufferedOutputStream.write(length[i]);
                counter = 0;
            }

        }

        if(counter >= 2){
            bufferedOutputStream.write(ClusterBytes);//tag for clustering
            bufferedOutputStream.write(counter);//number of elements in cluster
            counter = 0;
        }else {
            byte[] arrbyte = new byte[counter];
            for (int j = 0; j < counter; j++) {
                arrbyte[j] = (byte)0;
            }
            bufferedOutputStream.write(arrbyte);
        }
        ArrayList<Byte> canonicalData = new ArrayList<>();
        StringBuilder sb =  new StringBuilder();

        for (int i = 0; i < totalText.length(); i++) {
            sb.append(canonicalMap.get(totalText.charAt(i)));
            if(sb.length() >= 8){
                canonicalData.add((byte)Integer.parseInt(sb.substring(0,8) , 2));
                sb.delete(0,8);
            }
        }
        byte skipBitCounter = 0;
        if(sb.length() > 0){

            while(sb.length() != 8){
                skipBitCounter++;
                sb.append('0');
            }
            canonicalData.add((byte)Integer.parseInt(sb.toString(),2));
        }
        byte [] output = new byte[canonicalData.size()];
        for (int i = 0; i < canonicalData.size(); i++) {
            output[i] = canonicalData.get(i);
        }

        bufferedOutputStream.write(output);
        bufferedOutputStream.write(skipBitCounter);
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }
    private void populateBitLengthArray() {
        for (int i = 0; i < END - BEGIN + 1; i++) {
            if (canonicalMap.containsKey((char) i)) {
                byte s = (byte)(canonicalMap.get((char) i).length());
                length[i] = s;
            } else
                length[i] = 0;
        }
    }

    private void generateCanonicalCode() {
        System.out.println("Printing canonical form");
        int c_code = 0, last_len, curr_len;
        last_len = LookUpTable.get(0).rep.length();
        String format = String.format("%" + (last_len) + "s", Integer.toBinaryString(c_code));
        System.out.println(LookUpTable.get(0).character + " : " + format.replaceAll(" ", "0"));
        canonicalMap.put(LookUpTable.get(0).character, format.replaceAll(" ", "0"));
        for (int i = 1; i < LookUpTable.size(); i++) {
            curr_len = LookUpTable.get(i).rep.length();
            c_code = (c_code + 1) << curr_len - last_len;
            String binaryRep = String.format("%" + (curr_len) + "s", Integer.toBinaryString(c_code));
            System.out.println(LookUpTable.get(i).character + " : " + binaryRep.replaceAll(" ", "0"));
            canonicalMap.put(LookUpTable.get(i).character, binaryRep.replaceAll(" ", "0"));
            last_len = curr_len;
        }
    }

    private void sortData() {

        LookUpTable.sort((o1, o2) -> {
            if(o1.rep.length() < o2.rep.length()){
                return -1;
            }else if (o1.rep.length() == o2.rep.length() && o1.character - o2.character < 0) {
                return -1;
            }else if(o1.rep.length() == o2.rep.length() && o1.character - o2.character > 0) {
                return 1;
            }
            else {
                return 1;
            }

        });
    }

    private void printCode(Node root, StringBuilder stringBuilder) {
        if (root.left != null) {
            stringBuilder.append("0");
            printCode(root.left, stringBuilder);
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        if (root.right != null) {
            stringBuilder.append("1");
            printCode(root.right, stringBuilder);
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        if (root.left == null && root.right == null) {
            System.out.println(root.character + " : " + stringBuilder.toString());
            Data data1 = new Data();
            data1.character = root.character;
            data1.rep = stringBuilder.toString();
            LookUpTable.add(data1);
        }
    }

    private void createHuffmanTree() {

        Node leftChild, rightChild;
        while (minHeap.getHeapSize() != 1) {

            leftChild = minHeap.getMinimum();
            rightChild = minHeap.getMinimum();
            Node mergeNode = new Node();
            mergeNode.character = MERGE_CHARACTER;
            mergeNode.right = rightChild;
            mergeNode.left = leftChild;
            mergeNode.freq = mergeNode.left.freq + mergeNode.right.freq;
            minHeap.insertNode(mergeNode);
        }


    }

    private void calculateFrequncey() throws IOException {
        String text = IOUtils.toString(inputText, StandardCharsets.UTF_8);
        this.totalText = text;
        for (int i = 0; i < text.length(); i++) {
            if (charMap.containsKey(text.charAt(i))) {
                charMap.put(text.charAt(i), charMap.get(text.charAt(i)) + 1);
            } else {
                charMap.put(text.charAt(i), 1);
            }
        }

    }
}
