package com.hyhua.xhcommon.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 将Fragment操作内聚、提供一些通用的API
 */
public class XHFragmentTabView extends FrameLayout {

    private XHTabViewAdapter mAdapter;
    private int currPosition;

    public XHFragmentTabView(@NonNull Context context) {
        this(context, null);
    }

    public XHFragmentTabView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XHFragmentTabView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public XHTabViewAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(XHTabViewAdapter adapter) {
        if (mAdapter != null || adapter == null) {
            return;
        }
        mAdapter = adapter;
        currPosition = -1;
    }

    /**
     * 设置选中的fragment
     */
    public void setCurrItem(int position) {
        if (position < 0 || position >= mAdapter.getCount()) {
            return;
        }
        if (currPosition != position) {
            currPosition = position;
            mAdapter.instantiateItem(this, position);
        }
    }

    /**
     * 获取当前选中的fragment索引
     */
    public int getCurrItem() {
        return currPosition;
    }

    /**
     * 获取当前选中的fragment
     */
    public Fragment getCurrFragment() {
        if (this.mAdapter == null) {
            throw new IllegalArgumentException("Please call setAdapter first.");
        }
        return mAdapter.getCurrFragment();
    }
}
