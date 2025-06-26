package com.hyhua.xhui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.hyhua.xhlibrary.util.XHDisplayUtil;
import com.hyhua.xhlibrary.util.XHViewUtil;
import com.hyhua.xhui.R;
import com.hyhua.xhui.tab.common.IXHTabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XHTabBottomLayout extends FrameLayout implements IXHTabLayout<XHTabBottom, XHTabBottomInfo<?>> {

    private static final String TAG_TAB_BOTTOM = "TAG_TAB_BOTTOM";
    private final List<OnTabSelectedListener<XHTabBottomInfo<?>>> tabSelectedChangeListeners = new ArrayList<>();
    private XHTabBottomInfo<?> selectedInfo;
    private float bottomAlpha = 1f;
    private float tabBottomHeight = 50;
    private float topSplitLineHeight = 0.5f;
    private String topSplitLineColor = "#dfe0e1";
    private List<XHTabBottomInfo<?>> infoList;

    public XHTabBottomLayout(@NonNull Context context) {
        this(context, null);
    }

    public XHTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XHTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public XHTabBottom findTab(@NonNull XHTabBottomInfo<?> info) {
        ViewGroup fl = findViewWithTag(TAG_TAB_BOTTOM);
        for (int i = 0; i < fl.getChildCount(); i++) {
            View child = fl.getChildAt(i);
            if (child instanceof XHTabBottom) {
                XHTabBottom tab = (XHTabBottom) child;
                if (tab.getTabInfo() == info) {
                    return tab;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<XHTabBottomInfo<?>> listener) {
        tabSelectedChangeListeners.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull XHTabBottomInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void inflateInfo(@NonNull List<XHTabBottomInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }
        this.infoList = infoList;
        // 清空已经添加的View
        // i = 0 是非tab区域的FrameLayout，不能被移除
        for (int i = getChildCount() - 1; i > 0; i--) {
            removeViewAt(i);
        }
        selectedInfo = null;
        // 获取单tab宽高
        int height = XHDisplayUtil.dp2px(tabBottomHeight, getResources());
        int width = XHDisplayUtil.getDisplayWidthInPx(getContext()) / infoList.size();
        // 添加tab背景
        addBackground(height);
        // 清空已经添加的listener, 如果用for循环从0开始删除，同时对变量更改，会crash，应从最后一个开始删除，或使用迭代器
        Iterator<OnTabSelectedListener<XHTabBottomInfo<?>>> iterator = tabSelectedChangeListeners.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof XHTabBottom) {
                iterator.remove();
            }
        }
        // 创建tab布局
        // 如果使用LinearLayout会导致动态更改tab高度时gravity失效
        FrameLayout fl = new FrameLayout(getContext());
        fl.setTag(TAG_TAB_BOTTOM);
        for (int i = 0; i < infoList.size(); i++) {
            XHTabBottomInfo<?> info = infoList.get(i);
            XHTabBottom tabBottom = new XHTabBottom(getContext());
            tabSelectedChangeListeners.add(tabBottom);
            tabBottom.setXHTabInfo(info);
            LayoutParams params = new LayoutParams(width, height);
            params.gravity = Gravity.BOTTOM;
            params.leftMargin = i * width;
            fl.addView(tabBottom, params);
            tabBottom.setOnClickListener(v -> onSelected(info));
        }
        LayoutParams flParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        flParams.gravity = Gravity.BOTTOM;
        addTopSplitLine();
        addView(fl, flParams);
        fixContentView();
    }

    private void addTopSplitLine() {
        View splitLine = new View(getContext());
        splitLine.setBackgroundColor(Color.parseColor(topSplitLineColor));
        LayoutParams splitLineParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, XHDisplayUtil.dp2px(topSplitLineHeight, getResources()));
        splitLineParams.gravity = Gravity.BOTTOM;
        splitLineParams.bottomMargin = XHDisplayUtil.dp2px(tabBottomHeight - topSplitLineHeight, getResources());
        addView(splitLine, splitLineParams);
        // 透明度最后设置，能避免覆盖，减少重绘次数，提高性能
        splitLine.setAlpha(bottomAlpha);
    }

    private void onSelected(@NonNull XHTabBottomInfo<?> nextInfo) {
        for (OnTabSelectedListener<XHTabBottomInfo<?>> listener : tabSelectedChangeListeners) {
            listener.onTabSelectedChange(infoList.indexOf(nextInfo), selectedInfo, nextInfo);
        }
        this.selectedInfo = nextInfo;
    }

    private void addBackground(int height) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.xh_tab_bottom_layout_bg, null);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        params.gravity = Gravity.BOTTOM;
        view.setAlpha(bottomAlpha);
        addView(view, params);
    }

    private void fixContentView() {
        if (!(getChildAt(0) instanceof ViewGroup)) {
            return;
        }
        ViewGroup rootView = (ViewGroup) getChildAt(0);
        ViewGroup targetView = XHViewUtil.findTypeView(rootView, RecyclerView.class);
        if (targetView == null) {
            targetView = XHViewUtil.findTypeView(rootView, ScrollView.class);
        }
        if (targetView == null) {
            targetView = XHViewUtil.findTypeView(rootView, NestedScrollView.class);
        }
        if (targetView == null) {
            targetView = XHViewUtil.findTypeView(rootView, AbsListView.class);
        }
        if (targetView != null) {
            targetView.setPadding(0, 0, 0, XHDisplayUtil.dp2px(tabBottomHeight, getResources()));
            targetView.setClipToPadding(false);// 使子布局可以绘制到padding内
        }
    }

    public void setTabAlpha(float tabAlpha) {
        bottomAlpha = tabAlpha;
    }

    public void setTabHeight(float tabHeight) {
        tabBottomHeight = tabHeight;
    }

    public void setSplitLineHeight(float setSplitLineHeight) {
        topSplitLineHeight = setSplitLineHeight;
    }

    public void setSplitLineColor(String setSplitLineColor) {
        topSplitLineColor = setSplitLineColor;
    }
}
