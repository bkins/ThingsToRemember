package com.snikpoh.bhopkins.thingstoremember.Database;

import com.snikpoh.bhopkins.thingstoremember.Utilities.SQL;

import java.util.Date;

import static com.snikpoh.bhopkins.thingstoremember.Utilities.SQL.*;

public class EntrySql
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
	
	private static final String CREATE_TABLE_ENTRY = CREATE_TABLE +
			                                                 ENTRY_TABLE_NAME + " (" +
			                                                 ENTRY_COLUMN_ID + INTEGER + PRIMARY + KEY + AUTOINCREMENT + "," +
			                                                 ENTRY_COLUMN_DESCRIPTION + TEXT + ", " +
			                                                 ENTRY_COLUMN_ENTRY_DATE + DATE_TYPE + ", " +
			                                                 ENTRY_COLUMN_MOOD_ID + INTEGER + "," +
			                                                 ENTRY_COLUMN_JOURNAL_ID + INTEGER + ")";
	
	private static final String DROP_TABLE_ENTRY = DROP_TABLE + IF_EXISTS + ENTRY_TABLE_NAME;
	
	public static String getTableName()
	{
		return ENTRY_TABLE_NAME;
	}
	
	public static String getColumnId()
	{
		return ENTRY_COLUMN_ID;
	}
	
	public static String getColumnDescription()
	{
		return ENTRY_COLUMN_DESCRIPTION;
	}
	
	public static String getColumnEntryDate()
	{
		return ENTRY_COLUMN_ENTRY_DATE;
	}
	
	public static String getColumnMoodId()
	{
		return ENTRY_COLUMN_MOOD_ID;
	}
	
	public static String getColumnJournalId()
	{
		return ENTRY_COLUMN_JOURNAL_ID;
	}
	
	public static String[] getColumnList()
	{
		return ENTRY_COLUMN_LIST;
	}
	
	public static String getCreateTableStatement()
	{
		return CREATE_TABLE_ENTRY;
	}
	
	public static String getDropTableStatement()
	{
		return DROP_TABLE_ENTRY;
	}
	
}
