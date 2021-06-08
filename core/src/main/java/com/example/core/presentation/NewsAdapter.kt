package com.example.core.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.core.databinding.ItemNotificationBinding
import com.example.core.domain.model.News
import java.lang.StringBuilder
import java.util.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.MovieViewHolder>() {
    private var listData: ArrayList<News> = ArrayList()

    fun setData(newListData: List<News>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = listData[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = listData.size

    inner class MovieViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: News) {
            with(binding) {
                tvJudul.text = data.title
                val stringDescription = StringBuilder().append(data.description).append("...")
                tvSubjudul.text = stringDescription
            }
            itemView.setOnClickListener { onItemClickCallback?.onItemClicked(data) }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: News)
    }

}