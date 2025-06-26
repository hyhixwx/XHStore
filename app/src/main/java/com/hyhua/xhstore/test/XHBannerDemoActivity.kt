package com.hyhua.xhstore.test

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.bumptech.glide.Glide
import com.hyhua.xhstore.R
import com.hyhua.xhui.banner.XHBanner
import com.hyhua.xhui.banner.core.XHBannerModel
import com.hyhua.xhui.banner.indicator.XHCircleIndicator
import com.hyhua.xhui.banner.indicator.XHIndicator
import com.hyhua.xhui.banner.indicator.XHNumIndicator

class XHBannerDemoActivity : AppCompatActivity() {
    private val urls = arrayListOf(
        "https://www.geekailab.com/img/beauty_camera/beauty_camera1.jpg",
        "https://www.geekailab.com/img/beauty_camera/beauty_camera2.jpg",
        "https://www.geekailab.com/img/beauty_camera/beauty_camera3.jpg",
        "https://www.geekailab.com/img/beauty_camera/beauty_camera4.jpg",
        "https://www.geekailab.com/img/beauty_camera/beauty_camera5.jpg",
        "https://www.geekailab.com/img/beauty_camera/beauty_camera6.jpg",
        "https://www.geekailab.com/img/beauty_camera/beauty_camera7.jpg",
    )
    private var autoPlay: Boolean = false
    private var xhIndicator: XHIndicator<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xhbanner_demo)

        xhIndicator = XHCircleIndicator(this)
        initView(xhIndicator, false)
        findViewById<SwitchCompat>(R.id.auto_play).setOnCheckedChangeListener { _, isChecked ->
            autoPlay = isChecked
            initView(xhIndicator, autoPlay)
        }
        findViewById<Button>(R.id.btn_switch).setOnClickListener {
            if (xhIndicator is XHCircleIndicator) {
                xhIndicator = XHNumIndicator(this)
                initView(xhIndicator, autoPlay)
            } else {
                xhIndicator = XHCircleIndicator(this)
                initView(XHCircleIndicator(this), autoPlay)
            }
        }
    }

    private fun initView(xhIndicator: XHIndicator<*>?, autoPlay: Boolean) {
        val mXHBanner = findViewById<XHBanner>(R.id.banner)
        val moList: MutableList<XHBannerModel> = mutableListOf()
        for (i in 0 until urls.size) {
            val mo = BannerMo()
            mo.url = urls[i]
            moList.add(mo)
        }
        mXHBanner.setXHIndicator(xhIndicator)
        mXHBanner.setAutoPlay(autoPlay)
        mXHBanner.setIntervalTime(2000)
        mXHBanner.setScrollDuration(1000)
        mXHBanner.setBannerData(R.layout.banner_item_layout, moList)
        mXHBanner.setBindAdapter { viewHolder, mo, pos ->
            val imageView = viewHolder.findViewById<ImageView>(R.id.iv_image)
            val titleView = viewHolder.findViewById<TextView>(R.id.tv_title)

            Glide.with(this).load(mo.url).into(imageView)
            titleView.text = mo.url
            Log.d("----position----", pos.toString() + "url: " + mo.url)
        }
    }
}