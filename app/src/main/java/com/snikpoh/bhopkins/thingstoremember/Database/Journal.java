package com.snikpoh.bhopkins.thingstoremember.Database;

import com.snikpoh.bhopkins.thingstoremember.Utilities.SQL;

import static com.snikpoh.bhopkins.thingstoremember.Utilities.SQL.AUTOINCREMENT;
import static com.snikpoh.bhopkins.thingstoremember.Utilities.SQL.CREATE_TABLE;
import static com.snikpoh.bhopkins.thingstoremember.Utilities.SQL.DROP_TABLE;
import static com.snikpoh.bhopkins.thingstoremember.Utilities.SQL.IF_EXISTS;
import static com.snikpoh.bhopkins.thingstoremember.Utilities.SQL.INTEGER;
import static com.snikpoh.bhopkins.thingstoremember.Utilities.SQL.KEY;
import static com.snikpoh.bhopkins.thingstoremember.Utilities.SQL.NOT;
import static com.snikpoh.bhopkins.thingstoremember.Utilities.SQL.NULL;
import static com.snikpoh.bhopkins.thingstoremember.Utilities.SQL.PRIMARY;
import static com.snikpoh.bhopkins.thingstoremember.Utilities.SQL.TEXT;
import static com.snikpoh.bhopkins.thingstoremember.Utilities.SQL.UNIQUE;

public class Journal
{
	
	private static final String JOURNAL_TABLE_NAME = "Journals";
	
	private static final String JOURNAL_COLUMN_ID   = "_id";
	private static final String JOURNAL_COLUMN_NAME = "Name";
	private static final String JOURNAL_COLUMN_TYPE = "Type";
	
	private static final String CREATE_TABLE_JOURNAL = CREATE_TABLE +
			                                                   JOURNAL_TABLE_NAME + " (" +
			                                                   JOURNAL_COLUMN_ID + INTEGER + PRIMARY + KEY + AUTOINCREMENT +
			                                                   JOURNAL_COLUMN_NAME + TEXT + NOT + NULL + UNIQUE + ", " +
			                                                   JOURNAL_COLUMN_TYPE + TEXT + NOT + NULL + ")";
	
	private static final String DROP_TABLE_JOURNAL = DROP_TABLE + IF_EXISTS + JOURNAL_TABLE_NAME;
	private String name;
	private String type;
	
	public Journal(String name, String type)
	{
		this.name = name;
		this.type = type;
	}
	
	public static String getJournalTableName()
	{
		return JOURNAL_TABLE_NAME;
	}
	
	public static String getJournalColumnId()
	{
		return JOURNAL_COLUMN_ID;
	}

	public static String getJournalColumnName()
	{
		return JOURNAL_COLUMN_NAME;
	}
	
	public static String getJournalColumnType()
	{
		return JOURNAL_COLUMN_TYPE;
	}
	
	public static String getCreateTableStatement()
	{
		return CREATE_TABLE_JOURNAL;
	}
	
	public static String getDropTableJournal()
	{
		return DROP_TABLE_JOURNAL;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getType()
	{
		return type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
}
