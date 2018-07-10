package com.snikpoh.bhopkins.thingstoremember.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.snikpoh.bhopkins.thingstoremember.Database.Entry;
import com.snikpoh.bhopkins.thingstoremember.Database.EntrySql;
import com.snikpoh.bhopkins.thingstoremember.Database.ThingsToRememberDbAdapter;
import com.snikpoh.bhopkins.thingstoremember.R;

import java.util.ArrayList;

import static com.snikpoh.bhopkins.thingstoremember.Activities.MainActivity.JOURNAL_ID;
import static com.snikpoh.bhopkins.thingstoremember.Activities.MainActivity.JOURNAL_NAME;
import static com.snikpoh.bhopkins.thingstoremember.Activities.MainActivity.JOURNAL_TYPE;

public class ExploreEntriesActivity extends AppCompatActivity implements View.OnClickListener
{
	
	private final static String ACTIVITY_NAME = MainActivity.class.getSimpleName();
	
	private AdView adView;
	
	private TextView tvEntryDate;
	private TextView tvEntry;
	private TextView tvMood;
	private TextView tvEntryId;
	
	private ListView lvEntries;
	
	private ThingsToRememberDbAdapter ttrDb;
	private SimpleCursorAdapter       cursorAdapter;
	
	final String[] from = {EntrySql.getColumnEntryDate(), EntrySql.getColumnDescription(), EntrySql.getColumnMoodId(), EntrySql.getColumnId()};
	final int[]    to   = {R.id.tvEntryDate, R.id.tvEntry, R.id.tvMood, R.id.tvEntryId};
	
	String journalName;
	String journalId;
	String journalType;
	
	public static final String  ENTRY_DATE          = "ENTRY_DATE";
	public static final String  ENTRY_ID            = "ENTRY_ID";
	public static final String  ENTRY_DESCRIPTION   = "ENTRY_DESCRIPTION";
	public static final String  ENTRY_MOOD          = "ENTRY_MOOD";
	public static final String  CALLING_ACTIVITY    = "CALLING_ACTIVITY";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_explore);
		
		loadAdView(R.id.adViewExplore);
		
		initializeControls();
		setExtraDataFromActivity();
		initializeDatabase();
		displayListView();
	}
	
	private void loadAdView(int adViewID)
	{
		adView = findViewById(adViewID);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}
	
	private void initializeControls()
	{
		lvEntries   = findViewById(R.id.lvEntries);
		tvEntry     = findViewById(R.id.tvEntry);
		tvEntryId   = findViewById(R.id.tvEntryId);
		
		//lvEntries.setOnClickListener(this);
	}
	
	private void displayListView()
	{
		SimpleCursorAdapter entries = getEntryCursorAdapter();
		//ArrayAdapter<Entry> entries = getEntryArrayAdapter();
		
		if (entries != null)
		{
			lvEntries.setAdapter(entries);
		}
		
		listViewOnClick();
//		listViewOnClick();
//		listViewOnLongClick();
	}
	
	private void setExtraDataFromActivity()
	{
		journalName = getIntent().getStringExtra(JOURNAL_NAME);
		journalId   = getIntent().getStringExtra(JOURNAL_ID);
		journalType = getIntent().getStringExtra(JOURNAL_TYPE);
	}
	
	private void initializeDatabase()
	{
		ttrDb = new ThingsToRememberDbAdapter(this);
		ttrDb.open();
	}
	
	
	private ArrayAdapter getEntryArrayAdapter()
	{
		ArrayList<Entry> entries = ttrDb.getEntriesByJournalId(Integer.parseInt(journalId));
		
		ArrayAdapter<Entry> entryArrayAdapter = new ArrayAdapter<>(this,
		                                                           R.layout.entry_row,
		                                                           entries);
		return entryArrayAdapter;
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
	
	private void listViewOnClick()
	{
		lvEntries.setOnItemClickListener(new  AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				onClickOfEntryItem(position, EntryActivity.class);
			}
		});
	}
	
	
	private void onClickOfEntryItem(int position, Class activityClass)
	{
		Cursor cursor = (Cursor) lvEntries.getItemAtPosition(position);
		
		String entryDate        = cursor.getString(cursor.getColumnIndexOrThrow(EntrySql.getColumnEntryDate()));
		String entryId          = cursor.getString(cursor.getColumnIndexOrThrow(EntrySql.getColumnId()));
		String entryDescription = cursor.getString(cursor.getColumnIndexOrThrow(EntrySql.getColumnDescription()));
		String entryMood        = cursor.getString(cursor.getColumnIndexOrThrow(EntrySql.getColumnMoodId()));
		
		Toast.makeText(getBaseContext(), entryDate + " clicked() ", Toast.LENGTH_SHORT).show();
		
		startAnActivity(activityClass, entryDate, entryId, entryDescription, entryMood);
	}
	
	
	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		
		switch (id)
		{
			case R.id.fabAdd:
				
				startAnActivity(EntryActivity.class,
				                tvEntryDate.getText().toString(),
				                tvEntryId.getText().toString(),
				                tvEntry.getText().toString(),
				                tvMood.getText().toString());
				
				break;
			
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
		
		finish();
	}
	
	private void startAnActivity(Class activityClass,
	                             String entryDate,
	                             String entryId,
	                             String entryDescription,
	                             String entryMood)
	{
		Log.d(ACTIVITY_NAME, "Opening activity: " + activityClass.getSimpleName());
		
		Intent i = new Intent(this, activityClass);
		i.putExtra(ENTRY_DATE, entryDate);
		i.putExtra(ENTRY_ID, entryId);
		i.putExtra(ENTRY_DESCRIPTION, entryDescription);
		i.putExtra(ENTRY_MOOD, entryMood);
		i.putExtra(JOURNAL_ID, journalId);
		i.putExtra(JOURNAL_NAME, journalName);
		i.putExtra(JOURNAL_TYPE, journalType);
		
		startActivity(i);
		
		finish();
	}
	
	private void gotoPreviousActivity()
	{
//		startAnActivity(EntryActivity.class);
		startAnActivity(MainActivity.class);
	}
	
	private void gotoNextActivty()
	{
		//TODO: pass in Entry fields to be edited in the EntryActivity
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
