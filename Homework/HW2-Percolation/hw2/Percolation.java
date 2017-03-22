package hw2;                       

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private int size_of_sides;
	private int top;
	private int bottom;
	private int open_sites;
	private int WeightedQuickUnionUF uf;
	private int WeightedQuickUnionUF anti_back_wash;
	private boolean[] sites;
	
	public Percolation(int n)
	{
		if(n<=0)
		{
			throw new java.lang.IllegalArgumentException();
		}
		size_of_sides = n;
		top = n*n;
		bottom = n*n +1;
		uf = new WeightedQuickUnionUF(n*n +2);
		anti_back_wash = new WeightedQuickUnionUF(n*n +1);
		sites = new boolean[n*n +1];
		open_sites = 0;
		initialize();
	}
	
	private void initialize()
	{
		int i;
		for(i=0;i<size_of_sides;i++)
		{
			anti_back_wash.union(top,i);
		}
		int j;
		for(j=size_of_sides*(size_of_sides-1);j<size_of_sides*size_of_sides;j++)
		{
			uf.union(bottom,j);
		}
	}
	
	private int twoDTooneD(int i, int j)
	{
		return size_of_sides*i + j;
	}
	
	private void connectEmpty(int i, int j)
	{
		for(x:neighbours(i,j))
		{
			if(x!=-1 && sites[x])
			{
				uf.union(twoDTooneD(i,j),x);
				anti_back_wash.union(twoDTooneD(i,j),x);
				if(anti_back_wash.connected(top,x))
				{
					uf.union(top, twoDTooneD(i,j));
					anti_back_wash.union(top, twoDTooneD(i,j));
				}
			}
		}
	}
	
	private int[] neighbours(int i, int j)
	{
		int neighbours = {-1,-1,-1,-1};
		if(i==0)
		{
			neighbours[0] = twoDTooneD(i,j);
		}
		if(i>0)
		{
			neighbours[0] = twoDTooneD(i-1.j);
		}
		if(i!= size_of_sides - 1)
		{
			neighbours[1] = twoDTooneD(i+1,j);
		}
		if(j>0)
		{
			neighbours[2] = twoDTooneD(i,j-1);
		}
		if(j!=size_of_sides -1)
		{
			neighbours[3] = twoDTooneD(i,j+1);
		}
		return neighbours;
	}
	
	private void validateIndex(int i, int j)
	{
		if(i<0 || j<0 || i>size_of_sides-1 || j>size_of_sides-1)
		{
			throw new java.lang.IndexOutOfBoundsException;
		}
	}
	
	public void open(int i, int j)
	{
		validateIndex(i,j);
		int target = twoDTooneD(i,j);
		if(!sites[target])
		{
			sites[target] = true;
			connectEmpty(i,j);
			open_sites++;
		}
	}
	
	public boolean isOpen(int i, int j)
	{
		validateIndex(i,j);
		return (isOpen(i,j) && anti_back_wash.connected(top,twoDTooneD(i,j)));
	}
	
	public boolean isFull(int i, int j)
	{
		validateIndex(i,j);
		return (isOpen(i,j) && anti_back_wash.connected(top,twoDTooneD(i,j)));
	}
	
	public boolean percolates()
	{
		return uf.connected(top, bottom);
	}
	
	public int numberOfOpenSites()
	{
		return open_sites;
	}
	
	public static void main(String[] args)
	{
		
	}
}                       
