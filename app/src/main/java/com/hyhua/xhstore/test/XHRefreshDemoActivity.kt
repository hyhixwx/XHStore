package com.hyhua.xhstore.test

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.hyhua.xhstore.R
import com.hyhua.xhui.refresh.XHRefresh.XHRefreshListener
import com.hyhua.xhui.refresh.XHRefreshLayout
import com.hyhua.xhui.refresh.XHTextOverView

class XHRefreshDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xhrefresh_demo)

        val refreshLayout = findViewById<XHRefreshLayout>(R.id.refresh_layout)
        val overView = XHTextOverView(this)
        refreshLayout.setRefreshOverView(overView)
        refreshLayout.setRefreshListener(object : XHRefreshListener {
            override fun onRefresh() {
                Handler().postDelayed({ refreshLayout.refreshFinished() }, 2000)
            }

            override fun enableRefresh(): Boolean = true
        })
    }
}