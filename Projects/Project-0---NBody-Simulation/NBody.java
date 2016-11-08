public class NBody
{
	public static double readRadius (String s)
	{
		In in = new In(s);
		int n = in.readInt();
		double r = in.readDouble();	
		
		return r;
		
	}
	public static Planet[] readPlanets (String s)
	{
		In in = new In(s);
		int n = in.readInt();
		double r = in.readDouble();	
		int i,j;
		Planet [] planet = new Planet[n];
		for(i=0;i<n;i++)
		{
			double xp,xv,yp,yv,m;
			String img;
			xp = in.readDouble();
			yp = in.readDouble();
			xv = in.readDouble();
			yv = in.readDouble();
			m = in.readDouble();
			img = in.readString();
			Planet p = new Planet(xp,yp,xv,yv,m,img);
        	planet[i] = p;
			
		}
		return planet;
		
	}
	public static void main (String args[])
	{
		double t,dt;
		t = Double.parseDouble(args[0]);
		dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double radius = readRadius(filename);
		Planet [] planets  = readPlanets(filename);
		StdDraw.setScale(-radius, radius);
		StdDraw.clear();
		String backgroundImage = "./images/starfield.jpg";
		StdDraw.picture(0,0,backgroundImage);
		int i;
		for(i=0;i<planets.length;i++)
		{
			planets[i].draw();
		}
		double time = 0;
		while(time < t)
		{
			StdAudio.play("./audio/2001.mid");
			double [] xForces = new double[planets.length];
			double [] yForces = new double[planets.length];
			for(i=0;i<planets.length;i++)
			{
				xForces[i] = planets[i].calcNetForceExertedByX(planets);
				yForces[i] = planets[i].calcNetForceExertedByY(planets);
			}
			for(i=0;i<planets.length;i++)
			{
				planets[i].update(time,xForces[i],yForces[i]);
			}
			StdDraw.setScale(-radius, radius);
			StdDraw.clear();
			StdDraw.picture(0,0,backgroundImage);
			for(i=0;i<planets.length;i++)
			{
				planets[i].draw();
			}
			StdDraw.show(10);
			time += dt;
		}
		StdOut.printf("%d\n", planets.length);
		StdOut.printf("%.2e\n", radius);
		for (i = 0; i < planets.length; i++) 
		{
			StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
			planets[i].xxPos, planets[i].yyPos, planets[i].xxVel, planets[i].yyVel, planets[i].mass, planets[i].imgFileName);		
		}
		
	}
}