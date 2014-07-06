package com.iitb.interactiveclassroom;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Currency;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AudioDoubt extends Activity implements OnClickListener {
	
	
	AudioSession as = null;
	String queue = "1",mypath;
	Socket s,client_speak;;
	BufferedReader br;
	PrintWriter pw;
	final Context context = this;
	TextView queuePos, status;
	ImageView cancel,startsp,minimize;
	Intent back;
    DataOutputStream dos,edos;
	ServerSocket recSocket;
	Handler uiHandler;
	int runningFlag=1;
	DataInputStream rdis;
	DataInputStream dis,edis;
	Socket sendingSocket,esocket;
	int cancelsent;
	int isbackground=0;
	int ispeak=0,textsent;
	
	

	EditText etopic;
	EditText etext;
	ProgressBar ebar; 
	Button esend,ecancelbut;

	
	
	@Override
	protected void onCreate(Bundle qsavedInstanceState) {
		super.onCreate(qsavedInstanceState);
		//Log.e("Starting", "audiodoubt");
		setContentView(R.layout.activity_audio_doubt);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		this.setFinishOnTouchOutside(false);
		uiHandler=new Handler();
		//Log.e("Started", "audiodoubt");
		
		
		// INITIALIZE VARIABLES
		queuePos = (TextView) findViewById(R.id.queue_pos);
		status = (TextView) findViewById(R.id.state);
		status.setText("Hello "+TestConnection.username+"!");
		cancel = (ImageView) findViewById(R.id.cancel_request);
		startsp = (ImageView) findViewById(R.id.start_speaking);
		minimize=(ImageView)findViewById(R.id.minimizeButton);
		startsp.setVisibility(View.GONE);
		//cancel.setOnClickListener(this);
		mypath=Environment.getExternalStorageDirectory().toString()+"/AakashApp/"+TestConnection.username;
		
		
		///// Minimize Onclick
		
		
		minimize.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					
				
				if(AudioMainActivity.count!=0)
					{
					// CREATE A CONFIRMATION DIALOG
										
					final Dialog emerdialog=new Dialog(AudioDoubt.this);
					emerdialog.setContentView(R.layout.emerdg);
					emerdialog.setCanceledOnTouchOutside(false);
					emerdialog.setTitle("Enter Text-Doubt");

					etopic=(EditText)emerdialog.findViewById(R.id.emertopic);
					etext=(EditText)emerdialog.findViewById(R.id.emertext);
					 ebar=(ProgressBar)emerdialog.findViewById(R.id.emerbar); 
					 ebar.setVisibility(View.GONE);
					esend=(Button)emerdialog.findViewById(R.id.ebutton);
					ecancelbut=(Button)emerdialog.findViewById(R.id.ecancel);
	
					
					try{
					
					ecancelbut.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
						emerdialog.dismiss();	
						}
					});
					}
					
					catch(Exception e)
					{
						Log.e("ecancel button",e.toString());
					}
					emerdialog.show();
					
					
					esend.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							

							
										
										String textSubject=etopic.getText().toString();
										String textMsg=etext.getText().toString();
										
										if (textMsg.isEmpty() || textSubject.isEmpty()) 
										{ // CHECK IF ANY OF
											// THE TWO FIELD
											// IS EMPTY
												Toast.makeText(AudioDoubt.this, "Fill all the Fields...",
												Toast.LENGTH_LONG).show();
										} 
										
										
										
										else 
										{
												
											AudioMainActivity.count=5-AudioMainActivity.doubt.size();
											
												
											//Setting progressbar//
											
											runOnUiThread(new Runnable() {
												
												@Override
												public void run() {
													// TODO Auto-generated method stub
													ebar.setVisibility(View.VISIBLE);
													esend.setVisibility(View.GONE);
													etopic.setEnabled(false);
													etext.setEnabled(false);
													
													
													
												}
											});
											
											
											
													sendTextRequest();
														
										}
							
						}

						private void sendTextRequest() {
							// TODO Auto-generated method stub
							

							
							//MESSAGE SENDING THREAD STARTED
					final Thread textingThread = new Thread (new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub

							try {
									textsent=0;
									esocket = new Socket(TestConnection.ip, TestConnection.port);

								// Log.e("ClientActivity", "C: Sending command.");
									edos=new DataOutputStream(esocket.getOutputStream());
									edis=new DataInputStream(esocket.getInputStream());
							 
									edos.writeUTF("DOUBT");
								
								
							    
							//  	if(TestConnection.username!=null)
									edos.writeUTF(TestConnection.username);
									edos.writeUTF(TestConnection.roll);
									
									edos.writeUTF(TestConnection.macid);
									
									edos.writeUTF(etopic.getText().toString()); // SEND
																					// SUBJECT
									edos.writeUTF(etext.getText().toString()); // SEND
									
																			// MESSAGE
									
									// Sending imageflag
									
									
									//Image sending
									if(AudioMainActivity.imageflag!=1)
									{
										edos.writeUTF("send_image");
										Log.e("Image flag sent","send_image");
										
										AudioMainActivity.imageflag=1;
									File filedp = new File(mypath+"/dp_th.jpg");
									sendFile(filedp);
									}
									
									else
									{edos.writeUTF("not_send_image");
									Log.e("Image flag sent","not_send_image");
									String ifdone =edis.readUTF();
									
									if(ifdone.equals("not_done"))
									{
										File filedp = new File(mypath+"/dp_th.jpg");
										sendFile(filedp);
										Log.e("ZABARDASTI","Image Sent");
									}
									
										
									}
									
									final String msgServer = edis.readUTF(); // RECEIVE
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
												Toast.makeText(AudioDoubt.this, "Doubt Sent",
													Toast.LENGTH_SHORT).show();
												emerdialog.dismiss();
												textsent=1;
												
												AudioMainActivity.doubt.add(etopic.getText().toString());
												AudioMainActivity.textMessage.add(etext.getText().toString());
												AudioMainActivity.count=5-AudioMainActivity.doubt.size();
												//counter.setText("Doubts Remaining : "+count);
												
												
											} 
											else {
											Toast.makeText(AudioDoubt.this,
													"Server Error! Doubt not sent!",
													Toast.LENGTH_SHORT).show();
											}
											//doubtSubject.setText("");
											//doubtText.setText("");
											etopic.setEnabled(true);
											etext.setEnabled(true);
										}
									});
									
								}
								catch (Exception e) {
								e.printStackTrace();

								}
								finally {
								if (esocket != null) {
									try {
										// close socket connection after using it
										esocket.close();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}

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
											
												ebar.setVisibility(View.GONE);
												esend.setVisibility(View.VISIBLE);
												Toast.makeText(getApplicationContext(), "Connection Error... Could not Send Doubt..", Toast.LENGTH_SHORT).show();
												etopic.setEnabled(true);
												etext.setEnabled(true);
												
												
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
					});
					
											
					
					
					
					
					

					
					}
					
					
					
					
							else
							{
									final Dialog dialog = new Dialog(context);
									dialog.setContentView(R.layout.kickdialog);
									dialog.setTitle("Wait...");
									
									// set the custom dialog components - text, image and button
									TextView text = (TextView) dialog.findViewById(R.id.kicktext);
									text.setText("You already have 5 doubts in queue....");
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
			
		});
		
		
		
		
	
		
		///////////////////////////////////////
		
		
		// RECEIVE STATUS FROM SERVER
		
		
		
		 Thread rt=new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub

					try{
						Log.d("Lavish","Receive Thread running");
												
						
						recSocket= new ServerSocket(5570);
						
					while(runningFlag==1){
							
							client_speak=recSocket.accept();
						
						
						
					 // connect to the server
					//final DataOutputStream rdos=new DataOutputStream(recSocket.getOutputStream());
						
							 rdis=new DataInputStream(client_speak.getInputStream());
							
						
							queue=rdis.readUTF();
							
					
					Log.e("I Got","Data:"+queue);
					//Toast.makeText(gh.getApplicationContext(), "Read kar liya", Toast.LENGTH_LONG).show();

					runOnUiThread(new Runnable() {
						@SuppressWarnings("deprecation")
						public void run() {
							
							
							if((queue.equals("start_speaking")))
							{		
									ispeak=1;	
										Log.e("Inside got ","Data:"+queue);
										queuePos.setVisibility(View.GONE);
										
										startsp.setVisibility(View.VISIBLE);
										
								
										/////// Notification starts//////////
									
										if(isbackground==1)//&&AudioMainActivity.mainback==0){
										{
											NotificationManager NM=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
										      
											Notification notify=new Notification(R.drawable.mic_logo_small,"Classroom Interaction",System.currentTimeMillis());
											notify.flags |= Notification.FLAG_AUTO_CANCEL;
											Intent nmintent=new Intent(getApplicationContext(),DummyActivity.class);
											//nmintent.setAction(Intent.ACTION_MAIN);
											//nmintent.addCategory(Intent.CATEGORY_LAUNCHER);
											nmintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
										      PendingIntent pending=PendingIntent.getActivity(getApplicationContext(),0, nmintent,0);
										      notify.setLatestEventInfo(getApplicationContext(),"Permission Granted","Click to start speaking...",pending);
										      NM.notify(0, notify);
											}
										 
										
										
										//////////Notification ends///////
										
							
							}
							
							else if(queue.equals("kick_you"))
							{	
								ispeak=0;
								Log.e("inside if", "inside if block");
								runningFlag=0;
								try {
									recSocket.close();
									Log.e("closing", "socket closed");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
									
									Log.e("Inside got ","Data:"+queue);
									
									if(as!=null)
			 						{as.onWithdrawPress();
									
									}
									
									
											
									final Dialog dialog = new Dialog(context);
									dialog.setContentView(R.layout.kickdialog);
									dialog.setTitle("OOOPS...");
						 
									// set the custom dialog components - text, image and button
									TextView text = (TextView) dialog.findViewById(R.id.kicktext);
									
									Button dialogButton = (Button) dialog.findViewById(R.id.kickok);
									// if button is clicked, close the custom dialog
									dialogButton.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											dialog.dismiss();
											finish();
										}
									});
						 
									dialog.show();
									
							
							}
							
							else
							{
								
								Log.e("inside else", "inside else block");
								
									// TODO Auto-generated method stub
									Log.e("Inside got ","Data:"+queue);
									queuePos.setText("Your position is "+queue);
									startsp.setVisibility(View.GONE);
										
							
							
								
							}
							
							
							
							
							
							
						}
					});
					
					
										
					
					}
					
					
					
					}
					
					catch(Exception e)
					{
						System.out.println(e.toString());
					}
					
					finally
					{
						Log.e("INSIDE FINALLY","HURRAYYYY");
						//finish();
					}
					
				}
			});
			rt.start();
				
				
			
			
			// Sending THREAD
				
				Thread t=new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						
						
					      
					      
						try{
							
							
								cancel.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stu
									runningFlag=0;
									if(as!=null)
			 						{as.onWithdrawPress();
									}
									finish();
									
									runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											
											cancel.setAlpha((float)0.5);
											
										}
									});
									
									
									final Thread cancellingThread = new Thread(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											cancelsent=0;
											try {
												Log.e("About to get out","before sending kickmeout");
													if(dos!=null)
													{
														
														if(ispeak==1)
														{ispeak=0;
															dos.writeUTF("kick_me_out_speaking");
															Log.e("Kick me","KICKED SPEAKING");
														}
														
														else
														{
													dos.writeUTF("kick_me_out_waiting");
													dos.writeUTF(TestConnection.macid);
													Log.e("Kick me","KICKED WAITING");
														}
													
													}
													cancelsent=1;
													
												try {
													recSocket.close();
													Log.e("closing", "socket closed");
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												
											
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											
										}
									});
									
									cancellingThread.start();
									
									
									Thread timerThread=new Thread(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											long startingtime = System.currentTimeMillis();
											long endingtime = startingtime + 3*1000; // 60 seconds * 1000 ms/sec
											
											while(true)
											{
													if(cancelsent==1 || System.currentTimeMillis() > endingtime)
													{
														if(cancelsent!=1){
														cancellingThread.interrupt();
														try {
															recSocket.close();
															Log.e("closing", "socket closed");
														} catch (IOException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
														}
														
														
														break;
													}
													
												
											}
											
										}
									});
									timerThread.start();
									
								
									
									
								}
							});
							
							
							
								sendingSocket= new Socket(TestConnection.ip,5566); // connect to the server
								dos=new DataOutputStream(sendingSocket.getOutputStream());
								dis=new DataInputStream(sendingSocket.getInputStream());
								
							
							 dos.writeUTF(TestConnection.username);
							 dos.writeUTF(TestConnection.roll);
							 dos.writeUTF(TestConnection.macid);
							 dos.writeUTF(AudioMainActivity.topicname);
							 
							 Log.e("Data","Data completely sent");
					
							 if(AudioMainActivity.imageflag!=1)
							 {
								 
								 AudioMainActivity.imageflag=1;
								 dos.writeUTF("send_image");
								 Log.e("Image flag sent","send_image");
							 //Image sending
								
							 		DataOutputStream fdos = new DataOutputStream(sendingSocket.getOutputStream());
							 		
									File filedp = new File(mypath+"/dp_th.jpg");
									
									 FileInputStream fileIn = new FileInputStream(filedp);
								        byte[] buf = new byte[Short.MAX_VALUE];
								        int bytesRead;        
								        while( (bytesRead = fileIn.read(buf)) != -1 ) {
								            fdos.writeShort(bytesRead);
								            fdos.write(buf,0,bytesRead);
								        }
								        fdos.writeShort(-1);
								        fileIn.close();
								        Log.e("Image","Image completely sent");
								
							 
							///// Image sending closed///
						
							 }
							 
							 else
							 {
								 dos.writeUTF("not_send_image");
								 Log.e("Image flag sent","not_send_image");
								 String ifdone=dis.readUTF();
								 
								 if(ifdone.equals("not_done"))
								 {
									 DataOutputStream fdos = new DataOutputStream(sendingSocket.getOutputStream());
										File filedp = new File(mypath+"/dp_th.jpg");
										
										 FileInputStream fileIn = new FileInputStream(filedp);
									        byte[] buf = new byte[Short.MAX_VALUE];
									        int bytesRead;        
									        while( (bytesRead = fileIn.read(buf)) != -1 ) {
									            fdos.writeShort(bytesRead);
									            fdos.write(buf,0,bytesRead);
									        }
									        fdos.writeShort(-1);
									        fileIn.close();
									        Log.e("Image in Audio","Zabardasti sent");
									
									 
								 }
								 
							 }
					
							
							
							
							}
							
							catch(Exception e)
							{
								
							}
						
						
						
						finally
						{
							
						}
								
							
						
					}
				});
					
					t.start();
					
				//}
					
					
				
				startsp.setOnClickListener(this);	//IF THIS PRESSED, USER CAN START SPEAKING
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.audio_doubt, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.start_speaking:
			Log.e("AudioSession", "AudioSession is Going to start");
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					queuePos.setVisibility(View.VISIBLE);
					queuePos.setText("AUDIO streaming...");
					startsp.setVisibility(View.GONE);
				}
			});
			
			
			
			
			
			as = new AudioSession();	//THIS OBJECT HANDLES ALL ASPECTS OF COMMUNICATION
			as.onRequestPress();

			Log.e(" AudioSession", "AudioSession Started");//START AUDIO MESSAGE
			break;
		
			
		}
	}

	
	@Override
	public void onBackPressed()
	{
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		  isbackground=1;
		super.onPause();
		Log.e("AudioDoubt","OnPause called");
		
		
		////// Code remove////
		
		///////Remove this
		
	
		
		
	};
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	};

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		isbackground=0;
		super.onResume();
		
	};
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e("DESTROYED","I M DESTROYED");
		
		
	}
	
}


