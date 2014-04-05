package com.macrosoft.kaptar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class ModelParser
{
	private Context c = null;
	private int id = 0;
	private String tracker = null;

	public ModelParser(Context c)
	{
		this.c = c;
	}

	private static final String	ns	= null;

	public void parse( InputStream in ) throws XmlPullParserException, IOException
	{
		try
		{
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature( XmlPullParser.FEATURE_PROCESS_NAMESPACES, false );
			parser.setInput( in, null );
			parser.nextTag();
			readVersion( parser );
			return;
		}
		finally
		{
			in.close();
		}
	}

	private void readVersion( XmlPullParser parser ) throws XmlPullParserException, IOException
	{
		parser.require( XmlPullParser.START_TAG, ns, "version" );
		while( parser.next() != XmlPullParser.END_TAG )
		{
			if( parser.getEventType() != XmlPullParser.START_TAG )
			{
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if( name.equals( "product" ) )
			{
				readProduct( parser );
			}
			else
			{
				skip( parser );
			}
		}
		parser.require( XmlPullParser.END_TAG, ns, "version" );
		return;
	}
	
	// Processes link tags in the feed.
	private void readProduct( XmlPullParser parser ) throws IOException, XmlPullParserException
	{
		parser.require( XmlPullParser.START_TAG, ns, "product" );
		String tag = parser.getName();
		if( tag.equals( "product" ) )
		{
			String attribute = parser.getAttributeValue( null, "id" );
			id = attribute != null ? Integer.parseInt( attribute ) : -1;
			attribute = parser.getAttributeValue( null, "name" );
			tracker = attribute != null ? attribute : "unnamed";
		}
		Log.d( "readProduct", "ID="+id + " & name=" + tracker );
		
		while( parser.next() != XmlPullParser.END_TAG )
		{
			if( parser.getEventType() != XmlPullParser.START_TAG )
			{
				continue;
			}
			String name = parser.getName();
			if( name.equals( "media" ) )
			{
				readMedia( parser );
			}
			else
			{
				skip( parser );
			}
		}
		parser.require( XmlPullParser.END_TAG, ns, "product" );
		return;
	}
	
	// Processes title tags in the feed.
	private void readMedia( XmlPullParser parser ) throws IOException, XmlPullParserException
	{
		float posX = 0;
		float posY = 0;
		String type = null;
		int id = 0;
		parser.require( XmlPullParser.START_TAG, ns, "media" );
		String tag = parser.getName();
		if( tag.equals( "media" ) )
		{
			String attribute = parser.getAttributeValue( null, "id" );
			id = attribute != null ? Integer.parseInt( attribute ) : -1;
			
			attribute = parser.getAttributeValue( null, "type" ); 
			type = attribute != null ? attribute : "";
			
			attribute = parser.getAttributeValue( null, "relXpos" );
			posX = attribute != null ? Float.parseFloat( attribute ) : 0;
			
			attribute = parser.getAttributeValue( null, "relYpos" );
			posY = attribute != null ? Float.parseFloat( attribute ) : 0;
		}
		Log.d( "readMedia", "ID=" + id + " & pos=(" + posX + "," + posY + ") & type=" + type );
		
		while( parser.next() != XmlPullParser.END_TAG )
		{
			if( parser.getEventType() != XmlPullParser.START_TAG )
			{
				continue;
			}
			String name = parser.getName();
			if( name.equals( "image" ) )
			{
				readImage( parser );
			}
			else if( name.equals( "text" ) )
			{
				readText( parser );
			}
			else if( name.equals( "link" ) )
			{
				readLink( parser );
			}
			else
			{
				skip( parser );
			}
		}
		parser.require( XmlPullParser.END_TAG, ns, "media" );
		return;
	}
	
	private void readImage( XmlPullParser parser ) throws XmlPullParserException, IOException
	{
		String url = null;
		int size = Integer.MAX_VALUE;
		parser.require( XmlPullParser.START_TAG, ns, "image" );
		String tag = parser.getName();
		if( tag.equals( "image" ) )
		{
			String attribute = parser.getAttributeValue( null, "url" ); 
			url = attribute != null ? attribute : "";
			
			attribute = parser.getAttributeValue( null, "filesize" );
			size = attribute != null ? Integer.parseInt( attribute ) : Integer.MAX_VALUE;
		}
		parser.next();
		parser.require( XmlPullParser.END_TAG, ns, "image" );
		Log.d( "readImage", "url=" + url );
		String filename = Home.KaptarWorldPath + "augmentation/" + tracker + "/" + url.substring( url.lastIndexOf( "/" )+1, url.length() );
		File file = new File(filename);
		if(file.exists() == false)
		{
			file.getParentFile().mkdirs();
			DownloadWorker dw = new DownloadWorker( c, size, file);
			dw.execute( NetworkWorker.Server + ":" + NetworkWorker.serverport + url.substring( 2 ) );
		}
		
		return;
	}
	
	private void readLink( XmlPullParser parser ) throws XmlPullParserException, IOException
	{
		String url = null;
		parser.require( XmlPullParser.START_TAG, ns, "link" );
		String tag = parser.getName();
		if( tag.equals( "link" ) )
		{
			String attribute = parser.getAttributeValue( null, "url" ); 
			url = attribute != null ? attribute : "";
		}
		parser.next();
		parser.require( XmlPullParser.END_TAG, ns, "link" );
		Log.d( "readImage", "url=" + url );
		return;
	}

	// For the tags title and summary, extracts their text values.
	private void readText( XmlPullParser parser ) throws IOException, XmlPullParserException
	{
		String url = null;
		parser.require( XmlPullParser.START_TAG, ns, "text" );
		String tag = parser.getName();
		if( tag.equals( "text" ) )
		{
			String attribute = parser.getAttributeValue( null, "url" ); 
			url = attribute != null ? attribute : "";
		}
		parser.next();
		parser.require( XmlPullParser.END_TAG, ns, "text" );
		Log.d( "readText", "url=" + url );
		return;
	}

	private void skip( XmlPullParser parser ) throws XmlPullParserException, IOException
	{
		if( parser.getEventType() != XmlPullParser.START_TAG )
		{
			throw new IllegalStateException();
		}
		int depth = 1;
		while( depth != 0 )
		{
			switch( parser.next() )
			{
			case XmlPullParser.END_TAG :
				depth--;
				break;
			case XmlPullParser.START_TAG :
				depth++;
				break;
			}
		}
	}

}
