package com.hyhua.xhui.tab.top;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

public class XHTabTopInfo<Color> {
    public enum TabType {
        BITMAP, TEXT
    }

    public Class<? extends Fragment> fragment;
    public String name;
    public Bitmap defaultBitmap;
    public Bitmap selectedBitmap;
    public Color defaultColor;
    public Color tintColor;
    public TabType tabType;

    public XHTabTopInfo(String name, Bitmap defaultBitmap, Bitmap selectedBitmap) {
        this.name = name;
        this.defaultBitmap = defaultBitmap;
        this.selectedBitmap = selectedBitmap;
        this.tabType = TabType.BITMAP;
    }

    public XHTabTopInfo(String name, Color defaultColor, Color tintColor) {
        this.name = name;
        this.defaultColor = defaultColor;
        this.tintColor = tintColor;
        this.tabType = TabType.TEXT;
    }
}
