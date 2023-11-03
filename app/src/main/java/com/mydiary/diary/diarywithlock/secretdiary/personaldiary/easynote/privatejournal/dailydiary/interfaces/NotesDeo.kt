package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.interfaces


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Notes

@Dao
interface NotesDeo {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: Notes)

    @Query("DELETE FROM notes WHERE id = :id")
    fun delete(id: Int)

    @Update
    fun update(note: Notes)
    @Query("UPDATE notes SET folderId = :newTitle WHERE folderId = :oldTitle")
    suspend fun updateNoteFolder(oldTitle: String, newTitle: String)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): List<Notes>

    @Query("SELECT * FROM notes WHERE completedate = :searchDate")
    suspend fun searchByDate(searchDate: String): List<Notes>
    @Query("SELECT * FROM notes WHERE folderId = :searchDate")
    suspend fun searchfoldernote(searchDate: String): List<Notes>
    @Update
    suspend fun updateNoteFolder(note: Notes)
    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): Notes?
    @Query("SELECT * FROM notes WHERE folderId = :folderId")
    suspend fun getNotesByFolderId(folderId: Int): List<Notes>
}