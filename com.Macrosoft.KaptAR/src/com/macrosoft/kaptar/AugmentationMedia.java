package com.macrosoft.kaptar;

import org.xmlpull.v1.XmlPullParser;

public class AugmentationMedia
{
	private float posX = 0;
	private float posY = 0;
	private String type = "";
	private int id = 0;

	public AugmentationMedia(XmlPullParser parserAtMediaTag)
	{
		String attribute = parserAtMediaTag.getAttributeValue( null, "id" );
		id = attribute != null ? Integer.parseInt( attribute ) : -1;
		
		attribute = parserAtMediaTag.getAttributeValue( null, "type" ); 
		type = attribute != null ? attribute : "";
		
		attribute = parserAtMediaTag.getAttributeValue( null, "relXpos" );
		posX = attribute != null ? Float.parseFloat( attribute ) : 0;
		
		attribute = parserAtMediaTag.getAttributeValue( null, "relYpos" );
		posY = attribute != null ? Float.parseFloat( attribute ) : 0;
	}

	public float getPosX()
	{
		return posX;
	}

	public float getPosY()
	{
		return posY;
	}

	public String getType()
	{
		return type;
	}

	public int getId()
	{
		return id;
	}

}
