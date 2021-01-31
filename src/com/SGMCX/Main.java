package com.SGMCX;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("Enter the path of text file to start compression : ");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        HuffmanEncoder he = new HuffmanEncoder(path);
        he.encode();
        he.saveBinaryFile("src/com/SGMCX/Output.che");
    }
}