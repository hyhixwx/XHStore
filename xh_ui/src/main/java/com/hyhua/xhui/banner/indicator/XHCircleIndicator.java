package com.hyhua.xhui.banner.indicator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyhua.xhlibrary.util.XHDisplayUtil;
import com.hyhua.xhui.R;

public class XHCircleIndicator extends FrameLayout implements XHIndicator<FrameLayout> {
    private static final int VWC = ViewGroup.LayoutParams.WRAP_CONTENT;
    /**
     * 正常状态下的指示点
     */
    @DrawableRes
    private int mPointNormal = R.drawable.shape_point_normal;
    /**
     * 选中状态下的指示点
     */
    @DrawableRes
    private int mPointSelected = R.drawable.shape_point_select;
    /**
     * 指示点左右内间距
     */
    private int mPointLeftRightPadding;
    /**
     * 指示点上下内间距
     */
    private int mPointTopBottomPadding;

    public XHCircleIndicator(@NonNull Context context) {
        this(context, null);
    }

    public XHCircleIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XHCircleIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPointLeftRightPadding = XHDisplayUtil.dp2px(5, getResources());
        mPointTopBottomPadding = XHDisplayUtil.dp2px(15, getResources());
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
        ImageView imageView;
        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(VWC, VWC);
        imageViewParams.gravity = Gravity.CENTER_VERTICAL;
        imageViewParams.setMargins(mPointLeftRightPadding, mPointTopBottomPadding, mPointLeftRightPadding, mPointTopBottomPadding);
        for (int i = 0; i < count; i++) {
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(imageViewParams);
            if (i == 0) {
                imageView.setImageResource(mPointSelected);
            } else {
                imageView.setImageResource(mPointNormal);
            }
            groupView.addView(imageView);
        }
        LayoutParams groupViewParams = new LayoutParams(VWC, VWC);
        groupViewParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
        addView(groupView, groupViewParams);
    }

    @Override
    public void onPointChange(int current, int count) {
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            ImageView imageView = (ImageView) viewGroup.getChildAt(i);
            if (i == current) {
                imageView.setImageResource(mPointSelected);
            } else {
                imageView.setImageResource(mPointNormal);
            }
            imageView.requestLayout();
        }
    }
}
