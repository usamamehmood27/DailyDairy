package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var folderId: String,
    val tittle: String,
    val background: Int,
    val mood: Int,
    val stiker: Int,
    val font: Int,
    val fontsize: Int,
    val fontposition: Int,
    val x: Int,
    val y: Int,
    val imagewidth: Int,
    val imageheight: Int,
    val imagex: Int,
    val gliterx: Int,
    val glitery: Int,
    val image: String,
    val gliterimage: String,
    val month: String,
    val day: String,
    val time: String,
    val content: String,
    val completedate: String
)
