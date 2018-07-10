package com.snikpoh.bhopkins.thingstoremember.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.snikpoh.bhopkins.thingstoremember.R;

public class ManageMoodsActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_moods);
		
		this.setTitle("Manage Moods");
	}
}
