package com.macrosoft.kaptar;

import java.io.ByteArrayInputStream;
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
	Context c = null;
	String str = null;
	String Server = "http://olivierdiotte.servebeer.com:83/kaptar";
	int version = 1;
	String username = "phones";
	String password = "Macrosoft";
	

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
				//HttpUriRequest request = new HttpGet("http://olivierdiotte.servebeer.com:83/kaptar?modelId=1&ver=1");
				HttpUriRequest request = new HttpGet(Server + "?ver=" + version + "&modelId=" + modelid);
				String credentials = username + ":" + password;  
				String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
				request.addHeader("Authorization", "Basic " + base64EncodedCredentials);

				HttpClient httpclient = new DefaultHttpClient();  
				HttpEntity entity = httpclient.execute(request).getEntity();  
				str = EntityUtils.toString(entity, "UTF-8");
				//Log.d( "httpresponse", str );
				ModelParser parser = new ModelParser();
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
		Toast.makeText( c, "Successfully finished", Toast.LENGTH_SHORT ).show();
	}

}
