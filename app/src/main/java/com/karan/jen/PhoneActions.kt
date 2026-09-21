package com.karan.jen
import android.content.*;import android.hardware.camera2.*;import android.os.BatteryManager
object PhoneActions{
 fun torch(c:Context,on:Boolean):Boolean=try{
  val m=c.getSystemService(Context.CAMERA_SERVICE) as CameraManager
  val id=m.cameraIdList.firstOrNull{m.getCameraCharacteristics(it).get(CameraCharacteristics.FLASH_INFO_AVAILABLE)==true}?:return false
  m.setTorchMode(id,on);true}catch(_:Exception){false}
 fun battery(c:Context)=(c.getSystemService(Context.BATTERY_SERVICE) as BatteryManager).getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
 fun open(c:Context,p:String):Boolean=try{c.startActivity(c.packageManager.getLaunchIntentForPackage(p)!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));true}catch(_:Exception){false}
 fun whatsapp(c:Context,msg:String):Boolean=try{c.startActivity(Intent(Intent.ACTION_SEND).apply{type="text/plain";putExtra(Intent.EXTRA_TEXT,msg);setPackage("com.whatsapp");addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)});true}catch(_:Exception){false}
}
