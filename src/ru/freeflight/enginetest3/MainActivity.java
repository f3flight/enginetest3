package ru.freeflight.enginetest3;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import ru.freeflight.enginetest3.renderers.*;

public class MainActivity extends Activity
{
	final public String tag = "EngineTest3";
	
	SurfaceViewRenderer svr;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		Log.d(tag,this.getClass().getSimpleName()+" onCreate");
		
		if (!isTaskRoot ()) {
			Intent intent = getIntent();
			String action = intent.getAction();
			if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action != null && action.equals(Intent.ACTION_MAIN)) {
				finish();
				return;
			}
		}
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (svr == null)
		{
			svr = new SurfaceViewRenderer(this);
			setContentView(svr);
		}
		else
			setContentView(svr);
    }

	@Override
	protected void onDestroy()
	{
		Log.d(tag,this.getClass().getSimpleName()+" onDestroy");
		super.onDestroy();
	}
	
}
