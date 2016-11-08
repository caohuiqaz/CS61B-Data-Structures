public class Planet
{
	public double xxPos,yyPos,xxVel,yyVel,mass;
	public String imgFileName;
	public Planet(double xP,double yP,double xV,double yV, double m, String img)
	{
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
		
	}
	public Planet (Planet p)
	{
			xxPos = p.xxPos;
			yyPos = p.yyPos;
			xxVel = p.xxVel;
			yyVel = p.yyVel;
			mass = p.mass;
			imgFileName = p.imgFileName;
			
			
	}
	public double calcDistance(Planet x)
	{
		return Math.sqrt((this.xxPos - x.xxPos)*(this.xxPos - x.xxPos) + (this.yyPos - x.yyPos)*(this.yyPos - x.yyPos));
	}
	public double calcForceExertedBy(Planet x)
	{
		double g = 6.67 * Math.pow(10,-11);
		return (g*this.mass*x.mass)/(this.calcDistance(x)*this.calcDistance(x));
	}
	public double calcForceExertedByX(Planet x)
	{
		return (this.calcForceExertedBy(x)*(x.xxPos - this.xxPos))/this.calcDistance(x); 
	}
	public double calcForceExertedByY(Planet x)
	{
		return (this.calcForceExertedBy(x)*(x.yyPos - this.yyPos))/this.calcDistance(x);
	}
	public double calcNetForceExertedByX(Planet [] allplanet)
	{
		double totalforce = 0;
		int i;
		for(i=0;i<allplanet.length;i++)
		{
			if(!this.equals(allplanet[i]))
			totalforce += this.calcForceExertedByX(allplanet[i]);
		}
		return totalforce;
	}
	public double calcNetForceExertedByY(Planet [] allplanet)
	{
		double totalforce = 0;
		int i;
		for(i=0;i<allplanet.length;i++)
		{
			if(!this.equals(allplanet[i]))
			totalforce += this.calcForceExertedByY(allplanet[i]);
		}
		return totalforce;
	}
	public void update (double time, double xforce, double yforce)
	{
		double accX = xforce/this.mass;
		double accY = yforce/this.mass;
		 this.xxVel = this.xxVel + (accX*time);
		 this.yyVel = this.yyVel + (accY*time);
		xxPos = xxPos + (time*this.xxVel);
		yyPos = yyPos + (time*this.yyVel);
	}
	public void draw()
	{
		Planet p = new Planet(xxPos,yyPos,xxVel,yyVel,mass,imgFileName);
		
		StdDraw.picture(p.xxPos,p.yyPos,("./images/" + p.imgFileName));
	}
}