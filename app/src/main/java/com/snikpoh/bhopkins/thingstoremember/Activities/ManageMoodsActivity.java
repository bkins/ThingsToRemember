package com.snikpoh.bhopkins.thingstoremember.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.snikpoh.bhopkins.thingstoremember.Database.Journal;
import com.snikpoh.bhopkins.thingstoremember.Database.Mood;
import com.snikpoh.bhopkins.thingstoremember.Database.ThingsToRememberDbAdapter;
import com.snikpoh.bhopkins.thingstoremember.R;

import static com.snikpoh.bhopkins.thingstoremember.Activities.MainActivity.JOURNAL_ID;
import static com.snikpoh.bhopkins.thingstoremember.Activities.MainActivity.JOURNAL_NAME;
import static com.snikpoh.bhopkins.thingstoremember.Activities.MainActivity.JOURNAL_TYPE;

public class ManageMoodsActivity extends AppCompatActivity implements View.OnClickListener
{
	private final static String ACTIVITY_NAME = ManageMoodsActivity.class.getSimpleName();
	
	EditText    etMoodDescription;
	EditText    etMoodEmoji;
	ImageButton ibSaveMood;
	
	private ThingsToRememberDbAdapter ttrDb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_moods);
		
		this.setTitle("Manage Moods");
		
		initializeControls();
		initializeDatabase();
	}
	
	private void initializeControls()
	{
		etMoodDescription = findViewById(R.id.etMoodDescription);
		etMoodEmoji       = findViewById(R.id.etMoodEmoji);
		ibSaveMood        = findViewById(R.id.ibSaveMood);
		
		ibSaveMood.setOnClickListener(this);
	}
	
	private void initializeDatabase()
	{
		ttrDb = new ThingsToRememberDbAdapter(this);
		ttrDb.open();
	}
	
	private void saveMood(String description)
	{
		if(ttrDb.doesMoodExist(description))
		{
			//Update current mood.  Assume defining new emoji
			Cursor mood = ttrDb.fetchMoodByDescription(description);
			ttrDb.updateMood(getStringValueFromCursor(mood, Mood.getColumnId()),
			                 description,
			                 etMoodEmoji.getText().toString());
		}
		else
		{
			//Insert new mood.
			ttrDb.createMood(description,
			                 etMoodEmoji.getText().toString());
		}
		
	}
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.ibSaveMood:
				
				String moodDescription = etMoodDescription.getText().toString();
				
				if (! moodComplete())
				{
					Toast.makeText(this, "You must enter a Description and an Emoji.", Toast.LENGTH_LONG);
					
					break;
				}
				
				saveMood(moodDescription);
				
				break;
				
			default:
			
		}
	}
	
	private boolean moodComplete()
	{
		return  ! etMoodEmoji.getText().toString().isEmpty() &&
				! etMoodDescription.getText().toString().isEmpty();
	}
	
	private String getStringValueFromCursor(Cursor cursor, String columnName)
	{
		return 	cursor.getString(cursor.getColumnIndexOrThrow(columnName));
	}
	
	private void startAnActivity(Class activityClass)
	{
		Log.d(ACTIVITY_NAME, "Opening activity: " + activityClass.getSimpleName());
		
		Intent i = new Intent(this, activityClass);
		startActivity(i);
	}
	
	private void startAnActivity(Class activityClass, String journalName)
	{
		Log.d(ACTIVITY_NAME, "Opening activity: " + activityClass.getSimpleName());
		
		Intent i = new Intent(this, activityClass);
		i.putExtra(Journal.getColumnName(), journalName);
		
		startActivity(i);
	}
	
	private void startAnActivity(Class activityClass, String journalName, String journalId, String journalType)
	{
		Log.d(ACTIVITY_NAME, "Opening activity: " + activityClass.getSimpleName());
		
		Intent i = new Intent(this, activityClass);
		i.putExtra(JOURNAL_NAME, journalName);
		i.putExtra(JOURNAL_ID, journalId);
		i.putExtra(JOURNAL_TYPE, journalType);
		
		startActivity(i);
	}
	
}
