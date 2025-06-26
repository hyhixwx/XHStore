package com.hyhua.xhui.banner.core;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.hyhua.xhui.R;
import com.hyhua.xhui.banner.XHBanner;
import com.hyhua.xhui.banner.indicator.XHCircleIndicator;
import com.hyhua.xhui.banner.indicator.XHIndicator;

import java.util.List;

import javax.crypto.Mac;

/**
 * XHBanner的控制器
 * 辅助XHBanner完成各种功能的控制
 * 将XHBanner的一些逻辑内聚在这，保证XHBanner的干净整洁
 */
public class XHBannerDelegate implements IXHBanner, ViewPager.OnPageChangeListener {
    private Context mContext;
    private XHBanner mBanner;
    private XHBannerAdapter mAdapter;
    private XHIndicator<?> mIndicator;
    private boolean mAutoplay;
    private boolean mLoop;
    private List<? extends XHBannerModel> mBannerModels;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private int mIntervalTime = 5000;
    private IXHBanner.OnBannerClickListener mOnBannerClickListener;
    private XHViewPager mXHViewPager;
    private int mScrollDuration = -1;

    public XHBannerDelegate(Context context, XHBanner xhBanner) {
        mContext = context;
        mBanner = xhBanner;
    }

    @Override
    public void setBannerData(int layoutResId, @NonNull List<? extends XHBannerModel> models) {
        mBannerModels = models;
        init(layoutResId);
    }

    @Override
    public void setBannerData(@NonNull List<? extends XHBannerModel> models) {
        setBannerData(R.layout.xh_banner_item_image, models);
    }

    @Override
    public void setXHIndicator(XHIndicator<?> xhIndicator) {
        mIndicator = xhIndicator;
    }

    @Override
    public void setAutoPlay(boolean autoPlay) {
        mAutoplay = autoPlay;
        if (mAdapter != null) mAdapter.setAutoPlay(autoPlay);
        if (mXHViewPager != null) mXHViewPager.setAutoPlay(autoPlay);
    }

    @Override
    public void setLoop(boolean loop) {
        mLoop = loop;
    }

    @Override
    public void setIntervalTime(int intervalTime) {
        if (intervalTime > 0) mIntervalTime = intervalTime;
    }

    @Override
    public void setBindAdapter(IBindAdapter bindAdapter) {
        mAdapter.setBindAdapter(bindAdapter);
    }

    @Override
    public void setScrollDuration(int duration) {
        mScrollDuration = duration;
        if (mXHViewPager != null && duration > 0) mXHViewPager.setScrollerDuration(duration);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        mOnBannerClickListener = onBannerClickListener;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null && mAdapter.getRealCount() != 0) {
            mOnPageChangeListener.onPageScrolled(position % mAdapter.getRealCount(), positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mAdapter.getRealCount() == 0) {
            return;
        }
        position %= mAdapter.getRealCount();
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(position);
        }
        if (mIndicator != null) {
            mIndicator.onPointChange(position, mAdapter.getRealCount());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    private void init(int layoutResId) {
        if (mAdapter == null) {
            mAdapter = new XHBannerAdapter(mContext);
        }
        if (mIndicator == null) {
            mIndicator = new XHCircleIndicator(mContext);
        }
        mIndicator.onInflate(mBannerModels.size());
        mAdapter.setLayoutRedId(layoutResId);
        mAdapter.setBannerData(mBannerModels);
        mAdapter.setAutoPlay(mAutoplay);
        mAdapter.setLoop(mLoop);
        mAdapter.setOnBannerClickListener(mOnBannerClickListener);

        mXHViewPager = new XHViewPager(mContext);
        mXHViewPager.setIntervalTime(mIntervalTime);
        mXHViewPager.addOnPageChangeListener(this);
        mXHViewPager.setAutoPlay(mAutoplay);
        mXHViewPager.setAdapter(mAdapter);
        // 设置了滚动间隔时ViewPager还没实例化，则此处重新设置滚动间隔
        if (mScrollDuration > 0) mXHViewPager.setScrollerDuration(mScrollDuration);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        if ((mLoop || mAutoplay) && mAdapter.getRealCount() != 0) {
            // 无限轮播：第一张能反向滑动到最后一张
            int firstItem = mAdapter.getFirstShowItemPosition();
            mXHViewPager.setCurrentItem(firstItem, false);
        }
        // 清除缓存view
        mBanner.removeAllViews();
        mBanner.addView(mXHViewPager, params);
        mBanner.addView(mIndicator.get(), params);
    }
}
