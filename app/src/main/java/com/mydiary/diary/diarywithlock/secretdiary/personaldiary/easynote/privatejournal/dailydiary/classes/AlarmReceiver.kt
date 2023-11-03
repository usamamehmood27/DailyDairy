package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes

import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        util.alarmCallBack.alarm()

//        try {
//            // Your alarm code here
//
//            Log.d("AlarmReceiver", "Alarm received")
//        } catch (e: Exception) {
//            Log.e("AlarmReceiver", "Error in onReceive: ${e.message}")
//            e.printStackTrace()
//        }
//            Toast.makeText(context.applicationContext, "Alarm triggered", Toast.LENGTH_SHORT).show()
//            val startIntent = Intent(context, AlarmDialogActivity::class.java)
//            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(startIntent)


    }

}