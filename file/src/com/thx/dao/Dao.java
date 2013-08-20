package com.thx.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.thx.db.MyDBHelp;
import com.thx.info.DownloadInfo;

public class Dao {
    MyDBHelp dbHelp ;

	public Dao(Context context) {
		this.dbHelp = new MyDBHelp(context);
		//dbHelp.getReadableDatabase();
	}
	
	public boolean isDownload(String url){
		SQLiteDatabase sqlitedb = dbHelp.getReadableDatabase();
		String sql = "select * from download_info where url=?";
		Cursor cursor = sqlitedb.rawQuery(sql, new String[]{url});
		if(cursor.moveToNext()){
			cursor.close();
			sqlitedb.close();
			return true;
		}
		cursor.close();
		sqlitedb.close();
		return false;
	}
	/**
	 * 保存下载的断点的信息
	 * @param info
	 */
	public void saveInfos(DownloadInfo info) {
        SQLiteDatabase database = dbHelp.getWritableDatabase();
            String sql = "insert into download_info(startpoint, endpoint,compeleteload,filesize,url) values (?,?,?,?,?)";
           Object[] bindArgs = { info.getStartPoint(),
                    info.getEndPoint(),info.getCompleteload(),info.getLoadFileSize(),info.getUrl() };
            database.execSQL(sql, bindArgs);
        database.close();
    }
	/**
	 * 得到下载的断点的信息
	 * @param urlstr
	 * @return
	 */
	 public DownloadInfo getInfos(String urlstr) {
         SQLiteDatabase database = dbHelp.getReadableDatabase();
         String sql = "select startpoint, endpoint,compeleteload,filesize,url from download_info where url=?";
         Cursor cursor = database.rawQuery(sql, new String[] { urlstr });
         if (cursor.moveToNext()) {
             DownloadInfo info = new DownloadInfo(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),cursor.getInt(3),cursor.getString(4));
             cursor.close();
             database.close();
             return info;
         }
         cursor.close();
         database.close();
         return null;
     }
	 /**
	  * 更新下载点的信息
	  * @param info
	  */
	 public void updateInfos(DownloadInfo info){
		 SQLiteDatabase database = dbHelp.getWritableDatabase();
		 String sql = "update download_info set compeleteload=? where url =?";
		 Object[] values = {info.getCompleteload(),info.getUrl()};
		 database.execSQL(sql, values);
		 database.close();
	 }
	 public void deleteInfos(String url){
		 SQLiteDatabase database = dbHelp.getWritableDatabase();
		 String sql = "delete from download_info where url=?";
		 Object[] vlaue = {url};
		 database.execSQL(sql,vlaue);
		 database.close();
	 }
    
    
}
