package com.example.netdatademo

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.netdatademo.retrofit.PicAdress
import com.example.netdatademo.retrofit.RetroService
import com.example.netdatademo.uistate.ArticleState
import com.example.netdatademo.uistate.PitureState
import com.example.netdatademo.uistate.UserState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainStateHolder(private val retroService: RetroService) : ViewModel() {

    companion object {
        const val TAG = "MainStateHolder"
    }

    private val articleState = MutableStateFlow(ArticleState())
    val articleListStateFlow = articleState.asStateFlow()

    private val userState = MutableStateFlow(UserState())
    val userStateFlow = userState.asStateFlow()

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

    fun loginWanAndroid() {
        CoroutineScope(Dispatchers.IO).launch {
            val userName = retroService.loginWanAndroid("zhanfeng", "abcd1234@")
            Log.d(TAG, "loginWanAndroid: $userName")
            userState.update {
                it.copy(
                    userName = userName,
                    isLoading = false
                )
            }
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