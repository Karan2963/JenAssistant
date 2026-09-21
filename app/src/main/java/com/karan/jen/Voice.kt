package com.karan.jen
import android.content.*;import android.os.*;import android.speech.*;import java.util.*
class JenVoice(private val c:Context){
 private var s:SpeechRecognizer?=null
 fun listen(done:(String)->Unit,err:(String)->Unit){
  if(!SpeechRecognizer.isRecognitionAvailable(c)){err("Speech recognition available nahi hai.");return}
  s?.destroy();s=SpeechRecognizer.createSpeechRecognizer(c)
  s!!.setRecognitionListener(object:RecognitionListener{
   override fun onResults(b:Bundle){done(b.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()?:"")}
   override fun onError(e:Int){err("Voice recognition error: $e")}
   override fun onReadyForSpeech(p:Bundle?){ };override fun onBeginningOfSpeech(){};override fun onRmsChanged(v:Float){}
   override fun onBufferReceived(b:ByteArray?){};override fun onEndOfSpeech(){};override fun onPartialResults(b:Bundle?){}
   override fun onEvent(t:Int,b:Bundle?){}
  })
  s!!.startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply{
   putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
   putExtra(RecognizerIntent.EXTRA_LANGUAGE,"hi-IN");putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,3)
  })
 }
 fun close(){s?.destroy()}
}
