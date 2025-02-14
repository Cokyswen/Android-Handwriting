package com.cokyswen.writelib.utils;

public class TimedPoint {
    public float x;
    public float y;
    public float pressure;
    public long timestamp;

    public TimedPoint set(float x, float y, float pressure, long timestamp) {
        this.x = x;
        this.y = y;
        this.pressure = pressure;
        this.timestamp = timestamp;
        return this;
    }

    public float velocityFrom(TimedPoint start) {
        long diff = this.timestamp - start.timestamp;
        if(diff <= 0) {
            diff = 1;
        }
        float velocity = distanceTo(start) / diff;
        if (Float.isInfinite(velocity) || Float.isNaN(velocity)) {
            velocity = 0;
        }
        return velocity;
    }

    public float distanceTo(TimedPoint point) {
        return (float) Math.sqrt(Math.pow(point.x - this.x, 2) + Math.pow(point.y - this.y, 2));
    }

    public float distanceAndPTo(TimedPoint point) {
        return (float) Math.sqrt(Math.pow(point.x - this.x, 2) + Math.pow(point.y - this.y, 2) + Math.pow(point.pressure - this.pressure, 2));
    }
}
