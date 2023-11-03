package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes2")
class Draftdataclass (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tittle: String,
    val background: Int,
    val mood: Int,
    val stiker: Int,
    val font: Int,
    val fontsize: Int,
    val fontposition: Int,
    val x: Int,
    val y: Int,
    val imagex: Int,
    val imagey: Int,
    val gliterx: Int,
    val glitery: Int,
    val image: String,
    val gliterimage: String,
    val month: String,
    val day: String,
    val time: String,
    val content: String,
    val completedate: String)
