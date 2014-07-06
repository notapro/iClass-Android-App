package com.iitb.interactiveclassroom;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ListAdapter extends ArrayAdapter<String> {
int count=0;	
private final Activity context2;
private final String[] doubt;
private final String[] textMessage;
Context cx;
Socket socket;
String macid;
String dbt,txt;
DataInputStream dis;
DataOutputStream dos;
//private final Integer[] imageId;
LinkedList<String> doubt1=new LinkedList<String>();
LinkedList<String> textMessage1=new LinkedList<String>();
ImageView delete;
static int position=0;
// Constructor 
public ListAdapter(Activity context,String[] doubt2,String[] textMessage2,Context cc) {
super(context, R.layout.row, doubt2);
this.context2 = context;
this.doubt = doubt2;
this.textMessage=textMessage2;
this.cx=cc;
//this.imageId = imageId;

}





@Override
public View getView(final int position, View view, ViewGroup parent) {
LayoutInflater inflater =  context2.getLayoutInflater();
View rowView= inflater.inflate(R.layout.row, null, true);

TextView doubtt = (TextView) rowView.findViewById(R.id.t1);
TextView txtMessage = (TextView) rowView.findViewById(R.id.t2);
delete=(ImageView) rowView.findViewById(R.id.delete);
//ImageView imageinlist = (ImageView) rowView.findViewById(R.id.t3);
try{
doubtt.setText(doubt[position]);
txtMessage.setText(textMessage[position]);
count++;
}
catch(Exception e)
{
Log.d("mohit", "count="+count);	
}//imageinlist.setImageResource(imageId[position]);
delete.setOnClickListener(new OnClickListener(){
	@Override
	public void onClick(View v) {

		final Context context=getContext();
			AlertDialog.Builder adb=new AlertDialog.Builder(context);
	        adb.setTitle("Delete?");
	        adb.setMessage("Are you sure you want to cancel this doubt?");

	        final int positionToRemove = position;
	        dbt=ViewHistory.doubt2.get(positionToRemove);
	        txt=ViewHistory.textMessage2.get(positionToRemove);
	        adb.setNegativeButton("Cancel", null);
	       
	        try{
	        adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	   Log.d("mohit","Reached successfully");
	        		
	   
	   
	   
	   
	   
	   

		new Thread() {

			public void run() {
				try {

       				socket = new Socket(TestConnection.ip, TestConnection.port);

					// Log.e("ClientActivity", "C: Sending command.");
						dos=new DataOutputStream(socket.getOutputStream());
						dis=new DataInputStream(socket.getInputStream());
				 
						dos.writeUTF("remove");
					
					
/*				    
				//  	if(TestConnection.username!=null)
						dos.writeUTF(TestConnection.username);
						Log.d("mohit", "uname="+TestConnection.username);
						//	if(TestConnection.roll!=null)
						dos.writeUTF(TestConnection.roll);
						Log.d("mohit", "roll="+TestConnection.roll);
				*/		
						//	if(TestConnection.macid!=null)
						dos.writeUTF(AudioMainActivity.macadd);
						
				    
						dos.writeUTF(dbt); // SEND
																		// SUBJECT
						dos.writeUTF(txt); // SEND
						

					}
					catch (Exception e) {
					e.printStackTrace();

					}
					finally {
					if (socket != null) {
						try {
							// close socket connection after using it
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}// End run

		}.start();		//MESSAGE SENDING THREAD STARTED
	
	   
	   
	   
	   
	   
	   
	   
	   try{
	            	
	            	
	            	ViewHistory.doubt2.remove(positionToRemove);
	            	ViewHistory.textMessage2.remove(positionToRemove);
	            	AudioMainActivity.doubt.remove(positionToRemove);
	            	AudioMainActivity.textMessage.remove(positionToRemove);
	            	AudioMainActivity.count=5-AudioMainActivity.doubt.size();
	            	Log.d("mohit","doubt size="+AudioMainActivity.doubt.size());
	            	Log.d("mohit","doubt count="+AudioMainActivity.count);
	            	
	            	//AudioMainActivity.count--;
	            	AudioMainActivity.counter.setText("Doubt remaining:"+(AudioMainActivity.count));
	            	ViewHistory.doubt=new String[ViewHistory.doubt2.size()];
	            	ViewHistory.textMessage=new String[ViewHistory.textMessage2.size()];
	            }
	            catch(Exception e)
	            {
	            	Log.d("mohit","Access problem");
	            }
	            try{
	            
	            for(int i=0;i<ViewHistory.doubt2.size();i++)
	                {   
	                	ViewHistory.doubt[i]=ViewHistory.doubt2.get(i);
	                	ViewHistory.textMessage[i]=ViewHistory.textMessage2.get(i);
	                	Log.d("mohit", "Inserting from arraylist into string array");
	                }
	            }
	            catch(Exception e)
	            {
	            	Log.d("mohit","for loop error");
	            }
	            
	            Log.d("mohit", "Reacheddddddddddddddd");
	            try{
	            	
	           Intent intr=new Intent("com.iitb.interactiveclassroom.VIEWHISTORY");
	      	   intr.putStringArrayListExtra("doubtt",ViewHistory.doubt2);
			   intr.putStringArrayListExtra("textMessage", ViewHistory.textMessage2);
			
	            intr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            cx.startActivity(intr);
	            
	            
	            Log.d("mohit","msg token sent");
	            

	            
	            context2.finish();
	            
	            }
	            catch(Exception e)
	            {
	            	Log.d("mohit", "abc:"+e);
	            }
	            
	            
	            
	        	  }});
	       
	        
	        }catch(Exception e){
	        	Log.d("mohit","calling problem");
	        }
	        adb.show();

    	
    	
    	
	        }
	
});


return rowView;
}



}