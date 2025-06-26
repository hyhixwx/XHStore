package com.hyhua.xhui.banner.core;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class XHBannerAdapter extends PagerAdapter {
    private Context mContext;
//    private SparseArray<XHBannerViewHolder> mCachedViews = new SparseArray<>();
    private IXHBanner.OnBannerClickListener mBannerClickListener;
    private IBindAdapter mBindAdapter;
    private List<? extends XHBannerModel> models;
    /**
     * 是否自动轮播
     */
    private boolean mAutoPlay = true;
    /**
     * 非自动轮播状态下是否循环切换
     */
    private boolean mLoop = false;
    @LayoutRes
    private int mLayoutRedId = -1;

    public XHBannerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setBannerData(@NonNull List<? extends XHBannerModel> models) {
        this.models = models;
        // 初始化数据
        initCachedView();
        notifyDataSetChanged();
    }

    public void setOnBannerClickListener(IXHBanner.OnBannerClickListener onBannerClickListener) {
        mBannerClickListener = onBannerClickListener;
    }

    public void setBindAdapter(IBindAdapter bindAdapter) {
        mBindAdapter = bindAdapter;
    }

    public void setAutoPlay(boolean autoPlay) {
        mAutoPlay = autoPlay;
    }

    public void setLoop(boolean loop) {
        mLoop = loop;
    }

    public void setLayoutRedId(int layoutRedId) {
        mLayoutRedId = layoutRedId;
    }

    @Override
    public int getCount() {
        // 无限轮播关键点
        return mAutoPlay ? Integer.MAX_VALUE : (mLoop ? Integer.MAX_VALUE : getRealCount());
    }

    /**
     * 获取Banner页面数量
     *
     * @return Banner页面数量
     */
    public int getRealCount() {
        return models == null ? 0 : models.size();
    }

    /**
     * 获取初次展示的item位置
     *
     * @return 初次展示的item位置
     */
    public int getFirstShowItemPosition() {
        return Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % getRealCount();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position;
        if (getRealCount() > 0) {
            realPosition = position % getRealCount();
        }
        XHBannerViewHolder viewHolder = new XHBannerViewHolder(createView(LayoutInflater.from(mContext), null));
        if (container.equals(viewHolder.rootView.getParent())) {
            container.removeView(viewHolder.rootView);
        }
        // 数据绑定
        onBind(viewHolder, models.get(realPosition), realPosition);
        if (viewHolder.rootView.getParent() != null) {
            ((ViewGroup) viewHolder.rootView.getParent()).removeView(viewHolder.rootView);
        }
        container.addView(viewHolder.rootView);
        return viewHolder.rootView;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        // 让item每次都会刷新
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
//        if (object != null)
        object = null;
    }

    protected void onBind(@NonNull final XHBannerViewHolder viewHolder, @NonNull final XHBannerModel bannerModel, final int position) {
        viewHolder.rootView.setOnClickListener(v -> {
            if (mBannerClickListener != null) {
                mBannerClickListener.onBannerClick(viewHolder, bannerModel, position);
            }
        });
        if (mBindAdapter != null) {
            mBindAdapter.onBind(viewHolder, bannerModel, position);
        }
    }

    private void initCachedView() {
//        mCachedViews = new SparseArray<>();
//        for (int i = 0; i < models.size(); i++) {
//            XHBannerViewHolder viewHolder = new XHBannerViewHolder(createView(LayoutInflater.from(mContext), null));
//            mCachedViews.put(i, viewHolder);
//        }
    }

    private View createView(LayoutInflater layoutInflater, ViewGroup parent) {
        if (mLayoutRedId == -1) {
            throw new IllegalArgumentException("You must be set setLayoutResId first!");
        }
        return layoutInflater.inflate(mLayoutRedId, parent, false);
    }

    public static class XHBannerViewHolder {
        private SparseArray<View> viewSparseArray;
        View rootView;

        public XHBannerViewHolder(View rootView) {
            this.rootView = rootView;
        }

        public View getRootView() {
            return rootView;
        }

        public <V extends View> V findViewById(int id) {
            if (!(rootView instanceof ViewGroup)) {
                return (V) rootView;
            }
            if (this.viewSparseArray == null) {
                this.viewSparseArray = new SparseArray<>(1);
            }
            V childView = (V) viewSparseArray.get(id);
            if (childView == null) {
                childView = rootView.findViewById(id);
                this.viewSparseArray.put(id, childView);
            }
            return childView;
        }
    }
}
