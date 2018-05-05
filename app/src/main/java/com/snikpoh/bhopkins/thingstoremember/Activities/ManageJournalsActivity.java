package com.snikpoh.bhopkins.thingstoremember.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.snikpoh.bhopkins.thingstoremember.Database.ThingsToRememberDbAdapter;
import com.snikpoh.bhopkins.thingstoremember.R;

public class ManageJournalsActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener
{
	
	private final static String ACTIVITY_NAME = MainActivity.class.getSimpleName();
	
	private AdView adView;
	
	private ThingsToRememberDbAdapter ttrDb;
	
	//Activity Controls
	private EditText                etJournalName;
	private EditText                etJournalType;
	private EditText                etIntervalAmount;
	private AutoCompleteTextView    actInterval;
	private FloatingActionButton    fabBackManageJournal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_journal);
		
		initializeControls();
		initializeDatabase();
		
		loadAdView(R.id.adViewManageJournal);
	}
	
	private void initializeControls()
	{
		etJournalName           = findViewById(R.id.etJournalName);
		etJournalType           = findViewById(R.id.etJournalType);
		etIntervalAmount        = findViewById(R.id.etIntervalAmount);
		actInterval             = findViewById(R.id.actInterval);
		fabBackManageJournal    = findViewById(R.id.fabBackManageJournal);
		
		fabBackManageJournal.setOnClickListener(this);
	}
	
	private void initializeDatabase()
	{
		ttrDb = new ThingsToRememberDbAdapter(this);
		ttrDb.open();
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
			ttrDb.insertJournal(etJournalName.getText().toString(),
			                    etJournalType.getText().toString());
			
			Log.d(ACTIVITY_NAME,
			      etJournalName.getText().toString() +
					      ": DB Entry of: " +
					      etJournalName.getText().toString() + "; " +
					      etJournalType.getText().toString());
			return true;
		}
		catch (Exception ex)
		{
			Log.e(ACTIVITY_NAME, ex.getMessage());
			return false;
		}
	}
	
	private void startAnActivity(Class activityClass)
	{
		Log.d(ACTIVITY_NAME, "Opening activity: " + activityClass.getSimpleName());
		
		Intent i = new Intent(this, activityClass);
		startActivity(i);
	}
	
//	View.OnLongClickListener
	
	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		
		switch (id)
		{
			case R.id.fabBackManageJournal:
				
				if (tryToWriteEntryToDb())
				{
					startAnActivity(MainActivity.class);
				}
				
				break;
			
			default:
				
				Log.d(ACTIVITY_NAME, "default case in onClick (" + id + ")");
				
				break;
		}
	}
	
	
	@Override
	public boolean onLongClick(View v)
	{
		return false;
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
