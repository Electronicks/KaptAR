package com.macrosoft.kaptar;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class NetworkWorker extends AsyncTask< Void, Void, Void >
{
	private Context c = null;
	private String str = null;
	
	//� mettre dans shared prefs
	public static final String Server = "http://olivierdiotte.servebeer.com";
	public static final int serverport = 83;
	public static final int version = 2;
	public static final String username = "phones";
	public static final String password = "Macrosoft";
	

	public NetworkWorker(Context context)
	{
		super();
		c = context;
	}

	@Override
	protected void onPreExecute()
	{
		// TODO Auto-generated method stub
		super.onPreExecute();
		Toast.makeText( c, "Launching update", Toast.LENGTH_SHORT ).show();
	}

	@Override
	protected Void doInBackground( Void... params )
	{
		try
		{
			for(int modelid = 1 ; modelid <= 4 ; modelid++)
			{
				HttpUriRequest request = new HttpGet(
						Server + ":" + serverport + "/kaptar?ver=" + version + "&modelId=" + modelid);
				String credentials = username + ":" + password;  
				String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
				request.addHeader("Authorization", "Basic " + base64EncodedCredentials);

				HttpClient httpclient = new DefaultHttpClient();  
				HttpEntity entity = httpclient.execute(request).getEntity();  
				str = EntityUtils.toString(entity, "UTF-8");
				//Log.d( "httpresponse", str );
				ModelParser parser = new ModelParser(c);
				InputStream is = new ByteArrayInputStream(str.getBytes());
				parser.parse(is);
			}
		}
		catch( UnknownHostException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.cancel( true );
		}
		catch( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.cancel( true );
		} // Get from prefs
		catch( XmlPullParserException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d( "NetworkBackground", "I am a fail" );
		}
		return null;
	}

	@Override
	protected void onCancelled(Void result)
	{
		// TODO Auto-generated method stub
		super.onCancelled();
		Toast.makeText( c, "Update failed", Toast.LENGTH_SHORT ).show();
	}

	@Override
	protected void onProgressUpdate( Void... values )
	{
		// TODO Auto-generated method stub
		super.onProgressUpdate( values );
	}

	@Override
	protected void onPostExecute( Void result )
	{
		// TODO Auto-generated method stub
		super.onPostExecute( result );
		
		try
		{
			File f = new File(c.getExternalFilesDir( null ).getCanonicalPath() + "/KaptarWorld/augmentation/" + "TrakerName" + "/CowInBoots.gif");
			if(f.exists())
				Toast.makeText( c, "downloaded finished", Toast.LENGTH_SHORT ).show();
			else
				Toast.makeText( c, "not downloaded finished", Toast.LENGTH_SHORT ).show();
		}
		catch( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
