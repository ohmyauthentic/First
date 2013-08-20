package com.thx.info;

public class DownloadInfo {
	  private int _id;
	  /**���ص���ʼ��*/
      private int startPoint;
      /**���صĽ�����*/
      private int endPoint;
      /**���صĵ�ַ*/
      private  String  url;
      /**�����ļ��Ĵ�С*/
      private int loadFileSize;
      /**�������ļ��Ĵ�С*/
      private int completeload;
	public DownloadInfo(int startPoint, int endPoint,int completeload,int loadFileSize,String url) {
		super();
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.url = url;
		this.loadFileSize = loadFileSize;
		this.completeload = completeload;
	}
	public int getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(int startPoint) {
		this.startPoint = startPoint;
	}
	public int getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(int endPoint) {
		this.endPoint = endPoint;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getLoadFileSize() {
		return loadFileSize;
	}
	public void setLoadFileSize(int loadFileSize) {
		this.loadFileSize = loadFileSize;
	}
	public int getCompleteload() {
		return completeload;
	}
	public void setCompleteload(int completeload) {
		this.completeload = completeload;
	}
	
	
      
      
}
