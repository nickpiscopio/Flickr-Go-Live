package com.example.SimpleXML;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="feed")
public class Feed
{
	@ElementList(entry="entry", name="entry", inline=true)
	public List<Entry> entries;
}
