package com.snikpoh.bhopkins.thingstoremember.Examples;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.snikpoh.bhopkins.thingstoremember.R;

public class ContentProvider extends AppCompatActivity implements View.OnClickListener
{
	TextView txtDisplay;
	Button   btnFetch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.example_activity_content_provider);
		
		txtDisplay = (TextView) findViewById(R.id.textView);
		btnFetch = (Button) findViewById(R.id.button2);
		
		btnFetch.setOnClickListener(this);
		
		txtDisplay.setText("Not Fetched");
	}
	
	@Override
	public void onClick(View v)
	{
		String[] projection = new String[]
				                      {
						                      MediaStore.Audio.AudioColumns.ALBUM,
						                      MediaStore.Audio.AudioColumns.TITLE
				                      };
		Uri    contentUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
		Cursor cursor     = getContentResolver().query(contentUri, projection, null, null, null);
		
		int albumIdx = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM);
		int titleIdx = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE);
		
		String[] results = new String[cursor.getCount()];
		cursor.moveToFirst();
		
		while (cursor.moveToNext())
		{
			String title = cursor.getString(titleIdx);
			String album = cursor.getString(albumIdx);
			results[cursor.getPosition()] = title + "( " + album + ")";
		}
		
		cursor.close();
		
		txtDisplay.setText("Got results with rows = " + results.length);
	}
}
