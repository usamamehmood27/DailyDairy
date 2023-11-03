package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.DailyDiary.R
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Draftdataclass
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Notes
import kamai.app.ads.AdsManager

class SearchAdapter (val context: Activity, val list: MutableList<Any>, val click: (Notes) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class Search_layout(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var backgroundimages = itemview.findViewById<ConstraintLayout>(R.id.backgroundimage)
        var days = itemview.findViewById<TextView>(R.id.day)
        var months = itemview.findViewById<TextView>(R.id.month)
        var times= itemview.findViewById<TextView>(R.id.time)
        var tittles = itemview.findViewById<TextView>(R.id.tittle)
        var discriptions = itemview.findViewById<TextView>(R.id.discription)

    }
    class AdsPagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val Adscontainer: FrameLayout = itemView.findViewById(R.id.nativeAd)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.search_layout, parent, false)
                Search_layout(view)
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 0) {
            holder as Search_layout
            val currentItem = list[position] as Notes
            holder.tittles.text = currentItem.tittle
            holder.discriptions.text = currentItem.content
            holder.days.text = StringBuilder(currentItem.day)
            holder.months.text = StringBuilder(currentItem.month)

            //  holder.backgroundimage.setBackgroundResource(currentItem.background)
            holder.itemView.setOnClickListener {
                click.invoke(currentItem)
            }
        }else if(getItemViewType(position)==1){
            holder as AdsPagesViewHolder
            val currentItem = list[position]
            currentItem as String
            AdsManager.showNativeAd(context,holder.Adscontainer,"SearchAdaptor")
        }
    }
    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        return if (item is Notes)
            0
        else if (item is String)
            1
        else
            2
    }
}