package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.viewmodel

import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.NotesDatabase
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Draftdataclass
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Folder
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Notes

class NotesRepository(private val db: NotesDatabase) {
    fun getAllNotes() =   db.getNotesDao().getAllNotes()
    suspend fun addNote(notes: Notes) {
        db.getNotesDao().insert(notes)
    }

    suspend fun updateNote(notes: Notes) {
        db.getNotesDao().update(notes)
    }

    suspend fun deleteNotes(id: Int) {
        db.getNotesDao().delete(id)
    }
    suspend fun searchNotesByDate(date: String): List<Notes> {
        return db.getNotesDao().searchByDate(date)
    }
    suspend fun searchNotesByFolder(foldername: String): List<Notes> {
        return db.getNotesDao().searchfoldernote(foldername)
    }
    suspend fun updateNotefolder(oldTitle: String, newTitle: String) {
        db.getNotesDao().updateNoteFolder(oldTitle, newTitle)
    }

    suspend fun adddraftNote(notes: Draftdataclass) {
        db.getdraftNotesDao().insertdraft(notes)
    }

    suspend fun updatedraftNote(notes: Draftdataclass) {
        db.getdraftNotesDao().updatedraft(notes)
    }

    suspend fun deletedraftNotes(id: Int) {
        db.getdraftNotesDao().delete(id)
    }
    suspend fun addfolder(fol: Folder) {
        db.folderDao().insert(fol)
    }

    suspend fun updatefolder(fol: Folder) {
        db.folderDao().update(fol)
    }

    suspend fun deletefolder(fol: Folder) {
        db.folderDao().delete(fol)
    }
    fun getAlldraftNotes() =   db.getdraftNotesDao().getAlldraftNotes()
    suspend fun getAlldraftfolder() =   db.folderDao().getAllFolders()
    fun deleteAlDraftlNotes() =   db.getdraftNotesDao().deleteAlDraftlNotes()
//    suspend fun moveNoteToFolder(noteId: Int, folderId: Int) {
//        val note = db.getNotesDao().getNoteById(noteId)
//        note!!.folderId = folderId
//        db.getNotesDao().updateNoteFolder(note)
//    }
//    suspend fun notefolderlist(folderId: Int) {
//
//        db.getNotesDao().getNotesByFolderId(folderId)
//    }


}