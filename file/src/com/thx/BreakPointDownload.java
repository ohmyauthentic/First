package com.thx;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.file.R;
import com.thx.dao.Dao;
import com.thx.info.DownloadInfo;
import com.thx.servlet.Download;

public class BreakPointDownload extends Activity {
    Button down_star = null;
    Button down_stop = null;
    ProgressBar progressBar =null;
    String url ;
    String filepath;
    Download down;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.udfile);
        down_star = (Button) findViewById(R.id.play);
        down_stop = (Button) findViewById(R.id.play);
        progressBar = (ProgressBar) findViewById(R.id.load_progress);
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
        	try {
				filepath=Environment.getExternalStorageDirectory().getCanonicalFile()+File.separator+"my.mp3";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        url ="http://bgimg1.meimei22.com/list/2012-4-1/4/3.jpg";
        
        down_star.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				down= new Download(BreakPointDownload.this,url,filepath);
				down.execute(null);
				down.setProgressBar(progressBar);
			}
		});
        down_stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                down.setIsstop(true); 				
			}
		});
    }
    
    
    
}