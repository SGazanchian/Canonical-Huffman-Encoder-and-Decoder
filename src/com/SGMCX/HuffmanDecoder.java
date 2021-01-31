package com.SGMCX;

import java.io.*;
import java.util.*;


public class HuffmanDecoder {
    private class CanonicalData {
        char c;
        byte length;

        public CanonicalData(char c, byte length) {
            this.c = c;
            this.length = length;
        }


        @Override
        public String toString() {
            return c + " : " + length;
        }
    }
    private final char BEGIN = 0;
    private final char END = 127;
    private static final char MERGE_CHARACTER = 129;
    private String path;
    private FileInputStream inputFile;
    private HashMap<String, Character> canonicalMap = new HashMap<>();
    private ArrayList<CanonicalData> lengthList = new ArrayList<>();
    private String totalText;

    public HuffmanDecoder(String path) throws FileNotFoundException {
        this.path = path;
        inputFile = new FileInputStream(path);
    }

    public void decode() throws IOException {
        readCodeBook();
        sortList();
        createCanonicalData();
        decodeText();
    }

    private void decodeText() throws IOException {
        int i;
        StringBuilder sb = new StringBuilder();
        String LastBinaryBlock = "";
        int lastVal = 0;
        while ((i = inputFile.read()) != -1){
            LastBinaryBlock = String.format("%" + (8) + "s", Integer.toBinaryString(i)).replaceAll(" ", "0");
            sb.append(LastBinaryBlock);
            lastVal = i;
        }
        System.out.println(lastVal);
        sb.delete(sb.length() - lastVal - 8 , sb.length());
        System.out.println("Canonical Data : ");
        System.out.println(sb);
        int front_pos = 0;
        StringBuilder decodedText = new StringBuilder();
        for (int j = 1; j < sb.length() + 1; j++) {
            String subString = sb.substring(front_pos,j);
            if(canonicalMap.containsKey(subString)){
                decodedText.append(canonicalMap.get(subString));
                front_pos = j;
            }
        }
        System.out.println("Decoded Text : ");
        System.out.println(decodedText);
        totalText = decodedText.toString();
    }

    public void save(String path) throws IOException {
        OutputStreamWriter outputStreamWriter= new OutputStreamWriter(new FileOutputStream(path));
        outputStreamWriter.append(totalText);
        outputStreamWriter.flush();
        outputStreamWriter.close();

    }
    private void createCanonicalData() {
        if(lengthList.size() == 0){
            return;
        }
        int c_code = 0, last_len, curr_len;
        last_len = lengthList.get(0).length;
        String format = String.format("%" + (last_len) + "s", Integer.toBinaryString(c_code));
        System.out.println(lengthList.get(0).c + " : " + format.replaceAll(" ", "0"));
        canonicalMap.put(format.replaceAll(" ", "0"),lengthList.get(0).c);
        for (int i = 1; i < lengthList.size(); i++) {
            curr_len = lengthList.get(i).length;
            c_code = (c_code + 1) << curr_len - last_len;
            String binaryRep = String.format("%" + (curr_len) + "s", Integer.toBinaryString(c_code));
            System.out.println(lengthList.get(i).c + " : " + binaryRep.replaceAll(" ", "0"));
            canonicalMap.put( binaryRep.replaceAll(" ", "0"), lengthList.get(i).c);
            last_len = curr_len;
        }
    }

    private void sortList(){
        lengthList.sort((o1, o2) -> {
            if(o1.length < o2.length){
                return -1;
            }else if (o1.length == o2.length && o1.c - o2.c < 0) {
                return -1;
            }else if(o1.length == o2.length && o1.c - o2.c > 0) {
                return 1;
            }
            else {
                return 1;
            }

        });
    }
    private void readCodeBook() throws IOException {
        for (int i = 0; i < END - BEGIN + 1; i++) {
            byte in = (byte)inputFile.read();
            if(Byte.toUnsignedInt(in) == 0xff){
                byte val = (byte)inputFile.read();
                i += val - 1;
                continue;
            }
            if(in == 0){
                continue;
            }else {
                CanonicalData e = new CanonicalData((char) i, in);
                lengthList.add(e);
            }
        }
    }
}
