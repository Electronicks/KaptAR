package com.macrosoft.kaptar;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class ModelParser
{

	public ModelParser()
	{
		// TODO Auto-generated constructor stub
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
			if( name.equals( "productId" ) )
			{
				readProductID( parser );
			}
			else
			{
				skip( parser );
			}
		}
		parser.require( XmlPullParser.END_TAG, ns, "version" );
		return;
	}

	
	private void readImage( XmlPullParser parser ) throws XmlPullParserException, IOException
	{
		String url = null;
		parser.require( XmlPullParser.START_TAG, ns, "image" );
		String tag = parser.getName();
		if( tag.equals( "image" ) )
		{
				url = parser.getAttributeValue( null, "url" );
		}
		parser.next();
		parser.require( XmlPullParser.END_TAG, ns, "image" );
		Log.d( "readImage", "url=" + url );
		return;
	}
	
	private void readLink( XmlPullParser parser ) throws XmlPullParserException, IOException
	{
		String url = null;
		parser.require( XmlPullParser.START_TAG, ns, "link" );
		String tag = parser.getName();
		if( tag.equals( "link" ) )
		{
				url = parser.getAttributeValue( null, "url" );
		}
		parser.next();
		parser.require( XmlPullParser.END_TAG, ns, "link" );
		Log.d( "readImage", "url=" + url );
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
				id = Integer.parseInt( parser.getAttributeValue( null, "id" ) );
				type = parser.getAttributeValue( null, "type" );
				posX = Float.parseFloat( parser.getAttributeValue( null, "relXpos" ) );
				posY = Float.parseFloat( parser.getAttributeValue( null, "relYpos" ) );
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

	// Processes link tags in the feed.
	private void readProductID( XmlPullParser parser ) throws IOException, XmlPullParserException
	{
		int id = 0;
		parser.require( XmlPullParser.START_TAG, ns, "productId" );
		String tag = parser.getName();
		if( tag.equals( "productId" ) )
		{
				id = Integer.parseInt( parser.getAttributeValue( null, "value" ) );
		}
		Log.d( "readProductID", "ID="+id );
		
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
		parser.require( XmlPullParser.END_TAG, ns, "productId" );
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
				url = parser.getAttributeValue( null, "url" );
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
