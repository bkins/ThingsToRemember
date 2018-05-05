package com.snikpoh.bhopkins.thingstoremember.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.snikpoh.bhopkins.thingstoremember.Database.ThingsToRememberDbAdapter;
import com.snikpoh.bhopkins.thingstoremember.R;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener
{
	
	static final int DATE_DIALOG_ID = 999;
	
	static final String DATE_DIVIDER = "/";
	
	private final static String ACTIVITY_NAME = MainActivity.class.getSimpleName();
	
	private AdView               adView;
	private FloatingActionButton fabBack;
	private EditText             etEntry;
	private TextView             tvEntryDate;
	private AutoCompleteTextView actMood;
	private DatePicker           dpEntryDate;
	
	private ThingsToRememberDbAdapter ttrDb;
	
	private String journalName;
	private String journalId;
	
	private int year;
	private int month;
	private int day;
	
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener()
	{
		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view,
		                      int selectedYear,
		                      int selectedMonth,
		                      int selectedDay)
		{
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			
			// set selected date into textview
			tvEntryDate.setText(getFormattedDate(year, month, day));
			
			// set selected date into datepicker also
			dpEntryDate.init(year, month, day, null);
		}
	};
	
	@RequiresApi(api = Build.VERSION_CODES.N) //allows Calendar.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entry);
		
		loadAdView(R.id.adViewEntry);
		
		initializeControls();
		initializeDatabase();
		
		setExtraDataFromMain();
		setTitleText();
	}
	
	private void setExtraDataFromMain()
	{
		journalName = getIntent().getStringExtra(MainActivity.JOURNAL_NAME);
		journalId = getIntent().getStringExtra(MainActivity.JOURNAL_ID);
	}
	
	private void setTitleText()
	{
		if (journalName == null || journalId == null)
		{
			this.setTitle("Make an entry:");
		}
		else
		{
			this.setTitle("Make an entry for " + journalName + "(" + journalId + "):");
		}
	}
	
	@RequiresApi(api = Build.VERSION_CODES.N) //allows Calendar.getInstance();
	private void initializeControls()
	{
		fabBack = findViewById(R.id.fabBack);
		fabBack.setOnClickListener(this);
		
		etEntry = findViewById(R.id.etEntry);
		actMood = findViewById(R.id.actMood);
		
		tvEntryDate = findViewById(R.id.etEntryDate);
		dpEntryDate = findViewById(R.id.dpEntryDate);
		
		setCurrentDateOnView();
		addListenerOnButton();

//		fabAddJournal  = findViewById(R.id.fabAddJournal);
//		ivJournalImage = findViewById(R.id.ivJournalImage);
//		lvJournals     = findViewById(R.id.lvJournals);


//		fabPreferences.setOnClickListener(this);
//		fabAddJournal.setOnClickListener(this);
//		ivJournalImage.setOnClickListener(this);
		//lvJournals.setOnClickListener(this);
		
	}
	
	// display current date
	@RequiresApi(api = Build.VERSION_CODES.N) //allows Calendar.getInstance();
	public void setCurrentDateOnView()
	{
		final Calendar currentCalendar = Calendar.getInstance();
		
		year = currentCalendar.get(Calendar.YEAR);
		month = currentCalendar.get(Calendar.MONTH);
		day = currentCalendar.get(Calendar.DAY_OF_MONTH);
		
		// set current date into textview
		tvEntryDate.setText(getFormattedDate(year, month, day));
		
		// set current date into datepicker
		dpEntryDate.init(year, month, day, null);
	}
	
	public void addListenerOnButton()
	{
		tvEntryDate.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showDialog(DATE_DIALOG_ID);
			}
		});
	}
	
	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
			case DATE_DIALOG_ID:
				// set date picker as current date
				return new DatePickerDialog(this,
				                            datePickerListener,
				                            year,
				                            month,
				                            day);
		}
		return null;
	}
	
	private String getFormattedDate(int year, int month, int day)
	{
		return new StringBuilder().append(month + 1)
		                          .append(DATE_DIVIDER)
		                          .append(day).append(DATE_DIVIDER)
		                          .append(year)
		                          .append(" ").toString();
	}
	
	private void loadAdView(int adViewID)
	{
		adView = findViewById(adViewID);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}
	
	private boolean tryToWriteEntryToDb()
	{
		try
		{
			ttrDb.insertIntoEntry(etEntry.getText().toString(),
			                      tvEntryDate.getText().toString(),
			                      actMood.getText().toString(),
			                      journalId);
			
			Log.d(ACTIVITY_NAME,
			      journalName +
					      ": DB Entry of: " +
					      etEntry.getText().toString() + "; " +
					      tvEntryDate.getText().toString() + "; " +
					      actMood.getText().toString() + "; " +
					      "JournalID = " + journalId);
			return true;
		}
		catch (Exception ex)
		{
			Log.e(ACTIVITY_NAME, ex.getMessage());
			return false;
		}
	}
	
	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		
		switch (id)
		{
			case R.id.fabBack:
				
				if (tryToWriteEntryToDb())
				{
					startAnActivity(MainActivity.class);
				}
				
				break;
			
			case R.id.fabAddAnother:
				
				if (tryToWriteEntryToDb())
				{
					resetControls();
				}
				
				break;
			
			default:
				
				Log.d(ACTIVITY_NAME, "default case in onClick (" + id + ")");
				
				break;
		}
	}
	
	private void resetControls()
	{
		etEntry.setText("");
		tvEntryDate.setText("");
		actMood.setText("");
	}
	
	private void startAnActivity(Class activityClass)
	{
		Log.d(ACTIVITY_NAME, "Opening activity: " + activityClass.getSimpleName());
		
		Intent i = new Intent(this, activityClass);
		startActivity(i);
	}
	
	private void initializeDatabase()
	{
		ttrDb = new ThingsToRememberDbAdapter(this);
		ttrDb.open();
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
	}
	
	
	@Override
	protected void onStart()
	{
		super.onStart();
		Log.d(ACTIVITY_NAME, "onStart was called");
	}
	
	//endregion
	
}
