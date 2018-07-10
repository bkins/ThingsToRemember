package com.snikpoh.bhopkins.thingstoremember.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.snikpoh.bhopkins.thingstoremember.Database.Entry;
import com.snikpoh.bhopkins.thingstoremember.Database.ThingsToRememberDbAdapter;
import com.snikpoh.bhopkins.thingstoremember.R;

import static com.snikpoh.bhopkins.thingstoremember.Activities.MainActivity.JOURNAL_ID;
import static com.snikpoh.bhopkins.thingstoremember.Activities.MainActivity.JOURNAL_NAME;

public class ExploreEntriesActivity extends AppCompatActivity
{
	
	private final static String ACTIVITY_NAME = MainActivity.class.getSimpleName();
	
	private AdView adView;
	
	private TextView tvEntryDate;
	private TextView tvEntry;
	private TextView tvMood;
	
	private ListView lvEntries;
	
	private FloatingActionButton fabAddEntry;
	
	private ThingsToRememberDbAdapter ttrDb;
	private SimpleCursorAdapter       cursorAdapter;
	
	final String[] from = {Entry.getColumnEntryDate(), Entry.getColumnDescription(), Entry.getColumnMoodId()};
	final int[]    to   = {R.id.tvEntryDate, R.id.tvEntry, R.id.tvMood};
	
	String journalName;
	String journalId;
	
	public static final String ENTRY_DATE        = "ENTRY_DATE";
	public static final String ENTRY_DESCRIPTION = "ENTRY_DESCRIPTION";
	public static final String ENTRY_MOOD        = "ENTRY_MOOD";
	public static final String ENTRY_ID          = "ENTRY_ID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_explore);
		
		loadAdView(R.id.adViewExplore);
		
		initializeControls();
		setExtraDataFromActivity();
		setTitleText();
		initializeDatabase();
		displayListView();
		listViewOnClick();
	}
	
	private void setTitleText()
	{
		if (journalName == null || journalId == null)
		{
			this.setTitle("Make an entry:"); ///should be able to get here
		}
		else
		{
			this.setTitle("Entries for " + journalName);
		}
	}
	
	private void listViewOnClick()
	{
		lvEntries.setOnItemClickListener(new  AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				onClickOfEntryItem(position, EntryActivity.class); //ExploreEntriesActivity.class); //
			}
		});
	}
	
	private void onClickOfEntryItem(int position, Class activityClass)
	{
		Cursor entryCursor = (Cursor) lvEntries.getItemAtPosition(position);
		
		String entryDescription = getStringValueFromCursor(entryCursor, Entry.getColumnDescription());
		String entryDate        = getStringValueFromCursor(entryCursor, Entry.getColumnEntryDate());
		String entryMood        = getStringValueFromCursor(entryCursor, Entry.getColumnMoodId());
		String entryId          = getStringValueFromCursor(entryCursor, Entry.getColumnId());
		
		Toast.makeText(getBaseContext(), entryDate + " clicked() ", Toast.LENGTH_SHORT).show();
		
		startAnActivity(activityClass, entryDate, entryDescription, entryMood, entryId);
	}
	
	private String getStringValueFromCursor(Cursor cursor, String columnName)
	{
		return 	cursor.getString(cursor.getColumnIndexOrThrow(columnName));
	}
	
	private void loadAdView(int adViewID)
	{
		adView = findViewById(adViewID);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}
	
	private void initializeControls()
	{
		lvEntries = findViewById(R.id.lvEntries);
		//lvEntries.setOnClickListener(this);
		
		fabAddEntry = findViewById(R.id.fabAddEntry);
		addListenerOnButton();
	}
	
	public void addListenerOnButton()
	{
		fabAddEntry.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startNewActivity();
			}
		});
	}
	
	private void displayListView()
	{
		SimpleCursorAdapter entries = getEntryCursorAdapter();
		
		if (entries != null)
		{
			lvEntries.setAdapter(entries);
		}
		
//		listViewOnClick();
//		listViewOnLongClick();
	}
	
	private void setExtraDataFromActivity()
	{
		journalName = getIntent().getStringExtra(JOURNAL_NAME);
		journalId   = getIntent().getStringExtra(JOURNAL_ID);
	}
	
	private void initializeDatabase()
	{
		ttrDb = new ThingsToRememberDbAdapter(this);
		ttrDb.open();
	}
	
	private SimpleCursorAdapter getEntryCursorAdapter()
	{
		Cursor entryData = ttrDb.fetchEntriesByJournalId(Integer.parseInt(journalId));
		
		if (entryData != null)
		{
			cursorAdapter = new SimpleCursorAdapter(this,
			                                        R.layout.entry_row,
			                                        entryData,
			                                        from,
			                                        to,
			                                        0);
		}
		
		return cursorAdapter;
	}
	
	
	private void startAnActivity(Class activityClass)
	{
		Log.d(ACTIVITY_NAME, "Opening activity: " + activityClass.getSimpleName());
		
		Intent i = new Intent(this, activityClass);
		startActivity(i);
	}
	
	private void startNewActivity()
	{
		startAnActivity(EntryActivity.class, "", "", "", "");
	}
	private void startAnActivity(Class activityClass, String entryDate, String entryDescription, String entryMood, String entryId)
	{
		Log.d(ACTIVITY_NAME, "Opening activity: " + activityClass.getSimpleName());
		
		Intent i = new Intent(this, activityClass);
		i.putExtra(ENTRY_DATE, entryDate);
		i.putExtra(ENTRY_DESCRIPTION, entryDescription);
		i.putExtra(ENTRY_MOOD, entryMood);
		i.putExtra(JOURNAL_ID, journalId);
		i.putExtra(JOURNAL_NAME, journalName);
		i.putExtra(ENTRY_ID, entryId);
		
		startActivity(i);
	}
	
	private void gotoPreviousActivity()
	{
		startAnActivity(MainActivity.class);
	}
	
	private void gotoNextActivty()
	{
		startAnActivity(EntryActivity.class);
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		
		gotoPreviousActivity();
	}
	
	//region Activity States
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		
		super.onRestoreInstanceState(savedInstanceState);

//		String myText = savedInstanceState.getString("MYTEXT");
//		if (myText != null){
//			myEditText.setText(myText);
//		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState)
	{
		super.onSaveInstanceState(outState, outPersistentState);

//		String myText = myEditText.getText().toString();
//		outState.putString("MYTEXT", myText);
	
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		Log.d(ACTIVITY_NAME, "onPauses was called");
//		if(isFinishing(){
//			Log.d(ACTIVITY_NAME, "the activity is being finished ");
//		}
		
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
	}
	
	//endregion
	
}
