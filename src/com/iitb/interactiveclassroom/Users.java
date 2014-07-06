package com.iitb.interactiveclassroom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Users extends Activity {
	ListView list;
	int dgflag = 0, currentpos;
	Button forgetpw;
	ImageView newaccbutton;
	InputStream in, in1, in2, in3;
	AdapterContextMenuInfo info;
	
	BufferedReader reader, reader1, reader2;
	File[] users;
	String line, line1, line2, line3;
	String[] web;
	String[] rollweb;
	String[] passweb;
	String[] dobweb;
	List<String> state = new ArrayList<String>();
	List<String> rollstate = new ArrayList<String>();
	List<String> passstate = new ArrayList<String>();
	List<String> dobstate = new ArrayList<String>();

	CustomList adapter;
	
	String mypath, currentpass, currentusername, currentroll, currentdob;
	int statecount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.users);
		TextView noacc = (TextView) findViewById(R.id.heading);

		//
		try {
			mypath = Environment.getExternalStorageDirectory().toString()
					+ "/AakashApp/";

			int sd = 0;
			statecount = 0;
			users = new File(mypath).listFiles();

			// Storing all usernames in state and retriving password and roll
			// nos in same order
			for (int i = 0; i < users.length; i++) {
				// System.out.println(users[i]);
				if (users[i].isDirectory()) {
					state.add(users[i].getName());
					in = new FileInputStream(mypath + users[i].getName()
							+ "/roll.txt");
					in1 = new FileInputStream(mypath + users[i].getName()
							+ "/pass.txt");
					in2 = new FileInputStream(mypath + users[i].getName()
							+ "/dob.txt");
					reader = new BufferedReader(new InputStreamReader(in));
					reader1 = new BufferedReader(new InputStreamReader(in1));
					reader2 = new BufferedReader(new InputStreamReader(in2));
					line = reader.readLine();
					in.close();
					line1 = reader1.readLine();
					in1.close();
					line2 = reader2.readLine();
					in2.close();
					rollstate.add(line);
					passstate.add(line1);
					dobstate.add(line2);

					statecount++;
				}

			}

		} catch (Exception e) {
			// Toast.makeText(getApplicationContext(),
			// e.toString(),Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "No Accounts Created Yet",
					Toast.LENGTH_LONG).show();
			noacc.setText("NO ACCOUNTS");

		}

		if (users == null || users.length == 0) {
			Toast.makeText(getApplicationContext(), "No Accounts Created Yet",
					Toast.LENGTH_LONG).show();
			noacc.setText("NO ACCOUNTS");

		}

		// Converting arraylist to array

		web = state.toArray(new String[state.size()]);
		rollweb = rollstate.toArray(new String[rollstate.size()]);
		passweb = passstate.toArray(new String[passstate.size()]);
		dobweb = dobstate.toArray(new String[dobstate.size()]);

		newaccbutton = (ImageView) findViewById(R.id.createnewacc);

		// Register button
		newaccbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				v.setAlpha((float) 0.3);
				Intent it = new Intent(Users.this, Login.class);
				startActivity(it);
				// finish();

			}
		});

		// Setting listview
		adapter = new CustomList(Users.this, web, rollweb);
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setScrollingCacheEnabled(false);

		
		/*
		 * // Scrolling listview
		 * 
		 * parentScroll=(ScrollView)findViewById(R.id.scrollout);
		 * 
		 * parentScroll.setOnTouchListener(new View.OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { // TODO
		 * Auto-generated method stub findViewById(R.id.list).getParent()
		 * .requestDisallowInterceptTouchEvent(false); return false; } });
		 */

		// ///////////////List long click listener//////////////

		registerForContextMenu(list);

		
		  /*list.setOnItemLongClickListener(new
		  AdapterView.OnItemLongClickListener() {
		  
		  @Override public boolean onItemLongClick(AdapterView<?> av, View v,
		  int pos, long id) { // return onLongListItemClick(v,pos,id);
		  
		  
		  Toast.makeText(getApplicationContext(),
		  "LONGGG CLICK"+pos,Toast.LENGTH_SHORT).show();
		  
		  
		  
		  return true;
		  
		  } });*/
		  
		  
		 /* lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		 
		  @Override public void onItemClick(AdapterView<?> av, View v, int pos,
		 long id) { //onListItemClick(v,pos,id); } });
*/		 

		// ////////////////////////Long click ends here////////////////

		// SWIPE TO DELETE CODE 
		
		SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        list,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                	
                                	deleteUser(position);
                                	//adapter.remove(adapter.getItem(position));                                
                                }
                                //adapter.notifyDataSetChanged();
                            }
                        });
		
        list.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        list.setOnScrollListener(touchListener.makeScrollListener());
        
        
	/*	list.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// Log.v("CHILD", "CHILD TOUCH");
				// Disallow the touch request for parent scroll on touch of
				// child view
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});*/

		// //////////////////////

		// Giving pop up and validation

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				currentpass = passweb[position];
				currentroll = rollweb[position];
				currentusername = web[position];
				currentdob = dobweb[position];
				dgflag = 0;

				// ////////////////////////New
				// Dialog/////////////////////////////////

				final Dialog dialog = new Dialog(Users.this);
				dialog.setContentView(R.layout.dialog);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setTitle("Enter Password");

				// set the custom dialog components - text, image and button
				final TextView textd = (TextView) dialog
						.findViewById(R.id.dgpasstext);
				final EditText passd = (EditText) dialog
						.findViewById(R.id.dgpassedit);
				passd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				
				final TextView dobt = (TextView) dialog
						.findViewById(R.id.dobtext);
				final TextView dobe = (TextView) dialog.findViewById(R.id.dob);
				// EditText passd = (EditText) dialog.findViewById(R.id.dget);
				final TextView fpass = (TextView) dialog
						.findViewById(R.id.forgetpassdg);
				// fpass.setVisibility(View.GONE);
				dobt.setVisibility(View.GONE);
				dobe.setVisibility(View.GONE);

				fpass.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dobt.setVisibility(View.VISIBLE);
						dobe.setVisibility(View.VISIBLE);
						passd.setInputType(InputType.TYPE_CLASS_TEXT );
						dialog.setTitle("Forgot Password");
						dobt.setText("Date Of Birth");
						textd.setText("Roll No.");
						passd.setText("");
						dgflag = 1;
						fpass.setVisibility(View.GONE);

					}
				});

				textd.setText("Password");
				// ImageView image = (ImageView)
				// dialog.findViewById(R.id.image);
				// image.setImageResource(R.drawable.ic_launcher);

				Button dialogButton = (Button) dialog.findViewById(R.id.dgbut);
				Button cancelButton = (Button) dialog
						.findViewById(R.id.canceldg);

				// ////////// Date Picker////////////////////

				final Calendar myCalendar = Calendar.getInstance();

				final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						myCalendar.set(Calendar.YEAR, year);
						myCalendar.set(Calendar.MONTH, monthOfYear);
						myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						String myFormat = "MM/dd/yy"; // In which you need put
														// here
						SimpleDateFormat sdf = new SimpleDateFormat(myFormat,
								Locale.US);

						dobe.setText(sdf.format(myCalendar.getTime()));
					}

				};

				dobe.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new DatePickerDialog(Users.this, date, myCalendar
								.get(Calendar.YEAR), myCalendar
								.get(Calendar.MONTH), myCalendar
								.get(Calendar.DAY_OF_MONTH)).show();

					}
				});

				// ///////////////////Date picker ended//////////////////////

				// if button is clicked, close the custom dialog
				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						if (dgflag == 0)// In passchecking mode
						{

							String value1 = passd.getText().toString();

							// Checking password
							if (value1.equals(currentpass)) {
								Toast.makeText(getApplicationContext(),
										"SUCCESSFUL LOGIN", Toast.LENGTH_SHORT)
										.show();
								Intent tc = new Intent(Users.this,
										TestConnection.class);
								tc.putExtra("username1", currentusername);
								tc.putExtra("roll1", currentroll);
								dialog.dismiss();
								startActivity(tc);
								finish();

							}

							else {

								Toast.makeText(getApplicationContext(),
										"Authentication Failure",
										Toast.LENGTH_SHORT).show();

							}

						}

						else if (dgflag == 1)// In roll and dob checking mode
						{
							String value1 = passd.getText().toString();
							String value2 = dobe.getText().toString();

							if (value1.equals(currentroll)
									&& value2.equals(currentdob)) {

								dgflag = 2;
								dialog.setTitle("Reset Password");
								dobt.setVisibility(View.GONE);
								dobe.setVisibility(View.GONE);
								textd.setText("Enter New Password");
								passd.setText("");
							}

							else {
								Log.d("my", currentroll + "\n" + value1 + "\n"
										+ currentdob + "\n" + value2);
								Toast.makeText(getApplicationContext(),
										"Wrong Credentials...",
										Toast.LENGTH_SHORT).show();

							}

						}

						else if (dgflag == 2)// Password resetting mode
						{
							String value1 = passd.getText().toString();

							if (!(value1.equals(""))) {
								dialog.dismiss();
								Toast.makeText(getApplicationContext(),
										"Password Changed Successfully",
										Toast.LENGTH_SHORT).show();
								// /////////// File
								// management///////////////////

								File filedelete = new File(mypath
										+ currentusername + "/pass.txt");
								filedelete.delete();

								File folder11 = new File(mypath
										+ currentusername + "/");
								folder11.mkdirs();

								try {

									PrintWriter writer11;
									new File(folder11, "pass.txt");

									writer11 = new PrintWriter(mypath
											+ currentusername + "/"
											+ "pass.txt");
									writer11.print(passd.getText().toString());

									writer11.close();
									passweb[currentpos] = passd.getText()
											.toString();

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								// /////////////////////////////////////////////////

							}

							else

							{
								Toast.makeText(getApplicationContext(),
										"Field is Empty", Toast.LENGTH_SHORT)
										.show();

							}

						}

					}
				});

				cancelButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						dgflag = 0;

					}
				});

				dialog.show();

				// ///////////////////////New Dialog
				// end///////////////////////////////////

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mainfm, menu);
		return true;
	}

	// Setting onclicks for options menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		if (item.getItemId() == R.id.action_aboutfm) {
			Toast.makeText(
					getApplicationContext(),
					"App Developed By:\n i-Class Team :\n Kaushik Bhagwatkar \n Ankit Kumar \n Prakhar Sethi \n Lavish Kothari \n Adil Hussain \n Mohit Gurnani",
					Toast.LENGTH_LONG).show();
		}

		// DELETE OPTION in menu ----> remove
		/*else if (item.getItemId() == R.id.action_remove) {

			Intent iw = new Intent(Users.this, DeleteAccount.class);
			iw.putExtra("users", web);
			startActivity(iw);
			finish();

		}*/

		else if (item.getItemId() == R.id.action_helpfm) {

			Intent iw = new Intent(Users.this, Help.class);
			startActivity(iw);
		}

		else if (item.getItemId() == R.id.action_exit) {
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(startMain);
			finish();

		}

		return super.onOptionsItemSelected(item);
	}

	// Double backpress exit
	private static long back_pressed;

	@Override
	public void onBackPressed() {
		if (back_pressed + 2000 > System.currentTimeMillis()) {

			/*
			 * Intent startMain = new Intent(Intent.ACTION_MAIN);
			 * startMain.addCategory(Intent.CATEGORY_HOME);
			 * startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 * startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 * startActivity(startMain);
			 */
			finish();

		} else
			Toast.makeText(getBaseContext(), "Press back once again to exit!",
					Toast.LENGTH_SHORT).show();
		back_pressed = System.currentTimeMillis();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.list) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.contextmenu, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.deletecontext:
			// add stuff here

			deleteUser(currentpos);
			
			

			return true;

		case R.id.passcontext:
			// /////

			Toast.makeText(
					getApplicationContext(),
					"Click on Forgot Password while logging in to change password...",
					Toast.LENGTH_SHORT).show();

			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}

	private void deleteUser(final int pos) {
		// TODO Auto-generated method stub
		AlertDialog.Builder alert = new AlertDialog.Builder(Users.this);

		alert.setTitle("Enter password to delete account");
		alert.setMessage("Password: ");
		
		// Set an EditText view to get user input
		final EditText input = new EditText(Users.this);
		input.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		alert.setView(input);

		alert.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						String value = input.getText().toString();

						// Checking password
						if (value.equals(passweb[pos])) {

							// //////////////////////////
							Toast.makeText(
									getBaseContext(),
									"Successfully Deleted : "
											+ web[pos],
									Toast.LENGTH_SHORT).show();
							File dir = new File(mypath + web[pos]);

							if (dir.isDirectory()) {
								DeleteRecursive(dir);
							}

							Intent iu = new Intent(Users.this, Users.class);
							iu.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
							startActivity(iu);
							overridePendingTransition(0, 0);
							finish();
							

						}

						else {
							Toast.makeText(getApplicationContext(),
									"Incorrect password.", Toast.LENGTH_SHORT).show();
						}

					}
				});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						// Canceled.
					}
				});

		
		alert.show();
	}

	void DeleteRecursive(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				DeleteRecursive(child);

		fileOrDirectory.delete();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		newaccbutton.setAlpha((float) 1.0);
	}

	
}
