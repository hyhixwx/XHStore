package com.hyhua.xhstore

import android.os.Bundle
import android.widget.Toast
import com.hyhua.xh.app.logic.MainActivityLogic
import com.hyhua.xhcommon.ui.component.XHBaseActivity

class MainActivity : XHBaseActivity(), MainActivityLogic.ActivityProvider {

    private lateinit var activityLogic: MainActivityLogic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityLogic = MainActivityLogic(this, savedInstanceState)

        ActivityManager.instance.addFrontBackCallBack(object : ActivityManager.FrontBackCallBack {
            override fun onChanged(front: Boolean) {
                Toast.makeText(
                    applicationContext,
                    "当前是否前台: $front, 栈顶：${ActivityManager.instance.topActivity?.localClassName}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        activityLogic.onSaveInstanceState(outState)
    }
}
