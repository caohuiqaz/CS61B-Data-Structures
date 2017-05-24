
import java.util.BitSet;
import java.util.List;
import java.io.Serializable;

public class BitSequence implements Serializable {
    private static final long serialVersionUID = 157816335349L;

    private BitSet bits;
    private int numBits;
    private int firstBitNum;
    private int lastBitNum;
    public BitSequence() {
        bits = new BitSet();
        numBits = 0;
        firstBitNum = 0;
        lastBitNum = 0;
    }

    public BitSequence(BitSequence b) {
        bits = new BitSet();
        for (int i = 0; i < b.length(); i += 1) {
            append(b.bitAt(i));
        }
    }

    public BitSequence(String s) {
        BitSequence bs = new BitSequence();

        for (char b : s.toCharArray()) {
            if (b == '0') {
                bs.append(0);
            } else if (b == '1') {
                bs.append(1);
            } else {
                throw new IllegalArgumentException("Bit Sequence creation string contains: " + b);
            }
        }

        this.bits = bs.bits;
        this.numBits = bs.numBits;
        this.firstBitNum = 0;
        this.lastBitNum = bs.numBits;
    }

    public int length() {
        return lastBitNum - firstBitNum;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other == null) {
            return false;
        }

        if (other.getClass() != this.getClass()) {
            return false;
        }

        BitSequence that = (BitSequence) other;

        if (this.length() != that.length()) {
            return false;
        }

        for (int i = 0; i < length(); i += 1) {
            if (this.bitAt(i) != that.bitAt(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        for (int i = 0; i < length(); i += 1) {
            hashCode *= 31;
        }
        return hashCode;
    }

    public int bitAt(int position) {
        boolean bit = bits.get(position + firstBitNum);
        if (!bit) {
            return 0;
        } else {
            return 1;
        }
    }

    public BitSequence appended(int bit) {
        BitSequence bscopy = new BitSequence(this);
        bscopy.append(bit);
        return bscopy;
    }
    public BitSequence firstNBits(int N) {
        if (N > numBits) {
            throw new IllegalArgumentException("More bits requested than present in sequence, "
                    + "requested " + N + ", but have " + numBits + ".");
        }

        BitSequence firstN = new BitSequence();
        firstN.bits = this.bits;
        firstN.numBits = N;
        firstN.firstBitNum = this.firstBitNum;
        firstN.lastBitNum = this.firstBitNum + N;
        return firstN;
    }

    public BitSequence allButFirstNBits(int N) {
        if (N > numBits) {
            throw new IllegalArgumentException("Requested allButFirstNBits, where N is equal to "
                    + N + ", but have " + numBits + ".");
        }

        BitSequence allButFirstN = new BitSequence();
        allButFirstN.bits = this.bits;
        allButFirstN.numBits = this.numBits - N;
        allButFirstN.firstBitNum = this.firstBitNum + N;
        allButFirstN.lastBitNum = this.lastBitNum;
        return allButFirstN;
    }

    public BitSequence lastNBits(int N) {
        if (N > numBits) {
            throw new IllegalArgumentException("More bits requested than present in sequence, "
                    + "requested " + N + ", but have " + numBits + ".");
        }

        BitSequence lastN = new BitSequence();
        lastN.bits = this.bits;
        lastN.numBits = N;
        lastN.lastBitNum = this.lastBitNum;
        lastN.firstBitNum = this.lastBitNum - N;

        return lastN;
    }

    public static BitSequence assemble(List<BitSequence> sequences) {
        BitSequence masterSequence = new BitSequence();
        for (BitSequence bs : sequences) {
            masterSequence.append(bs);
        }
        return masterSequence;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length(); i += 1) {
            sb.append(String.valueOf(bitAt(i)));
        }
        return sb.toString();
    }

    private void append(int bit) {
        if (bit == 1) {
            bits.set(numBits, true);
        }
        numBits += 1;
        lastBitNum += 1;
    }

    private void append(BitSequence bs) {
        for (int i = 0; i < bs.numBits; i += 1) {
            append(bs.bitAt(i));
        }
    }

    public static void main(String[] args) {
        BitSequence bs = new BitSequence();
        bs = bs.appended(1);
        System.out.println(bs.toString());
        bs = bs.appended(1);
        System.out.println(bs.toString());
    }
} 
