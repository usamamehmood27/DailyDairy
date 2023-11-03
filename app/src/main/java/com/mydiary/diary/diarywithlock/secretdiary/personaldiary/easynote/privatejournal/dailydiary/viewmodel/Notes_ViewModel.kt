package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.DailyDiary.R
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.activities.MainActivity.Companion.noteRepository
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Draftdataclass
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Folder
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Notes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class Notes_ViewModel(app: Application, private val notesRepository: NotesRepository) :
    ViewModel() {
    val listofnotes = MutableLiveData<MutableList<Any>>()
    val listofdraft = MutableLiveData<MutableList<Any>>()
    val listoffolder = MutableLiveData<MutableList<Any>>()
    val foldernaemlist = MutableLiveData<MutableList<Any>>()
    val onenote = MutableLiveData<Any>()
    val calanderlist = MutableLiveData<MutableList<Any>>()
    val backgroundlist = MutableLiveData<MutableList<Any>>()
    val graidiantlist = MutableLiveData<MutableList<Any>>()
    @OptIn(DelicateCoroutinesApi::class)
    fun getNotes() {
        val noteslist = mutableListOf<Any>()
        GlobalScope.launch(Dispatchers.IO) {
            val notes = notesRepository.getAllNotes()
            for (note in notes) {
                noteslist.add(
                    Notes(
                        note.id,
                        note.folderId,
                        tittle = note.tittle,
                        background = note.background,
                        mood = note.mood,
                        stiker = note.stiker,
                        font = note.font,
                        fontsize = note.fontsize,
                        fontposition = note.fontposition,
                        x = note.x,
                        y = note.y,
                        note.imagewidth,
                        note.imageheight,
                        note.imagex,
                        gliterx = note.gliterx,
                        glitery = note.glitery,
                        image = note.image,
                        gliterimage = note.gliterimage,
                        month = note.month,
                        day = note.day,
                        time = note.time,
                        content = note.content,
                        completedate = note.completedate
                    )
                )

            }
            listofnotes.postValue(noteslist)
        }

    }
//    fun updatenotefolder(noteId:Int,folderId:Int){
//        viewModelScope.launch {
//            noteRepository.moveNoteToFolder(noteId, folderId)
//        }
//    }
    @OptIn(DelicateCoroutinesApi::class)
    fun getDraftNotes() {
        val draftlist = mutableListOf<Any>()
        GlobalScope.launch(Dispatchers.IO) {
            val notes = notesRepository.getAlldraftNotes()
            for (draftnote in notes) {
                draftlist.add(
                    Draftdataclass(
                        draftnote.id,
                        tittle = draftnote.tittle,
                        background = draftnote.background,
                        mood = draftnote.mood,
                        stiker = draftnote.stiker,
                        font = draftnote.font,
                        fontsize = draftnote.fontsize,
                        fontposition = draftnote.fontposition,
                        x = draftnote.x,
                        y = draftnote.y,
                        draftnote.imagex,
                        draftnote.imagey,
                        gliterx = draftnote.gliterx,
                        glitery = draftnote.glitery,
                        image = draftnote.image,
                        gliterimage = draftnote.gliterimage,
                        month = draftnote.month,
                        day = draftnote.day,
                        time = draftnote.time,
                        content = draftnote.content,
                        completedate = draftnote.completedate
                    )
                )

            }
            listofdraft.postValue(draftlist)
        }

    }
    @OptIn(DelicateCoroutinesApi::class)
    fun getfolders() {
        val folderlist = mutableListOf<Any>()
        GlobalScope.launch(Dispatchers.IO) {
            val Folder = notesRepository.getAlldraftfolder()
            for (fol in Folder) {
                folderlist.add(
                    Folder(fol.id,fol.name)
                )

            }
            listoffolder.postValue(folderlist)
        }

    }
    fun addNote(note: Notes) = CoroutineScope(Dispatchers.IO).launch {
        notesRepository.addNote(note)
    }

    fun adddraftNote(note: Draftdataclass) = CoroutineScope(Dispatchers.IO).launch {
        notesRepository.adddraftNote(note)
    }
    fun addfolder(fol: Folder) = CoroutineScope(Dispatchers.IO).launch {
        notesRepository.addfolder(fol)
    }
    fun deleteNotes(id: Int) = CoroutineScope(Dispatchers.IO).launch {
        notesRepository.deleteNotes(id)
    }
    fun deletefolder(fol: Folder) = CoroutineScope(Dispatchers.IO).launch {
        notesRepository.deletefolder(fol)
    }
    fun updateNote(note: Notes) = CoroutineScope((Dispatchers.IO)).launch {
        notesRepository.updateNote(note)
    }
    fun updatefolder(fol: Folder) = CoroutineScope((Dispatchers.IO)).launch {
        notesRepository.updatefolder(fol)
    }

    fun deletedraftNotes(id: Int) = CoroutineScope(Dispatchers.IO).launch {
        notesRepository.deletedraftNotes(id)
    }

    fun deletealldraftNotes() = CoroutineScope(Dispatchers.IO).launch {
        notesRepository.deleteAlDraftlNotes()
    }

    fun updatedraftNote(note: Draftdataclass) = CoroutineScope((Dispatchers.IO)).launch {
        notesRepository.updatedraftNote(note)
    }


        fun searchNotesByDate(date: String) {
            val list = mutableListOf<Any>()
            CoroutineScope((Dispatchers.IO)).launch {
                val matchingNotes = notesRepository.searchNotesByDate(date)
                for (i in matchingNotes) {
                    list.add(i)
                }
            }
            calanderlist.postValue(list)
        }
    fun searchNotesByFolder(foldername:String){
        val list = mutableListOf<Any>()
        CoroutineScope((Dispatchers.IO)).launch {
            val matchingNotes = notesRepository.searchNotesByFolder(foldername)
            for (i in matchingNotes) {
                list.add(i)
            }
        }
        foldernaemlist.postValue(list)
    }

        fun getbackgroundl(context: Context) {
            CoroutineScope((Dispatchers.Main)).launch {
                var list = mutableListOf<Any>()
                list.add(R.drawable.splashbackgroung)
                list.add(R.drawable.background1)
                list.add(R.drawable.backgound2)
                list.add(R.drawable.background3)
                list.add(R.drawable.background4)
                list.add(R.drawable.background5)
                list.add(R.drawable.background6)
                list.add(R.drawable.background7)
                list.add(R.drawable.background8)
                list.add(R.drawable.background9)
                list.add(R.drawable.background10)
                list.add(R.drawable.background11)
                backgroundlist.postValue(list)
            }

        }

        fun getGradiantlist(context: Context) {
            CoroutineScope((Dispatchers.Main)).launch {
                var list = mutableListOf<Any>()
                list.add(R.drawable.splashbackgroung)
                list.add(R.drawable.background0)
                list.add(R.drawable.gradiant1)
                list.add(R.drawable.gradiant2)
                list.add(R.drawable.gradiant3)
                list.add(R.drawable.gradiant4)
                list.add(R.drawable.gradiant5)
                list.add(R.drawable.gradiant6)
                list.add(R.drawable.gradiant7)
                list.add(R.drawable.gradiant8)
                list.add(R.drawable.gradiant9)
                list.add(R.drawable.gradiant11)
                graidiantlist.postValue(list)
            }
        }
}