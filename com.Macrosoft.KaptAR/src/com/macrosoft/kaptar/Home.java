package com.macrosoft.kaptar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Home extends Activity implements OnClickListener
{
	Button btnScanNow = null;
	Button btnConfiguration = null;
	Button btnExit = null;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_home );
		
		btnScanNow = (Button) findViewById( R.id.btnScanNow );
		btnConfiguration = (Button) findViewById( R.id.btnSettings );
		btnExit = (Button) findViewById( R.id.btnExit );
		
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
	public void onClick( View arg0 )
	{
		if(arg0 == btnScanNow)
		{
			Toast.makeText( this, "Scan!!!", Toast.LENGTH_SHORT ).show();
		}
		else if(arg0 ==  btnScanNow)
		{
			Toast.makeText( this, "Config!!!", Toast.LENGTH_SHORT ).show();
		}
		else if(arg0 == btnExit)
		{
			Toast.makeText( this, "Exit!!!", Toast.LENGTH_SHORT ).show();
		}
		else
		{
			Log.e( "Button manager", "Unknown view" );
		}
	}

}
