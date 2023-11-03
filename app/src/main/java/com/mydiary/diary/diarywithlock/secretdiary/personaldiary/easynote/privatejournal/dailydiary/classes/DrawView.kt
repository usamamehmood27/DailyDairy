package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var isDrawingEnabled = true
    private val paths = mutableListOf<Path>()
    private val paints = mutableListOf<Paint>()
    private val undonePaths = mutableListOf<Path>()
    private val undonePaints = mutableListOf<Paint>()
    companion object{
        var currentColor = Color.BLACK

    }
//    init {
//        val paint = createPaintWithColor(currentColor)
//        paints.add(paint)
//    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in paths.indices) {
            canvas.drawPath(paths[i], paints[i])
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isDrawingEnabled) {
            return false // Disable drawing when isDrawingEnabled is false
        }

        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startNewPath()
                moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                // Perform any necessary actions when the user finishes drawing
            }
        }

        invalidate()
        return true
    }

    private fun startNewPath() {
        val path = Path()
        paths.add(path)
        paints.add(createPaintWithColor(currentColor))
    }

    private fun moveTo(x: Float, y: Float) {
        val lastPathIndex = paths.lastIndex
        paths[lastPathIndex].moveTo(x, y)
    }

    private fun lineTo(x: Float, y: Float) {
        val lastPathIndex = paths.lastIndex
        paths[lastPathIndex].lineTo(x, y)
    }

    private fun createPaintWithColor(color: Int): Paint {
        return Paint().apply {
            this.color = color
            strokeWidth = 10f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
    }

//    fun setCurrentColor(color: Int) {
//        invalidate()
//        currentColor = color
//
//    }

    fun undo() {
        if (paths.isNotEmpty()) {
            val lastPathIndex = paths.lastIndex
            if (lastPathIndex >= 0) {
                undonePaths.add(paths[lastPathIndex])
                undonePaints.add(paints[lastPathIndex])
                paths.removeAt(lastPathIndex)
                paints.removeAt(lastPathIndex)
                invalidate()
            }
        }
    }

    fun redo() {
        if (undonePaths.isNotEmpty()) {
            val lastUndoneIndex = undonePaths.lastIndex
            paths.add(undonePaths.removeAt(lastUndoneIndex))
            paints.add(undonePaints.removeAt(lastUndoneIndex))
            invalidate()
        }
    }

    fun clearDrawing() {
        paths.clear()
        paints.clear()
        undonePaths.clear()
        undonePaints.clear()
        invalidate()
    }

    fun setDrawingEnabled(enabled: Boolean) {
        isDrawingEnabled = enabled
    }
}