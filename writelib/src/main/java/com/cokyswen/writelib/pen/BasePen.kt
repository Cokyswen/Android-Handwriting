package com.cokyswen.writelib.pen

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import com.cokyswen.writelib.utils.TimedPoint

abstract class BasePen {

    protected val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG and Paint.DITHER_FLAG)

    protected var strokeWidth = 5f
    protected var paintColor = Color.BLACK

    protected var minStrokeWidth = 1f
    protected var maxStrokeWidth = 20f

    init {
        mPaint.strokeWidth = strokeWidth
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.isAntiAlias = true
        mPaint.color = paintColor
//        mPaint.strokeMiterLimit = 50f

//        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
    }

    abstract fun draw(mCanvas: Canvas, startPoint: TimedPoint, c1: TimedPoint, c2: TimedPoint, endPoint: TimedPoint)

    fun getPaint(): Paint{
        return mPaint
    }

    open fun setPenWidth(width: Float){
        strokeWidth = width
        mPaint.strokeWidth = strokeWidth
    }

    open fun setPenColor(color: Int){
        paintColor = color
        mPaint.color = paintColor
    }

}