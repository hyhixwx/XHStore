package com.hyhua.xhstore.demos

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.hyhua.xhstore.R
import com.hyhua.xhstore.demos.data_item.ItemData
import com.hyhua.xhui.item.XHDataItem

class GridDataItem(data: ItemData) : XHDataItem<ItemData, GridDataItem.MyHolder>(data) {


    override fun onBindData(holder: MyHolder, position: Int) {
        holder.imageView!!.setImageResource(R.drawable.fire)
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_list_item_grid
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView? = itemView.findViewById(R.id.item_image)

        init {
            imageView = itemView.findViewById(R.id.item_image)
        }
    }
}