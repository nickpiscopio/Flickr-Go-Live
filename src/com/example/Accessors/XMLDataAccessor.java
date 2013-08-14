package com.example.Accessors;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.os.AsyncTask;
import android.util.Log;

import com.example.SimpleXML.Feed;
import com.example.flickrgolive.FlickrFeedActivity;
import com.example.flickrgolive.MainActivity;

/**
 * Retrieves the XML data from the URL provided.
 * 
 * @author Nick Piscopio
 *
 */
public class XMLDataAccessor extends AsyncTask<String, Void, Feed>
{
	private FlickrFeedActivity flickerFeedActivity;

	/**
	 * Constructor that takes in the Activity that called this class.
	 * 
	 * @param flickrFeedActivity	A reference to FlickrFeedActivity to send the data back that was received.
	 */
	public XMLDataAccessor(FlickrFeedActivity flickrFeedActivity)
	{
		this.flickerFeedActivity = flickrFeedActivity;
	}

	/**
	 * Gets the XML data from the URL provided then serializes the data to 
	 * be in a class structure to be easily used later.
	 * 
	 * @param params	The URL to grab the XML Flickr Feed
	 */
	@Override
	protected Feed doInBackground(String... params)
	{
		URL url = null;
		
		URLConnection connection = null;
		
		Feed feed = null;
		
		try
		{
			url = new URL(params[0]);
			
			connection = url.openConnection();
			
			connection.connect();
		}
		catch(MalformedURLException exception)
		{
			exception.printStackTrace();
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}

		//Serializes the data into a class structure to use later.
		Serializer serializer = new Persister();

		try
		{
			feed = serializer.read(Feed.class, url.openStream(), false);
		}
		catch(Exception exception)
		{
			Log.e(MainActivity.TAG, "Serialization Exception: " + exception);
		}
		
		return feed;
	}
	
	/**
	 * Called when the background task has completed.
	 * 
	 * @param result	The data in a class structure.
	 */
	@Override
	protected void onPostExecute(Feed result)
	{
		super.onPostExecute(result);

		flickerFeedActivity.handleData(result);
	}
}
