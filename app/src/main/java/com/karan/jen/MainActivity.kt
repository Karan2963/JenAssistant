package com.karan.jen
import android.Manifest;import android.app.*;import android.content.*;import android.os.*;import android.provider.Settings;import android.speech.*;import android.widget.*;import org.json.*;import java.net.*;import java.nio.charset.StandardCharsets;import java.util.*;import java.util.concurrent.Executors
class MainActivity:Activity(){
 lateinit var t:JenTts;lateinit var v:JenVoice;lateinit var status:TextView;lateinit var chat:TextView;lateinit var url:EditText
 private lateinit var memory:ConversationStore
 override fun onCreate(b:Bundle?){super.onCreate(b);t=JenTts(this);v=JenVoice(this);memory=ConversationStore(this)
 if(Build.VERSION.SDK_INT>=23&&checkSelfPermission(Manifest.permission.RECORD_AUDIO)!=0)requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA),10)
 val l=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(24,30,24,20)}
 status=TextView(this).apply{text="READY";textSize=18f};chat=TextView(this).apply{text="Jen: Hi Karan. Main ready hoon.";textSize=16f}
 url=EditText(this).apply{hint="GPT backend URL";setText(Prefs.endpoint(this@MainActivity))}
 fun btn(s:String,f:()->Unit)=Button(this).apply{text=s;setOnClickListener{f()}}
 l.addView(TextView(this).apply{text="JEN";textSize=30f});l.addView(status);l.addView(chat)
 l.addView(btn("🎤 TALK TO JEN"){listen()});l.addView(btn("🔦 FLASHLIGHT"){val ok=PhoneActions.torch(this,true);say(if(ok)"Flashlight on kar diya." else "Flashlight control nahi hua.")})
 l.addView(btn("⏰ 1 MINUTE REMINDER"){reminder(1,"Test reminder")})
 l.addView(btn("💬 WHATSAPP"){val ok=PhoneActions.whatsapp(this,"Hello from Jen");say(if(ok)"WhatsApp khol diya. Contact select karke send karo." else "WhatsApp nahi mila.")})
 l.addView(btn("♿ ACCESSIBILITY"){startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))})
 l.addView(btn("🔔 NOTIFICATIONS"){startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))})
 l.addView(btn("🧠 MEMORY"){showMemory()})
 l.addView(btn("🗑 FORGET MEMORY"){memory.clear();say("Conversation memory clear kar di.")})
 l.addView(url);l.addView(btn("💾 SAVE AI SERVER"){Prefs.save(this,url.text.toString().trim());say("AI server URL save kar diya.")})
 setContentView(l)}
 fun listen(){status.text="LISTENING";v.listen({q->remember("You",q);chat.text="You: $q\n\nJen: thinking...";local(q)},{say(it)})}
 fun local(q:String){val x=q.lowercase(Locale.getDefault());when{
  x.contains("flashlight")||x.contains("torch")||x.contains("टॉर्च")-> {val on=!(x.contains("off")||x.contains("बंद"));say(if(PhoneActions.torch(this,on))"Flashlight ${if(on)"on"else"off"} kar diya." else "Flashlight control nahi hua.")}
  x.contains("battery")||x.contains("बैटरी")->say("Battery ${PhoneActions.battery(this)} percent hai.")
  else->ai(q)}}
 fun ai(q:String){val e=Prefs.endpoint(this);if(e.isBlank()){say("GPT backend URL save karo.");return};status.text="THINKING";Executors.newSingleThreadExecutor().execute{try{
  val c=(URL(e).openConnection() as HttpURLConnection);c.requestMethod="POST";c.connectTimeout=15000;c.readTimeout=30000;c.doOutput=true;c.setRequestProperty("Content-Type","application/json")
  c.outputStream.use{it.write(JSONObject().put("message",q).toString().toByteArray(StandardCharsets.UTF_8))}
  val j=JSONObject(c.inputStream.bufferedReader().use{it.readText()});runOnUiThread{val reply=j.optString("reply");remember("Jen",reply);chat.text="You: $q\n\nJen: "+reply;t.say(reply);status.text="READY";j.optJSONObject("action")?.let{execute(it)}}}catch(_:Exception){runOnUiThread{say("AI server se connection nahi ho pa raha.")}}}}
 fun execute(a:JSONObject){when(a.optString("name")){
  "flashlight"->{val on=a.optBoolean("enabled");say(if(PhoneActions.torch(this,on))"Flashlight ${if(on)"on"else"off"} kar diya." else "Flashlight control nahi hua.")}
  "battery"->say("Battery ${PhoneActions.battery(this)} percent hai.")
  "open_app"->say(if(PhoneActions.open(this,a.optString("package")))"App khol diya." else "App nahi mila.")
  "whatsapp_share"->say(if(PhoneActions.whatsapp(this,a.optString("message")))"WhatsApp message prepare kar diya. Contact select karke send karo." else "WhatsApp nahi mila.")
  "reminder_after_minutes"->{val m=a.optInt("minutes",1);reminder(m,a.optString("message"))}
 }}
 fun reminder(m:Int,msg:String){val am=getSystemService(ALARM_SERVICE) as AlarmManager;val i=Intent(this,ReminderReceiver::class.java).putExtra("message",msg);val pi=PendingIntent.getBroadcast(this,77,i,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE);am.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+m*60000L,pi);say("$m minute ka reminder laga diya.")}
 fun remember(role:String,text:String){if(text.isNotBlank())memory.add(role,text)}
 fun showMemory(){val items=memory.entries();if(items.isEmpty()){say("Abhi koi conversation memory nahi hai.");return};val text=items.takeLast(10).joinToString("\n\n"){"${it.role}: ${it.text}"};chat.text=text;status.text="MEMORY";t.say("Recent memory screen par dikha di.")}
 fun say(s:String){remember("Jen",s);status.text="READY";chat.text="Jen: $s";t.say(s)}
 override fun onDestroy(){v.close();t.close();super.onDestroy()}
}
