package com.hyhua.xhui.tab.common;

import androidx.annotation.NonNull;
import androidx.annotation.Px;

public interface IXHTab<D> extends IXHTabLayout.OnTabSelectedListener<D> {
    void setXHTabInfo(@NonNull D data);

    void resetHeight(@Px int height);
}
