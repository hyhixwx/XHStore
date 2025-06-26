package com.hyhua.xhui.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.hyhua.xhui.R;
import com.hyhua.xhui.banner.core.IBindAdapter;
import com.hyhua.xhui.banner.core.IXHBanner;
import com.hyhua.xhui.banner.core.XHBannerDelegate;
import com.hyhua.xhui.banner.core.XHBannerModel;
import com.hyhua.xhui.banner.indicator.XHIndicator;

import java.util.List;

public class XHBanner extends FrameLayout implements IXHBanner {
    private final XHBannerDelegate delegate;

    public XHBanner(@NonNull Context context) {
        this(context, null);
    }

    public XHBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XHBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        delegate = new XHBannerDelegate(context, this);
        initCustomAttrs(context, attrs);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.XHBanner);
        boolean autoPlay = typedArray.getBoolean(R.styleable.XHBanner_autoPlay, true);
        boolean loop = typedArray.getBoolean(R.styleable.XHBanner_loop, true);
        int intervalTime = typedArray.getInteger(R.styleable.XHBanner_intervalTime, -1);
        setAutoPlay(autoPlay);
        setLoop(loop);
        setIntervalTime(intervalTime);
        typedArray.recycle();
    }

    @Override
    public void setBannerData(int layoutResId, @NonNull List<? extends XHBannerModel> models) {
        delegate.setBannerData(layoutResId, models);
    }

    @Override
    public void setBannerData(@NonNull List<? extends XHBannerModel> models) {
        delegate.setBannerData(models);
    }

    @Override
    public void setXHIndicator(XHIndicator<?> xhIndicator) {
        delegate.setXHIndicator(xhIndicator);
    }

    @Override
    public void setAutoPlay(boolean autoPlay) {
        delegate.setAutoPlay(autoPlay);
    }

    @Override
    public void setLoop(boolean loop) {
        delegate.setLoop(loop);
    }

    @Override
    public void setIntervalTime(int intervalTime) {
        delegate.setIntervalTime(intervalTime);
    }

    @Override
    public void setBindAdapter(IBindAdapter bindAdapter) {
        delegate.setBindAdapter(bindAdapter);
    }

    @Override
    public void setScrollDuration(int duration) {
        delegate.setScrollDuration(duration);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        delegate.setOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        delegate.setOnBannerClickListener(onBannerClickListener);
    }
}
