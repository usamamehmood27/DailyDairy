package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.adapter

import android.app.Activity
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
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils.Companion.deleteFolder
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.viewmodel.Notes_ViewModel
//import kamai.app.ads.AdsManager
import java.io.File

class FolderAdapter(var context: Activity,var viewModel: Notes_ViewModel,var list: MutableList<Any>,var click:(String,Folder)->Unit):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class Folder_layout(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var sellectedPosition=-1

        var name = itemview.findViewById<TextView>(R.id.name)
        var delete = itemview.findViewById<ImageView>(R.id.delete)
        var edit = itemview.findViewById<ImageView>(R.id.edit)

    }
    class AdsPagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val Adscontainer: FrameLayout = itemView.findViewById(R.id.nativeAd)
    }
    class GalleryFolder_layout(itemview: View) : RecyclerView.ViewHolder(itemview) {

        var name = itemview.findViewById<TextView>(R.id.name)
        var size = itemview.findViewById<TextView>(R.id.size)
        var image = itemview.findViewById<ImageView>(R.id.folderimage)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.folder_layout, parent, false)
                Folder_layout(view)
            }
            1 -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.galleryfolder_layout, parent, false)
                GalleryFolder_layout(view)
            }
            2 -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.ad_layoyt, parent, false)
                AdsPagesViewHolder(view)
            }
            else -> {
                throw IllegalArgumentException("Invailed viewtype provided")
            }
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 0) {
            holder as Folder_layout
            val currentItem = list[position] as Folder
            holder.edit.setOnClickListener {
                click.invoke("rename",currentItem)

            }
            holder.name.text=currentItem.name
            holder.delete.setOnClickListener {
                deleteFolder(context,viewModel,currentItem,position){
                    list.removeAt(position)
                    notifyItemRemoved(it)
                    notifyItemRangeChanged(it, list.size)

                }

            }
            holder.itemView.setOnClickListener {
                click.invoke("",currentItem)
            }
        }
       else if (getItemViewType(position) == 1) {
            holder as GalleryFolder_layout
            val currentItem = list[position] as GalleryFolder
            val file=File(currentItem.uri)
            val list=file.listFiles()
            holder.name.text=file.name
            holder.size.text=list.size.toString()
            Glide.with(context).load(list[0].path).into(holder.image)
          holder.itemView.setOnClickListener {
              click.invoke(file.path,Folder(0,"doe"))
          }
        }else if(getItemViewType(position)==2){
            holder as AdsPagesViewHolder
            val currentItem = list[position]
            currentItem as String
//            AdsManager.showNativeAd(context,holder.Adscontainer,"FolderAdapter")
        }
    }
    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        return if (item is Folder)
            0
        else if (item is GalleryFolder)
        1
        else if (item is String)
            2
        else
            2
    }


}