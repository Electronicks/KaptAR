package com.macrosoft.kaptar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadWorker extends AsyncTask< String, Integer, File >
{
	private Context c = null;
	private int size = Integer.MAX_VALUE;
	File file = null;
	public DownloadWorker(Context c, int size, File file)
	{
		this.c = c;
		this.size = size;
		this.file = file;
	}
	
	@Override
	protected File doInBackground( String... arg0 )
	{
		// TODO Auto-generated method stub
		return DownloadFromUrl( arg0[0] );
	}

	@Override
	protected void onCancelled( File result )
	{
		// TODO Auto-generated method stub
		super.onCancelled( );
	}

	@Override
	protected void onPostExecute( File result )
	{
		// TODO Auto-generated method stub
		super.onPostExecute( result );
		if( result != null && result.exists() )
		{
			Log.d("DownloadWorker","I haz downloaded");
		}
		else
		{
			Log.d("DownloadWorker","I haz failed");
		}
	}

	@Override
	protected void onPreExecute()
	{
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate( Integer... values )
	{
		// TODO Auto-generated method stub
		super.onProgressUpdate( values );
		Log.d("DownloadManager", "progress: " + values[0]);
	}

	public File DownloadFromUrl( String DownloadUrl )
	{
		try
		{


			URL url = new URL( DownloadUrl ); // you can write here any link

			long startTime = System.currentTimeMillis();
			Log.d( "DownloadManager", "download url:" + url );

			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();

			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream( is );

			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			if(	file.exists()) Log.d( "DownloadWorker", file.getName() + " already exists" );
			FileOutputStream fos = new FileOutputStream( file );
			ByteArrayBuffer baf = new ByteArrayBuffer( 5000 );
			int downloadbyte = 0;
			int lastprogress = 0;
			int bytesleft = size;
			while( ( downloadbyte = bis.read() ) != -1 )
			{
				if(baf.isFull())
				{
					fos.write( baf.toByteArray() );
					fos.flush();
					baf.clear();
				}
				baf.append( ( byte ) downloadbyte );
				bytesleft--;
				if((1 - bytesleft / size) * 100 > lastprogress)
				{
					lastprogress = (1 - bytesleft / size) * 100;
					publishProgress( lastprogress );
				}
			}

			/* Convert the Bytes read to a String. */
			fos.write( baf.toByteArray() );
			fos.flush();
			fos.close();
			Log.d( "DownloadManager", "download ready in" + ( ( System.currentTimeMillis() - startTime ) / 1000 )
					+ " sec" );
		}
		catch( IOException e )
		{
			Log.d( "DownloadManager", "Error: " + e );
			file = null;
		}
		return file;
	}
}
