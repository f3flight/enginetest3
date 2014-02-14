package ru.freeflight.enginetest3.threads;

import android.os.*;
import android.util.*;
import ru.freeflight.enginetest3.*;

// Controllable Thread class - version 0.2

abstract class ControllableThread extends Thread
{
	long startTime;
	long stepOfThread = 0;
	long stepOfLoop = 0;
	int fpsMax;
	int fpsMaxInitial;
	float fpsReal;
	long processingTime;
	long processingTimeMax;
	long lastExecutionTimeOfThread = 0, currentExecutionTimeOfThread = 0;
	long lastExecutionTimeOfLoop = 0, currentExecutionTimeOfLoop = 0;
	boolean isOnThread = true;
	boolean isOnLoop = true;
	MainActivity mainActivity;
	boolean debug = false;

	public ControllableThread(MainActivity ma, int fpsMax)
	{
		this.mainActivity = ma;
		this.fpsMax = fpsMax;
		fpsMaxInitial = fpsMax;
		processingTimeMax = 1000/fpsMax;
	}

	public void run()
	{
		startTime = SystemClock.uptimeMillis();
		while (isOnThread)
		{
			stepOfThread++;
			lastExecutionTimeOfThread = currentExecutionTimeOfThread;
			currentExecutionTimeOfThread = SystemClock.uptimeMillis();

			if (isOnLoop)
			{
				stepOfLoop++;
				lastExecutionTimeOfLoop = currentExecutionTimeOfLoop;
				currentExecutionTimeOfLoop = SystemClock.uptimeMillis();
				loop();
			}
			processingTime = SystemClock.uptimeMillis() - lastExecutionTimeOfThread;
			if (processingTime < processingTimeMax)
			{
				try
				{
					if (debug) Log.d(mainActivity.tag, this.getClass().getSimpleName() +" loop processing time is "+processingTime+"ms");
					sleep(processingTimeMax - processingTime);
				}
				catch (InterruptedException e)
				{}
			}
			fpsReal = 1000F / (SystemClock.uptimeMillis() - lastExecutionTimeOfThread);
		}
	}

	abstract void loop();

	public void setFPSMax(int fpsMax)
	{
		this.fpsMax = fpsMax;
		this.fpsMaxInitial = fpsMax;
		processingTimeMax = 1000/fpsMax;
	}

	public int getFPSMax()
	{
		return fpsMax;
	}

	public void turnThreadOff()
	{
		isOnThread = false;
	}

	public void setLoopState(boolean isOnLoop)
	{
		this.isOnLoop = isOnLoop;
	}

	public void pauseThread()
	{
		isOnLoop = false;
		fpsMax = 1;
	} 

	public void unpauseThread()
	{
		isOnLoop = true;
		fpsMax = fpsMaxInitial;
		lastExecutionTimeOfLoop = SystemClock.uptimeMillis() - processingTimeMax;
	} 
	
	public long getStepOfThread()
	{
		return stepOfThread;
	}
	
	public long getStepOfLoop()
	{
		return stepOfLoop;
	}
	
	public long getStartTime()
	{
		return startTime;
	}
	
	public long getLastExecutionTimeOfThread()
	{
		return lastExecutionTimeOfThread;
	}
	
	public long getLastExecutionTimeOfLoop()
	{
		if (lastExecutionTimeOfLoop != 0)
			return lastExecutionTimeOfLoop;
		else
			return SystemClock.uptimeMillis() - processingTimeMax;
	}
	
	public long getCurrentExecutionTimeOfLoop()
	{
		if (currentExecutionTimeOfLoop != 0)
			return currentExecutionTimeOfLoop;
		else
			return SystemClock.uptimeMillis();
	}

}
