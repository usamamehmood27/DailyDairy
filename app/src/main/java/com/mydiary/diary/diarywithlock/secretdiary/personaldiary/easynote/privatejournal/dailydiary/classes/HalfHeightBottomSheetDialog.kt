package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes

import android.content.Context
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog

class HalfHeightBottomSheetDialog(context: Context) : BottomSheetDialog(context) {

    override fun onStart() {
        super.onStart()
        val windowHeight = context.resources.displayMetrics.heightPixels
        val halfHeight = windowHeight / 2
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, halfHeight)
    }
}