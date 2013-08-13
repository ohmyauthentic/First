package com.example.file;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public  class MyAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private Bitmap directory,file,exit,home;
    //存储文件名称
    private ArrayList<String> names = null;
    //存储文件路径
    private ArrayList<String> paths = null;
    //参数初始化
    public MyAdapter(Context context,ArrayList<String> na,ArrayList<String> pa){
        names = na;
        paths = pa;
        exit = BitmapFactory.decodeResource(context.getResources(),R.drawable.exit);
        home = BitmapFactory.decodeResource(context.getResources(),R.drawable.home);
        directory = BitmapFactory.decodeResource(context.getResources(),R.drawable.folder);
        file = BitmapFactory.decodeResource(context.getResources(),R.drawable.mailinglists);
        //缩小图片
        directory = small(directory,0.8f);
        file = small(file,0.8f);
        home= small(home,0.8f);
        exit= small(exit,0.8f);
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (null == convertView){
            convertView = inflater.inflate(R.layout.file, null);
            holder = new ViewHolder();
            holder.text = (TextView)convertView.findViewById(R.id.textView);
            holder.image = (ImageView)convertView.findViewById(R.id.imageView);
            
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        File f = new File(paths.get(position).toString());
        if (names.get(position).equals("@1")){
            holder.text.setText("根目录/");
            holder.image.setImageBitmap(home);
        }
        else if (names.get(position).equals("@2")){
            holder.text.setText("返回..");
            holder.image.setImageBitmap(exit);
        }
        else{
            holder.text.setText(f.getName());
            if (f.isDirectory()){
                holder.image.setImageBitmap(directory);
            }
            else if (f.isFile()){
                holder.image.setImageBitmap(file);
            }
            else{
                System.out.println(f.getName());
            }
        }
        return convertView;
    }
    private class ViewHolder{
        private TextView text;
        private ImageView image;
    }
    private Bitmap small(Bitmap map,float num){
        Matrix matrix = new Matrix();
        matrix.postScale(num, num);
        return Bitmap.createBitmap(map,0,0,map.getWidth(),map.getHeight(),matrix,true);
    }
}