package com.snikpoh.bhopkins.thingstoremember.Services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/*
*   I hope this is the proper implementation of what I am trying to accomplish:
*
*   This service is to be responsible for keeping the "online" database
*   in sync with the local database.
*
*   This service will have two main functions:
*
 *   1)  Check if databases are out of sync
 *   2)  *Sync databases
 *
*   * 2) will only be call if 1) is true
*
*   This is service will be initiated:
*
*   1)  at the start of the app
*   2)
*
 */
public class DatabaseSyncer extends IntentService
{
//	public DatabaseSyncer()
//	{
//		super("DatabaseSyncer");
//	}
//
	public DatabaseSyncer()
	{
		super("DatabaseSyncer");
	}
	
	@Override
	protected void onHandleIntent(@Nullable Intent intent)
	{
	
	}
}
