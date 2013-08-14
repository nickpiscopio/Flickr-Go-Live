package com.example.flickrgolive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * The Main screen of the application.
 * 
 * @author Nick Piscopio
 *
 */
public class MainActivity extends Activity
{
	public static final String TAG = "FLICKR GO LIVE";
	
	private Button jsonButton;
	private Button xmlButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		jsonButton = (Button)findViewById(R.id.json_button);
		xmlButton = (Button)findViewById(R.id.xml_button);
		
		jsonButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startFlickrActivity(getResources().getString(R.string.json_title), false);				
			}
		});
		
		xmlButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startFlickrActivity(getResources().getString(R.string.xml_title),true);				
			}
		});
	}
	
	/**
	 * Starts the FlickrActivity.
	 * 
	 * @param title						The title to set the ActionBar to when FlickrActivity is shown.
	 * @param retrieveFlickrWithXML		Boolean value to retrieve Flickr data from XML or from JSON.
	 */
	private void startFlickrActivity(String title, boolean retrieveFlickrWithXML)
	{
		Intent intent = new Intent(MainActivity.this, FlickrFeedActivity.class);
		intent.putExtra(FlickrFeedActivity.EXTRA_TITLE, title);
		intent.putExtra(FlickrFeedActivity.EXTRA_FEED, retrieveFlickrWithXML);
		
		startActivity(intent);
	}
}
