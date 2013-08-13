package com.example.file;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyElistAdapter extends BaseExpandableListAdapter {                                                                                       
    // 分组数据
   private String[] group = { "正在下载", "已下载"}; 
   private String[][] child = { { "A01", "A02", "A03" }, 
           { "B01", "B02", "B03" }, { "C01", "C02", "C03" }, 
           { "D04", "D05", "D06" } }; 
   private Context mContext; 

   public MyElistAdapter(Context mContext) { 
           super(); 
           this.mContext = mContext; 
   } 

   @Override
   public int getGroupCount() { 
       return group.length; 
   } 

   @Override
   public int getChildrenCount(int groupPosition) { 
           return child[groupPosition].length; 
   } 

   @Override
   public Object getGroup(int groupPosition) { 
           return group[groupPosition]; 
   } 

   @Override
   public Object getChild(int groupPosition, int childPosition) { 
           return child[groupPosition][childPosition]; 
   } 

   @Override
   public long getGroupId(int groupPosition) { 
           return groupPosition; 
   } 

   @Override
   public long getChildId(int groupPosition, int childPosition) { 
           return childPosition; 
   } 

   @Override
   public boolean hasStableIds() { 
           return true; 
   } 

   @Override
   public View getGroupView(int groupPosition, boolean isExpanded, 
   View convertView, ViewGroup parent) { 
           // 实例化布局文件
           RelativeLayout glayout = (RelativeLayout) LayoutInflater.from(mContext)     
           .inflate(R.layout.group_layout, null); 
            ImageView iv = (ImageView) glayout.findViewById(R.id.giv); 
            // 判断分组是否展开，分别传入不同的图片资源
           if (isExpanded) { 
                   iv.setImageResource(R.drawable.arrowdown2); 
           } else { 
                   iv.setImageResource(R.drawable.arrowright2); 
           } 
          TextView tv = (TextView) glayout.findViewById(R.id.gtv); 
           tv.setText(this.getGroup(groupPosition).toString()); 
           return glayout; 
    } 

    @Override
   public View getChildView(int groupPosition, int childPosition, 
   boolean isLastChild, View convertView, ViewGroup parent) { 
           // 实例化布局文件
           LinearLayout clayout = (LinearLayout) LayoutInflater.from(mContext)              
           .inflate(R.layout.file, null); 
           TextView tv = (TextView) clayout.findViewById(R.id.textView); 
           tv.setText(getChild(groupPosition, childPosition).toString()); 
           return clayout; 
   } 

   @Override
   public boolean isChildSelectable(int groupPosition, int childPosition) { 
           return true; 
   } 
}
