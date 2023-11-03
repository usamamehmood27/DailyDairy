package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.DailyDiary.R
import com.example.DailyDiary.databinding.BottomsheetBinding
import com.example.DailyDiary.databinding.EditextLayoutBinding
import com.example.DailyDiary.databinding.FragmentDairyPageBinding
import com.example.DailyDiary.databinding.StikerBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.adapter.Background_Apater
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.FontData
import kamai.app.ads.AdsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BottomSheet_Utils {
    companion object {
        var newSizeInSp = 18
        fun backgroundbottomsheet(
            context: Activity,
            binding: FragmentDairyPageBinding,
            backgroundlist: MutableList<Any>,
            gradiantlist: MutableList<Any>,
            click: (Int) -> Unit
        ) {
            var backgroundid = 0
            val bottomSheetDialog = BottomSheetDialog(context)
            val bottomSheetBinding = BottomsheetBinding.inflate(LayoutInflater.from(context))
            bottomSheetDialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
            bottomSheetDialog.setContentView(bottomSheetBinding.root)
            bottomSheetDialog.show()
            bottomSheetBinding.apply {
                CoroutineScope(Dispatchers.Main).launch {
                    recyclerview.layoutManager = GridLayoutManager(context, 4)
                    recyclerview.adapter =
                        Background_Apater(context, backgroundlist) { background, file ->
                            backgroundid = background

                            apply.setBackgroundResource(R.drawable.create_button)
                            apply.setTextColor(Color.parseColor("#FFFFFF"))
                        }

                }
                apply.setOnClickListener {
                    AdsManager.showIntersWithClick(context) {
                        binding.mainlayout.setBackgroundResource(backgroundid)
                        click.invoke(backgroundid)
                        bottomSheetDialog.dismiss()
                    }

                }
                gradiant.setOnClickListener {

                    CoroutineScope(Dispatchers.Main).launch {
                        gradiantview.visibility = View.VISIBLE
                        textureview.visibility = View.INVISIBLE
                        gradianttext.setTextColor(Color.parseColor("#DC248F"))
                        texturetext.setTextColor(Color.parseColor("#727377"))
                        recyclerview.adapter =
                            Background_Apater(context, gradiantlist) { background, file ->
                                backgroundid = background
                                apply.setBackgroundResource(R.drawable.create_button)
                                apply.setTextColor(Color.parseColor("#FFFFFF"))

                            }
                    }

                }

                texture.setOnClickListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        gradiantview.visibility = View.INVISIBLE
                        textureview.visibility = View.VISIBLE
                        texturetext.setTextColor(Color.parseColor("#DC248F"))
                        gradianttext.setTextColor(Color.parseColor("#727377"))
                        recyclerview.adapter = Background_Apater(
                            context, backgroundlist
                        ) { background, file ->
                            backgroundid = background
                            apply.setBackgroundResource(R.drawable.create_button)
                            apply.setTextColor(Color.parseColor("#FFFFFF"))

                        }
                    }
                }
            }

        }

        fun edittextbottomsheet(
            context: Activity,
            binding: FragmentDairyPageBinding,
            click: (FontData) -> Unit
        ) {
            var fontsize = 0
            var fontid = 0
            var fontposition = 0

            val bottomSheetDialog = BottomSheetDialog(context)
            val bottomSheetBinding = EditextLayoutBinding.inflate(LayoutInflater.from(context))
            bottomSheetDialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
            bottomSheetDialog.setContentView(bottomSheetBinding.root)
            bottomSheetDialog.show()

            bottomSheetBinding.apply {
                recyclerview.layoutManager = GridLayoutManager(context, 3)
                recyclerview.adapter =
                    Background_Apater(context, util.fontlist(context)) { background, file ->
                        fontid = background
                        binding.createnote.typeface = ResourcesCompat.getFont(context, fontid)
                        apply.setBackgroundResource(R.drawable.create_button)
                        apply.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                bold.setOnClickListener {
                    bold.setBackgroundResource(R.drawable.font_icon)
                    italic.setBackgroundResource(R.drawable.font_icon_white)
                    simple.setBackgroundResource(R.drawable.font_icon_white)
                    binding.createnote.setTypeface(null, Typeface.BOLD)

                }
                italic.setOnClickListener {
                    bold.setBackgroundResource(R.drawable.font_icon_white)
                    italic.setBackgroundResource(R.drawable.font_icon)
                    simple.setBackgroundResource(R.drawable.font_icon_white)
                    binding.createnote.setTypeface(null, Typeface.ITALIC)
                }
                simple.setOnClickListener {
                    bold.setBackgroundResource(R.drawable.font_icon_white)
                    italic.setBackgroundResource(R.drawable.font_icon_white)
                    simple.setBackgroundResource(R.drawable.font_icon)
                    binding.createnote.setTypeface(null, Typeface.NORMAL)
                }
                aminus.setOnClickListener {
                    apply.setBackgroundResource(R.drawable.create_button)
                    apply.setTextColor(Color.parseColor("#FFFFFF"))
                    newSizeInSp--
                    binding.createnote.setTextSize(
                        TypedValue.COMPLEX_UNIT_SP,
                        newSizeInSp.toFloat()
                    )
                    fontsize = newSizeInSp
                }
                aplus.setOnClickListener {
                    apply.setBackgroundResource(R.drawable.create_button)
                    apply.setTextColor(Color.parseColor("#FFFFFF"))
                    newSizeInSp++
                    binding.createnote.setTextSize(
                        TypedValue.COMPLEX_UNIT_SP,
                        newSizeInSp.toFloat()
                    )
                    fontsize = newSizeInSp
                }
                left.setOnClickListener {
                    left.setBackgroundResource(R.drawable.font_icon)
                    center.setBackgroundResource(R.drawable.font_icon_white)
                    right.setBackgroundResource(R.drawable.font_icon_white)
                    fontposition = Gravity.START
                    binding.createnote.gravity = fontposition
                    binding.imagecontainer.gravity = fontposition
//                    val layoutParams =
//                        binding.frameContainerimage.layoutParams as ConstraintLayout.LayoutParams
//                    layoutParams.leftMargin = fontposition
                }
                center.setOnClickListener {
                    left.setBackgroundResource(R.drawable.font_icon_white)
                    center.setBackgroundResource(R.drawable.font_icon)
                    right.setBackgroundResource(R.drawable.font_icon_white)
                    fontposition = Gravity.CENTER
                    binding.createnote.gravity = fontposition
                    binding.imagecontainer.gravity = fontposition
//                    val layoutParams =
//                        binding.frameContainerimage.layoutParams as ConstraintLayout.LayoutParams
//                    layoutParams.leftMargin = fontposition
                }
                right.setOnClickListener {
                    left.setBackgroundResource(R.drawable.font_icon_white)
                    center.setBackgroundResource(R.drawable.font_icon_white)
                    right.setBackgroundResource(R.drawable.font_icon)
                    fontposition = Gravity.END
                    binding.createnote.gravity = fontposition
                    binding.imagecontainer.gravity = fontposition
//                    val layoutParams =
//                        binding.frameContainerimage.layoutParams as ConstraintLayout.LayoutParams
//                    layoutParams.leftMargin = fontposition
                }

                apply.setOnClickListener {
                    AdsManager.showIntersWithClick(context) {
                        click.invoke(FontData(fontid, fontsize, fontposition))
                        bottomSheetDialog.dismiss()
                    }

                }
            }
            bottomSheetDialog.setOnCancelListener {
                binding.createnote.setTypeface(null, Typeface.NORMAL)
                binding.createnote.gravity = Gravity.START
                binding.imagecontainer.gravity = Gravity.START
                binding.createnote.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20.0f)
                binding.createnote.typeface = ResourcesCompat.getFont(context, R.font.quicksand)
            }
        }

        fun stikerbottomsheet(
            context: Activity,
            binding: FragmentDairyPageBinding,
            click: (Int) -> Unit
        ) {
            var number = 0
            var stikerid = 0
            val bottomSheetDialog = BottomSheetDialog(context)
            val bottomSheetBinding = StikerBottomsheetBinding.inflate(LayoutInflater.from(context))
            bottomSheetDialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
            bottomSheetDialog.setContentView(bottomSheetBinding.root)
            bottomSheetDialog.show()

            bottomSheetBinding.apply {
                recyclerview.layoutManager = GridLayoutManager(context, 3)
                recyclerview.adapter =
                    Background_Apater(context, util.tagslist(context)) { background, file ->
                        number = background
                        apply.setBackgroundResource(R.drawable.create_button)
                        apply.setTextColor(Color.parseColor("#FFFFFF"))


                    }

                emojies.setOnClickListener {
                    emojiesview.visibility = View.VISIBLE
                    tagsview.visibility = View.INVISIBLE
                    emojiesttext.setTextColor(Color.parseColor("#DC248F"))
                    tagstext.setTextColor(Color.parseColor("#727377"))
                    recyclerview.layoutManager = GridLayoutManager(context, 4)
                    recyclerview.adapter =
                        Background_Apater(
                            context,
                            util.emojilist(context)
                        ) { background, file ->
                            number = background
                            apply.setBackgroundResource(R.drawable.create_button)
                            apply.setTextColor(Color.parseColor("#FFFFFF"))
                            binding.imageView.setImageResource(stikerid)
                        }
                }

                tags.setOnClickListener {
                    emojiesview.visibility = View.INVISIBLE
                    tagsview.visibility = View.VISIBLE
                    tagstext.setTextColor(Color.parseColor("#DC248F"))
                    emojiesttext.setTextColor(Color.parseColor("#727377"))
                    recyclerview.layoutManager = GridLayoutManager(context, 3)
                    recyclerview.adapter =
                        Background_Apater(
                            context,
                            util.tagslist(context)
                        ) { background, file ->
                            number = background
                            binding.imageView.setImageResource(stikerid)
                            apply.setBackgroundResource(R.drawable.create_button)
                            apply.setTextColor(Color.parseColor("#FFFFFF"))

                        }
                }
                apply.setOnClickListener {
                    AdsManager.showIntersWithClick(context) {
                        stikerid = number
                        click.invoke(stikerid)
                        bottomSheetDialog.dismiss()
                    }

                }
            }

        }
    }
}

