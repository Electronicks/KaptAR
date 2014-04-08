package com.macrosoft.kaptar;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

public class AugmentedProduct
{
	public ArrayList<String> overlayNames = null;

	private int id = -1;
	private String trackername = null;

	private String soundname = null;
	private boolean isSoundVideo = false;


	public String getSoundname()
	{
		return soundname;
	}

	public void setSoundname( String soundname )
	{
		this.soundname = soundname;
	}

	public boolean isSoundVideo()
	{
		return isSoundVideo;
	}

	public void setSoundVideo( boolean isSoundMedia )
	{
		this.isSoundVideo = isSoundMedia;
	}

	public AugmentedProduct(XmlPullParser parserAtProductTag)
	{
		overlayNames = new ArrayList< String >(5);
		
		String attribute = parserAtProductTag.getAttributeValue( null, "id" );
		id = attribute != null ? Integer.parseInt( attribute ) : -1;
		
		attribute = parserAtProductTag.getAttributeValue( null, "name" );
		trackername = attribute != null ? attribute : "unnamed";
	}

	public int getId()
	{
		return id;
	}


	public String getTrackername()
	{
		return trackername;
	}
	
}
