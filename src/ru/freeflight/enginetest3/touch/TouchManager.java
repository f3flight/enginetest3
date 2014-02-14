package ru.freeflight.enginetest3.touch;

import android.view.*;
import ru.freeflight.enginetest3.data.*;

public class TouchManager
{
	GameData gd;
	
	public TouchManager(GameData gd)
	{
		this.gd = gd;
	}
	
	public void ProcessTouch(MotionEvent e, ButtonActions ba)
	{
		switch (e.getAction())
		{ 
			case MotionEvent.ACTION_DOWN:
				switch (ba)
				{
					case throttle_forward:
						gd.vParSet(12);
						break;
						
					case throttle_backward:
						gd.vParSet(-12);
						break;
						
					case throttle_right:
						gd.vPerpSet(12);
						break;
						
					case throttle_left:
						gd.vPerpSet(-12);
						break;
						
					case throttle_turn_clockwise:
						gd.vrotSet(10);
						break;
						
					case throttle_turn_counterclockwise:
						gd.vrotSet(-10);
						break;
						
					case time_scale_up:
						gd.timeScaleChange(2);
						break;
					
					case time_scale_down:
						gd.timeScaleChange(0.5);
						break;
						
					case time_pause_resume:
						gd.timeToggle();
						break;
					
					case zoom_in:
						gd.zoomIn();
						break;
					
					case zoom_out:
						gd.zoomOut();
						break;
				}
				break;
				
			case MotionEvent.ACTION_UP:
				switch (ba)
				{
					case throttle_forward:
						gd.vParSet(0);
						break;
						
					case throttle_backward:
						gd.vParSet(0);
						break;
						
					case throttle_right:
						gd.vPerpSet(0);
						break;

					case throttle_left:
						gd.vPerpSet(0);
						break;

					case throttle_turn_clockwise:
						gd.vrotSet(0);
						break;

					case throttle_turn_counterclockwise:
						gd.vrotSet(0);
						break;
				}
				break;									
		}
	}
}
