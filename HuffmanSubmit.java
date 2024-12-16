/*
 * Niharika Agrawal
 * CSC 172
 * Project 2 - Huffman Coding
 */

import java.io.*;
import java.util.*;

import javax.management.ConstructorParameters;

public class HuffmanSubmit implements Huffman {

    public static void main(String[] args) {
        HuffmanSubmit huffman = new HuffmanSubmit();

        // Encoding and decoding for an image file
        huffman.encode("ur.jpg", "ur.enc", "freq.txt");
        huffman.decode("ur.enc", "ur_dec.jpg", "freq.txt");

        // Encoding and decoding for a text file
        huffman.encode("alice30.txt", "alice30.enc", "alice30_freq.txt");
        huffman.decode("alice30.enc", "alice30_decoded.txt", "alice30_freq.txt");
    }
    
    @Override
    public void encode(String inputFile, String outputFile, String freqFile) {
        try {
            // Create a frequency map to count the occurrence of each byte in the file
            URMap<Integer> freqMap = new URMap<>(256); 

            // Step 1: Read the input file and calculate byte frequencies
            calculateFrequencies(inputFile, freqMap);

            // Step 2: Build the Huffman Tree based on byte frequencies
            HuffmanNode root = buildHuffmanTree(freqMap);

            // Step 3: Generate Huffman codes for each byte
            URMap<String> huffmanCodeMap = generateHuffmanCodes(root);

            // Step 4: Write frequency data to an external file for decoding purposes
            writeFrequenciesToFile(freqFile, freqMap);

            // Step 5: Encode the input file and save the compressed output
            BinaryOut out = new BinaryOut(outputFile);
            encodeFile(inputFile, huffmanCodeMap, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace(); // Print error if IO issues occur
        }
    }

    @Override
    public void decode(String inputFile, String outputFile, String freqFile) {
        try {
            // Step 1: Read frequency data from the frequency file
            URMap<Integer> freqMap = readFrequenciesFromFile(freqFile);

            // Step 2: Rebuild the Huffman Tree from the frequency map
            HuffmanNode root = buildHuffmanTree(freqMap);

            // Step 3: Decode the compressed input file using the Huffman Tree
            BinaryIn in = new BinaryIn(inputFile);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile));
            decodeFile(in, out, root);
            out.close();
        } catch (IOException e) {
            e.printStackTrace(); // Handle any IO errors during decoding
        }
    }

    // --- Main Encoding and Decoding Operations ---

    // Calculate the frequency of each byte in the input file
    private void calculateFrequencies(String inputFile, URMap<Integer> freqMap) throws IOException {
        FileInputStream inputFileStream = new FileInputStream(inputFile);
        int c;
        while ((c = inputFileStream.read()) != -1) {
            byte key = (byte) c;
            // Update frequency in the map, default to 0 if key is absent
            freqMap.put(key, freqMap.getOrDefault(key, 0) + 1);
        }
        inputFileStream.close();
    }

    // Write frequency map to a file for later decoding
    private void writeFrequenciesToFile(String freqFile, URMap<Integer> freqMap) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(freqFile))) {
            for (byte key : freqMap.keys()) {
                out.writeByte(key);       // Write byte value
                out.writeInt(freqMap.get(key)); // Write corresponding frequency
            }
        }
    }

    // Read the frequency map from a file to rebuild the Huffman Tree
    private URMap<Integer> readFrequenciesFromFile(String freqFile) throws IOException {
        URMap<Integer> freqMap = new URMap<>(256);
        try (DataInputStream in = new DataInputStream(new FileInputStream(freqFile))) {
            while (in.available() > 0) {
                byte key = in.readByte(); // Read byte
                int freq = in.readInt();  // Read frequency
                freqMap.put(key, freq);
            }
        }
        return freqMap;
    }

    // Build the Huffman Tree using a priority queue
    private HuffmanNode buildHuffmanTree(URMap<Integer> freqMap) {
        URPriorityQueue pq = new URPriorityQueue(freqMap.size());
        for (byte key : freqMap.keys()) {
            pq.insert(new HuffmanNode(key, freqMap.get(key)));
        }
        while (pq.size() > 1) {
            HuffmanNode left = pq.removeMin(); // Remove the two smallest nodes
            HuffmanNode right = pq.removeMin();
            pq.insert(new HuffmanNode(left, right)); // Combine into a new node
        }
        return pq.removeMin(); // Root of the tree
    }

    // Generate Huffman codes from the tree
    private URMap<String> generateHuffmanCodes(HuffmanNode root) {
        URMap<String> huffmanCodeMap = new URMap<>(256);
        generateHuffmanCodesHelper(root, "", huffmanCodeMap);
        return huffmanCodeMap;
    }

    // Helper method to traverse the tree and generate codes
    private void generateHuffmanCodesHelper(HuffmanNode node, String code, URMap<String> map) {
        if (node.isLeaf()) {
            map.put(node.getByte(), code); // Assign code to leaf node
        } else {
            generateHuffmanCodesHelper(node.getLeft(), code + "0", map); // Traverse left
            generateHuffmanCodesHelper(node.getRight(), code + "1", map); // Traverse right
        }
    }

    // Encode the input file using Huffman codes
    private void encodeFile(String inputFile, URMap<String> huffmanCodeMap, BinaryOut out) throws IOException {
        FileInputStream inputFileStream = new FileInputStream(inputFile);
        int c;
        while ((c = inputFileStream.read()) != -1) {
            byte key = (byte) c;
            String code = huffmanCodeMap.get(key);
            for (char bit : code.toCharArray()) {
                out.write(bit == '1'); // Write bits as true/false
            }
        }
        inputFileStream.close();
    }

    // Decode the encoded file using the Huffman Tree
    private void decodeFile(BinaryIn in, BufferedOutputStream out, HuffmanNode root) throws IOException {
        HuffmanNode current = root;
        while (!in.isEmpty()) {
            boolean bit = in.readBoolean(); // Read next bit
            current = bit ? current.getRight() : current.getLeft(); // Traverse tree
            if (current.isLeaf()) {
                out.write(current.getByte()); // Write decoded byte
                current = root; // Reset to root
            }
        }
    }

    // --- Supporting Classes ---

    // Map implementation for frequency and Huffman code mappings
    private static class URMap<V> {
        private static class Node<V> {
            byte key;       // Byte key
            V value;        // Value associated with the key
            Node<V> next;   // Pointer to the next node in the chain

            Node(byte key, V value, Node<V> next) {
                this.key = key;
                this.value = value;
                this.next = next;
            }
        }

        private Node<V>[] table; // Hash table to store key-value pairs
        private int capacity;    // Size of the hash table

        @SuppressWarnings("unchecked")
        public URMap(int capacity) {
            this.capacity = capacity;
            this.table = (Node<V>[]) new Node[capacity];
        }

        private int hash(byte key) {
            return (key & 0xFF) % capacity; // Hash function
        }

        public void put(byte key, V value) {
            int index = hash(key);
            Node<V> head = table[index];
            while (head != null) {
                if (head.key == key) {
                    head.value = value;
                    return; // Update existing value
                }
                head = head.next;
            }
            table[index] = new Node<>(key, value, table[index]);
        }

        public V get(byte key) {
            int index = hash(key);
            Node<V> head = table[index];
            while (head != null) {
                if (head.key == key) {
                    return head.value; // Return value if key matches
                }
                head = head.next;
            }
            return null;
        }

        public V getOrDefault(byte key, V defaultValue) {
            V value = get(key);
            return value == null ? defaultValue : value;
        }

        public byte[] keys() {
            List<Byte> keys = new ArrayList<>();
            for (Node<V> node : table) {
                while (node != null) {
                    keys.add(node.key);
                    node = node.next;
                }
            }
            byte[] result = new byte[keys.size()];
            for (int i = 0; i < keys.size(); i++) {
                result[i] = keys.get(i);
            }
            return result;
        }

        public int size() {
            return keys().length;
        }
    }

    // Priority queue implementation for building the Huffman Tree
    private static class URPriorityQueue {
        private HuffmanNode[] heap; // Array representation of the heap
        private int size;           // Current number of elements in the heap

        public URPriorityQueue(int capacity) {
            this.heap = new HuffmanNode[capacity];
            this.size = 0;
        }

        public void insert(HuffmanNode node) {
            heap[size] = node;
            bubbleUp(size);
            size++;
        }

        public HuffmanNode removeMin() {
            if (size == 0) return null;
            HuffmanNode min = heap[0];
            heap[0] = heap[--size];
            bubbleDown(0);
            return min;
        }

        private void bubbleUp(int index) {
            while (index > 0) {
                int parent = (index - 1) / 2;
                if (heap[index].frequency >= heap[parent].frequency) break;
                swap(index, parent);
                index = parent;
            }
        }

        private void bubbleDown(int index) {
            while (index < size) {
                int left = 2 * index + 1;
                int right = 2 * index + 2;
                int smallest = index;

                if (left < size && heap[left].frequency < heap[smallest].frequency) {
                    smallest = left;
                }
                if (right < size && heap[right].frequency < heap[smallest].frequency) {
                    smallest = right;
                }
                if (smallest == index) break;
                swap(index, smallest);
                index = smallest;
            }
        }

        private void swap(int i, int j) {
            HuffmanNode temp = heap[i];
            heap[i] = heap[j];
            heap[j] = temp;
        }

        public int size() {
            return size;
        }
    }

    // Node representation for the Huffman Tree
    private static class HuffmanNode {
        private byte key;            // Byte value
        private int frequency;       // Frequency of the byte
        private HuffmanNode left;    // Left child
        private HuffmanNode right;   // Right child

        public HuffmanNode(byte key, int frequency) {
            this.key = key;
            this.frequency = frequency;
            this.left = null;
            this.right = null;
        }

        public HuffmanNode(HuffmanNode left, HuffmanNode right) {
            this.key = 0;
            this.frequency = left.frequency + right.frequency;
            this.left = left;
            this.right = right;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public byte getByte() {
            return key;
        }

        public HuffmanNode getLeft() {
            return left;
        }

        public HuffmanNode getRight() {
            return right;
        }
    }
}
