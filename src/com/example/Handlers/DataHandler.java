package com.example.Handlers;

import org.json.JSONObject;

import com.example.SimpleXML.Feed;

/**
 * Handles the data that comes back from the DataAccessors.
 */
public interface DataHandler
{	
	/**
	 * Handles the data that comes in from the XMLDataAccessor
	 * 
	 * @param Feed	The XML data in a class structure to parse.
	 */
	public void handleData(Feed feed);
	
	/**
	 * Handles the data that comes in from the JSONDataAccessor
	 * 
	 * @param jsonObject	The JSON data to parse.
	 */
	public void handleData(JSONObject jsonObject);
}
