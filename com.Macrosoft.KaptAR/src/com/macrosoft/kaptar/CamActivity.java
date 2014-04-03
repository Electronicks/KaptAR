package com.macrosoft.kaptar;

import android.content.Intent;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.wikitude.architect.ArchitectView.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView.SensorAccuracyChangeListener;

public class CamActivity extends AbstractArchitectCamActivity
{

	/**
	 * last time the calibration toast was shown, this avoids too many toast
	 * shown when compass needs calibration
	 */
	private long	lastCalibrationToastShownTimeMillis	= System.currentTimeMillis();

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		// TODO Auto-generated method stub
		super.onCreate( savedInstanceState );
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onPostCreate( Bundle savedInstanceState )
	{
		// TODO Auto-generated method stub
		super.onPostCreate( savedInstanceState );

		/*this.architectView.onPostCreate();
		try
		{
			this.architectView.load( getIntent().getExtras().getString( Home.EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL ) );
		}
		catch( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	@Override
	protected void onPostResume()
	{
		// TODO Auto-generated method stub
		super.onPostResume();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}

	public CamActivity()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public float getInitialCullingDistanceMeters()
	{
		// TODO Auto-generated method stub
		return ArchitectViewHolderInterface.CULLING_DISTANCE_DEFAULT_METERS;
	}

	@Override
	public String getActivityTitle()
	{
		// TODO Auto-generated method stub
		return getIntent().getExtras().getString( Home.EXTRAS_KEY_ACTIVITY_TITLE_STRING );
	}

	@Override
	public String getARchitectWorldPath()
	{
		// TODO Auto-generated method stub
		return getIntent().getExtras().getString( Home.EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL );
	}

	@Override
	public ArchitectUrlListener getUrlListener()
	{
		// TODO Auto-generated method stub
		return new ArchitectUrlListener()
		{

			@Override
			public boolean urlWasInvoked( String uriString )
			{
				// by default: no action applied when url was invoked
				return false;
			}
		};
	}

	@Override
	public int getContentViewId()
	{
		// TODO Auto-generated method stub
		return R.layout.architectview_layout;
	}

	@Override
	public String getWikitudeSDKLicenseKey()
	{
		// TODO Auto-generated method stub
		return Home.Licence;
	}

	@Override
	public int getArchitectViewId()
	{
		// TODO Auto-generated method stub
		return R.id.architectView;
	}

	@Override
	public ILocationProvider getLocationProvider( LocationListener locationListener )
	{
		return null;
	}

	@Override
	public SensorAccuracyChangeListener getSensorAccuracyListener()
	{
		// TODO Auto-generated method stub
		return new SensorAccuracyChangeListener()
		{
			@Override
			public void onCompassAccuracyChanged( int accuracy )
			{
				/* UNRELIABLE = 0, LOW = 1, MEDIUM = 2, HIGH = 3 */
				if( accuracy < SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM && CamActivity.this != null
						&& CamActivity.this.isFinishing()
						&& System.currentTimeMillis() - CamActivity.this.lastCalibrationToastShownTimeMillis > 5 * 1000 )
				{
					Toast.makeText( CamActivity.this, "low accuracy", Toast.LENGTH_LONG ).show();
					CamActivity.this.lastCalibrationToastShownTimeMillis = System.currentTimeMillis();
				}
			}
		};
	}

}
