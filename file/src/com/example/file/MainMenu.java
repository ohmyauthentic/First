package com.example.file;



import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class MainMenu extends Activity {
   public static MainMenu instance = null;
   private ViewPager TabPager;
   private ImageView mTab1,mTab2,mTab3,mTab4;
   private int zero = 0;
	private int currIndex = 0;
	LocalActivityManager manager = null;
	Context context = null;
	private int one;
	private int two;
	private int three;
   public void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.activity_main);
	   
	   context = MainMenu.this;
		manager = new LocalActivityManager(this , true);
		manager.dispatchCreate(savedInstanceState);
	   
	   
	   getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
       instance = this;
       TabPager= (ViewPager)findViewById(R.id.tabpager);
       TabPager.setOnPageChangeListener(new MyOnPageChangeListener());
       
       mTab1 = (ImageView) findViewById(R.id.fm);
       mTab2 = (ImageView) findViewById(R.id.down);
       mTab3 = (ImageView) findViewById(R.id.up);
       mTab4 = (ImageView) findViewById(R.id.wrench);
       mTab1.setOnClickListener(new MyOnClickListener(0));
       mTab2.setOnClickListener(new MyOnClickListener(1));
       mTab3.setOnClickListener(new MyOnClickListener(2));
       mTab4.setOnClickListener(new MyOnClickListener(3));
       Display currDisplay = getWindowManager().getDefaultDisplay();//获取屏幕当前分辨率
       int displayWidth = currDisplay.getWidth();
       int displayHeight = currDisplay.getHeight();
       one = displayWidth/4; //设置水平动画平移大小
       two = one*2;
       three = one*3;
       LayoutInflater mLi = LayoutInflater.from(this);
       View view2 = mLi.inflate(R.layout.download, null);
       View view3 = mLi.inflate(R.layout.upload, null);
       View view4 = mLi.inflate(R.layout.setting, null);
       
       final ArrayList<View> views = new ArrayList<View>();
       Intent intent= new Intent(context,FileM.class);
       Intent intent2= new Intent(context,Download.class);
       views.add(getView("",intent));
       views.add(getView("", intent2));
       views.add(view3);
       views.add(view4);     
		TabPager.setAdapter(new MyPagerAdapter(views));
		TabPager.setCurrentItem(0);
		
   }
   
	public class MyPagerAdapter extends PagerAdapter{
		List<View> list =  new ArrayList<View>();
		public MyPagerAdapter(ArrayList<View> list) {
			this.list = list;
		}

		@Override
		public void destroyItem(ViewGroup container, int position,
				Object object) {
			ViewPager pViewPager = ((ViewPager) container);
			pViewPager.removeView(list.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return list.size();
		}
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			ViewPager pViewPager = ((ViewPager) arg0);
			pViewPager.addView(list.get(arg1));
			return list.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}
   
   
   public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			TabPager.setCurrentItem(index);
		}
	};
	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	
	
   public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.folder_yellow));
				/*mTab1.setBackgroundColor(Color.parseColor("#000000"));
				mTab2.setBackgroundColor(0);
				mTab3.setBackgroundColor(0);
				mTab4.setBackgroundColor(0);*/
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.download2_white));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.upload2_white));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.wrench_white));
				}
				break;
			case 1:
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.download2_yellow));
				/*mTab2.setBackgroundColor(Color.parseColor("#000000"));
				mTab1.setBackgroundColor(0);
				mTab3.setBackgroundColor(0);
				mTab4.setBackgroundColor(0);*/
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.folder_white));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.upload2_white));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.wrench_white));
				}
				break;
			case 2:
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.upload2_yellow));
			/*	mTab3.setBackgroundColor(Color.parseColor("#000000"));
				mTab1.setBackgroundColor(0);
				mTab2.setBackgroundColor(0);
				mTab4.setBackgroundColor(0);*/
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.folder_white));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.download2_white));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.wrench_white));
				}
				break;
			case 3:
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.wrench_yellow));
				/*mTab4.setBackgroundColor(Color.parseColor("#000000"));
				mTab1.setBackgroundColor(0);
				mTab2.setBackgroundColor(0);
				mTab3.setBackgroundColor(0);*/
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, three, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.folder_white));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.download2_white));
				}
				else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.upload2_white));
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(150);
			
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
}
