package com.snikpoh.bhopkins.thingstoremember.Examples;

public class Person
{
	
	private String dob;
	private String phone;
	private String email;
	private String name;

	public Person(String dob, String phone, String email, String name)
	{
		this.dob = dob;
		this.phone = phone;
		this.email = email;
		this.name = name;
	}

	public String getDob()
	{
		return dob;
	}
	
	public void setDob(String dob)
	{
		this.dob = dob;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	
	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return "Person{" +
				       "name='" + name + '\'' +
				       ", dob='" + dob + '\'' +
				       '}';
	}
}
