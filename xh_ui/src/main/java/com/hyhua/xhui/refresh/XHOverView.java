package com.hyhua.xhui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyhua.xhlibrary.util.XHDisplayUtil;

/**
 * 下拉刷新的Overlay视图，可以重载这个类来定义自己的Overlay
 */
public abstract class XHOverView extends FrameLayout {

    public enum XHRefreshState {
        /**
         * 初始态
         */
        STATE_INIT,
        /**
         * Header展示状态
         */
        STATE_VISIBLE,
        /**
         * 正在刷新
         */
        STATE_REFRESH,
        /**
         * 超出开始刷新高度
         */
        STATE_OVER,
        /**
         * 超出开始刷新高度松开手后恢复最小刷新高度的状态
         */
        STATE_OVER_RELEASE
    }

    protected XHRefreshState mState = XHRefreshState.STATE_INIT;
    /**
     * 触发刷新需要的最小高度
     */
    public int mPullRefreshHeight;
    /**
     * 最小阻尼
     */
    public float minDamping = 1.6f;

    /**
     * 最大阻尼
     */
    public float maxDamping = 2.2f;

    public XHOverView(@NonNull Context context) {
        this(context, null);
    }

    public XHOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XHOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        preInit();
    }

    protected void preInit() {
        mPullRefreshHeight = XHDisplayUtil.dp2px(80, getResources());
        init();
    }

    /**
     * 初始化
     */
    public abstract void init();

    protected abstract void onScroll(int scrollY, int pullRefreshHeight);

    /**
     * 显示Overlay
     */
    protected abstract void onVisible();

    /**
     * 超过Overlay，释放手指就会加载
     */
    protected abstract void onOver();

    /**
     * 开始刷新
     */
    protected abstract void onRefresh();

    /**
     * 刷新完成
     */
    protected abstract void onFinish();

    public void setState(XHRefreshState state) {
        mState = state;
    }

    public XHRefreshState getState() {
        return mState;
    }
}
