package com.iitb.interactiveclassroom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Settings extends Activity {

	InputStream in,in1,in2,in3;
	BufferedReader reader;
	String line,line1,line2,line3;
	TextView t1;
	EditText e1,e2,e3,e4;
	Button savebutton;
	String path;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
	// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.set);
		path=Environment.getExternalStorageDirectory().toString()+"/AakashApp/";
		File folder = new File(path);
		if(!(folder.exists()&&folder.isDirectory()) )
		{folder.mkdirs();
		} 
		  try {
			  
			  new File(folder, "config.txt");	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		
		try{
		in = new FileInputStream(path+"config.txt");
	    reader = new BufferedReader(new InputStreamReader(in));
	    line = reader.readLine();in.close();
		}
		catch(Exception e)
		{
			//Toast.makeText(getApplicationContext(), "Exception Occured..", Toast.LENGTH_SHORT).show();
		}
		
		
		
		
		
		e1=(EditText)findViewById(R.id.address);
		
		
		e1.setText(line);
		
		savebutton=(Button)findViewById(R.id.savebutton);
		
		savebutton.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				
				InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE); 

inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                           InputMethodManager.HIDE_NOT_ALWAYS);
				// TODO Auto-generated method stub
				
				String newadd = e1.getText().toString();
				
				
				PrintWriter writer,writer1,writer2,writer3;
				try {
					writer = new PrintWriter(path+"config.txt");
					writer.print(newadd);
					//writer1.print(newoffname);writer2.print(newoffdes);
					writer.close();
					//writer1.close();writer2.close();
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
				e1.setEnabled(false);
				TestConnection.ip=newadd;
				
				
				
				
				
			}
		});
		
		
		
		
		
		
		
		
	}
	
	

}
