package com.hyhua.xhstore

import com.google.gson.Gson
import com.hyhua.xhcommon.ui.component.XHBaseApplication
import com.hyhua.xhlibrary.log.XHConsolePrinter
import com.hyhua.xhlibrary.log.XHLogConfig
import com.hyhua.xhlibrary.log.XHLogManager

class XHStoreApplication : XHBaseApplication() {

    override fun onCreate() {
        super.onCreate()
        initXHLog("XHAppLog", true)
        ActivityManager.instance.init(this)
    }

    private fun initXHLog(defaultTag: String, enable: Boolean) {
        XHLogManager.init(object : XHLogConfig() {
            override fun injectJsonParser(): JSONParser {
                return JSONParser { src: Any? -> Gson().toJson(src) }
            }

            override fun getGlobalTag(): String {
                return defaultTag
            }

            override fun enable(): Boolean {
                return enable
            }

            override fun stackTraceDepth(): Int {
                return 5
            }

            override fun includeThread(): Boolean {
                return true
            }
        }, XHConsolePrinter())
    }

    override fun onTerminate() {
        super.onTerminate()
        if (XHLogManager.getInstance() != null) {
            XHLogManager.getInstance().release()
        }
    }
}