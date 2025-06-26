package com.hyhua.xhui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyhua.xhui.R;

public class XHTextOverView extends XHOverView {
    private TextView tvTips;
    private ImageView ivRotate;

    public XHTextOverView(@NonNull Context context) {
        this(context, null);
    }

    public XHTextOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XHTextOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.xh_refresh_overview, this, true);
        tvTips = findViewById(R.id.tv_tips);
        ivRotate = findViewById(R.id.iv_rotate);
    }

    @Override
    protected void onScroll(int scrollY, int pullRefreshHeight) {

    }

    @Override
    protected void onVisible() {
        tvTips.setText("下拉刷新");
    }

    @Override
    protected void onOver() {
        tvTips.setText("松开刷新");
    }

    @Override
    protected void onRefresh() {
        tvTips.setText("正在刷新...");
        Animation operationAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        LinearInterpolator interpolator = new LinearInterpolator();
        operationAnim.setInterpolator(interpolator);
        ivRotate.startAnimation(operationAnim);
    }

    @Override
    protected void onFinish() {
        ivRotate.clearAnimation();
    }
}
