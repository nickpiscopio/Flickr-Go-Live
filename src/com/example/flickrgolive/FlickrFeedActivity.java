package com.example.flickrgolive;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.Accessors.ImageAccessor;
import com.example.Accessors.JSONDataAccessor;
import com.example.Accessors.XMLDataAccessor;
import com.example.Handlers.DataHandler;
import com.example.ShakeDetectLibrary.ShakeDetectActivity;
import com.example.ShakeDetectLibrary.ShakeDetectActivityListener;
import com.example.SimpleXML.Entry;
import com.example.SimpleXML.Feed;
import com.example.SimpleXML.Link;

/**
 * Displays the Flickr Feed from either JSON or XML.
 * 
 * @author Nick Piscopio
 *
 */
public class FlickrFeedActivity extends Activity implements DataHandler
{
	public static final String EXTRA_TITLE = "com.example.flickrgolive.FlickrFeedActivity.title";
	public static final String EXTRA_FEED = "com.example.flickrgolive.FlickrFeedActivity.protocol";

	private String title = "";

	private boolean retrieveFlickrWithXML = false;

	private GridLayout flickrFeedLayout;

	private ShakeDetectActivity shakeDetectActivity;
	
	private ActionBar actionBar;

	//Needed to tell Eclipse that the Build Version is correct.
	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.flickr_feed_activity);

		int screenWidth = 0;

		flickrFeedLayout = (GridLayout) findViewById(R.id.flickr_feed_layout);

		// Only sets the number of columns in the flickrFeedLayout if the SDK version is greater than 13
		// Else it will use the default from the flickr_feed_activity
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
		{
			float scalefactor = getResources().getDisplayMetrics().density * 75;

			Display display = getWindowManager().getDefaultDisplay();

			Point size = new Point();

			display.getSize(size);

			screenWidth = size.x;

			int columns = (int) ((float) screenWidth / (float) scalefactor);

			flickrFeedLayout.setColumnCount(columns);
		}

		//Get the title to set the ActionBar
		title = getIntent().getStringExtra(EXTRA_TITLE);
		
		//Sets the ActionBar's title
		setActionBarTitle(title);

		//Creates a shake detection from the open source Shake Detection Library.
		shakeDetectActivity = new ShakeDetectActivity(this);
		shakeDetectActivity.addListener(new ShakeDetectActivityListener()
		{
			@Override
			public void shakeDetected()
			{
				loadData();
			}
		});
		
		loadData();
	}

	/**
	 * Handles the data that comes in from the XMLDataAccessor
	 * 
	 * @param Feed	The XML data in a class structure to parse.
	 */
	@Override
	public void handleData(Feed feed)
	{
		for(Entry entry : feed.entries)
		{
			for(Link link : entry.links)
			{
				if(link.type.contains("image"))
				{
					getImageFromFlickr(link.href);
				}
			}
		}
		
		//Sets the ActionBar's title
		setActionBarTitle(title);
	}

	/**
	 * Handles the data that comes in from the JSONDataAccessor
	 * 
	 * @param jsonObject	The JSON data to parse.
	 */
	@Override
	public void handleData(JSONObject jsonObject)
	{
		try
		{
			JSONArray jsonArray = jsonObject.getJSONArray("items");

			for(int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject childJSONObject = jsonArray.getJSONObject(i);
				JSONObject mediaObject = childJSONObject.getJSONObject("media");

				String link = mediaObject.getString("m");

				getImageFromFlickr(link);
			}
		}
		catch(JSONException exception)
		{
			Log.e(MainActivity.TAG, "Could not find link: " + exception.toString());
		}
		
		//Sets the ActionBar's title
		setActionBarTitle(title);
	}

	/**
	 * Gets an image from Flickr.
	 * 
	 * @param link	The Link to get the image.
	 */
	private void getImageFromFlickr(String link)
	{
		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.flickr_image_size), getResources().getDimensionPixelSize(R.dimen.flickr_image_size)));
		imageView.setPadding(getResources().getDimensionPixelSize(R.dimen.flickr_image_padding), getResources().getDimensionPixelSize(R.dimen.flickr_image_padding),
								getResources().getDimensionPixelSize(R.dimen.flickr_image_padding), getResources().getDimensionPixelSize(R.dimen.flickr_image_padding));

		ImageAccessor imageAccessor = new ImageAccessor(imageView, flickrFeedLayout);
		imageAccessor.execute(link);
	}

	/**
	 * Called to save the data when rotating the device.
	 * 
	 * @param outState	The Bundle of the current state.
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		outState.putString(EXTRA_TITLE, title);
	}

	/**
	 * Called when the activity resumes.
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		
		shakeDetectActivity.onResume();
	}

	/**
	 * Called when the activity pauses.
	 */
	@Override
	protected void onPause()
	{
		shakeDetectActivity.onPause();
		
		super.onPause();
	}
	
	/**
	 * Loads data from Flickr.
	 */
	private void loadData()
	{	    		
		flickrFeedLayout.removeAllViews();
		
		setActionBarTitle(getResources().getString(R.string.loading) + " " + title);
		
		retrieveFlickrWithXML = getIntent().getBooleanExtra(EXTRA_FEED, false);

		if(retrieveFlickrWithXML)
		{
			XMLDataAccessor xmlDataAccessor = new XMLDataAccessor(this);
			xmlDataAccessor.execute(getResources().getString(R.string.flickr_xml));
		}
		else
		{
			JSONDataAccessor jsonDataAccessor = new JSONDataAccessor(this);
			jsonDataAccessor.execute(getResources().getString(R.string.flickr_json));
		}
	}
	
	/**
	 * Sets the ActionBar title.
	 * 
	 * @param title		The title to set the ActionBar to.
	 */
	private void setActionBarTitle(String title)
	{
		//Sets the ActionBar title if the SDK Version is above 11
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			actionBar = getActionBar();
			actionBar.setSubtitle(title);
		}
	}
}
