package com.iitb.interactiveclassroom;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class TestConnection extends Activity
{
	private CharSequence status = null;
	
	Button b;
	EditText e1,e2;
	String newadd;
	public static String ip;
	public static int port=5565;
	public static String sid,username,roll,macid;
	ImageView iv;
	String path;
	private Socket client;
	private PrintWriter printwriter;
	InputStream in,in1,in2,in3;
	BufferedReader reader;
	String line,line1,line2,line3;
	Socket socket = null;
	DataOutputStream dataOutputStream = null;
	DataInputStream dataInputStream = null;
	ProgressBar pgbar;
	
	StringBuffer br=new StringBuffer();  //For Appending purpose
	
	TextView textIn;
	
	
		@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
			// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.testnew);
		b=(Button)findViewById(R.id.testbutton);
		e1=(EditText)findViewById(R.id.iptest);
		e2=(EditText)findViewById(R.id.sidtest);
		pgbar=(ProgressBar)findViewById(R.id.progressBar1);
		pgbar.setVisibility(View.GONE);
		
		
		// Setting ip to previous saved
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
		
		e1.setText(line);
		
		
		
		//Getting username and roll from previous activity
		
		username = getIntent().getExtras().getString("username1");
		roll = getIntent().getExtras().getString("roll1");
		
		// getting macid 
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wInfo = wifiManager.getConnectionInfo();
		macid = wInfo.getMacAddress();
	
		// Setting dp on testconnection
		
		iv=(ImageView)findViewById(R.id.dpconnect);
		String mypath=Environment.getExternalStorageDirectory().toString()+"/AakashApp/";
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap bmp = BitmapFactory.decodeFile(mypath+username+"/dp.jpg",options);

		iv.setImageBitmap(bmp);
		TextView banner =(TextView)findViewById(R.id.banner);
		banner.setText("  Hi "+username+" !!  ");
		
		
	
		
		
		
		
		
		//Connect button
		
		
		
		b.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					InputMethodManager inputManager = (InputMethodManager)
		            getSystemService(Context.INPUT_METHOD_SERVICE); 
		
					inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);	
		
					
					ip = e1.getText().toString();
					sid = e2.getText().toString();
					b.setVisibility(View.GONE);
					pgbar.setVisibility(View.VISIBLE);
					Log.e("Yaha ","pahucha "+ip+sid);
					

					// Thread handling data writeUTF ,readUTF
					final Thread t=new Thread(new Runnable()
					{
						

						public void run()
						{
							try
							{
								Log.e("Thread Started","t.start() called");
								Socket server= new Socket(ip,5565); // connect to the server
							 
								DataOutputStream dos=new DataOutputStream(server.getOutputStream());
								DataInputStream dis=new DataInputStream(server.getInputStream());
							   
								 dos.writeUTF("USER");
								 
								String sss="1";
								String rec=dis.readUTF();
								if(sss.equals(rec))
								{ 
									dos.writeUTF(sid);
									status=dis.readUTF();
									Log.e("Status Read ","Status is "+status);
									server.close();
								}
								else
								{
									Log.e("Value of rec","Value is "+rec.toString());
								}
							}
							catch(Exception exp)
							{
								Log.e("Exception",""+exp.toString());
							}
							//
							
						}
					});
					t.start();
					
					
					///// Timer Management////
					
					Thread time = new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							long startingtime = System.currentTimeMillis();
							long endingtime = startingtime + 5*1000; // 60 seconds * 1000 ms/sec
							
							while (true)
							{
								
								
								if(status!=null || System.currentTimeMillis() > endingtime)
								{
									runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											b.setVisibility(View.VISIBLE);
											pgbar.setVisibility(View.GONE);
											t.interrupt();
											Log.e("Forcibly","Thread forcibly interrupted");
											
										}
									});
									
									/// Work to be done after 5 seconds//
									
									
////////////////////////////////////////////////////
									
		// Possible status values
		
		if (status==null)
		{
			//Toast.makeText(getApplicationContext(), "Please check your Connection and IP settings", Toast.LENGTH_LONG).show();
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
				
					AlertDialog.Builder builder = new AlertDialog.Builder(TestConnection.this);
					builder.setMessage("Please check your Connection and IP settings!")
					       .setCancelable(false)
					       .setNegativeButton("OK", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					                //do things
					        	   
					        	   
					           }
					       });
					AlertDialog alert = builder.create();
					alert.show();
					
					
					
					
				}
			});
			
		}
		
		
		else if (status.equals("0"))
		{
			//Toast.makeText(getApplicationContext(), "Session ID Mismatch. \nConnection Aborted.", Toast.LENGTH_LONG).show();
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					AlertDialog.Builder builder = new AlertDialog.Builder(TestConnection.this);
					builder.setMessage("Session ID Mismatch. \nConnection Aborted.")
					       .setCancelable(false)
					       .setNegativeButton("OK", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					                //do things
					        	   
					        	   
					           }
					       });
					AlertDialog alert = builder.create();
					alert.show();
				}
			});
		
		}
		
		else if (status.equals("1"))
		{
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "CONNECTION SUCCESSFUL...", Toast.LENGTH_LONG).show();
					
					//// Saving ip in file////
					newadd = e1.getText().toString();
					PrintWriter writer;
					try {
						writer = new PrintWriter(path+"config.txt");
						writer.print(newadd);
						Log.e("Newadd got is","ADD:"+newadd);
						
						writer.close();
						
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
				}
			});
			
			
			
			
			
			/////////////////
			
			
			Intent rc=new Intent(TestConnection.this,AudioMainActivity.class);
			rc.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(rc);
			finish();
			
			
		}
		
		else 
		{
			//Toast.makeText(getApplicationContext(), "Something Went Wrong...", Toast.LENGTH_LONG).show();
				runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					AlertDialog.Builder builder = new AlertDialog.Builder(TestConnection.this);
					builder.setMessage("Something Went Wrong...")
					       .setCancelable(false)
					       .setNegativeButton("OK", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					                //do things
					        	   
					        	   
					           }
					       });
					AlertDialog alert = builder.create();
					alert.show();
				}
			});
		}
		
		status=null;
		
		
		//////////////////////////////////////////////
										
									/// work after 5 secs ended
									break;	
								}
								
							}
							
							
						}
					});
					time.start();
					
					
				}
			});

	}
		
		
		
		
		  @Override
		    public void onBackPressed() {
		       
		        startActivity(new Intent(TestConnection.this,Users.class));
		        finish();
		    }
		  
		  
		  
		  
		
}
