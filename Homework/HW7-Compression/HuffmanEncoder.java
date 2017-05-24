import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> frequencyTable = new HashMap<>();
        for (char c : inputSymbols) {
            if (!frequencyTable.containsKey(c)) {
                frequencyTable.put(c, 0);
            } else {
                frequencyTable.put(c, frequencyTable.get(c) + 1);
            }
        }
        return frequencyTable;
    }

    public static void main(String[] args) {
        char[] inputs = FileUtils.readFile(args[0]);
        Map<Character, Integer> frequencyTable = buildFrequencyTable(inputs);
        BinaryTrie trie = new BinaryTrie(frequencyTable);
        ObjectWriter ow = new ObjectWriter(args[0] + ".huf");
        ow.writeObject(trie);
        ow.writeObject(inputs.length);
        Map<Character, BitSequence> lookupTable = trie.buildLookupTable();
        List<BitSequence> bits = new ArrayList<>();

        for (char c : inputs) {
            BitSequence b = lookupTable.get(c);
            bits.add(b);
        }
        ow.writeObject(BitSequence.assemble(bits));
    }
}
