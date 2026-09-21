package com.karan.jen
import android.content.Context
object Prefs {
 private const val P="jen"; private const val E="endpoint"
 fun endpoint(c:Context)=c.getSharedPreferences(P,0).getString(E,"")?:""
 fun save(c:Context,v:String)=c.getSharedPreferences(P,0).edit().putString(E,v).apply()
}
