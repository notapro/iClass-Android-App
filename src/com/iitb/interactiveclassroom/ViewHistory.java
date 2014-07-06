package com.iitb.interactiveclassroom;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

public class ViewHistory extends Activity {
static String[] doubt;
static String[] textMessage;
 static ListView list;
ScrollView parentScroll;
static ArrayList<String> doubt2=new ArrayList<String>();
static ArrayList<String> textMessage2=new ArrayList<String>();
 static ListAdapter adapter;
ImageView del;
static Context context;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    

	if (AudioMainActivity.count==5)
	{
		setContentView(R.layout.nohistory);
	}
	
    
    else
    {
    setContentView(R.layout.main);
    
//doubt2=getIntent().getExtras().getStringArrayList("doubt");
//textMessage2=getIntent().getExtras().getStringArrayList("textMessage");
 //   if(savedInstanceState==null){
   context=getBaseContext();
    Intent get = getIntent();  
    doubt2 = get.getStringArrayListExtra("doubtt"); 
    textMessage2=get.getStringArrayListExtra("textMessage");
    doubt=new String[doubt2.size()];
    textMessage=new String[textMessage2.size()];
    del=(ImageView)findViewById(R.id.delete);

    //savedInstanceState.putStringArrayList("doubt2", doubt2);
    //savedInstanceState.putStringArrayList("textMessage2", textMessage2);
    
    try{
    for(int i=0;i<doubt2.size();i++)
    {   
    	doubt[i]=doubt2.get(i);
    	textMessage[i]=textMessage2.get(i);
    	Log.d("mohit", "Inserting from arraylist into string array");
    	
        
    
    }
    }catch(Exception e){
     	
    	Log.d("mohit", "Inserting from arraylist into string array");
    	Log.d("mohit", doubt2.get(0)+"plus"+textMessage2.get(0));
        
    	
    }
    
    
    
    
    
    		// Setting listview
	try{
    		adapter = new ListAdapter(ViewHistory.this, doubt,textMessage,context);
			list=(ListView)findViewById(R.id.lv);
				
	}
  catch(Exception e)
  {
  	Log.d("mohit","List Adapter problem");
  }
try{
			list.setOnTouchListener(new View.OnTouchListener() {

              public boolean onTouch(View v, MotionEvent event) {
                 // Log.v("CHILD", "CHILD TOUCH");
                  // Disallow the touch request for parent scroll on touch of
                  // child view
                  v.getParent().requestDisallowInterceptTouchEvent(true);
                  return false;
              }
          });
			
}
  catch(Exception e)
  {
  	Log.d("mohit","onclick listeners");
  }
			
			
			
			
			
			////////////////////////
			
			
			



/** Setting the event listener for the delete button */





			// Giving pop up and validation
			
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	            @Override
	            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
	        ListAdapter.position=position;    	

	            }
	        });
			
			list.setAdapter(adapter);
			list.setScrollingCacheEnabled(false);

    }		
  }

public static void dofunction(){
	
            //	adapter=new ListAdapter(ViewHistory.this,doubt,textMessage);
	   			list.setAdapter(adapter);
	   			list.setScrollingCacheEnabled(false);

}
	
}
