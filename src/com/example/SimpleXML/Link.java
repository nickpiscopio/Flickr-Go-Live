package com.example.SimpleXML;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Element(name="link")
public class Link
{
	@Attribute(name="type")
	public String type;
	
	@Attribute(name="href")
	public String href;
}
