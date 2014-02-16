package ru.freeflight.enginetest3.math;

public class Vector2D
{
	private double x, y, rotX, rotY;
	
	public Vector2D(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public void setX(double x)
	{
		this.x = x;
	}

	public double getX()
	{
		return x;
	}

	public void setY(double y)
	{
		this.y = y;
	}

	public double getY()
	{
		return y;
	}
	
	public void set(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void zero()
	{
		x = 0;
		y = 0;
	}
	
	public double getAngle()
	{
		return Math.atan2(y,x);
	}
	
	public double getLength()
	{
		return Math.sqrt(x*x+y*y);
	}
	
	public void add(Vector2D addVect)
	{
		set(x+addVect.getX(),y+addVect.getY());
	}
	
	public void add(double x, double y)
	{
		set(this.x+x,this.y+y);
	}

	public void addScaledRotated(Vector2D addVect, double s, double r)
	{
		rotX = addVect.getLength()*Math.cos(addVect.getAngle()+r);
		rotY = addVect.getLength()*Math.sin(addVect.getAngle()+r);
		set(x+rotX*s,y+rotY*s);
	}
	
	public void scale(double s)
	{
		set(x * s,y * s);
	}
	
	@Override
	public String toString()
	{
		return String.format("x:%g; y:%g",x, y);
	}
	
}
