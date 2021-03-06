package com.snikpoh.bhopkins.thingstoremember.Activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.snikpoh.bhopkins.thingstoremember.R;

public class JournalEntriesActivity extends AppCompatActivity
{
	
	private final static String ACTIVITY_NAME = MainActivity.class.getSimpleName();
	
	private AdView adView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		loadAdView(R.id.adViewEntry);

	}
	
	private void loadAdView(int adViewID)
	{
		adView = findViewById(adViewID);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
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
