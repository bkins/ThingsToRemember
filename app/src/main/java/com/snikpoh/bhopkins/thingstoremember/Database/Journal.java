package com.snikpoh.bhopkins.thingstoremember.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
	private static final String[] JOURNAL_COLUMN_LIST = new String[]{JOURNAL_COLUMN_ID,
	                                                                 JOURNAL_COLUMN_NAME,
	                                                                 JOURNAL_COLUMN_TYPE};
	
	private static final String CREATE_TABLE_JOURNAL = CREATE_TABLE +
			                                                   JOURNAL_TABLE_NAME + " (" +
			                                                   JOURNAL_COLUMN_ID + INTEGER + PRIMARY + KEY + AUTOINCREMENT + ","  +
			                                                   JOURNAL_COLUMN_NAME + TEXT + NOT + NULL + UNIQUE + ", " +
			                                                   JOURNAL_COLUMN_TYPE + TEXT + NOT + NULL + ")";
	
	private static final String DROP_TABLE_JOURNAL = DROP_TABLE + IF_EXISTS + JOURNAL_TABLE_NAME;
	
	public static String getTableName()
	{
		return JOURNAL_TABLE_NAME;
	}
	
	public static String[] getColumnList()
	{
		return JOURNAL_COLUMN_LIST;
	}
	
	public static String getColumnId()
	{
		return JOURNAL_COLUMN_ID;
	}

	public static String getColumnName()
	{
		return JOURNAL_COLUMN_NAME;
	}
	
	public static String getColumnType()
	{
		return JOURNAL_COLUMN_TYPE;
	}
	
	public static String getCreateTableStatement()
	{
		return CREATE_TABLE_JOURNAL;
	}
	
	public static String getDropTableStatement()
	{
		return DROP_TABLE_JOURNAL;
	}
	
	
}
