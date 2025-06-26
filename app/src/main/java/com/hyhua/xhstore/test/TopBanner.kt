package com.hyhua.xhstore.test

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.hyhua.xhstore.R
import com.hyhua.xhui.item.XHDataItem

class TopBanner(data: ItemData) : XHDataItem<ItemData, RecyclerView.ViewHolder>(data) {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.item_image)
        imageView.setImageResource(R.drawable.fire)
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_list_item_banner
    }
}