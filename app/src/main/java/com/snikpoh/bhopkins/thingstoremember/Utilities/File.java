package com.snikpoh.bhopkins.thingstoremember.Utilities;

import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.net.URI;

public class File extends java.io.File
{
	BufferedReader reader;
	BufferedWriter writer;
	
	AssetManager theAssetManager;
	
	public File(@NonNull String pathname)
	{
		super(pathname);
	}
	
	public File(String parent, @NonNull String child)
	{
		super(parent, child);
	}
	
	public File(java.io.File parent, @NonNull String child)
	{
		super(parent, child);
	}
	
	public File(@NonNull URI uri)
	{
		super(uri);
	}

//	public File(Context myContext)
//	{
//		super();
//		theAssetManager = myContext.getAssets();
//	}
	
	public void write(String fileName, String value)
	{
		try
		{
			//TODO fix the BufferedWrite initialization
			//writer = new BufferedWriter(new OutputStreamWriter(theAssetManager.open(fileName), "UTF-8"));
			writer.write(value);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeWriter();
		}
	}
	
	public String read(String fileName)
	{
		
		String line;
		String completeText = "";
		try
		{
			reader = new BufferedReader(new InputStreamReader(theAssetManager.open(fileName),
			                                                  "UTF-8"));
			while ((line = reader.readLine()) != null)
			{
				completeText += line;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeReader();
		}
		
		return completeText;
	}
	
	
	private void closeReader()
	{
		try
		{
			if (reader != null)
			{
				reader.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void closeWriter()
	{
		try
		{
			if (writer != null)
			{
				writer.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
