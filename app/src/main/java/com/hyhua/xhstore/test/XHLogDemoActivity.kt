package com.hyhua.xhstore.test

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.hyhua.xhstore.R
import com.hyhua.xhlibrary.log.XHFilePrinter
import com.hyhua.xhlibrary.log.XHLog
import com.hyhua.xhlibrary.log.XHLogConfig
import com.hyhua.xhlibrary.log.XHLogManager
import com.hyhua.xhlibrary.log.XHLogType
import com.hyhua.xhlibrary.log.XHViewPrinter

class XHLogDemoActivity : AppCompatActivity() {

    private var viewPrinter: XHViewPrinter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xhlog_demo)
        viewPrinter = XHViewPrinter(this)
        viewPrinter?.viewProvider?.showFloatingView()
        XHLogManager.getInstance().addPrinter(viewPrinter)
        val filePrinter = XHFilePrinter.getInstance(filesDir.toString(), 0)
        XHLogManager.getInstance().addPrinter(filePrinter)
        val btn_print = findViewById<Button>(R.id.btn_print)
        btn_print.setOnClickListener { view: View? -> printLog() }
    }

    private fun printLog() {
        XHLog.d("1001")
        XHLog.log(object : XHLogConfig() {
            override fun includeThread(): Boolean {
                return true
            }

            override fun stackTraceDepth(): Int {
                return 0
            }
        }, XHLogType.E, "XHAppLog", "1002")
    }
}