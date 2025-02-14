package com.cokyswen.writelib.pen

import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import com.cokyswen.writelib.utils.TimedPoint

class SteelPen: BasePen() {

    init {
        mPaint.strokeWidth = 1f
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.isAntiAlias = true
        mPaint.color = paintColor

//        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
    }

    override fun draw(mCanvas: Canvas, startPoint: TimedPoint, c1: TimedPoint, c2: TimedPoint, endPoint: TimedPoint) {
        getWidthByPressure(startPoint.pressure)
        mCanvas.drawPoint(startPoint.x, startPoint.y, mPaint)
//        mCanvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, mPaint)
    }

    fun getWidthByPressure(pressure: Float){
        setPenWidth(maxStrokeWidth * pressure + (1 - pressure) * minStrokeWidth)
    }

}