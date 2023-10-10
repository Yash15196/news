package com.example.newsApp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsApp.data.Article
import com.example.newsApp.databinding.AdapterNewsBinding
import javax.inject.Inject


class NewsAdapter @Inject constructor() : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    var news = mutableListOf<Article>()
    private var clickInterface: ClickInterface<Article>? = null

    fun updateNews(news: List<Article>) {
        this.news = news.toMutableList()
        notifyItemRangeInserted(0, news.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding =
            AdapterNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsModel = news[position]
        holder.view.tvTitle.text = newsModel.title

        Glide
            .with(holder.view.imgNews.context)
            .load(newsModel.urlToImage)
            .centerCrop()
            .into(holder.view.imgNews)
        holder.view.card.setOnClickListener {
            clickInterface?.onClick(newsModel)
        }
    }

    override fun getItemCount(): Int {
        return news.size
    }

    fun setItemClick(clickInterface: ClickInterface<Article>) {
        this.clickInterface = clickInterface
    }

    class NewsViewHolder(val view: AdapterNewsBinding) : RecyclerView.ViewHolder(view.root)
}

interface ClickInterface<T> {
    fun onClick(data: T)
}