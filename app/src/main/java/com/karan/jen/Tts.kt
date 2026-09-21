package com.karan.jen
import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale
class JenTts(c:Context):TextToSpeech.OnInitListener{
 private val t=TextToSpeech(c,this); private var ok=false
 override fun onInit(s:Int){ok=s==TextToSpeech.SUCCESS;if(ok)t.language=Locale("hi","IN")}
 fun say(s:String){if(ok)t.speak(s,TextToSpeech.QUEUE_FLUSH,null,"jen",null)}
 fun close(){t.shutdown()}
}
