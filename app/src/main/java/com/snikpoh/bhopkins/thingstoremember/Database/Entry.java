package com.snikpoh.bhopkins.thingstoremember.Database;

import java.util.Date;

public class Entry
{
	private static final String ENTRY_TABLE_NAME = "Entries";
	
	private static final String ENTRY_COLUMN_ID          = "_id";
	private static final String ENTRY_COLUMN_DESCRIPTION = "Description";
	private static final String ENTRY_COLUMN_ENTRY_DATE  = "EntryDate";
	private static final String ENTRY_COLUMN_MOOD_ID     = "MoodId";
	private static final String ENTRY_COLUMN_JOURNAL_ID  = "JournalId";
	
	private static final String[] ENTRY_COLUMN_LIST = new String[]{ENTRY_COLUMN_ID,
	                                                               ENTRY_COLUMN_DESCRIPTION,
	                                                               ENTRY_COLUMN_ENTRY_DATE,
	                                                               ENTRY_COLUMN_MOOD_ID,
	                                                               ENTRY_COLUMN_JOURNAL_ID};
	
	private static final String CREATE_TABLE_ENTRY = "CREATE TABLE " +
			                                                 ENTRY_TABLE_NAME + " (" +
			                                                 ENTRY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			                                                 ENTRY_COLUMN_DESCRIPTION + " TEXT, " +
			                                                 ENTRY_COLUMN_ENTRY_DATE + " TEXT NOT NULL," +
			                                                 ENTRY_COLUMN_MOOD_ID + " INTEGER," +
			                                                 ENTRY_COLUMN_JOURNAL_ID + " INTEGER)";
	
	private static final String DROP_TABLE_ENTRY = "DROP TABLE IF EXISTS " + ENTRY_TABLE_NAME;
	
	private String description;
	private Date   entryDate;
	private String mood;
	
	public static String getEntryTableName()
	{
		return ENTRY_TABLE_NAME;
	}
	
	public static String getEntryColumnId()
	{
		return ENTRY_COLUMN_ID;
	}
	
	public static String getEntryColumnDescription()
	{
		return ENTRY_COLUMN_DESCRIPTION;
	}
	
	public static String getEntryColumnEntryDate()
	{
		return ENTRY_COLUMN_ENTRY_DATE;
	}
	
	public static String getEntryColumnMoodId()
	{
		return ENTRY_COLUMN_MOOD_ID;
	}
	
	public static String getEntryColumnJournalId()
	{
		return ENTRY_COLUMN_JOURNAL_ID;
	}
	
	public static String[] getEntryColumnList()
	{
		return ENTRY_COLUMN_LIST;
	}
	
	public static String getCreateTableEntry()
	{
		return CREATE_TABLE_ENTRY;
	}
	
	public static String getCreateTableStatement()
	{
		return CREATE_TABLE_ENTRY;
	}
	
	public static String getDropTableEntry()
	{
		return DROP_TABLE_ENTRY;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public Date getEntryDate()
	{
		return entryDate;
	}
	
	public void setEntryDate(Date entryDate)
	{
		this.entryDate = entryDate;
	}
	
	public String getMood()
	{
		return mood;
	}
	
	public void setMood(String mood)
	{
		this.mood = mood;
	}
	
	public void Entry()
	{

	}
}
