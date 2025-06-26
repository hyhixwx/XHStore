package com.hyhua.xhui.banner.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

/**
 * 实现了自动翻页的ViewPager
 */
public class XHViewPager extends ViewPager {

    private int mIntervalTime;
    /**
     * 是否开启自动轮播
     */
    private boolean mAutoPlay = true;
    private boolean isLayout;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            // 切换到下一个
            next();
            // 重复
            mHandler.postDelayed(this, mIntervalTime);
        }
    };

    public XHViewPager(@NonNull Context context) {
        super(context);
    }

    public XHViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAutoPlay(boolean autoPlay) {
        mAutoPlay = autoPlay;
        if (!mAutoPlay) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    public void setIntervalTime(int intervalTime) {
        mIntervalTime = intervalTime;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        isLayout = true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 通过反射修改ViewPager私有变量，解决嵌套于滑动组件内时（如RecyclerView），动画消失的bug
        if (isLayout && getAdapter() != null && getAdapter().getCount() > 0) {
            try {
                Field mFirstLayout = ViewPager.class.getDeclaredField("mFirstLayout");
                mFirstLayout.setAccessible(true);
                mFirstLayout.set(this, false);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        // 解决ViewPager滑动到一半时被回收导致动画卡住
        if (((Activity) getContext()).isFinishing()) {
            super.onDetachedFromWindow();
        }
        stop();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                start();
                break;
            default:
                stop();
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void start() {
        mHandler.removeCallbacksAndMessages(null);
        if (mAutoPlay) {
            mHandler.postDelayed(mRunnable, mIntervalTime);
        }
    }

    public void stop() {
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 设置ViewPager的滚动速度
     *
     * @param duration page切换的时间长度
     */
    public void setScrollerDuration(int duration) {
        Field scrollerField = null;
        try {
            scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            scrollerField.set(this, new XHBannerScroller(getContext(), duration));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置下一个要显示的item，并返回item的pos
     */
    private int next() {
        int nextPos = -1;
        if (getAdapter() == null || getAdapter().getCount() <= 1) {
            stop();
            return nextPos;
        }
        nextPos = getCurrentItem() + 1;
        // 下一个索引大于adapter的view的最大数量时重新开始
        if (nextPos >= getAdapter().getCount()) {
            // 获取第一个item的索引
            nextPos = ((XHBannerAdapter) getAdapter()).getFirstShowItemPosition();
        }
        setCurrentItem(nextPos, true);
        return nextPos;
    }
}
