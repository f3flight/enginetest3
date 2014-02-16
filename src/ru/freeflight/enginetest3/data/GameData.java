package ru.freeflight.enginetest3.data;

import android.graphics.*;
import java.io.*;
import ru.freeflight.enginetest3.renderers.helpers.*;
import ru.freeflight.enginetest3.math.*;
import java.nio.*;

public class GameData
{
	float camScale;
	double zoom = 1, timeZoom = 1, timeScale;
	// vx, vy, rot values - meters/radians per second
	public double posX, posY, velocity, velocityAngle = 0, vx, vy, vPar, vPerp, bodyAngle, velocityBodyAngle, vrot;
	Vector2D velocityVector = new Vector2D(0, 0), engineVector = new Vector2D(0, 0);
	Vector2D gravityVector = new Vector2D(0, 0);
	// double engineAngle = 0, deltaAngle = 0, deltaVelocity = 0, lastVelocity = 0, oldVelocityAngle = 0, velocityAngleDifferenceSign = 0;
	double insideArccos, arccos;
	public double viewL, viewT, viewR, viewB;
	public int sw, sh;
	public Matrix camera = new Matrix();
	double timeShift, lastTimeZoom;
	public boolean timeStopped = false;
	double v;
	private int debugLine;
	private static final int debugLineShift = 15;
	public GameStates gameState = GameStates.initialization;

	// gravity test
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

	public void applyGravity()
	{
		dX = pXpos - posX;
		dY = pYpos - posY;
		distanceFromPlanetSqr = dX * dX + dY * dY;
		surfaceVertV = velocityVector.getX() * Math.sin(-bodyAngle) - velocityVector.getY() * Math.cos(-bodyAngle);
		surfaceHorizV = velocityVector.getX() * Math.cos(-bodyAngle) - velocityVector.getY() * Math.sin(-bodyAngle);

		if (Math.sqrt(distanceFromPlanetSqr) > pRad)
		{
			gForce = gravGamma * pMass / distanceFromPlanetSqr;
			gravityVector.set(
				gForce * Math.atan(dX / dY * Math.signum(dY)) * 2 / Math.PI,
				gForce * Math.atan(dY / dX * Math.signum(dX)) * 2 / Math.PI);
			velocityVector.addScaledRotated(gravityVector, timeScale, 0);
		}
		else if (velocityVector.getLength() > 0)
		{
			collisionG = v * timeZoom;
			velocityVector.zero();
		}
	}

	public GameData()
	{
		posX = 400F;
		posY = 600F;
		bodyAngle = 0F;
		velocityBodyAngle = 0F;
		timeZoom = 0.1;
	}

	public void setScreenDimentions(int sw, int sh)
	{
		this.sw = sw;
		this.sh = sh;
	}

	public void vParSet(double vPar)
	{
		this.vPar = vPar;
		engineVector.setX(vPar);
	}

	public void vPerpSet(double vPerp)
	{
		this.vPerp = vPerp;
		engineVector.setY(vPerp);
	}

	public void vrotSet(double vrot)
	{
		this.vrot = vrot;
	}

	public void timeZoomChange(double factor)
	{
		timeZoom = timeZoom * factor;
	}

	private void timePause()
	{
		lastTimeZoom = timeZoom;
		timeZoom = 0;
		timeStopped = true;
	}

	private void timeResume()
	{
		timeZoom = lastTimeZoom;
		timeStopped = false;
	}


	public void timeToggle()
	{
		if (!timeStopped) timePause();
		else timeResume();
	}

	public void zoomIn()
	{
		zoom = zoom * 2;
	}

	public void zoomOut()
	{
		zoom = zoom * 0.5;
	}

	public void move(long curTimeMillis, long lastTimeMillis)
	{
		timeShift = (curTimeMillis - lastTimeMillis) / 1000D;
		timeScale = timeShift * timeZoom;
	    //vx = vx + (vPar * Math.cos(bodyAngle / 180 * Math.PI)
		//	+ vPerp * Math.sin(bodyAngle / 180 * Math.PI)) * timeShift * timeScale;
		//vy = vy + (vPar * Math.sin(bodyAngle / 180 * Math.PI)
		//	+ vPerp * Math.cos(bodyAngle / 180 * Math.PI)) * timeShift * timeScale;
		//v = Math.sqrt(vx * vx + vy * vy);
		//oldVelocityAngle = Math.atan2(vy,vx)*180/Math.PI;
		//velocityAngle = oldVelocityAngle;
		//velocity = v;
		//deltaVelocity = 0;
		//deltaAngle = 0;
		//engineAngle = 0;
		//lastVelocity = velocity;
//		if (Math.abs(vPar)>0)
//		{
//			engineAngle = 0;
//			if (vPar>0) engineAngle = 0;
//			else if (vPar<0) engineAngle = 180;
//			velocityAngleDifferenceSign = Math.signum(velocityAngle-engineAngle);
//			if (velocityAngleDifferenceSign == 0) velocityAngleDifferenceSign = 1;
//			deltaVelocity = timeShift*timeScale*Math.abs(vPar);
//			deltaAngle = 180 - bodyAngle - engineAngle - velocityAngle;
//			velocity = Math.sqrt(velocity*velocity + deltaVelocity*deltaVelocity - 2*velocity*deltaVelocity*Math.cos(deltaAngle*Math.PI/180));
//			insideArccos = (lastVelocity*lastVelocity+velocity*velocity-deltaVelocity*deltaVelocity)/(2*lastVelocity*velocity);
//			arccos = (Math.abs(insideArccos)>1) ? Math.PI*Math.signum(insideArccos) : Math.acos(insideArccos);
//			velocityAngle = velocityAngle + velocityAngleDifferenceSign*arccos*180/Math.PI;
//		}
//		
//		if (Math.abs(vPerp)>0)
//		{
//
//			engineAngle = 0;
//			if (vPerp>0) engineAngle = 90;
//			else if (vPerp<0) engineAngle = -90;
//			velocityAngleDifferenceSign = Math.signum(velocityAngle-engineAngle);
//			if (velocityAngleDifferenceSign == 0) velocityAngleDifferenceSign = 1;
//			deltaVelocity = timeShift*timeScale*Math.abs(vPerp);
//			deltaAngle = 180 - bodyAngle - engineAngle - velocityAngle;
//			velocity = Math.sqrt(velocity*velocity + deltaVelocity*deltaVelocity - 2*velocity*deltaVelocity*Math.cos(deltaAngle*Math.PI/180));
//			insideArccos = (lastVelocity*lastVelocity+velocity*velocity-deltaVelocity*deltaVelocity)/(2*lastVelocity*velocity);
//			arccos = (Math.abs(insideArccos)>1) ? Math.PI*Math.signum(insideArccos) : Math.acos(insideArccos);
//			velocityAngle = velocityAngle + velocityAngleDifferenceSign*arccos*180/Math.PI;
//		}
//		
////		if (Math.abs(vPerp)>0)
////		{
////			engineAngle = 0;
////			if (vPerp>0) engineAngle = 90;
////			else if (vPerp<0) engineAngle = -90;
////			deltaVelocity = timeShift*timeScale*Math.sqrt(vPar*vPar + vPerp*vPerp);
////			deltaAngle = 180 - bodyAngle - engineAngle - velocityAngle;
////			velocity = Math.sqrt(velocity*velocity + deltaVelocity*deltaVelocity - 2*velocity*deltaVelocity*Math.cos(deltaAngle*Math.PI/180));
////			velocityAngle = velocityAngle - Math.acos((lastVelocity*lastVelocity+velocity*velocity-deltaVelocity*deltaVelocity)/(2*lastVelocity*velocity))*180/Math.PI;
////		}
//		
//		while (velocityAngle > 180)
//		{
//			velocityAngle = velocityAngle - 360;
//			break;
//		}
//		while (velocityAngle <= -180)
//		{
//			velocityAngle = velocityAngle + 360;
//			break;
//		}
//		
		velocityVector.addScaledRotated(engineVector, timeScale, bodyAngle);

		applyGravity();

		velocityBodyAngle = velocityBodyAngle + vrot * timeScale;
		posX = posX + velocityVector.getX() * timeScale;
		posY = posY + velocityVector.getY() * timeScale;
		//posX = posX + vx * timeShift * timeScale;
		//posY = posY + vy * timeShift * timeScale;
		bodyAngle = bodyAngle + velocityBodyAngle * timeScale;
		while (bodyAngle > Math.PI) bodyAngle = bodyAngle - Math.PI * 2;
		while (bodyAngle < -Math.PI) bodyAngle = bodyAngle + Math.PI * 2;
		//camera.setRotate(-angle, sw/2, sh/2);
		camera.setTranslate((float)(sw / 2 - posX), (float)(sh / 2 - posY));		

//		if (Math.sqrt(distanceFromPlanetSqr)<=pRad+3E5*5)
//		{
//			//bodyAngle = Math.atan2(dY,dX)/Math.PI*180-90;
//			camScale = 1 / (1 + (float)(Math.abs(surfaceVertV)* 18 * timeScale / sw));
//		} else
//		{	
		camScale = 1 / (1 + (float)(velocityVector.getLength() * 18 * timeZoom / sw));
//		}
		camScale = camScale * (float)zoom;
		camera.postScale(camScale, camScale, sw / 2, sh / 2);
		viewL = posX - (sw / 2) / camScale;
		viewR = posX + (sw / 2) / camScale;
		viewT = posY - (sh / 2) / camScale;
		viewB = posY + (sh / 2) / camScale;
	}

	public void drawDebugInfo(Canvas c, PaintPalette pp)
	{
		debugLine = 3;
		c.drawText("posX:" + String.format("%g", posX), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("posY:" + String.format("%g", posY), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		//c.drawText("vx:" + String.format("%g",vx), 10, debugLineShift * debugLine, pp.textPaint);
		//debugLine++;
		//c.drawText("vy:" + String.format("%g",vy), 10, debugLineShift * debugLine, pp.textPaint);
		//debugLine++;
		c.drawText("velocity:" + String.format("%g", velocity), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("velocityVector:" + velocityVector, 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("engineVector:" + engineVector, 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("gravityVector:" + gravityVector, 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		//c.drawText("oldVelocityAngle:" + String.format("%g",oldVelocityAngle), 10, debugLineShift * debugLine, pp.textPaint);
		//debugLine++;
		//c.drawText("velocityAngle:" + String.format("%g",velocityAngle), 10, debugLineShift * debugLine, pp.textPaint);
		//debugLine++;
		//c.drawText("velocityAngle:" + String.format("%g",velocityAngle), 10, debugLineShift * debugLine, pp.textPaint);
		//debugLine++;
		//c.drawText("deltaVelocity:" + String.format("%g",deltaVelocity), 10, debugLineShift * debugLine, pp.textPaint);
		//debugLine++;
		//c.drawText("deltaAngle:" + String.format("%g",deltaAngle), 10, debugLineShift * debugLine, pp.textPaint);
		//debugLine++;
		//c.drawText("engineAngle:" + String.format("%g", engineAngle), 10, debugLineShift * debugLine, pp.textPaint);
		//debugLine++;
		//c.drawText("insideArccos:" + String.format("%g", insideArccos), 10, debugLineShift * debugLine, pp.textPaint);
		//debugLine++;
		//c.drawText("arccos:" + String.format("%g", arccos), 10, debugLineShift * debugLine, pp.textPaint);
		//debugLine++;
		c.drawText("vPar:" + vPar, 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("vPerp:" + vPerp, 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("angle:" + String.format("%g", bodyAngle), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("rot:" + String.format("%g", velocityBodyAngle), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("vrot:" + vrot, 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("camScale:" + String.format("%5g", camScale), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		//c.drawText("viewL:" + viewL, 10, debugLineShift * debugLine, pp.textPaint);
		//debugLine++;
		//c.drawText("viewR:" + viewR, 10, debugLineShift * debugLine, pp.textPaint);
		//debugLine++;
		//c.drawText("viewT:" + viewT, 10, debugLineShift * debugLine, pp.textPaint);
		//debugLine++;
		//c.drawText("viewB:" + viewB, 10, debugLineShift * debugLine, pp.textPaint);
		//debugLine++;
		c.drawText("gForce:" + String.format("%g", gForce), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("collisionG:" + String.format("%g", collisionG), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("timeZoom:" + String.format("%g", timeZoom), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("surfaceVertV:" + String.format("%g", surfaceVertV), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
		c.drawText("surfaceHorizV:" + String.format("%g", surfaceHorizV), 10, debugLineShift * debugLine, pp.textPaint);
		debugLine++;
	}
}
