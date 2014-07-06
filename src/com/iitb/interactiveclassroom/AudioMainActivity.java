package com.iitb.interactiveclassroom;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AudioMainActivity extends Activity implements OnClickListener {
	static String topicname=null;
	static int imageflag=0;
	static int mainback=0;
	int textsent;
	static int pingflag=1;
	Socket s, socket;
	BufferedReader br;
	PrintWriter pw;
	final Context context = this;
	String serverResponse, option, textMsg, textSubject, macAddress,path;
	Intent confirm;
	ImageButton raiseHandAudio;
	ImageView dpaudio; 
	public static ArrayList<String> doubt=new ArrayList<String>();
	public static ArrayList<String> textMessage=new ArrayList<String>();
	EditText doubtText, doubtSubject;
	TextView hiname;
	public static TextView counter;
	static String macadd;
	Button sendDoubtText,viewHistory;;
	DataInputStream dis;
	DataOutputStream dos;
	ProgressBar bar;
	public static int count=5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_main_audiotext);
		init(); // INITIALIZING VARIABLES
		pingflag=1;
		viewHistory.setOnClickListener(this); //LISTENER FOR VIEW HISTORY BUTTON
		
		path=Environment.getExternalStorageDirectory().toString()+"/AakashApp/"+TestConnection.username;
		
		raiseHandAudio.setOnClickListener(this); // LISTENER FOR AUDIO DOUBT
													// BUTTON
		sendDoubtText.setOnClickListener(this); // LISTENER FOR TEXT DOUBT
												// BUTTON
	
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap bmp = BitmapFactory.decodeFile(path+"/dp.jpg",options);

		dpaudio.setImageBitmap(bmp);
		
		hiname.setText("  Hi "+TestConnection.username+" !!  ");
		
		
		Thread updater=new Thread (new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.e("UPDATER","Updater thread started");
				while (pingflag==1)
				{
					
					try{Thread.sleep(500);
					
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
					
						if(counter!=null)
						{
							counter.setText("Doubts Remaining : "+count );
						}
						
						
					}
				});
				
					}
					
					catch(Exception e){}
				
				
					
				}
				Log.e("UPDATER","Updater thread stopped");
				
			}
		});
		updater.start();
		
		
		
		
		
	
	}

	public void init() {
		raiseHandAudio = (ImageButton) findViewById(R.id.audio_doubt_button);
		doubtText = (EditText) findViewById(R.id.text_doubt_msg);
		sendDoubtText = (Button) findViewById(R.id.send_text_doubt);
		doubtSubject = (EditText) findViewById(R.id.doubt_subject);
		dpaudio=(ImageView)findViewById(R.id.dpaudio);
		hiname=(TextView)findViewById(R.id.hi_name);
		viewHistory = (Button) findViewById(R.id.view_history);
		counter=(TextView)findViewById(R.id.counter);
		bar=(ProgressBar)findViewById(R.id.progressBar2);
		bar.setVisibility(View.GONE);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
// Setting onclicks for options menu
@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	
	if(item.getItemId()==R.id.action_about)
	{
		Toast.makeText(getApplicationContext(),"App Developed By:\n i-Class Team :\n Kaushik Bhagwatkar \n Ankit Kumar \n Prakhar Sethi \n Lavish Kothari \n Adil Hussain \n Mohit Gurnani",Toast.LENGTH_LONG).show();
	}
	
	else if (item.getItemId()==R.id.action_settings){
		
		Intent iw=new Intent(AudioMainActivity.this,Settings.class);
		startActivity(iw);
		
		
		
	}
	
	else if (item.getItemId()==R.id.action_help){
		
		Intent iw=new Intent(AudioMainActivity.this,Help.class);
		startActivity(iw);
		
		
		
	}
	
else if (item.getItemId()==R.id.action_logout){
		
		/*Intent iw=new Intent(AudioMainActivity.this,Users.class);
		startActivity(iw);
		
		finish();
		*/
	pingflag=0;
	imageflag=0;
	Intent startMain = new Intent(AudioMainActivity.this,Users.class);
    startMain.addCategory(Intent.CATEGORY_HOME);
    //startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(startMain);
    Toast.makeText(getApplicationContext(), "Logged out Successfully", Toast.LENGTH_SHORT).show();
    finish();
	doubt.removeAll(doubt);
	textMessage.removeAll(textMessage);
	count=5;
		
	}
	
	return super.onOptionsItemSelected(item);
}

	

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.send_text_doubt:
			
			
			
			textMsg = doubtText.getText().toString(); // READ MESSAGE
			textSubject = doubtSubject.getText().toString(); // READ SUBJECT
			if (textMsg.isEmpty() || textSubject.isEmpty()) { // CHECK IF ANY OF
																// THE TWO FIELD
																// IS EMPTY
				Toast.makeText(AudioMainActivity.this, "All fields are mandatory",
						Toast.LENGTH_LONG).show();
			} else {
				
				if(count!=0)
				createDialog(); // CREATE A CONFIRMATION DIALOG
				
				else
				{
					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.kickdialog);
					dialog.setTitle("Wait...");
		 
					// set the custom dialog components - text, image and button
					TextView text = (TextView) dialog.findViewById(R.id.kicktext);
					text.setText("You Already have 5 doubts in queue....");
					Button dialogButton = (Button) dialog.findViewById(R.id.kickok);
					// if button is clicked, close the custom dialog
					dialogButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							
						}
					});
		 
					dialog.show();
				}
				
			}
			break;
		case R.id.view_history:
			try{
					Intent vh=new Intent(AudioMainActivity.this,ViewHistory.class);
				  
				  vh.putStringArrayListExtra("doubtt",doubt);
				  vh.putStringArrayListExtra("textMessage", textMessage);
					 
				for(int i=0;i<doubt.size();i++)
					{Log.d("mohit","testing"+doubt.get(i));
					Log.d("mohit","testing  "+textMessage.get(i));
					
					}
				  startActivity(vh);
				 }
				 catch(Exception e)
				 {
					 Log.d("mohit", e.toString()+"  Starting of the activity");
				 }
				  break;
			
		case R.id.audio_doubt_button:
			
			final EditText input = new EditText(this);
			final AlertDialog d = new AlertDialog.Builder(context)
	        .setView(input)
	        .setTitle("Topic")
	        .setMessage("Enter Topic :")
	        .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
	        .setNegativeButton(android.R.string.cancel, null)
	        .create();

	d.setOnShowListener(new DialogInterface.OnShowListener() {

	    @Override
	    public void onShow(DialogInterface dialog) {

	        Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
	        b.setOnClickListener(new View.OnClickListener() {

	            @Override
	            public void onClick(View view) {
	                // TODO Do something
	            	topicname = input.getText().toString();
	  			  if (!topicname.equals(""))
	  				{Log.e("Topic is",topicname);
	  				confirm = new Intent(AudioMainActivity.this, AudioDoubt.class); // START
	  				  confirm.addCategory(Intent.CATEGORY_HOME);
	  		           // startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	  		            confirm.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);															// SESSION
	  				startActivity(confirm);
	  				//finish();
	  			//Dismiss once everything is OK.
	                d.dismiss();
	  				
	  				} 
	  				
	  				else
	  				{
	  					Toast.makeText(getBaseContext(), "Please Fill the Topic Field", Toast.LENGTH_SHORT).show();
	  					
	  				}
	            	
	            	
	                
	            }
	        });
	    }
	});
			
			d.show();
			
			
			
			break;
		}
	}

	private void createDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("Submit this message?");
		builder.setMessage("Subject:- " + textSubject + "\nDoubt:- \n"
				+ textMsg);

		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss(); // DISMISS DIALOG IF NO IS CLICKED
			}
		});
		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Log.e("yes", "yes");
			//	count++;
				count=5-doubt.size();
				if(count<=5 && count>0)
					{	
				
				
				//Setting progressbar//
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						bar.setVisibility(View.VISIBLE);
						sendDoubtText.setVisibility(View.GONE);
						doubtSubject.setEnabled(false);
						doubtText.setEnabled(false);
						
						
						
					}
				});
				
				
				
						sendTextRequest();
						
					}
				else
					{counter.setText("Doubts Remaining : 0");
					}
					// SEND DOUBT IF YES IS CLICKED
				}});
		AlertDialog alert = builder.create(); // CREATE ALERT DIALOG
		alert.show();
		alert.setCanceledOnTouchOutside(false);
	}

	private void sendTextRequest() {
		
				//MESSAGE SENDING THREAD STARTED
		final Thread textingThread = new Thread (new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
						textsent=0;
						socket = new Socket(TestConnection.ip, TestConnection.port);

					// Log.e("ClientActivity", "C: Sending command.");
						dos=new DataOutputStream(socket.getOutputStream());
						dis=new DataInputStream(socket.getInputStream());
				 
						dos.writeUTF("DOUBT");
					
					
				    
				//  	if(TestConnection.username!=null)
						dos.writeUTF(TestConnection.username);
						dos.writeUTF(TestConnection.roll);
						macadd=getMacAddress();
						dos.writeUTF(macadd);
						
						dos.writeUTF(doubtSubject.getText().toString()); // SEND
																		// SUBJECT
						dos.writeUTF(doubtText.getText().toString()); // SEND
						
																// MESSAGE
						
						// Sending imageflag
						
						
						//Image sending
						if(imageflag!=1)
						{
							dos.writeUTF("send_image");
							Log.e("Image flag sent","send_image");
							
							imageflag=1;
						File filedp = new File(path+"/dp_th.jpg");
						sendFile(filedp);
						}
						
						else
						{dos.writeUTF("not_send_image");
						Log.e("Image flag sent","not_send_image");
						String ifdone =dis.readUTF();
						
						if(ifdone.equals("not_done"))
						{
							File filedp = new File(path+"/dp_th.jpg");
							sendFile(filedp);
							Log.e("ZABARDASTI","Image Sent");
						}
						
							
						}
						
						final String msgServer = dis.readUTF(); // RECEIVE
						Log.e("Confirmation", "msgServer="+msgServer);
														// CONFIRMATION
														// IF MESSAGE
														// RECEIVED BY
														// SERVER
				    	
						
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (msgServer.contains("received")) {
									Toast.makeText(AudioMainActivity.this, "Doubt Sent",
										Toast.LENGTH_SHORT).show();
									bar.setVisibility(View.GONE);
									sendDoubtText.setVisibility(View.VISIBLE);
									textsent=1;
									
									doubt.add(doubtSubject.getText().toString());
									textMessage.add(doubtText.getText().toString());
									count=5-doubt.size();
									counter.setText("Doubts Remaining : "+count);
									
									
								} 
								else {
								Toast.makeText(AudioMainActivity.this,
										"Server Error! Doubt not sent!",
										Toast.LENGTH_SHORT).show();
								}
								doubtSubject.setText("");
								doubtText.setText("");
								doubtSubject.setEnabled(true);
								doubtText.setEnabled(true);
							}
						});
						
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

			}
		});
		
		textingThread.start();
		
		Thread textTimer=new Thread (new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				long startingtime = System.currentTimeMillis();
				long endingtime = startingtime + 5*1000; // 60 seconds * 1000 ms/sec
				
				while (true)
				{
					if(textsent==1||System.currentTimeMillis() > endingtime)
					{
						if(textsent!=1)
						{
							textingThread.interrupt();
							Log.e("Texting thread","Thread Interrupted");
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
								
									bar.setVisibility(View.GONE);
									sendDoubtText.setVisibility(View.VISIBLE);
									Toast.makeText(getApplicationContext(), "Connection Error... Could not Send Doubt..", Toast.LENGTH_SHORT).show();
									doubtSubject.setEnabled(true);
									doubtText.setEnabled(true);
									
									
								}
							});
							
							
							
							
							
							break;
						}
						
						
						else
						{
							break;
						}
						
					}
			
					
					
				}
				
				
				
			}
		});
		
		textTimer.start();
		
		
		
		
		
		
		
		
	}
	
	
	
	
	public void sendFile(File file) throws IOException {
        FileInputStream fileIn = new FileInputStream(file);
        byte[] buf = new byte[Short.MAX_VALUE];
        int bytesRead;        
        while( (bytesRead = fileIn.read(buf)) != -1 ) {
            dos.writeShort(bytesRead);
            dos.write(buf,0,bytesRead);
        }
        dos.writeShort(-1);
        fileIn.close();
    }
	private String getMacAddress() {	//GET MAC ADDRESS ( WIFI CONNECTION NEEDED )
		// TODO Auto-generated method stub
		WifiManager wifiManager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wInfo = wifiManager.getConnectionInfo();
		return wInfo.getMacAddress();
	}
	
	
	
	 @Override
	    public void onBackPressed() {
	       
	      //  Toast.makeText(getApplicationContext(), "LogOut From Options To Go Back", Toast.LENGTH_SHORT).show();
		 DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        switch (which){
			        case DialogInterface.BUTTON_POSITIVE:
			            //Yes button clicked
			        	/*
			        	Intent iw=new Intent(AudioMainActivity.this,Users.class);
			    		startActivity(iw);
			    		
			    		finish();
			    		*/
			        	imageflag=0;
			        	pingflag=0;
			        	Intent startMain = new Intent(AudioMainActivity.this,Users.class);
			            startMain.addCategory(Intent.CATEGORY_HOME);
			           // startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			            startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			            startActivity(startMain);
			            Toast.makeText(getApplicationContext(), "Logged out Successfully", Toast.LENGTH_SHORT).show();
			            finish();
			        	

			        	doubt.removeAll(doubt);
			        	textMessage.removeAll(textMessage);
			        	count=5;
			            break;

			        case DialogInterface.BUTTON_NEGATIVE:
			            //No button clicked
			        	
			        	
			        	
			        	
			            break;
			        }
			    }
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure to LOGOUT and go back?").setPositiveButton("Yes", dialogClickListener)
			    .setNegativeButton("No", dialogClickListener).show();
		 
		 
		 
		 
		 
	    }
	
	 
	 @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.e("MainAudioText","onPausecalled");
		 mainback=1;
		 super.onPause();
	}
	 
	 @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		 Log.e("MainAudioText","onResumecalled");
		 mainback=0;
		 super.onResume();
	}
	 
	 @Override
		public void onConfigurationChanged(Configuration newConfig) {
			// TODO Auto-generated method stub
			super.onConfigurationChanged(newConfig);
		};

}

