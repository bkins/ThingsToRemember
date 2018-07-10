package com.snikpoh.bhopkins.thingstoremember.Database;

public class Journal
{
	//region Data Members
	
	private int    _id;
	private String _name;
	private String _type;
	
	//endregion Data Members
	
	//region Getters and Setters
	
	public int getId()
	{
		return _id;
	}
	
	public void setId(int id)
	{
		this._id = id;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public void setName(String name)
	{
		this._name = name;
	}
	
	public String getType()
	{
		return _type;
	}
	
	public void setType(String type)
	{
		this._type = type;
	}
	
	//endregion Getters and Setters
	
	//region Constructors
	
	public Journal()
	{
	
	}
	
	public Journal(String name)
	{
		this._name = name;
	}
	
	public Journal(String name, String type)
	{
		this._name = name;
		this._type = type;
	}
	
	public Journal(int id, String name, String type)
	{
		this._id = id;
		this._name = name;
		this._type = type;
	}
	
	//endregion Constructors
	
}
