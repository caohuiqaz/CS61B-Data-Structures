package hw2;                       
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
public class PercolationStats {

private int size_of_side;
private int number_of_trials;
private double[] result;

public PercolationStats(int N, int T)
{
	if(N<=0 || T<=0)
	{
		throw new java.lang.IllegalArgumentException();
	}
	size_of_side = N;
	number_of_trials = T;
	result = new double[T];
	runTrails();
}

private void runTrails()
{
	int i;
	for(i=0;i<number_of_trials;i++)
	{
		Percolation per = new Percolation(size_of_side);
		double open_sites = 0;
		while(!per.percolates())
		{
			int x = StdRandom.uniform(1,size_of_side +1);
			int y = StdRandom.uniform(1,size_of_side +1);
			if(!per.isOpen(y,x))
			{
				per.open(y,x);
			}
		}
		result[i] = open_sites/(size_of_side*size_of_side);
	}
}

public double mean()
{
	return StdStats.mean(result);
}

public double stddev()
{
	return StdStats.stddev(result);
}

public double confidenceLo()
{
	return mean() - 1.96 * stddev()/Math.sqrt(number_of_trials);
}

public double confidenceHi()
{
	return mean() + 1.96 * stddev()/Math.sqrt(number_of_trials);
}

public static void main(String[] args)
{
	PercolationStats ps = new PercolationStats(200,1000);
	System.out.println("mean = "+ps.mean());
	System.out.println("stddev = "+ps.stddev());
	System.out.println("interval = "+ps.confidenceLo() + " " + ps.confidenceHi());
}
}                       

