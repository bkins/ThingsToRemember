package com.snikpoh.bhopkins.thingstoremember.Database;

public class Entry
{
	//region Data Members
	
	private int    _id;
	private String _description;
	private String _entryDate;
	private String _journalId;
	private String _moodId;
	
	//endregion
	
	//region Getters and Setters
	
	public int getId()
	{
		return _id;
	}
	
	public void setId(int _id)
	{
		this._id = _id;
	}
	
	public String getDescription()
	{
		return _description;
	}
	
	public void setDescription(String description)
	{
		this._description = description;
	}
	
	public String getEntryDate()
	{
		return _entryDate;
	}
	
	public void setEntryDate(String entryDate)
	{
		this._entryDate = entryDate;
	}
	
	public String getJournalId()
	{
		return _journalId;
	}
	
	public void setJournalId(String journalId)
	{
		this._journalId = journalId;
	}
	
	public String getMoodId()
	{
		return _moodId;
	}
	
	public void setMoodId(String moodId)
	{
		this._moodId = moodId;
	}
	
	//endregion Getters and Setters
	
	//region Constructors
	
	public Entry()
	{
	
	}
	
	public Entry(String description, String entryDate, String journalId)
	{
		this._description = description;
		this._entryDate = entryDate;
		this._journalId = journalId;
	}
	
	public Entry(String description, String entryDate, String journalId, String moodId)
	{
		this._description = description;
		this._entryDate = entryDate;
		this._journalId = journalId;
		this._moodId = moodId;
	}
	
	public Entry(int id,
	             String description,
	             String entryDate,
	             String journalId,
	             String moodId)
	{
		this._id = id;
		this._description = description;
		this._entryDate = entryDate;
		this._journalId = journalId;
		this._moodId = moodId;
	}
	
	//endregion Constructors
	
}
