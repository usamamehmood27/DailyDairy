package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes

import android.app.AlertDialog
import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.alarmCallBack

class AlarmDialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        this.runOnUiThread {
//            // Show your dialog here
//            //
//
//            if (!isFinishing){
//              //  val ringtone = playRingtone(this.applicationContext)
//                val alertDialog = AlertDialog.Builder(this)
    //                    .setTitle("Alarm")
//                    .setMessage("Alarm is ringing!")
//                    .setPositiveButton("Cancel") { dialog, which ->
//                        // Stop the ringtone
//                   //     ringtone.stop()
//                        dialog.dismiss()
//                    }
//                    .setCancelable(false)
//                    .create()
//
//                // Show the AlertDialog
//                alertDialog.show()
//            }
//
//        }
    }
    private fun playRingtone(context: Context): Ringtone {
        val alarmRingtoneUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        val ringtone: Ringtone = RingtoneManager.getRingtone(context, alarmRingtoneUri)

        ringtone.play()

        return ringtone
    }
}