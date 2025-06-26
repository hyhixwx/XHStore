package com.hyhua.xhui.banner.core;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.hyhua.xhui.banner.indicator.XHIndicator;

import java.util.List;

public interface IXHBanner {
    void setBannerData(@LayoutRes int layoutResId, @NonNull List<? extends XHBannerModel> models);

    void setBannerData(@NonNull List<? extends XHBannerModel> models);

    void setXHIndicator(XHIndicator<?> xhIndicator);

    void setAutoPlay(boolean autoPlay);

    void setLoop(boolean loop);

    void setIntervalTime(int intervalTime);

    void setBindAdapter(IBindAdapter bindAdapter);

    void setScrollDuration(int duration);

    void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener);

    void setOnBannerClickListener(OnBannerClickListener onBannerClickListener);

    interface OnBannerClickListener {
        void onBannerClick(@NonNull XHBannerAdapter.XHBannerViewHolder viewHolder, @NonNull XHBannerModel bannerModel, int position);
    }
}
