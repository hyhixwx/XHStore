package com.hyhua.xhui.banner.indicator;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyhua.xhlibrary.util.XHDisplayUtil;

public class XHNumIndicator extends FrameLayout implements XHIndicator<FrameLayout> {
    private static final int VWC = ViewGroup.LayoutParams.WRAP_CONTENT;
    /**
     * 指示点左右内间距
     */
    private int mPointLeftRightPadding;
    /**
     * 指示点上下内间距
     */
    private int mPointTopBottomPadding;

    public XHNumIndicator(@NonNull Context context) {
        this(context, null);
    }

    public XHNumIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XHNumIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPointLeftRightPadding = XHDisplayUtil.dp2px(10, getResources());
        mPointTopBottomPadding = XHDisplayUtil.dp2px(10, getResources());
    }

    @Override
    public FrameLayout get() {
        return this;
    }

    @Override
    public void onInflate(int count) {
        removeAllViews();
        if (count <= 0) {
            return;
        }
        LinearLayout groupView = new LinearLayout(getContext());
        groupView.setOrientation(LinearLayout.HORIZONTAL);
        groupView.setPadding(0, 0, mPointLeftRightPadding, mPointTopBottomPadding);

        TextView tvIndex = new TextView(getContext());
        tvIndex.setText("1");
        tvIndex.setTextColor(Color.WHITE);
        groupView.addView(tvIndex);

        TextView tvSymbol = new TextView(getContext());
        tvSymbol.setText(" / ");
        tvSymbol.setTextColor(Color.WHITE);
        groupView.addView(tvSymbol);

        TextView tvCount = new TextView(getContext());
        tvCount.setText(String.valueOf(count));
        tvCount.setTextColor(Color.WHITE);
        groupView.addView(tvCount);

        LayoutParams groupViewParams = new LayoutParams(VWC, VWC);
        groupViewParams.gravity = Gravity.END | Gravity.BOTTOM;
        addView(groupView, groupViewParams);
    }

    @Override
    public void onPointChange(int current, int count) {
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        TextView tvIndex = (TextView) viewGroup.getChildAt(0);
        TextView tvCount = (TextView) viewGroup.getChildAt(2);
        tvIndex.setText(String.valueOf(current + 1));
        tvCount.setText(String.valueOf(count));
    }
}
