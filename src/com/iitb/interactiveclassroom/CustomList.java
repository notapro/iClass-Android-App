package com.iitb.interactiveclassroom;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<String>{
	
private final Activity context;
private final String[] web;
private final String[] rollweb;
//private final Integer[] imageId;

// Constructor 
public CustomList(Activity context,String[] web,String[] rollweb) {
super(context, R.layout.list_single, web);
this.context = context;
this.web = web;
this.rollweb=rollweb;
//this.imageId = imageId;

}


@Override
public View getView(int position, View view, ViewGroup parent) {
LayoutInflater inflater = context.getLayoutInflater();
View rowView= inflater.inflate(R.layout.list_single, null, true);

TextView nameinlist = (TextView) rowView.findViewById(R.id.namedy);
//TextView rollinlist = (TextView) rowView.findViewById(R.id.rolldy);
ImageView imageinlist = (ImageView) rowView.findViewById(R.id.dpdy);

nameinlist.setText(web[position]);
//rollinlist.setText(rollweb[position]);
//imageinlist.setImageResource(imageId[position]);
String mypath=Environment.getExternalStorageDirectory().toString()+"/AakashApp/";
BitmapFactory.Options options=new BitmapFactory.Options();
options.inSampleSize = 4;
Bitmap bmp = BitmapFactory.decodeFile(mypath+web[position]+"/dp.jpg",options);

imageinlist.setImageBitmap(bmp);


return rowView;
}

}