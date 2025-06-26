package com.hyhua.xhstore.test

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hyhua.xhstore.R
import com.hyhua.xhlibrary.util.XHDisplayUtil
import com.hyhua.xhui.tab.bottom.XHTabBottomInfo
import com.hyhua.xhui.tab.bottom.XHTabBottomLayout

class XHTabBottomDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xhtab_bottom_demo)

        initTabBottom()
    }

    private fun initTabBottom() {
        val xhTabBottomLayout: XHTabBottomLayout = findViewById(R.id.xhTabLayout)
        xhTabBottomLayout.setTabAlpha(0.85f);
        val bottomInfoList: MutableList<XHTabBottomInfo<*>> = ArrayList()
        val infoHome = XHTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.icon_home),
            null,
            "#ff656667",
            "#ffd44949"
        )
        val infoRecommend = XHTabBottomInfo(
            "收藏",
            "fonts/iconfont.ttf",
            getString(R.string.icon_favorite),
            null,
            "#ff656667",
            "#ffd44949"
        )
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.fire, null)
        val infoCategory = XHTabBottomInfo<String>(
            "分类",
            bitmap,
            bitmap
        )
//        val infoCategory = XHTabBottomInfo(
//            "分类",
//            "fonts/iconfont.ttf",
//            getString(R.string.icon_category),
//            null,
//            "#ff656667",
//            "#ffd44949"
//        )
        val infoChat = XHTabBottomInfo(
            "推荐",
            "fonts/iconfont.ttf",
            getString(R.string.icon_chat),
            null,
            "#ff656667",
            "#ffd44949"
        )
        val infoProfile = XHTabBottomInfo(
            "我的",
            "fonts/iconfont.ttf",
            getString(R.string.icon_profile),
            null,
            "#ff656667",
            "#ffd44949"
        )
        bottomInfoList.add(infoHome)
        bottomInfoList.add(infoRecommend)
        bottomInfoList.add(infoCategory)
        bottomInfoList.add(infoChat)
        bottomInfoList.add(infoProfile)
        xhTabBottomLayout.inflateInfo(bottomInfoList)
        xhTabBottomLayout.addTabSelectedChangeListener { _, _, nextInfo ->
            Toast.makeText(this, nextInfo.name, Toast.LENGTH_SHORT).show()
        }
        xhTabBottomLayout.defaultSelected(infoHome)
        val tabBottom = xhTabBottomLayout.findTab(bottomInfoList[2])
        tabBottom.resetHeight(XHDisplayUtil.dp2px(66f, resources))
    }
}