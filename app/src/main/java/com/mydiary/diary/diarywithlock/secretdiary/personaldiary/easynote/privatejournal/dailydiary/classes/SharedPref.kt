package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes

import android.content.Context
import android.content.SharedPreferences

class SharedPref {
    companion object {
        lateinit var pref: SharedPreferences
        fun init(context: Context){
            if(!Companion::pref.isInitialized)
                pref = context.getSharedPreferences("DailyDairy", Context.MODE_PRIVATE)
        }

        fun putString(key: String, value: String) {
            if(!Companion::pref.isInitialized)
                init(ApplicationClass.getcontext())
            pref.edit().putString(key, value).apply()
        }

        fun getString(key: String): String {

            if(!Companion::pref.isInitialized)
                init(ApplicationClass.getcontext())
            return pref.getString(key,"")!!

        }

        fun getString(key: String,default: String): String {

            if(!Companion::pref.isInitialized)
                init(ApplicationClass.getcontext())
            return pref.getString(key,default)!!

        }

        fun putInt(key: String, value: Int) {
            if(!Companion::pref.isInitialized)
                init(ApplicationClass.getcontext())
            pref.edit().putInt(key, value).apply()
        }

        fun getInt(key: String): Int {
            if(!Companion::pref.isInitialized)
                init(ApplicationClass.getcontext())
            return pref.getInt(key, 1)

        }

        fun getInt(key: String,default:Int): Int {
            if(!Companion::pref.isInitialized)
                init(ApplicationClass.getcontext())
            return pref.getInt(key, default)

        }

        fun putBolean(key: String, value: Boolean) {
            if(!Companion::pref.isInitialized)
                init(ApplicationClass.getcontext())
            pref.edit().putBoolean(key, value).apply()
        }

        fun getBolean(key: String, default: Boolean): Boolean {
            if(!Companion::pref.isInitialized)
                init(ApplicationClass.getcontext())
            return pref.getBoolean(key, default)

        }

        fun putLong(key: String, value: Long) {
            if(!Companion::pref.isInitialized)
                init(ApplicationClass.getcontext())
            pref.edit().putLong(key, value).apply()
        }

        fun getLong(key: String): Long {
            if(!Companion::pref.isInitialized)
                init(ApplicationClass.getcontext())
            return pref.getLong(key, 1)
        }
    }
}