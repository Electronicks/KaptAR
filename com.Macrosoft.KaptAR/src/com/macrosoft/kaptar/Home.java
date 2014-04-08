package com.macrosoft.kaptar;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Home extends Activity implements OnClickListener, WorldReadyEventIF, OnLongClickListener
{
	private Button	btnScanNow			= null;
	private Button	btnConfiguration	= null;
	private Button	btnExit				= null; 
	private ImageView logo				= null;
	private boolean isTesting			= false;
	
	//private AsyncTask<Void,Void,Void> netWorker = null;
	String arWorld = "";
	NetworkWorker worldbuilder = null;
	
	//à mettre dans shared prefs
	public static final String defaultServer = "http://olivierdiotte.servebeer.com";
	public static final int defaultport = 83;
	public static final int version = 2;
	
	
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_home );

		btnScanNow = ( Button ) findViewById( R.id.btnScanNow );
		btnConfiguration = ( Button ) findViewById( R.id.btnSettings );
		btnExit = ( Button ) findViewById( R.id.btnExit );
		logo = (ImageView) findViewById( R.id.KaptAR_logo );

		btnScanNow.setOnClickListener( this );
		btnConfiguration.setOnClickListener( this );
		btnExit.setOnClickListener( this );
		logo.setOnLongClickListener( this );
		
	}
	
	private String getUrlToWorld()
	{
		return defaultServer + ":" + defaultport + "/KaptarWorld/xmlprince.xml";
		//TODO: change xml
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
		super.onStart();
		if(arWorld.isEmpty())
		{
			btnScanNow.setEnabled( false );
			worldbuilder = new NetworkWorker(this, this);
			worldbuilder.execute(getUrlToWorld());
		}
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		if(worldbuilder != null)
			worldbuilder.cancel( true );
	}

	@Override
	public void onClick( View arg0 )
	{
		if( arg0 == btnScanNow )
		{
			//Toast.makeText( this, CamActivity.class.getSimpleName() , Toast.LENGTH_SHORT ).show();
			//launchArWorld();
			launchUrlWorld( PreferenceManager.getDefaultSharedPreferences( this ).getString( "URL AR", "default :(" ) );
		}
		else if( arg0 == btnConfiguration )
		{
			//netWorker = new NetworkWorker(this);
			//netWorker.execute( );
			//Toast.makeText( this, "Config!!!", Toast.LENGTH_SHORT ).show();
			final Intent intent = new Intent(this, QuickPrefsActivity.class);
			startActivity( intent );
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
			intent.putExtra( CamActivity.EXTRAS_KEY_ACTIVITY_TITLE_STRING, "Monde augmenté de KaptAR" );
			intent.putExtra( CamActivity.EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL, "KaptarWorld/index.html" );
			intent.putExtra( CamActivity.EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_JS, arWorld );
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
			architectIntent.putExtra( CamActivity.EXTRAS_KEY_ACTIVITY_TITLE_STRING, "Monde augmenté de KaptAR" );
			architectIntent.putExtra( CamActivity.EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_URL, encodedUrl );
			architectIntent.putExtra( CamActivity.EXTRAS_KEY_ACTIVITY_ARCHITECT_WORLD_JS, arWorld );
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

	@Override
	public void OnWorldReady( String world )
	{
		this.arWorld = world;
		btnScanNow.setEnabled( true );
	}

	@Override
	public boolean onLongClick( View arg0 )
	{
		if(arg0 == logo)
		{
			new AlertDialog.Builder(this)
		    .setTitle("Tests unitaires")
		    .setMessage("Voulez vous lancer les tests unitaires?")
//		    .setView(input)
		    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		            Home.this.LaunchTests();
		        }
		    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		            // Do nothing.
		        }
		    }).show();
		}
		return true;
	}

	protected void LaunchTests()
	{
		if(worldbuilder != null) worldbuilder.cancel( true );
		try
		{
			//Toast.makeText( this, "I am launching tests", Toast.LENGTH_SHORT ).show();
			NetworkWorker nw = new NetworkWorker( this, new WorldReadyEventIF()
			{
				
				@Override
				public void OnWorldReady( String world )
				{
					InputStream resultFile = null;
					InputStream worldIS = null;
					try
					{
						resultFile = getAssets().open( "tests/js/KaptarWorldDefinition.js" );
						worldIS = new ByteArrayInputStream(world.getBytes("UTF-8"));
						int result1;
						int result2;
						do
						{
							result1 = resultFile.read();
							result2 = worldIS.read();
						}
						while(result1!=-1 && result2!=-1 && result1==result2);
						if(result1==-1 && result2==-1)
						{
							Log.d("w00t!", "test réussi!");
						}
					}
					catch( IOException e )
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally
					{
						try
						{
							if(resultFile != null) resultFile.close();
							if(worldIS != null) worldIS.close();
						}
						catch( IOException e )
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			} );
			nw.setTestingXmlSource( getAssets().open( "tests/xml/xmlprince.xml" ) );
			nw.execute( "http://olivierdiotte.servebeer.com:83/KaptarWorld/" );
		}
		catch( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
