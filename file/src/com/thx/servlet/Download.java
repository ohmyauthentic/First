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
   
	/**�������ص�ַ*/
	private String urlstr;
	/**�������ݿ����*/
	private Dao dao ;
	/**����������Ϣ��һ��ʵ����*/
	private DownloadInfo downloadinfo;
	/**��������*/
	private HttpURLConnection hurlconn;
	/**�������ص��ļ�*/
	private File file;
	/**�ļ������·��*/
	private String filepath;
	/**��������ж�����״̬ʱ��ֹͣ*/
	private boolean isstop=false;
	
	private ProgressBar progressBar;
	
	public Download(Context context,String url,String filepath){
		this.urlstr = url;
		this.filepath = filepath;
		dao = new Dao(context);
	}
	/**
	 * �ж��ǲ��ǵ�һ�����������Դ
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
	 * ��һ�����ص�ʱ���ʼ���ļ�
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
	 * �����ļ����ݴ���ĵ�ַ���ж��ǲ��ǵ�һ����������ǵ�һ�����������������أ�������ǵ�һ������������ϴ��Ķϵ�����
	 * @param urlstr
	 * @return
	 */
	public boolean DownloadStart(String urlstr){
		if(isFirstDownload(urlstr)){//��һ������ʱ
			init();
			downloadinfo = new DownloadInfo(0,hurlconn.getContentLength(),0,hurlconn.getContentLength(),urlstr);
			firstDownload(hurlconn);
		}else{
			init();
			downloadinfo = dao.getInfos(urlstr);
			if(downloadinfo.getCompleteload()<downloadinfo.getLoadFileSize()){//�Ѿ����ص����������û����
				unfirstDownload(hurlconn);
			}else{//�Ѿ����ع�������������
				dao.deleteInfos(urlstr);
				if(file.exists()){
					//�������sd���Ĵ���ɾ��Ȩ��
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
	 * �ж��Ƿ�Ϊ��һ�����ظ��ļ�
	 * @param hurlconn 
	 * @param isstop  ����һ��boolean�����ж��Ƿ�Ҫ��ͣ����
	 * @return  �����������򷵻�true�������ͣ������쳣�򷵻�false
	 */
	public boolean firstDownload(HttpURLConnection hurlconn){
		try {
			// ���÷�Χ����ʽΪRange��bytes x-y;
			//hurlconn.setRequestProperty("Range", "bytes="+0+"-"+hurlconn.getContentLength());
			if(hurlconn.getResponseCode()== HttpURLConnection.HTTP_OK){
				 hurlconn.connect();
				 InputStream instream = hurlconn.getInputStream();
				 RandomAccessFile rasf = new RandomAccessFile(file, "rwd");
				 //���ַ����б׶��ʺ�С�ļ�����
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
	 * �ж��Ƿ�Ϊ�ǵ�һ�����ظ��ļ�
	 * @param hurlconn 
	 * @param isstop  ����һ��boolean�����ж��Ƿ�Ҫ��ͣ����
	 * @return  �����������򷵻�true�������ͣ������쳣�򷵻�false��Ĭ����Ϊfalse
	 */
	public boolean unfirstDownload(HttpURLConnection hurlconn){
		try {
			// ���÷�Χ����ʽΪRange��bytes x-y;
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












