package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.DailyDiary.R
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Draftdataclass
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Folder
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Notes
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.viewmodel.Notes_ViewModel
import kamai.app.ads.AdsManager

class NotesAdapter(val context: Activity, val list: MutableList<Any>,var viewModel: Notes_ViewModel, val click: (Notes,Draftdataclass,String,Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
     var notes: Notes?=null
     var draftnotes: Draftdataclass?=null
    class Notes_layout(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var delete = itemview.findViewById<LinearLayout>(R.id.deletebutton)
        var move = itemview.findViewById<LinearLayout>(R.id.movebutton)
        var day = itemview.findViewById<TextView>(R.id.day)
        var month = itemview.findViewById<TextView>(R.id.month)
        var time = itemview.findViewById<TextView>(R.id.time)
        var tittle = itemview.findViewById<TextView>(R.id.tittle)
        var discription = itemview.findViewById<TextView>(R.id.discription)
        var menu = itemview.findViewById<ImageView>(R.id.menu)
        var menulayout = itemview.findViewById<LinearLayout>(R.id.menulayout)

    }
    class AdsPagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val Adscontainer: FrameLayout = itemView.findViewById(R.id.nativeAd)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.notes_layout, parent, false)
                Notes_layout(view)
            }
            1 -> {
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        draftnotes= Draftdataclass(0,"",0,0,0,0,0,0,0,0,0,0,0,0,"",
            "","","","","","")
        notes= Notes(0,"","",0,0,0,0,0,0,0,0,0,0,0,0,0,"",
            "","","","","","")
        val currentItem = list[position]
        if (getItemViewType(position) == 0) {
            holder as Notes_layout
            if (currentItem is Notes){
                notes=Notes( id=currentItem.id,
                    currentItem.folderId,
                    tittle = currentItem.tittle,
                    background = currentItem.background,
                    mood = currentItem.mood,
                    stiker = currentItem.stiker,
                    font = currentItem.font,
                    fontsize = currentItem.fontsize,
                    fontposition = currentItem.fontposition,
                    x = currentItem.x,
                    y = currentItem.y,
                    currentItem.imagewidth,
                    currentItem.imageheight,
                    currentItem.imagex,
                    gliterx = currentItem.gliterx,
                    glitery = currentItem.glitery,
                    image = currentItem.image,
                    gliterimage = currentItem.gliterimage,
                    month = currentItem.month,
                    day = currentItem.day,
                    time = currentItem.time,
                    content = currentItem.content,
                    completedate = currentItem.completedate,
                )
                holder.tittle.text = currentItem.tittle
                holder.discription.text = currentItem.content
                holder.day.text = StringBuilder(currentItem.day)
                holder.month.text = StringBuilder(currentItem.month)
                holder.time.text = StringBuilder(currentItem.time)
                holder.delete.setOnClickListener {
                    click.invoke(currentItem,draftnotes!!,"delete",position)
                }
                holder.menu.setOnClickListener {
                    holder.menulayout.visibility=View.VISIBLE
                }
                holder.move.setOnClickListener {
                    holder.menulayout.visibility=View.GONE
                    click.invoke(currentItem,draftnotes!!,"move",position)
                }
                holder.itemView.setOnClickListener {
                    click.invoke(currentItem,draftnotes!!,"",position)
                }
            }else if (currentItem is Draftdataclass){

                draftnotes=Draftdataclass( id=currentItem.id,
                    tittle = currentItem.tittle,
                    background = currentItem.background,
                    mood = currentItem.mood,
                    stiker = currentItem.stiker,
                    font = currentItem.font,
                    fontsize = currentItem.fontsize,
                    fontposition = currentItem.fontposition,
                    x = currentItem.x,
                    y = currentItem.y,
                    currentItem.imagex,
                    currentItem.imagey,
                    gliterx = currentItem.gliterx,
                    glitery = currentItem.glitery,
                    image = currentItem.image,
                    gliterimage = currentItem.gliterimage,
                    month = currentItem.month,
                    day = currentItem.day,
                    time = currentItem.time,
                    content = currentItem.content,
                    completedate = currentItem.completedate,
                )
                holder.menu.visibility=View.GONE
                holder.tittle.text = currentItem.tittle
                holder.discription.text = currentItem.content
                holder.day.text = StringBuilder(currentItem.day)
                holder.month.text = StringBuilder(currentItem.month)
                holder.delete.setOnClickListener {
                    click.invoke(notes!!,currentItem,"delete",position)
                }
                holder.itemView.setOnClickListener {
                    click.invoke(notes!!,currentItem,"",position)
                }
            }


        }else if(getItemViewType(position)==1){
            holder as AdsPagesViewHolder
            currentItem as String
            AdsManager.showNativeAd(context,holder.Adscontainer,"NotesAdapter")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        return if (item is Notes)
            0
        else if (item is Draftdataclass)
            0
        else if (item is Folder)
            0
        else if (item is String)
            1
            else
            2
    }

}