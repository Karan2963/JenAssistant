package com.karan.jen
import android.app.*;import android.content.*;import android.os.Build
class ReminderReceiver:BroadcastReceiver(){override fun onReceive(c:Context,i:Intent){
 val n=c.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
 if(Build.VERSION.SDK_INT>=26)n.createNotificationChannel(NotificationChannel("jen","Jen Reminders",NotificationManager.IMPORTANCE_HIGH))
 n.notify((System.currentTimeMillis() and 0x7fffffff).toInt(),Notification.Builder(c,"jen").setContentTitle("Jen Reminder").setContentText(i.getStringExtra("message")?:"Karan, reminder hai.").setSmallIcon(android.R.drawable.ic_lock_idle_alarm).setAutoCancel(true).build())
}}
