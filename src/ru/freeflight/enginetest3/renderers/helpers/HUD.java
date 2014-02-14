package ru.freeflight.enginetest3.renderers.helpers;

import android.graphics.*;
import java.util.*;
import ru.freeflight.enginetest3.touch.*;

public class HUD
{
	int sw, sh, halfside;
	Bitmap bmp;
	ArrayList<Pressable> buttons = new ArrayList();
	HashMap<Integer,ButtonActions> buttonMap = new HashMap();
	Paint bp = new Paint(), tp = new Paint();
	public HUD (int sw, int sh) {
		this.sw = sw;
		this.sh = sh;
		halfside = (sw+sh)/2/25;
		bmp = Bitmap.createBitmap(sw,sh,Bitmap.Config.ARGB_8888);
		populateHUD();
		Canvas c = new Canvas(bmp);
		drawButtonMap(c);
		tp.setTextSize((sw+sh)/2/60);
		tp.setAntiAlias(true);
		tp.setColor(Color.WHITE);
	}
	
	void populateHUD()
	{
		buttons.add(new Pressable(90,60,ButtonActions.throttle_forward,ButtonShapes.SQUARE));
		buttons.add(new Pressable(90,80,ButtonActions.throttle_backward,ButtonShapes.SQUARE));
		buttons.add(new Pressable(90,40,ButtonActions.throttle_right,ButtonShapes.SQUARE));
		buttons.add(new Pressable(90,20,ButtonActions.throttle_left,ButtonShapes.SQUARE));
		buttons.add(new Pressable(30,80,ButtonActions.throttle_turn_clockwise,ButtonShapes.SQUARE));
		buttons.add(new Pressable(10,80,ButtonActions.throttle_turn_counterclockwise,ButtonShapes.SQUARE));
		buttons.add(new Pressable(62,90,ButtonActions.time_scale_up,ButtonShapes.SQUARE));
		buttons.add(new Pressable(50,90,ButtonActions.time_scale_down,ButtonShapes.SQUARE));
		buttons.add(new Pressable(38,90,ButtonActions.time_pause_resume,ButtonShapes.SQUARE));
		buttons.add(new Pressable(22,90,ButtonActions.zoom_out,ButtonShapes.SQUARE));
		buttons.add(new Pressable(10,90,ButtonActions.zoom_in,ButtonShapes.SQUARE));
	}
	
	void drawButtonMap(Canvas c)
	{
		int counter = 0;
		c.drawColor(Color.WHITE);
		Iterator iter = buttons.iterator();
		while (iter.hasNext())
		{
			counter++;
			Pressable pre = (Pressable)iter.next();
			float realX = sw*pre.x/100F;
			float realY = sh*pre.y/100F;
			bp.setARGB(255, counter, counter, counter);
			if (pre.shape == ButtonShapes.SQUARE)
			{
				c.drawRect(realX-halfside,realY-halfside,realX+halfside,realY+halfside,bp);
			}
			buttonMap.put(bmp.getPixel((int)realX,(int)realY),pre.action);
			
		}
	}
	
	public void drawButtons(Canvas c)
	{
		Iterator iter = buttons.iterator();
		while (iter.hasNext())
		{
			bp.setARGB(128, 255, 255, 255);
			Pressable pre = (Pressable)iter.next();
			float realX = sw*pre.x/100F;
			float realY = sh*pre.y/100F;
			switch (pre.shape)
			{
				case SQUARE:
					c.drawRect(realX-halfside,realY-halfside,realX+halfside,realY+halfside,bp);
					break;
			}
			
			Path path = new Path();
			switch (pre.action)
			{
				case throttle_forward:
					bp.setStyle(Paint.Style.FILL_AND_STROKE);
					bp.setColor(Color.GREEN);
					path.moveTo(realX-halfside+5,realY+halfside-5);
					path.lineTo(realX,realY-halfside+5);
					path.lineTo(realX+halfside-5,realY+halfside-5);
					path.close();
					c.drawPath(path,bp);
					break;
				case throttle_backward:
					bp.setStyle(Paint.Style.FILL_AND_STROKE);
					bp.setColor(Color.RED);
					path.moveTo(realX-halfside+5,realY-halfside+5);
					path.lineTo(realX,realY+halfside-5);
					path.lineTo(realX+halfside-5,realY-halfside+5);
					path.close();
					c.drawPath(path,bp);
					break;
			}
			c.drawText(pre.action.toString(),realX-halfside,realY,tp);
			
		}
	}
	
	public ButtonActions getAction(int x, int y)
	{
		return buttonMap.get(bmp.getPixel(x,y));
	}
	
	//debug
	public int getPixel(int x, int y)
	{
		return bmp.getPixel(x,y);
	}
	
	public void setScreenDimensions(int sw, int sh)
	{
		this.sw = sw;
		this.sh = sh;
	}
	
	class Pressable {
		public int x,y,keycode;
		ButtonActions action;
		ButtonShapes shape;
		
		public Pressable(int x, int y, ButtonActions action, ButtonShapes shape)
		{
			this.x = x;
			this.y = y;
			//this.keycode = keycode;
			this.shape = shape;
			this.action = action;
		}
	}
	
	enum ButtonShapes
	{
		SQUARE, CIRCLE, RECT		
	}
}
