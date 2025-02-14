package com.cokyswen.writelib.manager

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.util.Log
import com.cokyswen.writelib.pen.BallPen
import com.cokyswen.writelib.pen.BasePen
import com.cokyswen.writelib.utils.TimedPoint
import kotlin.math.ceil
import kotlin.math.pow

class PointManager {

    protected val mPoints: ArrayList<TimedPoint> = ArrayList()
    protected val mPointsCache: ArrayList<TimedPoint> = ArrayList()

    private var mDrawPen: BasePen = BallPen()

    fun addPoint(mCanvas: Canvas, x: Float, y: Float, pressure: Float, eventTime: Long, isEnd: Boolean){
        val curPoint = getNewPoint(x, y, pressure, eventTime)
        var pointsCount = mPoints.size
        if(pointsCount > 0 && mPoints[pointsCount - 1].distanceTo(curPoint) == 0f){
            recyclePoint(curPoint)
            if(isEnd){
                if(pointsCount >= 3){
                    var count = mPoints.size
                    val firstPoint: TimedPoint = mPoints[count - 1]
                    val secondPoint: TimedPoint = mPoints[count - 2]
                    var x1 = 2 * firstPoint.x - secondPoint.x
                    var y1 = 2 * firstPoint.y - secondPoint.y

                    addPoint(mCanvas, x1, y1, firstPoint.pressure, firstPoint.timestamp, false)
                } else if(pointsCount == 1){
                    mDrawPen.draw(mCanvas, mPoints[0], mPoints[0], mPoints[0], mPoints[0])
                } else {
                    mDrawPen.draw(mCanvas, mPoints[0], mPoints[0], mPoints[0], mPoints[0])
                    mDrawPen.draw(mCanvas, mPoints[1], mPoints[1], mPoints[1], mPoints[1])
                }

                recycleAll()
            }
            return
        }

        mPoints.add(curPoint)
        pointsCount = mPoints.size

        if(pointsCount > 3){
            calculateCurvePoints(mCanvas, mPoints[0], mPoints[1], mPoints[2], mPoints[3])
            recyclePoint(mPoints.removeAt(0))
        } else if(pointsCount == 2){
            val firstPoint: TimedPoint = mPoints[0]
            val secondPoint: TimedPoint = mPoints[1]
            val x1 = 2 * firstPoint.x - secondPoint.x
            val y1 = 2 * firstPoint.y - secondPoint.y
            mPoints.add(0, getNewPoint(x1, y1, firstPoint.pressure, firstPoint.timestamp))
        }

        if(isEnd){
            if(pointsCount >= 3){
                var count = mPoints.size
                val firstPoint: TimedPoint = mPoints[count - 1]
                val secondPoint: TimedPoint = mPoints[count - 2]
                var x1 = 2 * firstPoint.x - secondPoint.x
                var y1 = 2 * firstPoint.y - secondPoint.y

                addPoint(mCanvas, x1, y1, firstPoint.pressure, firstPoint.timestamp, false)
            } else if(pointsCount == 1){
                mDrawPen.draw(mCanvas, mPoints[0], mPoints[0], mPoints[0], mPoints[0])
            } else {
                mDrawPen.draw(mCanvas, mPoints[0], mPoints[0], mPoints[0], mPoints[0])
                mDrawPen.draw(mCanvas, mPoints[1], mPoints[1], mPoints[1], mPoints[1])
            }

            recycleAll()
        }
    }

    fun setPenWidth(width: Float){
        mDrawPen.setPenWidth(width)
    }

    @SuppressLint("RestrictedApi")
    private fun calculateCurvePoints(
        mCanvas: Canvas,
        p0: TimedPoint,
        p1: TimedPoint,
        p2: TimedPoint,
        p3: TimedPoint
    ) {
        var tmp : TimedPoint = getNewPoint(p1.x, p1.y, p1.pressure, p1.timestamp)
        var dis = ceil(p1.distanceTo(p2) / pointCeil).toInt()

        for (tt in 1..dis){
            var amount = tt / (dis * 1.0f)

            var t0 = 0f
            var t1 = getT(t0, alpha, p0, p1)
            var t2 = getT(t1, alpha, p1, p2)
            var t3 = getT(t2, alpha, p2, p3)

            var t = t1 + (t2 - t1) * amount

            val A1x  = (t1 - t) / (t1 - t0) * p0.x + (t - t0) / (t1 - t0) * p1.x
            val A1y  = (t1 - t) / (t1 - t0) * p0.y + (t - t0) / (t1 - t0) * p1.y
            val A1p  = (t1 - t) / (t1 - t0) * p0.pressure + (t - t0) / (t1 - t0) * p1.pressure

            val A2x  = (t2 - t) / (t2 - t1) * p1.x + (t - t1) / (t2 - t1) * p2.x
            val A2y  = (t2 - t) / (t2 - t1) * p1.y + (t - t1) / (t2 - t1) * p2.y
            val A2p  = (t2 - t) / (t2 - t1) * p1.pressure + (t - t1) / (t2 - t1) * p2.pressure

            val A3x  = (t3 - t) / (t3 - t2) * p2.x + (t - t2) / (t3 - t2) * p3.x
            val A3y  = (t3 - t) / (t3 - t2) * p2.y + (t - t2) / (t3 - t2) * p3.y
            val A3p  = (t3 - t) / (t3 - t2) * p2.pressure + (t - t2) / (t3 - t2) * p3.pressure

            val B1x  = (t2 - t) / (t2 - t0) * A1x + (t - t0) / (t2 - t0) * A2x
            val B1y  = (t2 - t) / (t2 - t0) * A1y + (t - t0) / (t2 - t0) * A2y
            val B1p  = (t2 - t) / (t2 - t0) * A1p + (t - t0) / (t2 - t0) * A2p

            val B2x  = (t3 - t) / (t3 - t1) * A2x + (t - t1) / (t3 - t1) * A3x
            val B2y  = (t3 - t) / (t3 - t1) * A2y + (t - t1) / (t3 - t1) * A3y
            val B2p  = (t3 - t) / (t3 - t1) * A2p + (t - t1) / (t3 - t1) * A3p

            val Cx  = (t2 - t) / (t2 - t1) * B1x + (t - t1) / (t2 - t1) * B2x
            val Cy  = (t2 - t) / (t2 - t1) * B1y + (t - t1) / (t2 - t1) * B2y
            val Cp  = (t2 - t) / (t2 - t1) * B1p + (t - t1) / (t2 - t1) * B2p

            var pressure = ((p2.pressure - p1.pressure) * amount + p1.pressure)
            var timestamp = ((p2.timestamp - p1.timestamp) * amount + p1.timestamp).toLong()

            var result = getNewPoint(Cx, Cy, Cp, timestamp)
            mDrawPen.draw(mCanvas, tmp, result, result, result)
            recyclePoint(tmp)
            tmp = result
        }
        mDrawPen.draw(mCanvas, tmp, p2, p2, p2)
        recyclePoint(tmp)
    }

    fun getT(t: Float, alpha: Float, p0: TimedPoint, p1: TimedPoint): Float{
        val distance = p0.distanceTo(p1)
        return t + distance.pow(alpha)
    }

    private fun recyclePoint(point: TimedPoint) {
        mPointsCache.add(point)
    }

    private fun recycleAll(){
        mPointsCache.addAll(mPoints)
        mPoints.clear()
    }

    private fun getNewPoint(x: Float, y: Float, pressure: Float, eventTime: Long): TimedPoint{
        val mCacheSize = mPointsCache.size
        val timedPoint = if (mCacheSize == 0) {
            TimedPoint()
        } else {
            mPointsCache.removeAt(mCacheSize - 1)
        }
        timedPoint.set(x, y, pressure, eventTime)

        return timedPoint
    }

    private var alpha = 0.5f

    fun changeAlpha(alpha: Float){
        this.alpha = alpha
    }

    private var pointCeil = 1f

    fun setPointCeil(pointCeil: Float){
        this.pointCeil = pointCeil
    }

}