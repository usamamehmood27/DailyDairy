package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class BitmapTypeConverters {
  //  companion object{
        @TypeConverter
        fun fromBitmap(bitmap: Bitmap?): ByteArray? {
            if (bitmap == null) return null
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            return outputStream.toByteArray()
        }

        @TypeConverter
        fun toBitmap(byteArray: ByteArray?): Bitmap? {
            if (byteArray == null) return null
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
    //}

}