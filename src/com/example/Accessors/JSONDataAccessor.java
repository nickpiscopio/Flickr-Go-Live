package com.example.Accessors;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.flickrgolive.FlickrFeedActivity;
import com.example.flickrgolive.MainActivity;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Retrieves the JSON data from the URL provided.
 * 
 * @author Nick Piscopio
 *
 */
public class JSONDataAccessor extends AsyncTask<String, Void, JSONObject>
{
	private FlickrFeedActivity flickerFeedActivity;

	/**
	 * Constructor that takes in the Activity that called this class.
	 * 
	 * @param flickrFeedActivity	A reference to FlickrFeedActivity to send the data back that was received.
	 */
	public JSONDataAccessor(FlickrFeedActivity flickrFeedActivity)
	{
		this.flickerFeedActivity = flickrFeedActivity;
	}

	/**
	 * Does an HTTPPost to get the JSON object from the URL provided.
	 * 
	 * @param params	The URL to grab the JSON Flickr Feed
	 */
	@Override
	protected JSONObject doInBackground(String... params)
	{  		
		InputStream inputStream = null;
		
		JSONObject jsonObject = null;
		
    	String jsonString = "";

    	try
    	{
    		HttpClient httpclient = new DefaultHttpClient();
    		
    		//Take the URL and create a HttpPost
    		HttpPost httppost = new HttpPost(params[0]);
    		
	        HttpResponse response = httpclient.execute(httppost);
	        
	        HttpEntity entity = response.getEntity();
	        
	        inputStream = entity.getContent();
    	}
    	catch(Exception e)
    	{
    		Log.e(MainActivity.TAG, "Connection Error " + e.toString());
    	}

    	
    	//Convert Response
    	try
    	{
	        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
	        String stringBuilder = "";
	        String line = null;
	        while ((line = reader.readLine()) != null) 
	        {
	        	stringBuilder += line;
	        }

	        inputStream.close();
	 
	        if(!stringBuilder.toString().equalsIgnoreCase("null\t"))
	        {
	        	jsonString = stringBuilder.toString();
	        }
    	}
    	catch(Exception e)
    	{
    		Log.e(MainActivity.TAG, "Converting Error " + e.toString());
    	}

		//Parse the String to JSON Object
		try
		{
			jsonObject = new JSONObject(jsonString.substring(jsonString.indexOf("{"), jsonString.lastIndexOf("}") + 1));
		}
		catch(JSONException e)
		{
			Log.e(MainActivity.TAG, "Error parsing data " + e.toString());
		}

		return jsonObject;
	}
	
	/**
	 * Called when the background task has completed.
	 * 
	 * @param jsonObject	The data in a JSON format.
	 */
	@Override
    protected void onPostExecute(JSONObject jsonObject) 
    {
		super.onPostExecute(jsonObject);
		
		flickerFeedActivity.handleData(jsonObject);
    }
}
