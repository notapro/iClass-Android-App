#include <jni.h>
#include "speex/speex_echo.h"
#include "speex/speex_preprocess.h"
#include "libspeex.h"
#define NULL 0
SpeexEchoState *st;
SpeexPreprocessState *den;

JNIEXPORT void JNICALL Java_com_iitb_interactiveclassroom_AudioSession_Java_1speex_1EchoCanceller_1open
  (JNIEnv *env, jobject jObj, jint jSampleRate, jint jBufSize, jint jTotalSize)
{
     //init
     int sampleRate=jSampleRate;
     st = speex_echo_state_init(jBufSize, jTotalSize);
     den = speex_preprocess_state_init(jBufSize, sampleRate);
     speex_echo_ctl(st, SPEEX_ECHO_SET_SAMPLING_RATE, &sampleRate);

     speex_preprocess_ctl(den, SPEEX_PREPROCESS_SET_ECHO_STATE, st);
}

JNIEXPORT jshortArray JNICALL Java_com_iitb_interactiveclassroom_AudioSession_Java_1speex_1EchoCanceller_1process
  (JNIEnv * env, jobject jObj, jshortArray input_frame, jshortArray echo_frame)
{
  //create native shorts from java shorts
  jshort *native_input_frame = (*env)->GetShortArrayElements(env, input_frame, NULL);
  jshort *native_echo_frame = (*env)->GetShortArrayElements(env, echo_frame, NULL);

  //allocate memory for output data
  jint length = (*env)->GetArrayLength(env, input_frame);
  if(length < 2048)
  {
	  return input_frame;
  }
  jshortArray temp = (*env)->NewShortArray(env, length);
  jshort *native_output_frame = (*env)->GetShortArrayElements(env, temp, 0);

  //call echo cancellation

  speex_echo_playback(st, native_echo_frame);

  speex_echo_capture(st, native_echo_frame, native_output_frame);

 // speex_echo_cancellation(st, native_input_frame, native_echo_frame, native_output_frame);
  //preprocess output frame
//  speex_preprocess_run(den, native_output_frame);

  //convert native output to java layer output
  jshortArray output_shorts = (*env)->NewShortArray(env, length);
  (*env)->SetShortArrayRegion(env, output_shorts, 0, length, native_output_frame);

  //cleanup and return
  (*env)->ReleaseShortArrayElements(env, input_frame, native_input_frame, 0);
  (*env)->ReleaseShortArrayElements(env, echo_frame, native_echo_frame, 0);
  (*env)->ReleaseShortArrayElements(env, temp, native_output_frame, 0);

  return output_shorts;
}
JNIEXPORT void JNICALL Java_com_iitb_interactiveclassroom_AudioSession_Java_1speex_1EchoCanceller_1close
  (JNIEnv *env, jobject jObj)
{
     //close
     speex_echo_state_destroy(st);
     speex_preprocess_state_destroy(den);
}
