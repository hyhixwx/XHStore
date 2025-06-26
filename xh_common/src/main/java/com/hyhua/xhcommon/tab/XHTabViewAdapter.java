package com.hyhua.xhcommon.tab;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hyhua.xhui.tab.bottom.XHTabBottomInfo;

import java.util.List;

public class XHTabViewAdapter {
    private List<XHTabBottomInfo<?>> mInfoList;
    private Fragment mCurrFragment;
    private FragmentManager mFragmentManager;

    public XHTabViewAdapter(FragmentManager fragmentManager, List<XHTabBottomInfo<?>> infoList) {
        this.mFragmentManager = fragmentManager;
        this.mInfoList = infoList;
    }

    /**
     * 实例化并显示指定索引的fragment
     */
    public void instantiateItem(View container, int position) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mCurrFragment != null) {
            transaction.hide(mCurrFragment);
        }
        String name = container.getId() + ":" + position;
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            fragment = getItem(position);
            if (!fragment.isAdded()) {
                transaction.add(container.getId(), fragment, name);
            }
        }
        mCurrFragment = fragment;
        transaction.commitAllowingStateLoss();
    }

    public Fragment getCurrFragment() {
        return mCurrFragment;
    }

    public Fragment getItem(int position) {
        try {
            return mInfoList.get(position).fragment.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getCount() {
        return mInfoList == null ? 0 : mInfoList.size();
    }
}
