package com.thx.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelp extends SQLiteOpenHelper {

	public MyDBHelp(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public MyDBHelp(Context context){
		super(context, "thx.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//db.execSQL("create table downs(id integer,name char)");
		db.execSQL("create table download_info(startpoint integer,endpoint integer,compeleteload integer,filesize integer,url char)");
	}
    
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
