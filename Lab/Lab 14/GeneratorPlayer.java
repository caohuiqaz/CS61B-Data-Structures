import edu.princeton.cs.introcs.StdAudio;

public class GeneratorPlayer {
	private Generator generator;

	public GeneratorPlayer(Generator generator) {
		this.generator = generator;
	}

	public void play(int numSamples) {
		for (int ii = 0; ii < numSamples; ii += 1) {
			StdAudio.play(generator.next());		
		}
	}

	public static void main(String[] args) {
		Generator generator = new SineWaveGenerator(61);
		GeneratorPlayer gp = new GeneratorPlayer(generator);
		gp.play(1000000);
	}
}