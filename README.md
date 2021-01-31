# Canonical Huffman Encoder and Decoder

This application utilize Huffman encoding method to generate compressed text file

### How it works

1. Read text file
2. Create frequency table for eache individual character for ascii table if it's available 
3. Create Minheap based on generated data 
4. Make that generated tree to huffman tree in that way we assign 0 to left child of every node and 1 to right ones. Traversing that tree give us huffman codes
4.1.Make note that every leaf is character and every other nodes are Internal Node
5. Then we create list that we store sorted huffman tree in that way we first sort length-wise then if two of those nodes are the same length we sorted them lexicographically
6. Then we create Canonical Code from those information
7. and lastly we make codebook based on canonical info(length) and put them on header of file to transmit needed information for decoding

### How to Encode text file
example : 
```Java
        HuffmanEncoder he = new HuffmanEncoder(path);
        he.encode();
        he.saveBinaryFile("path/to/out");
```
which path determine location of text file 

### How to Decode file
example : 
```Java
        HuffmanDecoder huffmanDecoder = new HuffmanDecoder(path);
        huffmanDecoder.decode();
        huffmanDecoder.save("path/to/out");
```
which path determine location of encoded file 
