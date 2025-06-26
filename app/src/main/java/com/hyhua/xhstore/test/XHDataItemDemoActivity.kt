package com.hyhua.xhstore.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hyhua.xhstore.R
import com.hyhua.xhui.item.XHAdapter
import com.hyhua.xhui.item.XHDataItem

class XHDataItemDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xhdata_item_demo)

        var xhAdapter = XHAdapter(this)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = xhAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        var dataSets = ArrayList<XHDataItem<*, *>>()
        dataSets.add(TopTabDataItem(ItemData()))
        dataSets.add(TopBanner(ItemData()))
        dataSets.add(GridDataItem(ItemData()))

        xhAdapter.addItems(dataSets, false)
    }
}