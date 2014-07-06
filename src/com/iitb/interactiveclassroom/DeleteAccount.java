package com.iitb.interactiveclassroom;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DeleteAccount extends Activity
{

	
	ListView listView;
	
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.delete_account);
		
		
		
	     // Get ListView object from xml
        listView = (ListView) findViewById(R.id.delete_list);
        
        // Defined Array values to show in ListView
        String[] values = new String[] { "Android List View", 
                                         "Adapter implementation",
                                         "Simple List View In Android",
                                         "Create List View Android", 
                                         "Android Example", 
                                         "List View Source Code", 
                                         "List View Array Adapter", 
                                         "Android Example List View" 
                                        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
          android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter); 
        
        // ListView Item Click Listener
        listView.setOnItemClickListener(new OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				// TODO Auto-generated method stub
				
				// ListView Clicked item index
	               int itemPosition     = position;
	               
	               // ListView Clicked item value
	               String  itemValue    = (String) listView.getItemAtPosition(position);
	                  
	                // Show Alert 
	                Toast.makeText(getApplicationContext(),
	                  "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
	                  .show();
				
			}

         }); 
		
		
		
		
	
	
}
	
	
	public void onBackPressed()
	{
	        
	        	
	        	Intent startMain = new Intent(DeleteAccount.this,Users.class);
	            startMain.addCategory(Intent.CATEGORY_HOME);
	            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(startMain);
	            finish();
	        	
	        
	}
	
	
}
