package com.example.newsApp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsApp.BuildConfig
import com.example.newsApp.data.NewsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

@HiltViewModel
class NewsViewModel @Inject constructor(val repo: NewsRepo) : ViewModel() {
    val stateNewsHeadline = MutableStateFlow<State>(State.START)

init {
    getTopNews()
}
    fun getTopNews() {
        viewModelScope.launch {
            stateNewsHeadline.value = State.LOADING
            try {
                val response = withContext(Dispatchers.IO) {
                    repo.getTopHeadlines("us", BuildConfig.APP_KEY)
                }

                stateNewsHeadline.value = State.SUCCESS(response)
            } catch (e: Exception) {
                stateNewsHeadline.value = State.FAILURE(e.localizedMessage ?: "error")
            }

        }
    }


}
sealed class State {
    object START : State()
    object LOADING : State()
    data class SUCCESS(val response: NewsModel) : State()

    data class FAILURE(val message: String) : State()
}