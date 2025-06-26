package com.hyhua.xh.app.logic

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.hyhua.xhstore.R
import com.hyhua.xh.app.fragment.*
import com.hyhua.xhcommon.tab.XHTabViewAdapter
import com.hyhua.xhcommon.tab.XHFragmentTabView
import com.hyhua.xhui.tab.bottom.XHTabBottomInfo
import com.hyhua.xhui.tab.bottom.XHTabBottomLayout

class MainActivityLogic(
    private val mContext: Context,
    savedInstanceState: Bundle?
) {

    private var fragmentTabView: XHFragmentTabView? = null
    private var xhTabBottomLayout: XHTabBottomLayout? = null
    private val infoList: MutableList<XHTabBottomInfo<*>> = mutableListOf()
    private var currentItemIndex = 0
    private var activityProvider: ActivityProvider? = null

    companion object {
        private const val CURRENT_FRAGMENT_ID = "CURRENT_FRAGMENT_ID"
    }

    init {
        activityProvider = mContext as ActivityProvider
        // 解决开启不保留Activity导致的Fragment重叠问题
        currentItemIndex = savedInstanceState?.getInt(CURRENT_FRAGMENT_ID) ?: 0
        initTabBottom()
    }

    private fun initTabBottom() {
        activityProvider?.apply {
            xhTabBottomLayout = findViewById(R.id.tab_bottom_layout)
            xhTabBottomLayout?.apply {
                setTabAlpha(0.85f)
                val defaultColor = ContextCompat.getColor(mContext, R.color.tabBottomDefaultColor)
                val tintColor = ContextCompat.getColor(mContext, R.color.tabBottomTintColor)

                val infoHome = XHTabBottomInfo(
                    "首页",
                    "fonts/iconfont.ttf",
                    getString(R.string.icon_home),
                    null,
                    defaultColor,
                    tintColor
                )
                infoHome.fragment = HomeFragment::class.java
                val infoFavorite = XHTabBottomInfo(
                    "收藏",
                    "fonts/iconfont.ttf",
                    getString(R.string.icon_favorite),
                    null,
                    defaultColor,
                    tintColor
                )
                infoFavorite.fragment = RecommendFragment::class.java
                val infoCategory = XHTabBottomInfo(
                    "分类",
                    "fonts/iconfont.ttf",
                    getString(R.string.icon_category),
                    null,
                    defaultColor,
                    tintColor
                )
                infoCategory.fragment = CategoryFragment::class.java
                val infoRecommend = XHTabBottomInfo(
                    "推荐",
                    "fonts/iconfont.ttf",
                    getString(R.string.icon_recommend),
                    null,
                    defaultColor,
                    tintColor
                )
                infoRecommend.fragment = FavoriteFragment::class.java
                val infoProfile = XHTabBottomInfo(
                    "我的",
                    "fonts/iconfont.ttf",
                    getString(R.string.icon_profile),
                    null,
                    defaultColor,
                    tintColor
                )
                infoProfile.fragment = ProfileFragment::class.java
                infoList.apply {
                    add(infoHome)
                    add(infoFavorite)
                    add(infoCategory)
                    add(infoRecommend)
                    add(infoProfile)
                }
                inflateInfo(infoList)
                initFragmentTabView()
                addTabSelectedChangeListener { index, _, _ ->
                    fragmentTabView?.currItem = index
                    currentItemIndex = index
                }
                defaultSelected(infoList[currentItemIndex])
            }
        }
    }

    private fun initFragmentTabView() {
        val tabViewAdapter =
            XHTabViewAdapter(activityProvider?.getSupportFragmentManager(), infoList)
        fragmentTabView = activityProvider?.findViewById(R.id.fragment_tab_view)
        fragmentTabView?.adapter = tabViewAdapter
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENT_FRAGMENT_ID, currentItemIndex)
    }

    interface ActivityProvider {
        fun <T : View?> findViewById(@IdRes id: Int): T
        fun getResources(): Resources
        fun getSupportFragmentManager(): FragmentManager
        fun getString(@StringRes resId: Int): String
        fun getTheme(): Resources.Theme
    }
}