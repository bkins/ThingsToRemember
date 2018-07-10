package com.snikpoh.bhopkins.thingstoremember.Database;

public class Mood
{
	//region Data Members
	
	private int    _id;
	private String _description;
	private String _image;
	
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
	
	public String getDescription()
	{
		return _description;
	}
	
	public void setDescription(String description)
	{
		this._description = description;
	}
	
	public String getImage()
	{
		return _image;
	}
	
	public void setImage(String image)
	{
		this._image = image;
	}
	
	//endregion Getters and Setters
	
	//region Constructors
	
	public Mood()
	{
	
	}
	
	public Mood(String description)
	{
		this._description = description;
	}
	
	public Mood(String description, String image)
	{
		
		this._description = description;
		this._image = image;
	}
	
	public Mood(int id, String description, String image)
	{
		
		this._id = id;
		this._description = description;
		this._image = image;
	}
	
	//endregion Constructors
}
