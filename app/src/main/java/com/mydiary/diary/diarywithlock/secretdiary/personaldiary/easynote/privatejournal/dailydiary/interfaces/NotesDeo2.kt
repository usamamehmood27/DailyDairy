package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Draftdataclass

@Dao
interface NotesDeo2 {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertdraft(note: Draftdataclass)
    @Query("DELETE FROM notes2 WHERE id = :id")
    fun delete(id: Int)

    @Update
    fun updatedraft(note: Draftdataclass)


    @Query("SELECT * FROM notes2")
    fun getAlldraftNotes(): List<Draftdataclass>
    @Query("DELETE FROM notes2")
    fun deleteAlDraftlNotes()
}