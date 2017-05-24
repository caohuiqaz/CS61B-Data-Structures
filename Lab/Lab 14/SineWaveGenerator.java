public class SineWaveGenerator implements Generator {
    private double frequency;
    private int state;

    public SineWaveGenerator(double frequency) {
        state = 0;
        this.frequency = frequency;
    }

    public double next() {
        state = (state + 1);
        double period = StdAudio.SAMPLE_RATE / frequency;
        return Math.sin(state * 2 * Math.PI / period);
    }
}

