package com.example.newsApp.viewmodel

import com.example.newsApp.data.ApiService
import com.example.newsApp.data.NewsModel
import javax.inject.Inject

class NewsRepo @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getTopHeadlines(country: String, apiKey: String): NewsModel {
        return apiService.getTopHeadlines(country, apiKey)
    }

}