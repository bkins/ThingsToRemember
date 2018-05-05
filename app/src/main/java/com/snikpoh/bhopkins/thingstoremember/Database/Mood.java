package com.snikpoh.bhopkins.thingstoremember.Database;

public class Mood
{
	private static final String MOOD_TABLE_NAME = "Mood";
	
	private static final String MOOD_COLUMN_ID          = "_id";
	private static final String MOOD_COLUMN_DESCRIPTION = "Description";
	private static final String MOOD_COLUMN_IMAGE       = "Image";
	
	private static final String CREATE_TABLE_MOOD = "CREATE TABLE " +
			                                                MOOD_TABLE_NAME + " (" +
			                                                MOOD_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			                                                MOOD_COLUMN_DESCRIPTION + " TEXT NOT NULL UNIQUE, " +
			                                                MOOD_COLUMN_IMAGE + " TEXT)";
	
	private static final String[] MOOD_COLUMN_LIST = new String[]{MOOD_COLUMN_ID,
	                                                              MOOD_COLUMN_DESCRIPTION,
	                                                              MOOD_COLUMN_IMAGE};
	
	private static final String DROP_TABLE_MOOD = "DROP TABLE IF EXISTS " + MOOD_TABLE_NAME;
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
