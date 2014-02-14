package ru.freeflight.enginetest3.data;

import android.graphics.*;
import ru.freeflight.enginetest3.renderers.helpers.*;
import android.view.LayoutInflater.*;

public class GameData
{
	float camScale;
	double zoom = 1;
	// vx, vy, rot values - meters/degrees per second
	public double x, y, vx, vy, vPar, vPerp, angle, rot, vrot;
	public double viewL, viewT, viewR, viewB;
	public int sw, sh;
	public Matrix camera = new Matrix();
	double timeShift, timeScale, lastTimeScale;
	public boolean timeStopped = false;
	double v;
	private int debugLine;
	private static final int debugLineShift = 15;
	public GameStates gameState = GameStates.initialization;

	// gravity test
	//public double mass = 2000;
	public double pMass = 5.972E24;
	public double gravGamma = 6.67E-11;
	public double pXpos = 7E6;
	public double pYpos = 9E6;
	public double pRad = 6.3E6;
	public double dX = 0;
	public double dY = 0;
	public double gForce = 0;
	public double distanceFromPlanetSqr = 0;
	public double collisionG = 0;
	double surfaceVertV = 0;
	double surfaceHorizV = 0;
	
	public void applyGravity(double deltaTime)
	{
		dX = pXpos - x;
		dY = pYpos - y;
		distanceFromPlanetSqr = dX * dX + dY * dY;
		surfaceVertV = vx*Math.sin(-angle*Math.PI/180)-vy*Math.cos(-angle*Math.PI/180);
		surfaceHorizV =vx*Math.cos(-angle*Math.PI/180)-vy*Math.sin(-angle*Math.PI/180);
		if (Math.sqrt(distanceFromPlanetSqr) > pRad)
		{
			gForce = gravGamma * pMass / distanceFromPlanetSqr;
			vx = vx + gForce * deltaTime * Math.atan(dX / dY * Math.signum(dY)) * 2 / Math.PI;
			vy = vy + gForce * deltaTime * Math.atan(dY / dX * Math.signum(dX)) * 2 / Math.PI;
		}
		else if (Math.abs(vx)+Math.abs(vy)>0)
		{
			collisionG = v*timeScale/deltaTime;
			vx = 0;
			vy = 0;
		}
	}

	public GameData()
	{
		x = 400F;
		y = 600F;
		vx = 0F;
		vy = 0F;
		angle = 0F;
		rot = 0F;
		timeScale = 100;
	}

	public void setScreenDimentions(int sw, int sh)
	{
		this.sw = sw;
		this.sh = sh;
	}

	public void vParAdd(float vPar)
	{
		this.vPar = this.vPar + vPar;
	}

	public void vParSet(float vPar)
	{
		this.vPar = vPar;
	}

	public void vPerpAdd(float vPerp)
	{
		this.vPerp = this.vPerp + vPerp;
	}

	public void vPerpSet(float vPerp)
	{
		this.vPerp = vPerp;
	}

	public void vrotAdd(float vrot)
	{
		this.vrot = this.vrot + vrot;
	}

	public void vrotSet(float vrot)
	{
		this.vrot = vrot;
	}

	public void timeScaleChange(double factor) {
		timeScale = timeScale*factor;
	}
	
	private void timePause()
	{
		lastTimeScale = timeScale;
		timeScale = 0;
		timeStopped = true;
	}
	
	private void timeResume()
	{
		timeScale = lastTimeScale;
		timeStopped = false;
	}
	
	
	public void timeToggle()
	{
		if (!timeStopped) timePause();
		else timeResume();
	}
	
	public void zoomIn() {
		zoom = zoom * 2;
	}
	
	public void zoomOut() {
		zoom = zoom * 0.5;
	}
	
	public void move(long curTimeMillis, long lastTimeMillis)
	{
		timeShift = (curTimeMillis - lastTimeMillis) / 1000D;
	    vx = vx + (vPar * Math.cos(angle / 180 * Math.PI)
			+ vPerp * Math.sin(angle / 180 * Math.PI)) * timeShift * timeScale;
		vy = vy + (vPar * Math.sin(angle / 180 * Math.PI)
			+ vPerp * Math.cos(angle / 180 * Math.PI)) * timeShift * timeScale;
		v = Math.sqrt(vx * vx + vy * vy);	
		applyGravity(timeShift * timeScale);
		rot = rot + vrot * timeShift * timeScale;
		x = x + vx * timeShift * timeScale;
		y = y + vy * timeShift * timeScale;
		angle = angle + rot * timeShift * timeScale;
		while (angle > 180) angle = angle - 180;
		while (angle < -180) angle = angle + 180;
		//camera.setRotate(-angle, sw/2, sh/2);
		camera.setTranslate((float)(sw / 2 - x), (float)(sh / 2 - y));		
		
		if (Math.sqrt(distanceFromPlanetSqr)<=pRad+3E5*5)
		{
			angle = Math.atan2(dY,dX)/Math.PI*180-90;
			camScale = 1 / (1 + (float)(Math.abs(surfaceVertV)* 18 * timeScale / sw));
		} else
		{	
			camScale = 1 / (1 + (float)(v * 18 * timeScale / sw));
		}
		camScale = camScale * (float)zoom;
		camera.postScale(camScale, camScale, sw / 2, sh / 2);
		viewL = x - (sw / 2) / camScale;
		viewR = x + (sw / 2) / camScale;
		viewT = y - (sh / 2) / camScale;
		viewB = y + (sh / 2) / camScale;
	}

	public void drawDebugInfo(Canvas c, PaintPalette pp)
	{
		debugLine = 3;
		c.drawText("x:" + String.format("%g",x), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("y:" + String.format("%g",y), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("vx:" + String.format("%g",vx), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("vy:" + String.format("%g",vy), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("vPar:" + vPar, 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("vPerp:" + vPerp, 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("angle:" + String.format("%g",angle), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("rot:" + String.format("%g",rot), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("vrot:" + vrot, 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("camScale:" + String.format("%5g",camScale), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		//c.drawText("viewL:" + viewL, 10, debugLineShift * debugLine, pp.textPaint);
		//debugLine++;
		//c.drawText("viewR:" + viewR, 10, debugLineShift * debugLine, pp.textPaint);
		//debugLine++;
		//c.drawText("viewT:" + viewT, 10, debugLineShift * debugLine, pp.textPaint);
		//debugLine++;
		//c.drawText("viewB:" + viewB, 10, debugLineShift * debugLine, pp.textPaint);
		//debugLine++;
		c.drawText("gForce:" + String.format("%g",gForce), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("collisionG:" + String.format("%g",collisionG), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("timeScale:" + String.format("%g",timeScale), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("surfaceVertV:" + String.format("%g",surfaceVertV), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("surfaceHorizV:" + String.format("%g",surfaceHorizV), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
	}
}
