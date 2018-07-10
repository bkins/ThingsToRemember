package com.snikpoh.bhopkins.thingstoremember.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.*;
import java.util.List;

import static com.snikpoh.bhopkins.thingstoremember.Utilities.SQL.*;

public class ThingsToRememberDbAdapter
{
	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME = "ttrDb.db";
	private static final String LOG_TAG = "ttrDbAdapter";
	
	private final Context dbContext;
	
	private DatabaseHelper dbHelper;
	private SQLiteDatabase sqlDb;
	
	public ThingsToRememberDbAdapter(Context context)
	{
		this.dbContext = context;
		
		this.open();
		
		//TODO: Remove before release
		//dbHelper.resetDatabase(sqlDb);
	}
	
	public ThingsToRememberDbAdapter open() throws SQLException
	{
		dbHelper = new DatabaseHelper(dbContext);
		sqlDb = dbHelper.getWritableDatabase();
		
		Log.d(LOG_TAG, "DB Version " + sqlDb.getVersion() + " opened.");
		
		return this;
	}
	
	public void closeDb()
	{
		dbHelper.close();
	}
	
	//TODO:  Complete: creates, fetches, updates, deletes
	
	//region CRUD methods
	
	//region Journals
	
	public long insertJournal(String journalName,
	                          String journalType)
	{
		ContentValues rowValues = new ContentValues();
		
		rowValues.put(JournalSql.getColumnName(), journalName);
		rowValues.put(JournalSql.getColumnType(), journalType);
		
		Log.d(LOG_TAG, "Inserting: " + journalName + " (" + journalType + ")");
		
		return sqlDb.insert(JournalSql.getTableName(), null, rowValues);
	}
	
	public boolean updateJournal(String journalName,
	                             String journalType)
	{
		ContentValues typeValue = new ContentValues();
		
		typeValue.put(JournalSql.getColumnType(), journalType);
		
		return sqlDb.update(JournalSql.getTableName(),
		                    typeValue,
		                    JournalSql.getColumnName() + "='" + journalName + "'",
		                    null) > 0;
	}
	
	private int convertDateStringToInt(String dateAsString) throws ParseException
	{
		SimpleDateFormat    formatter = new SimpleDateFormat("dd/MM/yyy");
		Date date = formatter.parse(dateAsString);
		
		return (int) date.getTime();
	}
	
	public boolean deleteJournal(int journalId)
	{
		try
		{
			return sqlDb.delete(JournalSql.getTableName(),
			                    JournalSql.getColumnId() + "=" + journalId,
			                    null) > 0;
		}
		catch (SQLException sEx)
		{
			throw sEx;
		}
	}
	
	public boolean deleteJournal(String journalName)
	{
		return sqlDb.delete(JournalSql.getTableName(),
		                    JournalSql.getColumnName() + "='" + journalName + "'",
		                    null) > 0;
	}
	
	public boolean deleteAllJournals()
	{
		long result = sqlDb.delete(JournalSql.getTableName(),
		                           null,
		                           null);
		if (result > 0)
		{
			Log.d(LOG_TAG, "All Journals deleted.");
			return true;
		}
		else if (result == 0)
		{
			Log.d(LOG_TAG, "No Journals were deleted.");
			return true;
		}
		else
		{
			Log.d(LOG_TAG, "Could not delete any Journals.  Something went wrong.");
			return false;
		}
	}
	
	public Cursor fetchAllJournals()
	{
		return sqlDb.query(JournalSql.getTableName(),
		                   JournalSql.getColumnList(),
		                   null,
		                   null,
		                   null,
		                   null,
		                   null);
	}
	
	private Cursor fetchJournalByName(String journalName)
	{
		Cursor journal = sqlDb.query(true,
		                             JournalSql.getTableName(),
		                             JournalSql.getColumnList(),
		                             JournalSql.getColumnName() + "='" + journalName + "'",
		                             null,
		                             null,
		                             null,
		                             null,
		                             null);
		if (journal != null)
		{
			journal.moveToFirst();
		}
		
		return journal;
	}
	
	private Cursor fetchJournalByType(String journalType)
	{
		Cursor journals = sqlDb.query(true,
		                              JournalSql.getTableName(),
		                              JournalSql.getColumnList(),
		                              JournalSql.getColumnType() + "='" + journalType + "'",
		                              null,
		                              null,
		                              null,
		                              null,
		                              null);
		if (journals != null)
		{
			journals.moveToFirst();
		}
		
		return journals;
	}
	
	//endregion Journals
	
	//region Entries
	
	public long insertIntoEntry(String entryDescription,
	                            String entryDate,
	                            String entryMoodId,
	                            String entryJournalId) throws Exception
	{
		if (entryJournalId == null || entryJournalId.equals(""))
		{
			throw new Exception("No JournalID was provided when trying to create the Journal Entry.");
		}
		
		ContentValues rowValues = new ContentValues();
		
		//int dateAsInt = convertDateStringToInt(entryDate);
		
		rowValues.put(EntrySql.getColumnDescription(), entryDescription);
		rowValues.put(EntrySql.getColumnEntryDate(), entryDate);
		rowValues.put(EntrySql.getColumnMoodId(), entryMoodId);
		rowValues.put(EntrySql.getColumnJournalId(), entryJournalId);
		
		Log.d(LOG_TAG, "Inserting into Entry :: Date: " + entryDate + "(Converted to: " + entryDate +
				               ", Description: " + entryDescription +
				               ", MoodId: " + entryMoodId +
				               ", JournalId: " + entryJournalId + ".");
		
		return sqlDb.insert(EntrySql.getTableName(), null, rowValues);
	}
	
	public boolean updateJournal(String entryID,
	                             String entryDescription,
	                             String entryDate,
	                             String entryMoodId)
	{
		ContentValues rowValues = new ContentValues();
		
		rowValues.put(EntrySql.getColumnDescription(), entryDescription);
		rowValues.put(EntrySql.getColumnEntryDate(), entryDate);
		rowValues.put(EntrySql.getColumnMoodId(), entryMoodId);
		
		return sqlDb.update(EntrySql.getTableName(),
		                    rowValues,
		                    EntrySql.getColumnId() + "='" + entryID + "'",
		                    null) > 0;
	}
	
	public boolean deleteEntry(String entryID)
	{
		return sqlDb.delete(EntrySql.getTableName(),
		                    EntrySql.getColumnId() + "='" + entryID,
		                    null) > 0;
	}
	
	private Cursor fetchAllEntries()
	{
		return fetchEntriesByAnyCriteria(null);
	}
	
	private Cursor fetchEntriesByEntryID(String entryID)
	{
		return fetchEntriesByAnyColumn(EntrySql.getColumnId(), entryID);
	}
	
	public Cursor fetchEntriesByJournalId(int journalId)
	{
		Cursor entries = sqlDb.query(true,
		                             EntrySql.getTableName(),
		                             EntrySql.getColumnList(),
		                             EntrySql.getColumnJournalId() + "=" + journalId,
		                             null,
		                             null,
		                             null,
		                             EntrySql.getColumnEntryDate() + DESC,
		                             null);
		if (entries != null)
		{
			entries.moveToFirst();
		}
		
		return entries;
	}
	private Cursor fetchEntriesByJournalName(String journalName)
	{
		int journalId = this.fetchJournalByName(journalName).getColumnIndexOrThrow(JournalSql.getColumnId());
		
		Cursor entries = sqlDb.query(true,
		                             EntrySql.getTableName(),
		                             EntrySql.getColumnList(),
		                             EntrySql.getColumnJournalId() + "=" + journalId,
		                             null,
		                             null,
		                             null,
		                             null,
		                             null);
		if (entries != null)
		{
			entries.moveToFirst();
		}
		
		return entries;
	}
	
	private Cursor fetchEntriesByDate(String entryDate)
	{
		return fetchEntriesByAnyColumn(EntrySql.getColumnEntryDate(), entryDate);
	}
	
	private Cursor fetchEntriesByAnyColumn(String columnName, String searchValue)
	{
		Cursor entries = sqlDb.query(true,
		                             EntrySql.getTableName(),
		                             EntrySql.getColumnList(),
		                             columnName + "='" + searchValue + "'",
		                             null,
		                             null,
		                             null,
		                             EntrySql.getColumnEntryDate() + DESC,
		                             null);
		if (entries != null)
		{
			entries.moveToFirst();
		}
		
		return entries;
	}
	
	private Cursor fetchEntriesByAnyCriteria(String criteria)
	{
		Cursor entries = sqlDb.query(true,
		                             EntrySql.getTableName(),
		                             EntrySql.getColumnList(),
		                             criteria,
		                             null,
		                             null,
		                             null,
		                             EntrySql.getColumnEntryDate() + DESC,
		                             null);
		if (entries != null)
		{
			entries.moveToFirst();
		}
		
		return entries;
	}
	
	private Cursor fetchEntriesByMood(String entryMoodId)
	{
		return fetchEntriesByAnyColumn(EntrySql.getColumnMoodId(), entryMoodId);
	}
	
	private Cursor fetchAllMoods()
	{
		Cursor moods = sqlDb.query(true,
		                           MoodSql.getTableName(),
		                           MoodSql.getColumnList(),
		                           null,
		                           null,
		                           null,
		                           null,
		                           null,
		                           null);
		if (moods != null)
		{
			moods.moveToFirst();
		}
		
		return moods;
	}
	
	private Cursor fetchEntriesByKeywordInDescription(String keyword)
	{
		return fetchEntriesByAnyCriteria(EntrySql.getColumnDescription() + "LIKE '%" + keyword + "%'");
	}
	
	//endregion Entries
	
	//region Mood
	
	public long createMood(String moodDescription,
	                       String moodImage)
	{
		ContentValues rowValues = new ContentValues();
		
		rowValues.put(MoodSql.getColumnDescription(), moodDescription);
		rowValues.put(MoodSql.getColumnImage(), moodImage);
		
		return sqlDb.insert(MoodSql.getTableName(), null, rowValues);
	}
	
	public long createMood(String moodDescription)
	{
		ContentValues description = new ContentValues();
		
		description.put(MoodSql.getColumnDescription(), moodDescription);
		
		return sqlDb.insert(MoodSql.getTableName(), null, description);
	}
	
	public boolean updateMood(String moodId,
	                          String moodDescription,
	                          String moodImage)
	{
		ContentValues rowValues = new ContentValues();
		
		rowValues.put(MoodSql.getColumnDescription(), moodDescription);
		rowValues.put(MoodSql.getColumnImage(), moodImage);
		
		return sqlDb.update(MoodSql.getTableName(),
		                    rowValues,
		                    MoodSql.getColumnId() + "='" + moodId + "'",
		                    null) > 0;
	}
	
	public boolean updateMoodDescription(int moodId,
	                                     String moodDescription)
	{
		ContentValues description = new ContentValues();
		
		description.put(MoodSql.getColumnDescription(), moodDescription);
		
		return sqlDb.update(MoodSql.getTableName(),
		                    description,
		                    MoodSql.getColumnId() + "='" + moodId + "'",
		                    null) > 0;
	}
	
	public boolean updateMoodImage(String moodDescription,
	                               String moodImage)
	{
		ContentValues image = new ContentValues();
		
		image.put(MoodSql.getColumnImage(), moodImage);
		
		return sqlDb.update(MoodSql.getTableName(),
		                    image,
		                    MoodSql.getColumnDescription() + "='" + moodDescription + "'",
		                    null) > 0;
	}
	
	public boolean deleteMood(int id)
	{
		return sqlDb.delete(MoodSql.getTableName(),
		                    MoodSql.getColumnId() + "='" + id,
		                    null) > 0;
	}
	
	public boolean deleteMood(String description)
	{
		return sqlDb.delete(MoodSql.getTableName(),
		                    MoodSql.getColumnDescription() + "='" + description,
		                    null) > 0;
	}
	
	private Cursor fetchMoodById(int id)
	{
		Cursor mood = sqlDb.query(true,
		                          MoodSql.getTableName(),
		                          MoodSql.getColumnList(),
		                          MoodSql.getColumnId() + "='" + id + "'",
		                          null,
		                          null,
		                          null,
		                          null,
		                          null);
		if (mood != null)
		{
			mood.moveToFirst();
		}
		
		return mood;
	}
	
	private Cursor fetchMoodByDescription(String description)
	{
		Cursor mood = sqlDb.query(true,
		                          MoodSql.getTableName(),
		                          MoodSql.getColumnList(),
		                          MoodSql.getColumnDescription() + "='" + description + "'",
		                          null,
		                          null,
		                          null,
		                          null,
		                          null);
		if (mood != null)
		{
			mood.moveToFirst();
		}
		
		return mood;
	}
	
	private Cursor fetchMoodByImage(String image)
	{
		Cursor mood = sqlDb.query(true,
		                          MoodSql.getTableName(),
		                          MoodSql.getColumnList(),
		                          MoodSql.getColumnImage() + "='" + image + "'",
		                          null,
		                          null,
		                          null,
		                          null,
		                          null);
		if (mood != null)
		{
			mood.moveToFirst();
		}
		
		return mood;
	}
	
	//endregion Mood
	
	//endregion CRUD methods
	
	//region Creates
	
	public long addJournal(Journal aJournal)
	{
		ContentValues values = new ContentValues();
		values.put(JournalSql.getColumnName(), aJournal.getName());
		values.put(JournalSql.getColumnType(), aJournal.getType());
		
		return sqlDb.insert(JournalSql.getTableName(), null, values);
	}
	
	public long addEntry(Entry anEntry)
	{
		ContentValues values = new ContentValues();
		values.put(EntrySql.getColumnDescription(), anEntry.getDescription());
		values.put(EntrySql.getColumnEntryDate(), anEntry.getEntryDate());
		values.put(EntrySql.getColumnJournalId(), anEntry.getJournalId());
		values.put(EntrySql.getColumnMoodId(), anEntry.getMoodId());
		
		return sqlDb.insert(EntrySql.getTableName(), null, values);
		
	}
	
	public long addMood(Mood aMood)
	{
		ContentValues values = new ContentValues();
		values.put(MoodSql.getColumnDescription(), aMood.getDescription());
		values.put(MoodSql.getColumnImage(), aMood.getImage());
		
		return sqlDb.insert(MoodSql.getTableName(), null, values);
	}
	
	//endregion Creates
	
	
	private String getColumnStringValueByName(Cursor cursor, String columnName)
	{
		int index = cursor.getColumnIndexOrThrow(columnName);
		
		return cursor.getString(index);
	}
	
	
	private String getColumnIntAsStringValueByName(Cursor cursor, String columnName)
	{
		int index = cursor.getColumnIndexOrThrow(columnName);
		
		return Integer.toString(cursor.getInt(index));
	}
	
	//region Reads
	
	public ArrayList<Journal> getAllJournals()
	{
		Cursor cursor = this.fetchAllJournals();
		
		return convertJournalCursorToArrayList(cursor);
	}
	
	public Journal getJournalByName(String journalName)
	{
		Cursor cursor = this.fetchJournalByName(journalName);
		
		return convertCursorValueToJournal(cursor);
	}
	
	public ArrayList<Journal> getJournalsByType(String journalType)
	{
		Cursor cursor = this.fetchJournalByType(journalType);
		
		return convertJournalCursorToArrayList(cursor);
	}
	
	public ArrayList<Entry> getAllEntries()
	{
		Cursor cursor = this.fetchAllEntries();
		
		return convertEntryCursorToArrayList(cursor);
	}
	
	public ArrayList<Entry> getEntiesByAnyColumn(String columnName, String searchValue)
	{
		Cursor cursor = this.fetchEntriesByAnyColumn(columnName, searchValue);
		
		return convertEntryCursorToArrayList(cursor);
	}
	
	public ArrayList<Entry> getEntriesByAnyCriteria(String criteria)
	{
		Cursor cursor = this.fetchEntriesByAnyCriteria(criteria);
		
		return convertEntryCursorToArrayList(cursor);
	}
	
	public ArrayList<Entry> getEntriesByKeyword(String keyword)
	{
		Cursor cursor = this.fetchEntriesByKeywordInDescription(keyword);
		
		return convertEntryCursorToArrayList(cursor);
	}
	
	public ArrayList<Entry> getEntriesByDate(String entryDate)
	{
		Cursor cursor = this.fetchEntriesByDate(entryDate);
		
		return convertEntryCursorToArrayList(cursor);
	}
	
	public ArrayList<Entry> getEntriesById(String entryId)
	{
		Cursor cursor = this.fetchEntriesByEntryID(entryId);
		
		return convertEntryCursorToArrayList(cursor);
	}
	
	public ArrayList<Entry> getEntriesByJournalId(int journalId)
	{
		Cursor cursor = this.fetchEntriesByJournalId(journalId);
		
		return convertEntryCursorToArrayList(cursor);
	}
	
	public ArrayList<Entry> getEntriesByMoodId(String entryMoodId)
	{
		Cursor cursor = this.fetchEntriesByMood(entryMoodId);
		
		return convertEntryCursorToArrayList(cursor);
	}
	
	public ArrayList<Mood> getMoodsByDescription(String description)
	{
		Cursor cursor = this.fetchMoodByDescription(description);
		
		return convertMoodCursorToArrayList(cursor);
	}
	
	public Mood getMoodsById(int id)
	{
		Cursor cursor = this.fetchMoodById(id);
		
		return convertCursorValueToMood(cursor);
	}
	
	public ArrayList<Mood> getMoodsByImage(String image)
	{
		Cursor cursor = this.fetchMoodByImage(image);
		
		return convertMoodCursorToArrayList(cursor);
	}
	
	public ArrayList<Mood> getAllMoods()
	{
		Cursor cursor = this.fetchAllMoods();
		
		return convertMoodCursorToArrayList(cursor);
	}
	
	public List<String> getAllMoodsAsArray()
	{
		Cursor cursor = this.fetchAllMoods();
		
		return convertMoodCursorToArray(cursor);
	}
	
	//endregion Reads
	
	//region Converters
	
	private Journal convertCursorValueToJournal(Cursor cursor)
	{
		Journal aJournal = new Journal();
		
		aJournal.setId(Integer.parseInt(getColumnStringValueByName(cursor, JournalSql.getColumnId())));
		aJournal.setName(getColumnStringValueByName(cursor, JournalSql.getColumnName()));
		aJournal.setType(getColumnStringValueByName(cursor, JournalSql.getColumnType()));
		
		return aJournal;
	}
	
	private Entry convertCursorValueToEntry(Cursor cursor)
	{
		Entry anEntry = new Entry();
		
		anEntry.setId(Integer.parseInt(getColumnIntAsStringValueByName(cursor, EntrySql.getColumnId())));
		anEntry.setDescription(getColumnStringValueByName(cursor, EntrySql.getColumnDescription()));
		anEntry.setEntryDate(getColumnStringValueByName(cursor, EntrySql.getColumnEntryDate()));
		anEntry.setJournalId(getColumnIntAsStringValueByName(cursor, EntrySql.getColumnJournalId()));
		anEntry.setMoodId(getColumnIntAsStringValueByName(cursor, EntrySql.getColumnMoodId()));
		
		return anEntry;
	}
	
	private Mood convertCursorValueToMood(Cursor cursor)
	{
		Mood aMood = new Mood();
		
		aMood.setId(Integer.parseInt(getColumnStringValueByName(cursor, MoodSql.getColumnId())));
		aMood.setDescription(getColumnStringValueByName(cursor, MoodSql.getColumnDescription()));
		aMood.setImage(getColumnStringValueByName(cursor, MoodSql.getColumnImage()));
		
		return aMood;
	}
	
	private ArrayList<Journal> convertJournalCursorToArrayList(Cursor cursor)
	{
		ArrayList<Journal> journals = new ArrayList<>();
		
		for (cursor.moveToFirst();
		     ! cursor.isAfterLast();
		     cursor.moveToNext())
		{
			journals.add(convertCursorValueToJournal(cursor));
		}
		
		return journals;
	}
	
	private ArrayList<Entry> convertEntryCursorToArrayList(Cursor cursor)
	{
		ArrayList<Entry> entries = new ArrayList<>();
		
		for (cursor.moveToFirst();
		     ! cursor.isAfterLast();
		     cursor.moveToNext())
		{
			entries.add(convertCursorValueToEntry(cursor));
		}
		
		return entries;
	}
	
	private ArrayList<Mood> convertMoodCursorToArrayList(Cursor cursor)
	{
		ArrayList<Mood> moods = new ArrayList<>();
		
		for (cursor.moveToFirst();
		     ! cursor.isAfterLast();
		     cursor.moveToNext())
		{
			moods.add(convertCursorValueToMood(cursor));
		}
		
		return moods;
	}
	
	private List<String> convertMoodCursorToArray(Cursor cursor)
	{
		List<String> moods = new ArrayList<>();
		
		for (cursor.moveToFirst();
			 ! cursor.isAfterLast();
			 cursor.moveToNext())
		{
			moods.add(getColumnStringValueByName(cursor, MoodSql.getColumnDescription()));
		}
		
		return moods;
	}
	
	//endregion Converters
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		private static final String TAG = "DatabaseHelper";

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			Log.d(TAG, "Creating database tables...");
			
			db.execSQL(JournalSql.getCreateTableStatement());
			db.execSQL(EntrySql.getCreateTableStatement());
			db.execSQL(MoodSql.getCreateTableStatement());
		}
		
		private void dropTables(SQLiteDatabase db)
		{
			Log.d(TAG, "Dropping database tables...");
			
			db.execSQL(JournalSql.getDropTableStatement());
			db.execSQL(EntrySql.getDropTableStatement());
			db.execSQL(MoodSql.getDropTableStatement());
		}
		
		public void resetDatabase(SQLiteDatabase db)
		{
			dropTables(db);
			onCreate(db);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			if (oldVersion <= 2)
			{
				Log.d(TAG, "Attempting to upgrade DB Version from " + oldVersion + " to " + newVersion);
				db.execSQL(ALTER + TABLE + EntrySql.getTableName() +
						           ADD + EntrySql.getColumnJournalId() + INTEGER);
			}

//			if (oldVersion < 3)
//			{
//				//next DB upgrade goes here
//			}
			
			Log.d(TAG, "DB upgraded to version " + newVersion);
		}
		
		
		public DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		public DatabaseHelper(Context context,
		                      String name,
		                      SQLiteDatabase.CursorFactory factory,
		                      int version)
		{
			super(context, name, factory, version);
		}
		
		public DatabaseHelper(Context context,
		                      String name,
		                      SQLiteDatabase.CursorFactory factory,
		                      int version,
		                      DatabaseErrorHandler errorHandler)
		{
			super(context, name, factory, version, errorHandler);
		}
		
		@Override
		public String getDatabaseName()
		{
			return super.getDatabaseName();
		}
		
		@Override
		public void setWriteAheadLoggingEnabled(boolean enabled)
		{
			super.setWriteAheadLoggingEnabled(enabled);
		}
		
		@Override
		public SQLiteDatabase getWritableDatabase()
		{
			return super.getWritableDatabase();
		}
		
		@Override
		public SQLiteDatabase getReadableDatabase()
		{
			return super.getReadableDatabase();
		}
		
		@Override
		public synchronized void close()
		{
			super.close();
		}
		
		@Override
		public void onConfigure(SQLiteDatabase db)
		{
			super.onConfigure(db);
		}
		
		@Override
		public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			super.onDowngrade(db, oldVersion, newVersion);
		}
		
		@Override
		public void onOpen(SQLiteDatabase db)
		{
			super.onOpen(db);
		}
		
	}
}
