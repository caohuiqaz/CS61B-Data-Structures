public class HuffmanDecoder {
    public static void main(String[] args) {
        ObjectReader or = new ObjectReader(args[0]);
        BinaryTrie trie = (BinaryTrie) or.readObject();
        int numberOfSymbols = (int) or.readObject();
        BitSequence sequences = (BitSequence) or.readObject();

        char[] outputs = new char[numberOfSymbols];
        for (int i = 0; i < numberOfSymbols; i++) {
            Match m = trie.longestPrefixMatch(sequences);
            outputs[i] = m.getSymbol();
            sequences = sequences.allButFirstNBits(m.getSequence().length());
        }
        FileUtils.writeCharArray(args[1], outputs);
    }
}
