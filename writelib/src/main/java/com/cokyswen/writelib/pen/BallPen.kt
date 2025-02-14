package com.cokyswen.writelib.pen

import android.graphics.Canvas
import android.graphics.Paint
import com.cokyswen.writelib.utils.TimedPoint

class BallPen: BasePen() {

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
//        mCanvas.drawPoint(startPoint.x, startPoint.y, mPaint)
        mCanvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, mPaint)
    }

}