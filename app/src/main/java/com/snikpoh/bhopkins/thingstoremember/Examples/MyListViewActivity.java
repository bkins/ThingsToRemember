package com.snikpoh.bhopkins.thingstoremember.Examples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.snikpoh.bhopkins.thingstoremember.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MyListViewActivity extends AppCompatActivity
{
	
	static final String KEY_NAME = String.valueOf(R.string.KEY_NAME);
	static final String KEY_ORG  = String.valueOf(R.string.KEY_ORG);
	
	static String[] from = {KEY_NAME, KEY_ORG};
	//tatic int[]    to   = {R.id.personName, R.id.personOrg};
	
	ListView lv;
	
	ArrayList<HashMap<String, String>> peopleList = new ArrayList<HashMap<String, String>>();
	
	SimpleAdapter myAdaptor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//lv = (ListView) findViewById(R.id.myListView);

//		populateList();
//
//		myAdaptor = new SimpleAdapter(this, peopleList, R.layout.example_row, from, to);
//		lv.setAdapter(myAdaptor);
//
//		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//				Toast.makeText(MyListViewActivity.this,
//				               "Item with id [" + id + "] - Position [" + position + "] clicked.",
//				               Toast.LENGTH_SHORT).show();
//
//			}
//		});
		
	}
	
	private void populateList()
	{
		
		peopleList.add(createPerson("Ben", "snikpoH"));
		peopleList.add(createPerson("Jasmit", "EFlow"));
		peopleList.add(createPerson("Emily", "DOL"));
		peopleList.add(createPerson("Madhavi", "DOL"));
	}
	
	private HashMap<String, String> createPerson(String name, String org)
	{
		
		HashMap<String, String> person = new HashMap<String, String>();
		
		person.put(KEY_NAME, name);
		person.put(KEY_ORG, org);
		
		return person;
	}
}
