package com.snikpoh.bhopkins.thingstoremember.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.snikpoh.bhopkins.thingstoremember.Database.Entry;
import com.snikpoh.bhopkins.thingstoremember.Database.ThingsToRememberDbAdapter;
import com.snikpoh.bhopkins.thingstoremember.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static com.snikpoh.bhopkins.thingstoremember.Activities.ExploreEntriesActivity.ENTRY_DATE;
import static com.snikpoh.bhopkins.thingstoremember.Activities.ExploreEntriesActivity.ENTRY_DESCRIPTION;
import static com.snikpoh.bhopkins.thingstoremember.Activities.ExploreEntriesActivity.ENTRY_MOOD;
import static com.snikpoh.bhopkins.thingstoremember.Activities.MainActivity.JOURNAL_ID;
import static com.snikpoh.bhopkins.thingstoremember.Activities.MainActivity.JOURNAL_NAME;
import static com.snikpoh.bhopkins.thingstoremember.Activities.MainActivity.JOURNAL_TYPE;
import static java.util.Calendar.getInstance;

//import java.util.Calendar;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener
{
	
	static final int DATE_DIALOG_ID = 999;
	
	static final String DATE_DIVIDER = "/";
	static final String TIME_DIVIDER = ":";
	
	private final static String ACTIVITY_NAME = MainActivity.class.getSimpleName();
	
	private AdView               adView;
//	private FloatingActionButton fabBack;
//	private FloatingActionButton fabExplore;
	private EditText             etEntry;
	private TextView             tvEntryDate;
//	private AutoCompleteTextView actMood;
	private DatePicker           dpEntryDate;
	private Spinner              spinnerMood;
	
	private ThingsToRememberDbAdapter ttrDb;
	
	private String journalName;
	private String journalId;
	private String journalType;
	
	private String callingActivty;
	
	private int year;
	private int month;
	private int day;
	private Entry thisEntry = new Entry();
	
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
			tvEntryDate.setText(getFormattedDate());
			
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
		
		setExtraDataFromCallingActivity();
		setTitleText();
		
		
		
		//TODO:  Remove this!!
		//loadMoodsTestData();
		
		
		initializeSpinner();
		
	}
	
	private void setExtraDataFromCallingActivity()
	{
		
		journalName = getIntent().getStringExtra(JOURNAL_NAME);
		journalId = getIntent().getStringExtra(JOURNAL_ID);
		journalType = getIntent().getStringExtra(JOURNAL_TYPE);
		
		thisEntry.setDescription(getIntent().getStringExtra(ENTRY_DESCRIPTION));
		thisEntry.setEntryDate(getIntent().getStringExtra(ENTRY_DATE));
		thisEntry.setMoodId(getIntent().getStringExtra(ENTRY_MOOD));
		thisEntry.setJournalId(getIntent().getStringExtra(JOURNAL_ID));
		
		tvEntryDate.setText(thisEntry.getEntryDate());
		etEntry.setText(thisEntry.getDescription());
//		actMood.setText(thisEntry.getMoodId());
		
//		Intent i = new Intent(this, activityClass);
//		i.putExtra(ENTRY_DATE, entryDate);
//		i.putExtra(ENTRY_ID, entryId);
//		i.putExtra(ENTRY_DESCRIPTION, entryDescription);
//		i.putExtra(ENTRY_MOOD, entryMood);
//		i.putExtra(JOURNAL_ID, journalId);
	
	}
	
	private void setTitleText()
	{
		if (journalName == null)
		{
			this.setTitle("Make an entry:");
		}
		else
		{
			this.setTitle("Make an entry for " + journalName + ":");
		}
	}
	
	@RequiresApi(api = Build.VERSION_CODES.N) //allows Calendar.getInstance();
	private void initializeControls()
	{
//		fabBack = findViewById(R.id.fabBack);
//		fabBack.setOnClickListener(this);

//		fabExplore = findViewById(R.id.fabExplore);
//		fabExplore.setOnClickListener(this);

		etEntry = findViewById(R.id.etEntry);
//		actMood = findViewById(R.id.actMood);
		
		tvEntryDate = findViewById(R.id.etEntryDate);
		dpEntryDate = findViewById(R.id.dpEntryDate);
		
		spinnerMood = findViewById(R.id.spinnerMood);
		
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
		tvEntryDate.setText(getFormattedDate());
		
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
	public static String GetToday()
	{
		Date presentTime_Date = getInstance().getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
		return dateFormat.format(presentTime_Date);
	}
	
	private String getFormattedDate() //int year, int month, int day)
	{
		return new StringBuilder().append(year)
		                          .append(DATE_DIVIDER)
		                          .append(month + 1)
		                          .append(DATE_DIVIDER)
		                          .append(day)
		                          .append(" ")
		                          .append(getInstance().getTime().getHours())
		                          .append(TIME_DIVIDER)
		                          .append(getInstance().getTime().getMinutes())
		                          .append(TIME_DIVIDER)
		                          .append(getInstance().getTime().getSeconds()).toString();
	}
	
	private void loadAdView(int adViewID)
	{
		adView = findViewById(adViewID);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}
	
	@RequiresApi(api = Build.VERSION_CODES.O) // Allows ofEpochMilli
	private String formatStringToDate(String epoch)
	{
		long millisecondsSinceEpoch = Long.parseLong(epoch);
		
		Instant epochInstant = Instant.ofEpochMilli(millisecondsSinceEpoch);
		ZonedDateTime zdt = ZonedDateTime.ofInstant(epochInstant , ZoneOffset.UTC);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd h:mm:ss PM");
		String output = formatter.format ( zdt );
		
		return output;
		
	}
	
	private Long convertStringDateToMillisecondsFromEpoch(String dateAsString)
	{
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date date = null;
		try
		{
			date = df.parse(dateAsString);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		return date.getTime();
	}
	private boolean tryToWriteEntryToDb()
	{
		
		try
		{
			//String epoch = convertStringDateToMillisecondsFromEpoch(tvEntryDate.getText().toString().trim()).toString();
			String userDate = getFormattedDate(); //tvEntryDate.getText().toString().trim();
			
			ttrDb.insertIntoEntry(etEntry.getText().toString(),
			                      userDate,
			                      spinnerMood.getSelectedItem().toString(),
			                      journalId);
			
			Log.d(ACTIVITY_NAME,
			      journalName +
					      ": DB Entry of: " +
					      etEntry.getText().toString() + "; " +
					      userDate + "; " +
//					      actMood.getText().toString() + "; " +
						  spinnerMood.getSelectedItem() + ": " +
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
//			case R.id.fabBack:
//
//				if (tryToWriteEntryToDb())
//				{
//					startAnActivity(MainActivity.class);
//				}
//
//				break;
//
//			case R.id.fabAddAnother:
//
//				if (tryToWriteEntryToDb())
//				{
//					resetControls();
//				}
//
//				break;
//			case R.id.fabExplore:
//
//				startAnActivity(ExploreEntriesActivity.class, journalName, journalId);
//
			default:
				
				Log.d(ACTIVITY_NAME, "default case in onClick (" + id + ")");
				
				break;
		}
	}
	
	private void resetControls()
	{
		etEntry.setText("");
		tvEntryDate.setText("");
//		actMood.setText("");
	
	}
	
	private void startAnActivity(Class activityClass)
	{
		Log.d(ACTIVITY_NAME, "Opening activity: " + activityClass.getSimpleName());
		
		Intent i = new Intent(this, activityClass);
		startActivity(i);
		
		finish();
	}
	
	private void startAnActivity(Class activityClass, String journalName, String journalId, String journalType)
	{
		Log.d(ACTIVITY_NAME, "Opening activity: " + activityClass.getSimpleName());
		
		Intent i = new Intent(this, activityClass);
		i.putExtra(JOURNAL_NAME, journalName);
		i.putExtra(JOURNAL_ID, journalId);
		i.putExtra(JOURNAL_TYPE, journalType);
		startActivity(i);
		
		finish();
	}
	
	private void initializeDatabase()
	{
		ttrDb = new ThingsToRememberDbAdapter(this);
		ttrDb.open();
	}
	
	private void loadMoodsTestData()
	{
		ttrDb.createMood("Happy", "happyImage");
		ttrDb.createMood("Sad", "SadImage");
		ttrDb.createMood("Mad", "MadImage");
		ttrDb.createMood("Excited", "ExcitedImage");
		ttrDb.createMood("Shocked", "ShockedImage");
	}
	
//	public String[] getAllMoods()
//	{
//		Cursor moods = ttrDb.fetchAllMoods();
//
//		ArrayList<String> descriptions = new ArrayList<>();
//
//		while( ! moods.isAfterLast())
//		{
//			descriptions.add(moods.getString(moods.getColumnIndex(MoodSql.getColumnDescription())));
//			moods.moveToNext();
//		}
//
//		moods.close();
//
//		return descriptions.toArray(new String[descriptions.size()]);
//	}
	
	@Override
	public void onBackPressed()
	{
		try
		{
			super.onBackPressed();
			//		startAnActivity(MainActivity.class);
			tryToWriteEntryToDb();
			startAnActivity(ExploreEntriesActivity.class, journalName, journalId, journalType);
		}
		catch (Exception ex)
		{
		
		}
	}
	
	private void initializeSpinner()
	{
		List<String> moods = ttrDb.getAllMoodsAsArray(); //getAllMoods();
		
 		spinnerMood = findViewById(R.id.spinnerMood);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
		                                                  android.R.layout.simple_spinner_item,
		                                                  moods);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerMood.setAdapter(adapter);
		spinnerMood.setOnItemSelectedListener(this);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
	
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{
	
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
