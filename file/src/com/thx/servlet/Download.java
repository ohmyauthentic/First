package com.thx.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.thx.dao.Dao;
import com.thx.info.DownloadInfo;

public class Download extends AsyncTask<String, Float, Boolean>{
   
	/**定义下载地址*/
	private String urlstr;
	/**操作数据库的类*/
	private Dao dao ;
	/**定义下载信息的一个实体类*/
	private DownloadInfo downloadinfo;
	/**网络连接*/
	private HttpURLConnection hurlconn;
	/**定义下载的文件*/
	private File file;
	/**文件保存的路径*/
	private String filepath;
	/**定义变量判断下载状态时候停止*/
	private boolean isstop=false;
	
	private ProgressBar progressBar;
	
	public Download(Context context,String url,String filepath){
		this.urlstr = url;
		this.filepath = filepath;
		dao = new Dao(context);
	}
	/**
	 * 判断是不是第一次下载这个资源
	 * @param url
	 * @return
	 */
	public boolean isFirstDownload(String url){
		if(dao.isDownload(url)){
			return false;
		}
		return true;
	}
	/**
	 * 第一次下载的时候初始化文件
	 */
	public void init(){
		try {
			URL url = new URL(urlstr);
			hurlconn = (HttpURLConnection) url.openConnection();
			hurlconn.setDoInput(true);
			hurlconn.setDoOutput(true);
			hurlconn.setUseCaches(false);
			hurlconn.setRequestMethod("GET");
			hurlconn.setConnectTimeout(20000);
			file = new File(filepath);
			if(!file.exists()){
				System.out.println("AAAAAA");
				file.createNewFile();
				RandomAccessFile raf = new RandomAccessFile(file, "rwd");
				raf.setLength(hurlconn.getContentLength());
				raf.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	/**
	 * 下载文件根据传入的地址来判读是不是第一次下载如果是第一此下载这样从新下载，入过不是第一次下载则根据上传的断点下载
	 * @param urlstr
	 * @return
	 */
	public boolean DownloadStart(String urlstr){
		if(isFirstDownload(urlstr)){//第一次下载时
			init();
			downloadinfo = new DownloadInfo(0,hurlconn.getContentLength(),0,hurlconn.getContentLength(),urlstr);
			firstDownload(hurlconn);
		}else{
			init();
			downloadinfo = dao.getInfos(urlstr);
			if(downloadinfo.getCompleteload()<downloadinfo.getLoadFileSize()){//已经下载的情况，但是没下完
				unfirstDownload(hurlconn);
			}else{//已经下载过并且下载完了
				dao.deleteInfos(urlstr);
				if(file.exists()){
					//必须添加sd卡的创建删除权限
					file.delete();
				}
				downloadinfo = new DownloadInfo(0,hurlconn.getContentLength(),0,hurlconn.getContentLength(),urlstr);
				firstDownload(hurlconn);
			}
		}
		hurlconn.disconnect();
		return true;
	}
	@Override
	protected void onPreExecute(){
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
	}
	
	@Override
	protected void onProgressUpdate(Float... values) {
		this.progressBar.setSecondaryProgress(values[0].intValue());
	}
	@Override
	protected Boolean doInBackground(String... params) {
		return DownloadStart(urlstr);
	}
	
	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
		this.progressBar.setProgress(0);
	}
	
	public void setIsstop(boolean isstop) {
		this.isstop = isstop;
	}
	/**
	 * 判断是否为第一次下载该文件
	 * @param hurlconn 
	 * @param isstop  传入一个boolean参数判断是否要暂停下载
	 * @return  如果下载完成则返回true，如果暂停或出现异常则返回false
	 */
	public boolean firstDownload(HttpURLConnection hurlconn){
		try {
			// 设置范围，格式为Range：bytes x-y;
			//hurlconn.setRequestProperty("Range", "bytes="+0+"-"+hurlconn.getContentLength());
			if(hurlconn.getResponseCode()== HttpURLConnection.HTTP_OK){
				 hurlconn.connect();
				 InputStream instream = hurlconn.getInputStream();
				 RandomAccessFile rasf = new RandomAccessFile(file, "rwd");
				 //这种方法有弊端适合小文件传输
				 byte[] b = new byte[1024];
				 int length =-1;
				 int completeload = downloadinfo.getCompleteload();
				 while((length=instream.read(b))!=-1){
					 rasf.seek(completeload);
					 completeload+= length;
					 rasf.write(b,0,length);
					 float completeloadfloat = (float)completeload;
					 publishProgress(completeloadfloat/downloadinfo.getLoadFileSize()*100);
					 downloadinfo.setCompleteload(completeload);
					 if(completeload==downloadinfo.getLoadFileSize()){
						 dao.saveInfos(downloadinfo);
						 return true;
					 }
					 if(isstop){
						 dao.saveInfos(downloadinfo);
						 return false;
					 }
				 }
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			hurlconn.disconnect();
		}
		return false;
	}
	/**
	 * 判断是否为非第一次下载该文件
	 * @param hurlconn 
	 * @param isstop  传入一个boolean参数判断是否要暂停下载
	 * @return  如果下载完成则返回true，如果暂停或出现异常则返回false，默返回为false
	 */
	public boolean unfirstDownload(HttpURLConnection hurlconn){
		try {
			// 设置范围，格式为Range：bytes x-y;
			hurlconn.setRequestProperty("Range", "bytes="+downloadinfo.getCompleteload()+"-"+downloadinfo.getEndPoint());
			hurlconn.connect();
			//if(hurlconn.getResponseCode()== HttpURLConnection.HTTP_OK){
				 InputStream instream = hurlconn.getInputStream();
				 RandomAccessFile rasf = new RandomAccessFile(file, "rwd");
				 rasf.seek(downloadinfo.getCompleteload());
				 byte[] b = new byte[1024];
				 int length =-1;
				 int completeload = downloadinfo.getCompleteload();
				 while((length=instream.read(b))!=-1){
					 
					 completeload+= length;
					 rasf.write(b,0,length);
					 float completeloadfloat = (float)completeload;
					 publishProgress(completeloadfloat/downloadinfo.getLoadFileSize()*100);
					 downloadinfo.setCompleteload(completeload);
					 if(completeload==downloadinfo.getLoadFileSize()){
						 dao.updateInfos(downloadinfo);
						 return true;
					 }
					 if(isstop){
						 dao.updateInfos(downloadinfo);
						 return false;
					 }
				 }
		//	}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	 
	
	
	
}












