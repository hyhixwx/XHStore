package com.hyhua.xhui.tab.common;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

public interface IXHTabLayout<Tab extends ViewGroup, D> {
    Tab findTab(@NonNull D data);

    void addTabSelectedChangeListener(OnTabSelectedListener<D> listener);

    void defaultSelected(@NonNull D defaultInfo);

    void inflateInfo(@NonNull List<D> infoList);

    interface OnTabSelectedListener<D> {
        void onTabSelectedChange(int index, D preInfo, @NonNull D nextInfo);
    }
}
