package com.macrosoft.kaptar;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class NetworkWorker extends AsyncTask< String, Integer, String >
{
	private Context c = null;
	
	public static final String defaultUsername = "phones";
	public static final String defaultPassword = "Macrosoft";
	private WorldReadyEventIF worldreadylistener = null;
	private InputStream testingIS = null;
	
	private NetworkWorker(Context context)
	{
		super();
		c = context;
	}
	
	public NetworkWorker(Context context, WorldReadyEventIF listener)
	{
		super();
		c = context;
		worldreadylistener = listener;
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		Toast.makeText( c, "Connexion en cours...", Toast.LENGTH_SHORT ).show();
	}

	protected void onPostExecute( String result )
	{
		super.onPostExecute( result );
		if(result != null)
		{
			Toast.makeText( c, "Chargement terminé!", Toast.LENGTH_SHORT ).show();
			if(worldreadylistener != null)
			{
				worldreadylistener.OnWorldReady( result );
			}	
		}
	}

	@Override
	protected String doInBackground( String... url )
	{
		String arWorld = null;
		try
		{
			String username = PreferenceManager.getDefaultSharedPreferences( c ).getString( "Nom d'utilisateur", "toto" );
			String password = PreferenceManager.getDefaultSharedPreferences( c ).getString( "Mot de passe", "toto" );
			HttpUriRequest request = new HttpGet(url[0]);
			String credentials = username + ":" + password;  
			String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
			request.addHeader("Authorization", "Basic " + base64EncodedCredentials);
			HttpClient httpclient = new DefaultHttpClient();  
			
			HttpEntity entity = httpclient.execute(request).getEntity();  
			
			String str = EntityUtils.toString(entity, "UTF-8");
			
			ModelParser parser = new ModelParser(c);
			InputStream is = testingIS == null ? new ByteArrayInputStream(str.getBytes()) : testingIS;
			arWorld = parser.parse(is);
		}
		catch( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d( "NetworkBackground", "I am a fail" );
			arWorld = null;
		}
		return arWorld;
	}
	
	public void setTestingXmlSource(InputStream is)
	{
		this.testingIS = is;
	}

}
