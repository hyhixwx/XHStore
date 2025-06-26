package com.hyhua.xhlibrary.log;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hyhua.xhlibrary.util.XHDisplayUtil;

public class XHViewPrinterProvider {
    private static final String TAG_FLOATING_VIEW = "TAG_FLOATING_VIEW";
    private static final String TAG_LOG_VIEW = "TAG_LOG_VIEW";
    private final FrameLayout rootView;
    private View floatingView;
    private boolean isOpen;
    private FrameLayout logView;
    private final RecyclerView recyclerView;
    private final XHViewPrinter.LogAdapter adapter;

    public XHViewPrinterProvider(FrameLayout rootView, RecyclerView recyclerView, XHViewPrinter.LogAdapter adapter) {
        this.rootView = rootView;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
    }

    public void showFloatingView() {
        // 已有对应View不需要重复添加
        if (rootView.findViewWithTag(TAG_FLOATING_VIEW) != null) {
            return;
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.END;
        View floatingView = genFloatingView();
        floatingView.setTag(TAG_FLOATING_VIEW);
        floatingView.setBackgroundColor(Color.BLACK);
        floatingView.setAlpha(.5f);
        params.bottomMargin = XHDisplayUtil.dp2px(180, recyclerView.getResources());
        rootView.addView(genFloatingView(), params);
    }

    private View genFloatingView() {
        if (floatingView != null) {
            return floatingView;
        }
        TextView textView = new TextView(rootView.getContext());
        textView.setOnClickListener(v -> {
            if (!isOpen) {
                showLogView();
            }
        });
        textView.setTextColor(Color.WHITE);
        textView.setText("XHLog");
        return floatingView = textView;
    }

    public void closeFloatingView() {
        rootView.removeView(genFloatingView());
    }

    private void showLogView() {
        // 已有对应View不需要重复添加
        if (rootView.findViewWithTag(TAG_LOG_VIEW) != null) {
            return;
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, XHDisplayUtil.dp2px(200, rootView.getResources()));
        params.gravity = Gravity.BOTTOM;
        View logView = genLogView();
        logView.setTag(TAG_LOG_VIEW);
        rootView.addView(logView, params);
        isOpen = true;
    }

    private View genLogView() {
        if (logView != null) {
            return logView;
        }
        FrameLayout logView = new FrameLayout(rootView.getContext());
        logView.setBackgroundColor(Color.BLACK);
        logView.addView(recyclerView);

        FrameLayout.LayoutParams closeParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        closeParams.gravity = Gravity.END;
        TextView closeView = new TextView(rootView.getContext());
        closeView.setOnClickListener(v -> closeLogView());
        closeView.setTextColor(Color.WHITE);
        closeView.setText("CLOSE");
        logView.addView(closeView, closeParams);

        FrameLayout.LayoutParams clearParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        clearParams.gravity = Gravity.END;
        clearParams.topMargin = XHDisplayUtil.dp2px(40, rootView.getResources());
        TextView clearView = new TextView(rootView.getContext());
        clearView.setOnClickListener(v -> {
            adapter.clear();
        });
        clearView.setTextColor(Color.WHITE);
        clearView.setText("CLEAR");
        logView.addView(clearView, clearParams);
        return this.logView = logView;
    }

    public void closeLogView() {
        isOpen = false;
        rootView.removeView(genLogView());
    }
}
