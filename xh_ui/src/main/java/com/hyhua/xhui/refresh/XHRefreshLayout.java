package com.hyhua.xhui.refresh;

import static com.hyhua.xhui.refresh.XHOverView.XHRefreshState.STATE_INIT;
import static com.hyhua.xhui.refresh.XHOverView.XHRefreshState.STATE_OVER;
import static com.hyhua.xhui.refresh.XHOverView.XHRefreshState.STATE_OVER_RELEASE;
import static com.hyhua.xhui.refresh.XHOverView.XHRefreshState.STATE_REFRESH;
import static com.hyhua.xhui.refresh.XHOverView.XHRefreshState.STATE_VISIBLE;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyhua.xhlibrary.log.XHLog;
import com.hyhua.xhlibrary.util.XHScrollUtil;

public class XHRefreshLayout extends FrameLayout implements XHRefresh {
    private XHOverView.XHRefreshState mState;
    private GestureDetector mGestureDetector;
    private XHRefreshListener mXHRefreshListener;
    protected XHOverView mXHOverView;
    private int mLastY;
    private boolean disableRefreshScroll;
    private AutoScroller mScroller;

    public XHRefreshLayout(@NonNull Context context) {
        this(context, null);
    }

    public XHRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XHRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private final XHGestureListener gestureListener = new XHGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceX) > Math.abs(distanceY) || mXHRefreshListener != null && !mXHRefreshListener.enableRefresh()) {
                // 横向滑动，或刷新被禁止时不做处理
                return false;
            }
            if (disableRefreshScroll && mState == STATE_REFRESH) {
                // 刷新状态下开启了禁止滚动
                return true;
            }
            XHLog.d("asd");
            View head = getChildAt(0);
            View child = XHScrollUtil.findScrollableChild(XHRefreshLayout.this);
            if (XHScrollUtil.childScrolled(child)) {
                // 如果列表发生了滚动¬
                return false;
            }
            // 没有刷新或没有达到刷新的距离，且头部已经划出或下拉
            if ((mState != STATE_REFRESH || head.getBottom() <= mXHOverView.mPullRefreshHeight) && (head.getBottom() > 0 || distanceY <= 0f)) {
                // 还在滑动中
                if (mState != STATE_OVER_RELEASE) {
                    int speed;
                    // 计算速度
                    if (child.getTop() < mXHOverView.mPullRefreshHeight) {
                        speed = (int) (mLastY / mXHOverView.minDamping);
                    } else {
                        speed = (int) (mLastY / mXHOverView.maxDamping);
                    }
                    // 如果是正在刷新状态，则不允许在滑动的时候改变状态
                    boolean consumed = moveDown(speed, false);
                    mLastY = (int) -distanceY;
                    return consumed;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    };

    /**
     * 根据偏移量移动header与child
     *
     * @param offsetY 偏移量
     * @param auto    是否自动滚动触发
     * @return 是否消费了事件
     */
    private boolean moveDown(int offsetY, boolean auto) {
        View head = getChildAt(0);
        View child = getChildAt(1);
        int childTop = child.getTop() + offsetY;
        if (childTop <= 0) {// 异常情况
            offsetY = -child.getTop();
            // 移动head与child的位置到初始位置
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (mState != STATE_REFRESH) {
                mState = STATE_INIT;
            }
        } else if (mState == STATE_REFRESH && childTop > mXHOverView.mPullRefreshHeight) {// 如果正在下拉刷新中，禁止继续下拉
            return false;
        } else if (childTop <= mXHOverView.mPullRefreshHeight) {// 还没超出设定的刷新距离
            if (mXHOverView.getState() != STATE_VISIBLE && !auto) {// 头部开始显示
                mXHOverView.onVisible();
                mXHOverView.setState(mState = STATE_VISIBLE);
                mState = STATE_VISIBLE;
            }
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (childTop == mXHOverView.mPullRefreshHeight && mState == STATE_OVER_RELEASE) {
                refresh();
            }
        } else {
            if (mXHOverView.getState() != STATE_OVER && !auto) {
                // 超出刷新位置
                mXHOverView.onOver();
                mXHOverView.setState(STATE_OVER);
            }
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
        }
        if (mXHOverView != null) {
            mXHOverView.onScroll(head.getBottom(), mXHOverView.mPullRefreshHeight);
        }
        return true;
    }

    private void refresh() {
        if (mXHRefreshListener != null) {
            mState = STATE_REFRESH;
            mXHOverView.onRefresh();
            mXHOverView.setState(STATE_REFRESH);
            mXHRefreshListener.onRefresh();
        }
    }

    private void init() {
        mGestureDetector = new GestureDetector(getContext(), gestureListener);
        mScroller = new AutoScroller();
    }

    @Override
    public void setDisableRefreshScroll(boolean disableRefreshScroll) {
        this.disableRefreshScroll = disableRefreshScroll;
    }

    @Override
    public void refreshFinished() {
        View head = getChildAt(0);
        mXHOverView.onFinish();
        mXHOverView.setState(STATE_INIT);
        int bottom = head.getBottom();
        if (bottom > 0) {
            recover(bottom);
        }
        mState = STATE_INIT;
    }

    @Override
    public void setRefreshListener(XHRefreshListener xhRefreshListener) {
        this.mXHRefreshListener = xhRefreshListener;
    }

    @Override
    public void setRefreshOverView(XHOverView xhOverView) {
        if (this.mXHOverView != null) {
            removeView(mXHOverView);
        }
        this.mXHOverView = xhOverView;
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(xhOverView, 0, params);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!mScroller.isFinished()) {
            return false;
        }
        View head = getChildAt(0);
        if (ev.getAction() == MotionEvent.ACTION_UP
                || ev.getAction() == MotionEvent.ACTION_CANCEL) {// 松开手指
            if (head.getBottom() > 0) {// 被拉下来
                if (mState != STATE_REFRESH) {// 没在刷新
                    recover(head.getBottom());
                    return false;
                }
            }
            mLastY = 0;
        }
        boolean consumed = mGestureDetector.onTouchEvent(ev);
        if ((consumed || (mState != STATE_INIT && mState != STATE_REFRESH)) && head.getBottom() > 0) {
            ev.setAction(MotionEvent.ACTION_CANCEL);// 让父类接受不到真实的事件
            return super.dispatchTouchEvent(ev);
        }
        if (consumed) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }


    private void recover(int dis) {
        if (mXHRefreshListener != null && dis > mXHOverView.mPullRefreshHeight) {
            // 下拉距离够了，恢复刷新最小高度
            mScroller.recover(dis - mXHOverView.mPullRefreshHeight);
            mState = STATE_OVER_RELEASE;
        } else {
            // 下拉距离不够，恢复到初始位置
            mScroller.recover(dis);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // 定义head和child的排列位置
        View head = getChildAt(0);
        View child = getChildAt(1);
        if (head != null && child != null) {
            int childTop = child.getTop();
            if (mState == STATE_REFRESH) {
                head.layout(0, mXHOverView.mPullRefreshHeight - head.getMeasuredHeight(), right, mXHOverView.mPullRefreshHeight);
                child.layout(0, mXHOverView.mPullRefreshHeight, right, mXHOverView.mPullRefreshHeight + child.getMeasuredHeight());
            } else {
                head.layout(0, childTop - head.getMeasuredHeight(), right, childTop);
                child.layout(0, childTop, right, childTop + child.getMeasuredHeight());
            }
            View other;
            for (int i = 2; i < getChildCount(); i++) {
                other = getChildAt(i);
                other.layout(0, top, right, bottom);
            }
        }
    }

    /**
     * 借助Scroll实现视图的自动滚动
     */
    private class AutoScroller implements Runnable {
        private final Scroller mScroller;
        private int mLastY;
        private boolean mIsFinished;

        public AutoScroller() {
            mScroller = new Scroller(getContext(), new LinearInterpolator());
            mIsFinished = true;
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {// 还未滚动完成
                moveDown(mLastY - mScroller.getCurrY(), true);
                mLastY = mScroller.getCurrY();
                post(this);
            } else {
                removeCallbacks(this);
                mIsFinished = true;
            }
        }

        void recover(int dis) {
            if (dis <= 0) {
                return;
            }
            removeCallbacks(this);
            mLastY = 0;
            mIsFinished = false;
            mScroller.startScroll(0, 0, 0, dis, 300);
            post(this);
        }

        boolean isFinished() {
            return mIsFinished;
        }
    }
}
