package com.cokyswen.writelib.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.cokyswen.writelib.manager.PointManager

class HandwritingView(context: Context, attrs: AttributeSet): View(context, attrs) {

    private var mBitmap: Bitmap
    private var mCanvas: Canvas
    private val mPaint: Paint = Paint()

    val pointManager = PointManager()

    init {
        val screenWidth = (context.resources.displayMetrics).widthPixels
        val screenHeight = (context.resources.displayMetrics).heightPixels
        mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN ->{
                if(event.historySize > 0){
                    for ( i in 0..< event.historySize) {
                        pointManager.addPoint(mCanvas
                            , event.getHistoricalX(i),
                            event.getHistoricalY(i),
                            event.getHistoricalPressure(i),
                            event.getHistoricalEventTime(i),false)
                    }
                }
                pointManager.addPoint(mCanvas, event.x, event.y, event.pressure, event.eventTime, false)
            }
            MotionEvent.ACTION_MOVE ->{
                if(event.historySize > 0){
                    for ( i in 0..< event.historySize) {
                        pointManager.addPoint(mCanvas,
                            event.getHistoricalX(i),
                            event.getHistoricalY(i),
                            event.getHistoricalPressure(i),
                            event.getHistoricalEventTime(i),false)
                    }
                }
                pointManager.addPoint(mCanvas, event.x, event.y, event.pressure, event.eventTime, false)
            }
            MotionEvent.ACTION_UP ->{
                if(event.historySize > 0){
                    for ( i in 0..< event.historySize) {
                        pointManager.addPoint(mCanvas,
                            event.getHistoricalX(i),
                            event.getHistoricalY(i),
                            event.getHistoricalPressure(i),
                            event.getHistoricalEventTime(i),false)
                    }
                }
                pointManager.addPoint(mCanvas, event.x, event.y, event.pressure, event.eventTime, true)
            }
        }
        postInvalidate()
        return true
    }

    fun clearView(){
        mCanvas.drawColor(Color.WHITE)
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(mBitmap, 0f, 0f, mPaint)
    }
}