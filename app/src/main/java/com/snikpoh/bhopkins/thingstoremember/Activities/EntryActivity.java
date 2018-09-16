package com.snikpoh.bhopkins.thingstoremember.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.snikpoh.bhopkins.thingstoremember.Database.Journal;
import com.snikpoh.bhopkins.thingstoremember.Database.Mood;
import com.snikpoh.bhopkins.thingstoremember.Database.ThingsToRememberDbAdapter;
import com.snikpoh.bhopkins.thingstoremember.R;

import java.util.ArrayList;

import static com.snikpoh.bhopkins.thingstoremember.Activities.ExploreEntriesActivity.ENTRY_DATE;
import static com.snikpoh.bhopkins.thingstoremember.Activities.ExploreEntriesActivity.ENTRY_DESCRIPTION;
import static com.snikpoh.bhopkins.thingstoremember.Activities.ExploreEntriesActivity.ENTRY_ID;
import static com.snikpoh.bhopkins.thingstoremember.Activities.ExploreEntriesActivity.ENTRY_MOOD;
import static com.snikpoh.bhopkins.thingstoremember.Activities.MainActivity.JOURNAL_ID;
import static com.snikpoh.bhopkins.thingstoremember.Activities.MainActivity.JOURNAL_NAME;
import static com.snikpoh.bhopkins.thingstoremember.Activities.MainActivity.JOURNAL_TYPE;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener,
		                                                                AdapterView.OnItemSelectedListener
{
	
	static final int DATE_DIALOG_ID = 999;
	
	static final String DATE_DIVIDER = "/";
	
	private final static String ACTIVITY_NAME = MainActivity.class.getSimpleName();
	
	private AdView                    adView;
//	private FloatingActionButton fabBack;
//	private FloatingActionButton fabExplore;
	private FloatingActionButton      fabDone;
	private EditText                  etEntry;
	private TextView                  tvEntryDate;
	private AutoCompleteTextView      actMood;
	private DatePicker                dpEntryDate;
	private Spinner                   spinnerMood;
	private ImageView                 ivAddMood;
	
	private ThingsToRememberDbAdapter ttrDb;
	
	private String journalName;
	private String journalId;
	private String journalType;
	
	private String entryId;
	
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
		
		setExtraDataFromCallingActivity();
		setTitleText();
		
		//TODO:  Remove this!!
		//loadMoodsTestData();
		
		initializeSpinner();
	}
	
	private void setExtraDataFromCallingActivity()
	{
		tvEntryDate.setText(getIntent().getStringExtra(ENTRY_DATE));
		etEntry.setText(getIntent().getStringExtra(ENTRY_DESCRIPTION));
		actMood.setText(getIntent().getStringExtra(ENTRY_MOOD));
		
		journalId   = getIntent().getStringExtra(JOURNAL_ID);
		journalName = getIntent().getStringExtra(JOURNAL_NAME);
		entryId     = getIntent().getStringExtra(ENTRY_ID);
	}
	
	private void setTitleText()
	{
		if (journalName == null || journalId == null)
		{
			this.setTitle("Add an entry:");
		}
		else
		{
			this.setTitle("Entry for " + journalName);
		}
	}
	
	
	@RequiresApi(api = Build.VERSION_CODES.N) //allows Calendar.getInstance();
	private void initializeControls()
	{
		//fabBack = findViewById(R.id.fabBack);
//		fabBack.setOnClickListener(this);
		
		//fabExplore = findViewById(R.id.fabExplore);
//		fabExplore.setOnClickListener(this);
		
		fabDone = findViewById(R.id.fabDone);
		fabDone.setOnClickListener(this);
		
		etEntry = findViewById(R.id.etEntry);
		actMood = findViewById(R.id.actMood);
		actMood.setOnFocusChangeListener(new AutoCompleteTextView.OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (!hasFocus)
				{
					//Enter mood into DB
					tryToWriteMoodToDb();
					setSpinnerSelection(actMood.getText().toString());
					actMood.setText("");
				}
			}
		});
		
		tvEntryDate = findViewById(R.id.etEntryDate);
		dpEntryDate = findViewById(R.id.dpEntryDate);
		
		spinnerMood = findViewById(R.id.spinnerMood);
		
		ivAddMood = findViewById(R.id.ivAddMood);
		ivAddMood.setOnClickListener(this);
		
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
	
	private void setSpinnerSelection(String compareValue)
	{
		String[] moods = initializeSpinner();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		                                                        android.R.layout.simple_spinner_item,
		                                                        moods);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerMood.setAdapter(adapter);

		if (compareValue != null)
		{
			int spinnerPosition = adapter.getPosition(compareValue);
			spinnerMood.setSelection(spinnerPosition);
		}
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
	
	private boolean tryToWriteMoodToDb()
	{
		String newMood = actMood.getText().toString();
		try
		{
			if ( ! ttrDb.doesMoodExist(newMood))
			{
				ttrDb.createMood(newMood, "");
				
				Log.d(ACTIVITY_NAME,
				      newMood + " entered into the DB.");
			}
			else
			{
				Log.d(ACTIVITY_NAME,
				      newMood + " already exists in DB. Not entering into the DB.");
			}
			return true;
		}
		catch (Exception ex)
		{
			Log.e(ACTIVITY_NAME, ex.getMessage());
			return false;
		}
	}
	
	private boolean tryToWriteEntryToDb()
	{
		try
		{
			if (entryId == null ||
				entryId.isEmpty())
			{
				ttrDb.insertIntoEntry(etEntry.getText().toString(),
				                      tvEntryDate.getText().toString(),
				                      spinnerMood.getSelectedItem().toString(),
				                      journalId);
			}
			else
			{
				ttrDb.updateEntry(entryId,
				                  etEntry.getText().toString(),
				                  tvEntryDate.getText().toString(),
				                  spinnerMood.getSelectedItem().toString());
			}
			
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

			case R.id.fabDone:
				
				if (tvEntryDate.getText().toString().isEmpty())
				{
					Toast.makeText(this, "Please enter a date.", Toast.LENGTH_LONG).show();
				}
				else
				{
					if (tryToWriteEntryToDb())
					{
						startAnActivity(ExploreEntriesActivity.class);
					}
				}
				
				break;
				
			case R.id.fabAddEntry:
				
				if (tryToWriteEntryToDb())
				{
					resetControls();
				}
				
				break;
				
			case R.id.ivAddMood:
				
				startAnActivity(ManageMoodsActivity.class);
				
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
		Cursor cursor = ttrDb.fetchJournalById(Integer.parseInt(journalId));
		
		Intent i = new Intent(this, activityClass);
		i.putExtra(JOURNAL_NAME, cursor.getString(cursor.getColumnIndexOrThrow(Journal.getColumnName())));
		i.putExtra(JOURNAL_ID, journalId);
		i.putExtra(JOURNAL_TYPE, cursor.getString(cursor.getColumnIndexOrThrow(Journal.getColumnType())));
		
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
		
		finish();
	}
	
	private void startAnActivity(Class activityClass, String journalName, String journalId)
	{
		Log.d(ACTIVITY_NAME, "Opening activity: " + activityClass.getSimpleName());
		
		Intent i = new Intent(this, activityClass);
		i.putExtra(JOURNAL_NAME, journalName);
		i.putExtra(JOURNAL_ID, journalId);
		
		startActivity(i);
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
	
	public String[] getAllMoodsAsArray()
	{
		Cursor moods = ttrDb.fetchAllMoods();
		
		ArrayList<String> descriptions = new ArrayList<>();
		
		while( ! moods.isAfterLast())
		{
			descriptions.add(moods.getString(moods.getColumnIndex(Mood.getColumnDescription())));
			moods.moveToNext();
		}
		
		moods.close();
		
		return descriptions.toArray(new String[descriptions.size()]);
	}
	@Override
	public void onBackPressed()
	{
		try
		{
			super.onBackPressed();
			tryToWriteEntryToDb();
			startAnActivity(ExploreEntriesActivity.class, journalName, journalId, journalType);
		}
		catch (Exception ex)
		{
			//TODO: what could happen, and how to handle it
		}
	}
	
	private String[] initializeSpinner()
	{
		String[] moods = getAllMoodsAsArray();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		                                                        android.R.layout.simple_spinner_item,
		                                                        moods);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerMood.setAdapter(adapter);
		spinnerMood.setOnItemSelectedListener(this);
		
		return moods;
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
