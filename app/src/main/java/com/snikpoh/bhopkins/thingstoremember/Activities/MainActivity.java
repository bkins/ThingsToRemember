package com.snikpoh.bhopkins.thingstoremember.Activities;

// region TO-DO list
/*
* Requirement : 1)  Need to delete by Journal ID instead of Name
* Requirement : 2)  Implement emojis to be associated with Moods
* Requirement :           1)  Display emojis in Mood Spinner
* Requirement :           2)  Create new Activity for managing Moods
* Requirement : 3)  In the ExploreEntriesActivity sort Entries by date in DESC order
* Requirement : 5)  Implement Service to find entries that happened a year ago and send notification
* Requirement : 6)  Do not allow adding of "empty" entries
*
* Look/Feel : 1) Implement swipe to delete Journals (and entries?)
* Look/Feel : 2) In the ExploreEntriesActivity the Entry and Mood fields are crowded
*
 *
* Consider : 3) Implementing user managed list of Journal Types.  Like how will be doing Moods
* Consider :        1)  Implement in the ManageJournalsActivity.
* Consider : 4) Instead of creating a new activity for managing Moods, create a way to add them from the entry screen
* Consider : 5) Figure out/google better way to implement DB schema
* Consider : 6) A way to have full screen ads that won't be too distracting/annoying
*
* Optional : 1) Implement ActionBar back buttons in all activities
*
* *Future to-dos are to be made Requirements after the release of v1.0
*
 * Future : 1)    Implement custom images on journals (Main Activity)
 *
* */
//endregion

//region TODOne List
/*
 * Requirement : 4)  Figure why back button is not simply going back an activity, and then exiting when it gets to the main activity (DONE: 8/18/2018)
 * Requirement : 6)  Do not allow adding of "empty" journals (Verified this is DONE: 8/18/2018)
 * Requirement : 7)  Reordering flow: Main -> Explore, click '+' -> Entry (Verified this is DONE: 8/18/2018)
 * Requirement :         1) An "Add" button instead of having the entry added when the <- is pressed (Verified this is DONE: 8/18/2018)
 * Look/Feel : 3) Move Preference button up to the title bar (Looks better: 8/18/2018)
 *
 *
 */
//endregion

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.snikpoh.bhopkins.thingstoremember.BuildConfig;
import com.snikpoh.bhopkins.thingstoremember.Database.Journal;
import com.snikpoh.bhopkins.thingstoremember.Database.ThingsToRememberDbAdapter;
import com.snikpoh.bhopkins.thingstoremember.R;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener
{
	private final static String ACTIVITY_NAME = MainActivity.class.getSimpleName();
	
	public static String JOURNAL_NAME = "JOURNAL_NAME";
	public static String JOURNAL_ID   = "JOURNAL_ID";
	public static String JOURNAL_TYPE = "JOURNAL_TYPE";
	
	final String[] from = {Journal.getColumnName(), Journal.getColumnType()};
	final int[]    to   = {R.id.tvJournalName, R.id.tvJournalType};
	
	ListView lvJournals;
	
	private AdView adView;
	
	private FloatingActionButton fabPreferences;
	private FloatingActionButton fabAddJournal;
	private ImageView            ivJournalImage;
	
	private ThingsToRememberDbAdapter ttrDb;
	private SimpleCursorAdapter       cursorAdapter;
	private boolean adMobKey;
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		closeApp();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Force Orientation to Portrait
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		initializeControls();
		
		loadAdView(R.id.adViewMain);
		
		initializeDatabase();
	}
	
	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs)
	{
		return super.onCreateView(name, context, attrs);
	}
	
	private void loadTestData()
	{
		ttrDb.deleteAllJournals();
		
		ttrDb.insertJournal("A Test Journal", "Professional");
		ttrDb.insertJournal("A Test Journal 1", "Personal");
		ttrDb.insertJournal("A Test Journal 2", "Personal");
		ttrDb.insertJournal("A Test Journal 3", "Personal");
		ttrDb.insertJournal("A Test Journal 4", "Personal");
		ttrDb.insertJournal("A Test Journal 5", "Personal");
		ttrDb.insertJournal("A Test Journal 6", "Personal");
		ttrDb.insertJournal("A Test Journal 7", "Personal");
		ttrDb.insertJournal("A Test Journal 8", "Personal");
		ttrDb.insertJournal("A Test Journal 9", "Personal");
		ttrDb.insertJournal("A Test Journal 10", "Personal");
		ttrDb.insertJournal("A Test Journal 11", "Personal");
		
	}
	
	private void initializeControls()
	{
		fabPreferences = findViewById(R.id.fabPreferences);
		fabAddJournal = findViewById(R.id.fabAddJournal);
		ivJournalImage = findViewById(R.id.ivJournalImage);
		lvJournals = findViewById(R.id.lvJournals);
		
		fabPreferences.setOnClickListener(this);
		fabAddJournal.setOnClickListener(this);
		lvJournals.setOnLongClickListener(this);
		
	}
	
	private void initializeDatabase()
	{
		ttrDb = new ThingsToRememberDbAdapter(this);
		ttrDb.open();
	}
	
	private void loadAdView(int adViewID)
	{
		//How to create the BuildConfig.AdMobId: https://medium.com/code-better/hiding-api-keys-from-your-android-repository-b23f5598b906
		
		MobileAds.initialize(this,
		                     BuildConfig.AdMobId);

        adView = findViewById(adViewID);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}
	
	private void listViewOnClick()
	{
		lvJournals.setOnItemClickListener(new  AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				onClickOfJournalItem(position, ExploreEntriesActivity.class); // EntryActivity.class); //
			}
		});
	}
	
	private void listViewOnLongClick()
	{
		lvJournals.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
			{
				onClickOfJournalItem(position, ManageJournalsActivity.class);
				return true;
			}
		});
	}
	
	private void displayListView()
	{
		lvJournals.setAdapter(getJournalCursorAdapter());
		
		listViewOnClick();
		listViewOnLongClick();
	}
	
	private SimpleCursorAdapter getJournalCursorAdapter()
	{
		Cursor journalData = ttrDb.fetchAllJournals();
		
		cursorAdapter = new SimpleCursorAdapter(this,
		                                        R.layout.journal_row,
		                                        journalData,
		                                        from,
		                                        to,
		                                        0);
		return cursorAdapter;
	}
	
	private void onClickOfJournalItem(int position, Class activityClass)
	{
		Cursor cursor = (Cursor) lvJournals.getItemAtPosition(position);
		
		String journalName = cursor.getString(cursor.getColumnIndexOrThrow(Journal.getColumnName()));
		String journalId   = cursor.getString(cursor.getColumnIndexOrThrow(Journal.getColumnId()));
		String journalType = cursor.getString(cursor.getColumnIndexOrThrow(Journal.getColumnType()));
		
		Toast.makeText(getBaseContext(), journalName + " clicked() ", Toast.LENGTH_SHORT).show();
		
		startAnActivity(activityClass, journalName, journalId, journalType);
	}
	
	@Override
	public void onClick(View view)
	{
		int id = view.getId();
		
		switch (id)
		{
			case R.id.fabPreferences:
				
				startAnActivity(AppPreferencesActivity.class);
				
				break;
			
			case R.id.fabAddJournal:
				
				startAnActivity(ManageJournalsActivity.class);
				
				break;
			
			case R.id.lvJournals:
			default:
				
				Log.d(ACTIVITY_NAME, "default case in onClick (" + id + ")");
				
				break;
		}
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
	
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN) //for finishAffinity
	private void closeApp()
	{
		this.finishAffinity();
	}
	
	protected void onButtonClick(View view)
	{
		Log.d(ACTIVITY_NAME, "onButtonClick()");
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		
	}
	
	//region Activity States
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState)
	{
		super.onSaveInstanceState(outState, outPersistentState);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		Log.d(ACTIVITY_NAME, "onPauses was called");
		adView.pause();
	}
	
	@Override
	protected void onRestart()
	{
		super.onRestart();
		Log.d(ACTIVITY_NAME, "onRestart was called");
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		Log.d(ACTIVITY_NAME, "onResume was called");
		
		adView.resume();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.d(ACTIVITY_NAME, "onDestroy was called");
		
		adView.destroy();
		
		ttrDb.closeDb();
	}
	
	
	@Override
	protected void onStart()
	{
		super.onStart();
		Log.d(ACTIVITY_NAME, "onStart was called");
		
		displayListView();
	}
	
	@Override
	public boolean onLongClick(View v)
	{
		return false;
	}
	
	@Override
	public void onPointerCaptureChanged(boolean hasCapture)
	{
	
	}
	
	//endregion
	
}
