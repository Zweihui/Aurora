package com.zwh.mvparms.eyepetizer.mvp.ui.widget;

/**
 * Created by Administrator on 2017/8/17 0017.
 */

public class CircleInfo {
    private float x;
    private float y;
    private float radius;
    private boolean canDraw=false;

    public CircleInfo() {
    }

    public CircleInfo(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setCircleInfo(float x,float y,float radius){
        setX(x);
        setY(y);
        setRadius(radius);
    }

    public void setCircleInfo(float x,float y,float radius,boolean canDraw){
        setCircleInfo(x,y,radius);
        setCanDraw(canDraw);
    }

    public boolean isCanDraw() {
        return canDraw;
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }
}
