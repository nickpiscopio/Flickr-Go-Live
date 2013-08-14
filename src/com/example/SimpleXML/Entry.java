package com.example.SimpleXML;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

@Element(name="entry")
public class Entry
{
	@ElementList(entry="link", name="link", inline=true)
	public List<Link> links;
}
