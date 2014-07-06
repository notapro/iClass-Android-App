package com.iitb.interactiveclassroom;

import static android.media.MediaRecorder.AudioSource.VOICE_COMMUNICATION;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.regex.Pattern;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.util.Log;

public class AudioSession {
	static {
		System.loadLibrary("speex");
	}

	private native void Java_speex_EchoCanceller_open(int jSampleRate,
			int jBufSize, int jTotalSize);

	private native short[] Java_speex_EchoCanceller_process(
			short[] input_frame, short[] echo_frame);

	private native void Java_speex_EchoCanceller_close();

	private boolean isRecording = false;
	public AudioRecord recorder;
	private int port = 50005;
	private int sampleRate = AudioTrack
			.getNativeOutputSampleRate(VOICE_COMMUNICATION);
	private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
	private int encodingFormat = AudioFormat.ENCODING_PCM_16BIT;
	int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig,
			encodingFormat);
	int bufferSize = 0;
	public String ipAddress = TestConnection.ip;
	private static final String IPV4_NUM = "([01]?\\d\\d?|2[0-4]\\d|25[0-5])"; // REGULAR
																				// EXPRESSION
																				// IP

	private static final String IP_DOT = "\\.";
	private static final String IPV4_PATTERN = "^" + IPV4_NUM + IP_DOT
			+ IPV4_NUM + IP_DOT + IPV4_NUM + IP_DOT + IPV4_NUM + "$";
	public static final String PERMISSION_TEXT = "You may start talking";
	DatagramSocket socket=null, socket1=null, socket2=null, socket3=null;

	public void stopStreaming() { // TO STOP AUDIO RECORDER BY RELEASING IT
		if (recorder != null) {
			recorder.stop();
			recorder.release();
			recorder = null;
		}
		
	}

	public void startStreaming() { // START AUDIO RECORDING

		if (!isValidIPAddressAndPort(ipAddress, port))
			return; // CHECK FOR VALID IP AND PORT
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					short[] recordedShorts, filteredShorts;
					byte[] audioBytes;

					if(socket!=null)
					{
						socket.close();
						socket=null;
					}
					
					socket = new DatagramSocket(); // INITIALIZE SOCKET TO READ
													// PACKETS
					Log.e("minBufSize", minBufSize + "");
					DatagramPacket packet;
					final InetAddress destination = InetAddress
							.getByName(ipAddress);
					Log.e("before recorder", "about to initialize");
					recorder = new AudioRecord(VOICE_COMMUNICATION, sampleRate,
							channelConfig, encodingFormat, minBufSize * 10); // INITIALIZE

					Java_speex_EchoCanceller_open(sampleRate, minBufSize / 2,
							10*sampleRate/100);

					// RECORDER
					// test starts
					// test ends
					// recvShorts = new short[minBufSize/2];

					if (recorder.getState() == AudioRecord.STATE_INITIALIZED) // CHECK IF
					{

						// RECORDER

						recorder.startRecording();
						// bufferSize = recorder.read(recordedShorts, 0,
						// recordedShorts.length);

						int value = 0;

						recordedShorts = new short[minBufSize / 2];
						audioBytes = new byte[minBufSize];

						while (isRecording) { // KEEP ON RECORDING IN PARALLEL

							// UNTILL STOP STREAMING IS CALLED
							/*
							 * recorder.read(recvShorts, 0,
							 * 
							 * recvShorts.length); filteredShorts =
							 * 
							 * Java_speex_EchoCanceller_process(recordedShorts,
							 * recvShorts); audioBytes = new byte
							 * 
							 * [bufferSize*2]; ByteBuffer.wrap
							 * 
							 * (audioBytes).order(ByteOrder.LITTLE_ENDIAN).
							 * asShortBuffer ().put(filteredShorts); packet =
							 * new DatagramPacket
							 * 
							 * (audioBytes, audioBytes.length, destination,
							 * 
							 * port); socket.send(packet);
							 */

							recorder.read(recordedShorts, 0,
									recordedShorts.length);

				//			if (!recordedShorts.equals(null)
				//					&& recordedShorts.length > 100) {
								filteredShorts = Java_speex_EchoCanceller_process(
										recordedShorts, recordedShorts);
								Log.e("Filtered shorts", filteredShorts.length
										+ "");
								ByteBuffer.wrap(audioBytes)
										.order(ByteOrder.LITTLE_ENDIAN)
										.asShortBuffer().put(filteredShorts);
								packet = new DatagramPacket(audioBytes,
										audioBytes.length, destination, port);
								socket.send(packet);
								Log.e("" + value, audioBytes.length + "");

				//			}

							// if(toggler==0){
							// toggler=1;
							if (value > 100) {

								recorder.release();
								recorder = null;
								recorder = new AudioRecord(VOICE_COMMUNICATION,
										sampleRate, channelConfig,
										encodingFormat, minBufSize * 10);
								recorder.startRecording();
								Log.e("Recorder Status",
										"Recorder released and reset");

								System.gc();

								value = 0;
							}
							// }//end outer if
							value++;

						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					socket.close();
					socket=null;
				}
			}
		}).start();
	}

	private boolean isValidIPAddressAndPort(String ipAddress, int port) {
		if (!Pattern.compile(IPV4_PATTERN).matcher(ipAddress).matches())

		{
			// Toast.makeText(this,"IP Address is invalid",Toast.LENGTH_SHORT).show();
			return isRecording = false;
		}
		if (port < 49152 || port > 65535) {
			// Toast.makeText(this,"Allowed Range for port is 49152 to 65535",Toast.LENGTH_LONG).show();
			return isRecording = false;
		}
		return true;
	}

	public void onRequestPress() { // RAISE REQUEST FOR AUDIO DOUBT
		final byte[] request = ("Raise Hand").getBytes();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// port=getPort();
					final InetAddress destination =

					InetAddress.getByName(ipAddress);
					if(socket1!=null)
					{
						socket1.close();
						socket1=null;
					}
					
					socket1 = new DatagramSocket();
					socket1.send(new DatagramPacket(request,

					request.length, destination, port));
					Log.e("REquest", "Ssnt");
					while (waitingForPermission())
						; // SEND PERMISSION
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					socket1.close();
					socket1=null;
				}
			}
		}).start();
	}

	private boolean waitingForPermission() throws IOException { // WAITING TO
																// RECEIVE
																// PERMISSION TO
																// START AUDIO
																// DOUBT

		byte[] receiveData = new byte[8192];

		if(socket2!=null)
		{
			socket2.close();
			socket2=null;
		}
		socket2 = new DatagramSocket(port);

		DatagramPacket receivePacket = new DatagramPacket(receiveData,
				receiveData.length);
		Log.e("waiting", "to receive");
		socket2.receive(receivePacket);
		Log.e("Received", "received");
		final String requestText = new String(receivePacket.getData());

		// RECEIVE PACKET FROM SERVER
		Log.d("requestText", requestText);
		socket2.close();
		socket2=null;
		if (requestText.contains(PERMISSION_TEXT)) { // IF PERMISSION

			// START STREAMING
			isRecording = true;
			Log.e("startStreaming", "STREAMING");
			startStreaming();
			return false;
		}
		return true;
	}

	/*
	 * public void onDefaultIP(View
	 * view){ipAddressField.setText("10.129.156.20");}
	 * 
	 * public void onDefaultPort(View view){portField.setText("50005");}
	 */
	public void onWithdrawPress() { // IF WITHDRAW BUTTON PRESSED DURING
									// STREAMING
		final byte[] request = ("Withdraw").getBytes();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// port=getPort();
					final InetAddress destination =

					InetAddress.getByName(ipAddress);
					if(socket3!=null)
					{
						socket3.close();
						socket3=null;
					}
					socket3 = new DatagramSocket();
					socket3.send(new DatagramPacket(request,

					request.length, destination, port)); // SEND WITHDRAW
															// REQUEST TO

					// SERVER
					isRecording = false;
					stopStreaming(); // STOP STREAMING
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					socket3.close();
					socket3=null;
				}
			}
		}).start();
	}
}