package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.DailyDiary.R
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.SharedPref
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.ModelClasLanguges
//import kamai.app.ads.AdsManager
//
class LanguageAdapter(
    val context: Activity? = null,
    val listLang: List<Any>? = null,
    var click: (String, Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.language_items_layout, parent, false)
            ViewHolder(view)
        } else {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.ad_layoyt, parent, false)
            AdsViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 0) {
            val viewHolder: ViewHolder = holder as ViewHolder
            val modelClasLanguges: ModelClasLanguges = listLang!![position] as ModelClasLanguges
            if (SharedPref.getString(
                    "PHOTO_LANG_SELECTED",
                    "en"
                ) == modelClasLanguges.language_code
            ) {
                holder.lang_layout.setBackgroundResource(R.drawable.language_bg_sellected)
            }else{
                holder.lang_layout.setBackgroundResource(R.drawable.language_bg)
            }
            viewHolder.lang_tv.setText(modelClasLanguges.name)
            viewHolder.lang_img.setImageResource(modelClasLanguges.imgUrl)

            viewHolder.lang_layout.setOnClickListener {
                click.invoke(modelClasLanguges.language_code!!, position)

            }

        } else {
            holder as AdsViewHolder
//            AdsManager.showNativeAd(
//                context!!,
//                holder.frameLayout_native, "LanguageAdapter"
//            )

        }
    }

    override fun getItemCount(): Int {
        return listLang!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lang_tv: TextView
        var lang_img: ImageView
        var lang_layout: ConstraintLayout

        init {
            lang_tv = itemView.findViewById(R.id.text_lang)
            lang_img = itemView.findViewById(R.id.img_lang)
            lang_layout = itemView.findViewById(R.id.lang_layout)
        }
    }

    class AdsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var frameLayout_native: FrameLayout

        init {
            frameLayout_native = itemView.findViewById(R.id.nativeAd)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (listLang!![position] is ModelClasLanguges) {
            0
        } else if (listLang[position] is String)
            1
        else
            1

    }


}