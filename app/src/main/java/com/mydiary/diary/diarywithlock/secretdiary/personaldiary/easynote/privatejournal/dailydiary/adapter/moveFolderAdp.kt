package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.DailyDiary.R
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Folder
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.GalleryFolder
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.viewmodel.Notes_ViewModel
//import kamai.app.ads.AdsManager
import java.io.File
import java.lang.StringBuilder

class moveFolderAdp(var context: Context, var viewModel: Notes_ViewModel, var list: MutableList<Any>, var click:(String)->Unit):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var sellected=-1
    class Folder_layout(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var foldername = itemview.findViewById<TextView>(R.id.foldername)
        var check = itemview.findViewById<ImageView>(R.id.check)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       val view = LayoutInflater.from(context)
            .inflate(R.layout.foldercheck, parent, false)
        return Folder_layout(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as Folder_layout
        val currentItem = list[position] as Folder
        if (sellected==position)
            holder.check.setImageResource(R.drawable.selectpink)
        else
            holder.check.setImageResource(R.drawable.selectwhite)
        holder.foldername.text= StringBuilder(currentItem.name)
        holder.itemView.setOnClickListener {
            sellected=position
            notifyDataSetChanged()
            click.invoke(currentItem.name)
        }
    }


}