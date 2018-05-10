package com.snikpoh.bhopkins.thingstoremember.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
		
		rowValues.put(Journal.getColumnName(), journalName);
		rowValues.put(Journal.getColumnType(), journalType);
		
		Log.d(LOG_TAG, "Inserting: " + journalName + " (" + journalType + ")");
		
		return sqlDb.insert(Journal.getTableName(), null, rowValues);
	}
	
	public boolean updateJournal(String journalName,
	                             String journalType)
	{
		ContentValues typeValue = new ContentValues();
		
		typeValue.put(Journal.getColumnType(), journalType);
		
		return sqlDb.update(Journal.getTableName(),
		                    typeValue,
		                    Journal.getColumnName() + "='" + journalName + "'",
		                    null) > 0;
	}
	
	public boolean deleteJournal(String journalName)
	{
		return sqlDb.delete(Journal.getTableName(),
		                    Journal.getColumnName() + "='" + journalName + "'",
		                    null) > 0;
	}
	
	public boolean deleteAllJournals()
	{
		long result = sqlDb.delete(Journal.getTableName(),
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
		return sqlDb.query(Journal.getTableName(),
		                   Journal.getColumnList(),
		                   null,
		                   null,
		                   null,
		                   null,
		                   null);
	}
	
	public Cursor fetchJournalByName(String journalName)
	{
		Cursor journal = sqlDb.query(true,
		                             Journal.getTableName(),
		                             Journal.getColumnList(),
		                             Journal.getColumnName() + "='" + journalName + "'",
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
	
	public Cursor fetchJournalByType(String journalType)
	{
		Cursor journals = sqlDb.query(true,
		                              Journal.getTableName(),
		                              Journal.getColumnList(),
		                              Journal.getColumnType() + "='" + journalType + "'",
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
		
		rowValues.put(Entry.getColumnDescription(), entryDescription);
		rowValues.put(Entry.getColumnEntryDate(), entryDate);
		rowValues.put(Entry.getColumnMoodId(), entryMoodId);
		rowValues.put(Entry.getColumnJournalId(), entryJournalId);
		
		Log.d(LOG_TAG, "Inserting into Entry :: Date: " + entryDate +
				               ", Description: " + entryDescription +
				               ", MoodId: " + entryMoodId +
				               ", JournalId: " + entryJournalId + ".");
		
		return sqlDb.insert(Entry.getTableName(), null, rowValues);
	}
	
	public boolean updateJournal(String entryID,
	                             String entryDescription,
	                             String entryDate,
	                             String entryMoodId)
	{
		ContentValues rowValues = new ContentValues();
		
		rowValues.put(Entry.getColumnDescription(), entryDescription);
		rowValues.put(Entry.getColumnEntryDate(), entryDate);
		rowValues.put(Entry.getColumnMoodId(), entryMoodId);
		
		return sqlDb.update(Entry.getTableName(),
		                    rowValues,
		                    Entry.getColumnId() + "='" + entryID + "'",
		                    null) > 0;
	}
	
	public boolean deleteEntry(String entryID)
	{
		return sqlDb.delete(Entry.getTableName(),
		                    Entry.getColumnId() + "='" + entryID,
		                    null) > 0;
	}
	
	public Cursor fetchAllEntries()
	{
		return fetchEntriesByAnyCriteria(null);
	}
	
	public Cursor fetchEntriesByEntryID(String entryID)
	{
		return fetchEntriesByAnyColumn(Entry.getColumnId(), entryID);
	}
	
	public Cursor fetchEntriesByJournalId(int journalId)
	{
		Cursor entries = sqlDb.query(true,
		                             Entry.getTableName(),
		                             Entry.getColumnList(),
		                             Entry.getColumnJournalId() + "=" + journalId,
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
	public Cursor fetchEntriesByJournalName(String journalName)
	{
		int journalId = this.fetchJournalByName(journalName).getColumnIndexOrThrow(Journal.getColumnId());
		
		Cursor entries = sqlDb.query(true,
		                             Entry.getTableName(),
		                             Entry.getColumnList(),
		                             Entry.getColumnJournalId() + "=" + journalId,
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
	
	public Cursor fetchEntriesByDate(String entryDate)
	{
		return fetchEntriesByAnyColumn(Entry.getColumnEntryDate(), entryDate);
	}
	
	private Cursor fetchEntriesByAnyColumn(String columnName, String searchValue)
	{
		Cursor entries = sqlDb.query(true,
		                             Entry.getTableName(),
		                             Entry.getColumnList(),
		                             columnName + "='" + searchValue + "'",
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
	
	private Cursor fetchEntriesByAnyCriteria(String criteria)
	{
		Cursor entries = sqlDb.query(true,
		                             Entry.getTableName(),
		                             Entry.getColumnList(),
		                             criteria,
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
	
	public Cursor fetchEntriesByMood(String entryMoodId)
	{
		return fetchEntriesByAnyColumn(Entry.getColumnMoodId(), entryMoodId);
	}
	
	public Cursor fetchAllMoods()
	{
		Cursor moods = sqlDb.query(true,
		                             Mood.getTableName(),
		                             Mood.getColumnList(),
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
	
	public Cursor fetchEntriesByKeywordInDescription(String keyword)
	{
		return fetchEntriesByAnyCriteria(Entry.getColumnDescription() + "LIKE '%" + keyword + "%'");
	}
	
	//endregion Entries
	
	//region Mood
	
	public long createMood(String moodDescription,
	                       String moodImage)
	{
		ContentValues rowValues = new ContentValues();
		
		rowValues.put(Mood.getColumnDescription(), moodDescription);
		rowValues.put(Mood.getColumnImage(), moodImage);
		
		return sqlDb.insert(Mood.getTableName(), null, rowValues);
	}
	
	public long createMood(String moodDescription)
	{
		ContentValues description = new ContentValues();
		
		description.put(Mood.getColumnDescription(), moodDescription);
		
		return sqlDb.insert(Mood.getTableName(), null, description);
	}
	
	public boolean updateMood(String moodId,
	                          String moodDescription,
	                          String moodImage)
	{
		ContentValues rowValues = new ContentValues();
		
		rowValues.put(Mood.getColumnDescription(), moodDescription);
		rowValues.put(Mood.getColumnImage(), moodImage);
		
		return sqlDb.update(Mood.getTableName(),
		                    rowValues,
		                    Mood.getColumnId() + "='" + moodId + "'",
		                    null) > 0;
	}
	
	public boolean updateMoodDescription(int moodId,
	                                     String moodDescription)
	{
		ContentValues description = new ContentValues();
		
		description.put(Mood.getColumnDescription(), moodDescription);
		
		return sqlDb.update(Mood.getTableName(),
		                    description,
		                    Mood.getColumnId() + "='" + moodId + "'",
		                    null) > 0;
	}
	
	public boolean updateMoodImage(String moodDescription,
	                               String moodImage)
	{
		ContentValues image = new ContentValues();
		
		image.put(Mood.getColumnImage(), moodImage);
		
		return sqlDb.update(Mood.getTableName(),
		                    image,
		                    Mood.getColumnDescription() + "='" + moodDescription + "'",
		                    null) > 0;
	}
	
	public boolean deleteMood(int id)
	{
		return sqlDb.delete(Mood.getTableName(),
		                    Mood.getColumnId() + "='" + id,
		                    null) > 0;
	}
	
	public boolean deleteMood(String description)
	{
		return sqlDb.delete(Mood.getTableName(),
		                    Mood.getColumnDescription() + "='" + description,
		                    null) > 0;
	}
	
	public Cursor fetchMoodById(int id)
	{
		Cursor mood = sqlDb.query(true,
		                          Mood.getTableName(),
		                          Mood.getColumnList(),
		                          Mood.getColumnId() + "='" + id + "'",
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
	
	public Cursor fetchMoodByDescription(String description)
	{
		Cursor mood = sqlDb.query(true,
		                          Mood.getTableName(),
		                          Mood.getColumnList(),
		                          Mood.getColumnDescription() + "='" + description + "'",
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
	
	public Cursor fetchMoodByImage(String image)
	{
		Cursor mood = sqlDb.query(true,
		                          Mood.getTableName(),
		                          Mood.getColumnList(),
		                          Mood.getColumnImage() + "='" + image + "'",
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
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		private static final String TAG = "DatabaseHelper";

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			Log.d(TAG, "Creating database tables...");
			
			db.execSQL(Journal.getCreateTableStatement());
			db.execSQL(Entry.getCreateTableStatement());
			db.execSQL(Mood.getCreateTableStatement());
		}
		
		private void dropTables(SQLiteDatabase db)
		{
			Log.d(TAG, "Dropping database tables...");
			
			db.execSQL(Journal.getDropTableStatement());
			db.execSQL(Entry.getDropTableStatement());
			db.execSQL(Mood.getDropTableStatement());
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
				db.execSQL(ALTER + TABLE + Entry.getTableName() +
						           ADD + Entry.getColumnJournalId() + " INTEGER");
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
