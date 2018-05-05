package com.snikpoh.bhopkins.thingstoremember.Utilities;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;

public class File
{
	BufferedReader reader;
	BufferedWriter writer;
	
	AssetManager theAssetManager;
	
	public File(Context myContext)
	{
		theAssetManager = myContext.getAssets();
	}
	
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
