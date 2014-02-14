package ru.freeflight.enginetest3.renderers.helpers;

import android.graphics.*;

public class PaintPalette
{
	public Paint textPaint = new Paint();
	public Paint paint1 = new Paint();
	public Paint paint2 = new Paint();
	public Paint paint3 = new Paint();
	public Paint paint4 = new Paint();
	public Paint paint5 = new Paint();
	public Paint paint6 = new Paint();
	
	public PaintPalette()
	{
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(13);
		textPaint.setAntiAlias(true);
		textPaint.setTypeface(Typeface.MONOSPACE);
		
		paint1.setColor(Color.LTGRAY);
		
		paint2.setColor(Color.GRAY);
		
		paint3.setColor(Color.GREEN);
		
		paint4.setColor(Color.RED);
		
		paint5.setColor(Color.BLUE);
		
		paint6.setColor(Color.CYAN);
		
	}
	
}
