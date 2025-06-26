package com.hyhua.xhstore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.hyhua.xhcommon.ui.component.XHBaseActivity
import com.hyhua.xhstore.test.XHBannerDemoActivity
import com.hyhua.xhstore.test.XHDataItemDemoActivity
import com.hyhua.xhstore.test.XHLogDemoActivity
import com.hyhua.xhstore.test.XHRefreshDemoActivity
import com.hyhua.xhstore.test.XHTabBottomDemoActivity
import com.hyhua.xhstore.test.XHTabTopDemoActivity

class GuideActivity : XHBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
        initTestEntrance()
    }

    private fun initTestEntrance() {
        val btn1 = findViewById<Button>(R.id.btn_1)
        btn1.text = "Portal"
        btn1.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
        }

        val btn2 = findViewById<Button>(R.id.btn_2)
        btn2.text = "XHLog"
        btn2.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    XHLogDemoActivity::class.java
                )
            )
        }

        val btn3 = findViewById<Button>(R.id.btn_3)
        btn3.text = "XHTabBottom"
        btn3.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    XHTabBottomDemoActivity::class.java
                )
            )
        }
        val btn4 = findViewById<Button>(R.id.btn_4)
        btn4.text = "XHTabTop"
        btn4.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    XHTabTopDemoActivity::class.java
                )
            )
        }
        val btn5 = findViewById<Button>(R.id.btn_5)
        btn5.text = "XHRefresh"
        btn5.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    XHRefreshDemoActivity::class.java
                )
            )
        }
        val btn6 = findViewById<Button>(R.id.btn_6)
        btn6.text = "XHBanner"
        btn6.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    XHBannerDemoActivity::class.java
                )
            )
        }

        val btn7 = findViewById<Button>(R.id.btn_7)
        btn7.text = "XHDataItem"
        btn7.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    XHDataItemDemoActivity::class.java
                )
            )
        }
    }
}