package com.hyhua.xhui.tab.top;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.hyhua.xhlibrary.util.XHDisplayUtil;
import com.hyhua.xhui.tab.common.IXHTabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XHTabTopLayout extends HorizontalScrollView implements IXHTabLayout<XHTabTop, XHTabTopInfo<?>> {

    private final List<OnTabSelectedListener<XHTabTopInfo<?>>> tabSelectedChangeListeners = new ArrayList<>();
    private XHTabTopInfo<?> selectedInfo;
    private List<XHTabTopInfo<?>> infoList;
    private int tabWidth;

    public XHTabTopLayout(Context context) {
        this(context, null);
    }

    public XHTabTopLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XHTabTopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setHorizontalScrollBarEnabled(false);
    }

    @Override
    public XHTabTop findTab(@NonNull XHTabTopInfo<?> info) {
        ViewGroup fl = getRootLayout(false);
        for (int i = 0; i < fl.getChildCount(); i++) {
            View child = fl.getChildAt(i);
            if (child instanceof XHTabTop) {
                XHTabTop tab = (XHTabTop) child;
                if (tab.getTabInfo() == info) {
                    return tab;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<XHTabTopInfo<?>> listener) {
        tabSelectedChangeListeners.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull XHTabTopInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void inflateInfo(@NonNull List<XHTabTopInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }
        this.infoList = infoList;
        LinearLayout ll = getRootLayout(true);
        selectedInfo = null;
        // 清空已经添加的listener, 如果用for循环从0开始删除，同时对变量更改，会crash，应从最后一个开始删除，或使用迭代器
        Iterator<OnTabSelectedListener<XHTabTopInfo<?>>> iterator = tabSelectedChangeListeners.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof XHTabTop) {
                iterator.remove();
            }
        }
        for (int i = 0; i < infoList.size(); i++) {
            XHTabTopInfo<?> info = infoList.get(i);
            XHTabTop tabTop = new XHTabTop(getContext());
            tabSelectedChangeListeners.add(tabTop);
            tabTop.setXHTabInfo(info);
            ll.addView(tabTop);
            tabTop.setOnClickListener(v -> onSelected(info));
        }
    }

    private void onSelected(@NonNull XHTabTopInfo<?> nextInfo) {
        for (OnTabSelectedListener<XHTabTopInfo<?>> listener : tabSelectedChangeListeners) {
            listener.onTabSelectedChange(infoList.indexOf(nextInfo), selectedInfo, nextInfo);
        }
        this.selectedInfo = nextInfo;
        autoScroll(nextInfo);
    }

    /**
     * 自动滚动，实现点击的位置能够自动滚动以展示前后2个item
     */
    private void autoScroll(XHTabTopInfo<?> nextInfo) {
        XHTabTop tabTop = findTab(nextInfo);
        if (tabTop == null) {
            return;
        }
        int index = infoList.indexOf(nextInfo);
        int[] loc = new int[2];
        int scrollWidth;
        // 获取点击的空间在屏幕的位置
        tabTop.getLocationInWindow(loc);
        if (tabWidth == 0) {
            tabWidth = tabTop.getWidth();
        }
        if ((loc[0] + tabWidth / 2) > XHDisplayUtil.getDisplayWidthInPx(getContext()) / 2) {
            // 点击了屏幕右侧
            scrollWidth = rangeScrollWidth(index, 2);
        } else {
            // 点击了屏幕左侧
            scrollWidth = rangeScrollWidth(index, -2);
        }
        smoothScrollTo(getScrollX() + scrollWidth, 0);// 平滑滚动
//        scrollTo(getScrollX() + scrollWidth, 0);
    }

    /**
     * 获取可滚动的范围
     *
     * @param index 从第几个开始
     * @param range 向前向后的范围
     * @return 可滚动的范围
     */
    private int rangeScrollWidth(int index, int range) {
        int scrollWidth = 0;
        for (int i = 0; i <= Math.abs(range); i++) {
            int next;
            if (range < 0) {
                next = range + i + index;
            } else {
                next = range - i + index;
            }
            if (next >= 0 & next < infoList.size()) {
                if (range < 0) {
                    scrollWidth -= calScrollWidth(next, false);
                } else {
                    scrollWidth += calScrollWidth(next, true);
                }
            }
        }
        return scrollWidth;
    }

    /**
     * 指定位置的控件可滚动的距离
     *
     * @param index   指定位置的控件
     * @param toRight 点击的是否是屏幕右侧
     * @return 可滚动的距离
     */
    private int calScrollWidth(int index, boolean toRight) {
        XHTabTop target = findTab(infoList.get(index));
        if (target == null) {
            return 0;
        }
        Rect rect = new Rect();
        target.getLocalVisibleRect(rect);
        if (toRight) {
            if (rect.right > tabWidth) {// 完全没显示
                return tabWidth;
            } else {
                return tabWidth - rect.right;
            }
        } else {
            if (rect.left <= -tabWidth) {// 完全没显示
                return tabWidth;
            } else if (rect.left > 0) {
                return rect.left;
            }
        }
        return 0;
    }

    private LinearLayout getRootLayout(boolean clear) {
        LinearLayout rootView = (LinearLayout) getChildAt(0);
        if (rootView == null) {
            rootView = new LinearLayout(getContext());
            rootView.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            addView(rootView, params);
        } else if (clear) {
            rootView.removeAllViews();
        }
        return rootView;
    }
}
