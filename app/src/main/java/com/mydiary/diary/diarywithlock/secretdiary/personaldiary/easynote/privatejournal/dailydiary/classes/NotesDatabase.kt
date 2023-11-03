package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Draftdataclass
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Folder
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Notes
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.interfaces.FolderDao
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.interfaces.NotesDeo
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.interfaces.NotesDeo2

@Database(entities =[Notes::class,Draftdataclass::class,Folder::class], version = 25, exportSchema = false)
//@TypeConverters(BitmapTypeConverters::class)
abstract class NotesDatabase:RoomDatabase() {
    abstract fun getNotesDao(): NotesDeo
    abstract fun getdraftNotesDao(): NotesDeo2
    abstract fun folderDao(): FolderDao
    companion object {
        private const val DATABASE_NAME = "Notedatabase"

        @Volatile
        private var instance: NotesDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): NotesDatabase = instance ?:
        synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            NotesDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }
}