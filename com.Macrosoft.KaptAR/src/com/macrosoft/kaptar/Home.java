package com.macrosoft.kaptar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Intent;
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
	
	//à mettre dans shared prefs
	//public static String KaptarWorldPath = "";
	

	/**
	 * last time the calibration toast was shown, this avoids too many toast shown when compass needs calibration
	 */
	//private long lastCalibrationToastShownTimeMillis = System.currentTimeMillis();
	
	//private AsyncTask<Void,Void,Void> netWorker = null;

	
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
		
		/*
		try
		{
			KaptarWorldPath = this.getExternalFilesDir( null ).getCanonicalPath() + "/KaptarWorld/";
		}
		catch( IOException e )
		{
			e.printStackTrace();
			Log.e( "Home on create", "No access to files!" );
		}
		
		CopyWorldFiles();
		*/
	}
/*
	private void CopyWorldFiles()
	{
		AssetManager assets = this.getAssets();
		String[] filelist = {"index.html", "js/ade.js","js/compatibility.js","js/converter_off.js","wtc/Kaptar0.4.wtc","augmentation/PrinceOfPersia/a_wilson.jpg", "js/imagerecognition.js"};
		File dir = new File(KaptarWorldPath + "js");
		dir.mkdirs();
		dir = new File(KaptarWorldPath + "wtc");
		dir.mkdirs();
		
		dir = new File(KaptarWorldPath + "augmentation/PrinceOfPersia");
		dir.mkdirs();
		
		
		for(String filename : filelist)
		{
			File file = new File(KaptarWorldPath + filename);
			if(!file.exists())
			{
				Log.d( "CopyWorldFiles", "Copying missing file" + filename );
				try
				{
					InputStream is = assets.open( "KaptarWorld/" + filename );
					FileOutputStream fos = new FileOutputStream( file );
					for(int mybyte = is.read() ; mybyte != -1 ; mybyte = is.read())
					{
						fos.write( mybyte );
					}//next byte
					is.close();
					fos.close();
					Log.d( "CopyWorldFiles", "Successful copying of " + filename );
				}
				catch( IOException e )
				{
					e.printStackTrace();
					Log.e( "CopyWorldFiles", "Copying failed: " + filename );
				}
			}
		}//next file
		return;
	}
*/
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
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		/*
		if(netWorker != null)
			netWorker.cancel( true );
		*/
	}

	@Override
	public void onClick( View arg0 )
	{
		if( arg0 == btnScanNow )
		{
			//Toast.makeText( this, CamActivity.class.getSimpleName() , Toast.LENGTH_SHORT ).show();
			launchArWorld();
			//launchUrlWorld( "http://goo.gl/wc57hW" );
			//launchUrlWorld( "http:olivierdiotte.servebeer.com:83/KaptarWorld/" );
		}
		else if( arg0 == btnConfiguration )
		{
			//netWorker = new NetworkWorker(this);
			//netWorker.execute( );
			Toast.makeText( this, "Config!!!", Toast.LENGTH_SHORT ).show();
		}
		else if( arg0 == btnExit )
		{
			goHome();
		}
		else
		{
			Log.e( "Button manager", "Unknown view" );
		}
	}
	
	private void goHome()
	{
		 Intent intent = new Intent(Intent.ACTION_MAIN);
		 intent.addCategory(Intent.CATEGORY_HOME);
		 startActivity(intent);
	}

	private void launchArWorld()
	{
		try
		{
			final Intent intent = new Intent(this, CamActivity.class);
			intent.putExtra( CamActivity.EXTRAS_KEY_ACTIVITY_TITLE_STRING, "Monde augmentée de KaptAR" );
			intent.putExtra( CamActivity.EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL, "KaptarWorld/index.html" );
			startActivity( intent );
		}
		catch( Exception e )
		{
			Toast.makeText( this, "CamActivity\nnot defined/accessible", Toast.LENGTH_SHORT ).show();
		}
		

	}
	
	private void launchUrlWorld( final String url )
	{
		final Intent architectIntent = new Intent( this, CamActivity.class );

		try
		{
			final String encodedUrl = URLEncoder.encode( url, "UTF-8" );
			architectIntent.putExtra( CamActivity.EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL, encodedUrl );
		}
		catch( UnsupportedEncodingException e )
		{
			Toast.makeText( this, "Unexpected Exception: " + e.getMessage(), Toast.LENGTH_LONG ).show();
			e.printStackTrace();
		}
		startActivity( architectIntent );
	}
	
	static public String getWikitudeLicence()
	{
		return "g/wc2VhPq7D2vvwjyMHZ4bQTmVti4P94ODYKiKCFLoYHCzcguAJ7hpqWpT9bEYNxfh3Syqxa8IoGFbyrjUhxeSNYiRh705C75TWS+3B/unVWLdaRInRD+glhJLa68sQsrryw3UsUMIpLOrZAaZpYepQByllXrCJSv9C5pMdX7/ZTYWx0ZWRfX2s5LwoElVDbdVqU3AzV3Vyl5J0h6pKfe35pKy5QRRypNJQfVGWQZnb/af3DOVeC/cBAf9hZfCi+fU57mDxtkhnLpj80jrQ6FLen55eVBEOHLUzDUiMnHNwrSCznKnEB6Kbz+NPVSlyTc3ULIugEsIHchxTtkoSWVW8g1ShTC/qbS3SHv5JCrmAGIB6TpWed1jN40wUGJ2mk97qtZx7XrksQ5ij7enz0RBm8CSKfuChNzv88ol4FEyRjw1RsTL2cjmSb/UACIkY67eFAiLkLJeG9vgLuiQ2l+RP/f8yj+vtAbNcl1a86XcgMzhG4kDYeZfzG7+xCjP3UBIE3LpgvONUHrJko5c33iWJf29iJa6TbTJX0VG+AwGzGSs3e67TGqVxAz18G1kDB+HBOmRjWj7tgm8xVQ7XyVSc1rHkUwwPtQIMXpzvCfZHmkRH/AaxXhljbkPVr2Wi3u1Rwl2FtCOLPBxW9QKq8++zFDGGLLQ1Hk2q6FfLrFSM=";
	}
}
