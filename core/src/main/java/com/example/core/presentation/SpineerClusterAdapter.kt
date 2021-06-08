package com.example.core.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.core.R
import com.example.core.domain.model.Cluster

class SpineerClusterAdapter (private val context: Context, data: List<Cluster>) : BaseAdapter() {
    private var listData : List<Cluster> = data

    override fun getCount(): Int {
        return listData.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rootView : View = LayoutInflater.from(context).inflate(R.layout.item_spinner, parent, false)

        val tv = rootView.findViewById<TextView>(R.id.tv_item)
        val str = StringBuilder().append("Latitude = ").append(listData[position].longitude).append(", Longitude = ").append(listData[position].longitude).toString()
        tv.text = str

        return rootView
    }
}