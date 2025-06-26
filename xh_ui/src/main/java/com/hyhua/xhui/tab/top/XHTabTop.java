package com.hyhua.xhui.tab.top;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.hyhua.xhui.R;
import com.hyhua.xhui.tab.common.IXHTab;

/**
 * 单个底部tab
 */
public class XHTabTop extends RelativeLayout implements IXHTab<XHTabTopInfo<?>> {
    private XHTabTopInfo<?> tabInfo;
    private ImageView tabImageView;
    private TextView tabNameView;
    private View indicator;

    public XHTabTop(Context context) {
        this(context, null);
    }

    public XHTabTop(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XHTabTop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.xh_tab_top, this);
        tabImageView = findViewById(R.id.iv_image);
        tabNameView = findViewById(R.id.tv_name);
        indicator = findViewById(R.id.tab_top_indicator);
    }

    @Override
    public void setXHTabInfo(@NonNull XHTabTopInfo<?> data) {
        this.tabInfo = data;
        inflateInfo(false, true);
    }

    /**
     * 填充视图
     *
     * @param selected tab是否被选中
     * @param init     是否是第一次初始化
     */
    private void inflateInfo(boolean selected, boolean init) {
        if (tabInfo.tabType == XHTabTopInfo.TabType.TEXT) {
            if (init) {
                tabImageView.setVisibility(GONE);
                tabNameView.setVisibility(VISIBLE);
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabNameView.setText(tabInfo.name);
                }
            }
            if (selected) {
                indicator.setVisibility(VISIBLE);
                tabNameView.setTextColor(getTextColor(tabInfo.tintColor));
            } else {
                indicator.setVisibility(GONE);
                tabNameView.setTextColor(getTextColor(tabInfo.defaultColor));
            }
        } else if (tabInfo.tabType == XHTabTopInfo.TabType.BITMAP) {
            if (init) {
                tabImageView.setVisibility(VISIBLE);
                tabNameView.setVisibility(GONE);
            }
            if (selected) {
                tabImageView.setImageBitmap(tabInfo.selectedBitmap);
            } else {
                tabImageView.setImageBitmap(tabInfo.defaultBitmap);
            }
        }
    }

    public XHTabTopInfo<?> getTabInfo() {
        return tabInfo;
    }

    public ImageView getTabImageView() {
        return tabImageView;
    }

    public TextView getTabNameView() {
        return tabNameView;
    }

    @Override
    public void resetHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        setLayoutParams(layoutParams);
        tabNameView.setVisibility(GONE);
    }

    @Override
    public void onTabSelectedChange(int index, @NonNull XHTabTopInfo<?> preInfo, @NonNull XHTabTopInfo<?> nextInfo) {
        // 已经选中的和下一个选中的都不是当前tab，或已经选中的tab和下一个选中的tab是同一个，则不需要对当前tab做处理
        if (preInfo != tabInfo && nextInfo != tabInfo || preInfo == nextInfo) {
            return;
        }
        // 已经选中的tab和下一个选中的tab是同一个的情况，已被以上if排除，所以此处下一个tab一定不会是已经选中的tab
        if (preInfo == tabInfo) {// 已经选中的tab是当前tab，那么取消选中
            inflateInfo(false, false);
        } else {// 下一个选中的是当前tab，那么置为选中
            inflateInfo(true, false);
        }
    }

    @ColorInt
    private int getTextColor(Object color) {
        if (color instanceof String) {
            return Color.parseColor((String) color);
        } else {
            return (int) color;
        }
    }
}
