package com.SGMCX;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("Enter the path of encoded file : ");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        HuffmanDecoder huffmanDecoder = new HuffmanDecoder(path);
        huffmanDecoder.decode();
        huffmanDecoder.save("src/com/SGMCX/output.txt");
    }
}
