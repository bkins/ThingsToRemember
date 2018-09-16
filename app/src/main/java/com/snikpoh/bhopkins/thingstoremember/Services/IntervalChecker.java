package com.snikpoh.bhopkins.thingstoremember.Services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class IntervalChecker extends IntentService
{
	public IntervalChecker()
	{
		super("IntervalChecker");
	}
	
	/**
	 * Creates an IntentService.  Invoked by your subclass's constructor.
	 *
	 * @param name Used to name the worker thread, important only for debugging.
	 */
	public IntervalChecker(String name)
	{
		super(name);
	}
	
	@Override
	protected void onHandleIntent(@Nullable Intent intent)
	{
	
	}
}
