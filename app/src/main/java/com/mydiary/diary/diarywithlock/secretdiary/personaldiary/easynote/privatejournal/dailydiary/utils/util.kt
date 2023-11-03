package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.DailyDiary.R
import com.example.DailyDiary.databinding.FragmentDairyPageBinding
import com.example.DailyDiary.databinding.FragmentSettingBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.AlarmReceiver
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.SharedPref
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.StikerdataClass
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.EmojiDataClass
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.FontClass
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.GallerY_DataClass
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.GalleryFolder
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.interfaces.Alarm_Alert
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.interfaces.EnterenceBack
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.interfaces.Onbackpress
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.interfaces.SplashAd
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class util {
    companion object {
        lateinit var back: Onbackpress
        lateinit var enterenceback: EnterenceBack
        lateinit var alarmCallBack: Alarm_Alert
        lateinit var splashcallback: SplashAd
        var isLastSelectedDevice = ""
        var currentfoldername=""
        fun showToast(context: Activity, message: String) {
            val inflater = context.layoutInflater
            val layout = inflater.inflate(R.layout.toast_layout, null)
            val textViewMessage = layout.findViewById<TextView>(R.id.textViewMessage)
            textViewMessage.text = message

            val toast = Toast(context.applicationContext)
            toast.duration = Toast.LENGTH_SHORT
            toast.view = layout
            toast.show()
        }

        fun tagslist(context: Context): MutableList<Any> {
            var list = mutableListOf<Any>()
            list.add(StikerdataClass(R.drawable.tag1))
            list.add(StikerdataClass(R.drawable.tag2))
            list.add(StikerdataClass(R.drawable.tag3))
            list.add(StikerdataClass(R.drawable.tag4))
            list.add(StikerdataClass(R.drawable.tag5))
            list.add(StikerdataClass(R.drawable.tag6))
            list.add(StikerdataClass(R.drawable.tag7))
            list.add(StikerdataClass(R.drawable.tag8))
            list.add(StikerdataClass(R.drawable.tag9))
            list.add(StikerdataClass(R.drawable.tag10))
            list.add(StikerdataClass(R.drawable.tag11))
            list.add(StikerdataClass(R.drawable.tag12))


            return list
        }

        fun emojilist(context: Context): MutableList<Any> {
            var list = mutableListOf<Any>()
            list.add(EmojiDataClass(R.drawable.emoji1))
            list.add(EmojiDataClass(R.drawable.emoji2))
            list.add(EmojiDataClass(R.drawable.emoji3))
            list.add(EmojiDataClass(R.drawable.emoji4))
            list.add(EmojiDataClass(R.drawable.emoji5))
            list.add(EmojiDataClass(R.drawable.emoji6))
            list.add(EmojiDataClass(R.drawable.emoji7))
            list.add(EmojiDataClass(R.drawable.emoji8))
            list.add(EmojiDataClass(R.drawable.emoji9))
            list.add(EmojiDataClass(R.drawable.emoji10))
            list.add(EmojiDataClass(R.drawable.emoji11))
            list.add(EmojiDataClass(R.drawable.emoji12))


            return list
        }

        fun fontlist(context: Context): MutableList<Any> {
            var list = mutableListOf<Any>()
            list.add(FontClass("Default", R.font.quicksand))
            list.add(FontClass("Amiri", R.font.amiri_regular))
            list.add(FontClass("Monospace", R.font.monospace))
            list.add(FontClass("Light", R.font.light))
            list.add(FontClass("High Summit", R.font.highsummit))
            list.add(FontClass("Cardo Bold", R.font.cardo_bold))
            list.add(FontClass("Dancing Script", R.font.dancing_script))
            list.add(FontClass("Pacifico", R.font.pacifico_regular))
            list.add(FontClass("Aileron", R.font.aileron_regular))
            list.add(FontClass("Lobster", R.font.lobster_regular))
            list.add(FontClass("Helvetica", R.font.helvetica))
            list.add(FontClass("Coming Soon", R.font.quicksand))

            return list
        }

        @SuppressLint("Range")
        fun getImagesByBucket(context: Activity): MutableList<Any> {
            var fileList = mutableListOf<Any>()
            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection =
                arrayOf(MediaStore.Images.Media.DATA)
            val orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC"
            val cursor: Cursor? =
                context.contentResolver.query(uri, projection, null, null, orderBy)
            if (cursor != null) {
                var file: File
                while (cursor.moveToNext()) {

                    val path = cursor.getString(cursor.getColumnIndex(projection[0]))
                    file = File(path)
                    if (file.exists())
                        fileList.add(GallerY_DataClass(path))
                }
                cursor.close()
            }


            return fileList

        }

        @SuppressLint("Range")
        fun getImageDirectories(mContext: Context): MutableList<Any> {
            val directories = HashSet<Any>()
            val contentResolver = mContext.contentResolver
            val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val includeImages = "${MediaStore.Images.Media.MIME_TYPE} LIKE 'image/%'"
            val excludeGif =
                " AND ${MediaStore.Images.Media.MIME_TYPE} != 'image/gif' AND ${MediaStore.Images.Media.MIME_TYPE} != 'image/giff'"
            val selection = "$includeImages $excludeGif"
            val cursor = contentResolver.query(queryUri, projection, selection, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val photoUri = cursor.getString(cursor.getColumnIndex(projection[0]))
                    val parentFolder = File(photoUri).parent
                    if (parentFolder != null) {
                        directories.add(GalleryFolder(parentFolder))
                    }
                } while (cursor.moveToNext())
            }
            cursor?.close()
            return directories.toMutableList()
        }

        fun logAnalytic(context: Context, deviceName: String) {
            Log.d("chechking", "logAnalytic: " + deviceName)

            val analytics by lazy {
                FirebaseAnalytics.getInstance((context))
            }
            val analyticBundle = Bundle()
            analytics.logEvent(deviceName, analyticBundle)
            isLastSelectedDevice = deviceName
        }

        fun setAnimation(layout: LinearLayout, startvalue: Float, endvalue: Float) {
            val animator = ValueAnimator.ofFloat(startvalue, endvalue)
            animator.duration = 800
            animator.addUpdateListener { valueAnimator ->
                val translationY = valueAnimator.animatedValue as Float
                layout.translationY = translationY
            }
            animator.start()
        }

        fun getMonthName(calendar: Calendar): String {
            val dateFormat = SimpleDateFormat("MMMM", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }

        fun saveDrawnViewToStorage(context: Context, binding: FragmentDairyPageBinding): String {
            val timeStamp: String =
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "drawn_view_$timeStamp.png"

            // Create the directory to store the image
            val storageDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "DrawnViews"
            )
            if (!storageDir.exists()) {
                storageDir.mkdirs()
            }

            // Create the file to save the image
            val imageFile = File(storageDir, fileName)

            // Get the bitmap representation of the drawn view
            val bitmap =
                Bitmap.createBitmap(
                    binding.draw.width,
                    binding.draw.height,
                    Bitmap.Config.ARGB_8888
                )
            val canvas = Canvas(bitmap)
            binding.draw.draw(canvas)

            try {
                // Save the bitmap to the file
                val outputStream = FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()

                return imageFile.absolutePath
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return imageFile.absolutePath
        }

        fun CheckSharedPref(context: Activity, binding: FragmentSettingBinding) {
            binding.apply {
                if (!SharedPref.getBolean("lock", false)) {
                    lockselect.setImageResource(R.drawable.sellect)
                } else if (SharedPref.getBolean("lock", false)) {
                    lockselect.setImageResource(R.drawable.unselect)
                }
//                if (!SharedPref.getBolean("setalaram", false)) {
//                    clocksellect.setImageResource(R.drawable.sellect)
//                } else {
//                    clocksellect.setImageResource(R.drawable.unselect)
//                }
                if (!SharedPref.getBolean("cleardata", false)) {
                    clearsellect.setImageResource(R.drawable.sellect)
                } else {
                    clearsellect.setImageResource(R.drawable.unselect)
                }
                if (!SharedPref.getBolean("skipmood", false)) {
                    skipsellect.setImageResource(R.drawable.sellect)
                } else {
                    skipsellect.setImageResource(R.drawable.unselect)
                }
            }

        }

        fun setDailyAlarm(context: Context, hour: Int, minute: Int) {
            // Get an instance of the AlarmManager
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // Create an Intent for the BroadcastReceiver
            val intent = Intent(context, AlarmReceiver::class.java)

            // Create a PendingIntent to be triggered when the alarm goes off
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }
            )

            // Set the alarm to start at the specified time
            val calendar = Calendar.getInstance()
            calendar.apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }

            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }

        fun showTimePickerDialog(
            context: Context,
            onTimeSetListener: TimePickerDialog.OnTimeSetListener
        ) {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(context, onTimeSetListener, hour, minute, false)
            timePickerDialog.show()
        }

        fun cancelAlarm(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }
            )
            alarmManager.cancel(pendingIntent)
        }

        fun clearCache(context: Activity) {
            try {
                val dir: File = context.cacheDir
                deleteDir(dir)
                showToast(context, "App data cleared")
            } catch (e: Exception) {
            }
        }

        fun deleteDir(dir: File?): Boolean {
            return if (dir != null && dir.isDirectory) {
                val children: Array<String> = dir.list()!!
                for (i in children.indices) {
                    val success = deleteDir(File(dir, children[i]))
                    if (!success) {
                        return false
                    }
                }
                dir.delete()
            } else if (dir != null && dir.isFile) {
                dir.delete()
            } else {
                false
            }
        }
    }
}