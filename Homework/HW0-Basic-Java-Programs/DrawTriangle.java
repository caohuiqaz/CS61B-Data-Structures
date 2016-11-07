public class DrawTriangle
{
	public static void draw_triangle(int n)
	{
		int i,j;
		for(i=0;i<n+1;i++)
		{
			for(j=0;j<i;j++)
			{
				System.out.print("*");
			}
			System.out.println();
		}
	}
	public static void main (String args[])
	{
		int r = Integer.parseInt(args[0]);
		draw_triangle(r);
	}
	
}