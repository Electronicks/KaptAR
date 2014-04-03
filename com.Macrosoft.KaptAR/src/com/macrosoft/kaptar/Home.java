package com.macrosoft.kaptar;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Home extends Activity implements OnClickListener
{
	Button	btnScanNow			= null;
	Button	btnConfiguration	= null;
	Button	btnExit				= null;

	/**
	 * extras key for activity title, usually static and set in Manifest.xml
	 */
	protected static final String EXTRAS_KEY_ACTIVITY_TITLE_STRING = "activityTitle";
	
	/**
	 * extras key for architect-url to load, usually already known upfront, can be relative folder to assets (myWorld.html --> assets/myWorld.html is loaded) or web-url ("http://myserver.com/myWorld.html"). Note that argument passing is only possible via web-url 
	 */
	protected static final String EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL = "activityArchitectWorldUrl";
	
	public static final String Licence = "g/wc2VhPq7D2vvwjyMHZ4bQTmVti4P94ODYKiKCFLoYHCzcguAJ7hpqWpT9bEYNxfh3Syqxa8IoGFbyrjUhxeSNYiRh705C75TWS+3B/unVWLdaRInRD+glhJLa68sQsrryw3UsUMIpLOrZAaZpYepQByllXrCJSv9C5pMdX7/ZTYWx0ZWRfX2s5LwoElVDbdVqU3AzV3Vyl5J0h6pKfe35pKy5QRRypNJQfVGWQZnb/af3DOVeC/cBAf9hZfCi+fU57mDxtkhnLpj80jrQ6FLen55eVBEOHLUzDUiMnHNwrSCznKnEB6Kbz+NPVSlyTc3ULIugEsIHchxTtkoSWVW8g1ShTC/qbS3SHv5JCrmAGIB6TpWed1jN40wUGJ2mk97qtZx7XrksQ5ij7enz0RBm8CSKfuChNzv88ol4FEyRjw1RsTL2cjmSb/UACIkY67eFAiLkLJeG9vgLuiQ2l+RP/f8yj+vtAbNcl1a86XcgMzhG4kDYeZfzG7+xCjP3UBIE3LpgvONUHrJko5c33iWJf29iJa6TbTJX0VG+AwGzGSs3e67TGqVxAz18G1kDB+HBOmRjWj7tgm8xVQ7XyVSc1rHkUwwPtQIMXpzvCfZHmkRH/AaxXhljbkPVr2Wi3u1Rwl2FtCOLPBxW9QKq8++zFDGGLLQ1Hk2q6FfLrFSM=";
	

	/**
	 * last time the calibration toast was shown, this avoids too many toast shown when compass needs calibration
	 */
	private long lastCalibrationToastShownTimeMillis = System.currentTimeMillis();
	
	private AsyncTask<Void,Void,Void> netWorker = null;

	
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_home );

		btnScanNow = ( Button ) findViewById( R.id.btnScanNow );
		btnConfiguration = ( Button ) findViewById( R.id.btnSettings );
		btnExit = ( Button ) findViewById( R.id.btnExit );

		btnScanNow.setOnClickListener( this );
		btnConfiguration.setOnClickListener( this );
		btnExit.setOnClickListener( this );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate( R.menu.home, menu );
		return true;
	}



	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		// TODO Auto-generated method stub
		super.onStop();
		netWorker.cancel( true );
	}

	@Override
	public void onClick( View arg0 )
	{
		if( arg0 == btnScanNow )
		{
			//Toast.makeText( this, CamActivity.class.getSimpleName() , Toast.LENGTH_SHORT ).show();
			startArActivity();
		}
		else if( arg0 == btnConfiguration )
		{
			netWorker = new NetworkWorker(this);
			netWorker.execute( );
			//Toast.makeText( this, "Config!!!", Toast.LENGTH_SHORT ).show();
		}
		else if( arg0 == btnExit )
		{
			leaveKaptAR();
		}
		else
		{
			Log.e( "Button manager", "Unknown view" );
		}
	}

	public void startArActivity()
	{
		try
		{
			final Intent intent = new Intent(this, CamActivity.class);
			intent.putExtra( EXTRAS_KEY_ACTIVITY_TITLE_STRING, "KaptAR World" );
			intent.putExtra( EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL,
					"KaptarWorld" + File.separator + "index.html" );
			/* launch activity */
			this.startActivity( intent );
			

		}
		catch( Exception e )
		{
			/*
			 * may never occur, as long as all SampleActivities exist and are
			 * listed in manifest
			 */
			Toast.makeText( this, "CamActivity\nnot defined/accessible", Toast.LENGTH_SHORT ).show();
		}
		

	}
	
	public void leaveKaptAR()
	{
		 Intent intent = new Intent(Intent.ACTION_MAIN);
		 intent.addCategory(Intent.CATEGORY_HOME);
		 startActivity(intent);
	}
}
