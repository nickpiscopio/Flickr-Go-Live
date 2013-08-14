package com.example.Accessors;

import java.io.InputStream;
import java.net.URL;

import com.example.flickrgolive.MainActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.ImageView;

/**
 * Retrieves the images from a URL and places them in the ImageView.
 * 
 * @author Nick Piscopio
 *
 */
public class ImageAccessor extends AsyncTask<String, Void, Bitmap>
{
	private ImageView imageView;

	private GridLayout gridLayout;

	/**
	 * Constructor that takes in the ImageView and GridLayout.
	 * 
	 * @param imageView		The ImageView to set with an image.
	 * @param gridLayout	The GridLayout that will hold the image.
	 */
	public ImageAccessor(ImageView imageView, GridLayout gridLayout)
	{
		this.imageView = imageView;

		this.gridLayout = gridLayout;
	}

	/**
	 * Creates a Bitmap of the image from the URL being sent in
	 * 
	 * @param params	The URL String.
	 */
	@Override
	protected Bitmap doInBackground(String... params)
	{
		// //Create an Image from the link provided.
		Bitmap image = null;

		try
		{
			// Get the URL from the params
			InputStream inputStream = new URL(params[0]).openStream();
			image = BitmapFactory.decodeStream(inputStream);
		}
		catch(Exception exception)
		{
			Log.e(MainActivity.TAG, exception.toString());
		}

		return image;
	}

	/**
	 * Called after the image has been retrieved from the URL.
	 * 
	 * @param result	The image that is being set in the ImageView.
	 */
	@Override
	protected void onPostExecute(Bitmap result)
	{
		super.onPostExecute(result);

		imageView.setImageBitmap(result);

		gridLayout.addView(imageView);
	}
}
