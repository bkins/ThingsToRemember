package com.snikpoh.bhopkins.thingstoremember.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.AndroidException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.snikpoh.bhopkins.thingstoremember.Database.Entry;
import com.snikpoh.bhopkins.thingstoremember.Database.Journal;
import com.snikpoh.bhopkins.thingstoremember.Database.ThingsToRememberDbAdapter;
import com.snikpoh.bhopkins.thingstoremember.R;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener
{
	private final static String ACTIVITY_NAME = MainActivity.class.getSimpleName();
	
	public static String JOURNAL_NAME = "JOURNAL_NAME";
	public static String JOURNAL_ID   = "JOURNAL_ID";
	public static String JOURNAL_TYPE = "JOURNAL_TYPE";
	
	final String[] from = {Journal.getJournalColumnName(), Journal.getJournalColumnType()};
	final int[]    to   = {R.id.tvJournalName, R.id.tvJournalType};
	
	ListView lvJournals;
	
	private AdView adView;
	
	private FloatingActionButton fabPreferences;
	private FloatingActionButton fabAddJournal;
	private ImageView            ivJournalImage;
	
	private ThingsToRememberDbAdapter ttrDb;
	private SimpleCursorAdapter       cursorAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
		MobileAds.initialize(this,
		                     getString(R.string.adMod_App_Id)); //"ca-app-pub-1259969651432104~4839144088"); //
		
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
				onClickOfJournalItem(position, EntryActivity.class);
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
		
		String journalName = cursor.getString(cursor.getColumnIndexOrThrow(Journal.getJournalColumnName()));
		String journalId   = cursor.getString(cursor.getColumnIndexOrThrow(Journal.getJournalColumnId()));
		String journalType = cursor.getString(cursor.getColumnIndexOrThrow(Journal.getJournalColumnType()));
		
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

//			case R.id.ivJournalImage:
//			case R.id.lvJournals:
//			case R.id.tvJournalName:
//			case R.id.tvJournalType:
//
//				startAnActivity(EntryActivity.class);
//
//				break;
//
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
		i.putExtra(Journal.getJournalColumnName(), journalName);
		
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
	
	protected void onButtonClick(View view)
	{
		Log.d(ACTIVITY_NAME, "onButtonClick()");

//		Toast.makeText(getBaseContext(), "Button has been clicked", Toast.LENGTH_SHORT).show();
//
//		if (tv !=null) {
//			tv.setTextSize(40);
//			tv.setTextColor(getResources().getColor(R.color.myCustomColor));
//		}
		
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
