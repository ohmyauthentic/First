package com.example.file;

/*import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

public class Download extends Activity{
	 private ExpandableListView elistview;
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        elistview = (ExpandableListView) findViewById(R.id.downed);                               
	        //这里要把系统自带的图标去掉 
	        elistview.setGroupIndicator(null); 
	        elistview.setAdapter(new MyElistAdapter(this)); 
	    }	 
}
*/


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import Db.*;

public class Download extends ListActivity {
        // 固定下载的资源路径，这里可以设置网络上的地址
        private EditText URL;
        private Button button;
        // 固定存放下载的音乐的路径：SD卡目录下
        private static final String SD_PATH = "/mnt/sdcard/";
        // 存放各个下载器
        private Map<String, Downloader> downloaders = new HashMap<String, Downloader>();
        // 存放与下载器对应的进度条
        private Map<String, ProgressBar> ProgressBars = new HashMap<String, ProgressBar>();
        /**
         * 31 * 利用消息处理机制适时更新进度条 32
         */
        private Handler mHandler = new Handler() {
                public void handleMessage(Message msg) {
                        if (msg.what == 1) {
                                String url = (String) msg.obj;
                                int length = msg.arg1;
                                ProgressBar bar = ProgressBars.get(url);
                                if (bar != null) {
                                        // 设置进度条按读取的length长度更新
                                        bar.incrementProgressBy(length);
                                        if (bar.getProgress() == bar.getMax()) {
                                                Toast.makeText(Download.this, "下载完成！", 0).show();
                                                // 下载完成后清除进度条并将map中的数据清空
                                                LinearLayout layout = (LinearLayout) bar.getParent();
                                                layout.removeView(bar);
                                                ProgressBars.remove(url);
                                                downloaders.get(url).delete(url);
                                                downloaders.get(url).reset();
                                                downloaders.remove(url);

                                        }
                                }
                        }
                }
        };

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.download);
                URL=(EditText)findViewById(R.id.editText_url);
               
        }

        // 显示listView，这里可以随便添加音乐
       
        public void startDownload(View v) {
                // 得到textView的内容
                LinearLayout layout = (LinearLayout) v.getParent();
                String urlstr = URL.getText().toString();
                String localfile = SD_PATH ;
                // 设置下载线程数为4，这里是我为了方便随便固定的
                int threadcount = 4;
                // 初始化一个downloader下载器
                Downloader downloader = downloaders.get(urlstr);
                if (downloader == null) {
                        downloader = new Downloader(urlstr, localfile, threadcount, this,
                                        mHandler);
                        downloaders.put(urlstr, downloader);
                }
                if (downloader.isdownloading())
                        return;
                // 得到下载信息类的个数组成集合
                LoadInfo loadInfo = downloader.getDownloaderInfors();
                // 显示进度条
                showProgress(loadInfo, urlstr, v);
                // 调用方法开始下载
                downloader.download();
        }

        /**
         * 显示进度条
         */
        private void showProgress(LoadInfo loadInfo, String url, View v) {
                ProgressBar bar = ProgressBars.get(url);
                if (bar == null) {
                        bar = new ProgressBar(this, null,
                                        android.R.attr.progressBarStyleHorizontal);
                        bar.setMax(loadInfo.getFileSize());
                        bar.setProgress(loadInfo.getComplete());
                        System.out.println(loadInfo.getFileSize()+"--"+loadInfo.getComplete());
                        ProgressBars.put(url, bar);
                        LinearLayout.LayoutParams params = new LayoutParams(
                                        LayoutParams.FILL_PARENT, 5);
                        ((LinearLayout) ((LinearLayout) v.getParent()).getParent())
                                        .addView(bar, params);
                }
        }

        /**
         * 响应暂停下载按钮的点击事件
         */
        public void pauseDownload(View v) {
                LinearLayout layout = (LinearLayout) v.getParent();
                
                String urlstr = URL.getText().toString();
                downloaders.get(urlstr).pause();
        }
}
