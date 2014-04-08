package com.macrosoft.kaptar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.ArchitectView.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView.CaptureScreenCallback;
import com.wikitude.architect.ArchitectView.SensorAccuracyChangeListener;

public class CamActivity extends AbstractArchitectCamActivity
{

	/**
	 * last time the calibration toast was shown, this avoids too many toast
	 * shown when compass needs calibration
	 */
	private long	lastCalibrationToastShownTimeMillis	= System.currentTimeMillis();
	/**
	 * extras key for activity title, usually static and set in Manifest.xml
	 */
	protected static final String EXTRAS_KEY_ACTIVITY_TITLE_STRING = "activityTitle";
	/**
	 * extras key for architect-url to load, usually already known upfront, can be relative folder to assets (myWorld.html --> assets/myWorld.html is loaded) or web-url ("http://myserver.com/myWorld.html"). Note that argument passing is only possible via web-url 
	 */
	protected static final String EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL = "activityArchitectWorldUrl";
	protected static final String EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_JS = "activityArchitectWorldJs";

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event )
	{
		super.onKeyDown( keyCode, event );
		int code = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences( CamActivity.this ).getString( "Bouton partage", "2" ));
		
		if ( (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN && code == 2) || (keyCode == KeyEvent.KEYCODE_VOLUME_UP && code == 1) )
		{
			TakeScreenshot();
			//Toast.makeText( this, "Screenshot saved to SD/KaptAR Screenshots/" + imagename, Toast.LENGTH_SHORT ).show();
		}
	 	return true;
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onPostCreate( Bundle savedInstanceState )
	{
		super.onPostCreate( savedInstanceState );
	}

	@Override
	protected void onPostResume()
	{
		super.onPostResume();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		String world = (String) getIntent().getExtras().getString( EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_JS );
		this.architectView.callJavascript( world );
	}

	@Override
	public float getInitialCullingDistanceMeters()
	{
		return ArchitectViewHolderInterface.CULLING_DISTANCE_DEFAULT_METERS;
	}

	@Override
	public String getActivityTitle()
	{
		return getIntent().getExtras().getString( EXTRAS_KEY_ACTIVITY_TITLE_STRING );
	}

	@Override
	public String getARchitectWorldPath()
	{
		String url = (String) getIntent().getExtras().getString( EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL );
		if(url.startsWith( "http" ))
		{
		 	try
			{
				url = URLDecoder.decode(url , "UTF-8" );
			}
			catch( UnsupportedEncodingException e )
			{
				Toast.makeText( this, "Unexpected Exception: " + e.getMessage(), Toast.LENGTH_LONG ).show();
				e.printStackTrace();
				return null;
			}
		}
		return url;
	}

	@Override
	public ArchitectUrlListener getUrlListener()
	{
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
		return R.layout.architectview_layout;
	}

	@Override
	public String getWikitudeSDKLicenseKey()
	{
		return Home.getWikitudeLicence();
	}

	@Override
	public int getArchitectViewId()
	{
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
	private void TakeScreenshot()
	{
		this.architectView.captureScreen( ArchitectView.CaptureScreenCallback.CAPTURE_MODE_CAM_AND_WEBVIEW, new CaptureScreenCallback()
			{
				@Override
				public void onScreenCaptured( final Bitmap screenCapture )
				{
					// store screenCapture into external cache directory
					String dir = Environment.getExternalStorageDirectory().toString() + "/KaptAR Screenshots/";
					(new File(dir)).mkdirs();
					final File screenCaptureFile = new File( dir, "screenCapture_" + System.currentTimeMillis() + ".jpg" );
	
					// 1. Save bitmap to file & compress to jpeg. You may
					// use PNG too
					try
					{
						final FileOutputStream out = new FileOutputStream( screenCaptureFile );
						screenCapture.compress( Bitmap.CompressFormat.JPEG, 90, out );
						out.flush();
						out.close();
						
						if(PreferenceManager.getDefaultSharedPreferences( CamActivity.this ).getBoolean( "Partage d'image", true ))
						{
							// 2. create send intent
							final Intent share = new Intent( Intent.ACTION_SEND );
							share.setType( "image/jpg" );
							share.putExtra( Intent.EXTRA_STREAM, Uri.fromFile( screenCaptureFile ) );
		
							// 3. launch intent-chooser
							final String chooserTitle = "Partager l'image";
							CamActivity.this.startActivity( Intent.createChooser( share, chooserTitle ) );
	
						}
						else
						{
							CamActivity.this.runOnUiThread( new Runnable()
							{
								@Override
								public void run()
								{
									Toast.makeText( CamActivity.this, "Capture sauvegardé!", Toast.LENGTH_SHORT ).show();
								}
							} );
						}
					}
					catch( final Exception e )
					{
						// should not occur when all permissions are set
						CamActivity.this.runOnUiThread( new Runnable()
						{
							@Override
							public void run()
							{
								// show toast message in case something went
								// wrong
								Toast.makeText( CamActivity.this, "Unexpected error, " + e.getMessage(),
										Toast.LENGTH_LONG ).show();
							}
						} );
					}
				}
			} );
	}

}
