package com.hyhua.xhstore.test

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.hyhua.xhstore.R
import com.hyhua.xhui.tab.top.XHTabTopInfo
import com.hyhua.xhui.tab.top.XHTabTopLayout

class XHTabTopDemoActivity : AppCompatActivity() {
    private val tabsStr = listOf("热门", "服装", "数码", "鞋子", "零食", "家电", "汽车", "百货", "家居", "装修", "运动")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xhtab_top_demo)

        initTabTop()
    }

    private fun initTabTop() {
        val xhTabTopLayout: XHTabTopLayout = findViewById(R.id.tab_top_layout)
        val infoList: MutableList<XHTabTopInfo<*>> = mutableListOf()
        val defaultColor: Int = ContextCompat.getColor(this, R.color.tabBottomDefaultColor)
        val tintColor: Int = ContextCompat.getColor(this, R.color.tabBottomTintColor)
        tabsStr.forEach {
            val info: XHTabTopInfo<*> = XHTabTopInfo(it, defaultColor, tintColor)
            infoList.add(info)
        }
        xhTabTopLayout.inflateInfo(infoList)
        xhTabTopLayout.addTabSelectedChangeListener { index, preInfo, nextInfo ->
            Toast.makeText(this@XHTabTopDemoActivity, nextInfo.name, Toast.LENGTH_SHORT).show()
        }
        xhTabTopLayout.defaultSelected(infoList[0])
    }
}