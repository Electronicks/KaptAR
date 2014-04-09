package com.macrosoft.kaptar;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class ModelParser
{
	private Context c = null;
	private String world = "";

	public ModelParser(Context c)
	{
		this.c = c;
	}

	private static final String	ns	= null;

	public String parse( InputStream in ) throws XmlPullParserException, IOException
	{
		try
		{
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature( XmlPullParser.FEATURE_PROCESS_NAMESPACES, false );
			parser.setInput( in, null );
			parser.nextTag();
			
			world = getWorldHeader();
			
			readVersion( parser );
			
			world += getWorldCloser();
			return world;
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
			
			if( name.equals( "product" ) )
			{
				readProduct( parser );
			}
			else
			{
				skip( parser );
			}
		}//next child
		parser.require( XmlPullParser.END_TAG, ns, "version" );
		return;
	}
	
	// Processes link tags in the feed.
	private void readProduct( XmlPullParser parser ) throws IOException, XmlPullParserException
	{
		parser.require( XmlPullParser.START_TAG, ns, "product" );
		
		String tag = parser.getName();
		AugmentedProduct product = null;
		
		if( tag.equals( "product" ) )
		{
			product = new AugmentedProduct( parser );
		}
		Log.d( "readProduct", "ID="+product.getId() + " & name=" + product.getTrackername() );

		while( parser.next() != XmlPullParser.END_TAG )
		{
			if( parser.getEventType() != XmlPullParser.START_TAG )
			{
				continue;
			}
			String name = parser.getName();
			if( name.equals( "media" ) )
			{
				readMedia(product, parser );
			}
			else
			{
				skip( parser );
			}
		}
		parser.require( XmlPullParser.END_TAG, ns, "product" );
		world += generateOverlaysAssignment(product);
		return;
	}
	


	// Processes title tags in the feed.
	private void readMedia(AugmentedProduct product, XmlPullParser parser ) throws IOException, XmlPullParserException
	{
		AugmentationMedia media = null;
		
		parser.require( XmlPullParser.START_TAG, ns, "media" );
		String tag = parser.getName();
		if( tag.equals( "media" ) )
		{
			media = new AugmentationMedia( parser );
		}
		Log.d( "readMedia", "ID=" + media.getId() + " & pos=(" + media.getPosX() + "," + media.getPosY() + ") & type=" + media.getType() );
		
		while( parser.next() != XmlPullParser.END_TAG )
		{
			if( parser.getEventType() != XmlPullParser.START_TAG )
			{
				continue;
			}
			String name = parser.getName();
			if( name.equals( "image" ) )
			{
				product.overlayNames.add( readImage( media, parser ) );
			}
			else if( name.equals( "text" ) )
			{
				product.overlayNames.add( readText(media, parser ) );
			}
			else if( name.equals( "link" ) )
			{
				//product.overlayNames.add( readLink(media, parser ) );
			}
			else if( name.equals( "audio" ) && product.isSoundVideo() == false )
			{
				product.setSoundname( readAudio(media, parser ) );
			}
			else if( name.equals( "video" ) && product.isSoundVideo() == false)
			{
				product.setSoundVideo( true );
				product.setSoundname( readVideo(media, parser ) );
				product.overlayNames.add( product.getSoundname() );
			}
			else
			{
				skip( parser );
			}
		}
		parser.require( XmlPullParser.END_TAG, ns, "media" );
		return;
	}
	
	private String readVideo( AugmentationMedia media, XmlPullParser parserAtVideoTag ) throws XmlPullParserException, IOException
	{
		String url = null;
		
		parserAtVideoTag.require( XmlPullParser.START_TAG, ns, "video" );
		String tag = parserAtVideoTag.getName();
		if( tag.equals( "video" ) )
		{
			String attribute = parserAtVideoTag.getAttributeValue( null, "url" ); 
			url = attribute != null ? attribute.substring( 3, attribute.length()) : "";
		}
		parserAtVideoTag.next();
		parserAtVideoTag.require( XmlPullParser.END_TAG, ns, "video" );

		String videoname = url.substring( url.lastIndexOf( '/' )+1, url.lastIndexOf( '.' )).replace( " ", "" ).replace("-","_");
		
		world += generateVideoOverlay( videoname, url, media.getId(), media.getPosX(), media.getPosY() );

		return videoname;
	}

	private String readAudio( AugmentationMedia media, XmlPullParser parserAtAudioTag ) throws XmlPullParserException, IOException
	{
		String url = null;
		
		parserAtAudioTag.require( XmlPullParser.START_TAG, ns, "audio" );
		String tag = parserAtAudioTag.getName();
		if( tag.equals( "audio" ) )
		{
			String attribute = parserAtAudioTag.getAttributeValue( null, "url" ); 
			url = attribute != null ? attribute.substring( 3, attribute.length()) : "";
		}
		parserAtAudioTag.next();
		parserAtAudioTag.require( XmlPullParser.END_TAG, ns, "audio" );

		String videoname = url.substring( url.lastIndexOf( '/' )+1, url.lastIndexOf( '.' )).replace( " ", "" ).replace("-","_");
		
		world += generateAudioOverlay( videoname, url);

		return videoname;
	}

	private String readImage( AugmentationMedia media, XmlPullParser parserAtImageTag ) throws XmlPullParserException, IOException
	{
		String url = null;
		
		parserAtImageTag.require( XmlPullParser.START_TAG, ns, "image" );
		String tag = parserAtImageTag.getName();
		if( tag.equals( "image" ) )
		{
			String attribute = parserAtImageTag.getAttributeValue( null, "url" ); 
			url = attribute != null ? attribute.substring( 3, attribute.length()) : "";
		}
		parserAtImageTag.next();
		parserAtImageTag.require( XmlPullParser.END_TAG, ns, "image" );

		String overlayname = url.substring( url.lastIndexOf( '/' )+1, url.lastIndexOf( '.' )).replace( " ", "" ).replace("-","_");
		
		world += generateImageOverlay( overlayname, url, media.getId(), media.getPosX(), media.getPosY() );

		return overlayname;
	}

	// For the tags title and summary, extracts their text values.
	private String readText( AugmentationMedia media, XmlPullParser parserAtLinkTag ) throws IOException, XmlPullParserException
	{
		String url = null;
		parserAtLinkTag.require( XmlPullParser.START_TAG, ns, "text" );
		String tag = parserAtLinkTag.getName();
		if( tag.equals( "text" ) )
		{
			String attribute = parserAtLinkTag.getAttributeValue( null, "url" ); 
			url = attribute != null ? attribute.substring( 3, attribute.length() ) : "";
		}
		parserAtLinkTag.next();
		parserAtLinkTag.require( XmlPullParser.END_TAG, ns, "text" );
		
		String overlayname = url.substring( url.lastIndexOf( '/' )+1, url.lastIndexOf( '.' )).replace( " ", "" ).replace("-","_");
		
		String text = downloadText(url, true);
		
		world += generateTextOverlay(overlayname, text, media.getPosX(), media.getPosY());
		return overlayname;
	}
	
	private String readLink( AugmentationMedia media, XmlPullParser parserAtLinkTag ) throws XmlPullParserException, IOException
	{
		String url = null;
		parserAtLinkTag.require( XmlPullParser.START_TAG, ns, "link" );
		String tag = parserAtLinkTag.getName();
		if( tag.equals( "link" ) )
		{
			String attribute = parserAtLinkTag.getAttributeValue( null, "url" ); 
			url = attribute != null ? attribute : "";
		}
		parserAtLinkTag.next();
		parserAtLinkTag.require( XmlPullParser.END_TAG, ns, "link" );
		Log.d( "readImage", "url=" + url );
		return "";
	}

	private String downloadText( String string, boolean b ) throws ClientProtocolException, IOException
	{
		String retval = "";
		if(b==false)
		{
			retval = downloadText( string );
		}
		else
		{
			InputStream fis = null;
			try
			{
				fis = c.getAssets().open( string );
				for(int ibyte = fis.read() ; ibyte != -1 ; ibyte = fis.read())
				{
					retval += (char) ibyte;
				}
			}
			catch( IOException e )
			{
				e.printStackTrace();
				retval = null;
			}
			finally
			{
				if(fis!=null) fis.close();
			}
		}
		return retval;
	}

	private String downloadText( String url ) throws ClientProtocolException, IOException
	{
		//TODO: change server to dynamic from prefs
		HttpUriRequest request = new HttpGet("http://olivierdiotte.servebeer.com:83"+url);  
		HttpClient httpclient = new DefaultHttpClient();  
		
		HttpEntity entity = httpclient.execute(request).getEntity();  
		
		String str = EntityUtils.toString(entity, "UTF-8");
		return str;
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
	
	private String getWorldHeader()
	{
		return "var World =\n"+
				"{\n"+
				"\tloaded: false,\n\n"+
				"\tlaunch: function launchFn()\n"+
				"\t{\n"+
					"\t\tAR.context.services.sensors = false;\n\n"+
					"\t\tthis.tracker = new AR.Tracker(\"wtc/KaptarTargets.wtc\");\n\n";
	}
	
	private String getWorldCloser()
	{
		return "\t}\n"+
				"};\n\n"+
				"World.launch();\n";
	}
	
	private String generateOverlaysAssignment( AugmentedProduct product )
	{
		String varname = product.getTrackername().replace( " ", "" ).replace("-","_");
		String s = "\t\tvar "+ varname +" = new AR.Trackable2DObject(this.tracker, \""+product.getTrackername()+"\", {\n"+
	            "\t\t\tdrawables: {\n"+
                "\t\t\t\tcam: [";
		for(int i = 0 ; i < product.overlayNames.size() ; i++)
        {
        	s += product.overlayNames.get( i );
        	if(i!= product.overlayNames.size()-1)
        		s += ", ";
        }//next overlay
		
		s += "]\n"+
            "\t\t\t},\n";
            
        if(product.getSoundname() != null)
		{
			s += "\t\t\tonEnterFieldOfVision: function onEnterFieldOfViewFn () {\n";
			
			if(product.isSoundVideo() == false)
			{
				s +=	"\t\t\t\tif("+product.getSoundname()+".state == AR.CONST.STATE.PAUSED){\n";
			}
			s += 		"\t\t\t\t\t"+product.getSoundname()+".resume();\n";
			
			if(product.isSoundVideo() ==  false)
			{
				s +=	"\t\t\t\t} else {\n"+
							"\t\t\t\t\t"+product.getSoundname()+".play(-1);\n"+
						"\t\t\t\t}\n";
			}
			s += "\t\t\t},\n"+
					"\t\t\tonExitFieldOfVision: function onExitFieldOfViewFn () {\n"+
						"\t\t\t\t"+product.getSoundname()+".pause();\n"+
					"\t\t\t}\n";
		}
		s+=		"\t\t});\n\n";
		
		return s;
	}
	
	private String generateImageOverlay(String overlayname, String url, int id, float posX, float posY)
	{
		return 				"\t\tvar image" + id + " = new AR.ImageResource(\"" + url + "\");\n"+
				"\t\tvar " + overlayname + " = new AR.ImageDrawable(image" + id + ", 1, {\n"+
				"\t\t\toffsetX: " + posX + ",\n"+
				"\t\t\toffsetY: " + posY + "\n"+
			"\t\t});\n\n";
	}
	
	private String generateVideoOverlay( String videoname, String url, int id, float posX, float posY )
	{
		return 				"\t\tvar " + videoname + " = new AR.VideoDrawable(\"" + url + "\", 1, {\n"+
				"\t\t\toffsetX: " + posX + ",\n"+
				"\t\t\toffsetY: " + posY + ",\n"+
				"\t\t\tonClick: function "+videoname+"OnClickFn () {\n"+
					"\t\t\t\t" + videoname + ".play(-1);\n"+
				"\t\t\t}\n"+
				"\t\t});\n\n";
	}
	
	private String generateAudioOverlay( String soundname, String url )
	{
		return "\t\tvar " + soundname + " = new AR.Sound(\"" + url + "\");\n\n";
	}
	private String generateTextOverlay(String overlayname, String texte, float posX, float posY)
	{
		return "\t\tvar "+overlayname+" = new AR.Label(\""+texte+"\", 0.3, {\n" +
        		"\t\t\tscale: 1,\n" +
                "\t\t\toffsetX: "+posX+",\n" +
                "\t\t\toffsetY:  "+posY+",\n" +
                "\t\t\tstyle: {textColor: \"#000000\",\n" +
                "\t\t\t\tbackgroundColor: \"#FFFFFF\"}\n"+
        "\t\t});\n\n";
	}
	/*        */

}
