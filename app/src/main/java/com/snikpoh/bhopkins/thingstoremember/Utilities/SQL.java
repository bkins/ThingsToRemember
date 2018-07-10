package com.snikpoh.bhopkins.thingstoremember.Utilities;

public class SQL
{
	public static final String SELECT          = " SELECT ";
	public static final  String SELECT_ALL      = SELECT + " * ";
	public static final  String SELECT_TOP      = SELECT + " TOP ";
	public static final  String SELECT_DESTINCT = SELECT + " DISTINCT ";
	public static final  String FROM            = " FROM ";
	public static final  String WHERE           = " WHERE ";
	public static final  String CREATE          = " CREATE ";
	public static final  String TABLE           = " TABLE ";
	public static final  String CREATE_TABLE    = CREATE + TABLE;
	public static final String DROP            = " DROP ";
	public static final String DROP_TABLE      = DROP + TABLE;
	public static final String IF_EXISTS        = " IF EXISTS ";
	public static final String ALTER            = " ALTER ";
	public static final String ADD              = " ADD ";
	public static final String COLUMN           = " COLUMN ";
	public static final String INSERT           = " INSERT ";
	public static final String INTO             = " INTO ";
	public static final String INSERT_INTO      = INSERT + INTO;
	public static final String UPDATE           = " UPDATE ";
	public static final String DELETE           = " DELETE ";
	public static final String VALUES           = " VALUES ";
	public static final String SET              = " SET ";
	public static final String JOIN             = " JOIN ";
	public static final String ON               = " ON ";
	public static final String OUTER            = " OUTER ";
	public static final String INNER            = " INNER ";
	public static final String LEFT             = " LEFT ";
	public static final String RIGHT            = " RIGHT ";
	public static final String INTEGER          = " INTEGER ";
	public static final String PRIMARY          = " PRIMARY ";
	public static final String KEY              = " KEY ";
	public static final String AUTOINCREMENT    = " AUTOINCREMENT ";
	public static final String TEXT             = " TEXT ";
	public static final String NOT              = " NOT ";
	public static final String NULL             = " NULL";
	public static final String UNIQUE           = " UNIQUE ";
	public static final String DESC             = " DESC ";
	public static final String ASC              = " ASC ";
	public static final String DATE_TYPE        = " INTEGER NOT NULL DEFAULT (strftime('%s','now')) ";
	
	public static String getSelectTop(String number)
	{
		return SELECT_TOP + number;
	}
	
}
