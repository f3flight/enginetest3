package ru.freeflight.enginetest3.threads;

import android.os.*;
import ru.freeflight.enginetest3.*;
import ru.freeflight.enginetest3.renderers.*;
import ru.freeflight.enginetest3.data.*;

public class MainThread extends ControllableThread
{

	SurfaceViewRenderer svr;
	
	public MainThread(MainActivity ma, int fpsMax, SurfaceViewRenderer svr) {
		super(ma, fpsMax);
		this.svr = svr;
	}
	@Override
	void loop()
	{
		if (svr.gameData.gameState == GameStates.initialization)
		{
			svr.renderLoading();
			svr.loadResources();
			svr.gameData.gameState = GameStates.space_travel;
		}
		svr.gameData.move(SystemClock.uptimeMillis(), this.getLastExecutionTimeOfLoop());
		svr.render();
	}

}
