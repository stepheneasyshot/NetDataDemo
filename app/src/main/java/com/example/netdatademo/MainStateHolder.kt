package com.example.netdatademo

import androidx.lifecycle.ViewModel
import com.example.netdatademo.retrofit.PicAdress
import com.example.netdatademo.retrofit.RetroService
import com.example.netdatademo.uistate.ArticleState
import com.example.netdatademo.uistate.PitureState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainStateHolder(private val retroService: RetroService) : ViewModel() {

    private val articleState = MutableStateFlow(ArticleState())
    val articleListStateFlow = articleState.asStateFlow()

    private val pitureState = MutableStateFlow(PitureState(PicAdress()))
    val pitureListStateFlow = pitureState.asStateFlow()

    fun getArticleList(pageIndex: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            articleState.update {
                it.copy(
                    articleList = retroService.getArticleList(pageIndex),
                    isLoading = false
                )
            }
            articleState.value = articleState.value.toUiState()
        }
    }

    fun getCatPic() {
        CoroutineScope(Dispatchers.IO).launch {
            pitureState.update {
                it.copy(
                    picAdress = retroService.getCatPic(),
                    isLoading = false
                )
            }
        }
    }
}