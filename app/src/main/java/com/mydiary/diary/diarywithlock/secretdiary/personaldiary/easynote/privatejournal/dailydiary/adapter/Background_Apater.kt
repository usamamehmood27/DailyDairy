package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.DailyDiary.R
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.StikerdataClass
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.EmojiDataClass
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.FontClass
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.GallerY_DataClass

class Background_Apater(var context: Context,var listitem: MutableList<Any>,var click:(Int,String)->Unit):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var sellectedPosition=-1
    class backgroundlayout(itemview: View) : RecyclerView.ViewHolder(itemview){
        var image = itemview.findViewById<ImageView>(R.id.image)
        var back = itemview.findViewById<LinearLayout>(R.id.back)

    }
    class stikerlayout(itemview: View) : RecyclerView.ViewHolder(itemview){
        var image = itemview.findViewById<ImageView>(R.id.image)
        var back = itemview.findViewById<LinearLayout>(R.id.back)

    }
    class emojilayout(itemview: View) : RecyclerView.ViewHolder(itemview){
        var image = itemview.findViewById<ImageView>(R.id.image)
        var back = itemview.findViewById<LinearLayout>(R.id.back)

    }
    class fontlayout(itemview: View) : RecyclerView.ViewHolder(itemview){
        var fonttext = itemview.findViewById<TextView>(R.id.fonttext)

    }
    class filelayout(itemview: View) : RecyclerView.ViewHolder(itemview){
        var galleryimage = itemview.findViewById<ImageView>(R.id.galleryimage)
        var tickimage = itemview.findViewById<ImageView>(R.id.tickimage)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.backgroun_layout, parent, false)
                backgroundlayout(view)
            }
            1 -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.font_layout, parent, false)
                fontlayout(view)
            }
            2 -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.image_layout, parent, false)
                filelayout(view)
            }
            3 -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.notes_layout, parent, false)
                filelayout(view)
            }
            4 -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.stiker_layout, parent, false)
                stikerlayout(view)
            }
            5 -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.emoji_layout, parent, false)
                emojilayout(view)
            }
            else -> {
                throw IllegalArgumentException("Invailed viewtype provided")
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position)==0){
            holder as backgroundlayout
            val currentItem=listitem[position] as Int
            Glide.with(context).load(currentItem).into(holder.image)
            if (position==sellectedPosition){
                holder.back.setBackgroundResource(R.drawable.background_bg)
            }else{
                holder.back.setBackgroundColor(Color.parseColor("#00000000"))
            }
            holder.itemView.setOnClickListener {
                sellectedPosition=position
                notifyDataSetChanged()
                click.invoke(currentItem,"")
            }
        }
        if (getItemViewType(position)==4){
            holder as stikerlayout
            val currentItem=listitem[position] as StikerdataClass
            Glide.with(context).load(currentItem.stikerId).into(holder.image)

            if (position==sellectedPosition){
                holder.back.setBackgroundResource(R.drawable.stiker_bg)
            }else{
                holder.back.setBackgroundColor(Color.parseColor("#00000000"))
            }
            holder.itemView.setOnClickListener {
                sellectedPosition=position
                notifyDataSetChanged()
                click.invoke(currentItem.stikerId,"")
            }
        }
        if (getItemViewType(position)==5){
            holder as emojilayout
            val currentItem=listitem[position] as EmojiDataClass
            Glide.with(context).load(currentItem.emoojiId).into(holder.image)
            if (position==sellectedPosition){
                holder.back.setBackgroundResource(R.drawable.stiker_bg)
            }else{
                holder.back.setBackgroundColor(Color.parseColor("#00000000"))
            }
            holder.itemView.setOnClickListener {
                sellectedPosition=position
                notifyDataSetChanged()
                click.invoke(currentItem.emoojiId,"")
            }
        }
        if (getItemViewType(position)==1) {
            holder as fontlayout
            val currentItem=listitem[position] as FontClass
            holder.fonttext.text=currentItem.name
            if (position==sellectedPosition){
                holder.fonttext.setBackgroundColor(Color.parseColor("#DB8B56"))
                holder.fonttext.setTextColor(Color.WHITE)

            }else{
                holder.fonttext.setBackgroundColor(Color.parseColor("#F9C9A0"))
                holder.fonttext.setTextColor(Color.BLACK)
            }
            holder.itemView.setOnClickListener {
                sellectedPosition=position
                click.invoke(currentItem.font,"")
                notifyDataSetChanged()

            }
        }
        if (getItemViewType(position)==2) {
            holder as filelayout
            val currentItem=listitem[position] as GallerY_DataClass
            if (position==sellectedPosition){
                holder.tickimage.visibility=View.VISIBLE
            }else{
                holder.tickimage.visibility=View.GONE
            }
            Glide.with(context).load(currentItem.path).into( holder.galleryimage)
            holder.itemView.setOnClickListener {
                sellectedPosition=position
                click.invoke(0,currentItem.path!!)
                notifyDataSetChanged()
            }
        }

    }
    override fun getItemCount(): Int {
        return listitem.size
    }


    override fun getItemViewType(position: Int): Int {
        val item=listitem[position]
        return if (item is Int)
            0
        else if (item is FontClass)
            1
        else if (item is GallerY_DataClass)
            2
        else if (item is StikerdataClass)
            4
        else if (item is EmojiDataClass)
            5
        else
            1
    }
}