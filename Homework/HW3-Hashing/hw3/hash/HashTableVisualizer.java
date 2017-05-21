package hw3.hash;

import java.util.HashSet;
import java.util.Set;

public class HashTableVisualizer {

    public static void main(String[] args) {
        /* scale: StdDraw scale
           N:     number of items
           M:     number of buckets */

        double scale = 1.0;
        int N = 50;
        int M = 10;

        HashTableDrawingUtility.setScale(scale);
        Set<Oomage> oomies = new HashSet<Oomage>();
        for (int i = 0; i < N; i += 1) {
            oomies.add(SimpleOomage.randomSimpleOomage());
        }
        visualize(oomies, M, scale);
    }

    public static void visualize(Set<Oomage> set, int M, double scale) {
        HashTableDrawingUtility.drawLabels(M);
		int [] currentPos = new int[M];
		for(int i=0; i<M;i++)
		{
			currentPos[i] =0;
		}
		for(Oomage someOomage: set)
		{
			int y = (someOomage.hashcode()& 0x7FFFFFFF)%M;
			int x = currentPos[y];
			someOomage.draw(HashTableDrawingUtility.xCoord(x),HashTableDrawingUtility.yCoord(y,M), scale);
			currentPos[y] +=1;
		}
    }
} 
