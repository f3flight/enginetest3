package ru.freeflight.enginetest3.renderers;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import ru.freeflight.enginetest3.*;
import ru.freeflight.enginetest3.data.*;
import ru.freeflight.enginetest3.renderers.helpers.*;
import ru.freeflight.enginetest3.threads.*;
import ru.freeflight.enginetest3.touch.*;

public class SurfaceViewRenderer extends SurfaceView implements SurfaceHolder.Callback, OnTouchListener
{

	MainActivity mainActivity;
	MainThread mainThread;
	double[] fpsArray = new double[50];
	double fpsAverage;
	int fpsPointer = 0;
	long lastRunTime = 0;
	
	PaintPalette pp = new PaintPalette();
	TouchManager tm;
	public GameData gameData = new GameData();
	HUD hud;
	
	Bitmap ship;
	Bitmap bg;
	Rect sourceRect = new Rect(), destRect = new Rect();
	
	//for debug purp
	int lastTouchX = 0, lastTouchY = 0;
	
	public SurfaceViewRenderer(Context context)
	{
		super(context);
		mainThread = new MainThread((MainActivity)context, 30, this);
		getHolder().addCallback(this);
		setOnTouchListener(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder p1)
	{
		Log.d(mainActivity.tag, this.getClass().getSimpleName() + " surfaceCreated");
		gameData.setScreenDimentions(getWidth(),getHeight());
		if (tm == null) tm = new TouchManager(gameData);
		if (hud == null) hud = new HUD(getWidth(),getHeight());
			else hud.setScreenDimensions(getWidth(),getHeight());
		if (!mainThread.isAlive()) mainThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder p1, int p2, int p3, int p4)
	{
		Log.d(mainActivity.tag, this.getClass().getSimpleName() + " surfaceChanged");
		//gameData.setScreenDimentions(getWidth(),getHeight());
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder p1)
	{
		Log.d(mainActivity.tag, this.getClass().getSimpleName() + " surfaceDestroyed");
		mainThread.turnThreadOff();
	}

	public void loadResources()
	{
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inPreferredConfig = Bitmap.Config.RGB_565;
		ship = BitmapFactory.decodeResource(getResources(), R.drawable.test3);
		bg = BitmapFactory.decodeResource(getResources(), R.drawable.space, bfo);
	}
	
	public void renderLoading()
	{
		Canvas c = this.getHolder().lockCanvas();
		if (c != null)
		{
			c.drawColor(Color.MAGENTA);
			c.drawText("Loading...",gameData.sw/2,gameData.sh/2,pp.textPaint);
			this.getHolder().unlockCanvasAndPost(c);
		}
	}
	
	public void render()
	{
		Canvas c = this.getHolder().lockCanvas();
		if (c != null)
		{			
		    c.drawColor(Color.MAGENTA);
			
			int xshift = -(int)(gameData.x/100000)%bg.getWidth();
			if (xshift > 0) xshift = xshift-bg.getWidth();
			int yshift = -(int)(gameData.y/100000)%bg.getHeight();
			if (yshift > 0) yshift = yshift-bg.getHeight();
			int xtimes = (int)(Math.ceil(gameData.sw/(float)bg.getWidth())+Math.max(0,-Math.signum(xshift+bg.getWidth()-gameData.sw)));
			int ytimes = (int)(Math.ceil(gameData.sh/(float)bg.getHeight())+Math.max(0,-Math.signum(yshift+bg.getHeight()-gameData.sh)));
			//int xtimes = 1+(int)(Math.abs(xshift)%bg.getWidth());
			//int ytimes = (int)((gameData.sh+yshift)/bg.getHeight());
			sourceRect.set(xshift,yshift,bg.getWidth(),bg.getHeight());
			for (int xgo = 0; xgo < xtimes; xgo++)
			{
				for (int ygo = 0; ygo < ytimes; ygo++)
				{
					destRect.set(xgo*bg.getWidth(),ygo*bg.getHeight(),(xgo+1)*bg.getWidth(),(ygo+1)*bg.getHeight());
					c.drawBitmap(bg,xshift+xgo*bg.getWidth(),yshift+ygo*bg.getHeight(),null);
				}
			}
			
			c.setMatrix(gameData.camera);
			
			c.drawRect(0,0,400,300,pp.paint1);
			c.drawRect(400,0,800,300,pp.paint2);
			c.drawRect(0,300,400,600,pp.paint3);
			c.drawRect(400,300,800,600,pp.paint4);
			c.drawRect(0,600,400,900,pp.paint5);
			c.drawRect(400,600,800,900,pp.paint6);
			
			//draw planet! hehehe
			c.drawCircle((float)gameData.pXpos,(float)gameData.pYpos, (float)(gameData.pRad+3E5),pp.paint5);
			c.drawCircle((float)gameData.pXpos,(float)gameData.pYpos, (float)(gameData.pRad),pp.paint3);
			c.drawLine((float)gameData.x,(float)gameData.y,(float)gameData.pXpos,(float)gameData.pYpos,pp.paint4);
			//c.drawLine(0,0,gameData.x,gameData.y,pp.paint5);
			//c.drawCircle(1000,1000,100,pp.textPaint);
			//c.drawCircle(3000,0,50,pp.textPaint);
			//c.drawCircle(0,5000,300,pp.textPaint);
			
			Matrix m = new Matrix();
			m.setRotate((float)gameData.angle,64,64);
			m.postTranslate((float)gameData.x-64, (float)gameData.y-64);
			c.drawBitmap(ship, m, null);
			
			c.setMatrix(null);
			
			c.drawCircle(gameData.sw/2, gameData.sh/2, 2, pp.textPaint);
			
			hud.drawButtons(c);
			
//			c.drawText("lastTouchX:"+lastTouchX,10,500,pp.textPaint);
//			c.drawText("lastTouchY:"+lastTouchY,10,520,pp.textPaint);
//			c.drawText("getPixel:"+(hud.getPixel(lastTouchX,lastTouchY)),10,540,pp.textPaint);
//			c.drawText("getAction:"+hud.getAction(lastTouchX,lastTouchY),10,560,pp.textPaint);
			
			//c.drawText("check:"+Math.ceil(gameData.sw/(float)bg.getWidth()),10,620,pp.textPaint);
//			c.drawText("xshift:"+xshift,10,640,pp.textPaint);
//			c.drawText("yshift:"+yshift,10,660,pp.textPaint);
//			c.drawText("xtimes:"+xtimes,10,680,pp.textPaint);
//			c.drawText("ytimes:"+ytimes,10,700,pp.textPaint);
			gameData.drawDebugInfo(c, pp);
			
			double fpsAverage = 0;
			for (double fps : fpsArray) fpsAverage = fpsAverage + fps;
			fpsAverage = fpsAverage / fpsArray.length;
			if (fpsPointer >= fpsArray.length) fpsPointer = 0;
			fpsArray[fpsPointer] = 1000F/(SystemClock.uptimeMillis()-lastRunTime);
			lastRunTime = SystemClock.uptimeMillis();
			c.drawText("Aver. fps: "+(int)fpsAverage,10,25,pp.textPaint);
			fpsPointer++;
			this.getHolder().unlockCanvasAndPost(c);
		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent event)
	{
		//Log.d(mainActivity.tag, this.getClass().getSimpleName()+" touch "+event.getX()+" "+event.getY());
		lastTouchX = (int)event.getX();
		lastTouchY = (int)event.getY();
		tm.ProcessTouch(event,hud.getAction(lastTouchX,lastTouchY));
		return true;
	}

}
