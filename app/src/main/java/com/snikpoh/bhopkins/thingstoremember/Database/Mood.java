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

public class Mood
{
	private static final String MOOD_TABLE_NAME = "Mood";
	
	private static final String MOOD_COLUMN_ID          = "_id";
	private static final String MOOD_COLUMN_DESCRIPTION = "Description";
	private static final String MOOD_COLUMN_IMAGE       = "Image";
	
	private static final String CREATE_TABLE_MOOD = CREATE_TABLE +
			                                                MOOD_TABLE_NAME + " (" +
			                                                MOOD_COLUMN_ID + INTEGER + PRIMARY + KEY + AUTOINCREMENT +
			                                                MOOD_COLUMN_DESCRIPTION + TEXT + NOT + NULL + UNIQUE + ", " +
			                                                MOOD_COLUMN_IMAGE + TEXT + ")";
	
	private static final String[] MOOD_COLUMN_LIST = new String[]{MOOD_COLUMN_ID,
	                                                              MOOD_COLUMN_DESCRIPTION,
	                                                              MOOD_COLUMN_IMAGE};
	
	private static final String DROP_TABLE_MOOD = DROP_TABLE + IF_EXISTS + MOOD_TABLE_NAME;
	private String description;
	private String image;
	
	public Mood()
	{
		description = "";
		image = "";
	}
	
	public Mood(String description, String image)
	{
		this.description = description;
		this.image = image;
	}
	
	public static String getMoodTableName()
	{
		return MOOD_TABLE_NAME;
	}
	
	public static String getMoodColumnId()
	{
		return MOOD_COLUMN_ID;
	}

	public static String getMoodColumnDescription()
	{
		return MOOD_COLUMN_DESCRIPTION;
	}
	
	public static String getMoodColumnImage()
	{
		return MOOD_COLUMN_IMAGE;
	}
	
	public static String[] getMoodColumnList()
	{
		return MOOD_COLUMN_LIST;
	}
	
	public static String getCreateTableStatement()
	{
		return CREATE_TABLE_MOOD;
	}
	
	public static String getDropTableMood()
	{
		return DROP_TABLE_MOOD;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public String getImage()
	{
		return image;
	}
	
	public void setImage(String image)
	{
		this.image = image;
	}
}
