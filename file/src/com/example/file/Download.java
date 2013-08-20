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
	        //����Ҫ��ϵͳ�Դ���ͼ��ȥ�� 
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
        // �̶����ص���Դ·��������������������ϵĵ�ַ
        private EditText URL;
        private Button button;
        // �̶�������ص����ֵ�·����SD��Ŀ¼��
        private static final String SD_PATH = "/mnt/sdcard/";
        // ��Ÿ���������
        private Map<String, Downloader> downloaders = new HashMap<String, Downloader>();
        // �������������Ӧ�Ľ�����
        private Map<String, ProgressBar> ProgressBars = new HashMap<String, ProgressBar>();
        /**
         * 31 * ������Ϣ���������ʱ���½����� 32
         */
        private Handler mHandler = new Handler() {
                public void handleMessage(Message msg) {
                        if (msg.what == 1) {
                                String url = (String) msg.obj;
                                int length = msg.arg1;
                                ProgressBar bar = ProgressBars.get(url);
                                if (bar != null) {
                                        // ���ý���������ȡ��length���ȸ���
                                        bar.incrementProgressBy(length);
                                        if (bar.getProgress() == bar.getMax()) {
                                                Toast.makeText(Download.this, "������ɣ�", 0).show();
                                                // ������ɺ��������������map�е��������
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

        // ��ʾlistView�������������������
       
        public void startDownload(View v) {
                // �õ�textView������
                LinearLayout layout = (LinearLayout) v.getParent();
                String urlstr = URL.getText().toString();
                String localfile = SD_PATH ;
                // ���������߳���Ϊ4����������Ϊ�˷������̶���
                int threadcount = 4;
                // ��ʼ��һ��downloader������
                Downloader downloader = downloaders.get(urlstr);
                if (downloader == null) {
                        downloader = new Downloader(urlstr, localfile, threadcount, this,
                                        mHandler);
                        downloaders.put(urlstr, downloader);
                }
                if (downloader.isdownloading())
                        return;
                // �õ�������Ϣ��ĸ�����ɼ���
                LoadInfo loadInfo = downloader.getDownloaderInfors();
                // ��ʾ������
                showProgress(loadInfo, urlstr, v);
                // ���÷�����ʼ����
                downloader.download();
        }

        /**
         * ��ʾ������
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
         * ��Ӧ��ͣ���ذ�ť�ĵ���¼�
         */
        public void pauseDownload(View v) {
                LinearLayout layout = (LinearLayout) v.getParent();
                
                String urlstr = URL.getText().toString();
                downloaders.get(urlstr).pause();
        }
}
